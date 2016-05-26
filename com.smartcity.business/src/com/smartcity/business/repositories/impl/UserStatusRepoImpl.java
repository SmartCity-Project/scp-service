/**
 * 
 */
package com.smartcity.business.repositories.impl;

import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.UUID;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.mongodb.WriteResult;
import com.smartcity.business.security.authentication.token.IUserStatusController;
import com.smartcity.data.access.User;
import com.smartcity.data.access.token.UserStatus;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;

/**
 * @author gperreas
 *
 */
public class UserStatusRepoImpl
	extends BaseRepositoryImpl<UserStatus>
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<UserStatus> getModelType() {
		return UserStatus.class;
	}
	
	public boolean updateLogoutStatusByUserId(String userId, boolean isLoggedOut) {
		
		Query query = new Query();
		
		WriteResult wr = this.mt.updateFirst(
				query.addCriteria(
						where(DEFAULT_MONGO_KEY_ID).is(userId)),
				Update.update("isLoggedOut", isLoggedOut), 
				getModelType());
		
		return wr.getN() == 1;
	}

	
}
