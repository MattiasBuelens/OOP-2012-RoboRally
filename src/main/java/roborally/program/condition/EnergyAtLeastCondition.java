package roborally.program.condition;

import java.util.Locale;

import roborally.Robot;
import roborally.EnergyAmount.Unit;
import roborally.program.Statement;
import be.kuleuven.cs.som.annotate.Basic;

/**
 * A condition which evaluates to true
 * if and only if the robot has an energy amount
 * greater than or equal to a specified amount.
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
public class EnergyAtLeastCondition implements Condition {

	@Override
	public boolean canApply(Object obj) {
		if (isConstructed())
			return false;

		if (obj instanceof Number)
			return true;
		if (obj instanceof String) {
			try {
				Double.parseDouble((String) obj);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		return false;
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);

		if (obj instanceof Number) {
			setMinimumEnergy(((Number) obj).doubleValue());
		} else if (obj instanceof String) {
			setMinimumEnergy(Double.parseDouble((String) obj));
		}
	}

	/**
	 * Get the minimum energy of this condition.
	 */
	@Basic
	public Double getMinimumEnergy() {
		return minimumEnergy;
	}

	/**
	 * Set the minimum energy of this condition.
	 *
	 * @param minimumEnergy
	 *			The new minimum energy.
	 *
	 * @post	The new minimum energy equals the given minimum energy.
	 *			| new.getMinimumEnergy() == minimumEnergy
	 * @throws	IllegalArgumentException
	 *			If the given minimum energy is not valid.
	 *			| !isValidMinimumEnergy(minimumEnergy)
	 * @see #isValidMinimumEnergy(double)
	 */
	public void setMinimumEnergy(double minimumEnergy) throws IllegalArgumentException {
		if (!isValidMinimumEnergy(minimumEnergy))
			throw new IllegalArgumentException("Minimum energy must be positive.");
		this.minimumEnergy = minimumEnergy;
	}

	/**
	 * Check whether the given minimum energy is valid for this condition.
	 *
	 * @param minimumEnergy
	 *			The minimum energy to validate.
	 *
	 * @return	True if the given minimum energy amount is positive.
	 *			| result == (minimumEnergy >= 0)
	 */
	public boolean isValidMinimumEnergy(double minimumEnergy) {
		return minimumEnergy >= 0;
	}

	/**
	 * Variable registering the minimum energy of this condition.
	 */
	private Double minimumEnergy = null;

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		return (this == statement);
	}

	@Override
	public boolean isConstructed() {
		return getMinimumEnergy() != null;
	}

	@Override
	public String toSource() throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Energy at least condition not properly constructed.");

		return String.format(Locale.US, "(energy-at-least %.2f)", getMinimumEnergy());
	}

	@Override
	public boolean evaluate(Robot robot) throws IllegalStateException {
		if (!isConstructed())
			throw new IllegalStateException("Energy at least condition not properly constructed.");

		return robot.getEnergyAmount(Unit.WATTSECOND) >= getMinimumEnergy();
	}

}
