package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.InvalidPositionException;
import roborally.Orientation;
import roborally.Robot;

public class RobotMoveTest {

	Robot robotEnergy500;
	Robot robotEnergy50;
	Robot robotInCorner;

	@Before
	public void setUp() throws Exception {
		robotEnergy500 = new Robot(3, 7, Orientation.RIGHT, 500);
		robotEnergy50 = new Robot(1, 2, Orientation.RIGHT, 50);
		robotInCorner = new Robot(0, 0, Orientation.UP, 500);
	}

	@Test
	public void move_NormalCase() throws InvalidPositionException {
		robotEnergy500.move();
		assertEquals(4, robotEnergy500.getX());
		assertEquals(7, robotEnergy500.getY());
		assertEquals(Orientation.RIGHT, robotEnergy500.getOrientation());
		assertEquals(0, robotEnergy500.getEnergy(), 0.1);
	}

	@Test(expected = AssertionError.class)
	public void move_InsufficientEnergy() throws InvalidPositionException {
		robotEnergy50.move();
		fail();
	}

	@Test(expected = InvalidPositionException.class)
	public void move_InvalidPosition() throws InvalidPositionException {
		robotInCorner.move();
		fail();
	}
}
