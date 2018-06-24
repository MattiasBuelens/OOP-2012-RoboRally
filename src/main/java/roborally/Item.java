package roborally;

import be.kuleuven.cs.som.annotate.*;

/**
 * Represents an item in a game of RoboRally.
 * 
 * <p>An item is a piece which can be picked up, used and dropped by a robot.</p>
 * 
 * @invar	The weight of this item is valid.
 * 			| isValidWeight(weight)
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public abstract class Item extends Piece {

	/**
	 * Create a new item with the given weight.
	 * 
	 * @param weight
	 *			The weight of the new item.
	 *
	 * @post	The new item's weight equals
	 * 			the absolute value of the given weight.
	 *			| new.getWeight() == Math.abs(weight)
	 */
	protected Item(int weight) {
		if (!isValidWeight(weight))
			weight = Math.abs(weight);
		this.weight = weight;
	}

	/*
	 * Position
	 */

	/**
	 * @return	False if the other piece is not effective.
	 * 			| if (piece == null)
	 * 			|   result == false
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

		// Items can share their position with other items
		if (piece instanceof Item)
			return true;

		return piece.canSharePositionWith(this);
	}

	/*
	 * Weight
	 */

	/**
	 * Get the weight of this item.
	 */
	@Basic
	@Immutable
	public int getWeight() {
		return weight;
	}

	private final int weight;

	/**
	 * Check whether the given weight is valid for this item.
	 * 
	 * @param weight
	 * 			The weight to validate
	 * @return	True if the weight is positive.
	 * 			| result == (weight >= 0)
	 */
	public boolean isValidWeight(int weight) {
		return weight >= 0;
	}

	/*
	 * Using
	 */

	/**
	 * Use this item on a given robot.
	 * 
	 * @param robot
	 * 			The robot to use this item on.
	 */
	public abstract void use(Robot robot);

	@Override
	public String toString() {
		String result = super.toString();
		result += String.format(" weighing %d grams", getWeight());
		return result;
	}

}
