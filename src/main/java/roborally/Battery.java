package roborally;

import roborally.EnergyAmount.Unit;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;

/**
 * A battery in a game of RoboRally.
 * 
 * <p>A battery is an item that can be used by a robot
 * as an energy source.</p>
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
public class Battery extends Item implements EnergyCarrier {

	/**
	 * Create a new battery with the given energy and weight.
	 * 
	 * @param weight
	 * 			The weight in grams for this new battery.
	 * @param energyAmount
	 * 			The energy amount for this new battery.
	 * 
	 * @pre		The given amount of energy must be valid
	 * 			for the new maximum energy.
	 * 			| isValidEnergy(energyAmount, new.getMaximumEnergy())
	 * 
	 * @post	The maximum amount of energy of the new battery equals 5000 Ws.
	 * 			| new.getMaximumEnergy().getAmount(Unit.WATTSECOND) == 5000.0
	 * @post	The absorption energy of the new battery equals 500 Ws.
	 * 			| new.getAbsorptionEnergy().getAmount(Unit.WATTSECOND) == 500.0
	 * @effect	The new battery is initialized as a new item
	 * 			with the given weight.
	 * 			| super(weight)
	 * @effect	The new battery's energy amount is set
	 * 			to the given amount of energy.
	 * 			| setEnergy(energyAmount, new.getMaximumEnergy())
	 */
	public Battery(int weight, EnergyAmount energyAmount) {
		super(weight);
		setEnergy(energyAmount);
	}

	/**
	 * Create a new battery with the given energy and weight.
	 * 
	 * @param weight
	 * 			The weight in grams for this new battery.
	 * @param energy
	 * 			The amount of energy for this new battery.
	 * @param energyUnit
	 * 			The unit in which the energy amount is expressed.
	 * 
	 * @effect	The new battery is initialized with an energy amount
	 * 			described by the given amount and unit.
	 * 			| this(weight, new EnergyAmount(energy, energyUnit))
	 */
	public Battery(int weight, double energy, Unit energyUnit) {
		this(weight, new EnergyAmount(energy, energyUnit));
	}

	/**
	 * Create a new battery with the given energy and weight.
	 * 
	 * @param weight
	 * 			The weight in grams for this new battery.
	 * @param energy
	 * 			The amount of energy in Watt-seconds for this new battery.
	 * 
	 * @effect	The new battery is initialized with an energy amount
	 * 			expressed in Watt-seconds.
	 * 			| this(weight, energy, Unit.WATTSECOND)
	 */
	public Battery(int weight, double energy) {
		this(weight, energy, Unit.WATTSECOND);
	}

	/**
	 * Create a new battery with the given weight.
	 * 
	 * @param weight
	 * 			The weight for this new battery.
	 * 
	 * @effect	The new battery is initialized with no energy
	 * 			and the given weight.
	 * 			| this(weight, EnergyAmount.ZERO)
	 */
	public Battery(int weight) {
		this(weight, EnergyAmount.ZERO);
	}

	/*
	 * Energy
	 */

	@Basic
	@Override
	public EnergyProperty getEnergy() {
		return energy;
	}

	@Override
	public EnergyAmount getEnergyAmount() {
		return getEnergy().getAmount();
	}

	@Override
	public double getEnergyAmount(Unit unit) {
		return getEnergyAmount().getAmount(unit);
	}

	/**
	 * Set the amount of energy stored by this battery.
	 * 
	 * @param amount
	 * 			The new amount of energy.
	 * 
	 * @effect	The battery's energy is set for this battery's maximum energy.
	 * 			| setEnergy(amount, getMaximumEnergy())
	 * @see #setEnergy(EnergyAmount, EnergyAmount)
	 */
	public void setEnergy(EnergyAmount amount) throws IllegalStateException {
		setEnergy(amount, getMaximumEnergy());
	}

	/**
	 * Set the amount of energy stored by this battery
	 * for the given maximum amount of energy.
	 * 
	 * @param amount
	 * 			The new amount of energy.
	 * @param maximumAmount
	 * 			The maximum amount of energy.
	 * 
	 * @pre		The amount of energy must be valid
	 * 			for the given maximum amount of energy.
	 * 			| isValidEnergy(amount, maximumAmount)
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergyAmount().equals(amount)
	 * @throws	IllegalStateException
	 * 			If the battery is terminated.
	 * 			| isTerminated()
	 * @see #isValidEnergy(EnergyAmount, EnergyAmount)
	 */
	@Model
	void setEnergy(EnergyAmount amount, EnergyAmount maximumAmount) throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Battery cannot be terminated.");

		assert isValidEnergy(amount, maximumAmount);
		getEnergy().setAmount(amount, maximumAmount);
	}

	/**
	 * @return	True if and only if the amount of energy is valid
	 * 			for this battery's energy property.
	 * 			| result == getEnergy().isValidAmount(amount)
	 * @see #isValidEnergy(EnergyAmount, EnergyAmount)
	 */
	@Override
	public boolean isValidEnergy(EnergyAmount amount) {
		return getEnergy().isValidAmount(amount);
	}

	/**
	 * Check whether the given amount of energy is valid
	 * for the given maximum amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to validate.
	 * @param maximumAmount
	 * 			The maximum amount of energy.
	 * 
	 * @return	True if and only if the given amount of energy is
	 * 			a valid amount for an energy property with
	 * 			the given maximum amount of energy.
	 * 			| result == EnergyProperty.isValidAmount(amount, maximumAmount)
	 */
	public static boolean isValidEnergy(EnergyAmount amount, EnergyAmount maximumAmount) {
		return EnergyProperty.isValidAmount(amount, maximumAmount);
	}

	/**
	 * Variable registering the amount of energy stored by this battery.
	 */
	private EnergyProperty energy = new StoredEnergy();

	/**
	 * Get the maximum amount of energy that can be stored
	 * by this battery.
	 */
	@Basic
	public EnergyAmount getMaximumEnergy() {
		return maximumEnergy;
	}

	/**
	 * Check whether the given maximum energy amount is valid
	 * for a battery.
	 * 
	 * @return	True if the given maximum amount is a valid
	 * 			maximum amount for an energy property.
	 * 			| result == EnergyProperty.isValidMaximumAmount(maximumEnergy)
	 */
	public static boolean isValidMaximumEnergy(EnergyAmount maximumEnergy) {
		return EnergyProperty.isValidMaximumAmount(maximumEnergy);
	}

	/**
	 * Variable registering the maximum amount of energy
	 * that can be stored by a battery.
	 */
	private static final EnergyAmount maximumEnergy = new EnergyAmount(Double.MAX_VALUE, Unit.WATTSECOND);

	/**
	 * Recharge this battery with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @pre		This battery can be recharged with the given amount.
	 * 			| canRecharge(amount)
	 * 
	 * @effect	This battery's energy property is recharged with the given amount.
	 * 			| getEnergy().recharge(amount)
	 */
	public void recharge(EnergyAmount amount) {
		assert canRecharge(amount);
		getEnergy().recharge(amount);
	}

	/**
	 * Check whether this battery can be recharged
	 * with the given amount of energy.
	 * 
	 * @return	False if this battery is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this battery's
	 * 			energy property can be recharged with
	 * 			the given amount.
	 * 			| else
	 * 			|   result == getEnergy().canRecharge(amount)
	 */
	public boolean canRecharge(EnergyAmount amount) {
		if (isTerminated())
			return false;
		return getEnergy().canRecharge(amount);
	}

	/**
	 * Drain this battery with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to drain.
	 * 
	 * @pre		This battery can be drained with the given amount.
	 * 			| canDrain(amount)
	 * 
	 * @effect	This battery's energy property is drained with the given amount.
	 * 			| getEnergy().drain(amount)
	 */
	public void drain(EnergyAmount amount) {
		assert canDrain(amount);
		getEnergy().drain(amount);
	}

	/**
	 * Check whether this battery can be drained
	 * with the given amount of energy.
	 * 
	 * @return	False if this battery is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this battery's
	 * 			energy property can be drained with
	 * 			the given amount.
	 * 			| else
	 * 			|   result == getEnergy().canDrain(amount)
	 */
	public boolean canDrain(EnergyAmount amount) {
		if (isTerminated())
			return false;
		return getEnergy().canDrain(amount);
	}

	/**
	 * @pre		The given amount can be transferred from this
	 * 			battery to the receiving energy carrier.
	 * 			| canTransfer(receiver, amount)
	 *
	 * @effect	The given amount is transferred from this battery's
	 * 			energy property to the receiving carrier's
	 * 			energy property.
	 * 			| getEnergy().transfer(receiver.getEnergy(), amount)
	 */
	@Override
	public void transfer(EnergyCarrier receiver, EnergyAmount amount) {
		assert canTransfer(receiver, amount);
		getEnergy().transfer(receiver.getEnergy(), amount);
	}

	/**
	 * @return	False if this battery is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	False if the receiving carrier is not effective.
	 * 			| else if (receiver == null)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the given amount
	 * 			can be transferred from this battery's energy property
	 * 			to the receiving carrier's energy property.
	 * 			| else
	 * 			|   result == getEnergy().canTransfer(receiver.getEnergy(), amount)
	 */
	@Override
	public boolean canTransfer(EnergyCarrier receiver, EnergyAmount amount) {
		if (isTerminated())
			return false;
		if (receiver == null)
			return false;
		return getEnergy().canTransfer(receiver.getEnergy(), amount);
	}

	/*
	 * Using
	 */

	/**
	 * Use this battery on a given robot, transferring energy
	 * from this battery to the robot.
	 * 
	 * @effect	The amount of energy stored in this battery
	 * 			is transferred to the robot.
	 * 			| transfer(robot, getEnergyAmount())
	 */
	@Override
	public void use(Robot robot) throws IllegalStateException, IllegalArgumentException {
		if (isTerminated())
			throw new IllegalStateException("Cannot use terminated items.");
		if (robot == null)
			throw new IllegalArgumentException("Robot must be effective.");
		if (!robot.hasAsPossession(this))
			throw new IllegalArgumentException("Robot must have this item as one of its possessions.");

		transfer(robot, getEnergyAmount());
	}

	/*
	 * Shooting
	 */

	/**
	 * @effect	The absorption energy amount is recharged by this battery,
	 * 			or the battery fills up to its maximum energy amount.
	 * 			| let
	 * 			|   rechargeAmount = EnergyAmount.min(
	 * 			|      getAbsorptionEnergy(),
	 * 			|      getEnergy().getFreeAmount()
	 * 			| )
	 * 			| recharge(rechargeAmount)
	 */
	@Override
	public void hit() throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Battery must not be terminated.");

		EnergyAmount rechargeAmount = EnergyAmount.min(getAbsorptionEnergy(), getEnergy().getFreeAmount());
		recharge(rechargeAmount);
	}

	/**
	 * Get the energy amount that can be absorbed
	 * when this battery is hit by a laser or explosion.
	 */
	@Basic
	@Immutable
	public EnergyAmount getAbsorptionEnergy() {
		return absorptionEnergy;
	}

	/**
	 * Variable registering the energy amount
	 * that can be absorbed when hit by a laser or explosion.
	 * 
	 * @invar	The absorption energy is effective.
	 * 			| absorptionEnergy != null
	 */
	private static final EnergyAmount absorptionEnergy = new EnergyAmount(500.0, Unit.WATTSECOND);

	@Override
	public String toString() {
		String result = super.toString();
		result += String.format(" with %s energy", getEnergy());
		return result;
	}

	private class StoredEnergy extends EnergyProperty {

		/**
		 * @return	The result is the maximum amount of energy
		 * 			of this battery.
		 * 			| result.equals(Battery.this.getMaximumEnergy())
		 */
		@Override
		public EnergyAmount getMaximumAmount() {
			return Battery.this.getMaximumEnergy();
		}

	}
}
