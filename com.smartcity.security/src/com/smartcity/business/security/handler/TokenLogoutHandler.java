/**
 * 
 */
package com.smartcity.business.security.handler;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import com.smartcity.business.security.authentication.UserStatusControllerImpl;

import com.smartcity.data.access.User;


/**
 * @author gperreas
 *
 */
public class TokenLogoutHandler 
	extends SecurityContextLogoutHandler
{
	private final Logger log = LoggerFactory.getLogger(TokenLogoutHandler.class);

	@Override
	public void logout(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication) {

		super.logout(request, response, authentication);
	}

	

}
