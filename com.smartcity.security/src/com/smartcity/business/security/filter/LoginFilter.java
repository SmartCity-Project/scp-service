/**
 * 
 */
package com.smartcity.business.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.io.CharStreams;

import com.smartcity.data.access.UserLogin;

/**
 * @author gperreas
 *
 */
public class LoginFilter 
	extends AuthenticationFilter
{

	public static final String HEADER_AUTHORIZATION = "Authorization";
	
	@Override
	protected Authentication generateAuthenticationRequest(
			HttpServletRequest request, 
			HttpServletResponse response,
			FilterChain filterChain) throws ServletException, IOException {

		if (request.getHeader(HEADER_AUTHORIZATION)==null) {
			throw new AuthenticationServiceException("Authentication must contains "+ HEADER_AUTHORIZATION +" header");
		}
		
		if (!request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		
		String input = new String(Base64.decodeBase64(request.getHeader(HEADER_AUTHORIZATION)));
		UserLogin login = new ObjectMapper().readValue(input, UserLogin.class);

		if (login == null || Strings.isNullOrEmpty(login.getUsername()) || Strings.isNullOrEmpty(login.getPassword())) {
			return null;
		}

		UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(login.getUsername(),
				login.getPassword());
		authRequest.setDetails(getAuthenticationDetailsSource().buildDetails(request));
		return authRequest;
	}

}
