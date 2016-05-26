/**
 * 
 */
package com.smartcity.business.voting;

import com.smartcity.data.voting.Vote;
import com.smartcity.data.voting.VoteTypeEnum;

/**
 * @author gperreas
 *
 */
public interface IVoteRepository 
{
	Vote findExistVote(Vote v);
	
	boolean deleteVote(Vote v);
	
	boolean updateVoteCancel(Vote v);

	boolean updateVoteType(Vote v);

	boolean updateVote(Vote v);
}
