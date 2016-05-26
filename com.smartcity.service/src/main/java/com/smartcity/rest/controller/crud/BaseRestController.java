/**
 * 
 */
package com.smartcity.rest.controller.crud;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;

import com.smartcity.data.access.User;

/**
 * @author gperreas
 *
 */
public class BaseRestController
{
	private Map<String, Object> responseMap = new HashMap<String, Object>();
	
	public Map<String, Object> getResponseMap() {
		return this.responseMap;
	}
	
	public void clearResponseMap() {
		this.responseMap.clear();
	}
	
	public User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
}
