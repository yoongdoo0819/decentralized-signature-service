package com.poscoict.posledger.assets.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.poscoict.posledger.assets.TokenIssuer;
import com.poscoict.posledger.assets.exception.AccessTokenEmptyException;
import com.poscoict.posledger.assets.exception.AccessTokenInvalidException;

import lombok.extern.slf4j.Slf4j;

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