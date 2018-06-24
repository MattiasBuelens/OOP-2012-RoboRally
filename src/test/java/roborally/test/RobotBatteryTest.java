package roborally.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import roborally.*;

public class RobotBatteryTest {

	Robot normalBot, terminatedBot, carrierBot;
	Board board;
	Battery batteryCarried;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);
		normalBot = new Robot(Orientation.RIGHT, 500);
		normalBot.placeOnBoard(board, new Vector(3, 7));
		terminatedBot = new Robot(Orientation.RIGHT, 50);
		terminatedBot.placeOnBoard(board, new Vector(3, 3));
		terminatedBot.terminate();

		carrierBot = new Robot(Orientation.RIGHT, 500);
		carrierBot.placeOnBoard(board, Vector.ZERO);
		batteryCarried = new Battery(5);
		batteryCarried.placeOnBoard(board, Vector.ZERO);

		carrierBot.pickUp(batteryCarried);
	}

	/*
	 * Pick up items
	 */

	@Test
	public void use_Battery() {
		// Robot possesses item, uses battery
		EnergyAmount oldEnergy = carrierBot.getEnergyAmount();
		EnergyAmount batteryEnergy = batteryCarried.getEnergyAmount();
		carrierBot.use(batteryCarried);

		assertEquals(oldEnergy.subtract(batteryEnergy), carrierBot.getEnergyAmount());
		assertEquals(EnergyAmount.ZERO, batteryCarried.getEnergyAmount());
	}

}