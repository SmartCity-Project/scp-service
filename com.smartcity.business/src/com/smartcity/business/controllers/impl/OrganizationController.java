/**
 * 
 */
package com.smartcity.business.controllers.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.repositories.impl.ContactRepoImpl;
import com.smartcity.business.repositories.impl.LocationRepoImpl;
import com.smartcity.business.repositories.impl.OrganizationRepoImpl;
import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.business.tagging.TagController;
import com.smartcity.data.access.Organization;
import com.smartcity.data.access.User;
import com.smartcity.data.common.Contact;
import com.smartcity.data.geo.Location;
import com.smartcity.data.tagging.Tag;
import com.smartcity.exceptions.AccessDeniedException;

/**
 * @author gperreas
 *
 */
@Component
public class OrganizationController 
{
	@Autowired
	private OrganizationRepoImpl organizationRepoImpl;
	
	@Autowired
	private UserRepoImpl userRepoImpl;
	
	@Autowired
	private ContactRepoImpl contactRepoImpl;
	
	@Autowired
	private LocationRepoImpl locationRepoImpl;
	
	@Autowired
	private TagController tagController;
	
	public Organization saveOrganization(Organization org) {
		return this.organizationRepoImpl.save(org);
	}
	
	public Organization editOrganization(Organization org) {
		return this.organizationRepoImpl.updateOrganizationBaseInformation(org);
	}
	
	public boolean deleteOrganization(Organization org) {
		Assert.notNull(org);
		return this.deleteOrganization(org.getId());
	}
	
	public boolean deleteOrganization(String orgId) {
		Assert.notNull(orgId);
		return this.organizationRepoImpl.delete(orgId);
	}
	
	public List<Organization> findAllOrganizations() {
		return organizationRepoImpl.findAll();
	}
	
	public Organization findOrganizationById(String orgId) {
		Assert.notNull(orgId);
		return organizationRepoImpl.findOneByObjectId(orgId);
	}
	
	public String getOrganizationName(String orgId) {
		return this.organizationRepoImpl.getOrganizationName(orgId);
	}
	
	public List<Organization> getOrganizationByLocationId(String locationId) {
		Assert.notNull(locationId);
		return this.organizationRepoImpl.findListByMapKeyValue(
				Criteria.where("favoriteLocations").elemMatch(
						Criteria.where("location.id").is(locationId)));
	}

	public Organization getOrganizationByUserId(String userId) {
		Assert.notNull(userId);
		return this.organizationRepoImpl.findByUserId(userId);
	}
	
	public Contact saveContact(String orgId, Contact contact) {
		Assert.notNull(orgId);
		Assert.notNull(contact);
		
		Organization o = organizationRepoImpl.findOneByObjectId(orgId);
		if(o!=null) {
			
			if(contact.getId()==null) {
				contactRepoImpl.save(contact);
				o.addContact(contact);
				organizationRepoImpl.update(o);
			} else {
				contactRepoImpl.update(contact);
			}
			
			return contact;
		}
		return null;
	}
	
	public boolean deleteContact(String orgId, String contactId) {
		Assert.notNull(orgId);
		Assert.notNull(contactId);

		Organization o = organizationRepoImpl.findOneByObjectId(orgId);
		if(o!=null) {
			Contact contact = contactRepoImpl.findOneByObjectId(contactId);
			o.removeContact(contact);
			organizationRepoImpl.update(o);
			return contactRepoImpl.delete(contactId);
		}
		return false;
	}
	
	public Organization addFavoriteLocation(String orgId, Location location) {
		Assert.notNull(location);
		Assert.notNull(location.getId());
		
		Organization org = findOrganizationById(orgId);
		if(org!=null) {
			Location l = locationRepoImpl.findByLocationId(location.getId(), false);
			if(l!=null) {
				Organization uOrg = organizationRepoImpl.addLocation(orgId, location);
				if(uOrg!=null) org = uOrg;
			}
		}
		return org;
	}
	
	public Organization deleteFavoriteLocation(String orgId, String lid) {
		Assert.notNull(lid);
		
		Organization org = findOrganizationById(orgId);
		if(org!=null) {
			Location l = locationRepoImpl.findByLocationId(lid, false);
			if(l!=null) {
				org = organizationRepoImpl.removeLocation(orgId, l);
			}
		}
		return org;
	}
	
	public Organization addUser(String orgId, User user) {
		Organization org = findOrganizationById(orgId);
		if(org!=null) {
			org.addUser(user);
			organizationRepoImpl.update(org);
			user.setOrganization(org);
			userRepoImpl.save(user);
		}
		
		return org;
	}
	
	public Organization removeUser(String orgId, User user) {
		return null;
	}
	
	public Organization addFavoriteTag(String orgId, Tag tag) {
		Organization org = findOrganizationById(orgId);
		if(org!=null) {
			tagController.saveTag(tag);
			
			Organization uOrg = organizationRepoImpl.addTag(orgId, tag);
			if(uOrg!=null) org = uOrg;
		}
		
		return org;
	}
	
	public Organization deleteFavoriteTag(String orgId, Tag tag) {
		Organization org = findOrganizationById(orgId);
		if(org!=null) {
			org = organizationRepoImpl.removeTag(orgId, tag);
		}
		return org;
	}
	
	public boolean isUserExistOnOrganization(String userId, String orgId) {
		if(!this.organizationRepoImpl.isUserExistOnOrganization(userId, orgId)) {
			throw new AccessDeniedException("");
		}
		
		return true;
	}
	
	public boolean canOrganizationParticipateEvent(String orgId, String locationId) {
		return organizationRepoImpl.canOrganizationParticipateEvent(orgId, locationId);
	}
	
	public long countOrganizations() {
		return organizationRepoImpl.countOrganizations();
	}
}
