/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.stereotype.Component;

import com.smartcity.business.security.authentication.token.IOAuth2ClientTokenRepo;
import com.smartcity.data.access.token.OAuth2ClientToken;

/**
 * @author gperreas
 *
 */
public class OAuth2ClientTokenRepoImpl
	extends BaseRepositoryImpl<OAuth2ClientToken>
	implements IOAuth2ClientTokenRepo
{
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected Class<OAuth2ClientToken> getModelType() {

		return OAuth2ClientToken.class;
	}

	@Override
	public OAuth2ClientToken findByAuthenticationId(String authenticationId) {

		return super.findOneByKeyValue("authenticationId", authenticationId);
	}

	@Override
	public void deleteByAuthenticationId(String authenticationId) {
		
		super.deleteByKeyValue("authenticationId", authenticationId);
	}

}
