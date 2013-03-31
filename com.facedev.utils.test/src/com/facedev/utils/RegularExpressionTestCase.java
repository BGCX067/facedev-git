package com.facedev.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegularExpressionTestCase {

	@Test
	public void testCreateString() {
		RegularExpression regex = RegularExpression.create("abc");
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
		
		regex = RegularExpression.create("abc|bbb");
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
	}

	@Test
	public void testCreateRegularExpressionRegularExpressionArray() {
		RegularExpression regex1 = RegularExpression.create("abc");
		RegularExpression regex2 = RegularExpression.create("xxx");
		RegularExpression regex = RegularExpression.create(regex1, regex2);
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
		
		assertSame(regex, RegularExpression.create(regex));
	}

	@Test
	public void testCompileString() {
		RegularExpression regex = RegularExpression.compile("abc");
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
		
		regex = RegularExpression.compile("abc|bbb");
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
	}

	@Test
	public void testCompileRegularExpressionRegularExpressionArray() {
		RegularExpression regex1 = RegularExpression.compile("abc");
		RegularExpression regex2 = RegularExpression.compile("xxx");
		RegularExpression regex = RegularExpression.compile(regex1, regex2);
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
		
		assertSame(regex, RegularExpression.compile(regex));
	}

	@Test
	public void testCompile() {
		RegularExpression regex = RegularExpression.create("abc");
		regex.compile();
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
		
		regex = RegularExpression.create("abc|bbb");
		regex.compile();
		assertEquals(RegularExpression.STATE_UNDEFINED, regex.getState());
	}

	@Test
	public void testConsume() {
		RegularExpression regex = RegularExpression.create("a*bc*");
		assertEquals(7, regex.consume("aaabcccdddd", 0));
		assertEquals(1, regex.getState());
		
		regex.reset();
		regex.compile();
		
		assertEquals(7, regex.consume("aaabcccdddd", 0));
		assertEquals(1, regex.getState());
	}
	
	@Test
	public void testConsumeStates() {
		RegularExpression regex = RegularExpression.create("[abc]*");
		assertEquals(14, regex.consume("aaabbbbcccaaccdddd", 0));
		assertEquals(1, regex.getState());
		regex.reset();
		assertEquals(0, regex.consume("aaabbbbcccaaccdddd", 14));
		assertEquals(RegularExpression.STATE_ERROR, regex.getState());
		
		regex.reset();
		regex.compile();
		
		assertEquals(14, regex.consume("aaabbbbcccaaccdddd", 0));
		assertEquals(1, regex.getState());
		regex.reset();
		assertEquals(0, regex.consume("aaabbbbcccaaccdddd", 14));
		assertEquals(RegularExpression.STATE_ERROR, regex.getState());
	}
	
	@Test
	public void testConsumeMultipleStates() {
		RegularExpression regex = RegularExpression.create("a*|b*");
		assertEquals(3, regex.consume("aaabbbbcccaaccdddd", 0));
		assertEquals(1, regex.getState());
		regex.reset();
		assertEquals(4, regex.consume("aaabbbbcccaaccdddd", 3));
		assertEquals(2, regex.getState());
		regex.reset();
		assertEquals(0, regex.consume("aaabbbbcccaaccdddd", 7));
		assertEquals(RegularExpression.STATE_ERROR, regex.getState());

		regex.reset();
		regex.compile();
		
		assertEquals(3, regex.consume("aaabbbbcccaaccdddd", 0));
		assertEquals(1, regex.getState());
		regex.reset();
		assertEquals(4, regex.consume("aaabbbbcccaaccdddd", 3));
		assertEquals(2, regex.getState());
		regex.reset();
		assertEquals(0, regex.consume("aaabbbbcccaaccdddd", 7));
		assertEquals(RegularExpression.STATE_ERROR, regex.getState());
	}
	
	@Test
	public void testConsumeNestedStates() {
		RegularExpression regex = RegularExpression.create("a*|c*|b*");
		assertEquals(3, regex.consume("aaabbbbcccaaccdddd", 0));
		assertEquals(1, regex.getState());
		regex.reset();
		assertEquals(4, regex.consume("aaabbbbcccaaccdddd", 3));
		assertEquals(3, regex.getState());
		regex.reset();
		assertEquals(3, regex.consume("aaabbbbcccaaccdddd", 7));
		assertEquals(2, regex.getState());
		regex.reset();
		assertEquals(0, regex.consume("aaabbbbcccaaccdddd", 10));
		assertEquals(RegularExpression.STATE_ERROR, regex.getState());

		regex.reset();
		regex.compile();
		
		assertEquals(3, regex.consume("aaabbbbcccaaccdddd", 0));
		assertEquals(1, regex.getState());
		regex.reset();
		assertEquals(4, regex.consume("aaabbbbcccaaccdddd", 3));
		assertEquals(3, regex.getState());
		regex.reset();
		assertEquals(3, regex.consume("aaabbbbcccaaccdddd", 7));
		assertEquals(2, regex.getState());
		regex.reset();
		assertEquals(0, regex.consume("aaabbbbcccaaccdddd", 10));
		assertEquals(RegularExpression.STATE_ERROR, regex.getState());
	}
}
