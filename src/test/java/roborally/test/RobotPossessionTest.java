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

	Robot normalBot, terminatedBot, carrierBot, robot1, robot2;
	Board board, otherBoard;
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

		robot1 = new Robot(Orientation.RIGHT, EnergyAmount.ZERO);
		robot2 = new Robot(Orientation.RIGHT, 2000);

		otherBoard = new Board(10, 10);
		robot1.placeOnBoard(otherBoard, Vector.ZERO);
		robot2.placeOnBoard(otherBoard, new Vector(1, 0));
	}

	/*
	 * Pick up items
	 */

	@Test
	public void pickUp_NormalCase() throws Exception {
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
	public void pickUp_DifferentBoard() throws Exception {
		Battery battery = new Battery(20);
		Board board2 = new Board(10, 10);
		battery.placeOnBoard(board2, normalBot.getPosition());

		normalBot.pickUp(battery);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_DifferentPosition() throws Exception {
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
	public void pickUp_TerminatedRobot() throws Exception {
		Battery battery = new Battery(20);
		battery.placeOnBoard(board, Vector.ZERO);

		assertFalse(terminatedBot.canHaveAsPossession(battery));
		terminatedBot.pickUp(battery);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void pickUp_TerminatedItem() throws Exception {
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
	public void drop_NormalCase() throws Exception {
		carrierBot.drop(batteryCarried);

		assertEquals(0, carrierBot.getNbPossessions());
		assertTrue(batteryCarried.isPlaced());
		assertEquals(carrierBot.getPosition(), batteryCarried.getPosition());
	}

	@Test(expected = IllegalArgumentException.class)
	public void drop_TerminatedItem() throws Exception {
		batteryCarried.terminate();

		carrierBot.drop(batteryCarried);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void drop_NotPossessed() throws Exception {
		Battery battery = new Battery(2);
		carrierBot.drop(battery);
		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void drop_NotEffective() throws Exception {
		carrierBot.drop(null);
		fail();
	}

	/*
	 * Use items
	 */

	@Test(expected = IllegalArgumentException.class)
	public void use_NotPossessed() throws Exception {
		// Robot possesses item, uses battery
		Battery battery = new Battery(2);
		battery.placeOnBoard(board, Vector.ZERO);

		carrierBot.use(battery);
		fail();
	}

	@Test
	public void use_Terminated() throws Exception {
		// Robot possesses item, uses battery
		batteryCarried.terminate();
		carrierBot.use(batteryCarried);

		assertTrue(carrierBot.hasProperPossessions());
		// Terminated possessions are removed
		assertFalse(carrierBot.hasAsPossession(batteryCarried));
	}

	@Test(expected = IllegalStateException.class)
	public void use_NotEffective() throws Exception {
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
	public void pickUp_MultipleItems() throws Exception {
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
	public void getPossessionAt() throws Exception {
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
	public void getPossessions() throws Exception {
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

		Set<RepairKit> repairKits = normalBot.getPossessions(RepairKit.class);
		assertEquals(0, repairKits.size());

		Set<SurpriseBox> surpriseBoxes = normalBot.getPossessions(SurpriseBox.class);
		assertEquals(0, surpriseBoxes.size());
	}

	/*
	 * Transfer items
	 */

	@Test
	public void transferItems_NormalCase() throws Exception {
		Robot robot1 = new Robot(Orientation.RIGHT, EnergyAmount.ZERO);
		Robot robot2 = new Robot(Orientation.RIGHT, EnergyAmount.ZERO);
		Board emptyBoard = new Board(10, 10);

		robot1.placeOnBoard(emptyBoard, Vector.ZERO);
		robot2.placeOnBoard(emptyBoard, new Vector(1, 0));

		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		Set<Item> possessions = robot1.getPossessions();
		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);

		robot1.transferItems(robot2);

		assertTrue(robot1.getPossessions().isEmpty());
		assertEquals(2, robot2.getNbPossessions());
		assertTrue(robot2.getPossessions().containsAll(possessions));
	}

	@Test(expected = IllegalArgumentException.class)
	public void transferItems_SelfReference() throws Exception {
		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);

		robot1.transferItems(robot1);
	}

	@Test(expected = IllegalStateException.class)
	public void transferItems_Terminated() throws Exception {
		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);

		robot1.terminate();

		robot1.transferItems(robot2);
	}

	@Test(expected = IllegalStateException.class)
	public void transferItems_NotPlaced() throws Exception {
		robot1.removeFromBoard();

		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);

		robot1.transferItems(robot2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transferItems_NullReceiver() throws Exception {
		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);
		robot1.transferItems(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transferItems_ReceiverNotPlaced() throws Exception {
		robot2 = new Robot(Orientation.RIGHT, 20);
		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);
		robot1.transferItems(robot2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transferItems_NotOnSameBoard() throws Exception {
		robot2 = new Robot(Orientation.RIGHT, 20);
		robot2.placeOnBoard(new Board(2, 2), Vector.ZERO);
		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);
		robot1.transferItems(robot2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void transferItems_NotNextToOther() throws Exception {
		robot2.move();
		Battery b1 = new Battery(50);
		SurpriseBox s1 = new SurpriseBox(60);

		robot1.addAsPossession(b1);
		robot1.addAsPossession(s1);
		robot1.transferItems(robot2);
	}

	@Test
	public void addAsPossession_NormalCase() {
		Robot robot = new Robot(Orientation.RIGHT, 500);
		Battery b = new Battery(2);
		robot.addAsPossession(b);

		assertEquals(1, robot.getNbPossessions());
		assertTrue(robot.hasAsPossession(b));
	}

	@Test(expected = IllegalArgumentException.class)
	public void addAsPossession_AlreadyPossessed() {
		Robot robot = new Robot(Orientation.RIGHT, 500);
		Battery b = new Battery(2);
		robot.addAsPossession(b);
		robot.addAsPossession(b);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void addAsPossession_isPlaced() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Robot robot = new Robot(Orientation.RIGHT, 500);
		Battery b = new Battery(2);
		b.placeOnBoard(board, Vector.ZERO);
		robot.addAsPossession(b);
		fail();
	}

	@Test
	public void removeAsPossession_NormalCase() {
		Robot robot = new Robot(Orientation.RIGHT, 500);
		Battery b = new Battery(2);
		robot.addAsPossession(b);
		robot.removeAsPossession(b);

		assertEquals(0, robot.getNbPossessions());
		assertFalse(robot.hasAsPossession(b));
	}

	@Test(expected = IllegalArgumentException.class)
	public void removeAsPossession_NotInPossession() {
		Robot robot = new Robot(Orientation.RIGHT, 500);
		Battery b = new Battery(2);

		robot.removeAsPossession(b);
		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void removeAsPossession_CannotHave() {
		Robot robot = new Robot(Orientation.RIGHT, 500);
		Battery b = new Battery(2);
		robot.addAsPossession(b);

		robot.terminate();

		robot.removeAsPossession(b);
		fail();
	}

}