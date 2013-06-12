package com.facedev.js.editor.content;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for handling suggesters. Adds all necessary listeners for extensions.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsSuggesters {
	
	private JsSuggesters() {}

	public static List<JsContentAssistSuggestion> query(Object lastDocumentNode,
			String prefix) {
		List<JsContentAssistSuggestion> result = new ArrayList<JsContentAssistSuggestion>();
		for (JsContentAssistSuggester suggester : getRegisteredSuggesters()) {
			suggester.suggest(result, lastDocumentNode, prefix);
		}
		return result;
	}
	
	private static List<JsContentAssistSuggester> getRegisteredSuggesters() {
		// TODO: implement
		return Collections.emptyList();
	}

}
