package roborally.view;

import roborally.IRobot;
import roborally.InvalidPositionException;
import roborally.Robot;

public class Facade implements IFacade {

	@Override
	public IRobot createRobot(long x, long y, int orientation, double energy) {
		try {
			return new Robot(x, y, orientation, energy);
		} catch (Exception e) {
			// Invalid position or energy
			return null;
		}
	}

	@Override
	public long getX(IRobot robot) {
		return robot.getX();
	}

	@Override
	public long getY(IRobot robot) {
		return robot.getY();
	}

	@Override
	public int getOrientation(IRobot robot) {
		return robot.getOrientation().getValue();
	}

	@Override
	public void move(IRobot robot) {
		if (robot.canMove()) {
			try {
				robot.move();
			} catch (Exception e) {
				// Invalid position
			}
		}
	}

	@Override
	public void turnClockwise(IRobot robot) {
		if (robot.canTurn()) {
			try {
				robot.turnClockwise();
			} catch (Exception e) {
				// Invalid position
			}
		}
	}

	@Override
	public double getEnergy(IRobot robot) {
		return robot.getEnergy();
	}

	@Override
	public void recharge(IRobot robot, double energyAmount) {
		robot.recharge(energyAmount);
	}

	@Override
	public int isGetEnergyRequiredToReachAndMoveNextTo16Plus() {
		return Robot.isGetEnergyRequiredToReachAndMoveNextTo16Plus() ? 1 : 0;
	}

	@Override
	public double getEnergyRequiredToReach(IRobot robot, long x, long y) {
		try {
			return robot.getEnergyRequiredToReach(x, y);
		} catch(InvalidPositionException e) {
			// Invalid positions are not reachable
			return Double.MAX_VALUE;
		}
	}

	@Override
	public void moveNextTo(IRobot robot, IRobot robot2) {
		robot.moveNextTo(robot2);
	}

}
