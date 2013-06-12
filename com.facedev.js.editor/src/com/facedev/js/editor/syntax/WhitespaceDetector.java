package com.facedev.js.editor.syntax;

import org.eclipse.jface.text.rules.IWhitespaceDetector;

/**
 * Simple detector for whitespaces.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
class WhitespaceDetector implements IWhitespaceDetector {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.text.rules.IWhitespaceDetector#isWhitespace(char)
	 */
	public boolean isWhitespace(char c) {
		return Character.isWhitespace(c);
	}
}