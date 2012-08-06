package com.facedev.js.debug.browsers;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract non-leaf node.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
abstract class AbstractParentNode extends AbstractNode {
	
	private List<AbstractNode> children;

	AbstractParentNode(String name) {
		super(name);
		children = new ArrayList<AbstractNode>();
	}
	
	void addChild(AbstractNode child) {
		children.add(child);
		child.setParent(this);
	}
	
	void removeChild(AbstractNode child) {
		children.remove(child);
		child.setParent(null);
	}
	
	AbstractNode [] getChildren() {
		return children.toArray(new AbstractNode[children.size()]);
	}
	
	boolean hasChildren() {
		return !children.isEmpty();
	}

	/**
	 * 
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	static class RootNode extends AbstractParentNode {

		RootNode() {
			super("");
		}
		
	}
}
