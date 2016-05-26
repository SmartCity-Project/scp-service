/**
 * 
 */
package com.smartcity.business.security.authentication;

import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;

/**
 * @author gperreas
 *
 */
public interface IAuthenticationProvider{

	public IUser getAuthenticatedUser();
}
