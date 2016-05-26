package com.smartcity.business.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.security.authentication.UserStatusControllerImpl;

import com.smartcity.data.access.User;

@Component
public class LoginFailureHandler 
	extends SimpleUrlAuthenticationFailureHandler
{

	public static final String USERNAME_KEY = "username";

	@Autowired
	protected UserDetailsManager userDetailsManager;

	@Autowired
	protected UserStatusControllerImpl userStatusController;

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {

		if (exception instanceof BadCredentialsException) {
			String username = request.getParameter(USERNAME_KEY);

			try {
				WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
				User user = (User) userDetailsManager.loadUserByUsername(username);
				userStatusController.updateLoginAuthenticationFailure(user.getId(), webAuthenticationDetails);
			} catch (Exception e) {
			
			}
		}

		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
	}


}
