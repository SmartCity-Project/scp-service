/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.comments.AggregatedComments;
import com.smartcity.data.comments.Comment;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;

/**
 * @author gperreas
 *
 */
public class AggregatedCommentsRepoImpl 
	extends BaseRepositoryImpl<AggregatedComments>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<AggregatedComments> getModelType() {
		return AggregatedComments.class;
	}
	
	public AggregatedComments findAggregatedCommentsByBaseDocumentDetails(
			BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", baseDocumentDetails.getDocumentId());
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findOneByMapKeyValue(keyValueMap);
	}

	public List<AggregatedComments> findAggregatedCommentsListByBaseDocumentDetails(
			BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findListByMapKeyValue(keyValueMap);
	}
	
	public AggregatedComments updateAggregatedComments(Comment comment, boolean hasCommentDeleted) {
		
		Query query = new Query();
		
		Update update = new Update();
		update.inc("totalComments", getIncreasedNumber(hasCommentDeleted));

		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
	
		AggregatedComments ac = this.mt.findAndModify(
				query.addCriteria(
						where("documentId").is(comment.getDocumentId())
						.andOperator(where("classRef").is(comment.getClassRef()))), 
				update, 
				findAndModifyOptions,
				getModelType());
		
		return ac;
	}
	
	private Number getIncreasedNumber(boolean hasCommentDeleted) {
		if(!hasCommentDeleted) {
			return +1;
		}
		else {
			return -1;
		}	
	}

}
