/**
 * 
 */
package com.smartcity.data.access;

/**
 * @author gperreas
 *
 */
public class UserLogin {

	private String username;
	
	private String password;

	public UserLogin() {}
	
	/**
	 * @param username
	 * @param password
	 */
	public UserLogin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
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
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
