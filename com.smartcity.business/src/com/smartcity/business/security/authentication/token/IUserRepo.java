/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import com.smartcity.data.access.User;

/**
 * @author gperreas
 *
 */
public interface IUserRepo {
		
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	boolean changePassword(String oldPassword, String newPassword, String username);

}
