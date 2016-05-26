/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.smartcity.data.access.User;

/**
 * @author gperreas
 *
 */
public interface IPasswordController {

	String generateSalt();
	
	String encodePassword(String password, String salt);
	
	String encode(String data);
	
	boolean validate(String password, User userDetails);
	
	PasswordEncoder getPasswordEncoder();
	void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
