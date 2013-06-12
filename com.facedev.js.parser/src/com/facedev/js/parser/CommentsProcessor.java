package com.facedev.js.parser;

/**
 * Defines commentaries processor that can be registered with extension point of this bundle.
 * TODO: implement this functionality
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public interface CommentsProcessor {
	
	/**
	 * Processes passed token. By contract it is guaranteed that passed token will be always 
	 * javascript comment (i.e. {@link Token#isComment()} always returns <code>true</code>.
	 * @param comment to process
	 * @return result of comments processing or <code>null</code> if there is no result.
	 */
	String process(Token comment);
}
