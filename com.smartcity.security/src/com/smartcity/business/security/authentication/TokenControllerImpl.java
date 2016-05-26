/**
 * 
 */
package com.smartcity.business.security.authentication;

import java.security.SecureRandom;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.smartcity.business.security.authentication.token.ITokenController;

import com.smartcity.data.access.User;
import com.smartcity.data.access.token.TokenInfo;

/**
 * @author gperreas
 *
 */
@Component
public class TokenControllerImpl
    implements ITokenController
{
	public static final int RADIX = 20;
	
	@Value("${auth.server.password.salt}")
	public String serverSalt;
	    
	@Value("${auth.server.token.active.duration}")
	public int tokenActiveDuration;
	    
	@Value("${auth.server.token.active.threshold.duration}")
	public int tokenActiveThresholdDuration;
	
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	
	@Override
	public TokenInfo generateToken(User userDetails) {
		Random random = new SecureRandom();
        long signatureTimestamp  = 
        		System.currentTimeMillis() + 
        		Math.round(Math.floor(random.nextDouble() * 1000 * tokenActiveThresholdDuration));

        StringBuilder tokenBuilder = new StringBuilder();
        tokenBuilder.append(userDetails.getUsername());
        tokenBuilder.append(":");
        tokenBuilder.append(Long.toString(signatureTimestamp, RADIX));
        tokenBuilder.append(":");
        tokenBuilder.append(computeSignature(userDetails, signatureTimestamp));

        TokenInfo token = 
        		new TokenInfo(userDetails.getUsername(), new Date(), tokenBuilder.toString());
        return token;
	}

	/**
     * @param userAccountDetails
     * @param timestamp
     * @return
     */
    private String computeSignature(User userDetails, long timestamp) {
        return passwordEncoder.encode(rawSignature(userDetails, timestamp));
    }
    /**
     * @param userDetails
     * @param timestamp
     * @return
     */
    private String rawSignature(User userDetails, long timestamp) {
        StringBuilder signatureBuilder = new StringBuilder();
        signatureBuilder.append(userDetails.getUsername());
        signatureBuilder.append(":");
        signatureBuilder.append(timestamp);
        signatureBuilder.append(":");
        signatureBuilder.append(userDetails.getSalt());
        signatureBuilder.append(":");
        signatureBuilder.append(serverSalt);
        return signatureBuilder.toString();
    }

	@Override
	public boolean validateToken(String token, User userDetails) {
		 if (token == null || userDetails == null) {
			 return false;
		 }
	     
		 String[] parts = token.split(":");
	     long timestamp = Long.parseLong(parts[1], RADIX);
	     String authTokenSignature = parts[2];

	     long expires = timestamp + 1000L * 60 * tokenActiveDuration;
	     if (expires < System.currentTimeMillis()) {
	    	 return false;
	     }

	     return this.passwordEncoder.matches(rawSignature(userDetails, timestamp), authTokenSignature);
	}
	
	@Override
	public String getUsername(String token) {
		if(token == null) {
			return null;
		}

        String[] parts = token.split(":");
        
        return parts[0];
	}

	@Override
	public Date getTimestamp(String token) {
		if(token == null) {
	        return null;
	    }
		
	    String[] parts = token.split(":");
	    long timestamp = Long.parseLong(parts[1], RADIX);
	      
	    return new Date(timestamp);
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