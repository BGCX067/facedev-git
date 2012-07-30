package com.facedev.js.editor;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;

import com.facedev.js.editor.appearance.ColorManager;
import com.facedev.js.editor.behavior.JsDefaultDamagerRepairer;
import com.facedev.js.editor.behavior.DoubleClickStrategy;
import com.facedev.js.editor.syntax.JsCommentsScanner;
import com.facedev.js.editor.syntax.JsDocsScanner;
import com.facedev.js.editor.syntax.JsLiteralsScanner;
import com.facedev.js.editor.syntax.JsPartitionsScanner;
import com.facedev.js.editor.syntax.JsScanner;

/**
 * Configuration for the editor. Provides different behavior strategies and options
 * to use with boostscript editor.
 * 
 * @author alex.bereznevatiy@gmail.com
 */
public class JsSourceViewerConfiguration extends SourceViewerConfiguration {
	
	private ITextDoubleClickStrategy doubleClickStrategy;
	
	private ITokenScanner defaultScanner;
	
	private ITokenScanner boostDocScanner;
	
	private ITokenScanner commentsScanner;
	
	private ITokenScanner literalsScanner;
	
	@Override
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null) {
			doubleClickStrategy = new DoubleClickStrategy();
		}
		return doubleClickStrategy;
	}

	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return JsPartitionsScanner.getLegalContentTypes();
	}

	@Override
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler presentationReconciler = new PresentationReconciler();
		
		JsDefaultDamagerRepairer commentsDamagerRepairer = new JsDefaultDamagerRepairer(getCommentsScanner(), 
				new TextAttribute(ColorManager.getInstance().getColor(ColorManager.COMMENT_COLOR)));
		presentationReconciler.setDamager(commentsDamagerRepairer, JsPartitionsScanner.COMMENT_PART);
		presentationReconciler.setRepairer(commentsDamagerRepairer, JsPartitionsScanner.COMMENT_PART);
		
		JsDefaultDamagerRepairer jsDocDamagerRepairer = new JsDefaultDamagerRepairer(getJsDocScanner(),
				new TextAttribute(ColorManager.getInstance().getColor(ColorManager.JSDOC_COLOR)));
		presentationReconciler.setDamager(jsDocDamagerRepairer, JsPartitionsScanner.JSDOC_PART);
		presentationReconciler.setRepairer(jsDocDamagerRepairer, JsPartitionsScanner.JSDOC_PART);

		DefaultDamagerRepairer literalsDamagerRepairer = new JsDefaultDamagerRepairer(getLiteralsScanner(),
				new TextAttribute(ColorManager.getInstance().getColor(ColorManager.LITERAL_COLOR)));
		presentationReconciler.setDamager(literalsDamagerRepairer, JsPartitionsScanner.LITERAL_PART);
		presentationReconciler.setRepairer(literalsDamagerRepairer, JsPartitionsScanner.LITERAL_PART);
		
		DefaultDamagerRepairer defaultDamagerRepairer = new DefaultDamagerRepairer(getDefaultScanner());
		presentationReconciler.setDamager(defaultDamagerRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		presentationReconciler.setRepairer(defaultDamagerRepairer, IDocument.DEFAULT_CONTENT_TYPE);
		
		return presentationReconciler;
	}

	protected ITokenScanner getDefaultScanner() {
		if (defaultScanner == null) {
			defaultScanner = new JsScanner();
		}
		return defaultScanner;
	}

	protected ITokenScanner getLiteralsScanner() {
		if (literalsScanner == null) {
			literalsScanner = new JsLiteralsScanner();
		}
		return literalsScanner;
	}

	protected ITokenScanner getJsDocScanner() {
		if (boostDocScanner == null) {
			boostDocScanner = new JsDocsScanner();
		}
		return boostDocScanner;
	}

	protected ITokenScanner getCommentsScanner() {
		if (commentsScanner == null) {
			commentsScanner = new JsCommentsScanner();
		}
		return commentsScanner;
	}
}
