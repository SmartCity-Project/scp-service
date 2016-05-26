/**
 * 
 */
package com.smartcity.business.security.authentication;

import java.util.Set;

/**
 * @author gperreas
 *
 */
public interface IUser {
	
	IUserAttribute getAttribute(String name);
	
	Set<IUserAttribute> getAttributes();
	
	String getUsername();
		
	String getFirstName();
	
	String getLastName();
	
	String getEmail();
}
