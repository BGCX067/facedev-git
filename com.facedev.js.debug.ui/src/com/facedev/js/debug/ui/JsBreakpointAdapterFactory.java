package com.facedev.js.debug.ui;

import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.debug.ui.actions.IToggleBreakpointsTarget;

import com.facedev.js.editor.JsEditor;

/**
 * Adapter factory for FaceDev editor.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsBreakpointAdapterFactory implements IAdapterFactory {

	public Object getAdapter(Object adaptableObject, @SuppressWarnings("rawtypes") Class adapterType) {
		if (adaptableObject instanceof JsEditor) {
			return new JsLineBreakpointAdapter();
		}
		return null;
	}

	public Class<?>[] getAdapterList() {
		return new Class[] { IToggleBreakpointsTarget.class };
	}

}
