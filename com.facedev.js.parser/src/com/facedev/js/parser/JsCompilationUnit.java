package com.facedev.js.parser;

import java.net.URI;
import java.util.List;

/**
 * Descriptor for whole compilation unit (mapped typically one to one to *.js file).
 * Moreover this node represents the root of abstract syntax tree of javascript document.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsCompilationUnit extends JsSyntaxNode {
	/**
	 * @return name of the file this compilation unit compiled from.
	 */
	String getName();
	
	/**
	 * @return location of the resource this compilation unit compiled from.
	 */
	URI getLocation();
	
	/**
	 * @return descriptors this compilation unit consists of.
	 */
	List<JsSyntaxNode> getNodes();
}
