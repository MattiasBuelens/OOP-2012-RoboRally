package roborally;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions thrown when an invalid position was given
 * to a piece in a game of RoboRally.
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
public class InvalidPositionException extends Exception {

	public InvalidPositionException(Board board, Vector position) {
		this.board = board;
		this.position = position;
	}

	public InvalidPositionException(Board board, long x, long y) {
		this(board, new Vector(x, y));
	}

	/**
	 * Get the invalid position.
	 */
	@Basic
	@Immutable
	public Vector getPosition() {
		return position;
	}

	final Vector position;

	/**
	 * Get the board.
	 */
	@Basic
	@Immutable
	public Board getBoard() {
		return board;
	}

	final Board board;

	public String getMessage() {
		return String.format("Invalid position %s on %s", getPosition(), getBoard());
	}

	private static final long serialVersionUID = 1L;

}
