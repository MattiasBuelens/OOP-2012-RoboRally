package roborally.program.command;

import be.kuleuven.cs.som.annotate.Basic;
import roborally.Robot;
import roborally.Rotation;
import roborally.program.Statement;

/**
 * A command which makes the robot turn
 * in the specified rotation direction.
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
public class TurnCommand implements Command {

	@Override
	public boolean canApply(Object obj) {
		if (!(obj instanceof String))
			return false;
		return canApply((String) obj);
	}

	protected boolean canApply(String argument) {
		if (isConstructed())
			return false;
		return Rotation.getByName(argument) != null;
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);
		setRotation(Rotation.getByName((String) obj));
	}

	/**
	 * Get the rotation direction of this turn command.
	 */
	@Basic
	public Rotation getRotation() {
		return rotation;
	}

	/**
	 * Set the rotation direction of this turn command.
	 *
	 * @param rotation
	 *			The new rotation direction.
	 *
	 * @post	The new rotation direction equals
	 * 			the given rotation direction.
	 *			| new.getRotation() == rotation
	 * @throws	IllegalArgumentException
	 *			If the given rotation direction is not valid.
	 *			| !canHaveAsRotation(rotation)
	 * @see #canHaveAsRotation(Rotation)
	 */
	public void setRotation(Rotation rotation) throws IllegalArgumentException {
		if (!canHaveAsRotation(rotation))
			throw new IllegalArgumentException("Invalid rotation for this turn command.");
		this.rotation = rotation;
	}

	/**
	 * Check whether the given rotation direction
	 * is valid for this turn command.
	 *
	 * @param rotation
	 *			The rotation direction to validate.
	 *
	 * @return	True if the given rotation direction
	 * 			is effective.
	 *			| result == (rotation != null)
	 */
	public boolean canHaveAsRotation(Rotation rotation) {
		return (rotation != null);
	}

	/**
	 * Variable registering the rotation direction of this turn command.
	 */
	private Rotation rotation;

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		return (this == statement);
	}

	@Override
	public boolean isConstructed() {
		return canHaveAsRotation(getRotation());
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
	public boolean step(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Turn command is not properly constructed.");
		return true;
	}

	@Override
	public void execute(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Turn command is not properly constructed.");

		if (robot.canTurn()) {
			try {
				robot.turn(getRotation());
			} catch (Exception e) {
			}
		}
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Turn command is not properly constructed.");
		return "(turn " + getRotation().getName() + ")";
	}

}
