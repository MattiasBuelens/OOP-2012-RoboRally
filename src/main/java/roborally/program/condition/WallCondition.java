package roborally.program.condition;

import roborally.Robot;
import roborally.Vector;
import roborally.Wall;
import roborally.program.Statement;

/**
 * A condition which evaluates to true
 * if and only if the robot is facing
 * a wall.
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
public class WallCondition implements Condition {

	/**
	 * @return	Always false.
	 * 			| result == false
	 */
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

	/**
	 * @return	Always true.
	 * 			| result == true
	 */
	@Override
	public boolean isConstructed() {
		return true;
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Wall condition not properly constructed.");

		return String.format("(wall)");
	}

	@Override
	public boolean evaluate(Robot robot) throws IllegalStateException {
		Vector next = robot.getNextPosition();

//		If another position next to the robot is meant
//		Orientation orientation = Orientation.RIGHT;
//		Vector next = robot.getPosition().add(orientation.getVector());

		// True if and only if there are any walls
		// at the next position on the board
		return !robot.getBoard().getPiecesAt(next, Wall.class).isEmpty();
	}
}
