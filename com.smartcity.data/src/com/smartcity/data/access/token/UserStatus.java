/**
 * 
 */
package com.smartcity.data.access.token;


import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author gperreas
 *
 */
@Document
public class UserStatus
{
	@Id
    private String id;
	
	private boolean isRegistered = false;
    
	private boolean isEmailVerified = false;
    
	private boolean isLoggedOut = false;
	
	private TokenInfo tokenInfo;

	
	public UserStatus() {}
	/**
	 * @param uuid
	 * @param tokenInfo
	 */
	public UserStatus(String id, TokenInfo tokenInfo) {
		super();
		this.id = id;
		this.tokenInfo = tokenInfo;
	}

	/**
	 * @param id
	 * @param isRegistered
	 * @param isEmailVerified
	 * @param tokenInfo
	 */
	public UserStatus(String id, boolean isRegistered, boolean isEmailVerified, TokenInfo tokenInfo) {
		super();
		this.id = id;
		this.isRegistered = isRegistered;
		this.isEmailVerified = isEmailVerified;
		this.tokenInfo = tokenInfo;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the isRegistered
	 */
	public boolean isRegistered() {
		return isRegistered;
	}

	/**
	 * @param isRegistered the isRegistered to set
	 */
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}

	/**
	 * @return the isEmailVerified
	 */
	public boolean isEmailVerified() {
		return isEmailVerified;
	}

	/**
	 * @param isEmailVerified the isEmailVerified to set
	 */
	public void setEmailVerified(boolean isEmailVerified) {
		this.isEmailVerified = isEmailVerified;
	}

	/**
	 * @return the tokenInfo
	 */
	public TokenInfo getTokenInfo() {
		return tokenInfo;
	}

	/**
	 * @param tokenInfo the tokenInfo to set
	 */
	public void setTokenInfo(TokenInfo tokenInfo) {
		this.tokenInfo = tokenInfo;
	}
	/**
	 * @return the isLoggedOut
	 */
	public boolean isLoggedOut() {
		return isLoggedOut;
	}
	/**
	 * @param isLoggedOut the isLoggedOut to set
	 */
	public void setLoggedOut(boolean isLoggedOut) {
		this.isLoggedOut = isLoggedOut;
	}
	
}
