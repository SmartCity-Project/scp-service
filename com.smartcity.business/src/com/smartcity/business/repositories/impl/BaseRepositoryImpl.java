package com.smartcity.business.repositories.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.mongodb.WriteResult;
import com.smartcity.business.controllers.impl.CommentController;
import com.smartcity.business.controllers.impl.ReportDocumentController;
import com.smartcity.business.filter.SearchCriteria;
import com.smartcity.business.filter.SearchResult;
import com.smartcity.business.repositories.IRepository;
import com.smartcity.business.tracking.TrackingController;
import com.smartcity.business.voting.VotingController;
import com.smartcity.data.ISequenceId;
import com.smartcity.data.SequenceId;
import com.smartcity.data.comments.AggregatedComments;
import com.smartcity.data.comments.IComment;
import com.smartcity.data.reporting.AggregatedReports;
import com.smartcity.data.reporting.IReportDocument;
import com.smartcity.data.tracking.ITracking;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.IVotable;
import com.smartcity.exceptions.SequenceException;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * @author gperreas
 *
 */
public abstract class BaseRepositoryImpl<T>
	extends AbstractMongoEventListener<T>
	implements IRepository<T>
{
	
	private static final long serialVersionUID = 1L;

	protected Logger log = LoggerFactory.getLogger(getClass());
	
	protected static final String DEFAULT_MONGO_KEY_ID = "_id";
	protected static final String DEFAULT_MONGO_KEY_CLASS = "_class";
	protected static final String DEFAULT_REF_MONGO_KEY_ID = "$id";

	protected MongoTemplate mt;
	
	@Autowired
	protected TrackingController trackingController;
	
	@Autowired
	protected VotingController votingController;
	
	@Autowired
	protected CommentController commentController;
	
//	@Autowired 
//	protected ReportDocumentController reportDocumentController;
		
	@Override
	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mt = mongoTemplate;
	}
	
	protected abstract Class<T> getModelType();

	protected long count(Query query) {
		return this.mt.count(query, getModelType());
	}
	
	@Override
	public List<T> findAll() {
		return this.mt.findAll(getModelType());
	}

	@Override
	public T findOneByObjectId(Object id) {
		return this.mt.findOne(
				new Query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(id)), getModelType());
	}

	@Override
	public T findOneByKeyValue(String key, Object value) {
		return this.mt.findOne(
				new Query(Criteria.where(key).is(value)), getModelType());
	}
	
	/**
	 * Using andOperator
	 */
	@Override
	public T findOneByMapKeyValue(Map<String, Object> keyValueMap) {
		
		Query query = this.createQueryWithAndOperatorByMapKeyValue(keyValueMap);
		
		return this.mt.findOne(query, getModelType());
	}
	

	@Override
	public T findOneByMapKeyValue(Criteria criteria) {
		Assert.notNull(criteria);
		Query query = new Query(criteria);
		return this.mt.findOne(query, getModelType());
	}
	
	@Override
	public List<T> findSortedListByMapKeyValue(Map<String, Object> keyValueMap, Sort sort) {
		Query query = this.createQueryWithAndOperatorByMapKeyValue(keyValueMap);

		return this.mt.find(query.with(sort), getModelType());
	}
	
	@Override
	public List<T> findSortedListByKeyValue(String key, Object value, Sort sort) {
		return this.mt.find(
				new Query(Criteria.where(key).is(value)).with(sort), getModelType());
	}
	
	@Override
	public List<T> findListByKeyValue(String key, Object value) {
		return this.mt.find(
				new Query(Criteria.where(key).is(value)), getModelType());
	}
	
	/**
	 * Using andOperator
	 */
	@Override
	public List<T> findListByMapKeyValue(Map<String, Object> keyValueMap) {
		
		Query query = this.createQueryWithAndOperatorByMapKeyValue(keyValueMap);
		
		return this.mt.find(query, getModelType());
	}


	@Override
	public List<T> findListByMapKeyValue(Criteria criteria) {
		Assert.notNull(criteria);
		Query query = new Query(criteria);
		return this.mt.find(query, getModelType());
	}

	@Override
	public SearchResult<T> findPageByMapKeyValue(Set<Criteria> criteriaSet, 
			Sort sort, SearchCriteria criteria) {
		Assert.notNull(criteria, "criteria must not be null");
		
		Query query = new Query();
		if(!criteriaSet.isEmpty()) {
			query.addCriteria(criteriaSetToAndOperator(criteriaSet));
		}
		//query.skip((criteria.getPageIndex()-1) * criteria.getPageSize());
		query.skip((criteria.getPageIndex()) * criteria.getPageSize());
		query.limit(criteria.getPageSize());
		
		if(sort!=null) {
			query.with(sort);
		}
		
		List<T> list = this.mt.find(query, getModelType());
		
		SearchResult<T> results = new SearchResult<T>();
		results.setList(list);
		results.setPageIndex(criteria.getPageIndex()+1);
		results.setPageSize(list.size());
		
		return results;
	}

	@Override
	public T save(T object) {
		
		if(object instanceof ITracking) {
			((ITracking)object).setTracking(
					this.trackingController.tracking(((ITracking)object).getTracking()));
		}
		
		if(object instanceof ISequenceId) {
			((ISequenceId)object).setId(
					getNextSequenceId(object.getClass().getCanonicalName()));
		}
		
		if(object instanceof IVotable) {
			AggregatedVotes aggregateVotes = new AggregatedVotes();
			aggregateVotes.setClassRef(object.getClass().getCanonicalName());
			aggregateVotes.setDocumentId(String.valueOf(((IVotable) object).getId()));
			votingController.initAggregatedVotableDocument(aggregateVotes);
		}
		
		if(object instanceof IComment) {
			AggregatedComments aggregateComments = new AggregatedComments();
			aggregateComments.setClassRef(object.getClass().getCanonicalName());
			aggregateComments.setDocumentId(String.valueOf(((IComment) object).getId()));
			commentController.initAggregatedCommentDocument(aggregateComments);
		}
		
//		if(object instanceof IReportDocument) {
//			AggregatedReports aggregateReports = new AggregatedReports();
//			aggregateReports.setClassRef(object.getClass().getCanonicalName());
//			aggregateReports.setDocumentId(String.valueOf(((IReportDocument) object).getId()));
//			
//			reportDocumentController.initAggregatedReports(aggregateReports);
//		}
				
		this.mt.save(object);
		
		return object;
	}

	@Override
	public T update(T object) {

		if(object instanceof ITracking) {
			((ITracking)object).setTracking(
					this.trackingController.tracking(((ITracking)object).getTracking()));
		}
			
		this.mt.save(object);	
		
		return object;
	}
	
	protected T findAndModify(Query query, Update update) {
		
		if(getModelType().isAssignableFrom(ITracking.class)) {
			Map<String, Object> modifiedMap = this.trackingController.getModifiedMap();
			Set<String> keys = modifiedMap.keySet();
			for(String key:keys) {
				update.set(key, modifiedMap.get(key));
			}
		}
		
		return this.mt.findAndModify(query, update, getModelType());
	}

	protected boolean updateFirst(Query query, Update update) {
		
		if(getModelType().isAssignableFrom(ITracking.class)) {
			Map<String, Object> modifiedMap = this.trackingController.getModifiedMap();
			Set<String> keys = modifiedMap.keySet();
			for(String key:keys) {
				update.set(key, modifiedMap.get(key));
			}
		}
		
		return this.mt.updateFirst(query, update, getModelType()).getN() == 1;
	}
	
	@Override
	public boolean delete(Object id) {
		WriteResult wr = this.mt.remove(new Query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(id)), getModelType());
		return wr.getN() == 1;
	}
	
	@Override
	public boolean deleteByKeyValue(String key, Object value) {
		WriteResult wr = this.mt.remove(new Query(Criteria.where(key).is(value)), getModelType());
		return wr.getN() == 1;
	}
	

	@Override
	public long getNextSequenceId(String key) throws SequenceException {

		//get sequence id
		Query query = new Query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(key));

		//increase sequence id by 1
		Update update = new Update();
		update.inc("seq", 1);

		//return new increased id
		FindAndModifyOptions options = new FindAndModifyOptions();
		options.returnNew(true);

		//this is the magic happened.
		SequenceId seqId = 
	          this.mt.findAndModify(query, update, options, SequenceId.class);

		//if no id, throws SequenceException
	        //optional, just a way to tell user when the sequence id is failed to generate.
		if(seqId == null) {
			throw new SequenceException("Unable to get sequence id for key : " + key);
		}

		return seqId.getSeq();
	}
	
	private Query createQueryWithAndOperatorByMapKeyValue(
			Map<String, Object> keyValueMap) {
		Assert.notNull(keyValueMap, "keyValueMap must not be null");
		
		Query query = new Query();
		Set<Criteria> criteriaSet = new HashSet<Criteria>();
		
		Set<String> keys = keyValueMap.keySet();
		
		for(String key:keys) {
			criteriaSet.add(where(key).is(keyValueMap.get(key)));
		}
		
		query.addCriteria(criteriaSetToAndOperator(criteriaSet));
		
		return query;
	}
	
	private Criteria criteriaSetToAndOperator(Set<Criteria> criteriaSet) {
		return new Criteria().andOperator(
				criteriaSet.toArray(new Criteria[criteriaSet.size()]));
	}

	@Override
	public void createCollection() {
		if(!mt.collectionExists(getModelType())) {
			mt.createCollection(getModelType());
		}
	}

	@Override
	public void dropCollection() {
		if(mt.collectionExists(getModelType())) {
			mt.dropCollection(getModelType());
		}
	}
}
