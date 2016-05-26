/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.smartcity.business.controllers.impl.EventController;
import com.smartcity.business.repositories.IRepository;
import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.data.access.User;
import com.smartcity.data.common.Event;

/**
 * @author gperreas
 *
 */
@RestController
@RequestMapping("/api/crud/users")
public class UserRestController 
	extends BaseRestController
{
	private Logger log = LoggerFactory.getLogger(UserRestController.class);
	
	@Autowired
	private UserRepoImpl userRepoImpl;

	@Autowired
	private EventController eventController;

	@RequestMapping(value="/current", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getUserDetails() {
		getResponseMap().clear();
		
		User current = getCurrentUser();
		
		getResponseMap().putAll(getUserRestView(current));

		return getResponseMap();
	}
	
	@RequestMapping(value="/{id}", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> findUserById(@PathVariable(value="id") String id) {
		getResponseMap().clear();
		
		User user = userRepoImpl.findOneByObjectId(id);

		getResponseMap().putAll(getUserRestView(user));
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/authorities", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> getUserAuthorities() {
		getResponseMap().clear();
		User current = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		getResponseMap().put("role", current.getAuthorities().iterator().next().getAuthority());
		
		return getResponseMap();
	}
	
	@RequestMapping(value="/total", 
			method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> countUsers() {
		getResponseMap().clear();
		getResponseMap().put("total", userRepoImpl.countUsers());
		
		return getResponseMap();
	}
	
	private Map<String, Object> getUserRestView(User u) {
		Map<String, Object> view = new HashMap<String, Object>();
		
		view.put("firstName", u.getName());
		view.put("lastName", u.getSurname());
		view.put("email", u.getEmail());
		view.put("role", u.getAuthorities().iterator().next().getAuthority());
		
		if(u.getOrganization()!=null) {
			view.put("organizationId", u.getOrganization().getId());
		} else {
			view.put("id", u.getId());
			view.put("totalPublishedEvents", eventController.countEvents(u.getId()));
		}
	
		return view;
	}
}
