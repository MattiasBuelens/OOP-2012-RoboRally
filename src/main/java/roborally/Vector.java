package roborally;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Value;

/**
 * Represents a vector in a two axis coordinate system.
 * 
 * @note	This class uses a two axis coordinate system:
 * 			<dl><dt>X-axis</dt><dd>Horizontal axis, positive values at the right.</dd>
 * 			<dt>Y-axis</dt><dd>Vertical axis, positive values downward.</dd></dl>
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
@Value
public class Vector {

	/**
	 * The zero vector (0, 0) at the origin of the coordinate system.
	 */
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

	private final long x;

	/**
	 * Get the Y-coordinate of this vector.
	 */
	@Basic
	@Immutable
	public long getY() {
		return y;
	}

	private final long y;

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
	 * Get the Manhattan distance between this vector and the given vector.
	 * @param vector
	 * 			The vector for which to get the distance.
	 * @effect	The result is the Manhattan distance of the relative position vector.
	 * 			| this.subtract(vector).manhattanDistance()
	 */
	public long manhattanDistance(Vector vector) {
		return this.subtract(vector).manhattanDistance();
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
	 * Get a set of all direct neighbours of this vector.
	 * 
	 * @return	The resulting set of neighbours contains all
	 * 			neighbours at a distance of one from this vector.
	 * 			| result == getNeighbours(1)
	 */
	public Set<Vector> getNeighbours() {
		return getNeighbours(1);
	}

	/**
	 * Get a set of all neighbours of this vector
	 * at the given Manhattan distance.
	 * 
	 * @param distance
	 * 			The Manhattan distance between this vector
	 * 			and each neighbour.
	 * 
	 * @return	If the distance is zero, the resulting set
	 * 			is a singleton consisting of this vector.
	 * 			| if(distance == 0)
	 * 			|   result.size() == 1
	 * @return	If the distance is nonzero, the resulting set
	 * 			contains (4 * distance) neighbouring vectors.
	 * 			| else
	 * 			|   result.size() == 4*distance
	 * @return	Each neighbour in the resulting set has the given
	 * 			distance as the Manhattan distance between
	 * 			the neighbour and this vector.
	 * 			| for each neighbour in result :
	 * 			|   this.manhattanDistance(neighbour) == distance
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given distance is strictly negative.
	 * 			| distance < 0
	 */
	public Set<Vector> getNeighbours(long distance) throws IllegalArgumentException {
		if (distance < 0)
			throw new IllegalArgumentException("Distance to neighbour must be positive");
		if (distance == 0)
			return Collections.singleton(this);

		Set<Vector> neighbours = new HashSet<Vector>();
		long y = 0, dy = 1;
		for (long x = -distance; x <= distance; ++x) {
			// Add neighbours
			if (y == 0) {
				neighbours.add(this.add(x, 0));
			} else {
				neighbours.add(this.add(x, y));
				neighbours.add(this.add(x, -y));
			}
			// Step Y
			y += dy;
			// Invert at central axis
			if (y == distance)
				dy *= -1;
		}
		return neighbours;
	}

	/**
	 * @return	True if the given object reference equals this object reference.
	 * 			| if (this == obj)
	 * 			|   result == true
	 * @return	False if the given object is not a vector.
	 * 			| else if (getClass() != obj.getClass())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the respective coordinates are equal.
	 * 			| else
	 * 			|   let
	 * 			|      other = (Vector) obj
	 * 			|   result == (getX() == other.getX()) && (getY() == other.getY())
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Vector other = (Vector) obj;
		return getX() == other.getX() && getY() == other.getY();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 0;
		result = prime * result + Long.valueOf(getX()).hashCode();
		result = prime * result + Long.valueOf(getY()).hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "(" + getX() + ", " + getY() + ")";
	}
}
