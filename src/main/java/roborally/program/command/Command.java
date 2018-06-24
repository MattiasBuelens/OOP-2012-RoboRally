package roborally.program.command;

import roborally.Robot;
import roborally.program.Statement;

/**
 * A command in a program.
 * 
 * <p>A command is a statement which can be executed
 * by a robot.</p>
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
public interface Command extends Statement {

	/**
	 * Check if this command can stay as
	 * the current command.
	 * 
	 * @param robot
	 * 			The robot running the program
	 * 			containing this command.
	 * 
	 * @return	True if and only if this command
	 * 			can stay as the current command.
	 * 
	 * @throws	IllegalStateException
	 * 			If this command is not properly constructed yet.
	 * 			| !isConstructed()
	 */
	public abstract boolean canStayCurrent(Robot robot) throws IllegalStateException;

	/**
	 * Move the program counter one step forward.
	 * 
	 * <p>If this command cannot handle the step,
	 * the step should be handled by the containing
	 * command or the program is stopped.</p>
	 * 
	 * @param robot
	 * 			The robot running the program
	 * 			containing this command.
	 * 
	 * @return	True if and only if this command
	 * 			has handled the step.
	 * 
	 * @throws	IllegalStateException
	 * 			If this command is not properly constructed yet.
	 * 			| !isConstructed()
	 */
	public abstract boolean step(Robot robot) throws IllegalStateException;

	/**
	 * Execute the command.
	 * 
	 * @param robot
	 * 			The robot running the program
	 * 			containing this command.
	 * 
	 * @throws	IllegalStateException
	 * 			If this command is not properly constructed yet.
	 * 			| !isConstructed()
	 */
	public abstract void execute(Robot robot) throws IllegalStateException;

}
