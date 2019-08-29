package com.poscoict.posledger.assets;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.poscoict.posledger.assets.model.User;
import com.poscoict.posledger.assets.model.vo.AccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
@PropertySource("classpath:global.properties")
public class TokenIssuer {
	
	public static final String HEADER_PREFIX = "Bearer ";
	
	@Value("${secret}")
	private String secret;
	
	public AccessToken generateAuthenticateToken(User user) throws JWTCreationException {
		Date issueDate = new Date(System.currentTimeMillis());
		Date expireDate = new Date(System.currentTimeMillis() + AccessToken.expireTime);
		
	    String token = JWT.create()
                .withSubject(user.getId())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(secret));
	    log.info("token : " + token);
	    
	    return AccessToken.builder().type("BEARER").token(token).issueDate(issueDate).expireDate(expireDate).build();
	}
	
	public String decryptUserAccessToken(String accessToken) throws JWTVerificationException {
		String userId = JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(accessToken)
                .getSubject();
		log.info("userId : " + userId);
		return userId;
	}
}
