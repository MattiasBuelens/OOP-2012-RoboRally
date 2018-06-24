package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import roborally.*;
import roborally.EnergyAmount.Unit;

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
		assertEquals(500, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
	}

	@Test
	public void getCapacity() {
		assertEquals(20000, robot.getCapacityAmount(Unit.WATTSECOND), 0.1);
	}

	@Test
	public void getEnergyFraction() {
		double capacity = robot.getCapacityAmount(Unit.WATTSECOND);
		assertEquals(500 / capacity, robot.getEnergyFraction(), 0.1);
	}

	@Test
	public void setEnergy_NormalCase() {
		robot.setEnergy(new EnergyAmount(1337, Unit.WATTSECOND));
		assertEquals(1337, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
	}

	@Test(expected = IllegalStateException.class)
	public void setEnergy_Terminated() {
		robot.terminate();
		robot.setEnergy(new EnergyAmount(1337, Unit.WATTSECOND));
		fail();
	}

	@Test
	public void setEnergy_NegativeEnergy() {
		EnergyAmount energy = new EnergyAmount(-5, Unit.WATTSECOND);
		if (robot.isValidEnergy(energy)) {
			robot.setEnergy(energy);
			fail();
		}
		// Energy unchanged
		assertEquals(500, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
	}

	@Test
	public void setEnergy_Overflow() {
		EnergyAmount energy = new EnergyAmount(1e6, Unit.WATTSECOND);
		if (robot.isValidEnergy(energy)) {
			robot.setEnergy(energy);
			fail();
		}
		// Energy unchanged
		assertEquals(500, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
	}

	@Test
	public void isValidMaximumEnergy() {
		assertTrue(robot.isValidCapacity(new EnergyAmount(100, Unit.WATTSECOND)));
		assertTrue(robot.isValidCapacity(robot.getMaximumCapacity()));
		assertFalse(robot.isValidCapacity(new EnergyAmount(Long.MAX_VALUE, Unit.WATTSECOND)));
		assertFalse(robot.isValidCapacity(EnergyAmount.ZERO));
		assertFalse(robot.isValidCapacity(new EnergyAmount(-100, Unit.WATTSECOND)));
	}

	/*
	 * Recharge and drain
	 */

	@Test
	public void recharge_NormalCase() {
		EnergyAmount amount = new EnergyAmount(500, Unit.WATTSECOND);
		assertTrue(robot.canRecharge(amount));
		robot.recharge(amount);
		assertEquals(1000, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
	}

	@Test
	public void recharge_IllegalCases() {
		assertFalse(robot.canRecharge(new EnergyAmount(-2, Unit.WATTSECOND)));
		robot.terminate();
		assertFalse(robot.canRecharge(new EnergyAmount(2, Unit.WATTSECOND)));
	}

	@Test
	public void drain_NormalCase() {
		EnergyAmount amount = new EnergyAmount(500, Unit.WATTSECOND);
		assertTrue(robot.canDrain(amount));
		robot.drain(amount);
		assertEquals(EnergyAmount.ZERO, robot.getEnergyAmount());
	}

	@Test
	public void drain_IllegalCases() {
		assertFalse(robot.canDrain(new EnergyAmount(-2, Unit.WATTSECOND)));
		robot.terminate();
		assertFalse(robot.canDrain(new EnergyAmount(2, Unit.WATTSECOND)));
	}

	/*
	 * Transfer
	 */

	@Test
	public void transfer_NormalCase() {
		Battery battery = new Battery(2);

		EnergyAmount batteryEnergy = battery.getEnergyAmount();
		EnergyAmount robotEnergy = robot.getEnergyAmount();

		assertTrue(battery.canTransfer(robot, batteryEnergy));

		battery.transfer(robot, batteryEnergy);

		assertEquals(robotEnergy.subtract(batteryEnergy), robot.getEnergyAmount());
		assertEquals(EnergyAmount.ZERO, battery.getEnergyAmount());
	}

	@Test
	public void transfer_FromRobot() {
		Battery battery = new Battery(2);
		assertFalse(robot.canTransfer(battery, EnergyAmount.ZERO));
	}

}
