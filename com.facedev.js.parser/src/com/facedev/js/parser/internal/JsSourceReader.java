package com.facedev.js.parser.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reader from javascript sources. Works with unicode escape sequences as well.
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
final class JsSourceReader extends Reader {
	
	private Reader source;
	private int[] buf;
	private int index;

	/**
	 * Creates {@link JsSourceReader} based on other reader.
	 * @param source
	 */
	JsSourceReader(Reader source) {
		if (source == null) {
			throw new IllegalArgumentException("Unable to read from NULL source");
		}
		if (!(source instanceof BufferedReader)) {
			source = new BufferedReader(source);
		}
		this.source = source;
		this.buf = new int[5];
		this.index = -1;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Reader#read()
	 */
	@Override
	public int read() throws IOException {
		int c = readInternal();
		if (c == '\\') {
			c = tryReadEscape();
		}
		return c;
	}

	private int readInternal() throws IOException {
		if (index < 0) {
			return source.read();
		}
		return buf[index--];
	}

	private int tryReadEscape() throws IOException {
		int c = readInternal();
		if (c != 'u') {
			buf[0] = c;
			index = 0;
			return '\\';
		}
		
		int[] buffer = new int[4];
		
		int rez = 0;
		
		for (int i = 0; i < 4; i++) { 
			buffer[i] = readInternal();
			int dig = Character.digit(buffer[i], 16);
			if (dig < 0) {
				while (i > 0) {
					buf[++index] = buffer[i--];
				}
				return '\\';
			}
			rez = rez * 16 + dig;
		}
		
		return rez;
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Reader#close()
	 */
	@Override
	public void close() throws IOException {
		source.close();
	}

	/*
	 * (non-Javadoc)
	 * @see java.io.Reader#read(char[], int, int)
	 */
	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		throw new IOException("Not supported by this reader");
	}

}
