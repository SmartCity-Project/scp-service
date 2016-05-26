/**
 * 
 */
package com.smartcity.business.repositories.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.smartcity.business.security.authentication.token.IOAuth2AccessTokenRepo;
import com.smartcity.data.access.token.OAuth2AccessToken;

/**
 * @author gperreas
 *
 */
public class OAuth2AccessTokenRepoImpl 
	extends BaseRepositoryImpl<OAuth2AccessToken>
	implements IOAuth2AccessTokenRepo
{

	private static final long serialVersionUID = 1L;


	@Override
	protected Class<OAuth2AccessToken> getModelType() {
		return OAuth2AccessToken.class;
	}
	
	
	@Override
	public OAuth2AccessToken findByTokenId(String tokenId) {

		return super.findOneByKeyValue("tokenId", tokenId);
	}

	@Override
	public void deleteByTokenId(String tokenId) {

		super.deleteByKeyValue("tokenId", tokenId);
	}

	@Override
	public void deleteByRefreshTokenId(String refreshTokenId) {

		super.deleteByKeyValue("refreshTokenId", refreshTokenId);
	}

	@Override
	public OAuth2AccessToken findByAuthenticationId(String authenticationId) {

		return super.findOneByKeyValue("authenticationId", authenticationId);
	}

	@Override
	public List<OAuth2AccessToken> findByUsernameAndClientId(String username, String clientId) {

		Query query = Query.query(
				Criteria.where("username").is(username)
				.andOperator(Criteria.where("clientId").is(clientId)));
        return mt.find(query, OAuth2AccessToken.class);
    }

	@Override
	public List<OAuth2AccessToken> findByClientId(String clientId) {
		
		Query query = Query.query(Criteria.where("clientId").is(clientId));
        return mt.find(query, OAuth2AccessToken.class);
	}


}
