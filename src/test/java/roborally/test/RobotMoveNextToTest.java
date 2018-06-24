package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.InvalidPositionException;
import roborally.InvalidSizeException;
import roborally.Orientation;
import roborally.Robot;
import roborally.Board;
import roborally.Vector;
import roborally.Wall;

public class RobotMoveNextToTest {

	Board board;

	@Before
	public void setUp() throws InvalidSizeException {
		board = new Board(10, 10);
	}

	@Test
	public void moveNextTo_Stationary() throws InvalidPositionException, InvalidSizeException {
		// None of the robots can move
		Robot robot = new Robot(Orientation.DOWN, 400);

		Robot otherRobot = new Robot(Orientation.DOWN, 400);

		Board board = new Board(10, 10);
		robot.placeOnBoard(board, new Vector(1, 1));
		otherRobot.placeOnBoard(board, new Vector(0, 0));

		robot.moveNextTo(otherRobot);

		assertEquals(new Vector(1, 1), robot.getPosition());
		assertEquals(400, robot.getEnergy(), 0.1);
		assertEquals(new Vector(0, 0), otherRobot.getPosition());
		assertEquals(400, otherRobot.getEnergy(), 0.1);
	}

	@Test
	public void moveNextTo_OtherStepsOnce() throws InvalidPositionException {
		// Robot needs at least one turn
		Robot robot = new Robot(Orientation.DOWN, 9999);
		// Other robot can move without turns
		Robot otherRobot = new Robot(Orientation.LEFT, 500);
		robot.placeOnBoard(board, new Vector(0, 0));
		otherRobot.placeOnBoard(board, new Vector(2, 0));
		robot.moveNextTo(otherRobot);
		// Robot remain stationary
		assertEquals(new Vector(0, 0), robot.getPosition());
		assertEquals(9999, robot.getEnergy(), 0.1);
		// Other robot moves to the left
		assertEquals(new Vector(1, 0), otherRobot.getPosition());
		assertEquals(0, otherRobot.getEnergy(), 0.1);
	}

	@Test
	public void moveNextTo_SameHorizontalPosition() throws InvalidPositionException {
		/** Same horizontal, different vertical position**/
		Robot robot = new Robot(Orientation.UP, 9999);
		robot.placeOnBoard(board, new Vector(0, 0));
		Robot otherRobot = new Robot(Orientation.UP, 0);
		otherRobot.placeOnBoard(board, new Vector(0, 5));
		robot.moveNextTo(otherRobot);

		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());
	}

	@Test
	public void moveNextTo_SameVerticalPosition() throws InvalidPositionException {
		/** Same vertical, different horizontal position **/
		Robot robot = new Robot(Orientation.LEFT, 9999);
		robot.placeOnBoard(board, new Vector(0, 0));
		Robot otherRobot = new Robot(Orientation.UP, 9999);
		otherRobot.placeOnBoard(board, new Vector(5, 0));

		robot.moveNextTo(otherRobot);

		// Expected position
		assertEquals(1, otherRobot.getPosition().getX());

		// Expected orientation
		assertEquals(Orientation.LEFT, otherRobot.getOrientation());
	}

	@Test
	public void moveNextTo_TargetOrientations() throws InvalidPositionException, InvalidSizeException {
		/** Target is LEFT and DOWN **/
		// Initial orientation: LEFT
		Robot robot = new Robot(Orientation.LEFT, 9999);
		robot.placeOnBoard(board, new Vector(5, 0));
		Robot otherRobot = new Robot(Orientation.UP, 0);
		otherRobot.placeOnBoard(board, new Vector(0, 5));

		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());

		setUp();
		// Initial orientation: RIGHT
		robot = new Robot(Orientation.DOWN, 9999);
		robot.placeOnBoard(board, new Vector(5, 0));
		otherRobot = new Robot(Orientation.UP, 0);
		otherRobot.placeOnBoard(board, new Vector(0, 5));
		robot.moveNextTo(otherRobot);

		setUp();
		// Initial orientation: RIGHT
		robot = new Robot(Orientation.DOWN, 9999);
		robot.placeOnBoard(board, new Vector(5, 0));
		otherRobot = new Robot(Orientation.UP, 0);
		otherRobot.placeOnBoard(board, new Vector(0, 5));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.LEFT, robot.getOrientation());

		setUp();
		/** Target is LEFT and UP **/
		// Initial orientation: LEFT
		robot = new Robot(Orientation.LEFT, 9999);
		robot.placeOnBoard(board, new Vector(5, 5));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(0, 0));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.UP, robot.getOrientation());

		setUp();
		// Initial orientation: RIGHT
		robot = new Robot(Orientation.UP, 9999);
		robot.placeOnBoard(board, new Vector(5, 5));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(0, 0));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.LEFT, robot.getOrientation());

		setUp();
		/** Target is RIGHT and DOWN **/
		// Initial orientation: LEFT
		robot = new Robot(Orientation.DOWN, 9999);
		robot.placeOnBoard(board, new Vector(0, 0));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(5, 5));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.RIGHT, robot.getOrientation());

		setUp();
		// Initial orientation: RIGHT
		robot = new Robot(Orientation.RIGHT, 9999);
		robot.placeOnBoard(board, new Vector(0, 0));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(5, 5));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());

		setUp();
		// Initial orientation: UP
		robot = new Robot(Orientation.UP, 9999);
		robot.placeOnBoard(board, new Vector(0, 0));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(5, 5));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());

		setUp();
		/** Target is RIGHT and UP **/
		// Initial orientation: LEFT
		robot = new Robot(Orientation.LEFT, 9999);
		robot.placeOnBoard(board, new Vector(0, 5));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(5, 0));
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.RIGHT, robot.getOrientation());

		setUp();
		// Initial orientation: DOWN
		robot = new Robot(Orientation.DOWN, 9999);
		robot.placeOnBoard(board, new Vector(0, 5));
		otherRobot = new Robot(Orientation.RIGHT, 0);
		otherRobot.placeOnBoard(board, new Vector(5, 0));

		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.UP, robot.getOrientation());

	}

	@Test
	public void moveNextTo_RouteBlocked() throws InvalidSizeException, IllegalArgumentException, IllegalStateException,
			InvalidPositionException {

		Board board = new Board(7, 5);
		placeWalls(board);

		Robot robot1 = new Robot(Orientation.RIGHT, 5000);
		Robot robot2 = new Robot(Orientation.LEFT, 5000);
		robot1.placeOnBoard(board, Vector.ZERO);
		robot2.placeOnBoard(board, new Vector(6, 0));

		robot1.moveNextTo(robot2);

		assertEquals(new Vector(2, 0), robot1.getPosition());
		assertEquals(new Vector(4, 0), robot2.getPosition());

		assertFalse(robot1.canReach(new Vector(3, 0)));

		// @formatter:on

	}

	@Test(expected = IllegalStateException.class)
	public void moveNextTo_TerminatedRobot() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Robot robot1 = new Robot(Orientation.RIGHT, 5000);
		Robot robot2 = new Robot(Orientation.LEFT, 5000);

		robot1.placeOnBoard(board, Vector.ZERO);
		robot2.placeOnBoard(board, new Vector(6, 0));
		robot1.terminate();
		robot1.moveNextTo(robot2);
	}

	@Test(expected = IllegalStateException.class)
	public void moveNextTo_NotPlaced() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Robot robot1 = new Robot(Orientation.RIGHT, 5000);
		Robot robot2 = new Robot(Orientation.LEFT, 5000);

		robot2.placeOnBoard(board, new Vector(6, 0));

		robot1.moveNextTo(robot2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void moveNextTo_DifferentBoards() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException, InvalidSizeException {
		Robot robot1 = new Robot(Orientation.RIGHT, 5000);
		Robot robot2 = new Robot(Orientation.LEFT, 5000);

		robot1.placeOnBoard(board, new Vector(6, 0));
		robot2.placeOnBoard(new Board(7, 7), new Vector(6, 0));

		robot1.moveNextTo(robot2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void moveNextTo_SameRobot() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		Robot robot1 = new Robot(Orientation.RIGHT, 5000);
		robot1.placeOnBoard(board, new Vector(6, 0));

		robot1.moveNextTo(robot1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void moveNextTo_NotEffective() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		Robot robot1 = new Robot(Orientation.RIGHT, 5000);
		robot1.placeOnBoard(board, new Vector(6, 0));

		robot1.moveNextTo(null);
	}

	private void placeWalls(Board board) throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		// @formatter:off
		int[][] walls = new int[][] {
			new int[] { 0, 0, 0, 1, 0, 0, 0 },
			new int[] { 0, 0, 0, 1, 0, 0, 1 },
			new int[] { 0, 0, 0, 1, 0, 0, 0 },
			new int[] { 0, 0, 0, 1, 0, 0, 0 },
			new int[] { 0, 0, 0, 1, 0, 0, 0 }
		};
		// @formatter:on

		for (int y = 0; y < walls.length; ++y) {
			for (int x = 0; x < walls[y].length; ++x) {
				if (walls[y][x] == 1) {
					new Wall().placeOnBoard(board, new Vector(x, y));
				}
			}
		}
	}

}
