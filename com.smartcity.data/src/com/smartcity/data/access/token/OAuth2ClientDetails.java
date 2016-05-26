/**
 * 
 */
package com.smartcity.data.access.token;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

/**
 * @author gperreas
 *
 */
@Document
public class OAuth2ClientDetails implements ClientDetails {

    @Id
    private String clientId;
    private String clientSecret;
    private Set<String> scope = Collections.emptySet();
    private Set<String> resourceIds = Collections.emptySet();
    private Set<String> authorizedGrantTypes = Collections.emptySet();
    private Set<String> registeredRedirectUris;
    private List<GrantedAuthority> authorities = Collections.emptyList();
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Map<String, Object> additionalInformation = new LinkedHashMap<String, Object>();
    private Set<String> autoApproveScopes;


    public OAuth2ClientDetails() {
        
    }

    public OAuth2ClientDetails(final String clientId,
	            final String clientSecret,
	            final Set<String> scope,
	            final Set<String> resourceIds,
	            final Set<String> authorizedGrantTypes,
	            final Set<String> registeredRedirectUris,
	            final List<GrantedAuthority> authorities,
	            final Integer accessTokenValiditySeconds,
	            final Integer refreshTokenValiditySeconds,
	            final Map<String, Object> additionalInformation,
	            final Set<String> autoApproveScopes) {
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.scope = scope;
		this.resourceIds = resourceIds;
		this.authorizedGrantTypes = authorizedGrantTypes;
		this.registeredRedirectUris = registeredRedirectUris;
		this.authorities = authorities;
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
		this.additionalInformation = additionalInformation;
		this.autoApproveScopes = autoApproveScopes;
	}

	public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public Set<String> getScope() {
        return scope;
    }

    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAutoApproveScopes(final Set<String> autoApproveScopes) {
        this.autoApproveScopes = autoApproveScopes;
    }

    public Set<String> getAutoApproveScopes() {
        return autoApproveScopes;
    }

    @Override
    public boolean isScoped() {
        return this.scope != null && !this.scope.isEmpty();
    }

    @Override
    public boolean isSecretRequired() {
        return this.clientSecret != null;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUris;
    }

    @Override
    public boolean isAutoApprove(final String scope) {
        if (autoApproveScopes == null) {
            return false;
        }
        for (String auto : autoApproveScopes) {
            if (auto.equals("true") || scope.matches(auto)) {
                return true;
            }
        }
        return false;
    }
}
