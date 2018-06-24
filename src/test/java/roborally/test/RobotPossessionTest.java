package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import roborally.*;

public class RobotPossessionTest {

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
	public void pickUp_NormalCase() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Battery battery = new Battery(20);
		battery.placeOnBoard(board, normalBot.getPosition());

		normalBot.pickUp(battery);

		assertTrue(normalBot.hasAsPossession(battery));
		assertFalse(battery.isPlaced());
	}

	@Test(expected = IllegalStateException.class)
	public void pickUp_NotEffective() throws IllegalStateException {
		normalBot.pickUp(null);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_ItemNotOnBoard() throws IllegalArgumentException {
		Battery battery = new Battery(20);

		normalBot.pickUp(battery);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_DifferentBoard() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException, InvalidSizeException {
		Battery battery = new Battery(20);
		Board board2 = new Board(10, 10);
		battery.placeOnBoard(board2, normalBot.getPosition());

		normalBot.pickUp(battery);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_DifferentPosition() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Battery battery = new Battery(20);
		battery.placeOnBoard(board, new Vector(1, 1));

		normalBot.pickUp(battery);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_AlreadyOwned() throws IllegalArgumentException {
		carrierBot.pickUp(batteryCarried);
		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void pickUp_TerminatedRobot() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Battery battery = new Battery(20);
		battery.placeOnBoard(board, Vector.ZERO);

		assertFalse(terminatedBot.canHaveAsPossession(battery));
		terminatedBot.pickUp(battery);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_TerminatedItem() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Battery battery = new Battery(20);
		battery.placeOnBoard(board, normalBot.getPosition());
		battery.terminate();

		normalBot.pickUp(battery);
		fail();
	}

	/*
	 * Dropping possessions
	 */

	@Test
	public void drop_NormalCase() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		carrierBot.drop(batteryCarried);

		assertEquals(0, carrierBot.getNbPossessions());
		assertTrue(batteryCarried.isPlaced());
		assertEquals(carrierBot.getPosition(), batteryCarried.getPosition());
	}

	@Test(expected = IllegalArgumentException.class)
	public void drop_TerminatedItem() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		batteryCarried.terminate();

		carrierBot.drop(batteryCarried);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void drop_NotPossessed() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Battery battery = new Battery(2);
		carrierBot.drop(battery);
		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void drop_NotEffective() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		carrierBot.drop(null);
		fail();
	}

	/*
	 * Use items
	 */

	@Test
	public void use_NormalCase() {
		// Robot possesses item, uses battery
		double oldEnergy = carrierBot.getEnergy();
		double batteryEnergy = batteryCarried.getEnergy();
		carrierBot.use(batteryCarried);

		assertEquals(oldEnergy - batteryEnergy, carrierBot.getEnergy(), 0.1);
		assertEquals(0, batteryCarried.getEnergy(), 0.1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void use_NotPossessed() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		// Robot possesses item, uses battery
		Battery battery = new Battery(2);
		battery.placeOnBoard(board, Vector.ZERO);

		carrierBot.use(battery);
		fail();
	}

	@Test
	public void use_Terminated() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		// Robot possesses item, uses battery
		batteryCarried.terminate();
		carrierBot.use(batteryCarried);

		assertTrue(carrierBot.hasProperPossessions());
		// Terminated possessions are removed
		assertFalse(carrierBot.hasAsPossession(batteryCarried));
	}

	@Test(expected = IllegalStateException.class)
	public void use_NotEffective() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		carrierBot.use(null);
		fail();
	}

	@Test
	public void terminate_WithPossessions() {
		carrierBot.terminate();
		assertTrue(batteryCarried.isTerminated());
	}

	/*
	 * Pick up multiple possessions
	 */

	@Test
	public void pickUp_MultipleItems() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Battery battery1 = new Battery(3);
		Battery battery2 = new Battery(4);
		Battery battery3 = new Battery(5);
		Battery battery4 = new Battery(6);

		battery1.placeOnBoard(board, new Vector(3, 7));
		battery2.placeOnBoard(board, new Vector(3, 7));
		battery3.placeOnBoard(board, new Vector(3, 7));
		battery4.placeOnBoard(board, new Vector(3, 7));

		assertTrue(normalBot.canHaveAsPossession(battery1));
		assertTrue(normalBot.canHaveAsPossession(battery2));
		assertTrue(normalBot.canHaveAsPossession(battery3));
		assertTrue(normalBot.canHaveAsPossession(battery4));

		normalBot.pickUp(battery1);
		normalBot.pickUp(battery2);
		normalBot.pickUp(battery3);
		normalBot.pickUp(battery4);

		assertTrue(normalBot.hasAsPossession(battery1));
		assertTrue(normalBot.hasAsPossession(battery2));
		assertTrue(normalBot.hasAsPossession(battery3));
		assertTrue(normalBot.hasAsPossession(battery4));

		assertFalse(battery1.isPlaced());
		assertFalse(battery2.isPlaced());
		assertFalse(battery3.isPlaced());
		assertFalse(battery4.isPlaced());
		assertTrue(normalBot.hasProperPossessions());
	}

	/*
	 * Retrieve possessions
	 */

	@Test
	public void getPossessionAt() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Battery battery1 = new Battery(4);
		Battery battery2 = new Battery(3);
		Battery battery3 = new Battery(2);
		Battery battery4 = new Battery(1);

		battery1.placeOnBoard(board, new Vector(3, 7));
		battery2.placeOnBoard(board, new Vector(3, 7));
		battery3.placeOnBoard(board, new Vector(3, 7));
		battery4.placeOnBoard(board, new Vector(3, 7));

		normalBot.pickUp(battery1);
		normalBot.pickUp(battery2);
		normalBot.pickUp(battery3);
		normalBot.pickUp(battery4);

		assertEquals(4, normalBot.getNbPossessions());
		assertEquals(battery1, normalBot.getPossessionAt(1));

		assertTrue(normalBot.hasProperPossessions());
	}

	@Test
	public void getPossessions() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Battery battery1 = new Battery(4);
		Battery battery2 = new Battery(3);

		battery1.placeOnBoard(board, new Vector(3, 7));
		battery2.placeOnBoard(board, new Vector(3, 7));

		normalBot.pickUp(battery1);

		Set<Item> possessions = normalBot.getPossessions();
		assertEquals(1, possessions.size());
		assertEquals(1, normalBot.getNbPossessions());
		assertTrue(possessions.contains(battery1));

		normalBot.pickUp(battery2);

		Set<Battery> batteries = normalBot.getPossessions(Battery.class);
		assertEquals(2, batteries.size());
		assertEquals(2, normalBot.getNbPossessions());
		assertTrue(batteries.contains(battery1));
		assertTrue(batteries.contains(battery2));

		Set<DummyItem> dummies = normalBot.getPossessions(DummyItem.class);
		assertEquals(0, dummies.size());
	}

	private class DummyItem extends Item {

		protected DummyItem(int weight) {
			super(weight);
		}

		@Override
		public void use(Robot robot) {
			throw new UnsupportedOperationException();
		}

	}

}