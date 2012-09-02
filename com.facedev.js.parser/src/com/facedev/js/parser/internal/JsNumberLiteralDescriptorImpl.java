package com.facedev.js.parser.internal;

import java.util.Collections;
import java.util.List;

import com.facedev.js.parser.JsNumberLiteralDescriptor;
import com.facedev.js.parser.JsParseLogger;
import com.facedev.js.parser.Token;

/**
 * Implementation of {@link JsNumberLiteralDescriptor}.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsNumberLiteralDescriptorImpl implements JsNumberLiteralDescriptor {
	
	private Token number;

	JsNumberLiteralDescriptorImpl(Token number) {
		this.number = number;
	}
	
	/**
	 * @param number literal token
	 * @return parsed to java double representation of javascript number.
	 * General rules of this parsing are described in the 
	 * ECMA-262 specification chapter 7.8.3.
	 */
	static double parseNumber(Token number) {
		NumberParser parser = new NumberParser(number);
		parser.parse();
		return parser.doubleValue();
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#getTokens()
	 */
	public List<Token> getTokens() {
		return Collections.singletonList(number);
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsDescriptor#validate(com.facedev.js.parser.JsParseLogger)
	 */
	public void validate(JsParseLogger logger) {
	}

	/*
	 * (non-Javadoc)
	 * @see com.facedev.js.parser.JsNumberLiteralDescriptor#getValue()
	 */
	public double getValue() {
		return parseNumber(number);
	}

	/**
	 * Service class for numbers parsing.
	 * 
	 * @author alex.bereznevatiy@gmail.com
	 *
	 */
	private static class NumberParser {
		private long integer;
		private long decimal;
		private int exponent;
		
		private CharSequence input;
		
		NumberParser(CharSequence input) {
			this.input = input;
		}

		/**
		 * @return double presentation for parsed value
		 * @throws IllegalStateException in case if not parsed.
		 */
		double doubleValue() {
			double value = integer;
			if (decimal != 0) {
				long dec = decimal;
				int pow = 0;
				while (dec >= 2) {
					dec /= 10;
					pow++;
				}
				long denominator = fastPower(10, pow);
				value += ((double)(decimal - denominator))/((double)denominator);
			}
			if (exponent < 0) {
				value /= fastPowerD(10, -exponent);
			} else if (exponent > 0) {
				value *= fastPowerD(10, exponent);
			}
			
			return value;
		}
		
		private double fastPowerD(int base, int power) {
			if (base == 0) {
				return 0;
			}
			if (power == 0) {
				return 1;
			}
			double result = 1;
			double iterator = base;
			while (power > 0) {
				if ((power & 1) > 0) {
					result *= iterator;
				}
				iterator = iterator * iterator;
				power >>= 1;
			}
			return result;
		}

		private static long fastPower(int base, int power) {
			if (base == 0) {
				return 0;
			}
			if (power == 0) {
				return 1;
			}
			long result = 1;
			int iterator = base;
			while (power > 0) {
				if ((power & 1) > 0) {
					result *= iterator;
				}
				iterator = iterator * iterator;
				power >>= 1;
			}
			return result;
		}

		/**
		 * Performs number parsing. Throws {@link NumberFormatException} in case 
		 * parsing fails.
		 */
		void parse() {
			if (input.length() == 0) {
				throw new NumberFormatException("Empty digit literal");
			}
			if (input.length() < 2) {
				parseDecimal();
				return;
			}
			char c = input.charAt(1);
			if (c == 'x' || c == 'X') {
				parseHex();
			} else {
				parseDecimal();
			}
		}

		private void parseHex() {
			if (input.charAt(0) != '0') {
				throw new NumberFormatException("Bad hex literal start");
			}
			final int length = input.length();
			if (length < 3) {
				throw new NumberFormatException("Bad hex literal start");
			}
			int result = 0;
			for (int i = 2; i < length; i++) {
				char c = input.charAt(i);
				int value = fromHexDigit(c);
				if (value < 0) {
					throw new NumberFormatException(input + " is not a hex literal");
				}
				result *= 16;
				result += value;
			}
			integer = result;
		}

		private int fromHexDigit(char c) {
			if (c >= '0' && c <= '9') {
				return c - '0';
			}
			if (c >= 'a' && c <= 'f') {
				return 10 + c - 'a';
			}
			if (c >= 'A' && c <= 'F') {
				return 10 + c - 'A';
			}
			return -1;
		}

		private void parseDecimal() {
			int index = parseInteger();
			index = parseDecimalDigits(index);
			parseExponent(index);
		}

		private int parseInteger() {
			final int length = input.length();
			long result = 0;
			for (int i = 0; i < length; i++) {
				char c = input.charAt(i);
				if (c == '.') {
					integer = result;
					return i;
				}
				if (c == 'e' || c == 'E') {
					if (i == 0) {
						throw new NumberFormatException(
							"Number literal cannot start from exponent");
					}
					integer = result;
					return i;
				}
				int value = fromDecimalDigit(c);
				if (value < 0) {
					throw new NumberFormatException(input + " is wrong number literal");
				}
				if (i == 0 && value == 0 && 
						length > 1) {
					c = input.charAt(1);
					if (c != '.' && c != 'e' && c != 'E') {
						throw new NumberFormatException(input + " is wrong number literal");
					}
					continue;
				}
				result *= 10;
				result += value;
			}
			integer = result;
			return length;
		}

		private int fromDecimalDigit(char c) {
			if ( c >= '0' && c <= '9' ) {
				return c - '0';
			}
			return -1;
		}

		private int parseDecimalDigits(int index) {
			final int length = input.length();
			if (index == length) {
				return length;
			}
			if (input.charAt(index) != '.') {
				return index;
			}
			long result = 1;
			boolean hasDigits = false;
			for (int i = index + 1; i < length; i++) {
				char c = input.charAt(i);
				if (c == 'e' || c == 'E') {
					if (!hasDigits) {
						throw new NumberFormatException(input + " is invalid number");
					}
					decimal = result;
					return i;
				}
				int value = fromDecimalDigit(c);
				if (value < 0) {
					throw new NumberFormatException(input + " is invalid number literal");
				}
				result *= 10;
				result += value;
				hasDigits = true;
			}
			if (!hasDigits) {
				throw new NumberFormatException(input + " is invalid number");
			}
			decimal = result;
			return length;
		}

		private void parseExponent(int index) {
			final int length = input.length();
			if (index == length) {
				return;
			}
			char ch = input.charAt(index);
			if (ch != 'e' && ch != 'E') {
				throw new NumberFormatException(input + " is invalid number literal");
			}
			index++;
			if (index >= length) {
				throw new NumberFormatException(input + " is invalid number literal");
			}
			
			ch = input.charAt(index);
			int modifier = 1;
			if (ch == '-') {
				index++;
				modifier = -1;
			} else if (ch == '+') {
				index++;
			}
			int result = 0;
			boolean hasDigits = false;
			for (int i = index; i < length; i++) {
				char c = input.charAt(i);
				int value = fromDecimalDigit(c);
				if (value < 0) {
					throw new NumberFormatException(input + " is invalid number literal");
				}
				result *= 10;
				result += value;
				hasDigits = true;
			}
			if (!hasDigits) {
				throw new NumberFormatException(input + " is invalid number");
			}
			exponent = result * modifier;
		}
	}
}
