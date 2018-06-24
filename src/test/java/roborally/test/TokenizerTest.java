package roborally.test;

import static org.junit.Assert.*;

import org.junit.Test;

import roborally.program.CloseToken;
import roborally.program.OpenToken;
import roborally.program.Token;
import roborally.program.Tokenizer;
import roborally.program.ValueToken;

public class TokenizerTest {

	// @formatter:off
	private final String source1
		= "(while\n"
		+ "  (energy-at-least 1000)\n"
		+ "  (seq\n"
		+ "    (move)\n"
		+ "    (turn clockwise)\n"
		+ "  )\n"
		+ ")\n";
	// @formatter:on

	@Test
	public void tokenize_Source1() {
		Tokenizer tokenizer = new Tokenizer(source1);

		// (while
		Token token = tokenizer.nextToken();
		assertEquals("while", ((OpenToken) token).getName());

		// (energy-at-least 1000)
		token = tokenizer.nextToken();
		assertEquals("energy-at-least", ((OpenToken) token).getName());
		token = tokenizer.nextToken();
		assertEquals("1000", ((ValueToken) token).getValue());
		token = tokenizer.nextToken();
		assertEquals(CloseToken.class, token.getClass());

		// (seq
		token = tokenizer.nextToken();
		assertEquals("seq", ((OpenToken) token).getName());

		// (move)
		token = tokenizer.nextToken();
		assertEquals("move", ((OpenToken) token).getName());
		token = tokenizer.nextToken();
		assertEquals(CloseToken.class, token.getClass());

		// (turn clockwise)
		token = tokenizer.nextToken();
		assertEquals("turn", ((OpenToken) token).getName());
		token = tokenizer.nextToken();
		assertEquals("clockwise", ((ValueToken) token).getValue());
		token = tokenizer.nextToken();
		assertEquals(CloseToken.class, token.getClass());

		// ) # close seq
		token = tokenizer.nextToken();
		assertEquals(CloseToken.class, token.getClass());

		// ) # close while
		token = tokenizer.nextToken();
		assertEquals(CloseToken.class, token.getClass());
	}

}
