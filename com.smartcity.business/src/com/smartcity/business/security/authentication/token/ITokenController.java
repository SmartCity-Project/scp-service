/**
 * 
 */
package com.smartcity.business.security.authentication.token;


import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.smartcity.data.access.User;
import com.smartcity.data.access.token.TokenInfo;


/**
 * @author gperreas
 *
 */
public interface ITokenController {
	
	TokenInfo generateToken(User userDetails);
	
	boolean validateToken(String token, User userDetails);
	
	PasswordEncoder getPasswordEncoder();
	void setPasswordEncoder(PasswordEncoder passwordEncoder);
	
	String getUsername(String token);
	Date getTimestamp(String token);
}
