package roborally;

import be.kuleuven.cs.som.annotate.*;

/**
 * A class of exceptions thrown when an invalid position was given
 * to a piece in a game of RoboRally.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
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
	public final Vector getPosition() {
		return position;
	}

	final Vector position;

	/**
	 * Get the board.
	 */
	@Basic
	@Immutable
	public final Board getBoard() {
		return board;
	}

	final Board board;

	public String getMessage() {
		return String.format("Invalid position %s on %s", getPosition(), getBoard());
	}

	private static final long serialVersionUID = 1L;

}
