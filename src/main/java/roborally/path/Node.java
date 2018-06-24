package roborally.path;

import java.util.Collection;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Model;
import be.kuleuven.cs.som.annotate.Raw;
import roborally.Vector;
import roborally.util.Sortable;

/**
 * A node in a path.
 * 
 * @param <V>
 * 			The cost value type.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 3.0
 */
public abstract class Node<V extends Comparable<? super V>> implements Comparable<Node<V>>, Sortable {

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
	@Raw
	@Model
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
	public Vector getPosition() {
		return position;
	}

	/**
	 * Variable registering the position of this node.
	 * 
	 * @invar	The position is effective.
	 * 			| position != null
	 */
	private final Vector position;

	/**
	 * Get the total estimated cost from the start node
	 * to the target along this node.
	 */
	public abstract V getF();

	/**
	 * Get the actual cost from the start node to this node.
	 */
	@Basic
	public V getG() {
		return g;
	}

	/**
	 * Set the actual cost from the start node to this node.
	 * 
	 * @param g
	 * 			The new actual cost.
	 * 
	 * @post	The new actual cost equals the given cost.
	 * 			| new.getG().equals(g)
	 */
	protected void setG(V g) {
		this.g = g;
	}
	
	/**
	 * Reset the actual cost from the start node to this node
	 * to zero.
	 */
	public abstract void resetG();

	/**
	 * Variable registering the actual cost from the start node
	 * to this node.
	 */
	private V g;

	/**
	 * Get the estimated remaining cost from this node
	 * to the target.
	 */
	@Basic
	public V getH() {
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
	 * 			| new.getH().equals(h)
	 */
	protected void setH(V h) {
		this.h = h;
	}

	/**
	 * Variable registering the estimated remaining cost
	 * from this node to the target.
	 */
	private V h;

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
	public abstract void calculateH(Node<V> target) throws IllegalArgumentException;

	/**
	 * Get the previous node.
	 */
	@Basic
	public Node<V> getPrevious() {
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
	public void setPrevious(Node<V> node) {
		this.previous = node;
	}

	/**
	 * Variable registering the previous node of this node.
	 */
	private Node<V> previous;

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
	public boolean hasAsPrevious(Node<V> node) {
		if (node == null)
			return false;
		Node<V> previous = getPrevious();
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
	public boolean isNeighbour(Node<V> node) {
		return getNeighbours().contains(node);
	}

	/**
	 * Get the neighbour nodes of this node.
	 */
	public abstract Collection<Node<V>> getNeighbours();

	/**
	 * @return	The total estimated cost is used
	 * 			as sorting key.
	 */
	@Override
	public abstract double getKey();

	/**
	 * @return	The nodes are compared on their total
	 * 			estimated cost.
	 */
	@Override
	public abstract int compareTo(Node<V> node);

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
		@SuppressWarnings("rawtypes")
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
