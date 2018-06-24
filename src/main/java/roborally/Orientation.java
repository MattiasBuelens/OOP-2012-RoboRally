package roborally;

import be.kuleuven.cs.som.annotate.*;

/**
 * An enumeration of robot orientations.
 * 
 * @note	This class uses a two axis coordinate system:
 * 			<dl><dt>X-axis</dt><dd>Horizontal axis, positive values at the right.</dd>
 * 			<dt>Y-axis</dt><dd>Vertical axis, positive values downward.</dd></dl>
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
public enum Orientation {
	/**
	 * Upwards orientation in negative Y.
	 */
	UP(0, new Vector(0, -1)),
	/**
	 * Right orientation in positive X.
	 */
	RIGHT(1, new Vector(1, 0)),
	/**
	 * Downwards orientation in positive Y.
	 */
	DOWN(2, new Vector(0, 1)),
	/**
	 * Left orientation in negative X.
	 */
	LEFT(3, new Vector(-1, 0));

	private Orientation(int value, Vector vector) {
		this.value = value;
		this.vector = vector;
	}

	/**
	 * Get an integer representation of this orientation.
	 * 
	 * @return	Orientation.UP is represented by 0.
	 * 			| if (this == Orientation.UP)
	 * 			|		result == 0
	 * @return	Orientation.RIGHT is represented by 1.
	 * 			| if (this == Orientation.RIGHT)
	 * 			|		result == 1
	 * @return 	Orientation.DOWN is represented by 2.
	 * 			| if (this == Orientation.DOWN)
	 * 			|		result == 2
	 * @return	Orientation.LEFT is represented by 3.
	 * 			| if (this == Orientation.LEFT)
	 * 			|		result == 3
	 */
	@Basic
	@Immutable
	public int getValue() {
		return value;
	}

	/**
	 * Variable registering the integer representation
	 * of this orientation.
	 */
	private final int value;

	/**
	 * Get a vector representation of this orientation.
	 * 
	 * @return	The resulting vector has a length of one.
	 * 			| result.manhattanDistance() == 1
	 * @return  If orientation is UP, the resulting vector points upward.
	 * 			| if (this == Orientation.UP)
	 * 			|	result.isUp()
	 * @return  If orientation is RIGHT, the resulting vector points to the right.
	 * 			| if (this == Orientation.RIGHT)
	 * 			|	result.isRight()
	 * @return  If orientation is DOWN, the resulting vector points downward.
	 * 			| if (this == Orientation.DOWN)
	 * 			|	result.isDown()
	 * @return  If orientation is LEFT, the resulting vector points to the left.
	 * 			| if (this == Orientation.LEFT)
	 * 			|	result.isLeft()
	 */
	@Basic
	@Immutable
	public Vector getVector() {
		return vector;
	}

	/**
	 * Variable registering the vector representation
	 * of this orientation.
	 */
	private final Vector vector;

	/**
	 * Get an orientation by its integer representation.
	 * 
	 * @param value
	 * 			The integer representation.
	 * 
	 * @return	The orientation whose value representation equals the given value,
	 * 			or null if no such orientation exists.
	 * 			| if (for some orientation in values() :
	 * 			|      orientation.getValue() == value)
	 * 			|	result == orientation
	 * 			| else
	 * 			|	result == null
	 */
	public static Orientation getByValue(int value) {
		for (Orientation orientation : values()) {
			if (orientation.getValue() == value)
				return orientation;
		}
		return null;
	}

	/**
	 * Get the orientation in the direction of the given vector.
	 * 
	 * @param vector
	 * 		The vector to get the orientation from.
	 * 
	 * @return	If the vector is horizontal and has a negative X-coordinate,
	 * 			the resulting orientation is LEFT.
	 * 			| if (vector.isHorizontal() && vector.isLeft())
	 * 			|	result == Orientation.LEFT
	 * @return	If the vector is horizontal and has a positive X-coordinate,
	 * 			the resulting orientation is RIGHT.
	 * 			| else if (vector.isHorizontal() && vector.isRight())
	 * 			|	result == Orientation.RIGHT
	 * @return	If the vector is vertical and has a negative Y-coordinate,
	 * 			the resulting orientation is UP.
	 * 			| else if (vector.isVertical() && vector.isUp())
	 * 			|	result == Orientation.UP
	 * @return	If the vector is vertical and has a positive Y-coordinate,
	 * 			the resulting orientation is DOWN.
	 * 			| else if (vector.isVertical() && vector.isDown())
	 * 			|	result == Orientation.DOWN
	 * @return	If the vector is not horizontal and not vertical,
	 * 			null is returned.
	 * 			| else
	 * 			|	result == null
	 */
	public static Orientation fromVector(Vector vector) {
		if (vector.isHorizontal()) {
			return vector.isLeft() ? Orientation.LEFT : Orientation.RIGHT;
		} else if (vector.isVertical()) {
			return vector.isUp() ? Orientation.UP : Orientation.DOWN;
		}
		return null;
	}

	/**
	 * Perform a clockwise rotation on this orientation.
	 * 
	 * @return	If this orientation is UP, RIGHT is returned.
	 * 			| if (this == Orientation.UP)
	 * 			|	result == Orientation.RIGHT
	 * @return	If this orientation is RIGHT, DOWN is returned.
	 * 			| if (this == Orientation.RIGHT)
	 * 			|	result == Orientation.DOWN
	 * @return	If this orientation is DOWN, LEFT is returned.
	 * 			| if (this == Orientation.DOWN)
	 * 			|	result == Orientation.LEFT
	 * @return	If this orientation is LEFT, UP is returned.
	 * 			| if (this == Orientation.LEFT)
	 * 			|	result == Orientation.UP
	 */
	public Orientation turnClockwise() {
		int amount = values().length;
		int value = (this.getValue() + 1) % amount;
		return getByValue(value);
	}

	/**
	 * Perform a counterclockwise rotation on this orientation.
	 * 
	 * @return	If this orientation is UP, LEFT is returned.
	 * 			| if (this == Orientation.UP)
	 * 			|	result == Orientation.LEFT
	 * @return	If this orientation is RIGHT, UP is returned.
	 * 			| if (this == Orientation.RIGHT)
	 * 			|	result == Orientation.UP
	 * @return	If this orientation is DOWN, RIGHT is returned.
	 * 			| if (this == Orientation.DOWN)
	 * 			|	result == Orientation.RIGHT
	 * @return	If this orientation is LEFT, DOWN is returned.
	 * 			| if (this == Orientation.LEFT)
	 * 			|	result == Orientation.DOWN
	 * 
	 */
	public Orientation turnCounterClockwise() {
		int amount = values().length;
		int value = (this.getValue() - 1 + amount) % amount;
		return getByValue(value);
	}

	/**
	 * Perform a rotation on this orientation.
	 * 
	 * @param rotation
	 * 			The rotation direction to turn in.
	 * 
	 * @return	If the given rotation is clockwise,
	 * 			this orientation is turned clockwise.
	 * 			| if (rotation == Rotation.CLOCKWISE)
	 * 			|   result == turnClockwise()
	 * @return	If the given rotation is counter-clockwise,
	 * 			this orientation is turned counter-clockwise.
	 * 			| else if (rotation == Rotation.COUNTERCLOCKWISE)
	 * 			|   result == turnCounterClockwise()
	 * @return	Otherwise, this orientation is returned.
	 * 			| else
	 * 			|   result == this
	 */
	public Orientation turn(Rotation rotation) {
		if (rotation == Rotation.CLOCKWISE)
			return turnClockwise();
		else if (rotation == Rotation.COUNTERCLOCKWISE)
			return turnCounterClockwise();
		return this;
	}

	/**
	 * Get the minimum amount of turns needed to end up in the given orientation.
	 * 
	 * @param orientation
	 * 			The orientation to reach.
	 * @return	The result is the minimum amount of times turnClockWise()
	 * 			or turnCounterClockWise() has to be applied on this orientation
	 * 			to become equal to the orientation to reach.
	 * 			| let
	 * 			|   clockWiseTurns
	 * 			|      = min({n | this.turnClockWise()^n == orientation})
	 * 			|   counterClockWiseTurns
	 * 			|      = min({n | this.turnCounterClockWise()^n == orientation})
	 * 			|
	 * 			| result == min({cwTurns, ccwTurns})
	 * @note	The notation <code>this.x()^n</code> means that <code>x()</code>
	 * 			is applied <code>n</code> times to this orientation.
	 * 			| let
	 * 			|   orientation = this
	 * 			| for each i in 1..n :
	 * 			|   orientation = orientation.x()
	 */
	public int getDifference(Orientation orientation) {
		int amount = values().length;
		int difference = Math.abs(getValue() - orientation.getValue());
		// If difference greater than half of amount,
		// turn other way around
		if (2 * difference > amount)
			difference = amount - difference;
		return difference;
	}

}
