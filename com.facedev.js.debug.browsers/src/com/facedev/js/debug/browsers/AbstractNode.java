package com.facedev.js.debug.browsers;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Abstract tree node for the browsers view.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
abstract class AbstractNode implements IAdaptable {
	private String name;
	private AbstractParentNode parent;
	
	AbstractNode(String name) {
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	void setParent(AbstractParentNode parent) {
		this.parent = parent;
	}
	
	AbstractParentNode getParent() {
		return parent;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		return null;
	}
}