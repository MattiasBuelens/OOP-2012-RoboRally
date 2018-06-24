package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A piece in a game of RoboRally.
 * 
 * <p>A piece can be placed on a board at a certain position.</p>
 * 
 * @invar	The piece's position is valid.
 * 			| isValidPosition(getPosition())
 * 
 * @invar	The piece's board is valid.
 * 			| canHaveAsBoard(getBoard())
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
public abstract class Piece extends Terminatable {

	/*
	 * Position
	 */

	/**
	 * Get the position of this piece.
	 */
	@Basic
	@Raw
	public Vector getPosition() {
		return position;
	}

	/**
	 * Set the position of this piece.
	 *
	 * @param position
	 *			The new position.
	 *
	 * @post	The new position equals the given position.
	 *			| new.getPosition() == position
	 * @throws	InvalidPositionException
	 *			If the given position is not valid.
	 *			| !isValidPosition(position)
	 * @see #isValidPosition(Vector)
	 */
	@Raw @Model
	void setPosition(Vector position) {
		this.position = position;
	}

	/**
	 * Check whether the given position is valid for this piece.
	 *
	 * @param position
	 *			The position to validate.
	 *
	 * @return	If this piece is placed on a board,
	 * 			the position must be valid on that board.
	 * 			| if (isPlaced())
	 *			|   result == getBoard().isValidPosition(position)
	 * @return	If this piece is not placed on any board,
	 * 			the position must be null.
	 * 			| else
	 * 			|   result == (position == null)
	 */
	public boolean isValidPosition(Vector position) {
		if (!isPlaced())
			return position == null;
		return getBoard().isValidPosition(position);
	}

	private Vector position;

	/*
	 * Board
	 */

	/**
	 * Get the board of this piece.
	 */
	@Basic
	@Raw
	public Board getBoard() {
		return board;
	}

	/**
	 * Set the board of this piece.
	 * 
	 * @param board
	 * 			The board.
	 * 
	 * @post	The new board equals the given board.
	 * 			| new.getBoard() == board
	 * @throws	IllegalArgumentException
	 * 			If this piece cannot have the given board as its board.
	 * 			| !canHaveAsBoard(board)
	 */
	@Raw
	@Model
	void setBoard(Board board) throws IllegalArgumentException {
		if (!canHaveAsBoard(board))
			throw new IllegalArgumentException("Invalid board for this piece.");
		this.board = board;
	}

	/**
	 * Check whether this piece can have the given board as its board.
	 * 
	 * @param board
	 * 			The board to validate.
	 * 
	 * @return	If this piece is terminated, true if and only if
	 * 			the given board is not effective.
	 * 			| if (isTerminated())
	 * 			|   result == (board == null)
	 * @return	True if the given board is not effective.
	 * 			| else if (board == null)
	 * 			|   result == true
	 * @return	Otherwise, true if and only if the given board is not terminated.
	 * 			| else
	 * 			|   result == !board.isTerminated()
	 */
	public boolean canHaveAsBoard(Board board) {
		// Terminated pieces cannot be put on any board
		if (isTerminated())
			return board == null;

		// Pieces can have no board
		if (board == null)
			return true;

		// Pieces cannot be placed on terminated boards
		return !board.isTerminated();
	}

	private Board board;

	/**
	 * Check whether this piece is placed on a board.
	 * 
	 * @return	True if and only if this piece's board is effective.
	 * 			| result == (getBoard() != null)
	 */
	@Raw
	public boolean isPlaced() {
		return (getBoard() != null);
	}

	/**
	 * Place this piece on the given board at the given position.
	 * 
	 * @param board
	 * 			The board to place this piece on.
	 * @param position
	 * 			The position to place this piece at.
	 * 
	 * @effect	The position of this piece is set to the given position.
	 * 			| setPosition(position)
	 * @effect	This piece is added as a piece of the given board.
	 * 			| board.addAsPiece(this)
	 * @effect	The board of this piece is set to the given board.
	 * 			| setBoard(board)
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given board is not effective.
	 * 			| board == null
	 * @throws	IllegalStateException
	 * 			If this piece is already placed on a board.
	 * 			| isPlaced()
	 * @throws	InvalidPositionException
	 * 			If the given position is not a valid position on the given board.
	 * 			| !board.isValidPosition(position)
	 * @throws	IllegalArgumentException
	 * 			If the given board cannot add this piece at the given position.
	 * 			| !board.canAddPieceAt(this, position)
	 */
	public void placeOnBoard(Board board, Vector position) throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		if (board == null)
			throw new IllegalArgumentException("Board must be effective.");
		if (!canHaveAsBoard(board))
			throw new IllegalArgumentException("Invalid board for this piece.");
		if (isPlaced())
			throw new IllegalStateException("Piece cannot be placed on two boards at the same time.");

		if (!board.isValidPosition(position))
			throw new InvalidPositionException(board, position);
		if (!board.canAddPieceAt(this, position))
			throw new IllegalArgumentException("Piece cannot share position with an already placed piece.");

		// Setting the position causes this piece to become raw,
		// since it has a position but no board
		setPosition(position);
		// Create association on the board
		board.addAsPiece(this);
		// Create association on this piece,
		// thus restoring the invariants
		setBoard(board);
	}

	/**
	 * Remove this piece from its board.
	 * 
	 * @effect	The position of this piece is set to
	 * 			the null reference.
	 * 			| setPosition(null)
	 * @effect	This piece is removed from this piece's board.
	 * 			| board.removeAsPiece(this)
	 * @post	The piece is no longer placed on any board.
	 * 			| !new.isPlaced()
	 * 
	 * @throws	IllegalStateException
	 * 			If this piece is not placed on a board.
	 * 			| !isPlaced()
	 */
	public void removeFromBoard() throws IllegalStateException {
		if (!isPlaced())
			throw new IllegalStateException("Piece cannot be removed when it is not placed.");

		Board formerBoard = getBoard();
		// Destroy association on this piece
		// This causes this piece to become raw,
		// since it has a position but no board
		setBoard(null);
		// Destroy association on the board
		formerBoard.removeAsPiece(this);
		// Remove position on this piece,
		// thus restoring the invariants
		setPosition(null);
	}

	/**
	 * Move this piece to the given position on its board.
	 * 
	 * @param position
	 * 			The position to move this piece to.
	 * 
	 * @throws	IllegalStateException
	 * 			If this piece is not placed on a board.
	 * 			| !isPlaced()
	 * @throws	IllegalArgumentException
	 * 			If this piece cannot be moved to the given position.
	 * 			| !canMoveTo(position)
	 */
	public void moveOnBoard(Vector position) throws IllegalStateException, IllegalArgumentException {
		if (!canMoveTo(position))
			throw new IllegalArgumentException("Piece cannot be moved to the given position.");

		Board board = getBoard();
		// Remove from board
		removeFromBoard();
		// Place on board at new position
		try {
			placeOnBoard(board, position);
		} catch (InvalidPositionException cannotHappen) {
			// Checked in canMoveTo(position)
		}
	}

	/**
	 * Check whether this piece can be moved to the given position.
	 * 
	 * @param position
	 * 			The position to move this piece to.
	 * 
	 * @return	False if this piece is not placed on a board.
	 * 			| if (!isPlaced())
	 * 			|   result == false
	 * @return	False if the given position is not valid for this piece's board.
	 * 			| else if (!getBoard().isValidPosition(position))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this piece's board can have
	 * 			this piece at the given position.
	 * 			| else
	 * 			|   result == getBoard().canHavePieceAt(this, position)
	 */
	public boolean canMoveTo(Vector position) {
		// Piece is placed on a board
		if (!isPlaced())
			return false;
		Board board = getBoard();
		// Position is valid on this piece's board
		if (!board.isValidPosition(position))
			return false;
		// Board can have this piece at the given position
		return board.canHavePieceAt(this, position);
	}

	/**
	 * Check whether this piece shares its position with a given piece.
	 * 
	 * @param piece
	 * 			The other piece.
	 * 
	 * @return	False if this piece is not placed on a board.
	 * 			| if (!isPlaced())
	 * 			|   result == false
	 * @return	False if this piece's board differs from the other piece's board.
	 * 			| else if (getBoard() != piece.getBoard())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this piece's position
	 * 			equals the other piece's position.
	 * 			| else
	 * 			|   result == getPosition().equals(piece.getPosition())
	 */
	public boolean sharesPositionWith(Piece piece) {
		// Piece must be placed on a board
		if (!isPlaced())
			return false;
		// Pieces must have same board
		if (this.getBoard() != piece.getBoard())
			return false;
		// Pieces must have same position
		return this.getPosition().equals(piece.getPosition());
	}

	/**
	 * Check whether this piece can share its position
	 * with another piece.
	 * 
	 * @param piece
	 * 			The other piece to check.
	 * 
	 * @return	False if the other piece is not effective.
	 * 			| if (piece == null)
	 * 			|   result == false
	 */
	public abstract boolean canSharePositionWith(Piece piece);

	/**
	 * Check whether this piece can share its position
	 * with all pieces in the given collection.
	 * 
	 * @param pieces
	 * 			The pieces to check.
	 * 
	 * @return	True if and only if this piece can share
	 * 			its position with each piece from the given
	 * 			collection.
	 * 			| result ==
	 * 			|   for each piece in pieces :
	 * 			|      this.canSharePositionWith(piece)
	 */
	public boolean canSharePositionWith(Iterable<Piece> pieces) {
		for (Piece piece : pieces) {
			if (!this.canSharePositionWith(piece))
				return false;
		}
		return true;
	}
	
	/*
	 * Shooting
	 */

	/**
	 * React when being damaged by a laser or an explosion.
	 * 
	 * @throws	IllegalStateException
	 * 			If this piece is terminated.
	 * 			| isTerminated()
	 */
	public abstract void hit() throws IllegalStateException;

	/**
	 * @post	The piece is no longer placed on any board.
	 * 			| !new.isPlaced()
	 */
	@Override
	public void terminate() {
		// Remove from board
		if (isPlaced())
			removeFromBoard();

		super.terminate();
	}

	@Override
	public String toString() {
		String result = getClass().getSimpleName();
		if (isPlaced()) {
			result += String.format(" at %s", getPosition());
		}
		return result;
	}
}
