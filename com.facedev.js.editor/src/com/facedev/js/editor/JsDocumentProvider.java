package com.facedev.js.editor;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

import com.facedev.js.editor.syntax.JsPartitionsScanner;

/**
 * Document provider for this editor. Main entry point for it as provides bindings to other components.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsDocumentProvider extends FileDocumentProvider {
	
	JsDocumentProvider() {
		super();
	}

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument doc = super.createDocument(element);
		if (doc != null) {
			doc.setDocumentPartitioner(createDocumentPartitioner(doc));
		}
		return doc;
	}

	/*
	 * Creates document partitioner for splitting document on syntax-related parts.
	 */
	private IDocumentPartitioner createDocumentPartitioner(IDocument document) {
		JsPartitionsScanner scanner = new JsPartitionsScanner();
		IDocumentPartitioner result = new FastPartitioner(scanner, JsPartitionsScanner.getLegalContentTypes());
		result.connect(document);
		return result;
	}
}
