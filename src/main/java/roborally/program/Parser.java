package roborally.program;

import java.text.ParseException;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

import roborally.program.command.Command;
import roborally.util.Stack;

/**
 * A parser which takes a character sequence
 * and outputs a command.
 * 
 * @author	Mattias Buelens
 * @author	Thomas Goossens
 * @version	3.0
 * 
 * @note	This class is part of the 2012 project for
 * 			the course Object Oriented Programming in
 * 			the second phase of the Bachelor of Engineering
 * 			at KU Leuven, Belgium.
 */
public class Parser {

	/**
	 * Create a new parser for the given program source.
	 * 
	 * @param source
	 * 			The program source.
	 * 
	 * @post	The new parser's source is set to the given source.
	 * 			| new.getSource() == source
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given source is not effective.
	 * 			| source == null
	 */
	public Parser(CharSequence source) throws IllegalArgumentException {
		if (source == null)
			throw new IllegalArgumentException("Program source must be effective.");
		this.source = source;
	}

	/**
	 * Get the program source this parser operates on.
	 */
	@Basic
	@Immutable
	public CharSequence getSource() {
		return source;
	}

	/**
	 * Variable registering the program source of this parser.
	 */
	private final CharSequence source;

	/**
	 * Get the tokenizer this parser works with.
	 */
	private Tokenizer getTokenizer() {
		if (tokenizer == null)
			tokenizer = new Tokenizer(getSource());
		return tokenizer;
	}

	/**
	 * Variable registering the tokenizer of this parser.
	 */
	private Tokenizer tokenizer;

	/**
	 * Parse this parser's source to a program command.
	 * 
	 * @return	The parsed program command which source
	 * 			representation is equivalent with
	 * 			this parser's source.
	 * 
	 * @throws	ParseException
	 * 			If this parser's source is invalid.
	 */
	public Command parse() throws ParseException {
		Tokenizer tokenizer = getTokenizer();
		Stack<Statement> stack = new Stack<Statement>();

		Token token = tokenizer.nextToken();
		// First token must be an open token
		checkOrThrow(token instanceof OpenToken, "Expected statement as first token.");
		Statement root = parseStatement((OpenToken) token);
		// Root must be a command
		checkOrThrow(root instanceof Command, "Expected command as first statement.");
		stack.offer(root);

		while ((token = tokenizer.nextToken()) != null) {
			if (token instanceof OpenToken) {
				// Parse as statement
				Statement stmt = parseStatement((OpenToken) token);
				// Push on the stack
				stack.offer(stmt);
			} else if (token instanceof CloseToken) {
				// Pop off the stack
				Statement stmt = stack.poll();
				checkNotNull(stmt, "Unexpected close token.");
				if (stack.isEmpty())
					continue;
				// Apply on top of the stack
				Statement top = stack.peek();
				checkNotNull(top, "Unexpected close token.");
				checkOrThrow(top.canApply(stmt), "%s could not be applied to %s.", stmt.getClass().getSimpleName(), top
						.getClass().getSimpleName());
				top.apply(stmt);
			} else if (token instanceof ValueToken) {
				// Get value
				Object value = ((ValueToken) token).getValue();
				// Apply on top of the stack
				Statement top = stack.peek();
				checkNotNull(top, "Unexpected value token.");
				checkOrThrow(top.canApply(value), "Value %s could not be applied to %s.", value, top.getClass()
						.getSimpleName());
				top.apply(value);
			} else if (token instanceof EndOfFileToken) {
				// Stack must be empty at end of file
				checkOrThrow(stack.isEmpty(), "Unexpected end of file.");
				break;
			}
		}

		assert stack.isEmpty();
		return (Command) root;
	}

	/**
	 * Parse a statement from an open token.
	 * 
	 * @param token
	 * 			The open token.
	 * 
	 * @return	A statement constructed from the open token.
	 * 
	 * @throws	ParseException
	 * 			If no statement exists with the open token's name.
	 */
	public Statement parseStatement(OpenToken token) throws ParseException {
		for (StatementMatcher matcher : StatementMatcher.values()) {
			if (matcher.matches(token)) {
				return matcher.create();
			}
		}

		doThrow("Unknown statement: %s", token.getName());
		return null;
	}

	/**
	 * Throw a parse exception.
	 * 
	 * @param message
	 * 			The exception message.
	 * @param args
	 * 			The arguments to fill in the message.
	 * 
	 * @throws	ParseException
	 * 			The requested parse exception.
	 */
	private void doThrow(String message, Object... args) throws ParseException {
		throw new ParseException(String.format(message, args), getTokenizer().getLastIndex());
	}

	/**
	 * Check a condition and throw a parse exception
	 * if it is not met.
	 * 
	 * @param test
	 * 			The condition to test.
	 * @param message
	 * 			The exception message.
	 * @param args
	 * 			The arguments to fill in the message.
	 * 
	 * @see #doThrow(String, Object...)
	 */
	private void checkOrThrow(boolean test, String message, Object... args) throws ParseException {
		if (!test)
			doThrow(message, args);
	}

	/**
	 * Check if the given object is effective
	 * and throw a parse exception if it is not.
	 * 
	 * @param obj
	 * 			The object to check.
	 * @param message
	 * 			The exception message.
	 * @param args
	 * 			The arguments to fill in the message.
	 * 
	 * @see #checkOrThrow(boolean, String, Object...)
	 */
	private void checkNotNull(Object obj, String message, Object... args) throws ParseException {
		checkOrThrow(obj != null, message, args);
	}

}
