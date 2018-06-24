package roborally;

import java.util.*;

import roborally.util.AbstractIterator;
import roborally.util.FilteredIterable;
import roborally.util.FilteredIterator;
import roborally.util.Predicate;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A board in a game of RoboRally.
 * 
 * <p>A board has a width and height and can contain pieces.</p>
 * 
 * @invar	The board has a valid width and height.
 * 			| isValidWidth(getWidth()) && isValidHeight(getHeight())
 * @invar 	The board has proper pieces.
 * 			| hasProperPieces()
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
public class Board extends Terminatable implements FilteredIterable<Piece> {

	/**
	 * Create a new board.
	 * 
	 * @param width
	 *			The width of this board.
	 * @param height
	 *			The height of this board.
	 * 
	 * @post	The width of the new board equals the given width. 
	 *			| new.getWidth() == width
	 * @post	The height of the new board equals the given height. 
	 *			| new.getHeight() == height
	 * @throws	InvalidSizeException
	 *			If the given width or height is not valid. 
	 *			| !isValidWidth(width) || !isValidHeight(height)
	 */
	public Board(long width, long height) throws InvalidSizeException {
		if (!isValidWidth(width) || !isValidHeight(height))
			throw new InvalidSizeException(width, height);
		this.width = width;
		this.height = height;
	}

	/*
	 * Size
	 */

	/**
	 * Get the width of this board.
	 */
	@Basic
	public long getWidth() {
		return width;
	}

	/**
	 * Variable registering the width of this board.
	 */
	private final long width;

	/**
	 * Check whether the given width is valid for a board.
	 * 
	 * @param width
	 *			The width to validate.
	 * 
	 * @return	True if the width is positive and less than or equal to the
	 *			maximum width. 
	 *			| result == (width > 0 && width <= getMaximumWidth())
	 */
	public static boolean isValidWidth(long width) {
		return width > 0 && width <= getMaximumWidth();
	}

	/**
	 * Get the maximum width of a board.
	 */
	@Basic
	@Immutable
	public static long getMaximumWidth() {
		return maximumWidth;
	}

	/**
	 * Variable registering the maximum width of a board.
	 */
	private static final long maximumWidth = Long.MAX_VALUE;

	/**
	 * Get the height of this board.
	 */
	@Basic
	public long getHeight() {
		return height;
	}

	/**
	 * Variable registering the height of this board.
	 */
	private final long height;

	/**
	 * Check whether the given height is valid for a board.
	 * 
	 * @param height
	 *			The height to validate.
	 * 
	 * @return	True if the height is positive and less than or equal to the
	 *			maximum height. 
	 *			| result == (height > 0 && height <= getMaximumHeight())
	 */
	public static boolean isValidHeight(long height) {
		return height > 0 && height <= getMaximumHeight();
	}

	/**
	 * Get the maximum height of a board.
	 */
	@Basic
	@Immutable
	public static long getMaximumHeight() {
		return maximumHeight;
	}

	/**
	 * Variable registering the maximum height of a board.
	 */
	private static final long maximumHeight = Long.MAX_VALUE;

	/*
	 * Pieces
	 */

	/**
	 * Map mapping positions to sets of pieces placed on this board.
	 * 
	 * @invar	The map of piece sets is effective.
	 * 			| pieces != null
	 * @invar	Each set of pieces in the map is effective
	 * 			and not empty.
	 * 			| for each pieceSet in pieces.values() :
	 * 			|   pieceSet != null && !pieceSet.isEmpty()
	 * @invar	This board can have each piece in the map
	 * 			and can have each piece at its position.
	 * 			| for each pieceSet in pieces.values() :
	 * 			|   for each piece in pieceSet :
	 * 			|      canHaveAsPiece(piece)
	 * 			|       && canHavePieceAt(piece, piece.getPosition())
	 * @note	It is not guaranteed that each piece in the map
	 * 			has this board as its board. It is the responsibility
	 * 			of Piece to maintain this relationship.
	 */
	private final Map<Vector, Set<Piece>> pieces = new HashMap<Vector, Set<Piece>>();

	/**
	 * Check whether this board has proper pieces.
	 * 
	 * @return	True if and only if this board can have each of
	 * 			its pieces at their position and each piece
	 * 			has this board as its board.
	 * 			| for each piece in getPieces() :
	 * 			|   canHaveAsPiece(piece)
	 * 			|    && piece.getBoard() == this
	 * 			|    && canHavePieceAt(piece, piece.getPosition())
	 */
	public boolean hasProperPieces() {
		for (Set<Piece> piecesAtPosition : pieces.values()) {
			for (Piece piece : piecesAtPosition) {
				if (!canHaveAsPiece(piece))
					return false;
				if (piece.getBoard() != this)
					return false;
				if (!canHavePieceAt(piece, piece.getPosition()))
					return false;
			}
		}
		return true;
	}

	/**
	 * Check whether this board can have the given piece
	 * as one of its pieces.
	 * 
	 * @param piece
	 * 			The piece to check.
	 * 
	 * @return	True if and only if the piece is effective
	 * 			and the piece can have this board as its board. 
	 *			| result == (piece != null && piece.canHaveAsBoard(this))
	 */
	public boolean canHaveAsPiece(Piece piece) {
		return piece != null && piece.canHaveAsBoard(this);
	}

	/**
	 * Get a set of all the pieces placed on this board.
	 * 
	 * @return	The resulting set contains all pieces with this board
	 * 			as their board.
	 * 			| result == {piece:Piece | piece.getBoard() == this}
	 */
	public Set<Piece> getPieces() {
		return getPieces(Piece.class);
	}

	/**
	 * Get a set of all the pieces placed on this board
	 * which are instances of a given type.
	 * 
	 * @param pieceType
	 * 			The type of pieces to return.
	 * 
	 * @return	The resulting set contains all pieces which are
	 * 			instances of the given type and have this board
	 * 			as their board.
	 * 			| result ==
	 * 			|	{piece:Piece | pieceType.isInstance(piece)
	 * 			|                   && piece.getBoard() == this}
	 */
	public <T extends Piece> Set<T> getPieces(Class<T> pieceType) {
		Set<T> typedPieces = new HashSet<T>();
		for (Set<Piece> piecesAtPosition : pieces.values()) {
			for (Piece piece : piecesAtPosition) {
				if (pieceType.isInstance(piece)) {
					typedPieces.add(pieceType.cast(piece));
				}
			}
		}
		return typedPieces;
	}

	/**
	 * Get a set of all the pieces on this board at the given position.
	 * 
	 * @param position
	 * 			The position to find pieces at.
	 * 
	 * @return	The resulting set contains all pieces with this board
	 * 			as their board and the given position as their position.
	 * 			| result == {piece:Piece | piece.getBoard() == this
	 *          |                           && piece.getPosition().equals(position)}
	 */
	public Set<Piece> getPiecesAt(Vector position) {
		// Pieces at same position
		Set<Piece> piecesAtPosition = pieces.get(position);

		// If set is not effective, return empty set
		if (piecesAtPosition == null) {
			return Collections.emptySet();
		}

		// Return unmodifiable view of set
		return Collections.unmodifiableSet(piecesAtPosition);
	}

	/**
	 * Get a set of all the pieces on this board at the given position
	 * which are instances of a given type.
	 * 
	 * @param position
	 * 			The position to find pieces at.
	 * @param pieceType
	 * 			The type of pieces to return.
	 * 
	 * @return	The resulting set contains all pieces which are
	 * 			instances of the given type, have this board as
	 * 			their board and have the given position as
	 * 			their position.
	 * 			| result == {piece:Piece | pieceType.isInstance(piece)
	 * 			|                           && piece.getBoard() == this
	 *          |                           && piece.getPosition().equals(position)}
	 */
	public <T extends Piece> Set<T> getPiecesAt(Vector position, Class<T> pieceType) {
		Set<T> typedPieces = new HashSet<T>();
		for (Piece piece : getPiecesAt(position)) {
			if (pieceType.isInstance(piece)) {
				typedPieces.add(pieceType.cast(piece));
			}
		}
		return typedPieces;
	}

	/**
	 * Check whether this board has the given piece
	 * as one of its pieces.
	 * 
	 * @param piece
	 * 			The piece to check.
	 * 
	 * @return	False if the piece is not effective.
	 * 			| if (piece == null)
	 * 			|   result == false
	 * @return	True if this board has the given piece
	 * 			at the piece's position.
	 * 			| result == getPiecesAt(piece.getPosition()).contains(piece)
	 */
	public boolean hasAsPiece(Piece piece) {
		if (piece == null)
			return false;

		return getPiecesAt(piece.getPosition()).contains(piece);
	}

	/**
	 * Check whether this board has pieces at the given position.
	 * 
	 * @param position
	 * 			The position to check.
	 * 
	 * @return	True if and only if the set of pieces at the given position
	 * 			is not empty.
	 * 			| result == !getPiecesAt(position).isEmpty()
	 */
	public boolean hasPiecesAt(Vector position) {
		return !getPiecesAt(position).isEmpty();
	}

	/**
	 * Check whether this board can have the given piece
	 * at the given position on the board.
	 * 
	 * @param piece
	 *			The piece to check.
	 * @param position
	 * 			The position where the piece would be placed.
	 * 
	 * @return	False if this board is terminated.
	 *			| if (isTerminated())
	 *			|   result == false
	 * @return	False if the piece is not effective or is terminated.
	 * 			| else if (piece == null || piece.isTerminated())
	 * 			|   result == false
	 * @return  Otherwise, true if and only if the piece can share its position
	 * 			with all pieces at the given position.
	 *			| else
	 *			|   result == piece.canSharePositionWith(getPiecesAt(position))
	 */
	public boolean canHavePieceAt(Piece piece, Vector position) {
		if (isTerminated())
			return false;

		if (piece == null || piece.isTerminated())
			return false;

		return piece.canSharePositionWith(getPiecesAt(position));
	}

	/**
	 * Check whether this board can add the given piece
	 * at the given position on the board.
	 * 
	 * @param piece
	 *			The piece to check.
	 * @param position
	 * 			The position to place the piece at.
	 * 
	 * @return	False if the piece is already placed on this board.
	 * 			| if (hasAsPiece(piece))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the board can have the piece
	 * 			at the given position.
	 *			| else
	 *			|   result == canHavePieceAt(piece, position)
	 */
	public boolean canAddPieceAt(Piece piece, Vector position) {
		// Cannot contain the same piece twice
		if (hasAsPiece(piece))
			return false;
		// Check whether the piece can be at the given position
		return canHavePieceAt(piece, position);
	}

	/**
	 * Add the given piece to this board.
	 * 
	 * @param piece
	 * 			The piece to add.
	 * 
	 * @pre		The given piece can be added to this board.
	 *			| canAddAsPiece(piece)
	 * 
	 * @post	This board has the given piece
	 * 			as one of its pieces.
	 *			| new.hasAsPiece(piece)
	 */
	@Raw
	public void addAsPiece(@Raw Piece piece) {
		assert canAddAsPiece(piece);

		// Add piece
		Vector position = piece.getPosition();
		Set<Piece> piecesAtPosition = pieces.get(position);
		if (piecesAtPosition == null) {
			piecesAtPosition = new HashSet<Piece>();
		}
		piecesAtPosition.add(piece);
		pieces.put(position, piecesAtPosition);
	}

	/**
	 * Check whether the given piece can be added to this board.
	 * 
	 * @param piece
	 * 			The piece to be added.
	 * 
	 * @return	False if the given piece is not effective
	 * 			or already placed on a board.
	 *			| if (piece == null || piece.isPlaced())
	 *			|   result == false
	 * @return	False if the given piece doesn't have
	 * 			a valid position.
	 *			| else if (!isValidPosition(piece.getPosition()))
	 *			|	result == false
	 * @return	Otherwise, true if and only if the piece
	 * 			can be added at its position on this board.
	 *			| else
	 *			|   result == canAddPieceAt(piece, piece.getPosition())
	 */
	public boolean canAddAsPiece(@Raw Piece piece) {
		if (piece == null || piece.isPlaced())
			return false;

		Vector position = piece.getPosition();
		if (!isValidPosition(position))
			return false;

		return canAddPieceAt(piece, position);
	}

	/**
	 * Remove the given piece from this board.
	 * 
	 * @param piece
	 * 			The piece to remove.
	 * 
	 * @pre		The given piece can be removed from this board.
	 * 			| canRemoveAsPiece(piece)
	 * 
	 * @post	This board no longer has the given piece
	 * 			as one of its pieces.
	 * 			| !new.hasAsPiece(piece)
	 */
	@Raw
	public void removeAsPiece(@Raw Piece piece) {
		assert canRemoveAsPiece(piece);

		// Remove piece
		Set<Piece> piecesAtPosition = pieces.get(piece.getPosition());
		piecesAtPosition.remove(piece);
		// Remove set from map if empty
		if (piecesAtPosition.isEmpty()) {
			pieces.remove(piece.getPosition());
		}
	}

	/**
	 * Check whether the given piece can be removed from this board.
	 * 
	 * @param piece
	 * 			The piece to be removed.
	 * 
	 * @return	False if this board is terminated.
	 *			| if (isTerminated())
	 *			|   result == false
	 * @return	False if the given piece is null
	 * 			or still placed on a board.
	 *			| if (piece == null || piece.isPlaced())
	 *			|   result == false
	 * @return	False if the given piece doesn't have
	 * 			a valid position.
	 *			| if (!isValidPosition(piece.getPosition()))
	 *			|	result == false
	 * @return	Otherwise, true if and only if this board
	 * 			contains the given piece.
	 *			| else
	 *			|   result == hasAsPiece(piece)
	 */
	public boolean canRemoveAsPiece(@Raw Piece piece) {
		if (isTerminated())
			return false;

		if (piece == null || piece.isPlaced())
			return false;

		if (!isValidPosition(piece.getPosition()))
			return false;

		return hasAsPiece(piece);
	}

	@Override
	public Iterator<Piece> iterator() {
		return new Itr();
	}

	@Override
	public Iterator<Piece> iterator(Predicate<? super Piece> filter) {
		return new FilteredIterator<Piece>(iterator(), filter);
	}

	/*
	 * Merging
	 */

	/**
	 * Merge this board with the given board.
	 * 
	 * <p>All pieces from the given board are either moved to this board,
	 * or terminated if they cannot be moved.</p>
	 * 
	 * @param board
	 * 			The board to merge.
	 * 
	 * @post	Each piece on the given board is either moved to this board,
	 * 			or it is terminated if it cannot be moved to this board.
	 * 			| for each piece in board.getPieces() :
	 * 			|   ( (new piece).getBoard() == this
	 * 			|       && (new piece).getPosition().equals(piece.getPosition()) )
	 * 			|    || ( (new piece).isTerminated() )
	 * @effect	The given board is terminated.
	 * 			| board.terminate()
	 *
	 * @throws	IllegalStateException
	 * 			If this board is already terminated.
	 *			| isTerminated()
	 * @throws	IllegalArgumentException
	 * 			If the given board is not effective or is terminated.
	 *			| board == null || board.isTerminated()
	 */
	public void merge(Board board) throws IllegalStateException, IllegalArgumentException {
		if (isTerminated())
			throw new IllegalStateException("Cannot merge when this board is terminated.");
		if (board == null || board.isTerminated())
			throw new IllegalArgumentException("Board must be effective and not terminated.");

		// Collect all pieces on other board
		Set<Piece> boardPieces = board.getPieces();
		for (Piece piece : boardPieces) {
			// Position of this piece on the other board
			Vector position = piece.getPosition();
			// Remove the piece from its board
			piece.removeFromBoard();
			try {
				// Try to place the piece on this board
				piece.placeOnBoard(this, position);
			} catch (Exception e) {
				// If it cannot be placed, terminate it
				piece.terminate();
			}
		}
		// Terminate the board
		board.terminate();
	}

	/*
	 * Board positions
	 */

	/**
	 * Check whether the given position is a valid position on this board.
	 * 
	 * @param position
	 * 			The position to validate.
	 * 
	 * @return	False if the given position is not effective.
	 * 			| if (position == null)
	 * 			|   result == false
	 * @return	False if the given position's X-coordinate is negative
	 * 			or exceeds this board's width.
	 * 			| else if (position.getX() < 0 || position.getX() >= getWidth())
	 * 			|   result == false
	 * @return	False if the given position's Y-coordinate is negative
	 * 			or exceeds this board's height.
	 * 			| else if (position.getY() < 0 || position.getY() >= getHeight())
	 * 			|   result == false
	 * @return	Otherwise, true.
	 */
	public boolean isValidPosition(Vector position) {
		if (position == null)
			return false;

		long x = position.getX(), y = position.getY();
		return (0 <= x && x < getWidth()) && (0 <= y && y < getHeight());
	}

	/**
	 * Get the next position from a given position in a given direction. If no
	 * valid next position exists, null is returned.
	 * 
	 * @param position
	 *            The position from where to start.
	 * @param orientation
	 *            The orientation in which to move.
	 * 
	 * @return The next position equals the given position with the unit vector
	 *         associated with the given orientation added to it. If this next
	 *         position is invalid, null is returned instead.
	 *         | let 
	 *         |   nextPosition = position.add(orientation.getVector())
	 *         |
	 *         | if (isValidPosition(nextPosition))
	 *         |   result == nextPosition
	 *         | else
	 *         |   result == null
	 */
	public Vector getNextPosition(Vector position, Orientation orientation) {
		Vector nextPosition = position.add(orientation.getVector());
		return isValidPosition(nextPosition) ? nextPosition : null;
	}

	/**
	 * @effect	All the pieces placed on this board are terminated.
	 * 			| for each piece in getPieces() :
	 * 			|   piece.terminate()
	 */
	@Override
	public void terminate() {
		// Terminate all pieces
		for (Piece piece : getPieces()) {
			piece.terminate();
		}

		super.terminate();
	}

	/**
	 * Get a random position on this board.
	 * 
	 * @return	The resulting position is a valid position
	 * 			on this board.
	 * 			| isValidPosition(result)
	 */
	public Vector getRandomPosition() {
		roborally.util.Random random = new roborally.util.Random();
		Long x = random.nextLong(getWidth());
		Long y = random.nextLong(getHeight());
		return new Vector(x, y);
	}

	/**
	 * Get a random position for the given piece
	 * on this board.
	 * 
	 * @param piece
	 * 			The piece to find a position for.
	 * 
	 * @return	This board can have the given piece
	 * 			at the resulting position.
	 * 			| canHavePieceAt(piece, result);
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given piece is not effective.
	 * 			| piece == null
	 * @throws	IllegalArgumentException
	 * 			If the given piece is terminated.
	 * 			| piece.isTerminated()
	 * 
	 * @note	Caution must be taken when calling
	 * 			this method. If the board has pieces on
	 * 			every position and the given piece cannot
	 * 			share its position with any of the pieces
	 * 			on the board, this method will end up in
	 * 			an infinite loop.
	 */
	public Vector getRandomPosition(Piece piece) {
		if (piece == null)
			throw new IllegalArgumentException("Piece must be effective.");
		if (piece.isTerminated())
			throw new IllegalArgumentException("Piece must not be terminated.");

		Vector pos;
		do {
			pos = getRandomPosition();
		} while (!canHavePieceAt(piece, pos));

		return pos;
	}

	private class Itr extends AbstractIterator<Piece> {

		private Itr() {
			setItr = pieces.values().iterator();
		}

		/**
		 * Variable representing the piece sets iterator.
		 */
		private final Iterator<Set<Piece>> setItr;

		/**
		 * Variable representing the current piece iterator.
		 */
		private Iterator<Piece> pieceItr;

		@Override
		protected Piece computeNext() {
			// If there is no piece iterator yet
			// or the piece iterator is at its end
			if (pieceItr == null || !pieceItr.hasNext()) {
				// If the set iterator is not at its end
				if (setItr.hasNext()) {
					// Get the next piece iterator
					pieceItr = setItr.next().iterator();
				} else {
					// No more pieces to return
					return endOfData();
				}
			}
			// Get the next piece to return
			return pieceItr.next();
		}

	}

}
