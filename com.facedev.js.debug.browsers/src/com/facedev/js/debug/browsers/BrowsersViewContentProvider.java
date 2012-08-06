package com.facedev.js.debug.browsers;

import java.net.URL;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;
import com.facedev.js.debug.JsDebuggersManager;
import com.facedev.utils.OSGiUtils;

/**
 * Provides data for Browser View
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
class BrowsersViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider {

	private JsDebuggersManager debuggerManager;
	private AbstractParentNode.RootNode invisibleRoot;
	private BrowsersView owner;
	
	BrowsersViewContentProvider(BrowsersView owner) {
		this.owner = owner;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
		if (parent.equals(owner.getViewSite())) {
			if (invisibleRoot == null)
				initialize();
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public Object getParent(Object child) {
		if (child instanceof AbstractNode) {
			return ((AbstractNode) child).getParent();
		}
		return null;
	}

	public Object[] getChildren(Object parent) {
		if (parent instanceof AbstractParentNode) {
			return ((AbstractParentNode) parent).getChildren();
		}
		return new Object[0];
	}

	public boolean hasChildren(Object parent) {
		if (parent instanceof AbstractParentNode)
			return ((AbstractParentNode) parent).hasChildren();
		return false;
	}

	private void initialize() {
		invisibleRoot = new AbstractParentNode.RootNode();

		if (getDebuggerManager() == null) {
			return;
		}
		
		for (JsDebugger debugger : getDebuggerManager().getDebuggers()) {
			if (!debugger.isSupported()) {
				continue;
			}
			addDebugger(debugger);
		}
	}

	private void addDebugger(JsDebugger debugger) {
		try {
			AbstractParentNode parent = new DebuggerNode(debugger.getName(), createIcon(debugger));
			
			for (JsDebuggerInstance instance : debugger.getRegisteredInstances()) {
				AbstractNode leaf = new DebuggerInstanceNode(instance.getName());
				parent.addChild(leaf);
			}
			invisibleRoot.addChild(parent);
		} catch (JsDebuggerException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	private Image createIcon(JsDebugger debugger) {
		IExtension extension = getDebuggerManager().getExtension(debugger);
		if (extension == null) {
			return null;
		}
		IConfigurationElement config = extension.getConfigurationElements()[0];
		URL url = OSGiUtils.getResource(config.getAttribute("icon"), extension);
		
		if (url == null) {
			return null;
		}
		
		return ImageDescriptor.createFromURL(url).createImage();
	}

	private JsDebuggersManager getDebuggerManager() {
		if (debuggerManager == null) {
			debuggerManager = Activator.getDebuggerManager();
		}
		return debuggerManager;
	}
	
	/**
	 * Provides labels for the tree.
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	static class ViewLabelProvider extends LabelProvider {
		
		private static String DEFAULT_LEAF_ICON = "icons/defaultinstance.png";

		public String getText(Object obj) {
			if (obj instanceof AbstractNode) {
				return ((AbstractNode)obj).getName();
			}
			return obj.toString();
		}
		
		public Image getImage(Object obj) {
			if (!(obj instanceof IconHolder)) {
				return getDefaultImage(obj);
			}
			Image result = ((IconHolder)obj).getIcon();
			
			if (result == null) {
				return getDefaultImage(obj);
			}
			
			return result;
		}

		private Image getDefaultImage(Object obj) {
			if (obj instanceof AbstractParentNode) {
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			}
			
			URL url = Activator.getBundleContext().getBundle().getResource(DEFAULT_LEAF_ICON);
			return ImageDescriptor.createFromURL(url).createImage();
		}
	}
	
	/**
	 * Instances of this interface may provide icons images.
	 * 
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	static interface IconHolder {
		
		/**
		 * @return icon image or <code>null</code> if default should be used.
		 */
		Image getIcon();
	}
}
