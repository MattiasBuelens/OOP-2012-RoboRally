package roborally.view;

import java.util.Set;

import roborally.*;

public class Facade implements IFacade<Board, Robot, Wall, Battery> {

	@Override
	public Board createBoard(long width, long height) {
		try {
			return new Board(width, height);
		} catch (InvalidSizeException e) {
			System.err.println(e.getMessage());
			return null;
		}
	}

	@Override
	public void merge(Board board1, Board board2) {
		board1.merge(board2);
	}

	@Override
	public Battery createBattery(double initialEnergy, int weight) {
		return new Battery(weight, initialEnergy);
	}

	@Override
	public void putBattery(Board board, long x, long y, Battery battery) {
		putPiece(board, x, y, battery);
	}

	@Override
	public long getBatteryX(Battery battery) throws IllegalStateException {
		return getPieceX(battery);
	}

	@Override
	public long getBatteryY(Battery battery) throws IllegalStateException {
		return getPieceY(battery);
	}

	@Override
	public Robot createRobot(int orientation, double initialEnergy) {
		return new Robot(Orientation.getByValue(orientation), initialEnergy);
	}

	@Override
	public void putRobot(Board board, long x, long y, Robot robot) {
		putPiece(board, x, y, robot);
	}

	@Override
	public long getRobotX(Robot robot) throws IllegalStateException {
		return getPieceX(robot);
	}

	@Override
	public long getRobotY(Robot robot) throws IllegalStateException {
		return getPieceY(robot);
	}

	@Override
	public int getOrientation(Robot robot) {
		return robot.getOrientation().getValue();
	}

	@Override
	public double getEnergy(Robot robot) {
		return robot.getEnergy();
	}

	@Override
	public void move(Robot robot) {
		if (!robot.canMove()) {
			System.err.println("Robot has insufficient energy to move.");
			return;
		}

		try {
			robot.move();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void turn(Robot robot) {
		if (!robot.canTurn()) {
			System.err.println("Robot has insufficient energy to turn.");
			return;
		}

		robot.turnClockwise();
	}

	@Override
	public Set<Battery> getPossessions(Robot robot) {
		return robot.getPossessions(Battery.class);
	}

	@Override
	public void pickUp(Robot robot, Battery battery) {
		try {
			robot.pickUp(battery);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void use(Robot robot, Battery battery) {
		try {
			robot.use(battery);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void drop(Robot robot, Battery battery) {
		try {
			robot.drop(battery);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * @note Watch out guys, we're dealing with a bad ass over here!
	 */
	@Override
	public int isMinimalCostToReach17Plus() {
		return 1;
	}

	@Override
	public double getMinimalCostToReach(Robot robot, long x, long y) {
		try {
			double cost = robot.getMinimalCostToReach(new Vector(x, y));
			return (cost <= robot.getEnergy()) ? cost : -2;
		} catch (InvalidPositionException e) {
			return -1;
		} catch (UnreachablePositionException e) {
			return -1;
		}
	}

	/**
	 * @note Watch out guys, we're dealing with a bad ass over here!
	 */
	@Override
	public int isMoveNextTo18Plus() {
		return 1;
	}

	@Override
	public void moveNextTo(Robot robot, Robot other) {
		robot.moveNextTo(other);
	}

	@Override
	public void shoot(Robot robot) throws UnsupportedOperationException {
		if (!robot.canShoot()) {
			System.err.println("Robot has insufficient energy to shoot.");
			return;
		}

		robot.shoot();
	}

	@Override
	public Wall createWall() throws UnsupportedOperationException {
		return new Wall();
	}

	@Override
	public void putWall(Board board, long x, long y, Wall wall) throws UnsupportedOperationException {
		putPiece(board, x, y, wall);
	}

	@Override
	public long getWallX(Wall wall) throws IllegalStateException, UnsupportedOperationException {
		return getPieceX(wall);
	}

	@Override
	public long getWallY(Wall wall) throws IllegalStateException, UnsupportedOperationException {
		return getPieceY(wall);
	}

	@Override
	public Set<Robot> getRobots(Board board) {
		return board.getPieces(Robot.class);
	}

	@Override
	public Set<Battery> getBatteries(Board board) {
		return board.getPieces(Battery.class);
	}

	@Override
	public Set<Wall> getWalls(Board board) throws UnsupportedOperationException {
		return board.getPieces(Wall.class);
	}

	@Override
	public void terminateRobot(Robot robot) {
		robot.terminate();
	}

	@Override
	public void terminateBattery(Battery battery) {
		battery.terminate();
	}

	@Override
	public void terminateWall(Wall wall) {
		wall.terminate();
	}

	private void putPiece(Board board, long x, long y, Piece piece) {
		try {
			piece.placeOnBoard(board, new Vector(x, y));
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private long getPieceX(Piece piece) throws IllegalStateException {
		if (!piece.isPlaced())
			throw new IllegalStateException();
		return piece.getPosition().getX();
	}

	private long getPieceY(Piece piece) throws IllegalStateException {
		if (!piece.isPlaced())
			throw new IllegalStateException();
		return piece.getPosition().getY();
	}

}