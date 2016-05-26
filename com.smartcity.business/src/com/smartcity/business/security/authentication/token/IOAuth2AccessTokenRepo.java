/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.util.List;

import com.smartcity.data.access.token.OAuth2AccessToken;

/**
 * @author gperreas
 *
 */
public interface IOAuth2AccessTokenRepo {

	OAuth2AccessToken findByTokenId(String tokenId);
	
	void deleteByTokenId(String tokenId);
	
	void deleteByRefreshTokenId(String refreshTokenId);

    OAuth2AccessToken findByAuthenticationId(String authenticationId);

    List<OAuth2AccessToken> findByUsernameAndClientId(String username, String clientId);

    List<OAuth2AccessToken> findByClientId(String clientId);
}
