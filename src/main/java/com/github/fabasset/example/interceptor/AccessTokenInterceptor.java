package com.github.fabasset.example.interceptor;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.github.fabasset.example.TokenIssuer;
import com.github.fabasset.example.exception.AccessTokenEmptyException;
import com.github.fabasset.example.exception.AccessTokenInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class AccessTokenInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private TokenIssuer tokenIssuer;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		String requestAccessToken = (String) request.getSession().getAttribute("accessToken");
		
		log.info("accessToken : " + requestAccessToken);
		
		if (StringUtils.isEmpty(requestAccessToken)) {
			throw new AccessTokenEmptyException("Access Token of session is empty.");
		}
		
		try {
			tokenIssuer.decryptUserAccessToken(requestAccessToken);
		} catch(TokenExpiredException e) {
			throw new TokenExpiredException(e.getLocalizedMessage());
		} catch(Exception e) {
			throw new AccessTokenInvalidException("Your token is invalid.");
		}
		
		return true;
	}
}