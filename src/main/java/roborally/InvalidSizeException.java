package roborally;

/**
 * A class of exceptions thrown when an invalid width or height was given
 * to a board in a game of RoboRally.
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
public class InvalidSizeException extends Exception {

	public InvalidSizeException(long width, long height) {
		this.width = width;
		this.height = height;
	}

	/**
	 * Get the width of the invalid size.
	 */
	public long getWidth() {
		return width;
	}

	private final long width;

	/**
	 * Get the height of the invalid size.
	 */
	public long getHeight() {
		return height;
	}

	private final long height;

	public String getMessage() {
		return String.format("Invalid size: %d x %d", getWidth(), getHeight());
	}

	private static final long serialVersionUID = 1L;
}
