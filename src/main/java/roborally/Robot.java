package roborally;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import roborally.Piece;
import roborally.EnergyAmount.Unit;
import roborally.path.MinimalCostAStar;
import roborally.path.ReachAStar;
import roborally.path.ReachNode;
import roborally.path.RobotNode;
import roborally.program.Program;
import roborally.program.command.Command;
import roborally.util.Function;
import roborally.util.SortedList;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Represents a robot in a game of RoboRally.
 * 
 * <p>A robot is a player-controlled piece.</p>
 * 
 * @invar	The robot's orientation is valid.
 * 			| isValidOrientation(getOrientation())
 * @invar	The robot's maximum amount of capacity is valid.
 * 			| isValidMaximumCapacity(getMaximumCapacity())
 * @invar	The robot has a proper set of possessions.
 * 			| hasProperPossessions()
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
public class Robot extends Piece implements EnergyCarrier, CapacityCarrier {

	/**
	 * Create a new robot with the given orientation and energy.
	 * 
	 * @param orientation
	 * 			The orientation for this new robot.
	 * @param energyAmount
	 * 			The energy for this new robot.
	 * 
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(energyAmount, new.getCapacityAmount())
	 * 
	 * @post	The base step cost of the new robot equals 500 Ws.
	 * 			| new.getBaseStepCost().getAmount(Unit.WATTSECOND) == 500.0
	 * @post	The extra step cost per kilogram of possessions
	 * 			of the new robot equals 50 Ws.
	 * 			| new.getStepCostPerKilogram().getAmount(Unit.WATTSECOND) == 50.0
	 * @post	The turn cost of the new robot equals 100 Ws.
	 * 			| new.getTurnCost().getAmount(Unit.WATTSECOND) == 100.0
	 * @post	The shoot cost of the new robot equals 1000 Ws.
	 * 			| new.getShootCost().getAmount(Unit.WATTSECOND) == 1000.0
	 * @post	The capacity of the new robot equals 20000 Ws.
	 * 			| new.getCapacity().getAmount(Unit.WATTSECOND) == 20000.0
	 * @post	The maximum capacity of the new robot equals 20000 Ws.
	 * 			| new.getMaximumCapacity().getAmount(Unit.WATTSECOND) == 20000.0
	 * @post	The new robot does not have any possessions yet.
	 * 			| new.getNbPossessions() == 0
	 * 
	 * @effect	The new robot is initialized as a new piece.
	 * 			| super()
	 * @effect	The new robot's orientation is set to the given orientation.
	 * 			| setOrientation(orientation)
	 * @effect	The new robot's energy is set to the given amount of energy.
	 * 			| setEnergy(energyAmount, new.getCapacityAmount())
	 */
	public Robot(Orientation orientation, EnergyAmount energyAmount) {
		// Energy depends on capacity, initialize capacity first
		this.capacity = new StoredCapacity(getMaximumCapacity());
		this.energy = new StoredEnergy();

		setOrientation(orientation);
		setEnergy(energyAmount);
	}

	/**
	 * Create a new robot with the given orientation and energy.
	 * 
	 * @param orientation
	 * 			The orientation for this new robot.
	 * @param energy
	 * 			The amount of energy for this new robot.
	 * @param energyUnit
	 * 			The unit in which the energy amount is expressed.
	 * 
	 * @effect	The new robot is initialized with an energy amount
	 * 			described by the given amount and unit.
	 * 			| this(orientation, new EnergyAmount(energy, energyUnit))
	 */
	public Robot(Orientation orientation, double energy, Unit energyUnit) {
		this(orientation, new EnergyAmount(energy, energyUnit));
	}

	/**
	 * Create a new robot with the given orientation and energy.
	 * 
	 * @param orientation
	 * 			The orientation for this new robot.
	 * @param energy
	 * 			The amount of energy in Watt-seconds for this new robot.
	 * 
	 * @effect	The new robot is initialized with an energy amount
	 * 			expressed in Watt-seconds.
	 * 			| this(orientation, energy, Unit.WATTSECOND)
	 */
	public Robot(Orientation orientation, double energy) {
		this(orientation, energy, Unit.WATTSECOND);
	}

	/*
	 * Position
	 */

	/**
	 * @return	False if the other piece is not effective.
	 * 			| if (piece == null)
	 * 			|   result == false
	 * @return	If the other piece is a robot, true if and only if
	 * 			the other piece equals this robot.
	 * 			| else if (piece instanceof Robot)
	 * 			|   result == (piece == this)
	 * @return	True if the other piece is an item.
	 * 			| else if (piece instanceof Item)
	 * 			|   result == true
	 * @return	Otherwise, the call is dispatched to the other piece.
	 * 			| else
	 * 			|   result == piece.canSharePositionWith(this)
	 */
	@Override
	public boolean canSharePositionWith(Piece piece) {
		if (piece == null)
			return false;

		// Different robots cannot share positions
		if (piece instanceof Robot)
			return (piece == this);

		// Robots can share their position with items
		if (piece instanceof Item)
			return true;

		return piece.canSharePositionWith(this);
	}

	/*
	 * Orientation
	 */

	/**
	 * Get the orientation this robot is facing.
	 */
	@Basic
	public Orientation getOrientation() {
		return orientation;
	}

	/**
	 * Set the orientation this robot is facing.
	 * 
	 * @param orientation
	 * 			The new orientation.
	 * 
	 * @post	If the given orientation is valid,
	 * 			the new orientation equals the given orientation.
	 * 			If the given orientation is invalid,
	 * 			the new orientation is set to facing up.
	 * 			| if (isValidOrientation(orientation))
	 * 			|   new.getOrientation() == orientation
	 * 			| else
	 * 			|   new.getOrientation() == Orientation.UP
	 */
	public void setOrientation(Orientation orientation) {
		if (!isValidOrientation(orientation))
			orientation = Orientation.UP;
		this.orientation = orientation;
	}

	/**
	 * Check whether the given orientation is valid for this robot.
	 *
	 * @param orientation
	 *			The orientation to validate.
	 *
	 * @return	True if the orientation is effective.
	 *			| result == (orientation != null)
	 */
	public boolean isValidOrientation(Orientation orientation) {
		return orientation != null;
	}

	/**
	 * Variable registering the orientation this robot is facing.
	 */
	private Orientation orientation;

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
	 * Set the amount of energy stored by this robot.
	 * 
	 * @param amount
	 * 			The new amount of energy.
	 * 
	 * @effect	The robot's energy is set for this robot's capacity.
	 * 			| setEnergy(amount, getCapacity().getAmount())
	 * @see #setEnergy(EnergyAmount, EnergyAmount)
	 */
	public void setEnergy(EnergyAmount amount) throws IllegalStateException {
		setEnergy(amount, getCapacity().getAmount());
	}

	/**
	 * Set the amount of energy stored by this robot
	 * for the given maximum amount of energy.
	 * 
	 * @param amount
	 * 			The new amount of energy.
	 * @param maximumAmount
	 * 			The maximum amount of energy.
	 * 
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(amount, maximumAmount)
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergyAmount().equals(energy)
	 * @throws	IllegalStateException
	 * 			If the robot is terminated.
	 * 			| isTerminated()
	 * @see #isValidEnergy(EnergyAmount, EnergyAmount)
	 */
	@Model
	void setEnergy(EnergyAmount amount, EnergyAmount maximumAmount) throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Robot cannot be terminated.");

		assert isValidEnergy(amount, maximumAmount);
		getEnergy().setAmount(amount, maximumAmount);
	}

	/**
	 * @return	True if and only if the amount of energy is valid
	 * 			for this robot's energy property.
	 * 			| result == getEnergy().isValidAmount(amount)
	 * @see #isValidEnergy(EnergyAmount, EnergyAmount)
	 */
	@Override
	public boolean isValidEnergy(EnergyAmount amount) {
		return isValidEnergy(amount, getCapacity().getAmount());
	}

	/**
	 * Check whether the given amount of energy is a valid amount
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
	 * @see EnergyProperty#isValidAmount(EnergyAmount, EnergyAmount)
	 */
	public static boolean isValidEnergy(EnergyAmount amount, EnergyAmount maximumAmount) {
		return EnergyProperty.isValidAmount(amount, maximumAmount);
	}

	/**
	 * Variable registering the amount of energy stored by this robot.
	 */
	private final EnergyProperty energy;

	/*
	 * Capacity
	 */

	@Basic
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
	 * Variable registering the amount of capacity stored by this robot.
	 */
	private final EnergyProperty capacity;

	/**
	 * Check whether the given maximum energy amount is valid for this robot.
	 * 
	 * @return	True if the given maximum amount is positive.
	 * 			| result == (maximumEnergy > 0)
	 */
	@Override
	public boolean isValidCapacity(EnergyAmount capacity) {
		return EnergyProperty.isValidMaximumAmount(capacity) && getCapacity().isValidAmount(capacity);
	}

	/**
	 * Get the maximum amount of capacity that can be stored
	 * by this robot.
	 */
	@Basic
	public EnergyAmount getMaximumCapacity() {
		return maximumCapacity;
	}

	/**
	 * Check whether the given maximum capacity amount is valid
	 * for a robot.
	 * 
	 * @return	True if the given maximum amount is a valid
	 * 			maximum amount for an energy property.
	 * 			| result == EnergyProperty.isValidMaximumAmount(maximumEnergy)
	 */
	public static boolean isValidMaximumCapacity(EnergyAmount maximumCapacity) {
		return EnergyProperty.isValidMaximumAmount(maximumCapacity);
	}

	/**
	 * Variable registering the maximum energy capacity
	 * that can be stored by a robot.
	 */
	private static final EnergyAmount maximumCapacity = new EnergyAmount(20000, Unit.WATTSECOND);

	/*
	 * Energy operations
	 */

	/**
	 * Get the amount of energy of this robot as a fraction of the capacity.
	 * 
	 * @return	The result is the amount of energy divided by the capacity of this robot.
	 * 			This double division is precise to at least two decimals.
	 * 			| result == getEnergyAmount().asFractionOf(getCapacityAmount())
	 * @see #getEnergy()
	 * @see #getCapacity()
	 */
	public double getEnergyFraction() {
		assert isValidEnergy(getEnergy().getAmount());
		assert isValidCapacity(getCapacity().getAmount());
		return getEnergyAmount().asFractionOf(getCapacityAmount());
	}

	/**
	 * Recharge this robot with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @pre		This robot can be recharged with the given amount.
	 * 			| canRecharge(amount)
	 * 
	 * @effect	This robot's energy property is recharged with the given amount.
	 * 			| getEnergy().recharge(amount)
	 */
	public void recharge(EnergyAmount amount) {
		assert canRecharge(amount);
		getEnergy().recharge(amount);
	}

	/**
	 * Check whether this robot can be recharged
	 * with the given amount of energy.
	 * 
	 * @return	False if this robot is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this robot's
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
	 * Drain this robot with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to drain.
	 * 
	 * @pre		This robot can be drained with the given amount.
	 * 			| canDrain(amount)
	 * 
	 * @effect	This robot's energy property is drained with the given amount.
	 * 			| getEnergy().drain(amount)
	 */
	public void drain(EnergyAmount amount) {
		assert canDrain(amount);
		getEnergy().drain(amount);
	}

	/**
	 * Check whether this robot can be drained
	 * with the given amount of energy.
	 * 
	 * @return	False if this robot is terminated.
	 * 			| if (isTerminated())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this robot's
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
	 * Transfer the given amount of energy from this robot
	 * to the given receiving energy carrier.
	 * 
	 * <p>This operation is illegal for robots and will always
	 * throw an exception.
	 * 
	 * @throws	UnsupportedOperationException
	 * 			Always, since transferring energy from a robot
	 * 			is an illegal operation.
	 * 			| true
	 */
	@Override
	public void transfer(EnergyCarrier receiver, EnergyAmount amount) throws UnsupportedOperationException {
		assert canTransfer(receiver, amount);
		throw new UnsupportedOperationException("Robots cannot transfer energy to other energy carriers.");
	}

	/**
	 * @return	Always false, since transferring energy from a robot
	 * 			is an illegal operation.
	 * 			| result == false
	 */
	@Override
	public boolean canTransfer(EnergyCarrier receiver, EnergyAmount amount) {
		return false;
	}

	/**
	 * Transfer the given amount of capacity from this robot
	 * to the given receiving capacity carrier.
	 * 
	 * <p>This operation is illegal for robots and will always
	 * throw an exception.
	 * 
	 * @throws	UnsupportedOperationException
	 * 			Always, since transferring capacity from a robot
	 * 			is an illegal operation.
	 * 			| true
	 */
	@Override
	public void transfer(CapacityCarrier receiver, EnergyAmount amount) throws UnsupportedOperationException {
		assert canTransfer(receiver, amount);
		throw new UnsupportedOperationException("Robots cannot transfer capacity to other capacity carriers.");
	}

	/**
	 * @return	Always false, since transferring capacity from a robot
	 * 			is an illegal operation.
	 * 			| result == false
	 */
	@Override
	public boolean canTransfer(CapacityCarrier receiver, EnergyAmount amount) {
		return false;
	}

	/*
	 * Possessions
	 */

	/**
	 * Get the number of possessions of this robot.
	 */
	@Basic
	@Raw
	public int getNbPossessions() {
		return possessions.size();
	}

	/**
	 * Get the <i>index</i>-th heaviest possession of this robot.
	 * That is, get the possession which has a weight greater or
	 * equal to the weight of all but {@code (index - 1)} possessions.
	 * 
	 * @param index
	 * 			The index of the possession to return.
	 * 
	 * @throws	IndexOutOfBoundsException
	 * 			If the given index is not positive or
	 * 			it exceeds the number of possessions of this robot.
	 * 			| (index < 1) || (index > getNbPossessions())
	 */
	@Basic
	@Raw
	public Item getPossessionAt(int index) throws IndexOutOfBoundsException {
		return possessions.get(index - 1);
	}

	/**
	 * Check whether this robot can have the given
	 * item as one of its possessions.
	 * 
	 * @param item
	 * 			The item to check.
	 * 
	 * @return	True if and only if this robot is not
	 * 			terminated and the given item is effective.
	 * 			| result == (!isTerminated() && item != null)
	 */
	@Raw
	public boolean canHaveAsPossession(Item item) {
		return !isTerminated() && item != null;
	}

	/**
	 * Check whether this robot can have the given item
	 * as one of its possessions at the given index.
	 * 
	 * @param item
	 * 			The item to check.
	 * @param index
	 * 			The index of the item.
	 * 
	 * @return	False if the given index is not positive or exceeds
	 * 			the number of possessions of this robot.
	 * 			| if ( (index < 1) || (index > getNbPossessions()) )
	 * 			|   result == false
	 * @return	False if this robot cannot have the given item
	 * 			as one of its possessions.
	 * 			| else if (!canHaveAsPossession(item))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the given item is
	 *          not already registered at another index, and
	 *          all possessions with lower indexes have a weight
	 *          greater than or equal to the weight of the given item,
	 *          and all possessions with greater indexes have a weight
	 *          less than or equal to the weight of the given item.
	 * 			| else result ==
	 * 			|   for each pos in 1..getNbPossessions() :
	 * 			|     ( (pos == index) || (getPossessionAt(pos) != item) )
	 * 			|     && ( (pos >= index) || (getPossessionAt(pos).getWeight() >= item.getWeight()) )
	 * 			|     && ( (pos <= index) || (getPossessionAt(pos).getWeight() <= item.getWeight()) )
	 */
	@Raw
	public boolean canHaveAsPossessionAt(Item item, int index) {
		if ((index < 1) || (index > getNbPossessions()))
			return false;
		if (!canHaveAsPossession(item))
			return false;
		for (int pos = 1; pos <= getNbPossessions(); pos++) {
			Item current = getPossessionAt(pos);
			if ((pos != index) && (current == item))
				return false;
			if ((pos < index) && (current.getWeight() < item.getWeight()))
				return false;
			if ((pos > index) && (current.getWeight() > item.getWeight()))
				return false;
		}
		return true;
	}

	/**
	 * Check whether this robot has a proper set of possessions.
	 * 
	 * @return	True if and only if this robot can have each of
	 * 			its possessions at its current index and if
	 * 			none of its possessions are placed on any board.
	 * 			| for each index in 1..getNbPossessions() :
	 * 			|   canHaveAsPossessionAt(getPossessionAt(index), index)
	 * 			|    && !getPossessionAt(index).isPlaced()
	 */
	public boolean hasProperPossessions() {
		for (int index = 1; index <= getNbPossessions(); index++) {
			if (!canHaveAsPossessionAt(getPossessionAt(index), index))
				return false;
			if (getPossessionAt(index).isPlaced())
				return false;
		}
		return true;
	}

	/**
	 * Sorted list of items possessed by this robot,
	 * sorted on their weight in descending order.
	 * 
	 * @invar	The list of possessions is effective.
	 * 			| possessions != null
	 * @invar	This robot can have each possession in the list
	 * 			and no possession in the list is placed on any board.
	 * 			| for each possession in possessions :
	 * 			|   canHaveAsPossession(possession)
	 * 			|    && !possession.isPlaced()
	 */
	private final List<Item> possessions = new SortedList<Item>(
			Collections.reverseOrder(new ItemWeightComparator<Item>()));

	/**
	 * Get a set of all the possessions of this robot.
	 * 
	 * @return	The size of the resulting set is equal to
	 * 			the number of possessions of this robot.
	 * 			| result.size() == getNbPossessions()
	 * @return	The resulting set contains all possessions of this robot.
	 * 			| for each index in 1..getNbPossessions() :
	 * 			|   result.contains(getPossessionAt(index))
	 */
	public Set<Item> getPossessions() {
		return new HashSet<Item>(possessions);
	}

	/**
	 * Get a set of all the possessions of this robot
	 * which are instances of a given type.
	 * 
	 * @param possessionType
	 * 			The type of possessions to return.
	 * 
	 * @return	For each possession of this robot, if and only if it is
	 * 			an instance of the given type, it is contained in the resulting set.
	 * 			| for each index in 1..getNbPossessions() :
	 * 			|   result.contains(getPossessionAt(index))
	 * 			|     == possessionType.isInstance(getPossessionAt(index))
	 */
	public <I extends Item> Set<I> getPossessions(Class<I> possessionType) {
		Set<I> typedPieces = new HashSet<I>();
		for (Item item : possessions) {
			if (possessionType.isInstance(item)) {
				typedPieces.add(possessionType.cast(item));
			}
		}
		return typedPieces;
	}

	/**
	 * Check whether this robot has the given item as one
	 * of its possessions.
	 * 
	 * @param item
	 * 			The item to check.
	 * 
	 * @return	True if and only if this robot has the given item
	 * 			as one of its possessions at some index.
	 * 			| result ==
	 * 			|   for some index in 1..getNbPossessions() :
	 * 			|      getPossessionAt(index).equals(item)
	 */
	@Raw
	public boolean hasAsPossession(Item item) {
		return possessions.contains(item);
	}

	/**
	 * Get the total value of all possessions of this
	 * robot using the given extractor.
	 *
	 * @param	extractor
	 * 			The extractor to be used in evaluating
	 * 			all possessions of this robot.
	 *
	 * @return 	The total amount returned by applying the given
	 * 			extractor to all possessions of this robot.
	 * 			| result ==
	 * 			|   sum({ possession in getPossessions() : extractor.apply(possession)})
	 * 
	 * @throws 	IllegalStateException
	 * 			If this robot is already terminated.
	 * 			| isTerminated()
	 * @throws  IllegalArgumentException
	 *          If the given extractor is not effective.
	 * 			| extractor == null
	 */
	public BigDecimal getTotalFromPossessions(Function<? super Item, ? extends BigDecimal> extractor)
			throws IllegalStateException, IllegalArgumentException {
		if (isTerminated())
			throw new IllegalStateException("Robot must not be terminated.");
		if (extractor == null)
			throw new IllegalArgumentException("Extractor must be effective.");

		BigDecimal total = BigDecimal.ZERO;
		for (Item item : possessions) {
			total = total.add(extractor.apply(item));
		}
		return total;
	}

	/**
	 * Get the total weight of all possessions of this robot.
	 * 
	 * @return	The sum of the weight weights of all possessions
	 * 			of this robot.
	 * 			| result ==
	 * 			|   sum({ possession in getPossessions() : possession.getWeight()})
	 *
	 * @throws 	IllegalStateException
	 * 			If this robot is already terminated.
	 * 			| isTerminated()
	 */
	public BigInteger getPossessionsWeight() throws IllegalStateException {
		BigDecimal weight = getTotalFromPossessions(new Function<Item, BigDecimal>() {
			@Override
			public BigDecimal apply(Item item) {
				return BigDecimal.valueOf(item.getWeight());
			}
		});
		return weight.toBigInteger();
	}

	/**
	 * Pick up a given item and add it as a possession.
	 * 
	 * @param item
	 * 			The item to pick up.
	 * 
	 * @effect	The item is removed from the board.
	 * 			| item.removeFromBoard()
	 * @effect	The item is added as one of this robot's possessions.
	 * 			| addAsPossession(item)
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot is not placed on a board.
	 * 			| !isPlaced()
	 * @throws	IllegalStateException
	 * 			If this robot cannot have the given item as
	 * 			one of its possessions.
	 * 			| !canHaveAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If this robot already has the given item as
	 * 			one of its possessions.
	 * 			| hasAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If the given item is terminated.
	 * 			| item.isTerminated()
	 * @throws	IllegalArgumentException
	 * 			If this robot does not share its position
	 * 			with the given item.
	 * 			| !sharesPositionWith(item)
	 */
	public void pickUp(Item item) throws IllegalStateException, IllegalArgumentException {
		if (!isPlaced())
			throw new IllegalStateException("Cannot pick up items when this robot is not placed.");
		if (!canHaveAsPossession(item))
			throw new IllegalStateException("Robot must not be terminated and item must be effective.");
		if (hasAsPossession(item))
			throw new IllegalArgumentException("Robot already possesses this item.");
		if (item.isTerminated())
			throw new IllegalArgumentException("Cannot pick up terminated items.");
		if (!sharesPositionWith(item))
			throw new IllegalArgumentException("Robot and item must be located on the same board at the same position.");

		item.removeFromBoard();
		addAsPossession(item);
	}

	/**
	 * Drop a given item and remove it as one of this robot's possessions.
	 * 
	 * @param item
	 * 			The item to drop.
	 * 
	 * @effect	The item is placed on the same board as
	 * 			this robot at its current position.
	 * 			| item.placeOnBoard(getBoard(), getPosition())
	 * @effect	The item is removed as one of this robot's possessions.
	 * 			| removeAsPossession(item)
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot cannot have the given item as
	 * 			one of its possessions.
	 * 			| !canHaveAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If this robot does not have the given item as
	 * 			one of its possessions.
	 * 			| !hasAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If the given item is terminated.
	 * 			| item.isTerminated()
	 */
	public void drop(Item item) throws IllegalStateException, IllegalArgumentException {
		if (!canHaveAsPossession(item))
			throw new IllegalStateException("Robot must not be terminated and item must be effective.");
		if (!hasAsPossession(item))
			throw new IllegalArgumentException("Robot does not possess this item.");
		if (item.isTerminated())
			throw new IllegalArgumentException("Cannot drop terminated items.");

		removeAsPossession(item);
		if (isPlaced()) {
			try {
				item.placeOnBoard(getBoard(), getPosition());
			} catch (InvalidPositionException cannotHappen) {
				// Cannot happen since this robot has a valid position
			}
		}
	}

	/**
	 * Use the given possession.
	 * 
	 * @param item
	 * 			The possession to use.
	 *
	 * @effect	If the given possession is not terminated,
	 * 			the possession is used on this robot.
	 * 			| if(!item.isTerminated())
	 * 			|   item.use(this)
	 * @effect	If the given possession is terminated,
	 * 			the possession is removed as one of
	 * 			this robot's possessions.
	 * 			| if(item.isTerminated())
	 * 			|   removeAsPossession(item)
	 *  
	 * @throws	IllegalStateException
	 * 			If this robot cannot have the given possession as
	 * 			one of its possessions.
	 * 			| !canHaveAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If this robot does not have the given possession as
	 * 			one of its possessions.
	 * 			| !hasAsPossession(item)
	 */
	public void use(Item item) throws IllegalStateException, IllegalArgumentException {
		if (!canHaveAsPossession(item))
			throw new IllegalStateException("Robot must not be terminated and item must be effective.");
		if (!hasAsPossession(item))
			throw new IllegalArgumentException("Robot does not possess this item.");

		if (item.isTerminated()) {
			removeAsPossession(item);
		} else {
			item.use(this);
		}
	}

	/**
	 * Add an item as one of this robot's possessions.
	 * 
	 * @param item
	 * 			The item to add.
	 * 
	 * @post	The number of possessions of this robot
	 * 			is incremented by one.
	 * 			| new.getNbPossessions() == getNbPossessions() + 1
	 * @post	This robot now has the given item as
	 * 			one of its possessions.
	 * 			| new.hasAsPossession(item)
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot cannot have the given item as
	 * 			one of its possessions.
	 * 			| !canHaveAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If this robot already has the given item as
	 * 			one of its possessions.
	 * 			| hasAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If the given item is placed on a board.
	 * 			| item.isPlaced()
	 */
	public void addAsPossession(Item item) throws IllegalStateException, IllegalArgumentException {
		if (!canHaveAsPossession(item))
			throw new IllegalStateException("Robot must not be terminated and item must be effective.");
		if (hasAsPossession(item))
			throw new IllegalArgumentException("Robot already possesses this item.");
		if (item.isPlaced())
			throw new IllegalArgumentException("Cannot add placed items as possessions.");

		possessions.add(item);
	}

	/**
	 * Remove an item as one of this robot's possessions.
	 * 
	 * @param item
	 * 			The item to remove.
	 * 
	 * @post	The number of possessions of this robot
	 * 			is decremented by one.
	 * 			| new.getNbPossessions() == getNbPossessions() - 1
	 * @post	This robot no longer has the given item as
	 * 			one of its possessions.
	 * 			| !new.hasAsPossession(item)
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot cannot have the given item as
	 * 			one of its possessions.
	 * 			| !canHaveAsPossession(item)
	 * @throws	IllegalArgumentException
	 * 			If this robot does not have the given item as
	 * 			one of its possessions.
	 * 			| !hasAsPossession(item)
	 */
	public void removeAsPossession(Item item) throws IllegalStateException, IllegalArgumentException {
		if (!canHaveAsPossession(item))
			throw new IllegalStateException("Robot must not be terminated and item must be effective.");
		if (!hasAsPossession(item))
			throw new IllegalArgumentException("Robot does not possess this item.");

		possessions.remove(item);
	}

	/**
	 * Transfer all possessions of this robot
	 * to the given receiving robot.
	 * 
	 * @param receivingRobot
	 * 			The receiving robot.
	 * 
	 * @post	This robot no longer has any possessions.
	 * 			| new.getNbPossessions() == 0
	 *
	 * @post	Each possession of this robot is now either
	 * 			possessed by the receiving robot, or is terminated
	 * 			if the receiving robot cannot have it as one
	 * 			of its possessions.
	 * 			| for each index in 1..this.getNbPossessions() :
	 * 			|   let
	 * 			|      possession = this.getPossessionAt(index)
	 * 			|   if (receivingRobot.canHaveAsPossession(possession))
	 * 			|      (new receivingRobot).hasAsPossessionpossession)
	 * 			|   else
	 * 			|      (new possession).isTerminated()
	 * 
	 * @post	The receiving robot still has all of its already
	 * 			possessed items, however they may be at
	 * 			different indexes.
	 * 			| for each index in 1..receivingRobot.getNbPossessions() :
	 * 			|   (new receivingRobot).hasAsPossession(receivingRobot.getPossessionAt(index))
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot is terminated or
	 * 			is not placed on any board.
	 * 			| isTerminated() || !isPlaced()
	 * @throws	IllegalArgumentException
	 * 			If the receiving robot is not effective
	 * 			or is not placed on a board.
	 * 			| receivingRobot == null || !receivingRobot.isPlaced()
	 * @throws	IllegalArgumentException
	 * 			If the receiving robot equals this robot.
	 * 			| receivingRobot == this
	 * @throws	IllegalArgumentException
	 * 			If the receiving robot is placed
	 * 			on a different board.
	 * 			| receivingRobot.getBoard() != getBoard()
	 * @throws	IllegalArgumentException
	 * 			If the receiving robot is not placed
	 * 			next to this robot.
	 * 			| receivingRobot.getPosition().manhattanDistance(getPosition()) != 1
	 */
	public void transferItems(Robot receivingRobot) throws IllegalStateException, IllegalArgumentException {
		if (isTerminated() || !isPlaced())
			throw new IllegalStateException("Robot must be placed on a board.");
		if (receivingRobot == null || !receivingRobot.isPlaced())
			throw new IllegalArgumentException("Receiving robot must be effective and placed on a board.");
		if (receivingRobot == this)
			throw new IllegalArgumentException("Receiving robot cannot equal this robot.");
		if (receivingRobot.getBoard() != getBoard())
			throw new IllegalArgumentException("Receiving robot must be on the same board as this robot.");
		if (receivingRobot.getPosition().manhattanDistance(getPosition()) != 1)
			throw new IllegalArgumentException("Robot and receiving robot must be located next to each other.");

		Iterator<Item> it = possessions.iterator();
		while (it.hasNext()) {
			Item item = it.next();
			if (receivingRobot.canHaveAsPossession(item)) {
				// Add item to receiving robot
				receivingRobot.addAsPossession(item);
			} else {
				// Cannot transfer item, discard
				item.terminate();
			}
			// Remove item from this robot
			it.remove();
		}
	}

	/*
	 * Turning and moving
	 */

	/**
	 * Move this robot one step forward in the direction it's facing.
	 * 
	 * @pre		The robot must have enough energy to move.
	 * 			| canMove()
	 * @effect	The robot moves to the next position on the board.
	 * 			| moveOnBoard(getNextPosition())
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost of one step.
	 * 			| drain(getStepCost())
	 * @see #canMove()
	 */
	public void move() throws IllegalStateException, IllegalArgumentException {
		assert canMove();

		Vector nextPosition = getNextPosition();
		moveOnBoard(nextPosition);
		drain(getStepCost());
	}

	/**
	 * Get the next position this robot would move to
	 * when moving one step forward.
	 * 
	 * <p>If the robot is not placed or the next position is invalid,
	 * null is returned.
	 * 
	 * @return	If this robot is placed, the next position is retrieved
	 * 			from this robot's board with its current position and orientation.
	 *			| if (isPlaced())
	 * 			|   result == getBoard().getNextPosition(getPosition(), getOrientation())
	 * @return	If this robot is not placed on any board, null is returned.
	 * 			| if (!isPlaced())
	 * 			|   result == null
	 */
	public Vector getNextPosition() {
		if (!isPlaced())
			return null;
		return getBoard().getNextPosition(getPosition(), getOrientation());
	}

	/**
	 * Check whether this robot has enough energy to move one step forward.
	 * 
	 * @return	True if the energy cost of one step can be drained from this robot.
	 * 			| result == canDrain(getStepCost())
	 * @see #getStepCost()
	 */
	public boolean canMove() {
		return canDrain(getStepCost());
	}

	/**
	 * Get the energy cost for one step forward.
	 * 
	 * @return	The step cost is the sum of the base step cost
	 * 			and the total weight of this robot's possessions
	 * 			in kilogram times the extra step cost per kilogram.
	 * 			| result.getAmount(Unit.WATTSECOND)
	 * 			|   == getBaseStepCost().getAmount(Unit.WATTSECOND)
	 * 			|       + ( getStepCostPerKilogram().getAmount(Unit.WATTSECOND)
	 * 			|           * (getPossessionsWeight() / 1000) )
	 * 
	 * @throws 	IllegalStateException
	 * 			If this robot is already terminated.
	 * 			| isTerminated()
	 * 
	 * @note	The total weight of possessions is rounded down
	 * 			to the nearest kilogram.
	 */
	public EnergyAmount getStepCost() throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Robot must not be terminated.");

		// Base step cost of a robot
		EnergyAmount cost = getBaseStepCost();
		// Weight of possessions in kilogram, rounded down
		BigInteger weight = getPossessionsWeight();
		long weightInKilogram = weight.divide(BigInteger.valueOf(1000)).longValue();
		// Add an extra cost for every kilogram of possessions
		cost = cost.add(getStepCostPerKilogram().multiply(weightInKilogram));
		return cost;
	}

	/**
	 * Get the base step cost of this robot,
	 * i.e. the step cost if this robot had no possessions.
	 */
	@Basic
	@Immutable
	public EnergyAmount getBaseStepCost() {
		return stepCost;
	}

	/**
	 * Variable registering the base step cost of a robot.
	 */
	private static final EnergyAmount stepCost = new EnergyAmount(500.0, Unit.WATTSECOND);

	/**
	 * Get the extra step cost for this robot
	 * per kilogram of possessions that it carries.
	 */
	@Basic
	@Immutable
	public EnergyAmount getStepCostPerKilogram() {
		return stepCostPerKilogram;
	}

	/**
	 * Variable registering the extra step cost
	 * per kilogram of possessions of a robot.
	 */
	private static final EnergyAmount stepCostPerKilogram = new EnergyAmount(50.0, Unit.WATTSECOND);

	/**
	 * Turn this robot clockwise.
	 * 
	 * @pre		The robot must have enough energy to turn.
	 * 			| canTurn()
	 * @effect	The orientation of this robot is turned clockwise.
	 * 			| setOrientation(getOrientation().turnClockwise())
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost of one turn.
	 * 			| drain(getTurnCost())
	 * @see #canTurn()
	 */
	public void turnClockwise() {
		assert canTurn();

		setOrientation(getOrientation().turnClockwise());
		drain(getTurnCost());
	}

	/**
	 * Turn this robot counterclockwise.
	 * 
	 * @pre		The robot must have enough energy to turn.
	 * 			| canTurn()
	 * @effect	The orientation of this robot is turned counterclockwise.
	 * 			| setOrientation(getOrientation().turnCounterClockwise())
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost of one turn.
	 * 			| drain(getTurnCost())
	 * @see #canTurn()
	 */
	public void turnCounterClockwise() {
		assert canTurn();

		setOrientation(getOrientation().turnCounterClockwise());
		drain(getTurnCost());
	}

	/**
	 * Turn this robot clockwise.
	 * 
	 * @param rotation
	 * 			The rotation to turn.
	 * 
	 * @pre		The robot must have enough energy to turn.
	 * 			| canTurn()
	 * @effect	The orientation of this robot is turned
	 * 			in the given rotation.
	 * 			| setOrientation(getOrientation().turn(rotation))
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost of one turn.
	 * 			| drain(getTurnCost())
	 * @see #canTurn()
	 */
	public void turn(Rotation rotation) {
		assert canTurn();

		setOrientation(getOrientation().turn(rotation));
		drain(getTurnCost());
	}

	/**
	 * Check whether this robot has enough energy to turn once.
	 * 
	 * @return	True if the energy cost of one turn can be drained from this robot.
	 * 			| result == canDrain(getTurnCost())
	 * @see #getTurnCost()
	 */
	public boolean canTurn() {
		return canDrain(getTurnCost());
	}

	/**
	 * Get the energy cost for one turn.
	 */
	@Basic
	@Immutable
	public EnergyAmount getTurnCost() {
		return turnCost;
	}

	/**
	 * Variable registering the base turn cost of a robot.
	 */
	private static final EnergyAmount turnCost = new EnergyAmount(100.0, Unit.WATTSECOND);

	/**
	 * Get the minimal amount of energy required to reach a given position.
	 * 
	 * @param position
	 * 			The position to reach.
	 * 
	 * @return	The actual cost to reach the given position,
	 * 			as found by running the A* path finding algorithm
	 * 			for minimal robot energy costs.
	 * 			| result == new MinimalCostAStar(this, position).getCost()
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot is not placed on any board.
	 * 			| !isPlaced()
	 * @throws	InvalidPositionException
	 * 			If the given position is not valid on this board.
	 * 			| !isValidPosition(position)
	 * @throws	UnreachablePositionException
	 * 			If this robot cannot be reached by the robot,
	 * 			regardless of its energy.
	 * 			| !isReachable(position)
	 */
	public EnergyAmount getMinimalCostToReach(Vector position) throws IllegalStateException, InvalidPositionException,
			UnreachablePositionException {
		if (!isPlaced())
			throw new IllegalStateException("Robot must be placed and not terminated.");
		if (!isValidPosition(position))
			throw new InvalidPositionException(getBoard(), position);
		if (!canMoveTo(position))
			throw new UnreachablePositionException(this, position);

		// Run A* for minimal cost finding
		return new MinimalCostAStar(this, position).getCost();
	}

	/**
	 * Check whether this robot could reach the given position
	 * if given enough energy.
	 * 
	 * @param position
	 * 			The position to reach.
	 * 
	 * @return	False if the given position is not valid
	 * 			on this board.
	 * 			| if (!isValidPosition(position))
	 * 			|   result == false
	 * @return	False if this robot cannot move to the given position.
	 * 			| else if (!canMoveTo(position))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if there exists a sequence
	 * 			of vectors such that all of the following is true:
	 * 			<ul>
	 * 			<li>the first vector equals this robot's position;</li>
	 * 			<li>the last vector equals the given position;</li>
	 * 			<li>for each vector between the first and last vector in the sequence:
	 * 			<ul>
	 * 			<li>this robot can move to the vector;</li>
	 * 			<li>the Manhattan distance to the previous vector equals one;</li>
	 * 			<li>the Manhattan distance to the next vector equals one.</li>
	 * 			</ul>
	 * 			</ul>
	 * 			| else
	 * 			|   result == (for some path:Vector[] :
	 * 			|      path[0].equals(this.getPosition())
	 * 			|       && path[path.length-1].equals(position)
	 * 			|       && for each i in 1..path.length-2 :
	 * 			|            this.canMoveTo(path[i])
	 * 			|            && path[i-1].manhattanDistance(path[i]) == 1)
	 * 			|            && path[i+1].manhattanDistance(path[i]) == 1)
	 * 			|   )
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot is not placed on any board.
	 * 			| !isPlaced()
	 */
	public boolean isReachable(Vector position) throws IllegalStateException {
		try {
			return getMinimalCostToReach(position).isGreaterThanOrEqual(EnergyAmount.ZERO);
		} catch (InvalidPositionException e) {
			return false;
		} catch (UnreachablePositionException e) {
			return false;
		}
	}

	/**
	 * Check whether this robot can reach the given position
	 * with its current amount of energy.
	 * 
	 * @param position
	 * 			The position to reach.
	 * 
	 * @return	False if the given position cannot be reached
	 * 			by the robot, regardless of its energy.
	 * 			| if (!isReachable(position))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this robot has
	 * 			sufficient energy to reach the given position.
	 * 			| else
	 * 			|   result == canDrain(getMinimalCostToReach(position))
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot is not placed on any board.
	 * 			| !isPlaced()
	 */
	public boolean canReach(Vector position) throws IllegalStateException {
		try {
			return canDrain(getMinimalCostToReach(position));
		} catch (InvalidPositionException e) {
			return false;
		} catch (UnreachablePositionException e) {
			return false;
		}
	}

	/**
	 * Move this robot to the given node.
	 * 
	 * @param node
	 * 			The node to move to.
	 * 
	 * @pre		This robot must not be terminated
	 * 			and must be placed on a board.
	 * 			| !isTerminated() && isPlaced()
	 * @pre		The given node must be effective
	 * 			and apply to this robot.
	 * 			| node != null && node.getRobot() == this
	 * @pre		This robot must be able to move
	 * 			to the node's position.
	 * 			| canMoveTo(node.getPosition())
	 * 
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost to reach the node.
	 * 			| drain(node.getG())
	 * @effect	The robot has moved to the node's position.
	 * 			| moveOnBoard(node.getPosition())
	 * @effect	The robot's orientation is set to
	 * 			the node's orientation.
	 * 			| setOrientation(node.getOrienation())
	 */
	@Model
	void moveTo(RobotNode node) {
		assert !isTerminated() && isPlaced();
		assert node != null && node.getRobot() == this;
		assert canMoveTo(node.getPosition());

		// Drain the energy cost
		drain(node.getG());
		// Move on board
		moveOnBoard(node.getPosition());
		// Set orientation
		setOrientation(node.getOrientation());
	}

	/**
	 * Move this robot and the given other robot as close as possible
	 * to each other, taking into account energy restrictions
	 * of both robots.
	 * 
	 * @param otherRobot
	 * 			The other robot.
	 * 
	 * @note	Let <code>thisReachable</code> be the set of
	 * 			all reachable nodes by this robot, that is the position
	 * 			of the node is reachable by this robot and the node's cost
	 * 			equals the cost for this robot to reach that position.
	 * 			Let <code>otherReachable</code> be the set of
	 * 			all reachable nodes by the other robot.
	 * 			| let
	 * 			|   thisReachable = {thisNode:RobotNode
	 * 			|      | this.canReach(thisNode.getPosition())
	 * 			|         && thisNode.getG() == this.getMinimalCostToReach(thisNode.getPosition())
	 * 			|   }
	 * 			|   otherReachable = {otherNode:RobotNode
	 * 			|      | otherRobot.canReach(otherNode.getPosition())
	 * 			|         && otherNode.getG() == otherRobot.getMinimalCostToReach(otherNode.getPosition())
	 * 			|   }
	 * 
	 * @effect	One node is selected from each of these sets forming a
	 * 			pair of solutions, such that every other pair of nodes
	 * 			from these sets:
	 * 			<ul>
	 * 			<li>has the same position, thus a Manhattan distance of zero;</li>
	 * 			<li>or has a Manhattan distance greater than the distance
	 * 			between the solution nodes;</li>
	 * 			<li>or has a total energy cost greater than or equal to
	 * 			the total energy cost of the solution nodes.</li>
	 * 			</ul>
	 * 			The two robots then move to their respective solution nodes.
	 * 			| let
	 * 			|   bestThisNode:RobotNode, bestOtherNode:RobotNode
	 * 			|
	 * 			| for each thisNode in thisReachable :
	 * 			|   for each otherNode in otherReachable :
	 * 			|      thisNode.getPosition().equals(otherNode.getPosition())
	 * 			|       || thisNode.getPosition().manhattanDistance(otherNode.getPosition())
	 * 			|           > bestThisNode.getPosition().manhattanDistance(bestOtherNode.getPosition())
	 * 			|       || thisNode.getG() + otherNode.getG()
	 * 			|           >= bestThisNode.getG() + bestOtherNode.getG()
	 * 			|
	 * 			| this.moveTo(bestThisNode)
	 * 			| otherRobot.moveTo(bestOtherNode)
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot is not placed on any board.
	 * 			| !isPlaced()
	 * @throws	IllegalArgumentException
	 * 			If the other robot is not effective or is not placed on a board.
	 * 			| otherRobot == null || !otherRobot.isPlaced()
	 * @throws	IllegalArgumentException
	 * 			If the other robot equals this robot.
	 * 			| otherRobot == this
	 * @throws	IllegalArgumentException
	 * 			If the other robot is placed on a different board.
	 * 			| otherRobot.getBoard() != this.getBoard()
	 */
	public void moveNextTo(Robot otherRobot) throws IllegalArgumentException {
		if (isTerminated() || !isPlaced())
			throw new IllegalStateException("Robot must be placed on a board.");
		if (otherRobot == null || !otherRobot.isPlaced())
			throw new IllegalArgumentException("Other robot must be effective and placed on a board.");
		if (otherRobot == this)
			throw new IllegalArgumentException("Other robot cannot equal this robot.");
		if (otherRobot.getBoard() != getBoard())
			throw new IllegalArgumentException("Other robot must be on the same board as this robot.");

		// Run A* for retrieving the reachable positions of both robots
		ReachAStar thisAstar = new ReachAStar(this);
		Map<Vector, ReachNode> thisReachable = thisAstar.getReachable();

		ReachAStar otherAstar = new ReachAStar(otherRobot);
		Map<Vector, ReachNode> otherReachable = otherAstar.getReachable();

		// Initialize on current positions
		ReachNode bestThisNode = thisReachable.get(this.getPosition());
		ReachNode bestOtherNode = otherReachable.get(otherRobot.getPosition());
		long bestDistance = this.getPosition().manhattanDistance(otherRobot.getPosition());
		EnergyAmount bestCost = bestThisNode.getG().add(bestOtherNode.getG());

		// Check all reachable nodes for this robot
		for (ReachNode thisNode : thisReachable.values()) {
			Vector thisPosition = thisNode.getPosition();
			// Check all distances between one and the current best distance
			for (long distance = 1; distance <= bestDistance; ++distance) {
				// Check all neighbours at the current distance
				for (Vector otherPosition : thisPosition.getNeighbours(distance)) {
					// Get reachable node at this position from other robot
					ReachNode otherNode = otherReachable.get(otherPosition);
					// If not reachable by other robot, skip
					if (otherNode == null)
						continue;
					// Get the total cost to reach these positions
					EnergyAmount cost = thisNode.getG().add(otherNode.getG());
					// If the distance is lower or the cost is lower
					// for the same best distance, store as current best
					if (distance < bestDistance || cost.isLessThan(bestCost)) {
						bestThisNode = thisNode;
						bestOtherNode = otherNode;
						bestDistance = distance;
						bestCost = cost;
					}
				}
			}
		}

		// Move to positions
		this.moveTo(bestThisNode);
		otherRobot.moveTo(bestOtherNode);
	}

	/*
	 * Shooting
	 */

	/**
	 * Shoot in the direction this robot is facing.
	 * 
	 * <p>All pieces at the first occupied position
	 * in this direction are terminated.</p>
	 * 
	 * @pre		The robot must have enough energy to shoot.
	 * 			| canShoot()
	 * @effect	If this robot is not placed on any board,
	 * 			nothing happens.
	 * @effect	All shoot targets are hit by the laser.
	 * 			| if (isPlaced())
	 * 			|   for each piece in getShootTargets()
	 * 			|      piece.hit()
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost of one shot.
	 * 			| if (isPlaced())
	 * 			|   drain(getShootCost())
	 * 
	 * @see #getShootTargets()
	 */
	public void shoot() {
		assert canShoot();

		// Do nothing if not placed
		if (!isPlaced())
			return;

		// Shoot at targets
		Set<Piece> pieces = new HashSet<Piece>(getShootTargets());
		for (Piece victim : pieces) {
			victim.hit();
		}

		// Drain the shoot cost
		drain(getShootCost());
	}

	/**
	 * Get all pieces which will be hit when this robot shoots.
	 * 
	 * @return	If this robot is not placed on any board,
	 * 			the resulting set is empty.
	 * 			| if (!isPlaced())
	 * 			|   result.isEmpty()
	 * @return	Otherwise, the resulting set contains all pieces
	 * 			on the board at the first occupied position
	 * 			in the direction this robot is facing.
	 * 			| else
	 * 			|   let
	 * 			|      possibleTargets = {target:Vector 
	 * 			|            | getOrientation() == Orientation.fromVector(target.substract(getPosition()))
	 * 			|               && target.manhattanDistance(getPosition()) >= 1
	 * 			|               && getBoard().hasPiecesAt(target)} 
	 * 			|      firstTarget = {first:Vector | first in possibleHits 
	 * 			|            && for each target in possibleTargets :
	 * 			|                  first.manhattanDistance(getPosition()) <= target.manhattanDistance(getPosition())}
	 * 			|
	 * 			|   result.equals(getBoard().getPiecesAt(firstTarget))
	 */
	public Set<Piece> getShootTargets() {
		// No targets if not placed
		if (!isPlaced())
			return Collections.emptySet();

		Orientation direction = getOrientation();
		Vector position = getPosition();
		Board board = getBoard();

		// Get the first position in the direction this robot is facing
		// where another piece is located
		Vector target = position;
		do {
			target = board.getNextPosition(target, direction);
		} while (target != null && !board.hasPiecesAt(target));

		// Return pieces at the target position
		return board.getPiecesAt(target);
	}

	/**
	 * Check whether this robot has enough energy to shoot once.
	 * 
	 * @return	True if the energy cost of one shot can be drained from this robot.
	 * 			| result == canDrain(getShootCost())
	 * @see #getShootCost()
	 */
	public boolean canShoot() {
		return canDrain(getShootCost());
	}

	/**
	 * Get the energy cost for one shot.
	 */
	@Basic
	@Immutable
	public EnergyAmount getShootCost() {
		return shootCost;
	}

	/**
	 * Variable registering the base shoot cost of a robot.
	 */
	private final static EnergyAmount shootCost = new EnergyAmount(1000.0, Unit.WATTSECOND);

	/**
	 * @effect	If this robot's hit damage is less than
	 * 			this robot's capacity, it is drained from
	 * 			the capacity.
	 * 			| if (getHitDamage().isLessThan(getCapacityAmount()))
	 * 			|   getCapacity().drain(getHitDamage());
	 * @effect	Otherwise, this robot's capacity would become less
	 * 			than zero and thus it is terminated.
	 * 			| else
	 * 			|   terminate()
	 */
	@Override
	public void hit() throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Robot must not be terminated.");

		if (getHitDamage().isLessThan(getCapacityAmount()))
			getCapacity().drain(getHitDamage());
		else
			this.terminate();
	}

	/**
	 * Get the amount of capacity that a robot loses
	 * when it is hit by a laser or an explosion.
	 */
	@Basic
	@Immutable
	public EnergyAmount getHitDamage() {
		return hitDamage;
	}

	/**
	 * Variable registering the hit damage of a robot.
	 */
	private static final EnergyAmount hitDamage = new EnergyAmount(4000.0, Unit.WATTSECOND);

	/*
	 * Program
	 */

	/**
	 * Get the program of this robot.
	 */
	@Basic
	public Program getProgram() {
		return program;
	}

	/**
	 * Set the program of this robot.
	 *
	 * @param program
	 *			The new program.
	 *
	 * @post	The new program equals the given program.
	 *			| new.getProgram() == program
	 * @throws	IllegalArgumentException
	 *			If the given program is not valid.
	 *			| !isValidProgram(program)
	 * @see #isValidProgram(Program)
	 */
	public void setProgram(Program program) throws IllegalArgumentException {
		if (!isValidProgram(program))
			throw new IllegalArgumentException("Invalid program for this robot.");
		this.program = program;
	}

	/**
	 * Check whether the given program is valid for this robot.
	 *
	 * @param program
	 *			The program to validate.
	 *
	 * @return	If this robot is terminated, true if and only if
	 * 			the given program is not effective.
	 * 			| if (isTerminated())
	 * 			|   result == (program == null)
	 * @return	If this robot is not terminated, always true.
	 *			| else
	 *			|   result == true
	 */
	public boolean isValidProgram(Program program) {
		if (isTerminated())
			return program == null;
		return true;
	}

	/**
	 * Chech whether this robot has a program.
	 * 
	 * @return	True if and only if this robot's program
	 * 			is effective.
	 * 			| result == (getProgram() != null)
	 */
	public boolean hasProgram() {
		return getProgram() != null;
	}

	/**
	 * Variable registering the program of this robot.
	 */
	private Program program;

	/**
	 * Execute one step in the program of this robot.
	 * 
	 * @effect	The robot executes one step of its program.
	 * 			| stepProgram(1)
	 * 
	 * @throws	IllegalStateException
	 * 			If this robot has no program.
	 * 			| !hasProgram()
	 * 
	 * @see #stepProgram(int)
	 */
	public void stepProgram() throws IllegalStateException {
		stepProgram(1);
	}

	/**
	 * Execute the given amount of steps in the program
	 * of this robot.
	 * 
	 * @param steps
	 * 			The amount of steps to execute.
	 * 
	 * @effect	The main command of the robot's program
	 * 			is stepped and executed at most
	 * 			<code>step</code> times. The execution may
	 * 			stop earlier when the main command
	 * 			indicates that it cannot step any further.
	 * 			| let
	 * 			|   command = getProgram().getCommand()
	 * 			|
	 * 			| for i in 1..steps :
	 * 			|   if (command.step(this))
	 * 			|      command.execute(this)
	 * 			|   else
	 * 			|      break
	 * @note	We couldn't find a decent way to formally
	 * 			document the abortion of the loop, so
	 * 			we opted for a <code>break</code> keyword.
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given amount of steps is negative.
	 * 			| steps < 0
	 * @throws	IllegalStateException
	 * 			If this robot has no program.
	 * 			| !hasProgram()
	 */
	public void stepProgram(int steps) throws IllegalArgumentException, IllegalStateException {
		if (steps < 0)
			throw new IllegalArgumentException("Amount of steps must be non-negative.");
		if (!hasProgram())
			throw new IllegalStateException("Robot has no program to step.");

		Command command = getProgram().getCommand();
		while ((steps-- > 0) && command.step(this)) {
			command.execute(this);
		}
	}

	/**
	 * @effect	All the possessions of this robot are terminated.
	 * 			| for each possession in getPossessions() :
	 * 			|   possession.terminate()
	 * @post	This robot no longer has any possessions.
	 * 			| new.getNbPossessions() == 0
	 * @post	This robot no longer has a program.
	 * 			| !new.hasProgram()
	 */
	@Override
	public void terminate() {
		// Terminate and remove all possessions
		for (Item possession : possessions) {
			possession.terminate();
		}
		possessions.clear();

		// Remove program
		setProgram(null);

		super.terminate();
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += String.format(" with %s / %s energy", getEnergy(), getCapacity());
		result += String.format(" and %d possessions", getNbPossessions());
		return result;
	}

	private class StoredEnergy extends EnergyProperty {

		/**
		 * @return	The result is the current amount of capacity
		 * 			of this robot.
		 * 			| result.equals(Robot.this.getCapacityAmount())
		 */
		@Override
		public EnergyAmount getMaximumAmount() {
			return Robot.this.getCapacityAmount();
		}

	}

	private class StoredCapacity extends EnergyProperty {

		public StoredCapacity(EnergyAmount maximumCapacity) {
			super(maximumCapacity);
		}

		/**
		 * @return	The result is the maximum amount of capacity
		 * 			of this robot.
		 * 			| result.equals(Robot.this.getMaximumCapacity())
		 */
		@Override
		public EnergyAmount getMaximumAmount() {
			return Robot.this.getMaximumCapacity();
		}

		/**
		 * @effect	The new amount of capacity is set
		 * 			for the maximum amount of capacity.
		 * 			| setAmount(amount, getMaximumAmount())
		 * 
		 * @effect	The new amount of energy of the robot
		 * 			is set to the minimum of the new capacity
		 * 			and the current amount of energy of the robot.
		 * 			This ensures that the robot never has
		 * 			more energy than capacity.
		 * 			| let
		 * 			|   storedEnergy = Robot.this.getEnergy()
		 * 			|
		 * 			| storedEnergy.setAmount(EnergyAmount.min(
		 * 			|   new.getAmount(),
		 * 			|   storedEnergy.getAmount()
		 * 			| ));
		 */
		@Override
		public void setAmount(EnergyAmount amount) {
			assert isValidAmount(amount);

			// If needed, decrease the stored energy
			// to ensure that it never exceeds the capacity
			EnergyProperty storedEnergy = Robot.this.getEnergy();
			if (storedEnergy != null) {
				storedEnergy.setAmount(EnergyAmount.min(amount, storedEnergy.getAmount()));
			}

			// Afterwards, decrease the capacity
			super.setAmount(amount);
		}

	}

}