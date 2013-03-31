package com.facedev.js.debug.browsers;

import org.eclipse.swt.graphics.Image;

import com.facedev.js.debug.browsers.BrowsersViewContentProvider.IconHolder;


/**
 * Represents node that corresponds to some browser debugger.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class DebuggerNode extends AbstractParentNode implements IconHolder {
	
	private final Image icon;

	DebuggerNode(Object id, String name, Image icon) {
		super(id, name);
		
		this.icon = icon;
	}
	
	/**
	 * @return image object for the icon associated with this node.
	 */
	public Image getIcon() {
		return icon;
	}
}