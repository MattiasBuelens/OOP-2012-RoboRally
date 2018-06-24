package roborally;

/**
 * A wall in a game of RoboRally.
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
public class Wall extends Piece {

	/**
	 * Create a new wall.
	 */
	public Wall() {
	}

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

	/*
	 * Shooting
	 */

	/**
	 * @effect	A hit has no effect on a wall.
	 */
	@Override
	public void hit() {
	}

}
