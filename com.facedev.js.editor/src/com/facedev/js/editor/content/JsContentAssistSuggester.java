package com.facedev.js.editor.content;

import java.util.List;
import java.util.RandomAccess;

/**
 * Provides suggestions for content assist. Also there may be some service suggesters - like sorter or organizer.
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface JsContentAssistSuggester {
	
	/**
	 * Performs main work by providing suggestions by pasting them to the result.
	 * Result is guaranteed to be {@link RandomAccess} but is not thread-safe.
	 * @param result to add suggestions to
	 * @param lastDocumentNode - document node (TODO: replace with real type)
	 * @param prefix string for suggestion
	 */
	void suggest(List<JsContentAssistSuggestion> result, Object lastDocumentNode, String prefix);
	
	/**
	 * @return JsContentAssistSuggesterPriority 
	 * or <code>null</code> if priority is configured in extension configuration.
	 */
	JsContentAssistSuggesterPriority getPriority();
}
