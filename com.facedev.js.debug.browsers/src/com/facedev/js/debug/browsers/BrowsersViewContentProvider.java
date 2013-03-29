package com.facedev.js.debug.browsers;

import java.net.URL;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.facedev.js.debug.JsDebugger;
import com.facedev.js.debug.JsDebuggerChangeListener;
import com.facedev.js.debug.JsDebuggerException;
import com.facedev.js.debug.JsDebuggerInstance;
import com.facedev.js.debug.JsDebuggerInstanceListener;
import com.facedev.js.debug.JsDebuggersManager;
import com.facedev.utils.OSGiUtils;

/**
 * Provides data for Browser View
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
class BrowsersViewContentProvider implements IStructuredContentProvider,
		ITreeContentProvider, JsDebuggerChangeListener, JsDebuggerInstanceListener {

	private volatile AbstractParentNode.RootNode invisibleRoot;
	private final BrowsersView owner;
	private final TreeViewer viewer;
	
	BrowsersViewContentProvider(BrowsersView owner, TreeViewer viewer) {
		this.owner = owner;
		this.viewer = viewer;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public synchronized void dispose() {
		JsDebuggersManager manager = Activator.getDebuggerManager();
		
		if (manager == null) {
			return;
		}
		
		manager.removeJsDebuggerChangeListener(this);
	}

	public synchronized Object[] getElements(Object parent) {
		if (parent.equals(owner.getViewSite())) {
			if (invisibleRoot == null) {
				initialize();
			}
			return getChildren(invisibleRoot);
		}
		return getChildren(parent);
	}

	public synchronized Object getParent(Object child) {
		if (child instanceof AbstractNode) {
			return ((AbstractNode) child).getParent();
		}
		return null;
	}

	public synchronized Object[] getChildren(Object parent) {
		if (parent instanceof AbstractParentNode) {
			return ((AbstractParentNode) parent).getChildren();
		}
		return new Object[0];
	}

	public synchronized boolean hasChildren(Object parent) {
		if (parent instanceof AbstractParentNode)
			return ((AbstractParentNode) parent).hasChildren();
		return false;
	}

	public synchronized void onJsDebuggerChange(JsDebugger debugger, State state) {
		if (state.equals(JsDebuggerChangeListener.State.ADDED)) {
			addDebugger(debugger);
		} else if (state.equals(JsDebuggerChangeListener.State.REMOVED)) {
			removeDebugger(debugger);
		}
		requestForRefresh();
	}

	public synchronized void onInstancesChanged() {
		requestForRefresh();
	}

	private void requestForRefresh() {
		Display.getDefault().asyncExec(new Runnable() {
			
			public void run() {
				viewer.refresh();
			}
		});
	}

	public synchronized void onInstanceAdd(JsDebuggerInstance instance) {
		DebuggerNode debuggerNode = (DebuggerNode)invisibleRoot.findChild(instance.getDebugger().getID());
		if (debuggerNode == null) {
			return;
		}
		debuggerNode.addChild(new DebuggerInstanceNode(instance.getID(), instance.getName()));
	}

	public synchronized void onInstanceRemove(JsDebuggerInstance instance) {
		DebuggerNode debuggerNode = (DebuggerNode)invisibleRoot.findChild(instance.getDebugger().getID());
		if (debuggerNode == null) {
			return;
		}
		AbstractNode child = debuggerNode.findChild(instance.getID());
		if (child != null) {
			debuggerNode.removeChild(child);
		}
	}

	public synchronized void onInstanceChange(JsDebuggerInstance instance) {
		DebuggerNode debuggerNode = (DebuggerNode)invisibleRoot.findChild(instance.getDebugger().getID());
		if (debuggerNode == null) {
			return;
		}
		DebuggerInstanceNode child = (DebuggerInstanceNode)debuggerNode.findChild(instance.getID());
		if (child != null) {
			child.setName(instance.getName());
		}
	}

	private void initialize() {
		invisibleRoot = new AbstractParentNode.RootNode();

		JsDebuggersManager manager = Activator.getDebuggerManager();
		
		if (manager == null) {
			return;
		}
		
		for (JsDebugger debugger : manager.getDebuggers()) {
			if (!debugger.isSupported()) {
				continue;
			}
			addDebugger(debugger);
		}
		
		manager.addJsDebuggerChangeListener(this);
	}

	private void addDebugger(JsDebugger debugger) {
		if (!debugger.isSupported()) {
			return;
		}
		try {
			AbstractParentNode parent = new DebuggerNode(debugger.getID(), debugger.getName(), createIcon(debugger));
			
			for (JsDebuggerInstance instance : debugger.getRegisteredInstances()) {
				AbstractNode leaf = new DebuggerInstanceNode(instance.getID(), instance.getName());
				parent.addChild(leaf);
			}
			invisibleRoot.addChild(parent);
			debugger.addInstanceListener(this);
			
		} catch (JsDebuggerException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void removeDebugger(JsDebugger debugger) {
		debugger.removeInstanceListener(this);
		AbstractNode[] children = invisibleRoot.getChildren();
		if (children == null || children.length == 0) {
			return;
		}
		for (AbstractNode node : children) {
			if ((node instanceof DebuggerNode) && 
					node.getName().equals(debugger.getName())) {
				invisibleRoot.removeChild(node);
				return;
			}
		}
	}
	
	private Image createIcon(JsDebugger debugger) {
		JsDebuggersManager manager = Activator.getDebuggerManager();
		if (manager == null) {
			return null;
		}
		IExtension extension = manager.getExtension(debugger);
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
