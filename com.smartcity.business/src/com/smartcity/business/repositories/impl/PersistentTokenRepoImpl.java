package com.smartcity.business.repositories.impl;

import java.util.Date;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

import com.smartcity.data.access.PersistentToken;

/**
 * @author gperreas
 * 
 */
public class PersistentTokenRepoImpl
	extends BaseRepositoryImpl<PersistentToken>
	implements PersistentTokenRepository
{

	private static final long serialVersionUID = 1L;

	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		 PersistentToken mongoToken = new PersistentToken(token);
	     mt.insert(mongoToken);
	}

	@Override
	public PersistentToken getTokenForSeries(String series) {
		Query query = Query.query(Criteria.where("series").is(series));
        return mt.findOne(query, PersistentToken.class);
	}

	@Override
	public void removeUserTokens(String username) {
		Query query = Query.query(Criteria.where("username").is(username));
        mt.remove(query, PersistentToken.class);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		Query query = Query.query(Criteria.where("series").is(series));
	    Update update = Update.update("tokenValue", tokenValue).set("lastUsed", lastUsed);
	    mt.updateFirst(query, update, PersistentToken.class);
	}

	@Override
	protected Class<PersistentToken> getModelType() {
		return PersistentToken.class;
	}

}
