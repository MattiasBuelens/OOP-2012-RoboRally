package roborally;

import be.kuleuven.cs.som.annotate.*;

/**
 * Classes for representing robots should implement <code>IRobot</code>.
 * 
 * @invar	The robot's position is valid.
 * 			| isValidX(getX()) && isValidY(getY())
 * @invar	The robot's amount of energy is valid.
 * 			| isValidEnergy(getEnergy())
 */
public interface IRobot {
	/**
	 * Get the X-coordinate of this robot.
	 */
	@Basic
	long getX();

	/**
	 * Set the X-coordinate of this robot.
	 * 
	 * @param x
	 * 			The new X-coordinate.
	 * 
	 * @post	The new X-coordinate equals the given position.
	 * 			| new.getX() == x
	 * @throws	InvalidPositionException
	 * 			If the given X-coordinate is not valid.
	 * 			| !isValidX(x)
	 * @see #isValidX(long)
	 */
	void setX(long x) throws InvalidPositionException;

	/**
	 * Check whether the given X-coordinate is valid for this robot.
	 * 
	 * @param x
	 * 			The X-coordinate to validate.
	 */
	boolean isValidX(long x);

	/**
	 * Get the Y-coordinate of this robot.
	 */
	@Basic
	long getY();

	/**
	 * Set the Y-coordinate of this robot.
	 * 
	 * @param y
	 * 			The new Y-coordinate.
	 * 
	 * @post	The new Y-coordinate equals the given position.
	 * 			| new.getY() == y
	 * @throws	InvalidPositionException
	 * 			If the given Y-coordinate is not valid.
	 * 			| !isValidY(y)
	 * @see #isValidY(long)
	 */
	void setY(long y) throws InvalidPositionException;

	/**
	 * Check whether the given Y-coordinate is valid for this robot.
	 * 
	 * @param y
	 * 			The Y-coordinate to validate.
	 */
	boolean isValidY(long y);

	/**
	 * Get the orientation this robot is facing.
	 */
	@Basic
	Orientation getOrientation();

	/**
	 * Set the orientation this robot is facing.
	 * @post	If the given orientation is effective,
	 * 			the new orientation equals the given orientation.
	 * 			If the given orientation is not effective,
	 * 			the new orientation is set to facing up.
	 * 			| if (orientation == null)
	 * 			|	new.getOrientation() == Orientation.UP
	 * 			| else
	 * 			|	new.getOrientation() == orientation
	 */
	void setOrientation(Orientation orientation);

	/**
	 * Get the amount of energy stored by this robot.
	 */
	@Basic
	double getEnergy();

	/**
	 * Set the amount of energy stored by this robot.
	 * @pre		The amount of energy must be valid.
	 * 			| isValidEnergy(energy)
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergy() == energy
	 * @see #isValidEnergy(double)
	 */
	void setEnergy(double energy);

	/**
	 * Check whether the given amount of energy is a valid amount for this robot.
	 */
	boolean isValidEnergy(double energy);

	/**
	 * Get the maximum amount of energy that can be stored by this robot.
	 */
	@Basic
	double getMaximumEnergy();

	/**
	 * Get the amount of energy of this robot as a fraction of the maximum energy amount.
	 * 
	 * @return	The result is the amount of energy divided by the maximum energy amount.
	 * 			| result == getEnergy() / getMaximumEnergy()
	 * @see #getEnergy()
	 * @see #getMaximumEnergy()
	 */
	double getEnergyFraction();

	/**
	 * Recharge this robot with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @pre		The extra energy amount must be positive.
	 * 			| amount >= 0
	 * @effect	This robot's energy is increased with the given amount.
	 * 			| setEnergy(getEnergy() + amount)
	 * @see #setEnergy(double)
	 */
	void recharge(double amount);

	/**
	 * Drain this robot with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to drain.
	 * 
	 * @pre		The drained amount amount must be positive.
	 * 			| amount >= 0
	 * @effect	This robot's energy is reduced with the given amount.
	 * 			| setEnergy(getEnergy() - amount)
	 * @see #setEnergy(double)
	 */
	void drain(double amount);

	/**
	 * Move this robot one step forward in the direction it's facing.
	 * 
	 * @pre		The robot must have enough energy to move.
	 * 			| canMove()
	 * @effect	The X-coordinate is decremented when facing left.
	 * 			| if (this.getOrientation() == Orientation.LEFT)
	 * 			|	setX(getX() - 1)
	 * @effect	The X-coordinate is incremented when facing right.
	 * 			| if (this.getOrientation() == Orientation.RIGHT)
	 * 			|	setX(getX() + 1)
	 * @effect	The Y-coordinate is decremented when facing up.
	 * 			| if (this.getOrientation() == Orientation.UP)
	 * 			|	setY(getY() - 1)
	 * @effect	The Y-coordinate is incremented when facing down.
	 * 			| if (this.getOrientation() == Orientation.DOWN)
	 * 			|	setY(getY() + 1)
	 * @effect	The energy of this robot is decreased with
	 * 			the energy cost of one step.
	 * 			| drain(getStepCost())
	 * @throws	InvalidPositionException
	 * 			If this robot can't move in this direction.
	 * @see #canMove()
	 */
	void move() throws InvalidPositionException;

	/**
	 * Check if this robot has enough energy to move one step forward.
	 * 
	 * @return	True if this robot's energy is at least the energy cost of one step.
	 * 			| result == getEnergy() >= getStepCost()
	 * @see #getStepCost()
	 */
	boolean canMove();

	/**
	 * Get the energy cost for one step forward.
	 */
	@Basic
	double getStepCost();

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
	void turnClockwise();

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
	void turnCounterClockwise();

	/**
	 * Check if this robot has enough energy to turn once.
	 * 
	 * @return	True if this robot's energy is at least the energy cost of one turn.
	 * 			| result == getEnergy() >= getTurnCost()
	 * @see #getTurnCost()
	 */
	boolean canTurn();

	/**
	 * Get the energy cost for one turn.
	 */
	@Basic
	double getTurnCost();

	/**
	 * Get the minimal amount of energy required to reach a given position.
	 *  
	 * @param x
	 * 			The X-coordinate of the position to reach.
	 * @param y
	 * 			The Y-coordinate of the position to reach.
	 * 
	 * @return	The minimum cost is the sum of the total step and turn costs.
	 * 			The total step cost is the Manhattan distance between the
	 * 			current position and the position to reach, times the cost per step.
	 * 			The total turn cost is the amount of turns needed to end up in the
	 * 			new orientation, times the cost per turn.
	 * 			| let
	 * 			|	amountOfSteps = Math.abs(getX() - x) + Math.abs(getY() - y)
	 * 			|	amountOfTurns = getOrientationWhenMovedTo(x, y).getDifference(getOrientation())
	 * 			| result == amountOfSteps * getStepCost() + amountOfTurns * getTurnCost()
	 * @throws	InvalidPositionException
	 * 			If the position to reach is invalid for this robot.
	 * 			| !isValidX(x) || !isValidY(y)
	 */
	double getEnergyRequiredToReach(long x, long y) throws InvalidPositionException;

	/**
	 * Get the new orientation of this robot when it would have moved
	 * to the given position.
	 * 
	 * @param x
	 * 			The X-coordinate of the position to reach.
	 * @param y
	 * 			The Y-coordinate of the position to reach.
	 * 
	 * @return	The returned orientation is the orientation when the
	 * 			robot would have executed all necessary turns to
	 * 			reach the given position.
	 *
	 * @throws	InvalidPositionException
	 * 			If the position to reach is invalid for this robot.
	 * 			| !isValidX(x) || !isValidY(y)
	 */
	Orientation getOrientationWhenMovedTo(long x, long y) throws InvalidPositionException;

	/**
	 * Check whether this robot can reach the given position.
	 * 
	 * @param x
	 * 			The X-coordinate of the position to reach.
	 * @param y
	 * 			The Y-coordinate of the position to reach.
	 * 
	 * @return	True if this robot has sufficient energy to reach
	 * 			the given position, false otherwise.
	 * 			| result == getEnergyRequiredToReach(x, y) <= getEnergy()
	 * @throws	InvalidPositionException
	 * 			If the position to reach is invalid for this robot.
	 * 			| !isValidX(x) || !isValidY(y)
	 */
	boolean canReach(long x, long y) throws InvalidPositionException;

	/**
	 * Move this robot to the given position, taking in account
	 * turns and energy restrictions.
	 * 
	 * @param x
	 * 			The X-coordinate of the position to reach.
	 * @param y
	 * 			The Y-coordinate of the position to reach.
	 * 
	 * @pre		The robot must be able to reach the given position.
	 * 			| canReach(x, y)
	 * @effect	The robot has moved to the new position.
	 * 			| setX(x)
	 * 			| setY(y)
	 * @effect	The robot has made the optimal turns necessary
	 * 			to reach the given position.
	 * 			| setOrientation(getOrientationWhenMovedTo(x, y))
	 * @effect	The energy of this robot is decreased
	 * 			with the energy needed to reach the given position.
	 * 			| drain(getEnergyRequiredToReach(x, y))
	 * @throws	InvalidPositionException
	 * 			If the position to reach is invalid for this robot.
	 * 			| !isValidX(x) || !isValidY(y)
	 */
	void moveTo(long x, long y) throws InvalidPositionException;

	/**
	 * Move this robot and the given other robot as close as possible
	 * to each other, taking into account energy restrictions
	 * of both robots.
	 * 
	 * <p>If both robots initially occupy the same position, the robot
	 * which can move to an adjacent position with the minimum cost
	 * moves to this position. If none of the robots have sufficient
	 * energy to move to an adjacent position, both robots remain in
	 * their original conflicting positions.
	 * 
	 * <p>This goes one step further than the assignment specifies:
	 * not only does this method avoid inefficient moves and turns,
	 * it also finds the <em>most efficient path</em> to travel along.
	 * We found the use of the term "inefficient" in the assignment
	 * somewhat ambiguous, and thus we had a slightly different
	 * interpretation of the specification.
	 * 
	 * @param robot
	 * 			The other robot.
	 * 
	 * @effect	<p>If the robots are not equal equals and a minimum energy
	 * 			position pair exists, both robots move to their respective
	 * 			positions specified by the selected pair.
	 * 			<p>The set of minimum energy position pairs is specified
	 * 			in the notes below.
	 * 
	 * 			| if (this != robot && #(minDistEnergyPositions) != 0)
	 * 			|   for some position in minDistEnergyPositions
	 * 			|      this.moveTo(position.x1, position.y1)
	 * 			|      robot.moveTo(position.x2, position.y2)
	 * 
	 * @note	<p>In the first-order logic specifications, the following shorthand
	 * 			notations are used for the Manhattan distance between two positions
	 * 			and the total cost for both robots to reach two positions.
	 * 			| let
	 * 			|   manhattanDistance((x1, y1), (x2, y2))
	 * 			|      = Math.abs(x1 - x2) + Math.abs(y1 - y2)
	 * 			|   totalCost((x1, y1), (x2, y2))
	 * 			|      = this.getEnergyRequiredToReach(x1, y1)
	 * 			|         + robot.getEnergyRequiredToReach(x2, y2)
	 * 
	 * @note	<p>The set of reachable position pairs is the set of all position pairs
	 * 			which can (respectively) be reached by both robots.
	 * 			| let
	 * 			|   reachablePositions = {((x1, y1), (x2, y2))
	 * 			|      | this.canReach(x1, y1) && robot.canReach(x2, y2)
	 * 			|   }
	 * 
	 * @note	<p><b>If both robots occupy the same position, one of them should
	 * 			move to an adjacent position.</b>
	 * 
	 * 			<p>The set of adjacent position pairs is the subset of all reachable
	 * 			position pairs with a Manhattan distance of one and with one of the
	 * 			robots in its initial position.
	 * 
	 * 			<p>The set of minimum energy position pairs is the subset of adjacent
	 * 			position pairs with the minimum total cost of all pairs.
	 * 			
	 * 			| if (this.getX() == robot.getX() && this.getY() == robot.getY())
	 * 			|   let
	 * 			|      adjacentPositions = {position
	 * 			|         | position in reachablePositions
	 * 			|            && manhattanDistance(position) == 1
	 * 			|            && ((position.x1 == this.getX() && position.y1 == this.getY())
	 * 			|                  || (position.x2 == robot.getX() && position.y2 == robot.getY()))
	 * 			|      }
	 * 			|      minDistEnergyPositions = {minPosition
	 * 			|         | minPosition in adjacentPositions
	 * 			|            && for each position in adjacentPositions :
	 * 			|                  totalCost(minPosition) <= totalCost(position)
	 * 			|      }
	 * @note	<p><b>If the robots are in different positions, they should move
	 * 			next to or at least closer to each other.</b>
	 * 
	 * 			<p>The set of minimum distance position pairs is the subset of
	 * 			reachable position pairs with the minimum Manhattan distance
	 * 			of all pairs.
	 * 
	 * 			<p>The set of minimum energy position pairs is the subset of
	 * 			minimum distance position pairs with the minimum total cost
	 * 			of all pairs.
	 * 
	 * 			| else
	 * 			|   let
	 * 			|      minDistancePositions = {minPosition
	 * 			|         | minPosition in reachablePositions
	 * 			|            && for each position in reachablePositions :
	 * 			|                  manhattanDistance(minPosition) <= manhattanDistance(position)
	 * 			|      }
	 * 			|      minDistEnergyPositions = {minPosition
	 * 			|         | minPosition in minDistancePositions
	 * 			|            && for each position in minDistancePositions :
	 * 			|                  totalCost(minPosition) <= totalCost(position)
	 * 			|      }
	 */
	void moveNextTo(IRobot robot);

}