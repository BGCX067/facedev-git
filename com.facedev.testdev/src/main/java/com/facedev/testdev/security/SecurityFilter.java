package com.facedev.testdev.security;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class SecurityFilter implements Filter {
	
	private final SecurityManager instance = SecurityManager.getInstance();

	public void init(FilterConfig config) throws ServletException {
		instance.init();
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		chain.doFilter(request, response);
	}

	public void destroy() {
		instance.destroy();
	}

}
