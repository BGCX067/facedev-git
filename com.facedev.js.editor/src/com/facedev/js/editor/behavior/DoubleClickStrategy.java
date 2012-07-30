package com.facedev.js.editor.behavior;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.ITypedRegion;

import com.facedev.js.editor.syntax.JsPartitionsScanner;
import com.facedev.js.parser.CharUtils;

/**
 * Strategy to be applied on double click on source code.
 * If target token is name it should be selected.
 * If target is literal it should be selected including all additional symbols (like quotation).
 * Whitespaces also should be selected on double click (multiline are also applied).
 * 
 * TODO: string literal selection should follow next scheme: select word on first double click 
 * and select whole literal on second double click or if click target is not a word. 
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class DoubleClickStrategy implements ITextDoubleClickStrategy {
	
	private ITextViewer viewer;

	public void doubleClicked(ITextViewer viewer) {
		int pos = viewer.getSelectedRange().x;
		
		if (pos < 0) {
			return;
		}
		
		this.viewer = viewer;
		try {
			if (!selectString(pos) && !selectDigitLiteralOrWord(pos)) {
				selectWhiteSpace(pos);
			}
		} catch (BadLocationException ex) {
		}
	}

	private boolean selectString(int pos) throws BadLocationException {
		IDocument doc = viewer.getDocument();
		
		ITypedRegion partition = doc.getPartition(pos);
		
		if (!partition.getType().equals(JsPartitionsScanner.LITERAL_PART)) {
			return false;
		}
		
		if (viewer.getDocument().getChar(partition.getOffset()) == '/') {
			viewer.setSelectedRange(partition.getOffset(), partition.getLength());
		} else if (partition.getLength() <= 2) {
			viewer.setSelectedRange(partition.getOffset(), partition.getLength());
		} else {
			viewer.setSelectedRange(partition.getOffset() + 1, partition.getLength() - 2);
		}
		return true;
	}

	private boolean selectDigitLiteralOrWord(int pos) throws BadLocationException {
		IDocument doc = viewer.getDocument();
		
		char c = doc.getChar(pos);
		if (!CharUtils.isIdentifierPart(c)) {
			return false;
		}
		
		int start = pos, end = pos;
		
		while (start --> 0) {
			c = doc.getChar(start);
			if (!CharUtils.isIdentifierPart(c)) {
				start++;
				break;
			}
		}
		
		final int length = doc.getLength();
		
		while ( ++end < length) {
			c = doc.getChar(end);
			if (!CharUtils.isIdentifierPart(c)) {
				break;
			}
		}
		
		viewer.setSelectedRange(start, end - start);
		
		return true;
	}

	private boolean selectWhiteSpace(int pos) throws BadLocationException {
		IDocument doc = viewer.getDocument();
		char c = doc.getChar(pos);
		if (!Character.isWhitespace(c)) {
			return false;
		}
		
		int start = pos, end = pos;
		
		while (start --> 0) {
			c = doc.getChar(start);
			if (!Character.isWhitespace(c)) {
				start++;
				break;
			}
		}
		
		final int length = doc.getLength();
		
		while ( ++end < length) {
			c = doc.getChar(end);
			if (!Character.isWhitespace(c)) {
				break;
			}
		}
		
		viewer.setSelectedRange(start, end - start);
		return true;
	}

}