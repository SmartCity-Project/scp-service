/**
 * 
 */
package com.smartcity.business.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;

import com.smartcity.business.security.authentication.token.ITokenController;

import com.smartcity.data.access.token.TokenInfo;

/**
 * @author gperreas
 *
 */
public class TokenFilter
	extends AuthenticationFilter
{
	public static final String HEADER_TOKEN_KEY = "X-Auth-Token";
	 
	private ITokenController tokenController;
	
	@Override
	protected Authentication generateAuthenticationRequest(
			HttpServletRequest request, 
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		 String token = request.getHeader(HEADER_TOKEN_KEY);
	     if (token == null) {
	        return null;
	     }
	     String username = this.tokenController.getUsername(token);
	     if (username == null) {
	         return null;
	     }

	     TokenInfo accountToken = new TokenInfo(username, null, token);
	     UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, accountToken);
	     authRequest.setDetails(getAuthenticationDetailsSource().buildDetails(request));
	     return authRequest;	
	}
	
    /**
     * @param request
     * @param response
     * @param filterChain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfullAuthentication(
    		HttpServletRequest request, 
    		HttpServletResponse response, 
    		FilterChain filterChain, 
    		Authentication authResult) throws IOException, ServletException { 	
    	super.successfullAuthentication(request, response, filterChain, authResult);
    	filterChain.doFilter(request, response);
    }

	/**
	 * @return the tokenController
	 */
	public ITokenController getTokenController() {
		return tokenController;
	}

	/**
	 * @param tokenController the tokenController to set
	 */
	public void setTokenController(ITokenController tokenController) {
		Assert.notNull(tokenController, "TokenController must not be null");
		this.tokenController = tokenController;
	}

}
