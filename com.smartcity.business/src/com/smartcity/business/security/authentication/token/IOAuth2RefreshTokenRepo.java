/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import com.smartcity.data.access.token.OAuth2RefreshToken;

/**
 * @author gperreas
 *
 */
public interface IOAuth2RefreshTokenRepo {

	OAuth2RefreshToken findByTokenId(String tokenId);

	void deleteByTokenId(String tokenId);
}
