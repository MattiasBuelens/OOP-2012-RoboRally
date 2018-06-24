package roborally.path;

import roborally.*;

/**
 * A robot node in a reachable nodes finding algorithm.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 3.0
 */
public class ReachNode extends RobotNode {

	/**
	 * Create a new reach node.
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
	public ReachNode(Robot robot, Vector position, Orientation orientation) {
		super(robot, position, orientation);
	}

	/**
	 * Create a new reach node.
	 * 
	 * @param robot
	 * 			The robot for this new node.
	 * @param position
	 * 			The position for this new node.
	 * 
	 * @effect	| super(robot, position)
	 */
	public ReachNode(Robot robot, Vector position) {
		super(robot, position);
	}

	/**
	 * @effect	The estimated remaining cost is set to zero.
	 * 			| setH(EnergyAmount.ZERO)
	 */
	@Override
	public void calculateH(Node<EnergyAmount> destination) {
		setH(EnergyAmount.ZERO);
	}

	/**
	 * @return	True if and only if this node is valid as a robot node
	 * 			and this node's robot can reach this node.
	 * 			| super.isValid() && canReachWithEnergy()
	 */
	@Override
	public boolean isValid() {
		return super.isValid() && canReachWithEnergy();
	}

	@Override
	protected ReachNode create(Vector position, Orientation orientation) {
		return new ReachNode(getRobot(), position, orientation);
	}

	@Override
	public ReachNode getPrevious() {
		return (ReachNode) super.getPrevious();
	}

}
