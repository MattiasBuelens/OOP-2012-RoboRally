package roborally.path;

import java.util.Collection;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import roborally.Vector;
import roborally.util.Sortable;

/**
 * A node in a path.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public abstract class Node implements Comparable<Node>, Sortable {

	/**
	 * Create a node.
	 * 
	 * @param position
	 * 			The position for this new node.
	 * 
	 * @post	The new node's position is set to the given position.
	 * 			| new.getPosition().equals(position)
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given position is not effective.
	 * 			| position == null
	 */
	protected Node(Vector position) throws IllegalArgumentException {
		if (position == null)
			throw new IllegalArgumentException("Position must be effective.");

		this.position = position;
	}

	/**
	 * Get the position of this node.
	 */
	@Basic
	@Immutable
	public final Vector getPosition() {
		return position;
	}

	private final Vector position;

	/**
	 * Get the total estimated cost from the start node
	 * to the target along this node.
	 * 
	 * @return	The total estimated cost is sum of the
	 * 			actual cost to this node and the estimated
	 * 			remaining cost to the target.
	 * 			| result == getG() + getH()
	 */
	public double getF() {
		return getG() + getH();
	}

	/**
	 * Get the actual cost from the start node to this node.
	 */
	@Basic
	public double getG() {
		return g;
	}

	/**
	 * Set the actual cost from the start node to this node.
	 * 
	 * @param g
	 * 			The new actual cost.
	 * 
	 * @post	The new actual cost equals the given cost.
	 * 			| new.getG() == g
	 */
	protected void setG(double g) {
		this.g = g;
	}

	private double g;

	/**
	 * Get the estimated remaining cost from this node
	 * to the target.
	 */
	@Basic
	public double getH() {
		return h;
	}

	/**
	 * Set the estimated remaining cost from this node
	 * to the target.
	 * 
	 * @param h
	 * 			The estimated remaining cost.
	 * 
	 * @post	The new estimated remaining cost equals
	 * 			the given cost.
	 * 			| new.getH() == h
	 */
	protected void setH(double h) {
		this.h = h;
	}

	private double h;

	/**
	 * Calculate the estimated remaining cost from
	 * this node to the given target node
	 * and store the cost in the node.
	 * 
	 * @param target
	 * 			The target node.
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the target is not effective.
	 * 			| target == null
	 */
	public abstract void calculateH(Node target) throws IllegalArgumentException;

	/**
	 * Get the previous node.
	 */
	@Basic
	public Node getPrevious() {
		return previous;
	}

	/**
	 * Set the previous node.
	 * 
	 * @param node
	 * 			The new previous node.
	 * 
	 * @post	The new previous node equals the given node.
	 * 			| new.getPrevious() == previous
	 */
	public void setPrevious(Node node) {
		this.previous = node;
	}

	private Node previous;

	/**
	 * Check if the given node appears somewhere
	 * in this node's ancestor chain.
	 * 
	 * @param node
	 * 			The node to check.
	 * 
	 * @return	False if the given node is not effective.
	 * 			| if (node == null)
	 * 			|   result == false
	 * @return	False if this node has no previous node.
	 * 			| else if (getPrevious() == null)
	 * 			|   result == false
	 * @return	True if the previous node of this node
	 * 			equals the given node.
	 * 			| else if (getPrevious().equals(node))
	 * 			|   result == true
	 * @return	Otherwise, true if and only if
	 * 			the previous node of this node has
	 * 			the given node in its ancestor chain.
	 * 			| else
	 * 			|   result == getPrevious().hasAsPrevious(node)
	 */
	public boolean hasAsPrevious(Node node) {
		if (node == null)
			return false;
		Node previous = getPrevious();
		if (previous == null)
			return false;
		if (previous.equals(node))
			return true;
		return previous.hasAsPrevious(node);
	}

	/**
	 * Check if the given node is a neighbour of this node.
	 * 
	 * @param node
	 * 			The node to check.
	 * @return	True if and only if the set of neighbour nodes
	 * 			of this node contains the given node.
	 * 			| result == getNeighbours().contains(node)
	 */
	public boolean isNeighbour(Node node) {
		return getNeighbours().contains(node);
	}

	/**
	 * Get the neighbour nodes of this node.
	 */
	public abstract Collection<Node> getNeighbours();

	/**
	 * @return	The total estimated cost is used
	 * 			as sorting key.
	 * 			| result == getF()
	 */
	@Override
	public double getKey() {
		return getF();
	}

	/**
	 * @return	The nodes are compared on their total
	 * 			estimated cost.
	 * 			| let
	 * 			|   f1 = Double.valueOf(this.getF())
	 * 			|   f2 = Double.valueOf(node.getF())
	 * 			| result == f1.compareTo(f2)
	 */
	@Override
	public int compareTo(Node node) {
		Double f1 = Double.valueOf(this.getF());
		Double f2 = Double.valueOf(node.getF());
		return f1.compareTo(f2);
	}

	/**
	 * @return	True if the given object reference equals this object reference.
	 * 			| if (this == obj)
	 * 			|   result == true
	 * @return	False if the given object is not a node.
	 * 			| else if (getClass() != obj.getClass())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the positions are equal.
	 * 			| else
	 * 			|   let
	 * 			|      other = (Node) obj
	 * 			|   result == (getPosition().equals(other.getPosition())
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (getClass() != obj.getClass())
			return false;
		Node other = (Node) obj;
		return getPosition().equals(other.getPosition());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime * getPosition().hashCode();
		return result;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " at " + getPosition();
	}

}
