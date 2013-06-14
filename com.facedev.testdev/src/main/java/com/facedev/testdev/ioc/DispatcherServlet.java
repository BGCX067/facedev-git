package com.facedev.testdev.ioc;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public final class DispatcherServlet extends HttpServlet {
	private static final long serialVersionUID = 4651799999512347903L;
	
	private static final Container containter = Container.get();
	
	private final ConcurrentHashMap<String, UrlInterseptor> interseptors = new ConcurrentHashMap<String, UrlInterseptor>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		containter.init(config.getServletContext(), this);
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (intercept(request, response)) {
			return;
		}
		response.setStatus(404);
		response.getWriter().close();
	}

	private boolean intercept(HttpServletRequest request,
			HttpServletResponse response) {
		String path = getContextPath(request);
		
		if (path == null || path.isEmpty()) {
			return false;
		}
		UrlInterseptor interseptor = interseptors.get(path);
		if (interseptor == null) {
			return false;
		}
		interseptor.intercept(request, response);
		return true;
	}

	private String getContextPath(HttpServletRequest request) {
		if (request.getContextPath() == null) {
			return request.getRequestURI();
		}
		return request.getRequestURI().substring(request.getContextPath().length() + 1);
	}

	@Override
	public void destroy() {
		super.destroy();
		containter.destroy();
	}

	void registerInterseptor(UrlInterseptor urlInterseptor) {
		interseptors.put(urlInterseptor.getUrl(), urlInterseptor);
	}	
}
