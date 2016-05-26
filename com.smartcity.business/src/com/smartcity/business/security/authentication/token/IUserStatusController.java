/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.smartcity.data.access.User;
import com.smartcity.data.access.token.UserStatus;

/**
 * @author gperreas
 *
 */
public interface IUserStatusController {

	UserStatus getUserStatusByUserId(String userId);
	
	UserStatus updateLoginAuthenticationSuccess(User userDetails,
			WebAuthenticationDetails webAuthenticationDetails);
	
	UserStatus updateTokenAuthenticationSuccess(User userDetails, 
			WebAuthenticationDetails webAuthenticationDetails);
	
	UserStatus updateLoginAuthenticationFailure(String userId, 
			WebAuthenticationDetails webAuthenticationDetails);
	
	boolean updateLogoutSuccess(User userDetails, boolean isLoggedOut);
	        
}
