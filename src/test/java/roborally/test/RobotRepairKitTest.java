package roborally.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import roborally.*;

public class RobotRepairKitTest {

	Robot carrierBot, terminatedBot;
	Board board;
	RepairKit repair;
	public double repairKitCapacity = 2000.0;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);

		carrierBot = new Robot(Orientation.RIGHT, 500);
		carrierBot.placeOnBoard(board, Vector.ZERO);

		repair = new RepairKit(100, repairKitCapacity);
		repair.placeOnBoard(carrierBot.getBoard(), carrierBot.getPosition());

		carrierBot.pickUp(repair);

	}

	/*
	 * Pick up items
	 */

	@Test
	public void use_Repairkit_NotFullCapacity() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {

		// Current robot capacity
		EnergyAmount robotCapacity = carrierBot.getCapacityAmount();

		// Current repair kit capacity
		EnergyAmount repairCapacity = repair.getCapacityAmount();

		// Hit the robot to drain some capacity
		carrierBot.hit();

		// Use the repair kit
		carrierBot.use(repair);

		// Robot has recharged some capacity after taking the hit
		EnergyAmount afterhit = robotCapacity.subtract(carrierBot.getHitDamage());
		assertEquals(carrierBot.getCapacityAmount(), afterhit.add(repairCapacity.multiply(repair.getRechargeGain())));

		// Repair kit is fully drained
		assertEquals(EnergyAmount.ZERO, repair.getCapacityAmount());
	}

	@Test
	public void use_Repairkit_FullCapacity() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {

		// Current robot capacity
		EnergyAmount robotCapacity = carrierBot.getCapacityAmount();

		// Current repair kit capacity
		EnergyAmount repairCapacity = repair.getCapacityAmount();

		// Robot is at its maximum capacity
		assertEquals(carrierBot.getMaximumCapacity(), robotCapacity);

		// Use the repair kit
		carrierBot.use(repair);

		// Robot is still at its maximum capacity
		assertEquals(robotCapacity, carrierBot.getCapacityAmount());

		// Repair kit is left unchanged
		assertEquals(repairCapacity, repair.getCapacityAmount());
	}

}