package com.smartcity.data.access;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;


/**
 * @author gperreas
 * 
 */
@Document(collection = "persistentlogins")
@CompoundIndexes({
        @CompoundIndex(name = "username", def = "{ 'username' : 1 }"),
        @CompoundIndex(name = "series", def = "{ 'series' : 1 }", unique = true)
})
public class PersistentToken
	extends PersistentRememberMeToken
{
	@Id
    private String id;

	public PersistentToken() {
		this(null, null, null, null);
	}
	
	public PersistentToken(PersistentRememberMeToken token) {
		super(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
	}
	
	
	public PersistentToken(String email, String series, String tokenValue, Date date) {
		super(email, series, tokenValue, date);
	}

	public String getId() {
		return id;
	}
	
}
