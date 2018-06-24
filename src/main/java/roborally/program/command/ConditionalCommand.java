package roborally.program.command;

import roborally.Robot;
import roborally.program.condition.Condition;

/**
 * A command with a condition.
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
public interface ConditionalCommand extends Command {

	/**
	 * Get the condition of this conditional command.
	 */
	public abstract Condition getCondition();

	/**
	 * Set the condition of this conditional command.
	 * 
	 * @param condition
	 * 			The new condition.
	 * 
	 * @throws	IllegalArgumentException
	 * 			If this conditional command cannot have
	 * 			the given condition as its condition.
	 * 			| !canHaveAsCondition(condition)
	 */
	public abstract void setCondition(Condition condition) throws IllegalArgumentException;

	/**
	 * Check whether the given condition is valid
	 * for this conditional command.
	 * 
	 * @param condition
	 * 			The condition to validate.
	 * 
	 * @return	False if the given condition is not effective.
	 *			| if (condition != null)
	 *			|   result == false
	 * @return	False if the given condition is not properly
	 *			constructed.
	 *			| else if (!condition.isConstructed())
	 *			|   result == false
	 * @return	False if the given condition has this
	 * 			conditional command as a sub statement.
	 * 			| else if (condition.hasAsSubStatement(this))
	 * 			|   result == false
	 */
	public abstract boolean canHaveAsCondition(Condition condition);

	/**
	 * Evaluate the condition of this conditional command.
	 * 
	 * @param robot
	 * 			The robot to evaluate the condition on.
	 * 
	 * @return	True if and only if this command's condition
	 * 			evaluates to true.
	 * 			| result == getCondition().evaluate(robot)
	 * 
	 * @throws	IllegalStateException
	 * 			If this command is not properly constructed.
	 * 			| !isConstructed()
	 */
	public abstract boolean evaluateCondition(Robot robot) throws IllegalStateException;

}
