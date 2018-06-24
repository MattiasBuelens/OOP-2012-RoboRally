package roborally.program.condition;

import roborally.Robot;
import roborally.program.Statement;

/**
 * A condition which always evaluates to true.
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
public class TrueCondition implements Condition {

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
		assert false;
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

	/**
	 * @return	Always true.
	 * 			| result == true
	 */
	@Override
	public boolean evaluate(Robot robot) {
		return true;
	}

	@Override
	public String toSource() throws IllegalStateException {
		return "(true)";
	}

}
