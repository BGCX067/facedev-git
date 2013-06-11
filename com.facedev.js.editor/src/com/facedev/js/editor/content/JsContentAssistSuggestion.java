package com.facedev.js.editor.content;

/**
 * Implementation should be lightweight bean for storing suggestion information.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsContentAssistSuggestion {
	
	/**
	 * @return suggestion type (may be <code>null</code>)
	 */
	JsContenAssistSuggestionType getType();
	
	/**
	 * @return proposal string for current place of code.
	 */
	String getProposal();
	
	/**
	 * @return display name for current proposal.
	 */
	String getDisplayName();
	
	/**
	 * @return offset of replacement.
	 */
	int getReplacementOffset();
	
	/**
	 * @return length of replacement
	 */
	int getReplacementLength();
	
	/**
	 * @return cursor position after replacement.
	 */
	int getCursorPosition();
	
}
