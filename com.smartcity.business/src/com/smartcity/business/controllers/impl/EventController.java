/**
 * 
 */
package com.smartcity.business.controllers.impl;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.filter.EventSearchCriteria;
import com.smartcity.business.filter.SearchResult;
import com.smartcity.business.repositories.impl.AggregatedVotesRepoImpl;
import com.smartcity.business.repositories.impl.EventRepoImpl;
import com.smartcity.business.repositories.impl.LocationRepoImpl;
import com.smartcity.business.repositories.impl.VoteRepoImpl;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.EventState;
import com.smartcity.data.geo.Location;

/**
 * @author gperreas
 *
 */
@Component
public class EventController 
{
	@Autowired
	private EventRepoImpl eventRepoImpl;
	
	@Autowired 
	private AggregatedVotesRepoImpl aggregatedVotesRepoImpl;
	
	@Autowired
	private VoteRepoImpl voteRepoImpl;
	
	@Autowired
	private LocationRepoImpl locationRepoImpl;
	
	public EventController() {
		
	}
	
	public Event saveEvent(Event event) {
		Assert.notNull(event);
		return this.eventRepoImpl.save(event);
	}
	
	public Event updateEvent(Event event) {
		Assert.notNull(event);
		Assert.notNull(event.getId());
		return this.eventRepoImpl.update(event);
	}
	
	public boolean updateEventState(Long eventId, EventState eventState) {
		return this.updateEventState(eventId, eventState);
	}
	
	public Event findEventById(Long id) {
		Assert.notNull(id);
		return this.eventRepoImpl.findOneByObjectId(id);
	}

	public SearchResult<Event> find(EventSearchCriteria criteria) {
		
//		Query query = new Query();
//		new Distance(criteria.getRadius(), Metrics.KILOMETERS)
		Set<Criteria> criteriaSet = new HashSet<Criteria>();
		
		if(criteria.getAuthorId()!=null) {
			if(!criteria.getAuthorId().isEmpty()) {
				criteriaSet.add(Criteria.where("tracking.createdBy").is(criteria.getAuthorId()));
			}
		}
		
		if(criteria.getOrganizationId()!=null) {
			if(!criteria.getOrganizationId().isEmpty()) {
				criteriaSet.add(Criteria.where("organizationId").is(criteria.getOrganizationId()));
			}
		}
		
		if(criteria.getLocation()!=null) {
			criteriaSet.add(Criteria.where("location")
					.near(criteria.getLocation())
					.maxDistance(criteria.getRadius()));
		}
		
		if(criteria.getTitle()!=null) {
			if(!criteria.getTitle().isEmpty()) {
				criteriaSet.add(Criteria.where("title").regex(criteria.getTitle()));
			}
		}
		
		if(criteria.getEventState()!=null) {
			criteriaSet.add(Criteria.where("eventState").is(criteria.getEventState()));
		}
		
		if(criteria.getTags()!=null) {
			if(!criteria.getTags().isEmpty()) {
				criteriaSet.add(Criteria.where("tags").in(criteria.getTags()));
			}
		}
		
		if(criteria.getLocations()!=null) {
			if(!criteria.getLocations().isEmpty()) {
				Set<Criteria> locationSet = new HashSet<Criteria>();

				for(Location l:criteria.getLocations()) {
					locationSet.add(Criteria.where("locationId").is(l.getId()));
				}
				criteriaSet.addAll(locationSet);
			}
		}

		Sort sort = new Sort(Direction.DESC,"tracking.created");
		
		return eventRepoImpl.findPageByMapKeyValue(criteriaSet, sort, criteria);
	}
	
	public long countEventParticipations(String orgId) {	
		return eventRepoImpl.countEventParticipations(orgId);
	}

	public long countEvents(String userId) {
		return eventRepoImpl.countEvents(userId);
	}
	
	public long countEvents() {
		return eventRepoImpl.countEvents();
	}
}
