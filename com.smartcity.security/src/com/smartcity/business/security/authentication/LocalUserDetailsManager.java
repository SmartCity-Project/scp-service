/**
 * 
 */
package com.smartcity.business.security.authentication;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.repositories.impl.UserRepoImpl;
import com.smartcity.data.access.User;

/**
 * @author gperreas
 *
 */
@Component
public class LocalUserDetailsManager 
	implements UserDetailsManager
{
	
	private final Logger log = LoggerFactory.getLogger(LocalUserDetailsManager.class);

	@Autowired
    private UserRepoImpl userRepoImpl;

    private AuthenticationManager authenticationManager;

    public LocalUserDetailsManager() {}

	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepoImpl.findByUsername(username);
	}

	@Override
	public void createUser(UserDetails user) {
		validateUserDetails(user);
		userRepoImpl.save(getUser(user));
	}

	@Override
	public void updateUser(UserDetails user) {
		validateUserDetails(user);
		userRepoImpl.save(getUser(user));
	}

	@Override
	public void deleteUser(String username) {
		User user = userRepoImpl.findOneByObjectId(username);
		userRepoImpl.delete(user);
	}

	@Override
	public void changePassword(String oldPassword, String newPassword) {
		Authentication current = SecurityContextHolder.getContext().getAuthentication();

        if (current == null) {
            // This would indicate bad coding somewhere
            throw new AccessDeniedException("Can't change password as no Authentication object found in context " +
                    "for current user.");
        }

        String username = current.getName();

        // If an authentication manager has been set, re-authenticate the user with the supplied password.
        if (authenticationManager != null) {
            log.debug("Reauthenticating user '"+ username + "' for password change request.");

            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, oldPassword));
        } else {
            log.debug("No authentication manager set. Password won't be re-checked.");
        }

        log.debug("Changing password for user '"+ username + "'");

        userRepoImpl.changePassword(oldPassword, newPassword, username);
        
        SecurityContextHolder.getContext().setAuthentication(createNewAuthentication(current));
	}

	@Override
	public boolean userExists(String username) {
		User user = userRepoImpl.findOneByObjectId(username);
        return user != null;
	}
	
	protected Authentication createNewAuthentication(Authentication currentAuth) {
        UserDetails user = loadUserByUsername(currentAuth.getName());

        UsernamePasswordAuthenticationToken newAuthentication =
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        newAuthentication.setDetails(currentAuth.getDetails());

        return newAuthentication;
    }

    private User getUser(UserDetails userDetails) {
    	return (User) userDetails;
    }

	private void validateUserDetails(UserDetails user) {
        Assert.hasText(user.getUsername(), "Username must not be empty or null");
        validateAuthorities(user.getAuthorities());
    }

    private void validateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Assert.notNull(authorities, "Authorities list must not be null");

        for (GrantedAuthority authority : authorities) {
            Assert.notNull(authority, "Authorities list contains a null entry");
            Assert.hasText(authority.getAuthority(), "getAuthority() method must return a non-empty string");
        }
    }

	
}
