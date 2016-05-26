/**
 * 
 */
package com.smartcity.business.repositories.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.FindAndModifyOptions;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author gperreas
 *
 */
public class AggregatedVotesRepoImpl 
	extends BaseRepositoryImpl<AggregatedVotes>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<AggregatedVotes> getModelType() {
		return AggregatedVotes.class;
	}
	
	public AggregatedVotes findAggregatedVoteByBaseDocumentDetails(BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("documentId", baseDocumentDetails.getDocumentId());
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findOneByMapKeyValue(keyValueMap);
	}

	public List<AggregatedVotes> findAggregatedVoteListByBaseDocumentDetails(
			BaseDocumentDetails baseDocumentDetails) {

		Map<String, Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("classRef", baseDocumentDetails.getClassRef());
		
		return super.findListByMapKeyValue(keyValueMap);
	}
	
	public AggregatedVotes updateAggregatedVotes(Vote vote, boolean hasVoteTypeChanged) {
			
		Query query = new Query();
		
		Update update = getUpdateValueByVote(vote, hasVoteTypeChanged);
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
	
		AggregatedVotes av = this.mt.findAndModify(
				query.addCriteria(
						where("documentId").is(vote.getDocumentId())
						.andOperator(where("classRef").is(vote.getClassRef()))), 
				update, 
				findAndModifyOptions,
				getModelType());
		
		return av;
	}

	//TODO Add cancel scenario
	private Update getUpdateValueByVote(Vote vote, boolean hasVoteTypeChanged) {
		
		Update update = new Update();
		
		AggregatedVotes av = this.findAggregatedVoteByBaseDocumentDetails(vote);
		
		switch (vote.getType()) {
			case positive:
				update.inc("totalPositives", getIncreasedNumberByCanceled(vote.isCanceled()));
				
				if(hasVoteTypeChanged) {
					if(av.getTotalNegatives()>0) {
						update.inc("totalNegatives", -1);
					}
				}
				
				break;
	
			case negative:
				update.inc("totalNegatives", getIncreasedNumberByCanceled(vote.isCanceled()));
				
				if(hasVoteTypeChanged) {
					if(av.getTotalPositives()>0) {
						update.inc("totalPositives", -1);
					}
				}
				
				break;
		}
		
		return update;
	}
	
	private Number getIncreasedNumberByCanceled(boolean canceled) {
		if(!canceled) {
			return +1;
		}
		else {
			return -1;
		}	
	}
	 
}
