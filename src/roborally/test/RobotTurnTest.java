package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.Orientation;
import roborally.Robot;

public class RobotTurnTest {

	Robot robotEnergy500;
	Robot robotEnergy50;

	@Before
	public void setUp() throws Exception {
		robotEnergy500 = new Robot(3, 7, Orientation.RIGHT, 500);
		robotEnergy50 = new Robot(1, 2, Orientation.RIGHT, 50);
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

	@Test(expected = AssertionError.class)
	public void turnClockwise_InsufficientEnergy() {
		robotEnergy50.turnClockwise();
		fail();
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

	@Test(expected = AssertionError.class)
	public void turnCounterClockwise_InsufficientEnergy() {
		robotEnergy50.turnCounterClockwise();
		fail();
	}
}
