package com.facedev.js.editor;

import org.eclipse.ui.editors.text.TextEditor;

/**
 * Javascript editor implementation entry point for eclipse.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsEditor extends TextEditor {
	
	public JsEditor() {
		setDocumentProvider(new JsDocumentProvider());
		setSourceViewerConfiguration(new JsSourceViewerConfiguration());
	}
}
