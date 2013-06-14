package com.facedev.testdev.security;

import com.facedev.testdev.ioc.annotation.FactoryMethod;

public final class SecurityManager {
	private static class SecurityManagerHolder {
        static final SecurityManager INSTANCE = new SecurityManager();
    }
	
	private SecurityManager() {}
	
	@FactoryMethod
	static SecurityManager getInstance() {
		return SecurityManagerHolder.INSTANCE;
	}

	void init() {
		// TODO Auto-generated method stub
		
	}

	void destroy() {
		// TODO Auto-generated method stub
		
	}
}

