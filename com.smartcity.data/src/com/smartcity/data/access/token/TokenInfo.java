/**
 * 
 */
package com.smartcity.data.access.token;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gperreas
 *
 */
public class TokenInfo 
	implements Serializable 
{

	private static final long serialVersionUID = 1L;

	private String username;
	
	private String token;
	
	private Date created;
	
	/**
	 * @param username
	 * @param created
	 * @param token
	 */
	public TokenInfo(String username, Date created, String token) {
		this.username = username;
		this.created = created;
		this.token = token;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the created
	 */
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	
}
