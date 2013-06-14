package com.facedev.testdev.ioc;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.facedev.testdev.ioc.annotation.WebMethod;


final class UrlInterseptor {
	
	private final WebMethod meta;
	private final Method method;
	private final Object bean;

	UrlInterseptor(WebMethod meta, Method method, Object bean) {
		this.meta = meta;
		this.method = method;
		this.bean = bean;
	}

	String getUrl() {
		String value = meta.value();
		if (value == null || value.trim().isEmpty()) {
			value = meta.url();
		}
		if (value == null || value.trim().isEmpty()) {
			throw new ContainerException("Unable to attach method: " + method);
		}
		return value;
	}

	void intercept(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			// TODO:
			//  - json parsing and building
			//  - security handling
			//  - transactions handling
			Object result = method.invoke(bean);
			if (result != null) {
				response.getWriter().print(result);
			}
			response.getWriter().close();
		} catch (Exception ex) {
			response.setStatus(500);
		}
	}

}
