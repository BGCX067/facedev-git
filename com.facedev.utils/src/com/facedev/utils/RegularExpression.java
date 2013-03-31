package com.facedev.utils;

import java.util.regex.Pattern;

/**
 * <p>Represents regular expression for streams. This implementation compiles the regular 
 * expression into finite automaton (first non-deterministic, then deterministic).
 * 
 * <p>The main difference of this implementation from {@link Pattern} is that it provides
 * fast implementation for stream matching.
 * 
 * <p>This implementation has internal state, that reflects matching state of automaton.
 * 
 * <p>This implementation is not thread-safe, but can be used in thread-safe manner with
 * Cloning instances for each thread. Cloning implementation is very fast.
 * 
 * Pattern syntax is represented by following rules:
 * <ol>
 * <li>Single character matches itself.
 * <li>Character class is represented by square brackets. Class will match any character
 * within brackets. In other words character class represents union operation.
 * <li>By default the characters and classes are joined with concatenation operation.
 * <li>All reserved characters should be escaped with '\' to match themselves. Reserved
 * characters are: '[', ']', '(', ')', '{', '}', '&lt;', '&gt;', '+', '*', '\', ':',
 * '?', ',', '%', '&', '@', '$', '^', '!', '-', '|'.
 * <li>Star ('*') character represents iteration operation (e.g. matching any number of 
 * previous construction. 
 * <li>Curly brackets ('(' and ')') represents grouping operation.
 * <li>Union operation for multiple characters sequence is represented by '|' operation.
 * </ol>
 * Regular expression consumes the input on the "maximum munch" basis, in other worlds as
 * greedy as possible. Also if input of the same length matched two different states 
 * of the combined expression this implementation assumes that first passed state has
 * higher priority than next one. 
 * 
 * @author alex.bereznevatiy@gmail.com
 *
 */
public final class RegularExpression implements Cloneable {
	
	/**
	 * Represents error state of this automaton. As soon as this automaton moves 
	 * to this state it remains locked until #reset() method is called.
	 */
	public static final int STATE_ERROR = -1;
	
	/**
	 * Represents non-determined state of this automaton.
	 */
	public static final int STATE_UNDEFINED = 0;
	
	static {
		Compiler.compileClass(RegularExpression.class);
	}
	
	private volatile Node start;
	private volatile Node current;
	
	private char[][] lookup;
	
	private volatile int state;
	
	private final String pattern;

	private RegularExpression(String pattern, Node start) {
		this.pattern = pattern;
		this.start = start;
		reset();
	}
	
	/**
	 * Creates not compiled and not optimized regular expression. This expression 
	 * is initially represented as non-deterministic final automaton.
	 * @param pattern
	 * @return regular expression in the start state.
	 */
	public static RegularExpression create(String pattern) {
		Parser parser = new Parser(pattern);
		RootNode root = new RootNode();
		while (parser.hasNext()) {
			root.addNextState(parser.next());
		}
		return new RegularExpression(pattern, root);
	}

	/**
	 * Creates not compiled and not optimized regular expression from other expressions. 
	 * This expression is initially represented as non-deterministic final automaton. 
	 * @param expressions to combine into common one
	 * @return regular expression in the start state.
	 */
	public static RegularExpression create(RegularExpression expression, 
			RegularExpression...expressions) {
		if (expressions == null || expressions.length == 0) {
			return expression;
		}
		RootNode root = new RootNode();
		StringBuilder pattern = new StringBuilder();
		
		root.addNextState(expression.start);
		pattern.append(expression.pattern);
		
		for (RegularExpression next : expressions) {
			root.addNextState(next.start);
			pattern.append('|');
			pattern.append(next.pattern);
		}
		return new RegularExpression(pattern.toString(), root);
	}
	
	/**
	 * Creates compiled and optimized regular expression. This expression is represented
	 * as deterministic final automaton. Creation takes more time than 
	 * {@link #create(String)} method and requires more memory to store the automaton,
	 * however the performance of the result is much better than non-deterministic one.
	 * @param pattern
	 * @return regular expression in the start state.
	 */
	public static RegularExpression compile(String pattern) {
		RegularExpression result = create(pattern);
		result.compile();
		return result;
	}
	
	/**
	 * Creates compiled and optimized regular expression from other expressions. 
	 * This expression is represented as deterministic final automaton. Creation 
	 * takes more time than {@link #create(RegularExpression, RegularExpression...)} 
	 * method and requires more memory to store the automaton, however the performance 
	 * of the result is much better than non-deterministic one.
	 * @param expressions to combine into common one
	 * @return regular expression in the start state.
	 */
	public static RegularExpression compile(RegularExpression expression, 
			RegularExpression...expressions) {
		RegularExpression result = create(expression, expressions);
		result.compile();
		return result;
	}
	
	/**
	 * Compiles this non-deterministic automaton to deterministic one. If this regular
	 * expression is already compiled this method just returns.
	 */
	public void compile() {
		// TODO
	}
	
	/**
	 * <p>This method provides information about state of this automaton. State can provide
	 * a lot of information about part of the regular expression this automaton is
	 * constructed from. There are two special states:
	 * <ul>
	 * <li>{@link #STATE_ERROR} indicates that this automaton is not matching input at all
	 * (e.g. it cannot consume input string).
	 * <li>{@link #STATE_UNDEFINED} indicates that this automaton is not in the final 
	 * state and more input is required.
	 * </ul>
	 * <p>If returning value is positive integer than:
	 * <ul>
	 * <li>For stand-alone regular expression this indicates that final state is reached.
	 * <li>For combined regular expressions this indicates that final state is reached and
	 * provides the number of subexpression that specifically matches input.
	 * <li>If complex expression consists of other complex expressions - state is calculated
	 * only for top-level expressions combined with top-level tube ('|').
	 * </ul>
	 * @return current state of this automaton.
	 */
	public int getState() {
		return state;
	}
	
	/**
	 * Shorthand for <code>getState() == STATE_ERROR</code>.
	 * @see #getState()
	 * @return <code>true</code> if this automaton is in the error state. 
	 */
	public boolean isError() {
		return state == STATE_ERROR;
	}
	
	/**
	 * Shorthand for <code>getState() != STATE_UNDEFINED</code>.
	 * @see #getState()
	 * @return <code>true</code> if this automaton is in the final state. 
	 */
	public boolean isDefined() {
		return state != STATE_UNDEFINED;
	}
	
	/**
	 * Consumes some characters from passed {@link CharSequence}. Consuming is proceed 
	 * until the end of {@link CharSequence} or until this automaton is greedy switched 
	 * to the defined state.
	 * 
	 * @param input sequence.
	 * @param offset from which to start to consume
	 * @return number of characters consumed.
	 */
	public int consume(CharSequence input, int offset) {
		return 0;// TODO
	}

	/**
	 * Consumes some characters from passed char array. Consuming is proceed 
	 * until the end of array or until this automaton is greedy switched 
	 * to the defined state.
	 * 
	 * @param input array.
	 * @param offset from which to start to consume
	 * @return number of characters consumed.
	 */
	public int consume(char[] input, int offset) {
		return 0;// TODO
	}
	
	/**
	 * Resets the state of the automaton to the {@link #STATE_UNDEFINED}.
	 * This allows to consume more input using this automaton.
	 */
	public void reset() {
		start = current;
		state = STATE_UNDEFINED;
	}

	@Override
	public int hashCode() {
		return pattern.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof RegularExpression) {
			RegularExpression other = (RegularExpression)obj;
			return other.pattern.equals(pattern);
		}
		return false;
	}

	@Override
	public String toString() {
		return pattern;
	}

	/**
	 * Creates clone of this pattern. This is fast way to ensure thread-safety 
	 * of this class. This operation is very cheap and shares everything it can share
	 * providing a lot of memory usage optimization.
	 */
	@Override
	public RegularExpression clone() throws CloneNotSupportedException {
		return (RegularExpression) super.clone();
	}

	private static class Node {}
	
	private static class RootNode extends Node {

		public void addNextState(Node next) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private static class Parser {
		private String value;
		
		Parser(String value) {
			
		}

		public Node next() {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean hasNext() {
			// TODO Auto-generated method stub
			return false;
		}
	}
}
