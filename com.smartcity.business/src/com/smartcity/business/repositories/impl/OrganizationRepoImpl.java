/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

import com.smartcity.data.access.Organization;
import com.smartcity.data.geo.Location;
import com.smartcity.data.tagging.Tag;

/**
 * @author gperreas
 *
 */
public class OrganizationRepoImpl 
	extends BaseRepositoryImpl<Organization>
{
	private static final long serialVersionUID = 1L;

	@Override
	protected Class<Organization> getModelType() {
		return Organization.class;
	}
	
	public Organization updateOrganizationBaseInformation(
			Organization organization) {
		Assert.notNull(organization);
		Assert.notNull(organization.getId());
		
		Query query = new Query();
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
		
		Update update = new Update();
		update.set("name", organization.getName());
		update.set("description", organization.getDescription());
		update.set("address", organization.getAddress());
		update.set("url", organization.getUrl());
	
		Organization org = this.mt.findAndModify(
				query.addCriteria(where(DEFAULT_MONGO_KEY_ID).is(organization.getId())),
				update,
				findAndModifyOptions,
				getModelType());
		
		return org;
	}
	
	public Organization findByUserId(String userId) {
		return this.mt.findOne(
				new Query(
						Criteria.where("users").elemMatch(
								Criteria.where("user.id").is(userId))), 
				getModelType());
	}

	public boolean isUserExistOnOrganization(String userId, String orgId) {
		Assert.notNull(userId);
		Assert.notNull(orgId);
		return this.mt.exists(
				new Query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId)
								.andOperator(Criteria.where("users").elemMatch(
								Criteria.where(DEFAULT_REF_MONGO_KEY_ID).is(userId)))), 
				getModelType());
	}
	
	public String getOrganizationName(String orgId) {
		Assert.notNull(orgId);
		Query query = new Query();
		
		query.fields().include("id");
		query.fields().include("name");
		query.addCriteria(Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId));
		
		return this.mt.findOne(query, getModelType()).getName();
	}
	
	public Organization addTag(String orgId, Tag tag) {
		Assert.notNull(tag);
		Assert.notNull(orgId);
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);

		Query query = new Query();
		Update update = new Update().addToSet("favoriteTags", tag);

		return this.mt.findAndModify(
				query.addCriteria(Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId)), 
				update, 
				findAndModifyOptions, 
				getModelType());
	}
	
	public Organization removeTag(String orgId, Tag tag) {
		Assert.notNull(tag);
		Assert.notNull(orgId);
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
		
		Query query = new Query();
		Update update = new Update().pull("favoriteTags", 
				Query.query(Criteria.where(DEFAULT_REF_MONGO_KEY_ID).is(tag.getName())));
		
		return this.mt.findAndModify(
				query.addCriteria(
						Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId)), 
				update, 
				findAndModifyOptions, 
				getModelType());
	}
	
	public Organization addLocation(String orgId, Location location) {
		Assert.notNull(location.getId());
		Assert.notNull(orgId);
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
		
		Query query = new Query();
		Update update = new Update().addToSet("favoriteLocations", location);
		
		return this.mt.findAndModify(
				query.addCriteria(Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId)), 
				update, 
				findAndModifyOptions, 
				getModelType());
	}
	
	public Organization removeLocation(String orgId, Location location) {
		Assert.notNull(location.getId());
		Assert.notNull(orgId);
		
		FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
		findAndModifyOptions.returnNew(true);
		
		Query query = new Query();
		Update update = new Update().pull("favoriteLocations", Query.query(Criteria.where(DEFAULT_REF_MONGO_KEY_ID).is(location.getId())));
		
		return this.mt.findAndModify(
				query.addCriteria(
						Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId)), 
				update, 
				findAndModifyOptions, 
				getModelType());
	}

	
	public boolean canOrganizationParticipateEvent(String orgId, String locationId) {
		Assert.notNull(locationId);
		Assert.notNull(orgId);
		return this.mt.exists(
				new Query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(orgId)
								.andOperator(Criteria.where("favoriteLocations").elemMatch(
								Criteria.where(DEFAULT_REF_MONGO_KEY_ID).is(locationId)))), 
				getModelType());
	}
	
	public long countOrganizations() {
		return super.count(new Query());
	}
	
}
