/**
 * 
 */
package com.smartcity.data.access.token;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class OAuth2RefreshToken {

	@Id
    private String tokenId;
	
    private byte[] token;
    
    private byte[] authentication;

    public OAuth2RefreshToken() {}
    
	/**
	 * @return the tokenId
	 */
	public String getTokenId() {
		return tokenId;
	}

	/**
	 * @param tokenId the tokenId to set
	 */
	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	/**
	 * @return the token
	 */
	public byte[] getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(byte[] token) {
		this.token = token;
	}

	/**
	 * @return the authentication
	 */
	public byte[] getAuthentication() {
		return authentication;
	}

	/**
	 * @param authentication the authentication to set
	 */
	public void setAuthentication(byte[] authentication) {
		this.authentication = authentication;
	}
    
    
}
