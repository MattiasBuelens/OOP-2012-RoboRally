package roborally.program.condition;

import java.util.Set;

import roborally.Item;
import roborally.Robot;
import roborally.program.Statement;

/**
 * A condition which evaluates to true
 * if and only if the robot is at
 * the same position as an item.
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
public class AtItemCondition implements Condition {

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
			throw new IllegalStateException("Energy at least condition not properly constructed.");

		return String.format("(at-item)");
	}

	@Override
	public boolean evaluate(Robot robot) throws IllegalStateException {
		// False if the robot is not placed
		if (!robot.isPlaced())
			return false;

		// Item must be located on same position as robot
		Set<Item> items = robot.getBoard().getPiecesAt(robot.getPosition(), Item.class);

		// True if and only if any items were found
		return !items.isEmpty();
	}

}
