package com.facedev.js.editor.content;

import org.eclipse.swt.graphics.Image;

/**
 * This type provides descriptor for suggestion types.
 * Basic types are function and property. Suggesters may add own types. 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public class JsContenAssistSuggestionType {
	public static JsContenAssistSuggestionType FUNCTION = new JsContenAssistSuggestionType();
	public static JsContenAssistSuggestionType PROPERTY = new JsContenAssistSuggestionType();
	
	public Image getImage() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
