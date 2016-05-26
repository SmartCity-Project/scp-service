/**
 * 
 */
package com.smartcity.business.rest;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;

/**
 * @author gperreas
 *
 */
public interface IVotingRestController 
{
	Map<String, Object> vote(String documentType, Vote vote);
	
	Map<String, Object> getUserVote(String documentType, String voterId, String documentId);

	Map<String, Object> getAggregatedVotes(String documentType, String documentId);

	Map<String, Object> getAggregatedVotesList(String documentType);
}
