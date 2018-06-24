package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import roborally.*;

public class RobotEnergyTest {

	Robot robot;
	Board board;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(Orientation.RIGHT, 500);

		board = new Board(10, 10);
		robot.placeOnBoard(board, new Vector(3, 7));

	}

	@Test
	public void getEnergy() {
		assertEquals(500, robot.getEnergy(), 0.1);
	}

	@Test
	public void getMaxiumEnergy() {
		assertEquals(20000, robot.getMaximumEnergy(), 0.1);
	}

	@Test
	public void getEnergyFraction() {
		double maximumEnergy = robot.getMaximumEnergy();
		assertEquals(500 / maximumEnergy, robot.getEnergyFraction(), 0.1);
	}

	@Test
	public void setEnergy_NormalCase() {
		robot.setEnergy(1337);
		assertEquals(1337, robot.getEnergy(), 0.1);
	}

	@Test(expected = IllegalStateException.class)
	public void setEnergy_Terminated() {
		robot.terminate();
		robot.setEnergy(1337);
		fail();
	}

	@Test
	public void setEnergy_NegativeEnergy() {
		double energy = -5;
		if (robot.isValidEnergy(energy)) {
			robot.setEnergy(energy);
			fail();
		}
		// Energy unchanged
		assertEquals(500, robot.getEnergy(), 0.1);
	}

	@Test
	public void setEnergy_Overflow() {
		double energy = 1e6;
		if (robot.isValidEnergy(energy)) {
			robot.setEnergy(energy);
			fail();
		}
		// Energy unchanged
		assertEquals(500, robot.getEnergy(), 0.1);
	}

	@Test
	public void isValidMaximumEnergy() {
		assertTrue(Robot.isValidMaximumEnergy(100));
		assertTrue(Robot.isValidMaximumEnergy(Long.MAX_VALUE));
		assertFalse(Robot.isValidMaximumEnergy(0));
		assertFalse(Robot.isValidMaximumEnergy(-100));
	}

	/*
	 * Recharge and drain
	 */

	@Test
	public void recharge_NormalCase() {
		assertTrue(robot.canRecharge(500));
		robot.recharge(500);
		assertEquals(1000, robot.getEnergy(), 0.1);
	}

	@Test
	public void recharge_specialCases() {
		assertFalse(robot.canRecharge(-2));
		robot.terminate();
		assertFalse(robot.canRecharge(2));
	}

	@Test
	public void drain_NormalCase() {
		assertTrue(robot.canDrain(500));
		robot.drain(500);
		assertEquals(0, robot.getEnergy(), 0.1);
	}

	@Test
	public void drain_specialCases() {
		assertFalse(robot.canDrain(-2));
		robot.terminate();
		assertFalse(robot.canDrain(2));
	}

	/*
	 * Transfer
	 */

	@Test
	public void transfer_NormalCase() {
		Battery battery = new Battery(2);

		double batteryEnergy = battery.getEnergy();
		double robotEnergy = robot.getEnergy();

		assertTrue(battery.canTransfer(robot, battery.getEnergy()));

		battery.transfer(robot, battery.getEnergy());

		assertEquals(robotEnergy - batteryEnergy, robot.getEnergy(), 0.1);
		assertEquals(0, battery.getEnergy(), 0.1);
	}

	@Test
	public void transfer_FromRobot() {
		Battery battery = new Battery(2, 500);
		assertFalse(robot.canTransfer(battery, 100));
	}

}
