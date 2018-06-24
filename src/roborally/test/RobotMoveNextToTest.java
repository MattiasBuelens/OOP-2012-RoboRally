package roborally.test;

import static org.junit.Assert.*;

import org.junit.*;

import roborally.InvalidPositionException;
import roborally.Orientation;
import roborally.Robot;

public class RobotMoveNextToTest {

	@Test
	public void moveNextTo_Stationary() throws InvalidPositionException {
		// None of the robots can move
		Robot robot = new Robot(1, 1, Orientation.DOWN, 400);
		Robot otherRobot = new Robot(0, 0, Orientation.DOWN, 400);
		robot.moveNextTo(otherRobot);

		assertEquals(1, robot.getX());
		assertEquals(1, robot.getY());
		assertEquals(400, robot.getEnergy(), 0.1);
		assertEquals(0, otherRobot.getX());
		assertEquals(0, otherRobot.getY());
		assertEquals(400, otherRobot.getEnergy(), 0.1);
	}

	@Test
	public void moveNextTo_OtherStepsOnce() throws InvalidPositionException {
		// Robot needs at least one turn
		Robot robot = new Robot(0, 0, Orientation.DOWN, 9999);
		// Other robot can move without turns
		Robot otherRobot = new Robot(2, 0, Orientation.LEFT, 500);
		robot.moveNextTo(otherRobot);
		// Robot remain stationary
		assertEquals(0, robot.getX());
		assertEquals(0, robot.getY());
		assertEquals(9999, robot.getEnergy(), 0.1);
		// Other robot moves to the left
		assertEquals(1, otherRobot.getX());
		assertEquals(0, otherRobot.getY());
		assertEquals(0, otherRobot.getEnergy(), 0.1);
	}

	@Test
	public void resolveConflictingPositions_FirstCanMove() throws InvalidPositionException {
		// Robot can turn and move
		Robot robot = new Robot(0, 0, Orientation.UP, 600);
		// Other robot can only turn
		Robot otherRobot = new Robot(0, 0, Orientation.LEFT, 400);
		robot.moveNextTo(otherRobot);

		// Robot steps to the right
		assertEquals(1, robot.getX());
		assertEquals(0, robot.getY());
		assertEquals(Orientation.RIGHT, robot.getOrientation());
		assertEquals(0, robot.getEnergy(), 0.1);
		// Other robot remains stationary
		assertEquals(0, otherRobot.getX());
		assertEquals(0, otherRobot.getY());
		assertEquals(Orientation.LEFT, otherRobot.getOrientation());
		assertEquals(400, otherRobot.getEnergy(), 0.1);
	}

	@Test
	public void resolveConflictingPositions_SecondMoreEfficient() throws InvalidPositionException {
		// Robot needs at least one turn
		Robot robot = new Robot(0, 0, Orientation.UP, 9999);
		// Other robot can move without turns
		Robot otherRobot = new Robot(0, 0, Orientation.RIGHT, 500);
		robot.moveNextTo(otherRobot);

		// Robot remains stationary
		assertEquals(0, robot.getX());
		assertEquals(0, robot.getY());
		assertEquals(Orientation.UP, robot.getOrientation());
		assertEquals(9999, robot.getEnergy(), 0.1);
		// Other robot steps to the right
		assertEquals(1, otherRobot.getX());
		assertEquals(0, otherRobot.getY());
		assertEquals(Orientation.RIGHT, otherRobot.getOrientation());
		assertEquals(0, otherRobot.getEnergy(), 0.1);
	}

	@Test
	public void moveNextTo_SameHorizontalPosition() throws InvalidPositionException {
		/** Same horizontal, different vertical position**/
		Robot robot = new Robot(0, 0, Orientation.UP, 9999);
		Robot otherRobot = new Robot(0, 5, Orientation.UP, 0);
		robot.moveNextTo(otherRobot);

		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());
	}

	@Test
	public void moveNextTo_SameVerticalPosition() throws InvalidPositionException {
		/** Same vertical, different horizontal position **/
		Robot robot = new Robot(0, 0, Orientation.LEFT, 9999);
		Robot otherRobot = new Robot(5, 0, Orientation.UP, 9999);
		robot.moveNextTo(otherRobot);

		// Expected position
		assertEquals(1, otherRobot.getX());

		// Expected orientation
		assertEquals(Orientation.LEFT, otherRobot.getOrientation());
	}

	@Test
	public void moveNextTo_TargetOrientations() throws InvalidPositionException {
		/** Target is LEFT and DOWN **/
		// Initial orientation: LEFT
		Robot robot = new Robot(5, 0, Orientation.LEFT, 9999);
		Robot otherRobot = new Robot(0, 5, Orientation.UP, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());

		// Initial orientation: RIGHT
		robot = new Robot(5, 0, Orientation.DOWN, 9999);
		otherRobot = new Robot(0, 5, Orientation.UP, 0);
		robot.moveNextTo(otherRobot);

		// Initial orientation: RIGHT
		robot = new Robot(5, 0, Orientation.DOWN, 9999);
		otherRobot = new Robot(0, 5, Orientation.UP, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.LEFT, robot.getOrientation());

		/** Target is LEFT and UP **/
		// Initial orientation: LEFT
		robot = new Robot(5, 5, Orientation.LEFT, 9999);
		otherRobot = new Robot(0, 0, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.UP, robot.getOrientation());

		// Initial orientation: RIGHT
		robot = new Robot(5, 5, Orientation.UP, 9999);
		otherRobot = new Robot(0, 0, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.LEFT, robot.getOrientation());

		/** Target is RIGHT and DOWN **/
		// Initial orientation: LEFT
		robot = new Robot(0, 0, Orientation.DOWN, 9999);
		otherRobot = new Robot(5, 5, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.RIGHT, robot.getOrientation());

		// Initial orientation: RIGHT
		robot = new Robot(0, 0, Orientation.RIGHT, 9999);
		otherRobot = new Robot(5, 5, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());

		// Initial orientation: UP
		robot = new Robot(0, 0, Orientation.UP, 9999);
		otherRobot = new Robot(5, 5, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.DOWN, robot.getOrientation());

		/** Target is RIGHT and UP **/
		// Initial orientation: LEFT
		robot = new Robot(0, 5, Orientation.LEFT, 9999);
		otherRobot = new Robot(5, 0, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.RIGHT, robot.getOrientation());

		// Initial orientation: DOWN
		robot = new Robot(0, 5, Orientation.DOWN, 9999);
		otherRobot = new Robot(5, 0, Orientation.RIGHT, 0);
		robot.moveNextTo(otherRobot);
		// Expected orientation
		assertEquals(Orientation.UP, robot.getOrientation());

	}
}
