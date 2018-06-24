package roborally;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a robot in a game of RoboRally.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 1.0
 */
public class Robot implements IRobot {

	/*
	 * Constructors
	 */

	/**
	 * Create a new robot.
	 * 
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(energy)
	 * @post	| new.getX() == x
	 * @post	| new.getY() == y
	 * @post	| new.getOrientation() == orientation
	 * @post	| new.getEnergy() == energy
	 * @throws	InvalidPositionException
	 * 			The given X- or Y-coordinate is invalid.
	 * 			| !isValidX(x) || !isValidY(y)
	 */
	public Robot(long x, long y, Orientation orientation, double energy) throws InvalidPositionException {
		setX(x);
		setY(y);
		setOrientation(orientation);
		setEnergy(energy);
	}

	/**
	 * Create a new robot.
	 * 
	 * @post	| new.getOrientation().getValue() == orientation
	 * @see #Robot(long, long, Orientation, double)
	 */
	public Robot(long x, long y, int orientation, double energy) throws InvalidPositionException {
		setX(x);
		setY(y);
		setOrientation(orientation);
		setEnergy(energy);
	}

	/*
	 * Position
	 */

	@Override
	public long getX() {
		return x;
	}

	@Override
	public void setX(long x) throws InvalidPositionException {
		if (!isValidX(x))
			throw new InvalidPositionException(x, getY());
		this.x = x;
	}

	/**
	 * Check whether the given X-coordinate is valid for this robot.
	 * 
	 * @param x
	 * 			The X-coordinate to validate.
	 * 
	 * @return	True if the X-coordinate is positive.
	 * 			| result == (x >= 0)
	 */
	@Override
	public boolean isValidX(long x) {
		return x >= 0;
	}

	private long x;

	@Override
	public long getY() {
		return y;
	}

	@Override
	public void setY(long y) throws InvalidPositionException {
		if (!isValidY(y))
			throw new InvalidPositionException(getX(), y);
		this.y = y;
	}

	/**
	 * Check whether the given Y-coordinate is valid for this robot.
	 * 
	 * @param y
	 * 			The Y-coordinate to validate.
	 * 
	 * @return	True if the Y-coordinate is positive.
	 * 			| result == (y >= 0)
	 */
	@Override
	public boolean isValidY(long y) {
		return y >= 0;
	}

	private long y;

	/**
	 * Get the position of this robot.
	 * 
	 * @return	A vector representing the current position
	 * 			of this robot.
	 * 			| result.getX() == this.getX()
	 * 			| result.getY() == this.getY()
	 */
	private Vector getPosition() {
		return new Vector(getX(), getY());
	}

	/**
	 * Set the position of this robot.
	 * 
	 * @param vector
	 * 			The new position vector.
	 * 
	 * @post	The new position equals the given position.
	 * 			| new.getX() == vector.getX()
	 * 			| new.getY() == vector.getY()
	 * @throws	InvalidPositionException
	 *			If the given position is invalid for this robot.
	 *			| !isValidPosition(vector)
	 */
	private void setPosition(Vector vector) throws InvalidPositionException {
		if (!isValidPosition(vector))
			throw new InvalidPositionException(vector);
		setX(vector.getX());
		setY(vector.getY());
	}

	/**
	 * Check if the given vector is a valid position for this robot.
	 * 
	 * @param vector
	 * 			The position vector to check.
	 * 
	 * @return	The given vector must be effective for it to be a valid position.
	 * 			| if(vector == null)
	 * 			|	result == false
	 * @return	The X- and Y-coordinates must be valid for this robot.
	 * 			| result == isValidX(vector.getX()) && isValidY(vector.getY())
	 */
	private boolean isValidPosition(Vector vector) {
		return vector != null && isValidX(vector.getX()) && isValidY(vector.getY());
	}

	/*
	 * Orientation
	 */

	@Override
	public Orientation getOrientation() {
		return orientation;
	}

	@Override
	public void setOrientation(Orientation orientation) {
		if (orientation == null)
			orientation = Orientation.UP;
		this.orientation = orientation;
	}

	/**
	 * Set the orientation of this robot.
	 * @post	The new orientation is the orientation corresponding
	 * 			with the given integer representation.
	 * 			| new.getOrientation().getValue() == Math.abs(orientation) % Orientation.values().length
	 */
	public void setOrientation(int orientation) {
		orientation = Math.abs(orientation) % Orientation.values().length;
		setOrientation(Orientation.getByValue(orientation));
	}

	private Orientation orientation;

	/*
	 * Energy
	 */

	@Override
	public double getEnergy() {
		return energy;
	}

	@Override
	public void setEnergy(double energy) {
		assert isValidEnergy(energy);
		this.energy = energy;
	}

	/**
	 * Check whether the given amount of energy is a valid amount for this robot.
	 * 
	 * @return	True if the amount of energy is valid for the maximum amount
	 * 			of energy of this robot, false otherwise.
	 * 			| result == isValidEnergy(energy, getMaximumEnergy())
	 * @see #isValidEnergy(double, double)
	 */
	@Override
	public boolean isValidEnergy(double energy) {
		return isValidEnergy(energy, getMaximumEnergy());
	}

	/**
	 * Check whether the given amount of energy is valid.
	 * 
	 * @return	True if the amount of energy is positive and does not exceed
	 * 			the given maximum amount of energy, false otherwise.
	 * 			| result == (energy >= 0) && (energy <= maximumEnergy)
	 */
	public static boolean isValidEnergy(double energy, double maximumEnergy) {
		return energy >= 0 && energy <= maximumEnergy;
	}

	private double energy;

	@Override
	public double getMaximumEnergy() {
		return maximumEnergy;
	}

	/**
	 * Checks whether the given maximum energy amount is valid for this robot.
	 * @return	True if the given maximum amount is positive.
	 * 			| result == (maximumEnergy >= 0)
	 */
	public static boolean isValidMaximumEnergy(double maximumEnergy) {
		return maximumEnergy >= 0;
	}

	private final double maximumEnergy = 20000;

	@Override
	public double getEnergyFraction() {
		assert isValidEnergy(getEnergy());
		assert isValidMaximumEnergy(getMaximumEnergy());
		return getEnergy() / getMaximumEnergy();
	}

	@Override
	public void recharge(double amount) {
		assert amount >= 0;
		setEnergy(getEnergy() + amount);
	}

	@Override
	public void drain(double amount) {
		assert amount >= 0;
		setEnergy(getEnergy() - amount);
	}

	/*
	 * Turning and moving
	 */

	@Override
	public void move() throws InvalidPositionException {
		assert canMove();

		Vector newPosition = getNextPosition();
		setPosition(newPosition);
		drain(getStepCost());
	}

	/**
	 * Get the next position this robot would move to
	 * when moving one step forward.
	 * 
	 * @return	The current position of the robot, shifted by
	 * 			one step in its current orientation.
	 * @note	This method is not responsible for verifying
	 * 			the validity of the new position.
	 * 			<p>The calling method can check this manually
	 * 			by using {@link #isValidPosition(Vector)} or
	 * 			catch an {@link InvalidPositionException} when
	 * 			calling {@link #setPosition(Vector)}.
	 */
	private Vector getNextPosition() {
		Vector delta = getOrientation().getVector();
		Vector newPosition = getPosition().add(delta);
		return newPosition;
	}

	@Override
	public boolean canMove() {
		return getEnergy() >= getStepCost();
	}

	@Override
	public double getStepCost() {
		return stepCost;
	}

	private static final double stepCost = 500;

	@Override
	public void turnClockwise() {
		assert canTurn();

		setOrientation(getOrientation().turnClockwise());
		drain(getTurnCost());
	}

	@Override
	public void turnCounterClockwise() {
		assert canTurn();

		setOrientation(getOrientation().turnCounterClockwise());
		drain(getTurnCost());
	}

	@Override
	public boolean canTurn() {
		return getEnergy() >= getTurnCost();
	}

	@Override
	public double getTurnCost() {
		return turnCost;
	}

	private static final double turnCost = 100;

	@Override
	public double getEnergyRequiredToReach(long x, long y) throws InvalidPositionException {
		return getEnergyRequiredToReach(new Vector(x, y));
	}

	private double getEnergyRequiredToReach(Vector position) throws InvalidPositionException {
		if (!isValidPosition(position))
			throw new InvalidPositionException(position);

		// Position of target relative to this robot's position
		Vector deltaTarget = position.subtract(getPosition());
		// Amount of steps required to reach the target
		long amountOfSteps = deltaTarget.manhattanDistance();

		// New orientation when moved to target
		Orientation newOrientation = getOrientationWhenMovedTo(position);
		// Amount of turns required to turn to new orientation
		long amountOfTurns = newOrientation.getDifference(getOrientation());

		// Calculate energy cost
		return amountOfTurns * getTurnCost() + amountOfSteps * getStepCost();
	}

	@Override
	public void moveTo(long x, long y) throws InvalidPositionException {
		moveTo(new Vector(x, y));
	}

	private void moveTo(Vector position) throws InvalidPositionException {
		assert canReach(position);

		double energy = getEnergyRequiredToReach(position);
		Orientation orientation = getOrientationWhenMovedTo(position);

		try {
			setPosition(position);
		} catch (InvalidPositionException e) {
			// Already asserted in canReach()
		}
		setOrientation(orientation);
		drain(energy);
	}

	@Override
	public boolean canReach(long x, long y) throws InvalidPositionException {
		return canReach(new Vector(x, y));
	}

	private boolean canReach(Vector position) throws InvalidPositionException {
		return getEnergyRequiredToReach(position) <= getEnergy();
	}

	@Override
	public Orientation getOrientationWhenMovedTo(long x, long y) throws InvalidPositionException {
		return getOrientationWhenMovedTo(new Vector(x, y));
	}

	private Orientation getOrientationWhenMovedTo(Vector position) throws InvalidPositionException {
		if (!isValidPosition(position))
			throw new InvalidPositionException(position);

		// Position of target relative to this robot's position
		Vector deltaTarget = position.subtract(getPosition());
		Orientation orientation = getOrientation();

		// Vector of a step in this robot's orientation
		Vector deltaOrientation = orientation.getVector();

		// Retain orientation when not moving
		if (deltaTarget.equals(Vector.ZERO))
			return orientation;

		// Check if only moving in one direction
		if (deltaTarget.isHorizontal() || deltaTarget.isVertical()) {
			// Orient in same direction
			orientation = Orientation.fromVector(deltaTarget);
		} else if (deltaOrientation.isHorizontal()) {
			// Facing left or right
			if (deltaOrientation.isLeft() == deltaTarget.isLeft()) {
				// Step horizontally first, end vertically
				orientation = deltaTarget.isUp() ? Orientation.UP : Orientation.DOWN;
			} else {
				// Step vertically first, end horizontally
				orientation = deltaTarget.isLeft() ? Orientation.LEFT : Orientation.RIGHT;
			}
		} else {
			// Facing up or down
			if (deltaOrientation.isUp() == deltaTarget.isUp()) {
				// Step vertically first, end horizontally
				orientation = deltaTarget.isLeft() ? Orientation.LEFT : Orientation.RIGHT;
			} else {
				// Step horizontally first, end vertically
				orientation = deltaTarget.isUp() ? Orientation.UP : Orientation.DOWN;
			}
		}
		return orientation;
	}

	@Override
	public void moveNextTo(IRobot otherRobot) {
		try {
			moveNextTo((Robot) otherRobot);
		} catch (InvalidPositionException e) {
			// Other robot must be valid
			// so this exception cannot occur
		}
	}

	private void moveNextTo(Robot otherRobot) throws InvalidPositionException {
		// Check if this robot is trying to move next to itself
		if (this == otherRobot)
			return;

		// Check if robots occupy the same position
		if (getPosition().equals(otherRobot.getPosition())) {
			resolveConflictingPositions(otherRobot);
			return;
		}

		// Get the position of the other robot relative to this robot
		Vector delta = otherRobot.getPosition().subtract(getPosition());

		// Initial values
		Vector bestPosition = getPosition();
		Vector bestOtherPosition = otherRobot.getPosition();
		long minimumDistance = delta.manhattanDistance();
		double minimumCost = 0;

		// Build paths to walk
		List<List<Vector>> paths = buildPathsTo(otherRobot.getPosition());

		for (List<Vector> path : paths) {
			// Let this robot walk forward
			ListIterator<Vector> it = path.listIterator();
			Vector walkerPosition;
			int walkerIndex = 0;
			while (it.hasNext() && canReach(walkerPosition = it.next())) {
				// Get the remaining walkable positions along the path
				List<Vector> remainingPath = path.subList(walkerIndex + 1, path.size());

				// Let the other robot walk backwards along this path
				Vector otherPosition = otherRobot.getFirstReachablePosition(remainingPath);
				// If no position is reachable, stay at initial position
				if (otherPosition == null) {
					otherPosition = otherRobot.getPosition();
				}

				// Determine the distance and cost of this situation
				long distance = otherPosition.subtract(walkerPosition).manhattanDistance();
				double cost = getEnergyRequiredToReach(walkerPosition)
						+ otherRobot.getEnergyRequiredToReach(otherPosition);

				// If distance is lower or cost is lower for same distance,
				// save as current minimum
				if (distance < minimumDistance || (distance == minimumDistance && cost < minimumCost)) {
					bestPosition = walkerPosition;
					bestOtherPosition = otherPosition;
					minimumDistance = distance;
					minimumCost = cost;
				}

				// Move to next
				walkerIndex = it.nextIndex();
			}
		}

		// Move to best positions
		moveTo(bestPosition);
		otherRobot.moveTo(bestOtherPosition);
	}

	/**
	 * Resolve the conflict when two robots occupy the same position.
	 * 
	 * One of the robots makes one step to a valid new position
	 * (making turns if necessary), if it has sufficient energy
	 * to do so. If none of the robots has enough energy, nothing
	 * happens and the conflict remains unresolved.
	 * 
	 * @param otherRobot
	 * 			The conflicting robot.
	 * @pre		The robots are not equal.
	 * 			| this != otherRobot
	 * @pre		Both robots occupy the same position.
	 * 			| this.getPosition().equals(otherRobot.getPosition())
	 * @throws	InvalidPositionException
	 * 			This should not be thrown when the other robot
	 * 			conforms to its invariants.
	 */
	private void resolveConflictingPositions(Robot otherRobot) throws InvalidPositionException {
		assert this != otherRobot;
		assert this.getPosition().equals(otherRobot.getPosition());

		// Get possible positions
		List<Vector> positions = getAdjacentPositions(this.getPosition());
		// List of conflicting robots
		Robot[] robots = new Robot[] { this, otherRobot };

		// Initial values
		Robot bestRobot = this;
		Vector bestPosition = this.getPosition();
		double minimumCost = Double.MAX_VALUE;

		// Test every position on every robot
		for (Robot robot : robots) {
			for (Vector position : positions) {
				// If this robot can reach this position,
				// retrieve the cost to do so
				if (robot.canReach(position)) {
					double cost = robot.getEnergyRequiredToReach(position);
					// If cost is lower than current minimum,
					// use this robot and this position
					if (cost < minimumCost) {
						bestPosition = position;
						bestRobot = robot;
						minimumCost = cost;
					}
				}
			}
		}

		// Resolve conflict
		bestRobot.moveTo(bestPosition);
	}

	/**
	 * Get a list of valid positions adjacent to the given position.
	 * 
	 * @param position
	 * 			The position.
	 * 
	 * @return	A list of valid positions at a Manhattan distance of
	 * 			one around the given position.
	 * 			| for each adjacent in result:
	 * 			|	isValidPosition(adjacent)
	 * 			|	&& adjacent.subtract(position).manhattanDistance() == 1
	 */
	private List<Vector> getAdjacentPositions(Vector position) {
		List<Vector> adjacents = new ArrayList<Vector>();
		// Loop over all orientations
		for (Orientation orientation : Orientation.values()) {
			// Get position when stepping in this orientation
			Vector delta = orientation.getVector();
			Vector neighbour = position.add(delta);
			// Add to list if valid position
			if (isValidPosition(neighbour))
				adjacents.add(neighbour);
		}
		return adjacents;
	}

	/**
	 * Get the first position in the given list which can be reached
	 * by this robot.
	 * 
	 * @param positions
	 * 			A list of positions.
	 * 
	 * @return	If one of the positions in the list can be reached,
	 * 			the first reachable position is returned.
	 * 			If none of the positions can be reached,
	 * 			null is returned.
	 * 			| if(for some position in positions: canReach(position)
	 * 			|	let
	 * 			|		firstReachableIndex = min({i | canReach(positions.get(i))})
	 * 			|	result == position.get(firstReachableIndex)
	 * 			| else
	 * 			|	result == null
	 * 
	 * @throws	InvalidPositionException
	 * 			If one of the given positions is invalid for this robot.
	 * 			| for some position in positions:
	 * 			|	!isValidPosition(position)
	 */
	private Vector getFirstReachablePosition(List<Vector> positions) throws InvalidPositionException {
		Iterator<Vector> it = positions.iterator();
		Vector position;
		while (it.hasNext()) {
			position = it.next();
			if (canReach(position))
				return position;
		}
		return null;
	}

	/**
	 * Construct paths to move towards a given target position.
	 * 
	 * @param target
	 * 			The target position.
	 * 
	 * @return	A list of paths (list of positions).
	 */
	private List<List<Vector>> buildPathsTo(Vector target) {
		List<List<Vector>> paths = new ArrayList<List<Vector>>();

		// First path
		List<Vector> path = new ArrayList<Vector>();
		Vector position = this.getPosition();
		position = buildHorizontalPath(path, position, target);
		position = buildVerticalPath(path, position, target);
		paths.add(path);

		// Second path, if not on one line
		Vector deltaTarget = target.subtract(this.getPosition());
		if (!deltaTarget.isHorizontal() && !deltaTarget.isVertical()) {
			path = new ArrayList<Vector>();
			position = this.getPosition();
			position = buildVerticalPath(path, position, target);
			position = buildHorizontalPath(path, position, target);
			paths.add(path);
		}
		return paths;
	}

	/**
	 * Extend a path horizontally to move from a starting position
	 * towards the X-coordinate of the target position.
	 *  
	 * @param path
	 * 			The path (list of positions) to extend.
	 * @param start
	 * 			The starting position.
	 * @param target
	 * 			The target position.
	 * 
	 * @post	The given path is extended horizontally
	 * 			towards a position with the same X-coordinate
	 * 			as the target. This last position is not yet
	 * 			added to the path, but is returned instead.
	 * @return	The last position with the same X-coordinate
	 * 			as the target.
	 */
	private static Vector buildHorizontalPath(List<Vector> path, Vector start, Vector target) {
		Vector deltaTarget = target.subtract(start);
		Vector position = start;

		// Direction to move along
		int direction = deltaTarget.isRight() ? 1 : -1;
		// Distance to travel
		long distance = Math.abs(target.getX() - start.getX());

		for (long x = 0; x < distance; x++) {
			path.add(position);
			position = position.add(direction, 0);
		}
		return position;
	}

	/**
	 * Extend a path vertically to move from a starting position
	 * towards the Y-coordinate of the target position.
	 *  
	 * @param path
	 * 			The path (list of positions) to extend.
	 * @param start
	 * 			The starting position.
	 * @param target
	 * 			The target position.
	 * 
	 * @post	The given path is extended vertically
	 * 			towards a position with the same Y-coordinate
	 * 			as the target. This last position is not yet
	 * 			added to the path, but is returned instead.
	 * @return	The last position with the same Y-coordinate
	 * 			as the target.
	 */
	private static Vector buildVerticalPath(List<Vector> path, Vector start, Vector target) {
		Vector deltaTarget = target.subtract(start);
		Vector position = start;

		// Direction to move along
		int direction = deltaTarget.isDown() ? 1 : -1;
		// Distance to travel
		long distance = Math.abs(target.getY() - start.getY());

		for (long y = 0; y < distance; y++) {
			path.add(position);
			position = position.add(0, direction);
		}

		return position;
	}

	/**
	 * Check whether this robot implementation takes turns into account
	 * when finding the energy required to reach a position and
	 * when moving next to another robot.
	 * 
	 * @note Watch out guys, we're dealing with a bad ass over here!
	 * @see #getEnergyRequiredToReach(long, long)
	 * @see #moveNextTo(Robot)
	 */
	public static boolean isGetEnergyRequiredToReachAndMoveNextTo16Plus() {
		return true;
	}

}