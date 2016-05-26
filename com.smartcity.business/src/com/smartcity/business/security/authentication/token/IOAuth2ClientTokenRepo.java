/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import com.smartcity.data.access.token.OAuth2ClientToken;

/**
 * @author gperreas
 *
 */
public interface IOAuth2ClientTokenRepo {

	OAuth2ClientToken findByAuthenticationId(String authenticationId);
	
	void deleteByAuthenticationId(String authenticationId);

}
