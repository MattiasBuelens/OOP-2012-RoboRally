package roborally.test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import roborally.*;
import roborally.EnergyAmount.Unit;

public class RobotMinimalCostTest {

	Robot robot;
	Board board;

	// @formatter:off
	private static int[][] walls = new int[][] {
		new int[] { 0, 1, 0, 0, 0, 1, 0 },
		new int[] { 0, 0, 0, 1, 0, 0, 1 },
		new int[] { 0, 1, 0, 0, 0, 1, 0 },
		new int[] { 0, 0, 0, 1, 0, 0, 0 },
		new int[] { 0, 1, 0, 0, 0, 1, 1 }
	};
	// @formatter:on

	@Before
	public void setUp() throws Exception {
		robot = new Robot(Orientation.RIGHT, 20000);
		board = new Board(7, 5);
		robot.placeOnBoard(board, new Vector(0, 0));

		for (int y = 0; y < walls.length; ++y) {
			for (int x = 0; x < walls[y].length; ++x) {
				if (walls[y][x] == 1) {
					new Wall().placeOnBoard(board, new Vector(x, y));
				}
			}
		}
	}

	@Test
	public void getMinimalCostToReach_NormalCase() throws InvalidPositionException, UnreachablePositionException {
		double expectedCost = getExpectedCost(10, 7);
		double cost = robot.getMinimalCostToReach(new Vector(6, 2)).getAmount(Unit.WATTSECOND);
		assertTrue(robot.isReachable(new Vector(6, 2)));
		assertTrue(robot.canReach(new Vector(6, 2)));
		assertEquals(expectedCost, cost, 0.1);
	}

	@Test(expected = InvalidPositionException.class)
	public void getMinimalCostToReach_InvalidPosition() throws InvalidPositionException, UnreachablePositionException {
		assertFalse(robot.isReachable(new Vector(-1, -1)));
		assertFalse(robot.canReach(new Vector(-1, -1)));
		robot.getMinimalCostToReach(new Vector(-1, -1));
		fail();
	}

	@Test(expected = UnreachablePositionException.class)
	public void getMinimalCostToReach_Unreachable() throws InvalidPositionException, UnreachablePositionException {
		assertFalse(robot.isReachable(new Vector(6, 0)));
		assertFalse(robot.canReach(new Vector(6, 0)));
		robot.getMinimalCostToReach(new Vector(6, 0));
		fail();
	}

	@Test(expected = IllegalStateException.class)
	public void getMinimalCostToReach_NoPlaced() throws InvalidPositionException, UnreachablePositionException {
		robot.removeFromBoard();
		robot.getMinimalCostToReach(Vector.ZERO);
		fail();
	}

	@Test
	public void getMinimalCostToReach_CurrentPosition() throws InvalidPositionException, UnreachablePositionException {
		double cost = robot.getMinimalCostToReach(robot.getPosition()).getAmount(Unit.WATTSECOND);
		assertEquals(0, cost, 0.1);
	}

	@Test
	public void getMinimalCostToReach_WithOtherRobot() throws InvalidPositionException, UnreachablePositionException {
		new Robot(Orientation.UP, 500).placeOnBoard(board, new Vector(3, 2));

		double expectedCost = getExpectedCost(12, 7);
		double cost = robot.getMinimalCostToReach(new Vector(6, 2)).getAmount(Unit.WATTSECOND);
		assertEquals(expectedCost, cost, 0.1);
	}

	private double getExpectedCost(int expectedSteps, int expectedTurns) {
		return robot.getStepCost().getAmount(Unit.WATTSECOND) * expectedSteps
				+ robot.getTurnCost().getAmount(Unit.WATTSECOND) * expectedTurns;
	}

}
