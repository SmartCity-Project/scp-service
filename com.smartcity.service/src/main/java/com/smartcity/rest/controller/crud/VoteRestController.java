/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.rest.IVotingRestController;
import com.smartcity.business.voting.VotingController;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.access.User;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.Petition;
import com.smartcity.data.voting.AggregatedVotes;
import com.smartcity.data.voting.Vote;
import com.smartcity.exceptions.VoteDediedException;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/crud/{documentType}/votes")
public class VoteRestController 
	extends GenericTypeRestController
	implements IVotingRestController
{
	private Logger log = LoggerFactory.getLogger(VoteRestController.class);

	@Autowired
	private VotingController votingController;

	@Override
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> vote(@PathVariable(value = "documentType") String documentType, 
			@RequestBody Vote vote) {
		
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {

			String uid = getCurrentUser().getId();
						
			AggregatedVotes av = null;
			vote.setClassRef(documentTypeClass.getCanonicalName());
			vote.setVoterId(uid);
			try {
				av = votingController.addVote(vote);

				getResponseMap().put("vote", vote);
				getResponseMap().put("aggregatedVotes", av);
			} catch(IllegalArgumentException e) {
				getResponseMap().put("error", e.getMessage());
			} catch(VoteDediedException e) {
				//add message to response
				getResponseMap().put("error", e.getMessage());
			}
		}
		
		return getResponseMap();
	}
	
	@Override
	@RequestMapping(value="/getAggregatedVotes", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getAggregatedVotes(@PathVariable(value = "documentType") String documentType, 
			@RequestParam String documentId) {
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		
		if(documentTypeClass!=null) {
			BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
			baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
			baseDocumentDetails.setDocumentId(documentId);
			
			AggregatedVotes av = votingController.getAggregatedVotesBy(baseDocumentDetails);
		}
		
		return getResponseMap();
	}
	
	@Override
	@RequestMapping(value="/getAggregatedVotesList", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getAggregatedVotesList(@PathVariable(value = "documentType") String documentType) {
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {
		
			BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
			baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
			
			List<AggregatedVotes> avList = votingController.getAggregatedVotesList(baseDocumentDetails);
		}

		return getResponseMap();
	}

	@Override
	@RequestMapping(value="/getVote", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getUserVote(@PathVariable(value = "documentType") String documentType, 
			@RequestBody String voterId, @RequestBody String documentId) {
		
		Class documentTypeClass = getClassBasedOnPath(documentType);
		checkClassPath(documentTypeClass, documentType);
		if(documentTypeClass!=null) {
			BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
			baseDocumentDetails.setClassRef(documentTypeClass.getCanonicalName());
			baseDocumentDetails.setDocumentId(documentId);
			
			Vote v = votingController.getUserVote(voterId, baseDocumentDetails);
			AggregatedVotes av = votingController.getAggregatedVotesBy(baseDocumentDetails);
			
			getResponseMap().put("vote", v);
			getResponseMap().put("aggregatedVotes", av);
		}
			
		return getResponseMap();
	}
	
	protected Map<String,Class> initDocumentTypeMap() {
		Map<String, Class> documentTypeMap = new HashMap<String, Class>();

		documentTypeMap.put("events", Event.class);
		documentTypeMap.put("petitions", Petition.class);
		
		return documentTypeMap;
	}
 }
