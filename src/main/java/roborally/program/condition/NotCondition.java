package roborally.program.condition;

import roborally.Robot;

/**
 * A condition which inverts the evaluation
 * of another condition.
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
public class NotCondition extends FixedSizeComposedCondition {

	public NotCondition() {
		super(1);
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Not condition is not properly constructed.");

		return "(not " + getConditionAt(1).toSource() + ")";
	}

	@Override
	public boolean evaluate(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Not condition is not properly constructed.");

		return !getConditionAt(1).evaluate(robot);
	}

}
