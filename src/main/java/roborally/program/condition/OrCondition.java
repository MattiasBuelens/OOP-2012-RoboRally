package roborally.program.condition;

import roborally.Robot;

/**
 * A composed condition which evaluates
 * to true if and only two other conditions
 * are both true.
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
public class OrCondition extends FixedSizeComposedCondition {

	public OrCondition() {
		super(2);
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("And condition is not properly constructed.");

		StringBuilder builder = new StringBuilder();
		builder.append("(or");
		for (int i = 1; i <= getNbConditions(); ++i) {
			builder.append(' ').append(getConditionAt(i).toSource());
		}
		builder.append(')');
		return builder.toString();
	}

	@Override
	public boolean evaluate(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("And condition is not properly constructed.");

		for (int i = 1; i <= getNbConditions(); ++i) {
			if (getConditionAt(i).evaluate(robot))
				return true;
		}
		return false;
	}

}
