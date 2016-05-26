/**
 * 
 */
package com.smartcity.business.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

import com.smartcity.business.security.authentication.token.ITokenController;
import com.smartcity.business.security.authentication.token.IUserStatusController;
import com.smartcity.business.security.authentication.token.TokenDeniedException;

import com.smartcity.data.access.User;
import com.smartcity.data.access.token.TokenInfo;
import com.smartcity.data.access.token.UserStatus;

/**
 * @author gperreas
 *
 */
public class TokenAuthenticationProvider
	implements AuthenticationProvider
{
	@Autowired
	private UserDetailsManager userDetailsManager;
	
	@Autowired
	private ITokenController tokenController;
	
	@Autowired IUserStatusController userStatusController;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
	    }
		
		UsernamePasswordAuthenticationToken inPasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        if (!(inPasswordAuthenticationToken.getCredentials() instanceof TokenInfo)){
            return null;
        }
        
        TokenInfo token = (TokenInfo) inPasswordAuthenticationToken.getCredentials();
        String username = tokenController.getUsername(token.getToken());

        User user = (User) userDetailsManager.loadUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("User Name %s Not Valid", username));
        }
        
        UserStatus userStatus = userStatusController.getUserStatusByUserId(user.getId());
        if(userStatus!=null) {
	        if(userStatus.isLoggedOut()) {
	        	throw new TokenDeniedException(userStatus.getTokenInfo().getToken());
	        }
        }
        
        if (!tokenController.validateToken(token.getToken(), user)) {
            throw new BadCredentialsException("Secured Token Not Valid");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
        		new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(authentication.getDetails());
        return usernamePasswordAuthenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

}
