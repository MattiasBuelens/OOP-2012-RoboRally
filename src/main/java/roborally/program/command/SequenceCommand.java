package roborally.program.command;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

import roborally.Robot;
import roborally.program.Statement;

/**
 * A command composed of multiple commands
 * which are executed sequentially.
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
public class SequenceCommand implements Command {

	@Override
	public boolean canApply(Object obj) {
		if (!(obj instanceof Command))
			return false;
		return canApply((Command) obj);
	}

	protected boolean canApply(Command command) {
		return canAddAsCommand(command);
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);
		addAsCommand((Command) obj);
	}

	/**
	 * Get the number of commands in this sequence.
	 */
	@Basic
	@Raw
	public int getNbCommands() {
		return commands.size();
	}

	/**
	 * Get the command at the given index in this sequence.
	 * 
	 * @param index
	 * 			The index of the command.
	 * 
	 * @return	If the index is non-positive or exceeds
	 * 			the number of commands, null is returned.
	 * 			| if (index <= 0 || index > getNbCommands())
	 * 			|   result = null
	 */
	@Basic
	@Raw
	public Command getCommandAt(int index) {
		if (index <= 0 || index > getNbCommands())
			return null;
		return commands.get(index - 1);
	}

	/**
	 * Check whether the given command is valid for this sequence.
	 *
	 * @param command
	 *			The command to validate.
	 *
	 * @return	False if the given command is not effective.
	 *			| if (command != null)
	 *			|   result == false
	 * @return	False if the given command is not properly
	 *			constructed.
	 *			| else if (!command.isConstructed())
	 *			|   result == false
	 * @return	Otherwise, true if and only if the given
	 * 			command does not have this sequence
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
	 * Check whether the given command can be added
	 * to this sequence.
	 * 
	 * @param command
	 *			The command to add.
	 *
	 * @return	False if this sequence cannot have
	 * 			the given command.
	 * 			| if (!canHaveAsCommand(command))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this sequence
	 * 			does not have the given command as a sub statement.
	 * 			| else
	 * 			|   result == !hasAsSubStatement(command)
	 */
	public boolean canAddAsCommand(Command command) {
		return canHaveAsCommand(command) && !hasAsSubStatement(command);
	}

	/**
	 * Add the given command to this sequence.
	 * 
	 * @param command
	 * 			The command to add.
	 * 
	 * @post	The number of commands in this sequence
	 * 			is incremented by one.
	 * 			| new.getNbCommands() == this.getNbCommands() + 1
	 * @post	The given command is added at the end
	 * 			of this sequence.
	 * 			| new.getCommandAt(new.getNbCommands()) == command
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given command cannot be added to this sequence.
	 * 			| !canAddAsCommand(command)
	 */
	public void addAsCommand(Command command) throws IllegalArgumentException {
		if (!canAddAsCommand(command))
			throw new IllegalArgumentException("Invalid command for command sequence: " + command);
		commands.add(command);
	}

	/**
	 * List of commands in this sequence.
	 * 
	 * @invar	The list of commands is effective.
	 * 			| commands != null
	 * @invar	This sequence can have each command in the list.
	 * 			| for each command in commands:
	 * 			|   canHaveAsCommand(command)
	 */
	private List<Command> commands = new ArrayList<Command>();

	/**
	 * Get the index of the current command
	 * of this sequence.
	 */
	@Basic
	@Model
	int getCurrentCommandIndex() {
		return currentCommandIndex;
	}

	/**
	 * Get the current command of this sequence.
	 * 
	 * @return	The current command, or null if
	 * 			this sequence has no current command.
	 * 			| result == getCommandAt(getCurrentCommandIndex())
	 */
	@Raw
	public Command getCurrentCommand() {
		return getCommandAt(getCurrentCommandIndex());
	}

	/**
	 * Check whether this sequence has another command
	 * after the current command.
	 * 
	 * @return	True if and only if the current command index
	 * 			is less than or equal to the number
	 * 			of commands in this sequence.
	 * 			| getCurrentCommandIndex() <= getNbCommands()
	 */
	public boolean hasNextCommand() {
		return getCurrentCommandIndex() <= getNbCommands();
	}

	/**
	 * Move to the next command in this sequence.
	 * 
	 * @post	The current command index is incremented by one.
	 * 			| new.getCurrentCommandIndex() == getCurrentCommandIndex() + 1
	 * @return	The new current command is returned.
	 * 			| result == new.getCurrentCommand()
	 */
	protected Command nextCommand() {
		currentCommandIndex++;
		return getCurrentCommand();
	}

	/**
	 * Reset this sequence to just before the first command.
	 * 
	 * @post	The current command index is set to zero.
	 * 			| new.getCurrentCommandIndex() == 0
	 * @post	The new current command will be non-effective.
	 * 			| new.getCurrentCommand() == null
	 */
	protected void resetCommand() {
		currentCommandIndex = 0;
	}

	/**
	 * Variable registering the index of the current command.
	 */
	private int currentCommandIndex = 0;

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		if (this == statement)
			return true;

		for (int i = 1; i <= getNbCommands(); ++i) {
			if (getCommandAt(i).hasAsSubStatement(statement))
				return true;
		}
		return false;
	}

	@Override
	public boolean isConstructed() {
		// Guaranteed by canHaveAsCommand(Command)
		return true;
	}

	@Override
	public boolean canStayCurrent(Robot robot) {
		// True if and only if this sequence
		// has a next command
		return hasNextCommand();
	}

	@Override
	public boolean step(Robot robot) {
		if (!isConstructed())
			throw new IllegalStateException("Sequence is not properly constructed.");

		Command command = getCurrentCommand();

		// If cannot stay at current command,
		// move to next command
		if (command == null || !command.canStayCurrent(robot))
			command = nextCommand();

		// Move to next command
		// as long as current command cannot step
		// and there are other commands in this sequence
		while ((command == null || !command.step(robot)) && hasNextCommand()) {
			command = nextCommand();
		}

		// If no next command found,
		// reset this sequence
		if (!hasNextCommand()) {
			resetCommand();
			return false;
		}

		// Found next command and stepped it
		return true;
	}

	@Override
	public void execute(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Sequence is not properly constructed.");

		Command command = getCurrentCommand();
		if (command == null)
			throw new IllegalStateException("Cannot execute command sequence when current command is not effective.");
		command.execute(robot);
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Sequence is not properly constructed.");

		StringBuilder builder = new StringBuilder();
		builder.append("(seq");
		for (int i = 1; i <= getNbCommands(); ++i) {
			builder.append(' ').append(getCommandAt(i).toSource());
		}
		builder.append(')');
		return builder.toString();
	}
}
