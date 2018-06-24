package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import roborally.EnergyAmount.Unit;

/**
 * A property which represents a mutable amount of energy.
 * 
 * @invar	The energy amount is valid.
 * 			| isValidAmount(getAmount())
 * @invar	The maximum energy amount is valid.
 * 			| isValidMaximumAmount(getMaximumAmount())
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
public abstract class EnergyProperty {

	/**
	 * Create a new energy property with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy for this new energy property.
	 * 
	 * @effect	The new energy property's amount of energy is set
	 * 			to the given amount if its valid for
	 * 			the new maximum amount of energy.
	 * 			| setAmount(amount, new.getMaximumAmount())
	 */
	@Raw
	@Model
	protected EnergyProperty(EnergyAmount amount) {
		setAmount(amount);
	}

	/**
	 * Create a new energy property with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy for this new energy property.
	 * @param unit
	 * 			The unit in which the energy amount is expressed.
	 * 
	 * @effect	The new energy property is initialized with an energy amount
	 * 			described by the given amount and unit.
	 * 			| this(new EnergyAmount(amount, unit))
	 */
	@Raw
	@Model
	protected EnergyProperty(double amount, Unit unit) {
		this(new EnergyAmount(amount, unit));
	}

	/**
	 * Create a new energy property with no energy.
	 * 
	 * @effect	The new energy property is initialized with
	 * 			a zero energy amount.
	 * 			| this(EnergyAmount.ZERO)
	 */
	@Raw
	@Model
	protected EnergyProperty() {
		this(EnergyAmount.ZERO);
	}

	/**
	 * Get the amount of energy associated with this energy property.
	 */
	@Basic
	@Raw
	public EnergyAmount getAmount() {
		return amount;
	}

	/**
	 * Set the amount of energy associated with this energy property.
	 * 
	 * @param amount
	 * 			The new amount of energy.
	 * 
	 * @effect	The new amount of energy is set for this energy property's
	 * 			maximum amount of energy.
	 * 			| setAmount(amount, getMaximumAmount())
	 */
	@Raw
	public void setAmount(EnergyAmount amount) {
		setAmount(amount, getMaximumAmount());
	}

	/**
	 * Set the amount of energy associated with this energy property
	 * for the given maximum amount of energy.
	 * 
	 * @param amount
	 * 			The new amount of energy.
	 * @param maximumAmount
	 * 			The maximum amount of energy.
	 * 
	 * @pre		The given amount of energy is valid
	 * 			for the given maximum amount.
	 * 			| isValidAmount(amount, maximumAmount)  
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergy().equals(amount)
	 */
	@Raw
	@Model
	void setAmount(EnergyAmount amount, EnergyAmount maximumAmount) {
		assert isValidAmount(amount, maximumAmount);
		this.amount = amount;
	}

	/**
	 * Check whether the given amount of energy is valid for this energy property.
	 * 
	 * @param amount
	 * 			The amount of energy to validate.
	 * 
	 * @return	The given amount of energy is validated
	 * 			for this energy property's maximum amount of energy.
	 * 			| result == EnergyProperty.isValidAmount(amount, getMaximumAmount())
	 */
	public boolean isValidAmount(EnergyAmount amount) {
		return isValidAmount(amount, getMaximumAmount());
	}

	/**
	 * Check whether the given amount of energy is valid for
	 * an energy property with the given maximum amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to validate.
	 * @param maximumAmount
	 * 			The maximum amount of energy.
	 * 
	 * @return	False if the given amount is not effective.
	 * 			| if (amount == null)
	 * 			|   result == false
	 * @return	False if the given amount is negative.
	 * 			| else if (amount.isLessThan(EnergyAmount.ZERO)
	 * 			|   result == false
	 * @return	True if and only if the given amount is less than
	 * 			or equal to the given maximum amount.
	 * 			| else
	 * 			|   result == amount.isLessThanOrEqual(maximumAmount)
	 */
	public static boolean isValidAmount(EnergyAmount amount, EnergyAmount maximumAmount) {
		if (amount == null)
			return false;
		if (amount.isLessThan(EnergyAmount.ZERO))
			return false;
		return amount.isLessThanOrEqual(maximumAmount);
	}

	/**
	 * Variable registering the amount of energy of this energy property.
	 * 
	 * @invar	The energy amount is effective.
	 * 			| amount != null
	 * @invar	The energy amount is non-negative.
	 * 			| amount.isGreaterThanOrEqual(EnergyAmount.ZERO)
	 */
	private EnergyAmount amount;

	/**
	 * Get the maximum amount of energy associated with this energy property.
	 */
	@Basic
	@Raw
	public abstract EnergyAmount getMaximumAmount();

	/**
	 * Check whether the given maximum amount of energy is valid
	 * for an energy property.
	 * 
	 * @param maximumAmount
	 * 			The maximum amount of energy to validate.
	 * 
	 * @return	False if the given maximum amount is not effective.
	 * 			| if (maximumAmount == null)
	 * 			|   result == false
	 * @return	True if and only if the given maximum amount is positive.
	 * 			| else
	 * 			|   result == maximumAmount.isGreaterThan(EnergyAmount.ZERO)
	 */
	public static boolean isValidMaximumAmount(EnergyAmount maximumAmount) {
		if (maximumAmount == null)
			return false;
		return maximumAmount.isGreaterThan(EnergyAmount.ZERO);
	}

	/**
	 * Get the maximum amount of energy that
	 * can be recharged by this robot.
	 * 
	 * @return	The maximum recharge amount is the maximum amount
	 * 			of energy minus the current amount of energy.
	 * 			| result.equals(getMaximumAmount().subtract(getAmount()))
	 */
	public EnergyAmount getFreeAmount() {
		return getMaximumAmount().subtract(getAmount());
	}

	/**
	 * Recharge this energy property with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @pre		This energy property can be recharged with the given amount.
	 * 			| canRecharge(amount)
	 * @effect	This energy property's energy amount is increased
	 * 			with the given amount.
	 * 			| setAmount(getEnergy().add(amount))
	 * @see #setAmount(EnergyAmount)
	 */
	public void recharge(EnergyAmount amount) {
		assert canRecharge(amount);
		setAmount(getAmount().add(amount));
	}

	/**
	 * Check whether this energy property can be recharged
	 * with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @return	False if the given extra amount is not effective.
	 * 			| if (amount == null)
	 * 			|   result == false
	 * @return	False if the given extra amount is negative.
	 * 			| else if (amount.isLessThan(EnergyAmount.ZERO))
	 * 			|   result == false
	 * @return	False if the new amount of energy would be invalid.
	 * 			| else if (!isValidEnergy(getAmount().add(amount)))
	 * 			|   result == false
	 */
	public boolean canRecharge(EnergyAmount amount) {
		if (amount == null)
			return false;
		if (amount.isLessThan(EnergyAmount.ZERO))
			return false;
		if (!isValidAmount(getAmount().add(amount)))
			return false;
		return true;
	}

	/**
	 * Drain this energy property with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to drain.
	 * 
	 * @pre		This energy property can be drained with the given amount.
	 * 			| canDrain(amount)
	 * @effect	This energy property's energy amount is decreased
	 * 			with the given amount.
	 * 			| setAmount(getEnergy().subtract(amount))
	 * @see #setAmount(EnergyAmount)
	 */
	public void drain(EnergyAmount amount) {
		assert canDrain(amount);
		setAmount(getAmount().subtract(amount));
	}

	/**
	 * Check whether this energy property can be drained
	 * with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to drain.
	 * 
	 * @return	False if the given amount is not effective.
	 * 			| if (amount == null)
	 * 			|   result == false
	 * @return	False if the given amount is negative.
	 * 			| else if (amount.isLessThan(EnergyAmount.ZERO))
	 * 			|   result == false
	 * @return	False if the new amount of energy would be invalid.
	 * 			| else if (!isValidEnergy(getAmount().subtract(amount)))
	 * 			|   result == false
	 */
	public boolean canDrain(EnergyAmount amount) {
		if (amount == null)
			return false;
		if (amount.isLessThan(EnergyAmount.ZERO))
			return false;
		if (!isValidAmount(getAmount().subtract(amount)))
			return false;
		return true;
	}

	/**
	 * Transfer the given amount of energy from this energy property
	 * to the given energy property.
	 * 
	 * @param receiver
	 * 			The receiving energy property.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @pre		The given amount can be transferred from this
	 * 			energy property to the receiving energy property.
	 * 			| canTransfer(receiver, amount)
	 * 
	 * @effect	The actual transfer amount is drained from this energy property.
	 * 			| let
	 * 			|	actualAmount = getTransferAmount(receiver, amount)
	 * 			| this.drain(actualAmount)
	 * @effect	The actual transfer amount is recharged by the receiving energy property.
	 * 			| receiver.recharge(actualAmount)
	 */
	protected void transfer(EnergyProperty receiver, EnergyAmount amount) {
		assert canTransfer(receiver, amount);

		amount = getTransferAmount(receiver, amount);

		assert this.canDrain(amount);
		assert receiver.canRecharge(amount);

		this.drain(amount);
		receiver.recharge(amount);
	}

	/**
	 * Get the amount of energy which can actually be transferred
	 * from this energy property to the given energy property.
	 * 
	 * @param receiver
	 * 			The receiving energy property.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @pre		The given amount can be transferred
	 * 			from this property to the receiving property.
	 * 			| canTransfer(receiver, amount)
	 * 
	 * @return	The actual transfer amount is the minimum of the given amount,
	 * 			this property's current energy amount and the receiving property's
	 * 			current amount of free storage.
	 * 			| let
	 * 			|	maxDrainAmount = this.getEnergyAmount()
	 * 			|	maxRechargeAmount = receiver.getFreeAmount()
	 * 			|
	 * 			| result.getAmount(Unit.WATTSECOND)
	 * 			|   == min({amount.getAmount(Unit.WATTSECOND),
	 * 			|           maxDrainAmount.getAmount(Unit.WATTSECOND),
	 * 			|           maxRechargeAmount.getAmount(Unit.WATTSECOND)})
	 */
	protected EnergyAmount getTransferAmount(EnergyProperty receiver, EnergyAmount amount) {
		assert canTransfer(receiver, amount);

		// Cannot drain more than this amount of energy stored
		amount = EnergyAmount.min(amount, this.getAmount());
		// Cannot recharge more than receiver's amount of free storage available
		EnergyAmount receiverFreeAmount = receiver.getFreeAmount();
		amount = EnergyAmount.min(amount, receiverFreeAmount);

		return amount;
	}

	/**
	 * Check whether given amount of energy can be transferred
	 * from this energy property to the given energy property.
	 * 
	 * @param receiver
	 * 			The receiving energy property.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @return	False if the receiving property is not effective.
	 * 			| if (receiver == null)
	 * 			|   result == false
	 * @return	False if the given amount is not effective.
	 * 			| else if (amount == null)
	 * 			|   result == false
	 * @return	False if the given amount is negative.
	 * 			| else if (amount.isLessThan(EnergyAmount.ZERO))
	 * 			|   result == false
	 */
	protected boolean canTransfer(EnergyProperty receiver, EnergyAmount amount) {
		if (receiver == null)
			return false;
		if (amount == null)
			return false;
		if (amount.isLessThan(EnergyAmount.ZERO))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return getAmount().toString();
	}

}
