package com.poscoict.posledger.assets.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.ServletRequest;
import javax.servlet.FilterChain;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(urlPatterns = "/*")
public class HttpFilter implements Filter {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("HttpFilter is initialized ...");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		
		logger.info("request url - " + request.getRequestURL());
		request.setAttribute("ctx", request.getContextPath());
		
		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
		logger.info("HttpFilter is destroyed ...");
	}
}