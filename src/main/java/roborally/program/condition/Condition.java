package roborally.program.condition;

import roborally.Robot;
import roborally.program.Statement;

/**
 * A condition in a program.
 * 
 * <p>A condition is a statement which can be
 * evaluated to a boolean value by a robot.</p>
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
public interface Condition extends Statement {

	/**
	 * Evaluate the condition.
	 * 
	 * @param robot
	 * 			The robot running the program
	 * 			containing this condition.
	 * 
	 * @throws	IllegalStateException
	 * 			If this command is not properly constructed yet.
	 * 			| !isConstructed()
	 */
	public abstract boolean evaluate(Robot robot) throws IllegalStateException;

}
