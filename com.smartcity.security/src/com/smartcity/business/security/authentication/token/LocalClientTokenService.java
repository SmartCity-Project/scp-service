/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.ClientKeyGenerator;
import org.springframework.security.oauth2.client.token.ClientTokenServices;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;

import com.smartcity.business.repositories.impl.OAuth2ClientTokenRepoImpl;
import com.smartcity.data.access.token.OAuth2ClientToken;

/**
 * @author gperreas
 *
 */
public class LocalClientTokenService 
	implements ClientTokenServices
{
	private final Logger log = LoggerFactory.getLogger(LocalClientTokenService.class.getName());

	private OAuth2ClientTokenRepoImpl clientTokenRepo;

    private ClientKeyGenerator clientKeyGenerator;
    
    @Autowired
    public LocalClientTokenService(
    		OAuth2ClientTokenRepoImpl clientTokenRepository,
    		ClientKeyGenerator clientKeyGenerator) {
    	this.clientTokenRepo = clientTokenRepository;
    	this.clientKeyGenerator = clientKeyGenerator;
    }
	
	@Override
	public OAuth2AccessToken getAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
		
		OAuth2ClientToken cToken = 
				clientTokenRepo.findByAuthenticationId(
						clientKeyGenerator.extractKey(resource, authentication));
		
        return SerializationUtils.deserialize(cToken.getToken());    
	}

	@Override
	public void saveAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication,
			OAuth2AccessToken accessToken) {
		removeAccessToken(resource, authentication);
		
		OAuth2ClientToken cToken = new OAuth2ClientToken();
		cToken.setId(UUID.randomUUID().toString());
		cToken.setClientId(resource.getClientId());
		cToken.setTokenId(accessToken.getValue());
		cToken.setAuthenticationId(clientKeyGenerator.extractKey(resource, authentication));
		cToken.setUsername(authentication.getName());
		cToken.setToken(SerializationUtils.serialize(accessToken));
		
		clientTokenRepo.save(cToken);
	}

	@Override
	public void removeAccessToken(OAuth2ProtectedResourceDetails resource, Authentication authentication) {
		 clientTokenRepo.deleteByAuthenticationId(
				 clientKeyGenerator.extractKey(
						 resource, authentication));
	}

}
