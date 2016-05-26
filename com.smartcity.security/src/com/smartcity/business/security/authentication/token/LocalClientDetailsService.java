/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Sets.filter;
import static com.google.common.collect.Sets.newHashSet;

import com.smartcity.business.repositories.impl.OAuth2ClientDetailsRepoImpl;
import com.smartcity.data.access.token.OAuth2ClientDetails;

/**
 * @author gperreas
 *
 */
public class LocalClientDetailsService 
	implements ClientDetailsService, ClientRegistrationService
{

	@Autowired
	private OAuth2ClientDetailsRepoImpl clientDetailsRepoImpl;
	
	@Autowired
	private IPasswordController passwordController;
	
	@Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        try {
            return clientDetailsRepoImpl.findByClientId(clientId);
        } catch (IllegalArgumentException e) {
            throw new ClientRegistrationException("No Client Details for client id", e);
        }
    }

    @Override
    public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
        OAuth2ClientDetails mongoClientDetails = new OAuth2ClientDetails(clientDetails.getClientId(),
        		passwordController.encode(clientDetails.getClientSecret()),
                clientDetails.getScope(),
                clientDetails.getResourceIds(),
                clientDetails.getAuthorizedGrantTypes(),
                clientDetails.getRegisteredRedirectUri(),
                newArrayList(clientDetails.getAuthorities()),
                clientDetails.getAccessTokenValiditySeconds(),
                clientDetails.getRefreshTokenValiditySeconds(),
                clientDetails.getAdditionalInformation(),
                getAutoApproveScopes(clientDetails));

        clientDetailsRepoImpl.save(mongoClientDetails);
    }

    @Override
    public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
        OAuth2ClientDetails cd = new OAuth2ClientDetails(clientDetails.getClientId(),
                clientDetails.getClientSecret(),
                clientDetails.getScope(),
                clientDetails.getResourceIds(),
                clientDetails.getAuthorizedGrantTypes(),
                clientDetails.getRegisteredRedirectUri(),
                newArrayList(clientDetails.getAuthorities()),
                clientDetails.getAccessTokenValiditySeconds(),
                clientDetails.getRefreshTokenValiditySeconds(),
                clientDetails.getAdditionalInformation(),
                getAutoApproveScopes(clientDetails));
        OAuth2ClientDetails result = clientDetailsRepoImpl.update(cd);

        if (result!=null) {
            throw new NoSuchClientException("No such Client Id");
        }
    }

    @Override
    public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
        boolean result = clientDetailsRepoImpl.updateClientSecret(clientId, passwordController.encode(secret));
        if (!result) {
            throw new NoSuchClientException("No such client id");
        }
    }

    @Override
    public void removeClientDetails(String clientId) throws NoSuchClientException {
        boolean result = clientDetailsRepoImpl.deleteByClientId(clientId);
        if (!result) {
            throw new NoSuchClientException("No such client id");
        }
    }

    @Override
    public List<ClientDetails> listClientDetails() {
        List<OAuth2ClientDetails> all = clientDetailsRepoImpl.findAll();
        return FluentIterable.from(all).transform(toClientDetails()).toList();
    }

    private Set<String> getAutoApproveScopes(ClientDetails clientDetails) {
        if (clientDetails.isAutoApprove("true")) {
            return newHashSet("true"); // all scopes autoapproved
        }
        return filter(clientDetails.getScope(), ByAutoApproveOfScope(clientDetails));
    }

    private Predicate<String> ByAutoApproveOfScope(final ClientDetails clientDetails) {
        return new Predicate<String>() {
            @Override
            public boolean apply(final String scope) {
                return clientDetails.isAutoApprove(scope);
            }
        };
    }

    private Function<OAuth2ClientDetails, ClientDetails> toClientDetails() {
        return new Function<OAuth2ClientDetails, ClientDetails>() {
            @Override
            public ClientDetails apply(OAuth2ClientDetails input) {
                return input;
            }
        };
    }
}
