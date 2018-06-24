package roborally.path;

import java.util.Map;

import roborally.Robot;
import roborally.Vector;

/**
 * An A* algorithm to find all positions and their energy costs
 * which can be reached by a robot with its current amount of energy.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class ReachAStar extends RobotAStar<ReachNode> {

	/**
	 * Create a new minimal cost A* algorithm.
	 * 
	 * @param robot
	 * 			The robot for this new algorithm.
	 * 
	 * @effect	| super(robot)
	 * @post	| new.getStart().getRobot() == robot
	 * @post	| new.getStart().getPosition().equals(robot.getPosition())
	 * @post 	| new.getStart().getOrientation() == robot.getOrientation()
	 * @post	| new.getTarget() == null
	 */
	public ReachAStar(Robot robot) {
		super(robot);
		setStart(new ReachNode(robot, robot.getPosition(), robot.getOrientation()));
		setTarget(null);
	}

	/**
	 * Get all nodes reachable with the robot's
	 * current amount of energy.
	 * 
	 * @return	The node map after running the A* algorithm.
	 * 			| let
	 * 			|   lastNode = run()
	 * 			| result == getNodeMap()
	 */
	public Map<Vector, ReachNode> getReachable() {
		run();
		return getNodeMap();
	}

	/**
	 * @return	True if and only if the given target
	 * 			is not effective.
	 * 			| result == (target == null)
	 */
	protected boolean canHaveAsTarget(ReachNode target) {
		return (target == null);
	}

	/**
	 * @return	Always false.
	 * 			| result == false
	 */
	@Override
	public boolean isTarget(ReachNode node) {
		return false;
	}

}
