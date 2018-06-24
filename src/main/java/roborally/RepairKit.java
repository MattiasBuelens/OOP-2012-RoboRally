package roborally;

import be.kuleuven.cs.som.annotate.*;
import roborally.EnergyAmount.Unit;

/**
 * A repair kit in a game of RoboRally.
 * 
 * <p>A repair kit is an item that can be used by a robot
 * as a capacity source.</p>
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
public class RepairKit extends Item implements CapacityCarrier {

	/**
	 * Create a new repair kit with the given capacity and weight.
	 * 
	 * @param weight
	 * 			The weight in grams for this new repair kit.
	 * @param capacityAmount
	 * 			The capacity amount for this new repair kit.
	 * 
	 * @pre		The given amount of capacity must be valid
	 * 			for the new maximum capacity.
	 * 			| isValidCapacity(energyAmount, new.getMaximumCapacity())
	 * 
	 * @post	The maximum amount of energy in Watt-seconds
	 * 			of the new repair kit is <code>Double.MAX_VALUE</code>.
	 * 			| new.getMaximumCapacity().getAmount(Unit.WATTSECOND) == Double.MAX_VALUE
	 * @post	The absorption energy of the new repair kit equals 500 Ws.
	 * 			| new.getAbsorptionEnergy().getAmount(Unit.WATTSECOND) == 500.0
	 * @effect	The new repair kit is initialized as a new item
	 * 			with the given weight.
	 * 			| super(weight)
	 * @effect	The new repair kit's capacity amount is set
	 * 			to the given amount of capacity.
	 * 			| setCapacity(capacityAmount, new.getMaximumCapacity())
	 */
	public RepairKit(int weight, EnergyAmount capacityAmount) {
		super(weight);
		this.capacity = new StoredCapacity(getMaximumCapacity());
		setCapacity(capacityAmount);
	}

	/**
	 * Create a new repair kit with the given capacity and weight.
	 * 
	 * @param weight
	 * 			The weight in grams for this new repair kit.
	 * @param capacity
	 * 			The amount of capacity for this new repair kit.
	 * @param capacityUnit
	 * 			The unit in which the capacity amount is expressed.
	 * 
	 * @effect	The new repair kit is initialized with a capacity amount
	 * 			described by the given amount and unit.
	 * 			| this(weight, new EnergyAmount(capacity, capacityUnit))
	 */
	public RepairKit(int weight, double capacity, Unit capacityUnit) {
		this(weight, new EnergyAmount(capacity, capacityUnit));
	}

	/**
	 * Create a new repair kit with the given capacity and weight.
	 * 
	 * @param weight
	 * 			The weight in grams for this new repair kit.
	 * @param capacity
	 * 			The amount of capacity in Watt-seconds for this new repair kit.
	 * 
	 * @effect	The new repair kit is initialized with a capacity amount
	 * 			expressed in Watt-seconds.
	 * 			| this(weight, capacity, Unit.WATTSECOND)
	 */
	public RepairKit(int weight, double capacity) {
		this(weight, capacity, Unit.WATTSECOND);
	}

	/*
	 * Capacity
	 */

	@Override
	public EnergyProperty getCapacity() {
		return capacity;
	}

	@Override
	public EnergyAmount getCapacityAmount() {
		return getCapacity().getAmount();
	}

	@Override
	public double getCapacityAmount(Unit unit) {
		return getCapacityAmount().getAmount(unit);
	}

	/**
	 * Set the amount of capacity stored by this repair kit.
	 * 
	 * @param capacity
	 * 			The new amount of capacity.
	 * 
	 * @effect	The repair kit's capacity is set for
	 * 			this repair kit's maximum capacity.
	 * 			| setCapacity(capacity, getMaximumCapacity())
	 * @see #setCapacity(EnergyAmount, EnergyAmount)
	 */
	public void setCapacity(EnergyAmount capacity) throws IllegalStateException {
		setCapacity(capacity, getMaximumCapacity());
	}

	/**
	 * Set the amount of capacity stored by this repair kit
	 * for the given maximum amount of capacity.
	 * 
	 * @param capacity
	 * 			The new amount of capacity.
	 * @param maximumCapacity
	 * 			The maximum amount of capacity.
	 * 
	 * @pre		The amount of capacity must be valid
	 * 			for the given maximum amount of capacity.
	 * 			| isValidCapacity(capacity, maximumCapacity)
	 * @post	The new amount of capacity equals the given amount.
	 * 			| new.getCapacityAmount().equals(capacity)
	 * @throws	IllegalStateException
	 * 			If the repair kit is terminated.
	 * 			| isTerminated()
	 * @see #isValidCapacity(EnergyAmount, EnergyAmount)
	 */
	@Model
	void setCapacity(EnergyAmount capacity, EnergyAmount maximumCapacity) throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Repair kit cannot be terminated.");

		assert isValidCapacity(capacity, maximumCapacity);
		getCapacity().setAmount(capacity, maximumCapacity);
	}

	/**
	 * @return	True if and only if the amount of capacity is valid
	 * 			for this repair kit's energy property.
	 * 			| result == getCapacity().isValidAmount(amount)
	 * @see #isValidCapacity(EnergyAmount, EnergyAmount)
	 */
	@Override
	public boolean isValidCapacity(EnergyAmount capacity) {
		return getCapacity().isValidAmount(capacity);
	}

	/**
	 * Check whether the given amount of capacity is valid
	 * for the given maximum amount of capacity.
	 * 
	 * @param capacity
	 * 			The amount of capacity to validate.
	 * @param maximumCapacity
	 * 			The maximum amount of capacity.
	 * 
	 * @return	True if and only if the given amount of capacity is
	 * 			a valid amount for an energy property with
	 * 			the given maximum amount of capacity.
	 * 			| result == EnergyProperty.isValidAmount(capacity, maximumCapacity)
	 */
	public static boolean isValidCapacity(EnergyAmount capacity, EnergyAmount maximumCapacity) {
		return EnergyProperty.isValidAmount(capacity, maximumCapacity);
	}

	/**
	 * Variable registering the amount of capacity stored by this repair kit.
	 */
	private final EnergyProperty capacity;

	/**
	 * Get the maximum amount of capacity that can be stored
	 * by this repair kit.
	 */
	@Basic
	public EnergyAmount getMaximumCapacity() {
		return maximumCapacity;
	}

	/**
	 * Check whether the given maximum capacity amount is valid
	 * for a repair kit.
	 * 
	 * @return	True if the given maximum amount is a valid
	 * 			maximum amount for an energy property.
	 * 			| result == EnergyProperty.isValidMaximumAmount(maximumCapacity)
	 */
	public static boolean isValidMaximumCapacity(EnergyAmount maximumCapacity) {
		return EnergyProperty.isValidMaximumAmount(maximumCapacity);
	}

	/**
	 * Variable registering the maximum amount of capacity
	 * that can be stored by a repair kit.
	 */
	private static final EnergyAmount maximumCapacity = new EnergyAmount(Double.MAX_VALUE, Unit.WATTSECOND);

	/**
	 * @pre		The given amount can be transferred from this
	 * 			repair kit to the receiving capacity carrier.
	 * 			| canTransfer(receiver, amount)
	 *
	 * @effect	The given amount is transferred from this
	 * 			repair kit's capacity property to the
	 * 			receiving carrier's capacity property.
	 * 			| getCapacity().transfer(receiver.getCapacity(), amount)
	 */
	@Override
	public void transfer(CapacityCarrier receiver, EnergyAmount amount) throws UnsupportedOperationException {
		assert canTransfer(receiver, amount);
		getCapacity().transfer(receiver.getCapacity(), amount);
	}

	/**
	 * @return	False if this repair kit is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	False if the receiving carrier is not effective.
	 * 			| else if (receiver == null)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the given amount
	 * 			can be transferred from this repair kit's capacity property
	 * 			to the receiving carrier's capacity property.
	 * 			| else
	 * 			|   result == getCapacity().canTransfer(receiver.getCapacity(), amount)
	 */
	@Override
	public boolean canTransfer(CapacityCarrier receiver, EnergyAmount amount) {
		if (isTerminated())
			return false;
		if (receiver == null)
			return false;
		return getCapacity().canTransfer(receiver.getCapacity(), amount);
	}

	/*
	 * Using
	 */

	/**
	 * Use this repair kit on a given robot, transferring capacity
	 * from this repair kit to the robot.
	 * 
	 * @effect	The robot is recharged with the capacity of
	 * 			this repair kit times the recharge gain,
	 * 			or the robot reaches its maximum capacity.
	 * 			| let
	 * 			|   maxRechargeFromKit = getCapacityAmount().multiply(getRechargeGain())
	 * 			|   maxRechargeOnRobot = robot.getCapacity().getFreeAmount()
	 * 			|   rechargeAmount = EnergyAmount.min(maxRechargeFromKit, maxRechargeOnRobot)
	 * 			| robot.getCapacity().recharge(rechargeAmount)
	 * 
	 * @effect	This repair kit's capacity is drained with
	 * 			the amount that was recharged by the robot,
	 * 			divided by this repair kit's recharge gain.
	 * 			| let
	 * 			|   drainAmount = rechargeAmount.multiply(1.0 / getRechargeGain())
	 * 			| this.getCapacity().drain(drainAmount)
	 */
	@Override
	public void use(Robot robot) throws IllegalStateException, IllegalArgumentException {
		if (isTerminated())
			throw new IllegalStateException("Cannot use terminated items.");
		if (robot == null)
			throw new IllegalArgumentException("Robot must be effective.");
		if (!robot.hasAsPossession(this))
			throw new IllegalArgumentException("Robot must have this item as one of its possessions.");

		// The recharge amount is the minimum of
		// the capacity of this repair kit times the recharge gain
		// and the free capacity of the robot
		EnergyAmount maxRechargeFromKit = getCapacityAmount().multiply(getRechargeGain());
		EnergyAmount maxRechargeOnRobot = robot.getCapacity().getFreeAmount();
		EnergyAmount rechargeAmount = EnergyAmount.min(maxRechargeFromKit, maxRechargeOnRobot);

		// The drain amount is the recharge amount
		// times the inverse of the recharge gain
		EnergyAmount drainAmount = rechargeAmount.multiply(1.0 / getRechargeGain());

		// Transfer amounts
		this.getCapacity().drain(drainAmount);
		robot.getCapacity().recharge(rechargeAmount);
	}

	/*
	 * Shooting
	 */

	/**
	 * @effect	The absorption capacity amount is recharged by this repair kit,
	 * 			or the repair kit fills up to its maximum capacity.
	 * 			| let
	 * 			|   rechargeAmount = EnergyAmount.min(
	 * 			|      getAbsorptionCapacity(),
	 * 			|      getCapacity().getFreeAmount()
	 * 			| )
	 * 			| getCapacity().recharge(rechargeAmount)
	 */
	@Override
	public void hit() throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Repair kit must not be terminated.");

		EnergyAmount rechargeAmount = EnergyAmount.min(getAbsorptionCapacity(), getCapacity().getFreeAmount());
		getCapacity().recharge(rechargeAmount);
	}

	/**
	 * Get the capacity amount that can be absorped
	 * when this repair kit is hit by a laser or explosion.
	 */
	@Basic
	@Immutable
	public EnergyAmount getAbsorptionCapacity() {
		return absorptionCapacity;
	}

	/**
	 * Variable registering the capacity amount
	 * that can be absorbed when hit by a laser or explosion.
	 * 
	 * @invar	The absorption capacity is effective.
	 * 			| absorptionCapacity != null
	 */
	private static final EnergyAmount absorptionCapacity = new EnergyAmount(500.0, Unit.WATTSECOND);

	/**
	 * Get the capacity gain for a robot for each unit
	 * of capacity drained from this repair kit.
	 * 
	 * <p>That is: for every unit of capacity that
	 * is drained from this repair kit, the robot
	 * recharges this amount of capacity units.</p>
	 */
	@Basic
	@Immutable
	public double getRechargeGain() {
		return rechargeGain;
	}

	/**
	 * Variable registering the capacity gain
	 * for a robot for each unit of capacity
	 * drained from this repair kit.
	 * 
	 * @invar	The recharge gain is positive.
	 * 			| rechargeGain > 0
	 */
	private static final double rechargeGain = 0.5;

	@Override
	public String toString() {
		String result = super.toString();
		result += String.format(" with %s energy", getCapacity());
		return result;
	}

	private class StoredCapacity extends EnergyProperty {

		public StoredCapacity(EnergyAmount maximumCapacity) {
			super(maximumCapacity);
		}

		/**
		 * @return	The result is the maximum amount of capacity
		 * 			of this repair kit.
		 * 			| result.equals(RepairKit.this.getMaximumCapacity())
		 */
		@Override
		public EnergyAmount getMaximumAmount() {
			return RepairKit.this.getMaximumCapacity();
		}

	}

}
