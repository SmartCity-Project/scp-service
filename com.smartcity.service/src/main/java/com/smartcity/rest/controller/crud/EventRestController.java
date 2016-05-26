/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.smartcity.business.controllers.impl.CommentController;
import com.smartcity.business.controllers.impl.EventController;
import com.smartcity.business.controllers.impl.OrganizationController;
import com.smartcity.business.filter.EventSearchCriteria;
import com.smartcity.business.filter.SearchResult;
import com.smartcity.business.repositories.impl.EventRepoImpl;
import com.smartcity.business.repositories.impl.LocationRepoImpl;
import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.business.rest.IBaseCrudRestController;
import com.smartcity.business.tagging.TagController;
import com.smartcity.business.voting.VotingController;
import com.smartcity.data.BaseDocumentDetails;
import com.smartcity.data.access.User;
import com.smartcity.data.common.Author;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.EventState;
import com.smartcity.data.geo.Location;
import com.smartcity.data.tagging.Tag;
import com.smartcity.exceptions.AccessDeniedException;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping(value="/api/crud/events")
public class EventRestController
	extends BaseRestController
	implements IBaseCrudRestController<Event, Long>
{
	private Logger log = LoggerFactory.getLogger(EventRestController.class);
	
	@Autowired
	private UserRepoImpl userRepoImpl;
	
	@Autowired
	private EventRepoImpl eventRepoImpl;
	
	@Autowired
	private LocationRepoImpl locationRepoImpl;
	
	@Autowired
	private EventController eventController;
	
	@Autowired
	private VotingController votingController;
	
	@Autowired
	private CommentController commentController;
	
	@Autowired
	private TagController tagController;
	
	@Autowired
	private OrganizationController organizationController;
	
	@RequestMapping(value="/participate", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> participate(@RequestParam String orgId, @RequestParam Long eventId) {
		
		try {
			getResponseMap().clear();
			
			Event e = eventRepoImpl.findOneByObjectId(eventId);
			if(e!=null) {
				if(e.getOrganizationId()!=null&&!e.getOrganizationId().equals(orgId)) {
					getResponseMap().put("error", "event is managed from other organization");
				} else {
					organizationController.isUserExistOnOrganization(getCurrentUser().getId(), orgId);
					//check favorite locations and tags before submit participations
					
					if(e.getOrganizationId()!=null) {
						if(e.getOrganizationId().equals(orgId)) {
							e.setOrganizationId(null);
							
							eventRepoImpl.update(e);
							
							getResponseMap().putAll(getEventRestView(e));
						}
					} else if(organizationController.canOrganizationParticipateEvent(orgId, e.getLocationId())) {
						e.setOrganizationId(orgId);
						eventRepoImpl.update(e);
	
						getResponseMap().putAll(getEventRestView(e));

					} else {
						getResponseMap().put("error", "you cannot participate on this event,"
								+ " change your favorite locations");
					}
				}
			} else {
				getResponseMap().put("error", "event not found");
			}
			
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/edit", method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> editEventState(@RequestParam Long eventId, 
			@RequestParam EventState eventState) {
		
		try {
			if(getCurrentUser().getOrganization()==null) {
				throw new AccessDeniedException("You are not belong to organization");
			} else {
				String userOrgId = getCurrentUser().getOrganization().getId();
				Event e = eventRepoImpl.findOneByObjectId(eventId);
				if(e!=null) {
					if(e.getOrganizationId()==null) {
						getResponseMap().put("error", "event is not managed");
					} else if(!userOrgId.equals(e.getOrganizationId())) {
						getResponseMap().put("error", "event is managed from other organization");
					} else {
						boolean result = eventRepoImpl.updateEventState(eventId, eventState);
						getResponseMap().put("data", result);
					}
				} else {
					getResponseMap().put("error", "event not found");
				}
				
			}
			
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/states", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getEventStates() {
		return new Gson().toJson(EventState.values());
	}

	@RequestMapping(value="/find", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> findEventById(@RequestParam Long id) {
		
		try {				
			getResponseMap().clear();
			Event e = eventRepoImpl.findOneByObjectId(id);
			
			getResponseMap().putAll(getEventRestView(e));
		} catch(NullPointerException ex) {
			getResponseMap().put("error", "event not found with id: " + id.toString());
			log.debug(ex.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/find", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> find(@RequestBody EventSearchCriteria criteria,
			@RequestParam int pageIndex, @RequestParam int pageSize) {
		
		criteria.setPageIndex(pageIndex);
		criteria.setPageSize(pageSize);
		
		SearchResult<Event> results = eventController.find(criteria);
		Set<Map<String, Object>> viewSet = new LinkedHashSet<Map<String, Object>>();
		
		for(Event e:results.getList()) {
			
			viewSet.add(getEventRestView(e));
		}
		
		getResponseMap().clear();
		getResponseMap().put("pageIndex", results.getPageIndex());
		getResponseMap().put("pageSize", results.getPageSize());
		getResponseMap().put("data", viewSet);
		
		return getResponseMap();
	}

	@Override
	@RequestMapping(method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> save(@RequestBody Event json) {

		getResponseMap().clear();
		try {
			Event event = json;
			event.setEventState(EventState.opened);
			if(event.getLocation()!=null) {
				Location location = locationRepoImpl.findByLocationWithinPolygon(event.getLocation());
				if(location!=null) {
					event.setLocationId(location.getId());
				} else {			
					getResponseMap().put("error", "you have provided wrong location for event");
					return getResponseMap();
				}			
			} else {
				getResponseMap().put("error", "you must providing location for event");
				return getResponseMap();
			}
			
			if(event.getTags()!=null) {
				if(!event.getTags().isEmpty()) {
					for(Tag tag:event.getTags()) {
						tagController.saveTag(tag);
					}
				}
			}
			
			eventRepoImpl.save(event);
			
			getResponseMap().putAll(getEventRestView(event));

		} catch(AccessDeniedException e) {
			getResponseMap().put("error", "access denied, you cannot create event");
		}
		
		return getResponseMap();
	}

	@Override
	public Map<String, Object> edit(@RequestBody Event json) {
		
		return getResponseMap();
	}

	@Override
	public boolean delete(Long documentId) {
		return eventRepoImpl.delete(documentId);
	}
	
	@RequestMapping(value="/total", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> countEvents() {
		getResponseMap().clear();
		getResponseMap().put("total", eventController.countEvents());
		
		return getResponseMap();
	}
	
	
	private Map<String, Object> getEventRestView(Event e) {
		Map<String, Object> view = new HashMap<String, Object>();
		
		User u = userRepoImpl.findOneByObjectId(e.getTracking().getCreatedBy());
		//e.getTracking().setCreatedBy(u.getName()+" "+u.getSurname());
		
		BaseDocumentDetails baseDocumentDetails = new BaseDocumentDetails();
		baseDocumentDetails.setClassRef(Event.class.getCanonicalName());
		baseDocumentDetails.setDocumentId(String.valueOf(e.getId()));
		
		Author author = new Author();
		author.setId(u.getId());
		author.setName(u.getName()+" "+u.getSurname());
		
		view.put("id", e.getId());
		view.put("title", e.getTitle());
		view.put("description", e.getDescription());
		view.put("eventState", e.getEventState());
		view.put("organizationId", e.getOrganizationId());
		view.put("locationId", e.getLocationId());
		view.put("location", e.getLocation());
		view.put("tags", e.getTags());
		view.put("image", e.getImage());
		view.put("author", author);
		view.put("tracking", e.getTracking());
		view.put("aggregatedVotes", votingController.getAggregatedVotesBy(baseDocumentDetails));
		view.put("aggregatedComments", commentController.getAggregatedComments(baseDocumentDetails));
		
		return view;
	}
}
 