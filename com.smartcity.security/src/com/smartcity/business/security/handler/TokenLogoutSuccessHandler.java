/**
 * 
 */
package com.smartcity.business.security.handler;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import com.smartcity.business.security.authentication.UserStatusControllerImpl;
import com.smartcity.business.security.authentication.token.IUserStatusController;

import com.smartcity.data.access.User;
import com.smartcity.data.access.token.UserStatus;

/**
 * @author gperreas
 *
 */
public class TokenLogoutSuccessHandler 
	extends SimpleUrlLogoutSuccessHandler
{
	
	private final Logger log = LoggerFactory.getLogger(TokenLogoutSuccessHandler.class);
	
	private IUserStatusController userStatusController;

	public TokenLogoutSuccessHandler(IUserStatusController userStatusController) {
		super();
		this.userStatusController = userStatusController;
	}

	@Override
	public void onLogoutSuccess(
			HttpServletRequest request, 
			HttpServletResponse response, 
			Authentication authentication)
			throws IOException, ServletException {

		User current = (User) authentication.getPrincipal();
		UserStatus userStatus = userStatusController.getUserStatusByUserId(current.getId());
		if(!userStatus.isLoggedOut()) {
			userStatusController.updateLogoutSuccess(current, true);
		}
        
		response.setStatus(HttpServletResponse.SC_OK);
			
		super.onLogoutSuccess(request, response, authentication);
	}

	
}
