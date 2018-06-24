package roborally.path;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

import roborally.Orientation;
import roborally.Robot;
import roborally.Vector;

/**
 * A node in a path walked by a robot.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public abstract class RobotNode extends Node {

	/**
	 * Create a new robot node.
	 * 
	 * @param robot
	 * 			The robot for this new node.
	 * @param position
	 * 			The position for this new node.
	 * @param orientation
	 * 			The orientation for this new node.
	 * 
	 * @effect	| super(position)
	 * @post	| new.getRobot() == robot
	 * @post	| if(orientation != null)
	 * 			|   new.getOrientation() == orientation
	 * 			| else
	 * 			|   new.getOrientation() == Orientation.UP
	 * 
	 * @throws	IllegalArgumentException
	 * 			| robot == null || robot.isTerminated()
	 */
	protected RobotNode(Robot robot, Vector position, Orientation orientation) throws IllegalArgumentException {
		super(position);

		if (robot == null || robot.isTerminated())
			throw new IllegalArgumentException("Robot must be effective and not terminated.");

		this.robot = robot;
		this.orientation = orientation == null ? Orientation.UP : orientation;
	}

	/**
	 * Create a new robot node.
	 * 
	 * @param robot
	 * 			The robot for this new node.
	 * @param position
	 * 			The position for this new node.
	 * 
	 * @effect	| this(robot, position, null)
	 */
	protected RobotNode(Robot robot, Vector position) {
		this(robot, position, null);
	}

	/**
	 * Get the robot associated with this node.
	 */
	@Basic
	@Immutable
	public final Robot getRobot() {
		return robot;
	}

	private final Robot robot;

	/**
	 * Get the orientation of this node.
	 */
	@Basic
	@Immutable
	public final Orientation getOrientation() {
		return orientation;
	}

	private final Orientation orientation;

	/**
	 * Check whether this node's robot can reach this node.
	 * 
	 * @return	True if and only if the actual cost to this node
	 * 			is less than or equal to the robot's current
	 * 			amount of energy.
	 * 			| result == (getG() <= getRobot.getEnergy())
	 */
	public boolean canReachWithEnergy() {
		return getG() <= getRobot().getEnergy();
	}

	/**
	 * Check whether this node's robot can move to
	 * the position of this node.
	 * 
	 * @return	| getRobot().canMoveTo(getPosition())
	 */
	public boolean isValid() {
		// Position must be valid for this robot
		return getRobot().canMoveTo(getPosition());
	}

	/**
	 * Create a new node of the same type as this node.
	 * 
	 * @param previous
	 * 			The previous node of the new node.
	 * @param position
	 * 			The position of the new node.
	 * @param orientation
	 * 			The orientation of the new node.
	 * 
	 * @return	| result.getPosition().equals(position)
	 * 			|  && result.getOrientation() == orientation
	 * 			|  && result.getPrevious() == previous
	 * 			|  && result.getClass() == this.getClass()
	 */
	protected RobotNode create(Node previous, Vector position, Orientation orientation) {
		// Create the node
		RobotNode node = create(position, orientation);
		// Set the previous node
		node.setPrevious(this);
		// Calculate the G-score from the previous node to this node
		node.setG(node.calculateG());
		return node;
	}

	/**
	 * Create a new node of the same type as this node.
	 * 
	 * @param position
	 * 			The position of the new node.
	 * @param orientation
	 * 			The orientation of the new node.
	 * 
	 * @return	| result.getPosition().equals(position)
	 * 			|  && result.getOrientation() == orientation
	 * 			|  && result.getClass() == this.getClass()
	 */
	protected abstract RobotNode create(Vector position, Orientation orientation);

	@Override
	public RobotNode getPrevious() {
		return (RobotNode) super.getPrevious();
	}

	/**
	 * @return	| for each neighbour in result :
	 * 			|   neighbour.getPosition().manhattanDistance(this.getPosition()) == 1
	 * 			|    && neighbour.getRobot() == this.getRobot()
	 * 			|    && neighbour.getPrevious() == this
	 * 			|    && neighbour.isValid()
	 */
	@Override
	public Collection<Node> getNeighbours() {
		List<Node> neighbours = new ArrayList<Node>(Orientation.values().length);
		for (Orientation orientation : Orientation.values()) {
			// Get adjacent position in this orientation
			Vector position = getPosition().add(orientation.getVector());
			// Create a new neighbour node
			RobotNode neighbour = create(this, position, orientation);
			// Add to list of neighbours if it's valid
			if (neighbour.isValid())
				neighbours.add(neighbour);
		}
		return neighbours;
	}

	/**
	 * Calculate the actual cost of this node.
	 * 
	 * <p>The actual cost of this node is calculated as the sum
	 * of the actual cost of the previous node and the cost
	 * to move from the previous node to this node.</p>
	 */
	private double calculateG() {
		RobotNode previous = getPrevious();
		assert (previous != null);
		// Start from actual cost of previous node
		double g = previous.getG();
		// Add one step cost
		g += getRobot().getStepCost();
		// Get the amount of turns
		int amountOfTurns = getOrientation().getDifference(previous.getOrientation());
		// Add turn costs
		g += amountOfTurns * getRobot().getTurnCost();
		return g;
	}

	/**
	 * @return	True if the given object reference equals this object reference.
	 * 			| if (this == obj)
	 * 			|   result == true
	 * @return	False if the given object is not a robot node.
	 * 			| else if (getClass() != obj.getClass())
	 * 			|   result == false
	 * @return	False if the given object is not equal to this node
	 * 			according to the super class.
	 * 			| else if (!super.equals(obj))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the robots are equal.
	 * 			| else
	 * 			|   let
	 * 			|      other = (RobotNode) obj
	 * 			|   result == (getRobot().equals(other.getRobot())
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		if (!super.equals(obj))
			return false;
		RobotNode other = (RobotNode) obj;
		return getRobot().equals(other.getRobot());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + getRobot().hashCode();
		return result;
	}

	@Override
	public String toString() {
		String result = super.toString();
		result += " facing " + getOrientation();
		return result;
	}

}