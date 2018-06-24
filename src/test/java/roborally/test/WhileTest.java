package roborally.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import roborally.*;
import roborally.program.command.*;
import roborally.program.condition.Condition;
import roborally.program.condition.EnergyAtLeastCondition;
import roborally.program.condition.TrueCondition;

public class WhileTest {

	private Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(Orientation.RIGHT, 500);
	}

	@Test
	public void step_Always() throws InvalidSizeException, IllegalArgumentException, IllegalStateException,
			InvalidPositionException {

		WhileCommand loop = new WhileCommand();
		Condition condition = new TrueCondition();
		loop.apply(condition);
		Command command = new MoveCommand();
		loop.apply(command);

		assertFalse(loop.isInIteration());
		assertTrue(loop.step(robot));
		assertTrue(loop.isInIteration());
		assertTrue(loop.step(robot));
		assertTrue(loop.step(robot));
	}

	@Test
	public void step_Never() {
		WhileCommand loop = new WhileCommand();
		EnergyAtLeastCondition condition = new EnergyAtLeastCondition();
		condition.setMinimumEnergy(Double.POSITIVE_INFINITY);
		loop.apply(condition);
		Command command = new MoveCommand();
		loop.apply(command);

		assertFalse(loop.isInIteration());
		assertFalse(loop.step(robot));
		assertFalse(loop.isInIteration());
		assertFalse(loop.step(robot));
		assertFalse(loop.isInIteration());
	}

	@Test
	public void step_AlwaysSequence() {
		WhileCommand loop = new WhileCommand();
		Condition condition = new TrueCondition();
		loop.apply(condition);
		SequenceCommand seq = new SequenceCommand();
		seq.apply(new MoveCommand());
		seq.apply(new ShootCommand());
		loop.apply(seq);

		assertFalse(loop.isInIteration());
		assertNull(seq.getCurrentCommand());

		assertTrue(loop.step(robot));
		assertTrue(loop.isInIteration());
		assertEquals(MoveCommand.class, seq.getCurrentCommand().getClass());

		assertTrue(loop.step(robot));
		assertTrue(loop.isInIteration());
		assertEquals(ShootCommand.class, seq.getCurrentCommand().getClass());

		assertTrue(loop.step(robot));
		assertTrue(loop.isInIteration());
		assertEquals(MoveCommand.class, seq.getCurrentCommand().getClass());

		assertTrue(loop.step(robot));
		assertTrue(loop.isInIteration());
		assertEquals(ShootCommand.class, seq.getCurrentCommand().getClass());
	}

}
