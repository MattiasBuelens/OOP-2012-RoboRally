package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.Board;
import roborally.Orientation;
import roborally.Robot;
import roborally.Vector;

public class RobotOrientationTest {

	Robot robot;
	Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);
		robot = new Robot(Orientation.RIGHT, 500);
		robot.placeOnBoard(board, new Vector(3, 7));
	}

	@Test
	public void getOrientation() {
		assertEquals(Orientation.RIGHT, robot.getOrientation());
	}

	@Test
	public void setOrientation() {
		robot.setOrientation(Orientation.UP);
		assertEquals(Orientation.UP, robot.getOrientation());
	}

	@Test
	public void setOrientation_Null() {
		robot.setOrientation(null);
		assertEquals(Orientation.UP, robot.getOrientation());
	}
}
