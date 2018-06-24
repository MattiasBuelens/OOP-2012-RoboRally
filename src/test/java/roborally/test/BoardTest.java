package roborally.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import roborally.*;

public class BoardTest {

	private Board board_5x5, board_10x10, terminatedBoard, batteryBoard, wallBoard;
	private Piece battery, batteryOnBoard, wallOnBoard;

	@Before
	public void setUp() throws Exception {
		board_5x5 = new Board(5, 5);
		board_10x10 = new Board(10, 10);

		battery = new Battery(2);

		batteryBoard = new Board(5, 5);
		batteryOnBoard = new Battery(2);
		batteryOnBoard.placeOnBoard(batteryBoard, Vector.ZERO);

		wallBoard = new Board(10, 10);
		wallOnBoard = new Wall();
		wallOnBoard.placeOnBoard(wallBoard, new Vector(9, 9));

		terminatedBoard = new Board(5, 5);
		terminatedBoard.terminate();
	}

	@Test(expected = InvalidSizeException.class)
	public void newBoard_InvalidWidth() throws InvalidSizeException {
		new Board(-10, 10);
		fail();
	}

	@Test(expected = InvalidSizeException.class)
	public void newBoard_InvalidHeight() throws InvalidSizeException {
		new Board(10, -10);
		fail();
	}

	@Test
	public void hasProperPieces() {
		assertTrue(board_5x5.hasProperPieces());
		assertTrue(board_10x10.hasProperPieces());
		assertTrue(batteryBoard.hasProperPieces());
		assertTrue(wallBoard.hasProperPieces());
		assertTrue(terminatedBoard.hasProperPieces());
	}

	@Test
	public void canHaveAsPiece_NormalCase() {
		assertTrue(board_10x10.canHaveAsPiece(battery));
	}

	@Test
	public void canHaveAsPiece_NullPiece() {
		assertFalse(board_10x10.canHaveAsPiece(null));
	}

	@Test
	public void canHaveAsPiece_TerminatedPiece() {
		battery.terminate();
		assertFalse(board_10x10.canHaveAsPiece(battery));
	}

	@Test
	public void canHaveAsPiece_TerminatedBoard() {
		assertFalse(terminatedBoard.canHaveAsPiece(battery));
	}

	@Test
	public void canAddAsPiece() {
		// Piece placed on other board
		assertFalse(board_10x10.canAddAsPiece(batteryOnBoard));
		// Piece without position
		assertFalse(board_10x10.canAddAsPiece(battery));
		// Null piece
		assertFalse(board_10x10.canAddAsPiece(null));
		// Terminated board
		assertFalse(terminatedBoard.canAddAsPiece(battery));
		// Terminated piece
		battery.terminate();
		assertFalse(board_10x10.canAddAsPiece(battery));
	}

	@Test
	public void canRemoveAsPiece() {
		// Contained piece but still placed on board
		assertFalse(batteryBoard.canRemoveAsPiece(batteryOnBoard));
		// Piece without position
		assertFalse(batteryBoard.canRemoveAsPiece(battery));
		// Null piece
		assertFalse(batteryBoard.canRemoveAsPiece(null));
		// Terminated board
		assertFalse(terminatedBoard.canRemoveAsPiece(battery));
		// Terminated piece
		battery.terminate();
		assertFalse(batteryBoard.canRemoveAsPiece(battery));
	}

	@Test
	public void getPieces_AllTypes() {
		Set<Piece> pieces = batteryBoard.getPieces();
		assertEquals(1, pieces.size());
		assertTrue(pieces.contains(batteryOnBoard));
	}

	@Test
	public void getPieces_SpecificTypes() {
		Set<Battery> batteries = batteryBoard.getPieces(Battery.class);
		assertEquals(1, batteries.size());
		assertTrue(batteries.contains(batteryOnBoard));

		Set<Wall> walls = wallBoard.getPieces(Wall.class);
		assertEquals(1, walls.size());
		assertTrue(walls.contains(wallOnBoard));

		Set<Wall> noWalls = batteryBoard.getPieces(Wall.class);
		assertTrue(noWalls.isEmpty());
	}

	@Test
	public void getPiecesAt() {
		Set<Piece> pieces = batteryBoard.getPiecesAt(Vector.ZERO);
		assertEquals(1, pieces.size());
		assertTrue(pieces.contains(batteryOnBoard));

		Set<Piece> noPieces = batteryBoard.getPiecesAt(new Vector(2, 2));
		assertTrue(noPieces.isEmpty());
	}

	@Test
	public void hasAsPiece() {
		// battery is not placed on any board
		assertFalse(batteryBoard.hasAsPiece(battery));
		assertFalse(board_5x5.hasAsPiece(battery));
		assertFalse(terminatedBoard.hasAsPiece(battery));

		// batteryOnBoard is placed on batteryBoard
		assertTrue(batteryBoard.hasAsPiece(batteryOnBoard));
		batteryOnBoard.terminate();
		// After termination, batteryOnBoard is removed from batteryBoard
		assertFalse(batteryBoard.hasAsPiece(batteryOnBoard));

		// Null piece
		assertFalse(batteryBoard.hasAsPiece(null));
	}

	@Test
	public void hasPiecesAt_HasPieces() {
		assertTrue(batteryBoard.hasPiecesAt(Vector.ZERO));
	}

	@Test
	public void hasPiecesAt_NoPieces() {
		assertFalse(batteryBoard.hasPiecesAt(new Vector(2, 2)));
	}

	@Test
	public void canHavePieceAt() {
		assertTrue(board_5x5.canHavePieceAt(battery, Vector.ZERO));
		assertTrue(board_5x5.canHavePieceAt(batteryOnBoard, Vector.ZERO));
		assertTrue(board_5x5.canHavePieceAt(wallOnBoard, Vector.ZERO));

		// Cannot have wall on battery
		assertTrue(batteryBoard.canHavePieceAt(battery, Vector.ZERO));
		assertTrue(batteryBoard.canHavePieceAt(batteryOnBoard, Vector.ZERO));
		assertFalse(batteryBoard.canHavePieceAt(wallOnBoard, Vector.ZERO));

		// Cannot have batteries on wall
		assertTrue(wallBoard.canHavePieceAt(wallOnBoard, new Vector(9, 9)));
		assertFalse(wallBoard.canHavePieceAt(battery, new Vector(9, 9)));
		assertFalse(wallBoard.canHavePieceAt(batteryOnBoard, new Vector(9, 9)));

		// Terminated board
		assertFalse(terminatedBoard.canHavePieceAt(battery, Vector.ZERO));

		// Terminated piece
		battery.terminate();
		assertFalse(board_5x5.canHavePieceAt(battery, Vector.ZERO));

		// Null piece
		assertFalse(batteryBoard.canHavePieceAt(null, Vector.ZERO));
	}

	@Test
	public void canAddPieceAt() {
		assertTrue(batteryBoard.canAddPieceAt(battery, Vector.ZERO));
		assertFalse(batteryBoard.canAddPieceAt(batteryOnBoard, Vector.ZERO));
	}

	@Test
	public void merge_NormalCase() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		// Place wall on wallBoard which doesnt conflict
		// with batteryOnBoard and can be merged to batteryBoard
		Wall wall = new Wall();
		wall.placeOnBoard(wallBoard, new Vector(2, 2));

		batteryBoard.merge(wallBoard);

		// Battery at (0,0) was preserved
		assertTrue(batteryBoard.hasAsPiece(batteryOnBoard));
		// Wall at (9,9) was terminated
		assertFalse(wallBoard.hasAsPiece(wallOnBoard));
		assertTrue(wallOnBoard.isTerminated());

		assertTrue(wallBoard.isTerminated());
	}

	@Test
	public void merge_ConflictingPieces() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		// Place a wall in the origin of wallBoard
		// so that it will conflict with the battery on batteryBoard
		Wall wall = new Wall();
		wall.placeOnBoard(wallBoard, Vector.ZERO);

		batteryBoard.merge(wallBoard);

		// Battery at (0,0) was preserved
		assertTrue(batteryBoard.hasAsPiece(batteryOnBoard));
		// Wall at (9,9) was terminated
		assertFalse(wallBoard.hasAsPiece(wallOnBoard));
		assertTrue(wallOnBoard.isTerminated());
		// Wall at (0,0) was terminated
		assertFalse(wallBoard.hasAsPiece(wall));
		assertTrue(wall.isTerminated());

		assertTrue(wallBoard.isTerminated());
	}

	@Test(expected = IllegalStateException.class)
	public void merge_OnTerminatedBoard() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		terminatedBoard.merge(batteryBoard);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void merge_WithNullBoard() throws IllegalArgumentException, IllegalStateException, InvalidPositionException {
		batteryBoard.merge(null);
		fail();
	}

	@Test(expected = IllegalArgumentException.class)
	public void merge_WithTerminatedBoard() throws IllegalArgumentException, IllegalStateException,
			InvalidPositionException {
		batteryBoard.merge(terminatedBoard);
		fail();
	}

	@Test
	public void isValidPosition() {
		assertTrue(board_5x5.isValidPosition(Vector.ZERO));

		assertFalse(board_5x5.isValidPosition(new Vector(-1, 1)));
		assertFalse(board_5x5.isValidPosition(new Vector(1, -1)));

		assertFalse(board_5x5.isValidPosition(new Vector(5, 0)));
		assertFalse(board_5x5.isValidPosition(new Vector(3, 5)));
		assertFalse(board_5x5.isValidPosition(new Vector(7, 8)));

		assertFalse(board_5x5.isValidPosition(null));
	}

	@Test
	public void getNextPosition_NormalCase() {
		Vector nextPosition = board_10x10.getNextPosition(Vector.ZERO, Orientation.RIGHT);
		assertEquals(new Vector(1, 0), nextPosition);
	}

	@Test
	public void getNextPosition_InvalidPosition() {
		Vector nextPosition = board_10x10.getNextPosition(Vector.ZERO, Orientation.LEFT);
		assertNull(nextPosition);
	}

	@Test
	public void terminate_NormalCase() {
		batteryBoard.terminate();
		assertTrue(batteryBoard.isTerminated());
		assertTrue(batteryOnBoard.isTerminated());
	}

	/*
	 * Iterator
	 */

	@Test
	public void iterate_Batteries() throws Exception {
		// Filter for batteries that have a minimum weight of 40 grams
		class BatteryPredicate implements roborally.util.Predicate<Piece> {
			@Override
			public boolean apply(Piece input) {
				if (input instanceof Battery)
					if (((Battery) input).getWeight() > 40)
						return true;
				return false;
			}
		}

		// Amount of batteries
		int n = 5;
		// Amount of batteries heavier than 40 grams
		int h = 4;
		// Amount of other junk (not batteries)
		int j = 40;

		assertTrue(h <= n);

		Board board = new Board(n + j, n + j);
		Set<Piece> pieces = new HashSet<Piece>();

		// Create batteries
		for (int i = 0; i < n; i++) {
			int weight = i < h ? 60 : 30;
			Battery b = new Battery(weight);
			pieces.add(b);
			Vector pos = board.getRandomPosition(b);
			b.placeOnBoard(board, pos);
		}

		// Create non-battery pieces
		for (int i = 0; i < j; i++) {
			RepairKit b = new RepairKit(60, 100);
			pieces.add(b);
			Vector pos = board.getRandomPosition(b);
			b.placeOnBoard(board, pos);
		}

		// Get iterator
		Iterator<Piece> it = board.iterator(new BatteryPredicate());

		// Test that every piece the iterator returns is a piece that
		// satisfies the condition (battery with weight > 40)
		for (int i = 0; i < h; i++) {
			assertTrue(it.hasNext());
			Piece piece = it.next();
			assertTrue(pieces.contains(piece));
			assertTrue(piece instanceof Battery);
			assertTrue(((Battery) piece).getWeight() > 40);

		}

		// All batteries must have been returned now
		assertFalse(it.hasNext());
	}

}
