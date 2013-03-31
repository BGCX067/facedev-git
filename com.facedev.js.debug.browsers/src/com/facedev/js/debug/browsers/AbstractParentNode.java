package com.facedev.js.debug.browsers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract non-leaf node.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
abstract class AbstractParentNode extends AbstractNode {
	
	private final List<AbstractNode> children;

	AbstractParentNode(Object id, String name) {
		super(id, name);
		children = Collections.synchronizedList(new ArrayList<AbstractNode>());
	}
	
	void addChild(AbstractNode child) {
		children.add(child);
		child.setParent(this);
	}
	
	void removeChild(AbstractNode child) {
		children.remove(child);
		child.setParent(null);
	}
	
	AbstractNode findChild(Object id) {
		if (id == null) {
			return null;
		}
		for (AbstractNode child : children) {
			if (child.getId() != null && child.getId().equals(id)) {
				return child;
			}
		}
		return null;
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
			super("ROOT", "");
		}
		
	}
}
