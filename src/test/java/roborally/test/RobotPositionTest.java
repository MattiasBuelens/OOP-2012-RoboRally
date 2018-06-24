package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.Board;
import roborally.InvalidPositionException;
import roborally.Orientation;
import roborally.Robot;
import roborally.Vector;

public class RobotPositionTest {

	Robot robot;
	Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);
		robot = new Robot(Orientation.RIGHT, 500);
		robot.placeOnBoard(board, new Vector(3, 7));
	}

	@Test
	public void getPosition() {
		assertEquals(new Vector(3, 7), robot.getPosition());
	}

	@Test
	public void setPosition_NormalCase() throws InvalidPositionException {
		robot.moveOnBoard(new Vector(5, 8));
		assertEquals(new Vector(5, 8), robot.getPosition());
	}

	@Test(expected = IllegalArgumentException.class)
	public void setPosition_InvalidPosition() throws InvalidPositionException {
		robot.moveOnBoard(new Vector(-5, -8));
		fail();
	}

}
