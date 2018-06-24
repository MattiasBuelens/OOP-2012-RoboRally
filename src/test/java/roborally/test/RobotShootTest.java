package roborally.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import roborally.*;

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
		Robot hunter = new Robot(Orientation.RIGHT, 2000);
		hunter.placeOnBoard(board, new Vector(2, 5));

		Piece target1 = new Battery(4);
		Piece target2 = new Battery(5);
		Piece target3 = new Robot(Orientation.LEFT, 2000);

		Piece unharmed1 = new Battery(2);
		Piece unharmed2 = new Battery(2);
		Piece unharmed3 = new Battery(2);
		Piece unharmed4 = new Battery(2);

		Vector target = new Vector(5, 5);
		target1.placeOnBoard(board, target);
		target2.placeOnBoard(board, target);
		target3.placeOnBoard(board, target);

		unharmed1.placeOnBoard(board, target.add(new Vector(1, 0)));
		unharmed2.placeOnBoard(board, target.add(new Vector(-5, 0)));
		unharmed3.placeOnBoard(board, target.add(new Vector(0, 1)));
		unharmed4.placeOnBoard(board, target.add(new Vector(0, -1)));

		hunter.shoot();

		assertTrue(target1.isTerminated());
		assertTrue(target2.isTerminated());
		assertTrue(target3.isTerminated());

		assertFalse(unharmed1.isTerminated());
		assertFalse(unharmed2.isTerminated());
		assertFalse(unharmed3.isTerminated());
		assertFalse(unharmed4.isTerminated());
	}

	@Test
	public void shoot_NotPlaced() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Robot robot = new Robot(Orientation.RIGHT, 2000);

		Piece target1 = new Battery(4);
		Vector target = new Vector(5, 5);
		target1.placeOnBoard(board, target);

		double energy = robot.getEnergy();
		robot.shoot();

		// Nothing should change
		assertEquals(energy, robot.getEnergy(), 0.1);
		assertFalse(target1.isTerminated());
		assertEquals(target, target1.getPosition());
	}

	@Test
	public void canTurn() {
		assertTrue(robotEnergy1000.canShoot());
		assertFalse(robotEnergy500.canShoot());
	}

}
