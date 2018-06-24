package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import roborally.*;
import roborally.EnergyAmount.Unit;

public class RobotShootTest {

	Robot robotEnergy1000, robotEnergy500;
	Board board;

	@Before
	public void setUp() throws Exception {
		board = new Board(10, 10);
		robotEnergy1000 = new Robot(Orientation.RIGHT, 1000);
		robotEnergy1000.placeOnBoard(board, new Vector(3, 7));
		robotEnergy500 = new Robot(Orientation.RIGHT, 500);
		robotEnergy500.placeOnBoard(board, new Vector(1, 2));
	}

	@Test
	public void shoot_NormalCase() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Robot hunter = new Robot(Orientation.RIGHT, 9000);
		hunter.placeOnBoard(board, new Vector(2, 5));

		Battery target1 = new Battery(4);
		EnergyAmount e1 = target1.getEnergyAmount();

		Battery target2 = new Battery(5);
		EnergyAmount e2 = target2.getEnergyAmount();
		Robot target3 = new Robot(Orientation.LEFT, 2000);
		EnergyAmount capacity = target3.getCapacityAmount();

		Robot almostDead = new Robot(Orientation.LEFT, 2000);
		almostDead.getCapacity().setAmount(new EnergyAmount(50, Unit.WATTSECOND));

		Battery unharmed1 = new Battery(2);
		EnergyAmount u1 = unharmed1.getEnergyAmount();
		Battery unharmed2 = new Battery(2);
		EnergyAmount u2 = unharmed2.getEnergyAmount();

		Vector target = new Vector(5, 5);
		target1.placeOnBoard(board, target);

		target2.placeOnBoard(board, target);
		target3.placeOnBoard(board, target);

		// This robot will die. Karma's a bitch.
		almostDead.placeOnBoard(board, target.add(new Vector(-1, 0)));

		unharmed1.placeOnBoard(board, target.add(new Vector(3, 0)));
		unharmed2.placeOnBoard(board, target.add(new Vector(-5, 0)));

		hunter.shoot();

		// Dead ones
		assertTrue(almostDead.isTerminated());

		// Shoot again at other targets
		hunter.shoot();

		assertEquals(capacity.subtract(target3.getHitDamage()), target3.getCapacityAmount());

		assertEquals(target1.getEnergyAmount(), e1.add(target1.getAbsorptionEnergy()));
		assertEquals(target2.getEnergyAmount(), e2.add(target2.getAbsorptionEnergy()));

		// Unharmed. Lucky bastards.
		assertEquals(unharmed1.getEnergyAmount(), u1);
		assertEquals(unharmed2.getEnergyAmount(), u2);
	}

	@Test
	public void shoot_SurpriseBox() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Robot hunter = new Robot(Orientation.RIGHT, 8000);
		Robot neighbour2 = new Robot(Orientation.RIGHT, 8000);
		Robot neighbour3 = new Robot(Orientation.RIGHT, 8000);
		Robot neighbour4 = new Robot(Orientation.RIGHT, 8000);
		hunter.placeOnBoard(board, new Vector(2, 5));

		neighbour2.placeOnBoard(board, hunter.getPosition().add(2, 0));
		neighbour3.placeOnBoard(board, hunter.getPosition().add(1, 1));
		neighbour4.placeOnBoard(board, hunter.getPosition().add(1, -1));

		EnergyAmount hunterCapacity = hunter.getCapacityAmount();
		EnergyAmount n2Capacity = neighbour2.getCapacityAmount();
		EnergyAmount n3Capacity = neighbour3.getCapacityAmount();
		EnergyAmount n4Capacity = neighbour4.getCapacityAmount();

		SurpriseBox surprise = new SurpriseBox(2);
		surprise.placeOnBoard(board, hunter.getNextPosition());

		hunter.shoot();
		assertEquals(hunterCapacity.subtract(hunter.getHitDamage()), hunter.getCapacityAmount());
		assertEquals(n2Capacity.subtract(neighbour2.getHitDamage()), neighbour2.getCapacityAmount());
		assertEquals(n3Capacity.subtract(neighbour3.getHitDamage()), neighbour3.getCapacityAmount());
		assertEquals(n4Capacity.subtract(neighbour4.getHitDamage()), neighbour4.getCapacityAmount());
		assertTrue(surprise.isTerminated());
	}

	@Test
	public void shoot_NotPlaced() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Robot robot = new Robot(Orientation.RIGHT, 2000);

		Piece target1 = new Battery(4);
		Vector target = new Vector(5, 5);
		target1.placeOnBoard(board, target);

		double energy = robot.getEnergyAmount(Unit.WATTSECOND);
		robot.shoot();

		// Nothing should change
		assertEquals(energy, robot.getEnergyAmount(Unit.WATTSECOND), 0.1);
		assertFalse(target1.isTerminated());
		assertEquals(target, target1.getPosition());
	}

	@Test
	public void canShoot() {
		assertTrue(robotEnergy1000.canShoot());
		assertFalse(robotEnergy500.canShoot());
	}

	@Test
	public void getShootTargets_NotPlaced() {
		Robot robot = new Robot(Orientation.RIGHT, 2000);
		assertTrue(robot.getShootTargets().isEmpty());
	}

}
