package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import roborally.Robot;

public class RobotEnergyTest {

	Robot robot;

	@Before
	public void setUp() throws Exception {
		robot = new Robot(3, 7, /* Orientation.RIGHT */ 1, 500);
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
	
	@Test
	public void setEnergy_NegativeEnergy() {
		double energy = -5;
		if(robot.isValidEnergy(energy)) {
			robot.setEnergy(energy);
			fail();
		}
		// Energy unchanged
		assertEquals(500, robot.getEnergy(), 0.1);
	}
	
	@Test
	public void setEnergy_Overflow() {
		double energy = 1e6;
		if(robot.isValidEnergy(energy)) {
			robot.setEnergy(energy);
			fail();
		}
		// Energy unchanged
		assertEquals(500, robot.getEnergy(), 0.1);
	}
	
	@Test
	public void recharge_NormalCase() {
		robot.recharge(500);
		assertEquals(1000, robot.getEnergy(), 0.1);
	}
	
	@Test
	public void drain_NormalCase() {
		robot.drain(500);
		assertEquals(0, robot.getEnergy(), 0.1);
	}
}
