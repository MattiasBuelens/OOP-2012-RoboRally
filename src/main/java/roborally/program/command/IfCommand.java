package roborally.program.command;

import roborally.Robot;
import roborally.program.Statement;
import roborally.program.condition.Condition;
import be.kuleuven.cs.som.annotate.Basic;

/**
 * A command which conditionally executed
 * a then command or an else command.
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
public class IfCommand implements ConditionalCommand {

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

		// If this if command already has
		// the given condition, return false
		if (hasAsSubStatement(condition))
			return false;

		// Check if it is a valid condition
		return canHaveAsCondition(condition);
	}

	protected boolean canApply(Command command) {
		// If no condition set yet,
		// return false
		if (getCondition() == null)
			return false;

		// If else command is set, all commands
		// have already been set
		if (getElseCommand() != null)
			return false;

		// If this if command already has
		// the given command, return false
		if (hasAsSubStatement(command))
			return false;

		// If there is no then command yet,
		// check if it is a valid then command
		if (getThenCommand() == null)
			return canHaveAsThenCommand(command);

		// If there is a then command
		// but no else command yet,
		// check if it is a valid else command
		return canHaveAsElseCommand(command);
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);

		if (obj instanceof Condition)
			setCondition((Condition) obj);
		if (obj instanceof Command) {
			if (getThenCommand() == null)
				setThenCommand((Command) obj);
			else
				setElseCommand((Command) obj);
		}
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
			throw new IllegalArgumentException("Invalid condition for this if statement.");
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
			throw new IllegalStateException("If statement is not properly constructed.");
		return getCondition().evaluate(robot);
	}

	/*
	 * Then command
	 */

	/**
	 * Get the then command of this if command.
	 */
	@Basic
	public Command getThenCommand() {
		return thenCommand;
	}

	/**
	 * Set the then command of this if command.
	 *
	 * @param command
	 *			The new then command.
	 *
	 * @post	The new then command equals the given command.
	 *			| new.getCommand() == command
	 * @throws	IllegalArgumentException
	 *			If the given command is not valid.
	 *			| !canHaveAsThenCommand(command)
	 * @see #canHaveAsThenCommand(Command)
	 */
	public void setThenCommand(Command command) throws IllegalArgumentException {
		if (!canHaveAsThenCommand(command))
			throw new IllegalArgumentException("Invalid command for this if statement.");
		this.thenCommand = command;
	}

	/**
	 * Check whether the given then command is valid
	 * for this if statement.
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
	 * 			command does not have has this if command
	 * 			as a sub statement.
	 * 			| else
	 * 			|   result == !command.hasAsSubStatement(this)
	 */
	public boolean canHaveAsThenCommand(Command command) {
		if (command == null)
			return false;
		if (!command.isConstructed())
			return false;
		if (command.hasAsSubStatement(this))
			return false;
		return true;
	}

	/**
	 * Variable registering the then command of this if command.
	 */
	private Command thenCommand;

	/*
	 * Else command
	 */

	/**
	 * Get the else command of this if statement.
	 */
	@Basic
	public Command getElseCommand() {
		return elseCommand;
	}

	/**
	 * Set the else command of this if command.
	 *
	 * @param command
	 *			The new else command.
	 *
	 * @post	The new else command equals the given command.
	 *			| new.getCommand() == command
	 * @throws	IllegalArgumentException
	 *			If the given command is not valid.
	 *			| !canHaveAsElseCommand(command)
	 * @see #canHaveAsElseCommand(Command)
	 */
	public void setElseCommand(Command command) throws IllegalArgumentException {
		if (!canHaveAsElseCommand(command))
			throw new IllegalArgumentException("Invalid command for this if statement.");
		this.elseCommand = command;
	}

	/**
	 * Check whether the given else command is valid
	 * for this if statement.
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
	 * 			command does not have has this if command
	 * 			as a sub statement.
	 * 			| else
	 * 			|   result == !command.hasAsSubStatement(this)
	 */
	public boolean canHaveAsElseCommand(Command command) {
		if (command == null)
			return false;
		if (!command.isConstructed())
			return false;
		if (command.hasAsSubStatement(this))
			return false;
		return true;
	}

	/**
	 * Variable registering the else command of this if command.
	 */
	private Command elseCommand;

	/**
	 * Get the branch this if command is currently in.
	 */
	@Basic
	public Command getCurrentBranch() {
		return currentBranch;
	}

	/**
	 * Check whether this if command is currently
	 * inside any of its commands.
	 * 
	 * @return	True if and only if this if command's
	 * 			current branch is effective.
	 * 			| result == (getCurrentCommand() != null)
	 */
	public boolean isInBranch() {
		return getCurrentBranch() != null;
	}

	/**
	 * Enter the given branch.
	 * 
	 * @pre		This if command is not inside any branch.
	 * 			| !isInBranch()
	 * 
	 * @post	The new current branch is the given command.
	 * 			| new.getCurrentBranch() == command
	 */
	private void enterBranch(Command command) {
		assert !isInBranch();
		this.currentBranch = command;
	}

	/**
	 * Exit the branch this if command is currently in.
	 * 
	 * @pre		This if command is inside a branch.
	 * 			| isInBranch()
	 * 
	 * @post	This if command is no longer in any branch.
	 * 			| !new.isInBranch()
	 */
	private void exitBranch() {
		assert isInBranch();
		this.currentBranch = null;
	}

	/**
	 * Variable registering the current branch of this if command.
	 */
	private Command currentBranch;

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		if (this == statement)
			return true;
		if (getCondition() != null && getCondition().hasAsSubStatement(statement))
			return true;
		if (getElseCommand() != null && getElseCommand().hasAsSubStatement(statement))
			return true;
		if (getThenCommand() != null && getThenCommand().hasAsSubStatement(statement))
			return true;
		return false;
	}

	@Override
	public boolean isConstructed() {
		return canHaveAsCondition(getCondition()) && canHaveAsThenCommand(getThenCommand())
				&& canHaveAsElseCommand(getElseCommand());
	}

	/**
	 * @return	True if and only if this if command
	 * 			is inside a branch and the current branch
	 * 			can stay the current command.
	 * 			| result == isInBranch()
	 * 			|   && getCurrentBranch().canStayCurrent(robot)
	 */
	@Override
	public boolean canStayCurrent(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("If statement is not properly constructed.");

		return isInBranch() && getCurrentBranch().canStayCurrent(robot);
	}

	@Override
	public boolean step(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("If statement is not properly constructed.");

		if (!isInBranch()) {
			// Enter branch if not in one
			if (evaluateCondition(robot)) {
				enterBranch(getThenCommand());
			} else {
				enterBranch(getElseCommand());
			}
		} else if (!getCurrentBranch().canStayCurrent(robot)) {
			// Exit branch if cannot stay on it
			exitBranch();
			return false;
		}

		if (getCurrentBranch().step(robot)) {
			// Stepped inside branch
			return true;
		} else {
			// Cannot step, exit
			exitBranch();
			return false;
		}

	}

	@Override
	public void execute(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("If statement is not properly constructed.");
		if (!isInBranch())
			throw new IllegalStateException("If statement is not inside a branch.");

		getCurrentBranch().execute(robot);
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("If statement is not properly constructed.");

		StringBuilder builder = new StringBuilder();
		builder.append("(if ");
		builder.append(getCondition().toSource()).append(' ');
		builder.append(getThenCommand().toSource()).append(')');
		builder.append(getElseCommand().toSource()).append(')');
		return builder.toString();
	}

}
