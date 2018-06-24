package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.Orientation;
import roborally.Robot;

public class RobotOrientationTest {

	Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(3, 7, Orientation.RIGHT, 500);
	}

	@Test
	public void getOrientation() {
		assertEquals(Orientation.RIGHT, robot.getOrientation());
	}

	@Test
	public void setOrientation() {
		robot.setOrientation(Orientation.UP);
		assertEquals(Orientation.UP, robot.getOrientation());

		robot.setOrientation(3);
		assertEquals(Orientation.LEFT, robot.getOrientation());
	}

	@Test
	public void setOrientation_Null() {
		robot.setOrientation(null);
		assertEquals(Orientation.UP, robot.getOrientation());
	}
}
