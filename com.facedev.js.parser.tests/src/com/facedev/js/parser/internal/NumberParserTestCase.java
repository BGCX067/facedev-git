package com.facedev.js.parser.internal;

import static org.junit.Assert.fail;

import java.io.StringReader;

import junit.framework.Assert;

import org.junit.Test;

import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.Token;

public class NumberParserTestCase {

	@Test
	public void testOkHex() throws JsParseException {
		assertEquals(0x0, parse("0x0"));
		assertEquals(0x04, parse("0X04"));
		assertEquals(0x04, parse("0x4"));
		assertEquals(0x0567, parse("0x567"));
		assertEquals(0x0ABCDEF, parse("0X0AbcDEF"));
	}

	@Test
	public void testWrongHex() throws JsParseException {
		try {
			parse("1x1");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("aX1");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("0X1G");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("0xFG");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
	}
	
	@Test
	public void testOkInteger() throws JsParseException {
		assertEquals(1d, parse("1"));
		assertEquals(123d, parse("123"));
		assertEquals(9342323d, parse("9342323"));
	}
	
	@Test
	public void testOkFloating() throws JsParseException {
		assertEquals(.5d, parse(".5"));
		assertEquals(1.675d, parse("1.675"));
		assertEquals(0.01d, parse("0.01"));
	}
	

	@Test
	public void testOkExpo() throws JsParseException {
		assertEquals(1e-1d, parse("1e-1"));
		assertEquals(5e-10d, parse("5e-10"));
		assertEquals(5e+10d, parse("5e+10"));
		assertEquals(19E20d, parse("19E20"));
		assertEquals(.5e5d, parse(".5e5"));
		assertEquals(0.1E12d, parse("0.1E12"));
	}
	
	@Test
	public void testWrongInteger() throws JsParseException {
		try {
			parse("1a");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
	}
	
	@Test
	public void testWrongFloating() throws JsParseException {
		try {
			parse(".");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse(".1a");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("0.01d");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("1.");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
	}
	
	@Test
	public void testWrongExpo() throws JsParseException {
		try {
			parse("0.e1");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("0ea");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("0e1a");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
		try {
			parse("e1");
			fail("Should throw NumberFormatException");
		} catch(NumberFormatException ex) {
		}
	}
	
	private static void assertEquals(double expected, double actual) {
		Assert.assertEquals(expected, actual, 0.0);
	}
	
	private double parse(String val) throws JsParseException {
		Tokenizer tokenizer = new Tokenizer(new StringReader(val));
		Assert.assertNotNull(tokenizer.next());
		Token number = tokenizer.current();
		Assert.assertNull(tokenizer.next());
		return JsNumberLiteralDescriptorImpl.parseNumber(number);
	}
}
