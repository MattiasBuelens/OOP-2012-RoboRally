package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A class of exceptions thrown when a robot tried to reach
 * an unreachable position in a game of RoboRally.
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
public class UnreachablePositionException extends Exception {

	public UnreachablePositionException(Robot robot, Vector position) {
		this.robot = robot;
		this.position = position;
	}

	public UnreachablePositionException(Robot robot, long x, long y) {
		this(robot, new Vector(x, y));
	}

	/**
	 * Get the unreachable position.
	 */
	@Basic
	@Immutable
	public Vector getPosition() {
		return position;
	}

	final Vector position;

	/**
	 * Get the robot.
	 */
	@Basic
	@Immutable
	public Robot getRobot() {
		return robot;
	}

	final Robot robot;

	public String getMessage() {
		return String.format("Unreachable position %s for %s", getPosition(), getRobot());
	}

	private static final long serialVersionUID = 1L;

}
