package com.smartcity.business.voting;

import java.util.List;

import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;

/**
 * @author gperreas
 *
 */
public interface IVotingController {

	AggregatedVotes addVote(Vote v);
		
	void initAggregatedVotableDocument(AggregatedVotes aggregateVotes);
	
	Vote getUserVote(String voterId, BaseDocumentDetails baseVoteDetails);
	
	AggregatedVotes getAggregatedVotesBy(BaseDocumentDetails baseVoteDetails);
	
	List<AggregatedVotes> getAggregatedVotesList(BaseDocumentDetails baseVoteDetails);
}
