/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import org.springframework.security.core.AuthenticationException;

/**
 * @author gperreas
 *
 */
public class TokenDeniedException 
	extends AuthenticationException
{

	private static final long serialVersionUID = 1L;

	/**
	 * @param repository
	 */
	public TokenDeniedException(String oldTokenValue) {
		super("You must login, you give us bad token, old token:" + oldTokenValue);
	}

}
