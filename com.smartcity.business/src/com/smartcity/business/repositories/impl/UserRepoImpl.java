/**
 * 
 */
package com.smartcity.business.repositories.impl;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.WriteResult;
import com.smartcity.business.security.authentication.token.IUserRepo;
import com.smartcity.data.access.User;

/**
 * @author gperreas
 *
 */
public class UserRepoImpl 
	extends BaseRepositoryImpl<User>
	implements IUserRepo 
{

	private static final long serialVersionUID = 1L;

	@Override
	protected Class<User> getModelType() {
		return User.class;
	}

	@Override
	public User findByUsername(String username) {
	
		return this.mt.findOne(new Query(Criteria.where("username").is(username)),
				getModelType());
	}	
	
	@Override
	public User findByEmail(String email) {
	
		return this.mt.findOne(new Query(Criteria.where("email").is(email)),
				getModelType());
	}
	
	@Override
	public boolean changePassword(String oldPassword, String newPassword, String username) {
		Query query = new Query(
				Criteria.where("username").is(username)
						.andOperator(Criteria.where("password").is(oldPassword)));
        WriteResult wr = mt.updateFirst(query, Update.update("password", newPassword), User.class);
        return wr.getN() == 1;
	}
	
	
	/**
	 * Total users with role: ROLE_USER
	 * @return count
	 */
	public long countUsers() {
		return super.count(new Query(
				Criteria.where("roles").elemMatch(
						Criteria.where(DEFAULT_REF_MONGO_KEY_ID).is("ROLE_USER"))));
	}
}
