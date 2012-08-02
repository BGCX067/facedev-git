package com.facedev.js.debug.internal;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.LineBreakpoint;

/**
 * Provides javascript breakpoint implementation.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsBreakpoint extends LineBreakpoint {

	static final String MARKER_ID = "com.facedev.js.debug.facedev.breakpoint.marker";

	/**
	 * Default constructor is required for the breakpoint manager
	 * to re-create persisted breakpoints. After instantiating a breakpoint,
	 * the <code>setMarker(...)</code> method is called to restore
	 * this breakpoint's attributes.
	 */
	public JsBreakpoint() {
	}

	/**
	 * Constructs a line breakpoint on the given resource at the given
	 * line number. The line number is 1-based (i.e. the first line of a
	 * file is line number 1).
	 * 
	 * @param resource file on which to set the breakpoint
	 * @param lineNumber 1-based line number of the breakpoint
	 * @throws CoreException if unable to create the breakpoint
	 */
	public JsBreakpoint(final IResource resource, final int lineNumber) throws CoreException {
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = resource.createMarker(MARKER_ID);
				setMarker(marker);
				marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
				marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: "
						+ resource.getName() + " [line: " + lineNumber + "]");
			}
		};
		run(getMarkerRule(resource), runnable);
	}

	public String getModelIdentifier() {
		return JsModelPresentation.ID_JS_DEBUG_MODEL;
	}

}
