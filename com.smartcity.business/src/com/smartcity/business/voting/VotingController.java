/**
 * 
 */
package com.smartcity.business.voting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.repositories.impl.AggregatedVotesRepoImpl;
import com.smartcity.business.repositories.impl.VoteRepoImpl;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;
import com.smartcity.data.voting.VoteTypeEnum;
import com.smartcity.exceptions.VoteDediedException;

/**
 * @author gperreas
 *
 */
@Component
public class VotingController
	implements IVotingController
{
	@Autowired
	private VoteRepoImpl voteRepoImpl;
	
	@Autowired 
	private AggregatedVotesRepoImpl aggregatedVotesRepoImpl;
	
	public VotingController() {
		
	}
	
	@Override
	public AggregatedVotes addVote(Vote v) {	
		assertVote(v);
		//Only ROLE_USER can vote
		SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		boolean hasChangedVoteType = false;
		Vote existVote = voteRepoImpl.findExistVote(v);
		AggregatedVotes aggregateVotes = null;
		
		if(existVote!=null) {
			hasChangedVoteType = hasChangedVoteType(existVote.getType(), v.getType());
			
			if(existVote.isCanceled()) {
				existVote.setCanceled(false);
				existVote.setType(v.getType());
				voteRepoImpl.updateVote(existVote);
				aggregateVotes = calculateVotes(existVote, hasChangedVoteType);
			}
			else if(hasChangedVoteType) {
				existVote.setType(v.getType());
				voteRepoImpl.updateVote(existVote);
				aggregateVotes = calculateVotes(existVote, hasChangedVoteType);
			}
			else {
				aggregateVotes = cancelVote(existVote);
//				throw new VoteDediedException(
//						"[voting] Voter with id: " + existVote.getVoterId() + " has already voted " + existVote.getType());
			}
		}
		else {
			v.setCanceled(false);
			voteRepoImpl.save(v);
			aggregateVotes = calculateVotes(v, hasChangedVoteType);
		}
		//Only ROLE_USER can vote
		//find and return refreshed data
		return aggregateVotes;	
	}

	private AggregatedVotes cancelVote(Vote v) {
		assertVote(v);
		//find and return refreshed data
		
		//Only ROLE_USER can vote
//		SecurityContextHolder.getContext().getAuthentication().getAuthorities();
//		
//		Vote existVote = voteRepoImpl.findExistVote(v);
//		boolean hasChangedVoteType = false;
//		
//		if(existVote==null) {
//			throw new VoteDediedException("[cancel vote] you cannot cancel your vote because you haven't voted yet");
//		} else {
//			hasChangedVoteType = hasChangedVoteType(existVote.getType(), v.getType());
//		}
//		
//		if(hasChangedVoteType) {
//			throw new VoteDediedException("[cancel vote] you cannot cancel your vote with different type");
//		}
		
		v.setCanceled(true);
		
		AggregatedVotes aggregateVotes = calculateVotes(v, false);
		voteRepoImpl.updateVoteCancel(v);
		
		return aggregateVotes;
	}
	
	@Override
	public void initAggregatedVotableDocument(AggregatedVotes aggregateVotes) {
		this.aggregatedVotesRepoImpl.save(aggregateVotes);
	}

	@Override
	public Vote getUserVote(String voterId, BaseDocumentDetails baseDocumentDetails) {
		
		Vote v = new Vote();
		v.setVoterId(voterId);
		v.setDocumentId(baseDocumentDetails.getDocumentId());
		v.setClassRef(baseDocumentDetails.getClassRef());
		return this.voteRepoImpl.findExistVote(v);
	}

	@Override
	public AggregatedVotes getAggregatedVotesBy(BaseDocumentDetails baseDocumentDetails) {
		return this.aggregatedVotesRepoImpl.findAggregatedVoteByBaseDocumentDetails(baseDocumentDetails);
	}
	
	@Override
	public List<AggregatedVotes> getAggregatedVotesList(BaseDocumentDetails baseDocumentDetails) {
		return this.aggregatedVotesRepoImpl.findAggregatedVoteListByBaseDocumentDetails(baseDocumentDetails);
	}

	
	private AggregatedVotes calculateVotes(Vote v, boolean hasVoteTypeChanged) {
		return this.aggregatedVotesRepoImpl.updateAggregatedVotes(v, hasVoteTypeChanged);
	}

	private void assertVote(Vote v) {
		Assert.notNull(v,"Vote must not be null");
		Assert.notNull(v.getDocumentId(),"Document id must not be null");
		Assert.notNull(v.getClassRef(),"Reference class must not be null");
		Assert.notNull(v.getVoterId(),"Voter id must not be null");
		Assert.notNull(v.getType(),"Vote type must not be null");
	}
	
	private boolean hasChangedVoteType(VoteTypeEnum exist, VoteTypeEnum now) {
		return !(exist.compareTo(now) == 0);
	}
}
