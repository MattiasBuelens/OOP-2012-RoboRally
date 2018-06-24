package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.InvalidPositionException;
import roborally.Orientation;
import roborally.Robot;

public class RobotPositionTest {

	Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(3, 7, Orientation.RIGHT, 500);
	}

	@Test
	public void getPosition() {
		assertEquals(3, robot.getX());
		assertEquals(7, robot.getY());
	}

	@Test
	public void setPosition_NormalCase() throws InvalidPositionException {
		robot.setX(5);
		assertEquals(5, robot.getX());

		robot.setY(8);
		assertEquals(8, robot.getY());
	}

	@Test(expected = InvalidPositionException.class)
	public void setPosition_InvalidX() throws InvalidPositionException {
		robot.setX(-5);
		fail();
	}

	@Test(expected = InvalidPositionException.class)
	public void setPosition_InvalidY() throws InvalidPositionException {
		robot.setY(-13);
		fail();
	}
}
