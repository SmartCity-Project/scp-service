/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import com.smartcity.data.access.token.OAuth2ClientDetails;

/**
 * @author gperreas
 *
 */
public interface IOAuth2ClientDetailsRepo {
	
	boolean deleteByClientId(String clientId);

	OAuth2ClientDetails update(OAuth2ClientDetails mongoClientDetails);

    boolean updateClientSecret(String clientId, String newSecret);

    OAuth2ClientDetails findByClientId(String clientId) throws IllegalArgumentException;

}
