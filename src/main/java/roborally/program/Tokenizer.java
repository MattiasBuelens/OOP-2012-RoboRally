package roborally.program;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A tokenizer which takes a program source
 * and produces a sequence of tokens representing
 * the individual components of the program.
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
public class Tokenizer {

	public Tokenizer(CharSequence source) {
		this.source = source;
	}

	private final CharSequence source;

	public int getLastIndex() {
		return lastIndex;
	}

	private int lastIndex;

	public Token nextToken() {
		Token token = null;

		// Check all token matchers for a match
		for (TokenMatcher tokenMatcher : TokenMatcher.values()) {
			Matcher matcher = tokenMatcher.matcher(source);
			// Find a match starting at the last index
			if (matcher.find(lastIndex) && lastIndex == matcher.start()) {
				// Parse token
				token = tokenMatcher.parse(matcher.toMatchResult());
				// Move index after last match
				lastIndex = matcher.end();
				break;
			}
		}

		return token;
	}

	/**
	 * An enumeration of matchers for program tokens.
	 */
	enum TokenMatcher {

		/**
		 * Matches an open token, such as
		 * {@code "(while"} or {@code "(at-robot"}.
		 */
		OPEN("\\s*\\(([a-z\\-]+)") {

			@Override
			public Token parse(MatchResult result) {
				String name = result.group(1).toLowerCase();
				return new OpenToken(name);
			}

		},

		/**
		 * Matches a close token {@code ")"}.
		 */
		CLOSE("\\s*\\)") {

			@Override
			public Token parse(MatchResult result) {
				return new CloseToken();
			}

		},

		/**
		 * Matches an end-of-file token.
		 */
		EOF("\\s*$") {
			@Override
			public Token parse(MatchResult result) {
				return new EndOfFileToken();
			}
		},

		/**
		 * Matches a value token, such as
		 * {@code 1000} or {@code clockwise}.
		 */
		VALUE("\\s*([^\\(\\)]+)") {

			@Override
			public Token parse(MatchResult result) {
				String value = result.group(1);
				return new ValueToken(value);
			}

		};

		private TokenMatcher(Pattern pattern) {
			this.pattern = pattern;
		}

		private TokenMatcher(String regex) {
			this(Pattern.compile(regex, Pattern.CASE_INSENSITIVE));
		}

		/**
		 * Variable registering the pattern of this matcher.
		 */
		private final Pattern pattern;

		/**
		 * Get a pattern matcher for the given input.
		 * 
		 * @param input
		 * 			The input to match on.
		 * @return	A pattern matcher for this token matcher.
		 */
		public Matcher matcher(CharSequence input) {
			return pattern.matcher(input);
		}

		/**
		 * Parse a token from a pattern match result.
		 * 
		 * @param result
		 * 			The pattern match result.
		 * 
		 * @return	The constructed token.
		 */
		public abstract Token parse(MatchResult result);

	}

}
