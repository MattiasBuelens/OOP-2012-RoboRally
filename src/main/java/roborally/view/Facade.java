package roborally.view;

import java.io.Writer;
import java.util.Set;

import roborally.*;
import roborally.EnergyAmount.Unit;
import roborally.program.Program;

public class Facade implements IFacade<Board, Robot, Wall, Battery, RepairKit, SurpriseBox> {

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
	public RepairKit createRepairKit(double repairAmount, int weight) {
		return new RepairKit(weight, repairAmount);
	}

	@Override
	public void putRepairKit(Board board, long x, long y, RepairKit repairKit) {
		putPiece(board, x, y, repairKit);
	}

	@Override
	public long getRepairKitX(RepairKit repairKit) throws IllegalStateException {
		return getPieceX(repairKit);
	}

	@Override
	public long getRepairKitY(RepairKit repairKit) throws IllegalStateException {
		return getPieceY(repairKit);
	}

	@Override
	public SurpriseBox createSurpriseBox(int weight) {
		return new SurpriseBox(weight);
	}

	@Override
	public void putSurpriseBox(Board board, long x, long y, SurpriseBox surpriseBox) {
		putPiece(board, x, y, surpriseBox);
	}

	@Override
	public long getSurpriseBoxX(SurpriseBox surpriseBox) throws IllegalStateException {
		return getPieceX(surpriseBox);
	}

	@Override
	public long getSurpriseBoxY(SurpriseBox surpriseBox) throws IllegalStateException {
		return getPieceY(surpriseBox);
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
		return robot.getEnergyAmount(Unit.WATTSECOND);
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

//	@Override
//	public Set<Item> getPossessions(Robot robot) {
//		return robot.getPossessions();
//	}

	@Override
	public void pickUpBattery(Robot robot, Battery battery) {
		pickUpItem(robot, battery);
	}

	@Override
	public void useBattery(Robot robot, Battery battery) {
		useItem(robot, battery);
	}

	@Override
	public void dropBattery(Robot robot, Battery battery) {
		dropItem(robot, battery);
	}

	@Override
	public void pickUpRepairKit(Robot robot, RepairKit repairKit) {
		pickUpItem(robot, repairKit);
	}

	@Override
	public void useRepairKit(Robot robot, RepairKit repairKit) {
		useItem(robot, repairKit);
	}

	@Override
	public void dropRepairKit(Robot robot, RepairKit repairKit) {
		dropItem(robot, repairKit);
	}

	@Override
	public void pickUpSurpriseBox(Robot robot, SurpriseBox surpriseBox) {
		pickUpItem(robot, surpriseBox);
	}

	@Override
	public void useSurpriseBox(Robot robot, SurpriseBox surpriseBox) {
		useItem(robot, surpriseBox);
	}

	@Override
	public void dropSurpriseBox(Robot robot, SurpriseBox surpriseBox) {
		dropItem(robot, surpriseBox);
	}

	@Override
	public void transferItems(Robot from, Robot to) {
		try {
			from.transferItems(to);
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
			double cost = robot.getMinimalCostToReach(new Vector(x, y)).getAmount(Unit.WATTSECOND);
			return (cost <= robot.getEnergyAmount(Unit.WATTSECOND)) ? cost : -2;
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
	public Set<Wall> getWalls(Board board) throws UnsupportedOperationException {
		return board.getPieces(Wall.class);
	}

	@Override
	public Set<RepairKit> getRepairKits(Board board) {
		return board.getPieces(RepairKit.class);
	}

	@Override
	public Set<SurpriseBox> getSurpriseBoxes(Board board) {
		return board.getPieces(SurpriseBox.class);
	}

	@Override
	public Set<Battery> getBatteries(Board board) {
		return board.getPieces(Battery.class);
	}

	@Override
	public int loadProgramFromFile(Robot robot, String path) {
		try {
			robot.setProgram(Program.load(path));
			return 0;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}

	@Override
	public int saveProgramToFile(Robot robot, String path) {
		if (!robot.hasProgram()) {
			System.err.println("Robot has no program to save.");
			return -1;
		}

		try {
			robot.getProgram().save(path);
			return 0;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			return -1;
		}
	}

	@Override
	public void prettyPrintProgram(Robot robot, Writer writer) {
		if (!robot.hasProgram()) {
			System.err.println("Robot has no program to print.");
			return;
		}

		try {
			writer.append(robot.getProgram().toSource());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public void stepn(Robot robot, int n) {
		try {
			robot.stepProgram(n);
		} catch (IllegalStateException e) {
			// Silently skip robots with no programs
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	@Override
	public Set<Battery> getRobotBatteries(Robot robot) {
		return robot.getPossessions(Battery.class);
	}

	@Override
	public Set<RepairKit> getRobotRepairKits(Robot robot) {
		return robot.getPossessions(RepairKit.class);
	}

	@Override
	public Set<SurpriseBox> getRobotSurpriseBoxes(Robot robot) {
		return robot.getPossessions(SurpriseBox.class);
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
	public void terminateRepairKit(RepairKit repairKit) {
		repairKit.terminate();
	}

	@Override
	public void terminateSurpriseBox(SurpriseBox surpriseBox) {
		surpriseBox.terminate();
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

	private void pickUpItem(Robot robot, Item item) {
		try {
			robot.pickUp(item);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void useItem(Robot robot, Item item) {
		try {
			robot.use(item);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void dropItem(Robot robot, Item item) {
		try {
			robot.drop(item);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

}