package roborally;

import be.kuleuven.cs.som.annotate.*;

/**
 * Represents the orientation of a robot.
 * 
 * @note	This class uses a two axis coordinate system:
 * 			<dl><dt>X-axis</dt><dd>Horizontal axis, positive values at the right.</dd>
 * 			<dt>Y-axis</dt><dd>Vertical axis, positive values downward.</dd></dl>
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 1.0
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

	private int value;
	private Vector vector;

	private Orientation(int value, Vector vector) {
		this.value = value;
		this.vector = vector;
	}

	/**
	 * Get an integer representation of this orientation.
	 * 
	 * @return	Orientation.UP is represented by 0.
	 * 			| if (Orientation.UP)
	 * 			|		result == 0
	 * @return	Orientation.RIGHT is represented by 1.
	 * 			| if (Orientation.RIGHT)
	 * 			|		result == 1
	 * @return 	Orientation.DOWN is represented by 2.
	 * 			| if (Orientation.DOWN)
	 * 			|		result == 2
	 * @return	Orientation.LEFT is represented by 3.
	 * 			| if (Orientation.LEFT)
	 * 			|		result == 3
	 */
	@Basic
	@Immutable
	public int getValue() {
		return value;
	}

	/**
	 * Get a vector representation of this orientation.
	 * 
	 * @return	The resulting vector has a length of one.
	 * 			| result.manhattanDistance() == 1
	 * @return  If orientation is UP, the resulting vector points upward.
	 * 			| if (Orientation.UP)
	 * 			|	result.isUp()
	 * @return  If orientation is RIGHT, the resulting vector points to the right.
	 * 			| if (Orientation.RIGHT)
	 * 			|	result.isRight()
	 * @return  If orientation is DOWN, the resulting vector points downward.
	 * 			| if (Orientation.DOWN)
	 * 			|	result.isDown()
	 * @return  If orientation is LEFT, the resulting vector points to the left.
	 * 			| if (Orientation.LEFT)
	 * 			|	result.isLeft()
	 */
	@Basic
	@Immutable
	public Vector getVector() {
		return vector;
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
	 * 			| if (vector.isHorizontal() && vector.isRight())
	 * 			|	result == Orientation.RIGHT
	 * @return	If the vector is vertical and has a negative Y-coordinate,
	 * 			the resulting orientation is UP.
	 * 			| if (vector.isVertical() && vector.isUp())
	 * 			|	result == Orientation.UP
	 * @return	If the vector is vertical and has a positive Y-coordinate,
	 * 			the resulting orientation is DOWN.
	 * 			| if (vector.isVertical() && vector.isDown())
	 * 			|	result == Orientation.DOWN
	 * @return	If the vector is not horizontal and not vertical,
	 * 			null is returned.
	 * 			| if (!vector.isHorizontal() && !vector.isVertical())
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
	 * Get an orientation by its integer representation.
	 * @param value
	 * 			The integer representation.
	 * @result	The orientation whose value representation equals the given value,
	 * 			or null if no such orientation exists.
	 * 			| if (for some elem in getValues() : elem.getValue() == value)
	 * 			|	result == elem
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
	 * 			is applied <code>n</code> to this orientation.
	 * 			| let
	 * 			|   orientation = this
	 * 			| for i in 1..n :
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
