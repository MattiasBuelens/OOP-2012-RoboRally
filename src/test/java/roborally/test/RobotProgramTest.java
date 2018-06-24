package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import roborally.Orientation;
import roborally.Robot;
import roborally.Rotation;
import roborally.program.Program;
import roborally.program.command.Command;
import roborally.program.command.MoveCommand;
import roborally.program.command.PickupUseCommand;
import roborally.program.command.SequenceCommand;
import roborally.program.command.ShootCommand;
import roborally.program.command.TurnCommand;

public class RobotProgramTest {

	private Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(Orientation.RIGHT, 500);
	}

	@Test
	public void step_Simple() {
		SequenceCommand seq = new SequenceCommand();
		Command move1 = new MoveCommand();
		TurnCommand turn1 = new TurnCommand();
		turn1.setRotation(Rotation.CLOCKWISE);
		Command shot1 = new ShootCommand();
		Command pick1 = new PickupUseCommand();
		Command move2 = new MoveCommand();

		seq.apply(move1);
		seq.apply(turn1);
		seq.apply(shot1);
		seq.apply(pick1);
		seq.apply(move2);

		Program program = new Program(seq);

		robot.setProgram(program);
		assertNull(seq.getCurrentCommand());

		robot.stepProgram();
		assertEquals(move1, seq.getCurrentCommand());

		robot.stepProgram();
		assertEquals(turn1, seq.getCurrentCommand());

		robot.stepProgram();
		assertEquals(shot1, seq.getCurrentCommand());

		robot.stepProgram();
		assertEquals(pick1, seq.getCurrentCommand());

		robot.stepProgram();
		assertEquals(move2, seq.getCurrentCommand());
	}

	@Test
	public void step_Nested() {
		SequenceCommand seq1 = new SequenceCommand();

		Command move1 = new MoveCommand();
		TurnCommand turn1 = new TurnCommand();
		turn1.setRotation(Rotation.CLOCKWISE);
		seq1.apply(move1);
		seq1.apply(turn1);

		SequenceCommand seq2 = new SequenceCommand();
		Command shot1 = new ShootCommand();
		Command pick1 = new PickupUseCommand();
		seq2.apply(shot1);
		seq2.apply(pick1);
		seq1.apply(seq2);

		Command move2 = new MoveCommand();
		seq1.apply(move2);

		assertNull(seq1.getCurrentCommand());
		assertNull(seq2.getCurrentCommand());

		Program program = new Program(seq1);
		robot.setProgram(program);

		robot.stepProgram();
		assertEquals(move1, seq1.getCurrentCommand());

		robot.stepProgram();
		assertEquals(turn1, seq1.getCurrentCommand());

		robot.stepProgram();
		assertEquals(seq2, seq1.getCurrentCommand());
		assertEquals(shot1, seq2.getCurrentCommand());

		robot.stepProgram();
		assertEquals(seq2, seq1.getCurrentCommand());
		assertEquals(pick1, seq2.getCurrentCommand());

		robot.stepProgram();
		assertEquals(move2, seq1.getCurrentCommand());

		assertFalse(seq1.step(robot));
	}

	@Test
	public void stepN_NormalCase() {
		SequenceCommand seq = new SequenceCommand();
		Command move1 = new MoveCommand();
		TurnCommand turn1 = new TurnCommand();
		turn1.setRotation(Rotation.CLOCKWISE);
		Command shot1 = new ShootCommand();
		Command pick1 = new PickupUseCommand();
		Command move2 = new MoveCommand();

		seq.apply(move1);
		seq.apply(turn1);
		seq.apply(shot1);
		seq.apply(pick1);
		seq.apply(move2);

		Program program = new Program(seq);

		robot.setProgram(program);
		assertNull(seq.getCurrentCommand());

		robot.stepProgram(5);
		assertEquals(move2, seq.getCurrentCommand());

	}

	@Test(expected = IllegalStateException.class)
	public void stepN_NoProgram() {
		robot.stepProgram(5);
	}
}
