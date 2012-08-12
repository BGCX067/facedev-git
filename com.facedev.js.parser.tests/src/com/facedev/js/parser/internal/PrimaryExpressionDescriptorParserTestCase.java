package com.facedev.js.parser.internal;

import static junit.framework.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.facedev.js.parser.JsArrayLiteralDescriptor;
import com.facedev.js.parser.JsBooleanLiteralDescriptor;
import com.facedev.js.parser.JsDescriptor;
import com.facedev.js.parser.JsExpressionDescriptor;
import com.facedev.js.parser.JsIdentifierDescriptor;
import com.facedev.js.parser.JsNullLiteralDescriptor;
import com.facedev.js.parser.JsNumberLiteralDescriptor;
import com.facedev.js.parser.JsObjectLiteralDescriptor;
import com.facedev.js.parser.JsParseException;
import com.facedev.js.parser.JsRegexpLiteralDescriptor;
import com.facedev.js.parser.JsStringLiteralDescriptor;
import com.facedev.js.parser.JsUndefinedLiteralDescriptor;

public class PrimaryExpressionDescriptorParserTestCase {
	
	@Test
	public void testThis() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "this.fn();");
		assertTrue(descriptor instanceof JsIdentifierDescriptor);
		JsIdentifierDescriptor identifier = (JsIdentifierDescriptor) descriptor;
		assertEquals("this", identifier.getIdentifier());
	}
	
	@Test
	public void testIdentifier() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "$iname = fn();");
		assertTrue(descriptor instanceof JsIdentifierDescriptor);
		JsIdentifierDescriptor identifier = (JsIdentifierDescriptor) descriptor;
		assertEquals("$iname", identifier.getIdentifier());
	}
	
	@Test
	public void testStringLiteral() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "\"this is a string\".substr(2);");
		assertTrue(descriptor instanceof JsStringLiteralDescriptor);
		JsStringLiteralDescriptor literal = (JsStringLiteralDescriptor) descriptor;
		assertEquals("this is a string", literal.getValue());		
	}
	
	@Test
	public void testDigitLiteral() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "0x022.method();");
		assertTrue(descriptor instanceof JsNumberLiteralDescriptor);
		JsNumberLiteralDescriptor literal = (JsNumberLiteralDescriptor) descriptor;
		assertEquals((double)0x022, literal.getValue());		
	}
	
	@Test
	public void testRegexLiteral() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "/[a-z]*/gi.test('str');");
		assertTrue(descriptor instanceof JsRegexpLiteralDescriptor);
		JsRegexpLiteralDescriptor literal = (JsRegexpLiteralDescriptor) descriptor;
		assertEquals("/[a-z]*/gi", literal.getValue());		
	}
	
	@Test
	public void testBooleanLiteral() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "true.test('str');");
		assertTrue(descriptor instanceof JsBooleanLiteralDescriptor);
		JsBooleanLiteralDescriptor literal = (JsBooleanLiteralDescriptor) descriptor;
		assertTrue(literal.getValue());		
	}
	
	@Test
	public void testNullLiteral() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "null == undefined");
		assertTrue(descriptor instanceof JsNullLiteralDescriptor);	
	}
	
	@Test
	public void testUndefinedLiteral() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "undefined == null");
		assertTrue(descriptor instanceof JsUndefinedLiteralDescriptor);
	}
	
	@Test
	public void testArray() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "[aaa,bbb,ccc]");
		assertTrue(descriptor instanceof JsArrayLiteralDescriptor);
		List<JsDescriptor> values = ((JsArrayLiteralDescriptor)descriptor).getValues();
		
		assertEquals(3, values.size());
		assertTrue(values.get(0) instanceof JsIdentifierDescriptor);
		assertTrue(values.get(1) instanceof JsIdentifierDescriptor);
		assertTrue(values.get(2) instanceof JsIdentifierDescriptor);
	}
	
	@Test
	public void testJson() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "{aaa:aaa,\"bbb\":bbb,ccc:1}");
		assertTrue(descriptor instanceof JsObjectLiteralDescriptor);
		Map<String, JsDescriptor> values = ((JsObjectLiteralDescriptor)descriptor).getValues();
		
		assertEquals(3, values.size());
		assertTrue(values.get("aaa") instanceof JsIdentifierDescriptor);
		assertTrue(values.get("bbb") instanceof JsIdentifierDescriptor);
		assertTrue(values.get("ccc") instanceof JsNumberLiteralDescriptor);
	}
	
	@Test
	public void testComplexExpression() throws JsParseException {
		JsDescriptor descriptor = ParseTestUtils.parse(
				JsDescriptorType.PRIMARY_EXPRESSION, "(aa = bb).test()");
		
		assertTrue(descriptor instanceof JsExpressionDescriptor);// ?
		
		fail("Not implemented");// TODO: implement
	}
}
