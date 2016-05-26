/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.stereotype.Component;

import com.smartcity.business.security.authentication.token.IOAuth2RefreshTokenRepo;
import com.smartcity.data.access.token.OAuth2RefreshToken;

/**
 * @author gperreas
 *
 */
public class OAuth2RefreshTokenRepoImpl 
	extends BaseRepositoryImpl<OAuth2RefreshToken>
	implements IOAuth2RefreshTokenRepo
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<OAuth2RefreshToken> getModelType() {
		return OAuth2RefreshToken.class;
	}

	@Override
	public OAuth2RefreshToken findByTokenId(String tokenId) {

		return super.findOneByKeyValue("tokenId", tokenId);
	}

	@Override
	public void deleteByTokenId(String tokenId) {

		super.deleteByKeyValue("tokenId", tokenId);
	}
	
	

}
