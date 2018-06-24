package roborally.program.condition;

import roborally.Piece;
import roborally.Robot;
import roborally.program.Statement;

/**
 * A condition which evaluates to true
 * if and only if the robot can
 * hit another robot by shooting its laser.
 * 
 * @author	Mattias Buelens
 * @author	Thomas Goossens
 * @version	3.0
 * 
 * @note	This class is part of the 2012 project for
 * 			the course Object Oriented Programming in
 * 			the second phase of the Bachelor of Engineering
 * 			at KU Leuven, Belgium.
 */
public class CanHitRobotCondition implements Condition {

	@Override
	public boolean canApply(Object obj) {
		return false;
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);
		throw new IllegalArgumentException("Unexpected subexpression for " + getClass().getSimpleName() + ": " + obj);
	}

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		return (this == statement);
	}

	@Override
	public boolean isConstructed() {

		return true;
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Can hit robot condition not properly constructed.");

		return String.format("(can-hit-robot)");
	}

	@Override
	public boolean evaluate(Robot robot) throws IllegalStateException {
		// False if the robot can't shoot
		if (!robot.canShoot())
			return false;

		// True if and only if there is a robot
		// in the set of target pieces
		for (Piece piece : robot.getShootTargets()) {
			if (piece instanceof Robot)
				return true;
		}
		return false;
	}

}
