package com.facedev.js.parser.internal;

import com.facedev.js.parser.JsKeywords;

/**
 * Provides fast specialized hash-based map for javascript keywords.
 * This implementation provides O(1) time with very low coefficients.
 * 
 * Char sequences are represented as keys and interned {@link String}s are
 * represented as values.
 * 
 * @author alex.bereznevatiy@gmail.com
 * @see String#intern()
 *
 */
final class KeywordsFastMap implements JsKeywords {
	
	private static final Object LOCK = new Object();
	
	private static volatile Entry[] store;
	
	KeywordsFastMap() {
		initialize();
	}
	
	String get(CharSequence key) {
		return null;
	}
	
	private static void initialize() {
		if (store != null) {
			return;// for fast already-initialized 
		}
		
		synchronized (LOCK) {
			if (store != null) {
				return;
			}
			
			put(KEYWORD_ABSTRACT);
			// TODO;
		}
	}
	
	private static void put(String value) {
		
	}
	
	private static class Entry {
		
	}
}
