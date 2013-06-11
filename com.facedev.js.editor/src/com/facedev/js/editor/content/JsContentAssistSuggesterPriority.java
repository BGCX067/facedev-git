package com.facedev.js.editor.content;

/**
 * Provides sort priority for suggesters.
 * Suggesters will be executed in the order defined in this enum.
 * In case two suggesters have the same priority they order will be not defined.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public enum JsContentAssistSuggesterPriority {
	PRELIMINARY,
	LANGUAGE,
	CORE,
	BROWSER,
	LIBRARY,
	POSTORDER;
}
