/**
 * 
 */
package com.smartcity.business.security.authentication;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.mongodb.WriteResult;
import com.smartcity.business.security.authentication.token.ITokenController;
import com.smartcity.business.security.authentication.token.IUserStatusController;

import com.smartcity.business.repositories.impl.UserStatusRepoImpl;
import com.smartcity.data.access.User;
import com.smartcity.data.access.token.TokenInfo;
import com.smartcity.data.access.token.UserStatus;

/**
 * @author gperreas
 *
 */
@Component
public class UserStatusControllerImpl
	implements IUserStatusController 
{
	
    @Autowired
    protected ITokenController tokenController;

    @Autowired
    protected UserStatusRepoImpl userStatusRepoImpl;
    
	@Override
	public UserStatus getUserStatusByUserId(String userId) {
		Assert.notNull(userId);
        return userStatusRepoImpl.findOneByObjectId(userId);
	}
	
	@Override
	public boolean updateLogoutSuccess(User userDetails, boolean isLoggedOut) {

		return userStatusRepoImpl.updateLogoutStatusByUserId(userDetails.getId(), isLoggedOut);
	}

	@Override
	public UserStatus updateLoginAuthenticationSuccess(
			User userDetails, 
			WebAuthenticationDetails webAuthenticationDetails) {
		
		UserStatus userStatus = userStatusRepoImpl.findOneByObjectId(userDetails.getId());
        if (userStatus == null) {
        	userStatus = new UserStatus();
        	userStatus.setId(userDetails.getId());
        	userStatus.setEmailVerified(true);
        	userStatus.setRegistered(true);    
        }

        TokenInfo token = tokenController.generateToken(userDetails);

        userStatus.setTokenInfo(token);
        userStatus.setLoggedOut(false);
        userStatusRepoImpl.save(userStatus);
        
        return userStatus;
	}

	@Override
	public UserStatus updateTokenAuthenticationSuccess(
			User userDetails, 
			WebAuthenticationDetails webAuthenticationDetails) {

		UserStatus userStatus = userStatusRepoImpl.findOneByObjectId(userDetails.getId());
        
        return userStatus;
	}


	@Override
	public UserStatus updateLoginAuthenticationFailure(
			String userId, 
			WebAuthenticationDetails webAuthenticationDetails) {
		
		UserStatus userStatus = userStatusRepoImpl.findOneByObjectId(userId);
        if (userStatus == null) {
        	userStatus = new UserStatus();
            userStatus.setId(userId);
        }
		
//		AuthenticationTraking logingAuthTraking = addAuthenticationAttempt(userStatus.getLogingAuthTraking(), new AuthenticationAttempt(webAuthenticationDetails.getRemoteAddress(), new Date(), AuthenticationAttemptStatus.FAILED));
//
//	    // fail count
//	    logingAuthTraking.setFailCount(logingAuthTraking.getFailCount() + 1);
//
//	    // lockout type
//	    LockoutType lockoutType = null;
//	    int expectedLockoutDuration = 0;
//	    Date lockoutExperation = null;
//
//	    if (isLockoutEnabled && logingAuthTraking.getFailCount() == totalAttempts) {
//	        lockoutType = LockoutType.LOCKED;
//	    } else if (isTemporaryLockoutEnabled) { 
//	        if (logingAuthTraking.getFailCount() >= lockoutAttempts) {
//	            lockoutType = LockoutType.TEMPORARY_LOCKED;
//	            expectedLockoutDuration = lockoutDuration;
//	        }
//	        if (logingAuthTraking.getFailCount() >= lockoutAttempts + lockoutAttemptsThreshold) {
//	            expectedLockoutDuration += lockoutDurationThreshold;
//	        }
//	        lockoutExperation = DateUtils.addMinutes(new Date(), expectedLockoutDuration);
//	    }
//
//	    userStatus.setAccountToken(null);
//	    userStatus.setLogingAuthTraking(logingAuthTraking);
//	    userStatus.setLockoutExperation(lockoutExperation);
//	    userStatus.setLockoutType(lockoutType);
	    
	    userStatusRepoImpl.save(userStatus);
	    
	    return userStatus;		
	}

}
