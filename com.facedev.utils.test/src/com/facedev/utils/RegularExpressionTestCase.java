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
		assertEquals(12, regex.consume("aaabcccdddd", 0)); 
	}

	@Test
	public void testConsumeEOF() {
		fail("Not yet implemented");
	}

	@Test
	public void testReset() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testClone() {
		fail("Not yet implemented");
	}

}
