package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * Represents a vector in a two axis coordinate system.
 * 
 * @note	This class uses a two axis coordinate system:
 * 			<dl><dt>X-axis</dt><dd>Horizontal axis, positive values at the right.</dd>
 * 			<dt>Y-axis</dt><dd>Vertical axis, positive values downward.</dd></dl>
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 1.0
 */
public class Vector {

	public static final Vector ZERO = new Vector(0, 0);

	public Vector(long x, long y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Get the X-coordinate of this vector.
	 */
	@Basic
	@Immutable
	public long getX() {
		return x;
	}

	final long x;

	/**
	 * Get the Y-coordinate of this vector.
	 */
	@Basic
	@Immutable
	public long getY() {
		return y;
	}

	final long y;

	/**
	 * Add a vector to this vector.
	 * 
	 * @param vector
	 * 			The vector to add to this vector.
	 * 
	 * @effect	The X- and Y-coordinates of the given vector are added to this vector.
	 * 			| add(vector.getX(), vector.getY())
	 * 
	 */
	public Vector add(Vector vector) {
		return add(vector.getX(), vector.getY());
	}

	/**
	 * Add the given amounts to the X- and Y-coordinates of this vector, respectively.
	 * 
	 * @param dx
	 * 			The amount to add to the X-coordinate.
	 * @param dy
	 * 			The amount to add to the Y-coordinate.
	 * 
	 * @return  dx is added to the X-coordinate of the resulting vector.
	 * 			| result.getX() == this.getX() + dx
	 * @return	dy is added to the Y-coordinate of the resulting vector.
	 * 			| result.getY() == this.getY() + dy
	 * 		
	 */
	public Vector add(long dx, long dy) {
		long x = getX() + dx;
		long y = getY() + dy;
		return new Vector(x, y);
	}

	/**
	 * Subtract a vector from this vector.
	 * 
	 * @param vector
	 * 			The vector to subtract from this vector.
	 * 
	 * @effect	The X- and Y-coordinates of the given vector are subtracted to this vector.
	 * 			| substract(vector.getX(), vector.getY())
	 */
	public Vector subtract(Vector vector) {
		return subtract(vector.getX(), vector.getY());
	}

	/**
	 * Subtract the given amounts from the X- and Y-coordinates of this vector, respectively.
	 * 
	 * @param dx
	 * 			The amount to subtract from the X-coordinate.
	 * @param dy
	 * 			The amount to subtract from the Y-coordinate.
	 * 
	 * @return  dx is subtracted from the X-coordinate of the resulting vector.
	 * 			| result.getX() == this.getX() - dx
	 * @return	dy is subtracted from the Y-coordinate of the resulting vector.
	 * 			| result.getY() == this.getY() - dy
	 */
	public Vector subtract(long dx, long dy) {
		return add(-dx, -dy);
	}

	/**
	 * Get the Manhattan distance from this vector to the origin of the coordinate system.
	 * @return	The result is the sum of the absolute values of this vector's coordinates.
	 * 			| result == Math.abs(getX()) + Math.abs(getY())
	 */
	public long manhattanDistance() {
		return Math.abs(getX()) + Math.abs(getY());
	}

	/**
	 * Check whether this vector is oriented horizontally.
	 * 
	 * @return  True if this vector has a zero Y-coordinate.
	 * 			| result == (getY() == 0)
	 */
	public boolean isHorizontal() {
		return getY() == 0;
	}

	/** 
	 * Check whether this vector is oriented vertically.
	 * 
	 * @return  True if this vector has a zero X-coordinate.
	 * 			| result == (getX() == 0)
	 *
	 */
	public boolean isVertical() {
		return getX() == 0;
	}

	/**
	 * Rotate this vector clockwise by 90 degrees.
	 * 
	 * @return	| result.getX() = this.getY()
	 * 			| result.getY() = -this.getX()
	 */
	public Vector rotateClockwise() {
		return new Vector(getY(), -getX());
	}

	/**
	 * Rotate this vector counterclockwise by 90 degrees.
	 * 
	 * @return	| result.getX() = -this.getY()
	 * 			| result.getY() = this.getX()
	 */
	public Vector rotateCounterClockwise() {
		return new Vector(-getY(), getX());
	}

	/**
	 * Check whether this vector points upward.
	 * 
	 * @return True if this vector has a negative Y-coordinate.
	 * 			| result == (getY() < 0)
	 */
	public boolean isUp() {
		return getY() < 0;
	}

	/**
	 * Check whether this vector points downward.
	 * 
	 * @return True if this vector has a positive Y-coordinate.
	 * 			| result == (getY() > 0)
	 */
	public boolean isDown() {
		return getY() > 0;
	}

	/**
	 * Check whether this vector points to the left.
	 * 
	 * @return True if this vector has a negative X-coordinate.
	 * 			| result == (getX() < 0)
	 */
	public boolean isLeft() {
		return getX() < 0;
	}

	/**
	 * Check whether this vector points to the right.
	 * 
	 * @return True if this vector has a positive X-coordinate.
	 * 			| result == (getX() > 0)
	 */
	public boolean isRight() {
		return getX() > 0;
	}

	/**
	 * Two vectors are equal if and only if their respective coordinates are equal.
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Vector))
			return false;
		Vector v = (Vector) o;
		return getX() == v.getX() && getY() == v.getY();
	}

	@Override
	public int hashCode() {
		int h = new Long(getX()).hashCode();
		h += new Long(getY()).hashCode() * 37;
		return h;
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
}
