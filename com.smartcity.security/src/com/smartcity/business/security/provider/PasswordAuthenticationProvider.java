package com.smartcity.business.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import com.smartcity.business.security.authentication.token.IPasswordController;

import com.smartcity.data.access.User;

@Component
public class PasswordAuthenticationProvider 
	implements AuthenticationProvider
{
 
    @Autowired
    protected UserDetailsManager userDetailsManager;
    
    @Autowired
    private IPasswordController passwordControllerImpl;
    
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }

        UsernamePasswordAuthenticationToken inPasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        if (!(inPasswordAuthenticationToken.getCredentials() instanceof String)) {
            return null;
        }

        String username = inPasswordAuthenticationToken.getName();
        String password = (String) inPasswordAuthenticationToken.getCredentials();
        
        User userAccountDetails = (User) userDetailsManager.loadUserByUsername(username);
        if (userAccountDetails == null) {
            throw new UsernameNotFoundException(String.format("User Name %s Not Valid", username));
        }

        if (!passwordControllerImpl.validate(password, userAccountDetails)) {
            throw new BadCredentialsException("Secured Password Not Valid");
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userAccountDetails, null, userAccountDetails.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(authentication.getDetails());
        return usernamePasswordAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
