package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import roborally.*;
import roborally.EnergyAmount.Unit;
import roborally.program.command.Command;
import roborally.program.command.IfCommand;
import roborally.program.command.MoveCommand;
import roborally.program.command.TurnCommand;
import roborally.program.condition.EnergyAtLeastCondition;

public class IfTest {

	private Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(Orientation.RIGHT, 600);
	}

	@Test
	public void step_MoveOrTurn() throws IllegalArgumentException, IllegalStateException, InvalidPositionException,
			InvalidSizeException {
		Board board = new Board(20, 20);
		robot.placeOnBoard(board, new Vector(10, 10));

		IfCommand ifc = new IfCommand();
		EnergyAtLeastCondition condition = new EnergyAtLeastCondition();
		condition.apply(500);
		ifc.apply(condition);

		Command thenCommand = new MoveCommand();
		TurnCommand elseCommand = new TurnCommand();
		elseCommand.apply("clockwise");

		ifc.apply(thenCommand);
		ifc.apply(elseCommand);

		assertTrue(ifc.step(robot));
		ifc.execute(robot);
		assertEquals(100, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
		assertEquals(new Vector(11, 10), robot.getPosition());
		assertEquals(Orientation.RIGHT, robot.getOrientation());

		assertFalse(ifc.step(robot));

		assertTrue(ifc.step(robot));
		ifc.execute(robot);
		assertEquals(0, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
		assertEquals(new Vector(11, 10), robot.getPosition());
		assertEquals(Orientation.DOWN, robot.getOrientation());
	}

}
