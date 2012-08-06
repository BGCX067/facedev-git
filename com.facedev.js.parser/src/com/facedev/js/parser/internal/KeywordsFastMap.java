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
	private static final int MAX_SIZE = 'z' - 'a' + 1;
	
	private static volatile Entry[] store;
	
	KeywordsFastMap() {
		initialize();
	}
	
	String get(CharSequence key) {
		if (key == null || key.length() < 2) {
			return null;
		}
		int c = key.charAt(0) - 'a';
		if (c < 0 || c >= store.length || store[c] == null) {
			return null;
		}
		return store[c].get(key);
	}
	
	private static void initialize() {
		if (store != null) {
			return;// for fast already-initialized 
		}
		
		synchronized (LOCK) {
			if (store != null) {
				return;
			}
			
			store = new Entry[MAX_SIZE];
			
			put(KEYWORD_ABSTRACT);
			put(KEYWORD_AS);
			put(KEYWORD_BOOLEAN);
			put(KEYWORD_BREAK);
			put(KEYWORD_BYTE);
			put(KEYWORD_CASE);
			put(KEYWORD_CATCH);
			put(KEYWORD_CHAR);
			put(KEYWORD_CLASS);
			put(KEYWORD_CONTINUE);
			put(KEYWORD_CONST);
			put(KEYWORD_DEBUGGER);
			put(KEYWORD_DEFAULT);
			put(KEYWORD_DELETE);
			put(KEYWORD_DO);
			put(KEYWORD_DOUBLE);
			put(KEYWORD_ELSE);
			put(KEYWORD_ENUM);
			put(KEYWORD_EXPORT);
			put(KEYWORD_EXTENDS);
			put(KEYWORD_FALSE);
			put(KEYWORD_FINAL);
			put(KEYWORD_FINALLY);
			put(KEYWORD_FLOAT);
			put(KEYWORD_FOR);
			put(KEYWORD_FUNCTION);
			put(KEYWORD_GOTO);
			put(KEYWORD_IF);
			put(KEYWORD_IMPLEMENTS);
			put(KEYWORD_IMPORT);
			put(KEYWORD_IN);
			put(KEYWORD_INSTANCEOF);
			put(KEYWORD_INT);
			put(KEYWORD_INTERFACE);
			put(KEYWORD_IS);
			put(KEYWORD_LONG);
			put(KEYWORD_NAMESPACE);
			put(KEYWORD_NATIVE);
			put(KEYWORD_NEW);
			put(KEYWORD_NULL);
			put(KEYWORD_PACKAGE);
			put(KEYWORD_PRIVATE);
			put(KEYWORD_PROTECTED);
			put(KEYWORD_PUBLIC);
			put(KEYWORD_RETURN);
			put(KEYWORD_SHORT);
			put(KEYWORD_STATIC);
			put(KEYWORD_SUPER);
			put(KEYWORD_SWITCH);
			put(KEYWORD_SYNCHRONIZED);
			put(KEYWORD_THIS);
			put(KEYWORD_THROW);
			put(KEYWORD_THROWS);
			put(KEYWORD_TRANSIENT);
			put(KEYWORD_TRUE);
			put(KEYWORD_TRY);
			put(KEYWORD_TYPEOF);
			put(KEYWORD_USE);
			put(KEYWORD_VAR);
			put(KEYWORD_VOID);
			put(KEYWORD_VOLATILE);
			put(KEYWORD_WHILE);
			put(KEYWORD_WITH);
		}
	}
	
	private static void put(String value) {
		int index = value.charAt(0) - 'a';
		if (store[index] == null) {
			store[index] = new Entry();
		}
		store[index].add(value);
	}
	
	static boolean eq(CharSequence o1, CharSequence o2) {
		final int len = o1.length();
		for (int i = 1; i < len; i++) {
			if (o1.charAt(i) != o2.charAt(i)) {
				return false;
			}
		}
		return true;
	}
	
	private static final class Entry {
		
		private Object[] values = new Object[11];

		void add(String value) {
			int hash = value.length() - 2;

			if (values[hash] == null) {
				values[hash] = value;
				return;
			}
			if (values[hash] instanceof String) {
				values[hash] = new SubEntry(value, (String)values[hash]);
				return;
			}
			
			((SubEntry)values[hash]).add(value);
		}

		String get(CharSequence key) {
			int hash = key.length() - 2;
			if (hash >= values.length) {
				return null;
			}
			Object obj = values[hash];
			if (obj == null) {
				return null;
			}
			if (obj instanceof String) {
				String result = (String)obj;
				if (result == null || !eq(result, key)) {
					return null;
				}
				return result;
			}
			return ((SubEntry)obj).get(key);
		}
	}
	
	private static final class SubEntry {
		
		private String[] values = new String[MAX_SIZE];
		private String collision;
		private int collisionHash;

		public SubEntry(String value1, String value2) {
			add(value1);
			add(value2);
		}

		public String get(CharSequence key) {
			int hash = key.charAt(key.length() - 1) - 'a';
			if (hash < 0 || hash >= values.length) {
				return null;
			}
			String result = values[hash];
			if (result == null) {
				return null;
			}
			
			if (eq(result, key)) {
				return result;
			}
			
			if (collisionHash == hash && 
				eq(collision, key)) {
				return collision;
			}
			return null;
		}

		public void add(String value) {
			int hash = value.charAt(value.length() - 1) - 'a';
			if (values[hash] == null) {
				values[hash] = value;
				return;
			}
			if (collision != null) {
				throw new InternalError("Multiple collisions detected");
			}
			collision = value;
		}
		
	}
}
