/**
 * 
 */
package com.smartcity.business.security.authentication.token;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.common.util.SerializationUtils;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import com.smartcity.business.repositories.impl.OAuth2AccessTokenRepoImpl;
import com.smartcity.business.repositories.impl.OAuth2RefreshTokenRepoImpl;


/**
 * @author gperreas
 *
 */
@Component
public class LocalTokenStore 
	implements TokenStore
{
	private final Logger log = LoggerFactory.getLogger(LocalTokenStore.class.getName());
	
	private OAuth2AccessTokenRepoImpl accessTokenRepo;
	
    private OAuth2RefreshTokenRepoImpl refreshTokenRepo;
	
    private AuthenticationKeyGenerator authKeyGenerator;
 	
    public LocalTokenStore() {};
    
    @Autowired
	public LocalTokenStore(
			OAuth2AccessTokenRepoImpl accessTokenRepository,
			OAuth2RefreshTokenRepoImpl refreshTokenRepository,
			AuthenticationKeyGenerator authKeyGenerator) {
		this.accessTokenRepo = accessTokenRepository;
		this.refreshTokenRepo = refreshTokenRepository;
		this.authKeyGenerator = authKeyGenerator;
	}

	@Override
	public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
		return readAuthentication(token.getValue());
	}

	@Override
	public OAuth2Authentication readAuthentication(String token) {

		String tokenId = extractTokenKey(token);

        com.smartcity.data.access.token.OAuth2AccessToken accessToken = 
        		accessTokenRepo.findByTokenId(tokenId);

        if (accessToken != null) {
            try {
                return deserializeAuthentication(accessToken.getAuthentication());
            } catch (IllegalArgumentException e) {
                removeAccessToken(token);
            }
        }

        return null;
	}

	@Override
	public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
		String rToken = null;
		
		if(token.getRefreshToken()!=null)
			rToken = token.getRefreshToken().getValue();
		
		if(readAccessToken(token.getValue())!=null)
			removeAccessToken(token.getValue());
		
		final String tokenKey = extractTokenKey(token.getValue());

		com.smartcity.data.access.token.OAuth2AccessToken accessToken = new 
				com.smartcity.data.access.token.OAuth2AccessToken();  
		
		accessToken.setTokenId(tokenKey);
		accessToken.setClientId(authentication.getOAuth2Request().getClientId());
		accessToken.setAuthenticationId(authKeyGenerator.extractKey(authentication));
		accessToken.setUsername(authentication.isClientOnly() ? null : authentication.getName());
		accessToken.setToken(serializeAccessToken(token));
		accessToken.setRefreshToken(extractTokenKey(rToken));
		accessToken.setAuthentication(serializeAuthentication(authentication));

        accessTokenRepo.save(accessToken);
	}

	@Override
	public OAuth2AccessToken readAccessToken(String tokenValue) {
		String tokenKey = extractTokenKey(tokenValue);
		com.smartcity.data.access.token.OAuth2AccessToken accessToken = accessTokenRepo.findByTokenId(tokenKey);
        if (accessToken != null) {
            try {
                return deserializeAccessToken(accessToken.getToken());
            } catch (IllegalArgumentException e) {
                removeAccessToken(tokenValue);
            }
        }
        return null;
	}

	@Override
	public void removeAccessToken(OAuth2AccessToken token) {
		removeAccessToken(token.getValue());
	}

	@Override
	public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
		String tokenId = extractTokenKey(refreshToken.getValue());
        byte[] token = serializeRefreshToken(refreshToken);
        byte[] auth = serializeAuthentication(authentication);

        com.smartcity.data.access.token.OAuth2RefreshToken rToken = 
        		new com.smartcity.data.access.token.OAuth2RefreshToken();
        
        rToken.setTokenId(tokenId);
        rToken.setToken(token);
        rToken.setAuthentication(auth);

        refreshTokenRepo.save(rToken);
	}

	@Override
	public OAuth2RefreshToken readRefreshToken(String tokenValue) {
		String tokenKey = extractTokenKey(tokenValue);
		com.smartcity.data.access.token.OAuth2RefreshToken rToken = refreshTokenRepo.findByTokenId(tokenKey);
        if (rToken != null) {
            try {
                return deserializeRefreshToken(rToken.getToken());
            } catch (IllegalArgumentException e) {
                removeAccessToken(tokenValue);
            }
        }
        return null;
	}

	@Override
	public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {
		return readAuthenticationForRefreshToken(token.getValue());
	}

	@Override
	public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
		removeRefreshToken(refreshToken.getValue());
	}

	@Override
	public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
		removeAccessTokenUsingRefreshToken(refreshToken.getValue());
	}

	@Override
	public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
		OAuth2AccessToken accessToken = null;

        String authId = authKeyGenerator.extractKey(authentication);

        com.smartcity.data.access.token.OAuth2AccessToken aToken = 
        		accessTokenRepo.findByAuthenticationId(authId);

        if (aToken != null) {
            accessToken = deserializeAccessToken(aToken.getToken());
        }

        if (accessToken != null
                && !authId.equals(
                		authKeyGenerator.extractKey(
                				readAuthentication(accessToken.getValue())))) {
            removeAccessToken(accessToken.getValue());
            // Keep the store consistent (maybe the same user is represented by this authentication but the details have
            // changed)
            storeAccessToken(accessToken, authentication);
        }
        return accessToken;
    }

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {

		List<com.smartcity.data.access.token.OAuth2AccessToken> aTokens = 
				accessTokenRepo.findByUsernameAndClientId(userName, clientId);
        Collection<com.smartcity.data.access.token.OAuth2AccessToken> noNullsTokens =
        		Collections2.filter(aTokens, byNotNulls());
        return Collections2.transform(noNullsTokens, toOAuth2AccessToken());
	}

	@Override
	public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
		List<com.smartcity.data.access.token.OAuth2AccessToken> aTokens = 
				accessTokenRepo.findByClientId(clientId);
        Collection<com.smartcity.data.access.token.OAuth2AccessToken> noNullTokens = 
        		Collections2.filter(aTokens, byNotNulls());
        return Collections2.transform(noNullTokens, toOAuth2AccessToken());
	}
	
    private Predicate<com.smartcity.data.access.token.OAuth2AccessToken> byNotNulls() {
        return new Predicate<com.smartcity.data.access.token.OAuth2AccessToken>() {
            @Override
            public boolean apply(com.smartcity.data.access.token.OAuth2AccessToken token) {
                return token != null;
            }
        };
    }

    private Function<com.smartcity.data.access.token.OAuth2AccessToken, OAuth2AccessToken> toOAuth2AccessToken() {
        return new Function<com.smartcity.data.access.token.OAuth2AccessToken, OAuth2AccessToken>() {
            @Override
            public OAuth2AccessToken apply(com.smartcity.data.access.token.OAuth2AccessToken token) {
                return SerializationUtils.deserialize(token.getToken());
            }
        };
    }
	
	protected byte[] serializeAccessToken(OAuth2AccessToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeRefreshToken(OAuth2RefreshToken token) {
        return SerializationUtils.serialize(token);
    }

    protected byte[] serializeAuthentication(OAuth2Authentication authentication) {
        return SerializationUtils.serialize(authentication);
    }

    protected OAuth2AccessToken deserializeAccessToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2RefreshToken deserializeRefreshToken(byte[] token) {
        return SerializationUtils.deserialize(token);
    }

    protected OAuth2Authentication deserializeAuthentication(byte[] authentication) {
        return SerializationUtils.deserialize(authentication);
    }
	
    private void removeAccessToken(String value) {
		String tokenId = extractTokenKey(value);
		accessTokenRepo.deleteByTokenId(tokenId);
	}

	private void removeRefreshToken(String token) {
        String tokenId = extractTokenKey(token);
        refreshTokenRepo.deleteByTokenId(tokenId);
    }

	private void removeAccessTokenUsingRefreshToken(String refreshToken) {
        String tokenId = extractTokenKey(refreshToken);
        accessTokenRepo.deleteByRefreshTokenId(tokenId);
    }
    
    private OAuth2Authentication readAuthenticationForRefreshToken(String value) {
        String tokenId = extractTokenKey(value);

        com.smartcity.data.access.token.OAuth2RefreshToken rToken = 
        		refreshTokenRepo.findByTokenId(tokenId);

        if (rToken != null) {
            try {
                return deserializeAuthentication(rToken.getAuthentication());
            } catch (IllegalArgumentException e) {
                removeRefreshToken(value);
            }
        }

        return null;
    }

    private String extractTokenKey(String value) {
    	 if (value == null) {
             return null;
         }
         MessageDigest digest;
         try {
             digest = MessageDigest.getInstance("MD5");
         }
         catch (NoSuchAlgorithmException e) {
             throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
         }

         try {
             byte[] bytes = digest.digest(value.getBytes("UTF-8"));
             return String.format("%032x", new BigInteger(1, bytes));
         }
         catch (UnsupportedEncodingException e) {
             throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
         }
	}

	
}
