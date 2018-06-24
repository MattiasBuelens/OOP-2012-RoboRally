package roborally.program.command;

import roborally.Robot;
import roborally.program.Statement;

/**
 * A simple command which doesn't contain any
 * other statements.
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
public abstract class SimpleCommand implements Command {

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

	/**
	 * @return	Always false.
	 * 			| result == false
	 */
	@Override
	public boolean canStayCurrent(Robot robot) {
		return false;
	}

	/**
	 * @return	Always true.
	 * 			| result == true
	 */
	@Override
	public boolean step(Robot robot) {
		return true;
	}

}