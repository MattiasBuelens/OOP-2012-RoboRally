package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.InvalidPositionException;
import roborally.Orientation;
import roborally.Robot;

public class RobotEnergyRequiredTest {

	Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(3, 7, Orientation.RIGHT, 500);
	}

	@Test
	public void getEnergyRequiredToReach_NormalCase() throws InvalidPositionException {
		Robot robot = new Robot(0, 0, Orientation.DOWN, 9999);
		int expectedTurns = 1;
		int expectedSteps = 10;
		double expectedCost = robot.getStepCost() * expectedSteps + robot.getTurnCost() * expectedTurns;

		assertEquals(expectedCost, robot.getEnergyRequiredToReach(5, 5), 0.1);
	}

	@Test
	public void getEnergyRequiredToReach_CurrentPosition() throws InvalidPositionException {
		Robot robot = new Robot(0, 0, Orientation.DOWN, 500);
		assertEquals(0, robot.getEnergyRequiredToReach(0, 0), 0.1);
	}

}
