/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.mongodb.WriteResult;
import com.smartcity.business.voting.IVoteRepository;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;
import com.smartcity.data.voting.VoteTypeEnum;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

import java.util.Date;

import org.springframework.data.domain.Sort;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.sort;

/**
 * @author gperreas
 *
 */
public class VoteRepoImpl 
	extends BaseRepositoryImpl<Vote>
	implements IVoteRepository
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Vote> getModelType() {
		return Vote.class;
	}

	@Override
	public Vote save(Vote object) {
		Vote v = super.save(object);
		
		return v;
	}

	@Override
	public Vote findExistVote(Vote v) {

		Query query = new Query();

		return this.mt.findOne(
				query.addCriteria(
						where("voterId").is(v.getVoterId())
						.andOperator(
								where("documentId").is(v.getDocumentId())
								.andOperator(where("classRef").is(v.getClassRef())))), 
				getModelType());
	}


	@Override
	public boolean deleteVote(Vote v) {

		Query query = new Query();

		WriteResult wr = 
				this.mt.remove(query.addCriteria(
						where("voterId").is(v.getVoterId())
						.andOperator(
								where("documentId").is(v.getDocumentId())
								.andOperator(where("classRef").is(v.getClassRef())))), 
						getModelType());
		
		return wr.getN() == 1;
	}

	@Override
	public boolean updateVoteCancel(Vote v) {
		
		Query query = new Query();
		
		WriteResult wr = this.mt.updateFirst(
				query.addCriteria(
						where("voterId").is(v.getVoterId())
						.andOperator(
								where("documentId").is(v.getDocumentId())
								.andOperator(where("classRef").is(v.getClassRef())))),
				Update.update("canceled", v.isCanceled()), 
				getModelType());
		
		return wr.getN() == 1;
	}
	
	@Override
	public boolean updateVoteType(Vote v) {
		
		Query query = new Query();
		
		WriteResult wr = this.mt.updateFirst(
				query.addCriteria(
						where("voterId").is(v.getVoterId())
						.andOperator(
								where("documentId").is(v.getDocumentId())
								.andOperator(where("classRef").is(v.getClassRef())))),
				Update.update("type", v.getType()), 
				getModelType());
		
		return wr.getN() == 1;
	}
	
	@Override
	public boolean updateVote(Vote v) {
		
		Query query = new Query();
		Update update = new Update();
		
		update.set("type", v.getType())
			  .set("canceled", v.isCanceled())
			  .set("modified", new Date());
		
		WriteResult wr = this.mt.updateFirst(
				query.addCriteria(
						where("voterId").is(v.getVoterId())
						.andOperator(
								where("documentId").is(v.getDocumentId())
								.andOperator(where("classRef").is(v.getClassRef())))),
				update, 
				getModelType());
		
		return wr.getN() == 1;
	}
	

}
