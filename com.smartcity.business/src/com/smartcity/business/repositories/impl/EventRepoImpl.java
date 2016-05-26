package com.smartcity.business.repositories.impl;

import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.mongodb.WriteResult;
import com.smartcity.data.common.Event;
import com.smartcity.data.common.EventState;

/**
 * @author gperreas
 *
 */
public class EventRepoImpl
	extends BaseRbacRepositoryImpl<Event> 
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Event> getModelType() {
		return Event.class;
	}
	
	public boolean updateEventState(Long eventId, EventState eventState) {
		Assert.notNull(eventId);
		
		Query query = new Query();
		query.addCriteria(Criteria.where(DEFAULT_MONGO_KEY_ID).is(eventId));
		
		return super.updateFirst(query, Update.update("eventState", eventState)); 
	}
	
	public boolean updateOrganizationId(Long eventId, String organizationId) {
		Assert.notNull(eventId);
		Assert.notNull(organizationId);
		
		Query query = new Query();
		query.addCriteria(Criteria.where(DEFAULT_MONGO_KEY_ID).is(eventId));
		
		WriteResult wr = this.mt.updateFirst(query, 
				Update.update("organizationId", organizationId), 
				getModelType());
		
		return wr.getN()==1;
	}

	public long countEventParticipations(String orgId) {
		Assert.notNull(orgId);
		return super.count(new Query(Criteria.where("organizationId").is(orgId)));
	}

	public long countEvents(String userId) {
		Assert.notNull(userId);
		return super.count(new Query(Criteria.where("tracking.createdBy").is(userId)));
	}
	
	public long countEvents() {
		return super.count(new Query());
	}

}
