package roborally.path;

import roborally.*;

/**
 * An A* algorithm to find the minimal energy cost for a robot
 * to reach a target position.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 3.0
 */
public class MinimalCostAStar extends RobotAStar<MinimalCostNode> {

	/**
	 * Create a new minimal cost A* algorithm.
	 * 
	 * @param robot
	 * 			The robot for this new algorithm.
	 * @param target
	 * 			The target position for this new algorithm.
	 * 
	 * @effect	| super(robot)
	 * @post	| new.getStart().getRobot() == robot
	 * @post	| new.getStart().getPosition().equals(robot.getPosition())
	 * @post 	| new.getStart().getOrientation() == robot.getOrientation()
	 * @post 	| new.getTarget().getRobot() == robot
	 * @post	| new.getTarget().getPosition().equals(target)
	 * 
	 * @throws	IllegalArgumentException
	 * 			| target == null
	 */
	public MinimalCostAStar(Robot robot, Vector target) throws IllegalArgumentException {
		super(robot);

		if (target == null)
			throw new IllegalArgumentException("Target position must be effective.");

		setStart(new MinimalCostNode(robot, robot.getPosition(), robot.getOrientation()));
		setTarget(new MinimalCostNode(getRobot(), target));
	}

	/**
	 * Get the minimal energy cost to reach the target position.
	 * 
	 * @return	The minimal energy cost is the cost of the last
	 * 			visited node after running the A* algorithm.
	 * 			| let
	 * 			|   lastNode = run()
	 * 			| result == lastNode.getG()
	 * 
	 * @throws	UnreachablePositionException
	 * 			If the last visited node is not the target node.
	 * 			In this case, the A* algorithm has exhausted
	 * 			the open set and could not find any path
	 * 			to the target node.
	 * 			| !isTarget(lastNode)
	 */
	public EnergyAmount getCost() throws UnreachablePositionException {
		MinimalCostNode lastNode = run();
		// If last node is not the target, the target is unreachable
		if (!isTarget(lastNode))
			throw new UnreachablePositionException(getRobot(), getTarget().getPosition());
		// Else, return the cost to reach the target
		return lastNode.getG();
	}

	/**
	 * @return	False if the given target is not effective.
	 * 			| if (target == null)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the given target
	 * 			is effective and applies to
	 * 			the same robot as this algorithm.
	 * 			| else
	 * 			|   result == (target.getRobot() == getRobot())
	 */
	@Override
	protected boolean canHaveAsTarget(MinimalCostNode target) {
		if (target == null)
			return false;
		return target.getRobot() == getRobot();
	}

	/**
	 * @return	False if the given node is not effective.
	 * 			| if (node == null)
	 * 			|   result == false
	 * @return	Otherwise, true if and only if
	 * 			the given node equals the target node.
	 * 			| else
	 * 			|   result == target.equals(node)
	 */
	@Override
	public boolean isTarget(MinimalCostNode node) {
		MinimalCostNode target = getTarget();
		return target != null && target.equals(node);
	}

}
