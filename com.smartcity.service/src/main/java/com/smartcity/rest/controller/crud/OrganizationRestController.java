/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.controllers.impl.EventController;
import com.smartcity.business.controllers.impl.OrganizationController;
import com.smartcity.business.controllers.impl.PetitionController;
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
@RestController
@RequestMapping(value="/api/crud/organizations")
public class OrganizationRestController 
	extends BaseRestController
{
	private Logger log = LoggerFactory.getLogger(OrganizationRestController.class);
	
	@Autowired
	private OrganizationController organizationController;
	
	@Autowired
	private EventController eventController;
	
	@Autowired
	private PetitionController petitionController;
	
	@RequestMapping(value="/{id}",
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationInfo(@PathVariable(value="id") String id) {
		getResponseMap().clear();
		Organization o = organizationController.findOrganizationById(id);
		if(o!=null) {
			getResponseMap().putAll(getOrganizationRestView(o));
		} else {
			getResponseMap().put("error", "organization not found");
		}
		
		return getResponseMap();
	}
	
	//TODO CREATE VIEW OF Users
	@RequestMapping(value="/{id}/users",
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationUsersBaseInfo(@PathVariable(value="id") String id) {
		getResponseMap().clear();
		Organization o = organizationController.findOrganizationById(id);
		if(o!=null) {
			getResponseMap().put("data", o.getUsers());
		}
		
		return getResponseMap();
	}
	
	//TODO CREATE VIEW OF Favorite Locations
	@RequestMapping(value="/{id}/favorite/locations", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationFavoriteLocation(@PathVariable(value="id") String id) {
		getResponseMap().clear();
		Organization o = organizationController.findOrganizationById(id);
		if(o!=null) {
			getResponseMap().put("organizationId", o.getId());
			getResponseMap().put("favoriteLocations", o.getFavoriteLocations());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/favorite/locations", 
			method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationFavoriteLocation(@PathVariable(value="id") String id,
			@RequestBody Location location) {
		
		try {
			getResponseMap().clear();	
			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), id);
				Organization o = organizationController.addFavoriteLocation(id, location);
				if(o!=null) {
					getResponseMap().put("organizationId", o.getId());
					getResponseMap().put("favoriteLocations", o.getFavoriteLocations());
				}
			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/favorite/locations", 
			method=RequestMethod.DELETE)
	public Map<String, Object> getOrganizationFavoriteLocation(@PathVariable(value="id") String id,
			@RequestParam String locationId) {
		
		try {
			getResponseMap().clear();	
			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), id);
				Organization o = organizationController.deleteFavoriteLocation(id, locationId);
				if(o!=null) {
					getResponseMap().put("organizationId", o.getId());
					getResponseMap().put("favoriteLocations", o.getFavoriteLocations());
				}
			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	//TODO CREATE VIEW OF Organization
	@RequestMapping(value="/edit", 
			method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> editOrganizationFavoriteLocation(@RequestBody Organization organization) {

		try {
			getResponseMap().clear();	
			Assert.notNull(organization.getId());

			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), organization.getId());
				Organization o = organizationController.editOrganization(organization);
				if(o!=null) {
					getResponseMap().put("data", o);
				} else {
					getResponseMap().put("error", "organization not found");
				}
			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	//TODO CREATE VIEW OF Organization
	@RequestMapping(value="/list", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationList() {
		getResponseMap().clear();
		List<Organization> results = organizationController.findAllOrganizations();
		if(results!=null) {
			Set<Map<String, Object>> viewSet = new LinkedHashSet<Map<String, Object>>();
			
			for(Organization o : results){
				viewSet.add(getOrganizationRestView(o));
			}
			
			getResponseMap().put("data", viewSet);
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/contacts", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationContacts(@PathVariable(value="id") String id) {
		getResponseMap().clear();
		Organization o = organizationController.findOrganizationById(id);
		if(o!=null) {
			getResponseMap().put("organizationId", o.getId());
			getResponseMap().put("contacts", o.getContacts());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/contacts", 
			method=RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> addContactForOrganizationId(@PathVariable(value="id") String id, 
			@RequestBody Contact contact) {
		
		try {
			getResponseMap().clear();		
			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), id);
				organizationController.saveContact(id, contact);
				getResponseMap().put("organizationId", id);
				getResponseMap().put("contact", contact);
			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/contacts/{contactId}", 
			method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteContactIdForOrganizationId(@PathVariable(value="id") String id, 
			@PathVariable(value="contactId") String contactId) {
		try {
			getResponseMap().clear();		
			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), id);
				getResponseMap().put("delete", organizationController.deleteContact(id, contactId));

			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
//	@RequestMapping(value="/{id}", 
//			method=RequestMethod.DELETE, produces=MediaType.APPLICATION_JSON_VALUE)
//	public Map<String, Object> deleteOrganization(@PathVariable(value="id") String id) {
//		getResponseMap().clear();
//		getResponseMap().put("delete", organizationController.deleteOrganization(id));
//		
//		return getResponseMap();
//	}
	
	@RequestMapping(value="/{id}/favorite/tags", 
			method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE,
			produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> saveOrganizationFavoriteTag(@PathVariable(value="id") String id,
			@RequestBody Tag tag) {
		try {
			getResponseMap().clear();		
			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), id);
				getResponseMap().put("organizationId", id);
				getResponseMap().put("favoriteTags", 
						organizationController.addFavoriteTag(id, tag).getFavoriteTags());
			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/favorite/tags", 
			method=RequestMethod.DELETE)
	public Map<String, Object> removeOrganizationFavoriteTag(@PathVariable(value="id") String id,
			@RequestParam Tag tag) {

		try {
			getResponseMap().clear();		
			User current = getCurrentUser();
			if(current!=null) {
				organizationController.isUserExistOnOrganization(current.getId(), id);
				getResponseMap().put("organizationId", id);
				getResponseMap().put("favoriteTags", 
						organizationController.deleteFavoriteTag(id, tag).getFavoriteTags());
			}
		} catch(IllegalArgumentException e) {
			getResponseMap().put("error", e.getMessage());
		} catch(AccessDeniedException e) {
			getResponseMap().put("error", e.getMessage());
		}
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}/favorite/tags", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getOrganizationFavoriteTags(@PathVariable(value="id") String id) {
		getResponseMap().clear();
		Organization o = organizationController.findOrganizationById(id);
		if(o!=null) {
			getResponseMap().put("organizationId", o.getId());
			getResponseMap().put("favoriteTags", o.getFavoriteTags());
		}
		return getResponseMap();
	}

	@RequestMapping(value="/total", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> countOrganizations() {
		getResponseMap().clear();
		getResponseMap().put("total", organizationController.countOrganizations());
		
		return getResponseMap();
	}

	private Map<String, Object> getOrganizationRestView(Organization o) {
		Map<String, Object> view = new HashMap<String, Object>();
		
		view.put("id", o.getId());
		view.put("name", o.getName());
		view.put("description", o.getDescription());
		view.put("url", o.getUrl());
		view.put("address", o.getAddress());
		view.put("contacts", o.getContacts());
		view.put("totalParticipations", eventController.countEventParticipations(o.getId()));
		view.put("totalPetitions", petitionController.countPetitions(o.getId()));
		view.put("favoriteTags", o.getFavoriteTags());
		view.put("favoriteLocations", o.getFavoriteLocations());
	
		return view;
	}
}
