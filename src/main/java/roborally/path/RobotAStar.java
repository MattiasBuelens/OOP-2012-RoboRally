package roborally.path;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import roborally.*;
import roborally.util.FibonacciQueue;

/**
 * An A* algorithm for a robot moving on a board.
 * 
 * @param <N>
 * 			The node type.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 3.0
 */
public abstract class RobotAStar<N extends RobotNode> extends AStar<N, EnergyAmount> {

	/**
	 * Create a new robot A* algorithm.
	 * 
	 * @param robot
	 * 			The robot for this new algorithm.
	 * 
	 * @post	This algorithm's robot is set to the given robot.
	 * 			| new.getRobot() == robot
	 * @post	A queue is created to use for the open set.
	 * 			| new.getOpenSet() != null
	 * 			
	 * @throws	IllegalArgumentException
	 * 		    If the given robot is not effective or is terminated.
	 * 			| robot == null || robot.isTerminated()
	 */
	@Raw
	@Model
	protected RobotAStar(Robot robot) throws IllegalArgumentException {
		if (robot == null || robot.isTerminated())
			throw new IllegalArgumentException("Robot must be effective and not terminated.");

		this.robot = robot;
		setOpenSet(new FibonacciQueue<N>());
	}

	/**
	 * Get the robot this algorithm works on.
	 */
	@Basic
	@Immutable
	public Robot getRobot() {
		return robot;
	}

	/**
	 * Variable registering the robot this algorithm works on.
	 * 
	 * @invar	The robot is effective.
	 * 			| robot != null
	 */
	protected final Robot robot;

}