package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;

/**
 * Represents a battery in a game of RoboRally.
 * 
 * <p>A battery is an item that can be used as an energy source.</p>
 *
 * @invar	The batter's amount of energy is valid.
 * 			| isValidEnergy(getEnergy())
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class Battery extends Item implements EnergyCarrier {

	/**
	 * Create a new battery with the given energy and weight.
	 * 
	 * @param weight
	 * 			The weight for this new battery.
	 * @param energy
	 * 			The energy for this new battery.
	 * 
	 * @post	The maximum amount of energy of the new battery equals 5000 Ws.
	 * 			| new.getMaximumEnergy() == 5000.0
	 * @effect	The new battery is initialized as a new item
	 * 			with the given weight.
	 * 			| super(weight)
	 * @effect	The new battery's energy is set to the given amount of energy
	 * 			if its valid for the new maximum energy.
	 * 			| setEnergy(energy, new.getMaximumEnergy())
	 */
	public Battery(int weight, double energy) {
		super(weight);
		setEnergy(energy);
	}

	/**
	 * Create a new battery with the given weight.
	 * 
	 * @param weight
	 * 			The weight for this new battery.
	 * 
	 * @effect	The new battery is initialized with no energy
	 * 			and the given weight.
	 * 			| this(weight, 0.0d)
	 */
	public Battery(int weight) {
		this(weight, 0.0d);
	}

	/*
	 * Energy
	 */

	@Basic
	@Override
	public double getEnergy() {
		return energy;
	}

	/**
	 * Set the amount of energy stored by this battery.
	 * 
	 * @param energy
	 * 			The new amount of energy.
	 * 
	 * @effect	The battery's energy is set for this battery's maximum energy.
	 * 			| setEnergy(energy, getMaximumEnergy())
	 * @see #setEnergy(double, double)
	 */
	@Override
	public void setEnergy(double energy) {
		setEnergy(energy, getMaximumEnergy());
	}

	/**
	 * Set the amount of energy stored by this battery
	 * for the given maximum amount of energy.
	 * 
	 * @param energy
	 * 			The new amount of energy.
	 * @param maxEnergy
	 * 			The maximum amount of energy.
	 * 
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(energy)
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergy() == energy
	 * @throws	IllegalStateException
	 * 			If the battery is terminated.
	 * 			| isTerminated()
	 * @see #isValidEnergy(double)
	 */
	@Model
	void setEnergy(double energy, double maxEnergy) throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Battery cannot be terminated.");

		assert isValidEnergy(energy, maxEnergy);
		this.energy = energy;
	}

	/**
	 * Check whether the given amount of energy is a valid amount for this battery.
	 * 
	 * @param energy
	 * 			The amount of energy to validate.
	 * 
	 * @return	True if the amount of energy is valid for the maximum amount
	 * 			of energy of this battery, false otherwise.
	 * 			| result == isValidEnergy(energy, getMaximumEnergy())
	 * @see #isValidEnergy(double, double)
	 */
	public boolean isValidEnergy(double energy) {
		return isValidEnergy(energy, getMaximumEnergy());
	}

	/**
	 * Check whether the given amount of energy is valid.
	 * 
	 * @param energy
	 * 			The amount of energy to validate.
	 * @param maximumEnergy
	 * 			The maximum amount of energy.
	 * 
	 * @return	True if the amount of energy is positive and does not exceed
	 * 			the given maximum amount of energy, false otherwise.
	 * 			| result == (energy >= 0) && (energy <= maximumEnergy)
	 */
	public static boolean isValidEnergy(double energy, double maximumEnergy) {
		return energy >= 0 && energy <= maximumEnergy;
	}

	private double energy;

	@Basic
	@Override
	public double getMaximumEnergy() {
		return maximumEnergy;
	}

	/**
	 * Check whether the given maximum energy amount is valid for this battery.
	 * 
	 * @return	True if the given maximum amount is strictly positive.
	 * 			| result == (maximumEnergy > 0)
	 */
	public static boolean isValidMaximumEnergy(double maximumEnergy) {
		return maximumEnergy > 0;
	}

	private final double maximumEnergy = 5000;

	@Override
	public void recharge(double amount) {
		assert canRecharge(amount);
		setEnergy(getEnergy() + amount);
	}

	/**
	 * @return	False if this battery is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	False if the extra energy amount is negative.
	 * 			| if (amount < 0)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the new amount of energy is valid.
	 * 			| else
	 * 			|   result == isValidEnergy(getEnergy() + amount)
	 */
	@Override
	public boolean canRecharge(double amount) {
		if (isTerminated())
			return false;
		if (amount < 0)
			return false;
		return isValidEnergy(getEnergy() + amount);
	}

	@Override
	public void drain(double amount) {
		assert canDrain(amount);
		setEnergy(getEnergy() - amount);
	}

	/**
	 * @return	False if this battery is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	False if the extra energy amount is negative.
	 * 			| if (amount < 0)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the new amount of energy is valid.
	 * 			| else
	 * 			|   result == isValidEnergy(getEnergy() - amount)
	 */
	@Override
	public boolean canDrain(double amount) {
		if (isTerminated())
			return false;
		if (amount < 0)
			return false;
		return isValidEnergy(getEnergy() - amount);
	}

	/**
	 * @effect	The actual transfer amount is drained from this carrier.
	 * 			| let
	 * 			|	actualAmount = getTransferAmount(receivingCarrier, amount)
	 * 			| this.drain(actualAmount)
	 * @effect	The actual transfer amount is recharged by the receiving carrier.
	 * 			| receivingCarrier.recharge(actualAmount)
	 */
	@Override
	public void transfer(EnergyCarrier receivingCarrier, double amount) {
		assert canTransfer(receivingCarrier, amount);

		amount = getTransferAmount(receivingCarrier, amount);

		assert this.canDrain(amount);
		assert receivingCarrier.canRecharge(amount);

		this.drain(amount);
		receivingCarrier.recharge(amount);
	}

	/**
	 * @return	False if this battery is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	False if the receiving carrier is not effective.
	 * 			| if (receivingCarrier == null)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the given amount is positive.
	 * 			| else
	 * 			|   result == (amount >= 0)
	 */
	@Override
	public boolean canTransfer(EnergyCarrier receivingCarrier, double amount) {
		if (isTerminated())
			return false;
		if (receivingCarrier == null)
			return false;
		return amount >= 0;
	}

	/**
	 * Get the amount of energy which can actually be transferred
	 * from this battery to the receiving carrier.
	 * 
	 * @param receivingCarrier
	 * 			The receiving energy carrier.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @pre		The given amount can be transferred
	 * 			from this battery to the receiving carrier.
	 * 			| canTransfer(receivingCarrier, amount)
	 * 
	 * @return	The actual transfer amount is the minimum of the given amount,
	 * 			this battery's current energy amount and the receiving carrier's
	 * 			current amount of free storage.
	 * 			| let
	 * 			|	maxDrainAmount = this.getEnergy()
	 * 			|	maxRechargeAmount = receivingCarrier.getMaximumEnergy() - receivingCarrier.getEnergy()
	 * 			| result == min({amount, maxDrainAmount, maxRechargeAmount})
	 */
	public double getTransferAmount(EnergyCarrier receivingCarrier, double amount) {
		assert canTransfer(receivingCarrier, amount);

		// Cannot drain more than this amount of energy stored
		amount = Math.min(amount, this.getEnergy());
		// Cannot recharge more than receiver's amount of free storage available
		amount = Math.min(amount, receivingCarrier.getMaximumEnergy() - receivingCarrier.getEnergy());

		return amount;
	}

	/*
	 * Using
	 */

	/**
	 * Use this battery on a robot, transferring energy from
	 * this battery to the robot.
	 * 
	 * @effect	The energy stored in this battery is transferred
	 * 			to the robot.
	 * 			| transfer(robot, getEnergy())
	 */
	@Override
	public void use(Robot robot) {
		transfer(robot, getEnergy());
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += String.format(" with %.2f Ws energy", getEnergy());
		return result;
	}
}
