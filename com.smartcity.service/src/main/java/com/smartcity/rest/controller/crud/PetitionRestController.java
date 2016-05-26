/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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

import com.smartcity.business.controllers.impl.CommentController;
import com.smartcity.business.controllers.impl.OrganizationController;
import com.smartcity.business.controllers.impl.PetitionController;
import com.smartcity.business.filter.PetitionSearchCriteria;
import com.smartcity.business.filter.SearchResult;
import com.smartcity.business.repositories.impl.OrganizationRepoImpl;
import com.smartcity.business.rest.IBaseCrudRestController;
import com.smartcity.business.tagging.TagController;
import com.smartcity.business.voting.VotingController;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.access.Organization;
import com.smartcity.data.access.User;
import com.smartcity.data.common.Author;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.Petition;
import com.smartcity.data.petition.AggregatedSignatures;
import com.smartcity.data.petition.PetitionStatusEnum;
import com.smartcity.data.petition.Signature;
import com.smartcity.data.tagging.Tag;
import com.smartcity.exceptions.AccessDeniedException;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/crud/petitions")
public class PetitionRestController 
	extends BaseRestController
	implements IBaseCrudRestController<Petition, Long>
{
	private Logger log = LoggerFactory.getLogger(PetitionRestController.class);
	
	@Autowired
	private PetitionController petitionController;
	
	@Autowired
	private OrganizationController organizationController;
	
	@Autowired
	private VotingController votingController;
	
	@Autowired
	private CommentController commentController;
	
	@Autowired
	private TagController tagController;
	
	//TODO ADD THIS REST METHOD AS OPEN
	@RequestMapping(value="/sign",
			method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> signToPetition(@RequestBody Signature signature, HttpServletRequest request) {
		getResponseMap().clear();
		
		if(signature.getPetitionId()!=null) { 
			//java.lang.IllegalArgumentException: [Assertion failed] - the object argument must be null
			if(petitionController.findPetitionById(signature.getPetitionId())!=null) {
				String email = ((User) SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal()).getEmail();
				
				signature.setSigned(new Date());
				signature.setEmail(email);
				signature.setIpAddress(request.getRemoteAddr());
				
				AggregatedSignatures aggregatedSignatures = petitionController.signToPetition(signature);
				getResponseMap().put("sign", signature);
				getResponseMap().put("aggregatedSignatures", aggregatedSignatures);
				
//				if(result) {
//					getResponseMap().put("success", result);
//				} else {
//					getResponseMap().put("success", result);
//					getResponseMap().put("error", "you have give us a signature before");
//				}
			}
			else {
				getResponseMap().put("error", "petition not found");
			}
		} else {
		
			getResponseMap().put("error", "petitionId must not be null");
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{petitionId}/signatures", method=RequestMethod.GET, 
			consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getPetitionSignature(@PathVariable Long petitionId) {
		getResponseMap().clear();
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/signatures", method=RequestMethod.GET, 
			consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getMySignature(@RequestParam boolean mySigns) {
		getResponseMap().clear();
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/find", method=RequestMethod.POST, 
			consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> find(@RequestBody PetitionSearchCriteria criteria,
			@RequestParam int pageIndex, @RequestParam int pageSize) {

		criteria.setPageIndex(pageIndex);
		criteria.setPageSize(pageSize);
		
		SearchResult<Petition> results = petitionController.find(criteria);
		Set<Map<String, Object>> viewSet = new LinkedHashSet<Map<String, Object>>();
		
		for(Petition p:results.getList()) {
			viewSet.add(getPetitionRestView(p));
		}
		
		getResponseMap().clear();
		getResponseMap().put("pageIndex", results.getPageIndex());
		getResponseMap().put("pageSize", results.getPageSize());
		getResponseMap().put("data", viewSet);
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/find", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> findPetitionById(@RequestParam Long id) {

		try {	
			getResponseMap().clear();
			
			Petition p = petitionController.findPetitionById(id);
			
			getResponseMap().putAll(getPetitionRestView(p));

		} catch(NullPointerException ex) {
			getResponseMap().put("error", "petition not found with id: " + id.toString());
			log.debug(ex.getMessage());
		}
		
		return getResponseMap();
	}
	
	@Override
	@RequestMapping(method=RequestMethod.POST, 
			consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> save(@RequestBody Petition json) {
		getResponseMap().clear();
		
		Petition p = json;
		
		if(p!=null) {
			if(p.getOrganizationId()!=null) {
				Organization o = organizationController.findOrganizationById(p.getOrganizationId());
				if(o!=null) {
					try {		
						BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
						baseDocumentDetails.setClassRef(Petition.class.getCanonicalName());
						baseDocumentDetails.setDocumentId(String.valueOf(p.getId()));
												
						if(p.getId()==null) {
							organizationController.isUserExistOnOrganization(
									getCurrentUser().getId(), o.getId());

							p.setPetitionStatus(PetitionStatusEnum.started);
							saveTags(p.getTags());
							petitionController.savePetition(p);
						} else {
							Petition exPetition = petitionController.findPetitionById(p.getId());
							
							organizationController.isUserExistOnOrganization(
									getCurrentUser().getId(), exPetition.getOrganizationId());

							saveTags(p.getTags());

							petitionController.updatePetition(p);
						}

						
						getResponseMap().putAll(getPetitionRestView(p));
						
					} catch(AccessDeniedException e) {
						getResponseMap().put("error", e.getMessage());
					}
					
				} else {
					getResponseMap().put("error", "organization with id: " + 
							p.getOrganizationId() + " does not exist");
				}
			} else {
				getResponseMap().put("error", "organizationId must not be null");
			}	
		} else {
			getResponseMap().put("error", 500);
		}
		
		return getResponseMap();
	}
	
	//TODO EXTEND AGGREGATED TAGS TO MANAGE TOTAL INCREAMENT
	private void saveTags(Set<Tag> tags) {
		if(tags!=null) {
			if(!tags.isEmpty()) {
				for(Tag tag:tags) {
					tagController.saveTag(tag);
				}
			}
		}
	}

	@Override
	public Map<String, Object> edit(Petition json) {
		return null;
	}

	@Override
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public boolean delete(@PathVariable(value="id") Long documentId) {
		try {
			if(documentId!=null) {		
				
				Petition p = petitionController.findPetitionById(documentId);

				if(p!=null) {
					organizationController.isUserExistOnOrganization(
							getCurrentUser().getId(), p.getOrganizationId());
					return petitionController.deletePetition(documentId);
				}
			}
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return false;
	}
	

	@RequestMapping(value="/total", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> countPetitions() {
		getResponseMap().clear();
		getResponseMap().put("total", petitionController.countPetitions());

		return getResponseMap();
	}
	
	private Map<String, Object> getPetitionRestView(Petition p) {
		Map<String, Object> view = new HashMap<String, Object>();
		
		//User u = userRepoImpl.findOneByObjectId(e.getTracking().getCreatedBy());
			
		BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
		baseDocumentDetails.setClassRef(Petition.class.getCanonicalName());
		baseDocumentDetails.setDocumentId(String.valueOf(p.getId()));
		
		view.put("id", p.getId());
		view.put("title", p.getTitle());
		view.put("description", p.getDescription());
		view.put("petitionStatus", p.getPetitionStatus());
		view.put("goal", p.getGoal());
		view.put("tags", p.getTags());
		view.put("image", p.getImage());
		view.put("endBy", p.getEndBy());
		view.put("organizationId", p.getOrganizationId());
		view.put("organizationName", organizationController.getOrganizationName(p.getOrganizationId()));
		view.put("tracking", p.getTracking());
		view.put("aggregatedSignatures", petitionController.getAggregatedSignaturesByPetitionId(p.getId()));
		view.put("aggregatedVotes", votingController.getAggregatedVotesBy(baseDocumentDetails));
		view.put("aggregatedComments", commentController.getAggregatedComments(baseDocumentDetails));
		
		return view;
	}
	
}
