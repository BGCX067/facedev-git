package com.facedev.js.editor;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * Javascript editor implementation entry point for eclipse.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsEditor extends TextEditor {
	
	private static final String RULLER_CONTEXT_MENU_ID = "com.facedev.js.editor#rulerContextMenu";

	public JsEditor() {
		setDocumentProvider(new JsDocumentProvider());
		setSourceViewerConfiguration(new JsSourceViewerConfiguration());
		
		setRulerContextMenuId(RULLER_CONTEXT_MENU_ID);
	}
}
