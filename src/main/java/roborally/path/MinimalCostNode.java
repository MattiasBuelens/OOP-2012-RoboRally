package roborally.path;

import roborally.Orientation;
import roborally.Robot;
import roborally.Vector;

/**
 * A robot node in a minimal energy cost pathfinding algorithm.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class MinimalCostNode extends RobotNode {

	/**
	 * Create a new minimal cost node.
	 * 
	 * @param robot
	 * 			The robot for this new node.
	 * @param position
	 * 			The position for this new node.
	 * @param orientation
	 * 			The orientation for this new node.
	 * 
	 * @effect	| super(robot, position, orientation)
	 */
	public MinimalCostNode(Robot robot, Vector position, Orientation orientation) {
		super(robot, position, orientation);
	}

	/**
	 * Create a new minimal cost node.
	 * 
	 * @param robot
	 * 			The robot for this new node.
	 * @param position
	 * 			The position for this new node.
	 * 
	 * @effect	| super(robot, position)
	 */
	public MinimalCostNode(Robot robot, Vector position) {
		super(robot, position);
	}

	/**
	 * @effect	The estimated remaining cost is set to the product of
	 * 			this robot's step cost and the Manhattan distance
	 * 			between this node and the target.
	 * 			| let
	 * 			|   amountOfSteps = getPosition().manhattanDistance(target.getPosition())
	 * 			| setH(getRobot().getStepCost() * amountOfSteps)
	 */
	@Override
	public void calculateH(Node target) throws IllegalArgumentException {
		if (target == null)
			throw new IllegalArgumentException("Target must be not effective.");

		long amountOfSteps = getPosition().manhattanDistance(target.getPosition());
		double h = amountOfSteps * getRobot().getStepCost();
		setH(h);
	}

	@Override
	protected MinimalCostNode create(Vector position, Orientation orientation) {
		return new MinimalCostNode(getRobot(), position, orientation);
	}
	
	@Override
	public MinimalCostNode getPrevious() {
		return (MinimalCostNode) super.getPrevious();
	}
}
