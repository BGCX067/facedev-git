package com.facedev.js.parser;

import java.io.File;
import java.util.List;

/**
 * Descriptor for whole compilation unit (mapped one to one to *.js file).
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsCompilationUnitDescriptor extends JsDescriptor {
	/**
	 * @return name of the file this compilation unit compiled from.
	 */
	String getName();
	
	/**
	 * @return location of the file this compilation unit compiled from.
	 */
	File getPath();
	
	/**
	 * @return descriptors this compilation unit consists of.
	 */
	List<JsDescriptor> getDescriptors();
}
