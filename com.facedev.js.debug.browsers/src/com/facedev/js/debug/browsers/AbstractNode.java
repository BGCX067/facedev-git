package com.facedev.js.debug.browsers;

import org.eclipse.core.runtime.IAdaptable;

/**
 * Abstract tree node for the browsers view.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
abstract class AbstractNode implements IAdaptable {
	private final Object id;
	private volatile String name;
	private volatile AbstractParentNode parent;
	
	AbstractNode(Object id, String name) {
		this.id = id;
		this.name = name;
	}
	
	String getName() {
		return name;
	}
	
	void setName(String name) {
		this.name = name;
	}
	
	void setParent(AbstractParentNode parent) {
		this.parent = parent;
	}
	
	AbstractParentNode getParent() {
		return parent;
	}
	
	Object getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		return null;
	}
}