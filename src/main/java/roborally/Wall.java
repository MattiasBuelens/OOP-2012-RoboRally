package roborally;

/**
 * Represents a wall in a game of RoboRally.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class Wall extends Piece {

	/*
	 * Position
	 */

	/**
	 * @return	True if and only if the other piece equals this wall.
	 * 			| result == (piece == this)
	 */
	@Override
	public boolean canSharePositionWith(Piece piece) {
		// Walls cannot share their positions with other pieces
		return (piece == this);
	}

}
