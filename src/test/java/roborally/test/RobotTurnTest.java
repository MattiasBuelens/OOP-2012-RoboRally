package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.Board;
import roborally.Orientation;
import roborally.Robot;
import roborally.Vector;

public class RobotTurnTest {

	Robot robotEnergy500, robotEnergy50;
	Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);
		robotEnergy500 = new Robot(Orientation.RIGHT, 500);
		robotEnergy500.placeOnBoard(board, new Vector(3, 7));
		robotEnergy50 = new Robot(Orientation.RIGHT, 50);
		robotEnergy50.placeOnBoard(board, new Vector(1, 2));
	}

	@Test
	public void turnClockwise_NormalCase() {
		robotEnergy500.turnClockwise();
		assertEquals(Orientation.DOWN, robotEnergy500.getOrientation());
		assertEquals(400, robotEnergy500.getEnergy(), 0.1);

		robotEnergy500.turnClockwise();
		assertEquals(Orientation.LEFT, robotEnergy500.getOrientation());
		assertEquals(300, robotEnergy500.getEnergy(), 0.1);
	}

	@Test
	public void turnCounterClockwise_NormalCase() {
		robotEnergy500.turnCounterClockwise();
		assertEquals(Orientation.UP, robotEnergy500.getOrientation());
		assertEquals(400, robotEnergy500.getEnergy(), 0.1);

		robotEnergy500.turnCounterClockwise();
		assertEquals(Orientation.LEFT, robotEnergy500.getOrientation());
		assertEquals(300, robotEnergy500.getEnergy(), 0.1);
	}

	@Test
	public void canTurn() {
		assertTrue(robotEnergy500.canTurn());
		assertFalse(robotEnergy50.canTurn());
	}
}
