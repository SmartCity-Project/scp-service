/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;
import com.smartcity.business.security.authentication.token.IOAuth2ClientDetailsRepo;
import com.smartcity.data.access.token.OAuth2ClientDetails;

/**
 * @author gperreas
 *
 */
public class OAuth2ClientDetailsRepoImpl 
	extends BaseRepositoryImpl<OAuth2ClientDetails>
	implements IOAuth2ClientDetailsRepo
{

	private static final long serialVersionUID = 1L;
	
    protected static final String CLIENT_SECRET = "clientSecret";

	@Override
	protected Class<OAuth2ClientDetails> getModelType() {

		return OAuth2ClientDetails.class;
	}
    
    
    @Override
    public boolean deleteByClientId(String clientId) {

        return super.delete(clientId);
    }

    @Override
    public OAuth2ClientDetails update(OAuth2ClientDetails clientDetails) {
        Query query = Query.query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(clientDetails.getClientId()));

        Update update = Update.update("scope", clientDetails.getScope())
                .set("resourceIds", clientDetails.getResourceIds())
                .set("authorizedGrantTypes", clientDetails.getAuthorizedGrantTypes())
                .set("authorities", clientDetails.getAuthorities())
                .set("accessTokenValiditySeconds", clientDetails.getAccessTokenValiditySeconds())
                .set("refreshTokenValiditySeconds", clientDetails.getRefreshTokenValiditySeconds())
                .set("additionalInformation", clientDetails.getAdditionalInformation())
                .set("autoApproveScopes", clientDetails.getAutoApproveScopes())
                .set("registeredRedirectUris", clientDetails.getRegisteredRedirectUri());

        WriteResult writeResult = mt.updateFirst(query, update, OAuth2ClientDetails.class);

        return clientDetails;
    }

    @Override
    public boolean updateClientSecret(String clientId,
                                      String newSecret) {
        Query query = Query.query(Criteria.where(DEFAULT_MONGO_KEY_ID).is(clientId));

        Update update = Update.update(CLIENT_SECRET, newSecret);

        WriteResult writeResult = mt.updateFirst(query, update, OAuth2ClientDetails.class);
        
        return writeResult.getN() == 1;
    }

    @Override
    public OAuth2ClientDetails findByClientId(String clientId) throws IllegalArgumentException {
       
        OAuth2ClientDetails clientDetails = super.findOneByKeyValue(DEFAULT_MONGO_KEY_ID, clientId);
        
        if (clientDetails == null) {
            throw new IllegalArgumentException("No valid client id");
        }
        
        return clientDetails;
    }


}
