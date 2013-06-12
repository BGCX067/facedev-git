package com.facedev.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.osgi.framework.Bundle;

/**
 * Provides useful OSGi platform utilities.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public final class OSGiUtils {
		
	private static final String ROOT_BUNDLE = "org.eclipse.osgi";

	private OSGiUtils() {}
	
	/**
	 * @param extension
	 * @return bundle that provide extension contribution or <code>null</code> if was not succeed to find the bundle.
	 */
	public static Bundle getBundle(IExtension extension) {
		if (extension == null) {
			return null;
		}
		IContributor contributor = extension.getContributor();
		
		if (contributor instanceof RegistryContributor) {
			RegistryContributor regContributor = (RegistryContributor) contributor;
			return getBundle(Long.parseLong(regContributor.getActualId()));
		}
		
		// revert to non-versioned solution
		return Platform.getBundle(contributor.getName());
	}
	
	/**
	 * 
	 * @param id
	 * @return
	 */
	public static Bundle getBundle(long id) {
		return Platform.getBundle(ROOT_BUNDLE).getBundleContext().getBundle(id);
	}
	
	/**
	 * Finds resource in the bundle and returns {@link URL} to it.
	 * If location is empty or <code>null</code> - returns <code>null</code>.
	 * Bundle will be resolved from extension contributor.
	 * 
	 * @param location
	 * @param extension
	 * @return resource {@link URL} in the contributor or <code>null</code> if location is empty or <code>null</code>.
	 */
	public static URL getResource(String location, IExtension extension) {
		return getResource(location, getBundle(extension));
	}
	
	/**
	 * Finds resource in the bundle and returns {@link URL} to it.
	 * If location is empty or <code>null</code> - returns <code>null</code>.
	 * 
	 * @param location
	 * @param bundle
	 * @return resource {@link URL} in the bundle or <code>null</code> if location is empty or <code>null</code>.
	 */
	public static URL getResource(String location, Bundle bundle) {
		if (location == null || location.trim().length() == 0 || bundle == null) {
			return null;
		}
		
		URL result = bundle.getResource(location);
		
		if (result != null) {
			return result;
		}
		
		try {
			return new URL(location);
		} catch (MalformedURLException ex) {
			return null;
		}
	}
}
