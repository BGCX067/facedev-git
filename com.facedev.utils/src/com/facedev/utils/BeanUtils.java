package com.facedev.utils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Common utilities to work with java beans.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public final class BeanUtils {
	
	private BeanUtils() {}
	
	/**
	 * Binds sourceProperty of sourceBean to targetProperty of targetBean.
	 * Source and target beans must honor java beans specification.
	 * @param sourceBean
	 * @param targetBean
	 * @param sourceProperty
	 * @param targetProperty
	 * @return PropertyChangeListener registered for binding
	 */
	public static PropertyChangeListener bind(Object sourceBean, final Object targetBean, final String sourceProperty, final String targetProperty) {
		PropertyChangeListener listener = new PropertyChangeListener() {

			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(sourceProperty)) {
					setProperty(targetBean, targetProperty, evt.getNewValue());
				}
			}
			
		};
		addPropertyListener(sourceBean, listener);
		setProperty(targetBean, targetProperty, getProperty(sourceBean, sourceProperty));
		return listener;
	}
	
	/**
	 * Adds PropertyChangeListener to the bean.
	 * @param bean
	 * @param listener
	 */
	public static void addPropertyListener(Object bean, PropertyChangeListener listener) {
		if (bean == null || listener == null) {
			throw new NullPointerException();
		}
		try { 
			Method method = bean.getClass().getMethod("addPropertyChangeListener", PropertyChangeListener.class);
			method.invoke(bean, listener);
		} catch(InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to add PropertyChangeListener beacause passed bean is not a java bean", ex);
		}
	}
	
	/**
	 * Removes PropertyChangeListener previously registered for this bean.
	 * @param bean
	 * @param listener
	 */
	public static void removePropertyListener(Object bean, PropertyChangeListener listener) {
		if (bean == null || listener == null) {
			throw new NullPointerException();
		}
		try { 
			Method method = bean.getClass().getMethod("removePropertyChangeListener", PropertyChangeListener.class);
			method.invoke(bean, listener);
		} catch(InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to remove PropertyChangeListener beacause passed bean is not a java bean", ex);
		}
	}
	
	/**
	 * Sets propperty with name specified on bean passed to the value provided.
	 * @param bean
	 * @param property
	 * @param value
	 */
	public static void setProperty(Object bean, String property, Object value) {
		if (bean == null || property == null) {
			throw new NullPointerException();
		}
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(property, bean.getClass());
			descriptor.getWriteMethod().invoke(bean, value);
		} catch(InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to remove PropertyChangeListener beacause passed bean is not a java bean", ex);
		}
	}
	
	/**
	 * @param bean
	 * @param property
	 * @return value of the property on the bean.
	 */
	public static Object getProperty(Object bean, String property) {
		if (bean == null || property == null) {
			throw new NullPointerException();
		}
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(property, bean.getClass());
			return descriptor.getReadMethod().invoke(bean);
		} catch(InvocationTargetException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new IllegalArgumentException("Unable to remove PropertyChangeListener beacause passed bean is not a java bean", ex);
		}
	}
}
