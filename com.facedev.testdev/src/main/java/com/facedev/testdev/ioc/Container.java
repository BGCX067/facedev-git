package com.facedev.testdev.ioc;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.ServletContext;

import com.facedev.testdev.ioc.annotation.Controller;
import com.facedev.testdev.ioc.annotation.FactoryMethod;
import com.facedev.testdev.ioc.annotation.WebMethod;
import com.facedev.testdev.security.SecurityManager;


public final class Container {
	
	private static class ContainerHolder {
        static final Container INSTANCE = new Container();
    }
	
	private volatile Configuration config;
	
	private final ConcurrentMap<String, Object> beans;
	
	private final BeanLoadingChain next;
	
	private volatile DispatcherServlet dispatcher;
	
	private Container() {
		beans = new ConcurrentHashMap<String, Object>();
		next = new ControllersLoader();
	}

	static Container get() {
		return ContainerHolder.INSTANCE;
	}

	void init(ServletContext servletContext, DispatcherServlet dispatcher) {
		config = Configuration.load(servletContext);
		this.dispatcher = dispatcher;
		loadBeans(config.getConfiguredClassNames(), Thread.currentThread().getContextClassLoader());
	}

	void destroy() {
		
	}

	private void loadBeans(List<String> names, ClassLoader classLoader) {
		instantiateBeans(names, classLoader);
		loadBeans(beans.values());
	}

	private void instantiateBeans(List<String> names, ClassLoader classLoader) {
		for (String name : names) {
			instantiateBean(name, classLoader);
		}
		instantiateBean(SecurityManager.class.getName(), classLoader);
	}

	private void loadBeans(Collection<Object> beans) {
		for (Object bean : beans) {
			loadBean(bean, bean.getClass());
		}
	}

	private void loadBean(Object bean, Class<?> clazz) {
		try {
			next.loadBean(clazz, bean);
		} catch (ContainerException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ContainerException("Unable to load bean of class: " + clazz, ex);
		}
	}

	private void instantiateBean(String name, ClassLoader classLoader) {
		try {
			Class<?> clazz = classLoader.loadClass(name);
			Object bean = instantiateBean(clazz);
			beans.put(clazz.getCanonicalName(), bean);
		} catch (ContainerException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new ContainerException("Unable to instantiate bean of class: " + name, ex);
		}
	}

	private Object instantiateBean(Class<?> clazz) throws Exception {
		Method factory = findFactoryMethod(clazz);
		if (factory == null) {
			return clazz.newInstance();
		}
		try {
			factory.setAccessible(true);
			return factory.invoke(null);
		} finally {
			factory.setAccessible(false);
		}
	}
	
	private Method findFactoryMethod(Class<?> clazz) {
		Method result = null;
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.getAnnotation(FactoryMethod.class) == null) {
				continue;
			}
			if (result != null) {
				throw new ContainerException(
						"More than one factory method in the class: " + clazz);
			}
			result = method;
		}
		return result;
	}

	private class BeanLoadingChain {
		
		private final BeanLoadingChain next;
		
		BeanLoadingChain(BeanLoadingChain next) {
			this.next = next;
		}
		
		void loadBean(Class<?> clazz, Object bean) throws Exception {
			if (next != null) {
				loadBean(clazz, bean);
			}
		}
	}

	private class ControllersLoader extends BeanLoadingChain {
		ControllersLoader() {
			super(null);
		}
		
		@Override
		void loadBean(Class<?> clazz, Object bean) throws Exception {
			Controller controller = clazz.getAnnotation(Controller.class);
			if (controller != null) {
				initController(clazz, bean, controller);
			}
			super.loadBean(clazz, bean);
		}

		private void initController(Class<?> clazz, Object bean, Controller controller) {
			for (Method method : clazz.getMethods()) {
				declareWebMethod(method, bean);
			}
		}

		private void declareWebMethod(Method method, Object bean) {
			WebMethod meta = method.getAnnotation(WebMethod.class);
			if (meta == null) {
				return;
			}
			declareWebMethod(meta, method, bean);
		}

		private void declareWebMethod(WebMethod meta, Method method, Object bean) {
			dispatcher.registerInterseptor(new UrlInterseptor(meta, method, bean));
		}
	}
}
