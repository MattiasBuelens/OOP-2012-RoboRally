package roborally.program.command;

import roborally.Robot;
import roborally.program.Statement;
import roborally.program.condition.Condition;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A command which keeps executing a command
 * as long as a specified condition
 * evaluates to true.
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
public class WhileCommand implements ConditionalCommand {

	@Override
	public boolean canApply(Object obj) {
		if (obj instanceof Condition)
			return canApply((Condition) obj);
		if (obj instanceof Command)
			return canApply((Command) obj);
		return false;
	}

	protected boolean canApply(Condition condition) {
		// If condition already set,
		// return false
		if (getCondition() != null)
			return false;

		// If this while loop already has
		// the given condition, return false
		if (hasAsSubStatement(condition))
			return false;

		// Check if it is a valid condition
		return canHaveAsCondition(condition);
	}

	protected boolean canApply(Command command) {
		// If no condition is set yet,
		// return false
		if (getCondition() == null)
			return false;

		// If command is already set,
		// return false
		if (getCommand() != null)
			return false;

		// If this while loop already has
		// the given command, return false
		if (hasAsSubStatement(command))
			return false;

		// Check if it is a valid command
		return canHaveAsCommand(command);
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);

		if (obj instanceof Condition)
			setCondition((Condition) obj);
		if (obj instanceof Command)
			setCommand((Command) obj);
	}

	/*
	 * Condition
	 */

	@Override
	public Condition getCondition() {
		return condition;
	}

	@Override
	public void setCondition(Condition condition) throws IllegalArgumentException {
		if (!canHaveAsCondition(condition))
			throw new IllegalArgumentException("Invalid condition for this while loop.");
		this.condition = condition;
	}

	/**
	 * @return	False if the given condition is not effective.
	 *			| if (condition == null)
	 *			|   result == false
	 * @return	False if the given condition is not properly
	 *			constructed.
	 *			| else if (!condition.isConstructed())
	 *			|   result == false
	 * @return	Otherwise, true if and only if the given
	 * 			condition does not have has this while loop
	 * 			as a sub statement.
	 * 			| else
	 * 			|   result == !condition.hasAsSubStatement(this)
	 */
	@Override
	public boolean canHaveAsCondition(Condition condition) {
		if (condition == null)
			return false;
		if (!condition.isConstructed())
			return false;
		if (condition.hasAsSubStatement(this))
			return false;
		return true;
	}

	/**
	 * Variable registering the condition of this if command.
	 */
	private Condition condition;

	@Override
	public boolean evaluateCondition(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("While loop is not properly constructed.");
		return getCondition().evaluate(robot);
	}

	/**
	 * Get the command of this while loop.
	 */
	@Basic
	public Command getCommand() {
		return command;
	}

	/**
	 * Set the command of this while loop.
	 *
	 * @param command
	 *			The new command.
	 *
	 * @post	The new command equals the given command.
	 *			| new.getCommand() == command
	 * @throws	IllegalArgumentException
	 *			If the given command is not valid.
	 *			| !canHaveAsCommand(command)
	 * @see #canHaveAsCommand(Command)
	 */
	public void setCommand(Command command) throws IllegalArgumentException {
		if (!canHaveAsCommand(command))
			throw new IllegalArgumentException("Invalid command for this while loop.");
		this.command = command;
	}

	/**
	 * Check whether the given command is valid
	 * for this while loop.
	 *
	 * @param command
	 *			The command to validate.
	 *
	 * @return	False if the given command is not effective.
	 *			| if (command == null)
	 *			|   result == false
	 * @return	False if the given command is not properly
	 *			constructed.
	 *			| else if (!command.isConstructed())
	 *			|   result == false
	 * @return	Otherwise, true if and only if the given
	 * 			command does not have has this while loop
	 * 			as a sub statement.
	 * 			| else
	 * 			|   result == !command.hasAsSubStatement(this)
	 */
	public boolean canHaveAsCommand(Command command) {
		if (command == null)
			return false;
		if (!command.isConstructed())
			return false;
		if (command.hasAsSubStatement(this))
			return false;
		return true;
	}

	/**
	 * Variable registering the command of this while loop.
	 */
	private Command command;

	/**
	 * Check whether this while command is inside an iteration.
	 */
	@Basic
	@Raw
	public boolean isInIteration() {
		return isInIteration;
	}

	/**
	 * Enter an iteration.
	 * 
	 * @post	This while loop is now in an iteration.
	 * 			| new.isInIteration()
	 */
	private void enterIteration() {
		isInIteration = true;
	}

	/**
	 * Exit the iteration.
	 * 
	 * @post	This while loop is no longer in any iteration.
	 * 			| !new.isInIteration()
	 */
	private void exitIteration() {
		isInIteration = false;
	}

	/**
	 * Variable registering whether this while loop
	 * is currently inside an iteration.
	 */
	private boolean isInIteration = false;

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		if (this == statement)
			return true;
		if (getCondition() != null && getCondition().hasAsSubStatement(statement))
			return true;
		if (getCommand() != null && getCommand().hasAsSubStatement(statement))
			return true;
		return false;
	}

	@Override
	public boolean isConstructed() {
		return canHaveAsCondition(getCondition()) && canHaveAsCommand(getCommand());
	}

	@Override
	public boolean canStayCurrent(Robot robot) {
		if (!isConstructed())
			throw new IllegalStateException("While loop is not properly constructed.");
		// True if in iteration and command can stay current
		if (isInIteration() && getCommand().canStayCurrent(robot))
			return true;
		// True if and only if the while condition evaluates to true
		return evaluateCondition(robot);
	}

	@Override
	public boolean step(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("While loop is not properly constructed.");

		// If in iteration, true if command
		// can stay current and can handle the step
		if (isInIteration()) {
			if (getCommand().step(robot) && getCommand().canStayCurrent(robot))
				return true;
		}

		// Try to enter a new iteration
		if (evaluateCondition(robot) && getCommand().step(robot)) {
			enterIteration();
			return true;
		} else {
			exitIteration();
			return false;
		}
	}

	/**
	 * @throws	IllegalStateException
	 * 			If this while command is not inside an iteration.
	 * 			| !isInIteration()
	 */
	@Override
	public void execute(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("While loop is not properly constructed.");
		if (!isInIteration())
			throw new IllegalStateException("While loop not inside an iteration.");

		getCommand().execute(robot);
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("While loop is not properly constructed.");

		StringBuilder builder = new StringBuilder();
		builder.append("(while ");
		builder.append(getCondition().toSource()).append(' ');
		builder.append(getCommand().toSource()).append(')');
		return builder.toString();
	}
}
