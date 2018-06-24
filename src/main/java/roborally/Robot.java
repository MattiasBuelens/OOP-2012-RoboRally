package roborally;

import java.util.*;

import roborally.path.*;
import roborally.util.SortedList;
import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Represents a robot in a game of RoboRally.
 * 
 * <p>A robot is a player-controlled piece.</p>
 * 
 * @invar	The robot's orientation is valid.
 * 			| isValidOrientation(getOrientation())
 * @invar	The robot's amount of energy is valid.
 * 			| isValidEnergy(getEnergy())
 * @invar	The robot has a proper set of possessions.
 * 			| hasProperPossessions()
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class Robot extends Piece implements EnergyCarrier {

	/*
	 * Constructors
	 */

	/**
	 * Create a new robot with the given orientation and energy.
	 * 
	 * @param orientation
	 * 			The orientation for this new robot.
	 * @param energy
	 * 			The energy for this new robot.
	 * 
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(energy)
	 * 
	 * @post	The move cost of the new robot equals 500 Ws.
	 * 			| new.getMoveCost() == 500.0
	 * @post	The turn cost of the new robot equals 100 Ws.
	 * 			| new.getTurnCost() == 100.0
	 * @post	The shoot cost of the new robot equals 1000 Ws.
	 * 			| new.getShootCost() == 1000.0
	 * @post	The maximum amount of energy of the new robot equals 20000 Ws.
	 * 			| new.getMaximumEnergy() == 20000.0
	 * @post	The new robot does not have any possessions yet.
	 * 			| new.getNbPossessions() == 0
	 * 
	 * @effect	The new robot is initialized as a new piece.
	 * 			| super()
	 * @effect	The new robot's orientation is set to the given orientation.
	 * 			| setOrientation(orientation)
	 * @effect	The new robot's energy is set to the given amount of energy
	 * 			if its valid for the new maximum energy.
	 * 			| setEnergy(energy, new.getMaximumEnergy())
	 */
	public Robot(Orientation orientation, double energy) {
		setOrientation(orientation);
		setEnergy(energy);
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
	 * 			|	new.getOrientation() == orientation
	 * 			| else
	 * 			|	new.getOrientation() == Orientation.UP
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

	private Orientation orientation;

	/*
	 * Energy
	 */

	@Basic
	@Override
	public double getEnergy() {
		return energy;
	}

	/**
	 * Set the amount of energy stored by this robot.
	 * 
	 * @param energy
	 * 			The new amount of energy.
	 * 
	 * @effect	The robot's energy is set for this robot's maximum energy.
	 * 			| setEnergy(energy, getMaximumEnergy())
	 * @see #setEnergy(double, double)
	 */
	@Override
	public void setEnergy(double energy) throws IllegalStateException {
		setEnergy(energy, getMaximumEnergy());
	}

	/**
	 * Set the amount of energy stored by this robot
	 * for the given maximum amount of energy.
	 * 
	 * @param energy
	 * 			The new amount of energy.
	 * @param maxEnergy
	 * 			The maximum amount of energy.
	 * 
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(energy, maxEnergy)
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergy() == energy
	 * @throws	IllegalStateException
	 * 			If the robot is terminated.
	 * 			| isTerminated()
	 * @see #isValidEnergy(double, double)
	 */
	@Model
	void setEnergy(double energy, double maxEnergy) throws IllegalStateException {
		if (isTerminated())
			throw new IllegalStateException("Robot must not be terminated.");

		assert isValidEnergy(energy, maxEnergy);
		this.energy = energy;
	}

	/**
	 * Check whether the given amount of energy is a valid amount for this robot.
	 * 
	 * @param energy
	 * 			The amount of energy to validate.
	 * 
	 * @return	True if the amount of energy is valid for the maximum amount
	 * 			of energy of this robot, false otherwise.
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
	 * Check whether the given maximum energy amount is valid for this robot.
	 * 
	 * @return	True if the given maximum amount is strictly positive.
	 * 			| result == (maximumEnergy > 0)
	 */
	public static boolean isValidMaximumEnergy(double maximumEnergy) {
		return maximumEnergy > 0;
	}

	private final double maximumEnergy = 20000;

	/**
	 * Get the amount of energy of this robot as a fraction of the maximum energy amount.
	 * 
	 * @return	The result is the amount of energy divided by the maximum energy amount.
	 * 			This double division is precise to at least two decimals.
	 * 			| result == getEnergy() / getMaximumEnergy()
	 * @see #getEnergy()
	 * @see #getMaximumEnergy()
	 */
	public double getEnergyFraction() {
		assert isValidEnergy(getEnergy());
		assert isValidMaximumEnergy(getMaximumEnergy());
		return getEnergy() / getMaximumEnergy();
	}

	@Override
	public void recharge(double amount) {
		assert canRecharge(amount);
		setEnergy(getEnergy() + amount);
	}

	/**
	 * @return	False if this robot is terminated.
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
	 * @return	False if this robot is terminated.
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
	 * Transfer the given amount of energy from this robot
	 * to the given receiving carrier.
	 * 
	 * <p>This operation is illegal for robots and will always
	 * throw an exception.
	 * 
	 * @throws	UnsupportedOperationException
	 * 			Always, since transferring energy from a robot
	 * 			is an illegal operation.
	 */
	@Override
	public void transfer(EnergyCarrier receivingCarrier, double amount) throws UnsupportedOperationException {
		assert canTransfer(receivingCarrier, amount);
		throw new UnsupportedOperationException("Robots cannot transfer energy to other energy carriers.");
	}

	/**
	 * @return	Always false, since transferring energy from a robot
	 * 			is an illegal operation.
	 * 			| result == false
	 */
	@Override
	public boolean canTransfer(EnergyCarrier receivingCarrier, double amount) {
		return false;
	}

	/*
	 * Possessions
	 */

	/**
	 * The possessions of this robot are stored as a sorted list
	 * of items, sorted on their weight in descending order.
	 */
	private List<Item> possessions = new SortedList<Item>(Collections.reverseOrder(new ItemWeightComparator()));

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
	 * equal to the weight of all but <code>(index - 1)</code> possessions.
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
	 * Check whether this robot has a proper set of possessions.
	 * 
	 * @return	True if and only if this robot can have each of
	 * 			its possessions and if none of its possessions
	 * 			are placed on a board.
	 * 			| for each index in 1..getNbPossessions() :
	 * 			|   canHaveAsPossession(getPossessionAt(index))
	 * 			|    && !getPossessionAt(index).isPlaced()
	 */
	public boolean hasProperPossessions() {
		for (Item item : possessions) {
			if (!canHaveAsPossession(item))
				return false;
			if (item.isPlaced())
				return false;
		}
		return true;
	}

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
	public <T extends Item> Set<T> getPossessions(Class<T> possessionType) {
		Set<T> typedPieces = new HashSet<T>();
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
	 * 			the item is used on this robot.
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
	@Model
	void addAsPossession(Item item) throws IllegalStateException, IllegalArgumentException {
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
	@Model
	void removeAsPossession(Item item) throws IllegalStateException, IllegalArgumentException {
		if (!canHaveAsPossession(item))
			throw new IllegalStateException("Robot must not be terminated and item must be effective.");
		if (!hasAsPossession(item))
			throw new IllegalArgumentException("Robot does not possess this item.");

		possessions.remove(item);
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
	 * @return	| if (isPlaced())
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
	 */
	@Basic
	public double getStepCost() {
		return stepCost;
	}

	private static final double stepCost = 500;

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
	public double getTurnCost() {
		return turnCost;
	}

	private static final double turnCost = 100;

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
	public double getMinimalCostToReach(Vector position) throws IllegalStateException, InvalidPositionException,
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
			return getMinimalCostToReach(position) >= 0;
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
		double bestCost = bestThisNode.getG() + bestOtherNode.getG();

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
					double cost = thisNode.getG() + otherNode.getG();
					// If the distance is lower or the cost is the same
					// for the same best distance, store as current best
					if (distance < bestDistance || cost < bestCost) {
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
	 * @effect	If this robot is placed on a board,
	 * 			the energy of this robot is decreased with
	 * 			the energy cost of one shot.
	 * 			| if (isPlaced())
	 * 			|   drain(getShootCost())
	 * @effect	The target position is the first occupied position
	 * 			on the board in the direction this robot is facing.
	 * 			If this robot is placed on a board, all the pieces
	 * 			on the board at the target position are terminated.
	 * 			| if (isPlaced())
	 * 			|    let
	 * 			|      possibleTargets = {target:Vector 
	 * 			|            | getOrientation() == Orientation.fromVector(target.substract(getPosition()))
	 * 			|               && target.manhattanDistance(getPosition()) >= 1
	 * 			|               && getBoard().hasPiecesAt(target)} 
	 * 			|      firstTarget = {first:Vector | first in possibleHits 
	 * 			|            && for each target in possibleTargets :
	 * 			|                  first.manhattanDistance(getPosition()) <= target.manhattanDistance(getPosition())} 
	 * 			|    for each piece in getBoard().getPiecesAt(firstHit)
	 * 			|      piece.terminate()
	 */
	public void shoot() {
		assert canShoot();

		// Do nothing if not placed
		if (!isPlaced())
			return;

		Orientation direction = getOrientation();
		Vector position = getPosition();
		Board board = getBoard();

		// Get the first position in the direction this robot is facing
		// where another piece is located
		Vector target = position;
		do {
			target = board.getNextPosition(target, direction);
		} while (target != null && !board.hasPiecesAt(target));

		// Terminate all positions at the target position
		Set<Piece> pieces = new HashSet<Piece>(board.getPiecesAt(target));

		for (Piece victim : pieces) {
			victim.terminate();
		}

		// Drain the shoot cost
		drain(getShootCost());
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
	public double getShootCost() {
		return shootCost;
	}

	private final static double shootCost = 1000;

	/**
	 * @effect	All the possessions of this robot are terminated.
	 * 			| for each possession in getPossessions() :
	 * 			|   possession.terminate()
	 * @post	This robot no longer has any possessions.
	 * 			| new.getNbPossessions() == 0
	 */
	@Override
	public void terminate() {
		// Terminate and remove all possessions
		for (Item possession : possessions) {
			possession.terminate();
		}
		possessions.clear();

		super.terminate();
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += String.format(" with %.2f Ws energy", getEnergy());
		result += String.format(" and %d possessions", getNbPossessions());
		return result;
	}
}