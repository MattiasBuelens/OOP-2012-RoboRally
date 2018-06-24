package roborally.test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;

import roborally.*;
import roborally.EnergyAmount.Unit;
import roborally.program.Parser;
import roborally.program.command.*;
import roborally.program.condition.EnergyAtLeastCondition;

public class ParserTest {

	private Robot robot;
	private Board board;

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

	// @formatter:off
	private final String source2
		  	= "(while (not (not (true))) (if (at-item) (pickup-and-use) (move) ) )";
	// @formatter:on

	// @formatter:off
	private final String source3
			= "(seq\n"
			+ "  (seq\n"
			+ "    (seq\n"
			+ "      (move)\n"
			+ "      (move)\n"
			+ "    )\n"
			+ "    (turn clockwise)\n"
			+ "    (turn clockwise)\n"
			+ "  )\n"
			+ "  (move)\n"
			+ "  (move)\n"
			+ ")\n";
	// @formatter:on

	// @formatter:off
	private final String source4
			= "(seq\n"
			+ "  (move)\n"
			+ "  (while\n"
			+ "    (at-item)\n"
			+ "    (pickup-and-use)\n"
			+ "  )\n"
			+ "  (move)\n"
			+ ")\n";
		// @formatter:on

	@Before
	public void setUp() throws Exception {
		board = new Board(50, 50);
		robot = new Robot(Orientation.RIGHT, 1000);
		robot.placeOnBoard(board, new Vector(10, 10));
	}

	@Test
	public void parse_Source1() throws ParseException {
		Parser parser = new Parser(source1);
		Command program = parser.parse();
		assertNotNull(program);

		// While
		assertEquals(WhileCommand.class, program.getClass());
		WhileCommand loop = (WhileCommand) program;
		assertEquals(EnergyAtLeastCondition.class, loop.getCondition().getClass());
		assertEquals(1000, ((EnergyAtLeastCondition) loop.getCondition()).getMinimumEnergy(), 0.1);

		// Sequence
		assertEquals(SequenceCommand.class, loop.getCommand().getClass());
		SequenceCommand seq = (SequenceCommand) loop.getCommand();
		assertEquals(2, seq.getNbCommands());
		assertEquals(MoveCommand.class, seq.getCommandAt(1).getClass());
		assertEquals(TurnCommand.class, seq.getCommandAt(2).getClass());
		assertEquals(Rotation.CLOCKWISE, ((TurnCommand) seq.getCommandAt(2)).getRotation());
	}

	/*
	 * Robot starts with 3399 Ws at (10, 10).
	 * 
	 * Robot moves to the right and turns clockwise. Robot's energy is 2799 Ws.
	 * Robot repeats this another three times. Robot's energy decreases to 2199
	 * Ws, 1599 Ws and 999 Ws. Program ends.
	 */
	@Test
	public void execute_Source1() throws ParseException {
		Parser parser = new Parser(source1);
		Command program = parser.parse();
		assertNotNull(program);

		final int nbCycles = 4;
		final int nbLoopCommands = 2;
		double energy = 1000 - 1 + 600 * nbCycles;
		robot.setEnergy(new EnergyAmount(energy, Unit.WATTSECOND));

		for (int i = 1; i <= nbCycles * nbLoopCommands; ++i) {
			assertTrue(program.step(robot));
			program.execute(robot);
		}

		// Robot ends in same position with same orientation
		// and 999 Ws energy
		assertEquals(1000 - 1, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
		assertEquals(new Vector(10, 10), robot.getPosition());
		assertEquals(Orientation.RIGHT, robot.getOrientation());
	}

	/*
	 * Robot starts with 1500 Ws at (10, 10). Battery with 500 Ws is located at
	 * (13,10).
	 * 
	 * Robot moves to the right. It detects that an item is at (11, 10) and
	 * picks it up. Robot's energy increases from 1000 Ws to 1500 Ws. Robot
	 * moves to the right three more times. Program stays in infinite loop.
	 */
	@Test
	public void execute_Source2() throws Exception {
		Parser parser = new Parser(source2);
		Command program = parser.parse();
		assertNotNull(program);

		Battery b = new Battery(1, 500);
		b.placeOnBoard(robot.getBoard(), new Vector(13, 10));

		double energy = 3 * 500;
		robot.setEnergy(new EnergyAmount(energy, Unit.WATTSECOND));
		for (int i = 0; i < 5; i++) {
			assertTrue(program.step(robot));
			program.execute(robot);
		}

		// Robot reaches final position after 5 steps
		assertEquals(new Vector(14, 10), robot.getPosition());

		// Infinite loop, equivalent with (while (true) (move))
		// No further effects on robot
		for (int i = 0; i < 10; i++) {
			assertTrue(program.step(robot));
			program.execute(robot);
		}
		assertTrue(program.step(robot));

	}

	/*
	 * Robot starts with 2200 Ws at (10, 10).
	 * 
	 * Robot moves to the right twice, turns clockwise twice and moves to the
	 * left twice. Robot ends up in its original position facing left. Program
	 * ends.
	 */
	@Test
	public void execute_Source3() throws ParseException, IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Parser parser = new Parser(source3);
		Command program = parser.parse();
		assertNotNull(program);

		double energy = 4 * 500 + 2 * 100;
		robot.setEnergy(new EnergyAmount(energy, Unit.WATTSECOND));
		for (int i = 0; i < 6; i++) {
			assertTrue(program.step(robot));
			program.execute(robot);
		}

		assertFalse(program.step(robot));
		assertEquals(new Vector(10, 10), robot.getPosition());
		assertEquals(Orientation.LEFT, robot.getOrientation());
		assertEquals(0, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);

	}

	/*
	 * Robot starts with 1000 Ws at (10, 10). Battery with 500 Ws is located at
	 * (11,10).
	 * 
	 * Robot first moves to the right, consuming 500 Ws. While loop is entered
	 * and the item is picked up and used. Robot's energy increases from 0 Ws to
	 * 500 Ws. Robot moves to the right, consuming 500 Ws. Program ends.
	 */
	@Test
	public void execute_Source4() throws ParseException, IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Parser parser = new Parser(source4);
		Command program = parser.parse();
		assertNotNull(program);

		Battery b = new Battery(1, 500);
		b.placeOnBoard(robot.getBoard(), new Vector(11, 10));

		double energy = 500;
		robot.setEnergy(new EnergyAmount(energy, Unit.WATTSECOND));
		for (int i = 0; i < 3; i++) {
			assertTrue(program.step(robot));
			program.execute(robot);
		}

		assertFalse(program.step(robot));
		assertEquals(new Vector(12, 10), robot.getPosition());

	}
}
