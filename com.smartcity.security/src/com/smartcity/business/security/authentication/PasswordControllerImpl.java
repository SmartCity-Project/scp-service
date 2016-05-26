/**
 * 
 */
package com.smartcity.business.security.authentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.security.authentication.token.IPasswordController;

import com.smartcity.data.access.User;

/**
 * @author gperreas
 *
 */
@Component
public class PasswordControllerImpl 
	implements IPasswordController
{

	@Value("${auth.server.password.salt}")
	public String serverSalt;
	    
	@Value("${auth.server.password.salt.length}")
	public int passwordSecretLength;
	
	public PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	@Override
	public String generateSalt() {
		return KeyGenerators.secureRandom(passwordSecretLength).toString();
	}

	@Override
	public String encodePassword(String password, String salt) {
		return passwordEncoder.encode(rawPassword(password, salt));
	}
	
	@Override
	public String encode(String data) {
		return passwordEncoder.encode(data);
	}

	private String rawPassword(String password, String salt) {
        StringBuilder rpBuilder = new StringBuilder();
        rpBuilder.append(password);
        rpBuilder.append(":");
        rpBuilder.append(salt);
        rpBuilder.append(":");
        rpBuilder.append(serverSalt);
        return rpBuilder.toString();
    }

	@Override
	public boolean validate(String password, User userDetails) {
		return this.passwordEncoder.matches(rawPassword(password, userDetails.getSalt()), userDetails.getPassword());
	}

	@Override
	public PasswordEncoder getPasswordEncoder() {
		return this.passwordEncoder;
	}

	@Override
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		Assert.notNull(passwordEncoder, "PasswordEncoder must not be null");
		this.passwordEncoder = passwordEncoder;
	}

}
