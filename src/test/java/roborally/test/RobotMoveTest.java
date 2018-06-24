package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import roborally.Board;
import roborally.Orientation;
import roborally.Robot;
import roborally.Vector;

public class RobotMoveTest {

	Robot robotEnergy500, robotEnergy50, robotInCorner;
	Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);
		robotEnergy500 = new Robot(Orientation.RIGHT, 500);
		robotEnergy500.placeOnBoard(board, new Vector(3, 7));
		robotEnergy50 = new Robot(Orientation.RIGHT, 50);
		robotEnergy50.placeOnBoard(board, new Vector(1, 2));
		robotInCorner = new Robot(Orientation.UP, 500);
		robotInCorner.placeOnBoard(board, new Vector(0, 0));
	}

	@Test
	public void move_NormalCase() {
		robotEnergy500.move();

		assertEquals(new Vector(4, 7), robotEnergy500.getPosition());
		assertEquals(Orientation.RIGHT, robotEnergy500.getOrientation());
		assertEquals(0, robotEnergy500.getEnergy(), 0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void move_InvalidPosition() {
		robotInCorner.move();
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void move_NotPlaced() {
		robotEnergy500.removeFromBoard();
		robotEnergy500.move();
		fail();
	}

	@Test
	public void canMove() {
		assertTrue(robotEnergy500.canMove());
		assertFalse(robotEnergy50.canMove());
	}
}
