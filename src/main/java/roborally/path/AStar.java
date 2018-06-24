package roborally.path;

import java.util.*;

import roborally.Vector;
import be.kuleuven.cs.som.annotate.Basic;

/**
 * An implementation of the A* pathfinding algorithm.
 * 
 * @invar	The start node is valid.
 * 			| canHaveAsStart(getStart())
 * @invar	The target node is valid.
 * 			| canHaveAsTarget(getTarget())
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public abstract class AStar<N extends Node> {

	/**
	 * Run the pathfinding algorithm and return the last evaluated node.
	 * 
	 * <p>If there exists a path to the target, the last node
	 * in the optimal path is returned. The complete path
	 * can be retrieved with {@link #reconstructPath(Node)}.</p>
	 * 
	 * <p>If there are no paths to the target, all reachable
	 * positions are evaluated and stored with their
	 * minimal cost in the node map.</p>
	 * 
	 * @return	For all paths starting at the start node and
	 * 			ending at the target node, the result is the last
	 * 			node in the path with the minimal cost to reach the
	 * 			target by travelling along the path.
	 * 			| let
	 * 			|   pathsTo(target) = {path:N[]
	 * 			|      | path[0].equals(this.getStart())
	 * 			|         && path[path.length-1].equals(target)
	 * 			|         && for each i in 1..path.length-2 :
	 * 			|               path[i].getPrevious() == path[i-1]
	 * 			|                && path[i+1].getPrevious() == path[i]
	 * 			|                && path[i].isNeighbour(path[i-1])
	 * 			|                && path[i].isNeighbour(path[i+1])
	 * 			|   }
	 * 			|
	 * 			|   bestPathTo(target) = {path | path in pathsTo(target)
	 * 			|      | for each otherPath in pathsTo(target) :
	 * 			|           path[path.length-1].getG()
	 * 			|             <= otherPath[otherPath.length-1].getG()
	 * 			|   }
	 * 			|
	 * 			| if (#{pathsTo(getTarget())} > 0)
	 * 			|   result == last{bestPathTo(getTarget())}
	 * 
	 * @return	If no paths exist to the target, then every position
	 * 			which can be reached from the starting position
	 * 			is contained in the node map and is mapped to the
	 * 			last node in the optimal path to that position.
	 * 			| else
	 * 			|   for each node:N :
	 * 			|      if (#{pathsTo(node)} > 0)
	 * 			|         getNodeMap().get(node.getPosition())
	 * 			|           == last{bestPathTo(node)}
	 * 			|      else
	 * 			|         !getNodeMap().containsKey(node.getPosition())
	 */
	protected N run() {
		reset();
		N current = getStart();

		while (!openSet.isEmpty()) {
			// Remove from open set and add to closed set
			current = openSet.poll();
			closedSet.add(current.getPosition());
			nodeMap.put(current.getPosition(), current);

			// If this is the target, we're finished
			if (isTarget(current)) {
				openSet.clear();
				break;
			}

			// Inspect all neighbours of current node
			Iterable<Node> neighbours = current.getNeighbours();
			for (Node neighbourNode : neighbours) {
				@SuppressWarnings("unchecked")
				N neighbour = (N) neighbourNode;
				Vector neighbourPosition = neighbour.getPosition();
				// If neighbour position is in closed set, don't check it again
				if (closedSet.contains(neighbourPosition)) {
					continue;
				}

				// Get old neighbour as it is stored in the node map
				Node oldNeighbour = getNodeByPosition(neighbourPosition);
				boolean isBetterNeighbour = false;

				if (oldNeighbour == null) {
					// If no node at this position yet, add it
					isBetterNeighbour = true;
				} else {
					// If there is already a node at this position,
					// replace it if it has a higher G-score
					double oldG = oldNeighbour.getG();
					double newG = neighbour.getG();
					isBetterNeighbour = (newG < oldG);
				}

				// If the new neighbour is better
				if (isBetterNeighbour) {
					// Calculate its H-score
					neighbour.calculateH(getTarget());
					// Remove the old one and add the new one
					openSet.remove(oldNeighbour);
					openSet.offer(neighbour);
					// Store in node map
					nodeMap.put(neighbour.getPosition(), neighbour);
				}
			}
		}

		return current;
	}

	/**
	 * Reset the algorithm to its inital state.
	 * 
	 * @post	The cost to reach the start node is reset to zero.
	 * 			| new.getStart().getG() == 0
	 * @post	The open set only contains the start node.
	 * 			| new.getOpenSet().size() == 1
	 * 			| new.getOpenSet().element() == getStart()
	 * @post	The closed set is cleared.
	 * 			| new.getClosedSet().isEmpty()
	 * @post	The node map is cleared.
	 * 			| new.getNodeMap().isEmpty()
	 * 
	 * @throws	NullPointerException
	 * 			If the open set is not effective.
	 * 			| getOpenSet() == null
	 */
	protected void reset() throws NullPointerException {
		// Clear sets
		nodeMap.clear();
		closedSet.clear();
		openSet.clear();

		// Reset start node and add to open set
		getStart().setG(0);
		openSet.offer(getStart());
	}

	/**
	 * Reconstruct the path to reach the given node
	 * from its chain of previous nodes.
	 * 
	 * @param node
	 * 			The node.
	 * 
	 * @pre		The given node must originate from
	 * 			this algorithm's start node.
	 * 			| node.hasAsPrevious(getStart())
	 * 
	 * @return	The first node in the resulting sequence
	 * 			is the start node and the last node is
	 * 			the given node.
	 * 			| result.getFirst() == getStart()
	 * 			| result.getLast() == node
	 * @return	Every node in the resulting sequence
	 * 			is preceded by its previous node.
	 * 			| for each node in result :
	 * 			|   node.getPrevious() == null
	 * 			|    || result.indexOf(node) == result.indexOf(node.getPrevious()) + 1
	 * @throws	IllegalArgumentException
	 * 			If the given node is not effective.
	 * 			| node == null
	 */
	@SuppressWarnings("unchecked")
	public LinkedList<N> reconstructPath(N node) {
		if (node == null)
			throw new IllegalArgumentException("Node must be effective.");
		assert node.hasAsPrevious(getStart());

		LinkedList<N> path = new LinkedList<N>();
		N current = node;
		do {
			path.offerFirst(current);
			current = (N) current.getPrevious();
		} while (current != null);
		return path;
	}

	/**
	 * Get the start node of this algorithm.
	 */
	@Basic
	public N getStart() {
		return start;
	}

	/**
	 * Set the start node for this algorithm.
	 * 
	 * @param start
	 * 			The new start node.
	 * 
	 * @post	The start node is set to the given node.
	 * 			| new.getStart() == start
	 * @throws	IllegalArgumentException
	 * 			If the given node is not a valid start node.
	 * 			| !canHaveAsStart(start)
	 */
	protected void setStart(N start) throws IllegalArgumentException {
		if (!canHaveAsStart(start))
			throw new IllegalArgumentException("Invalid start node.");
		this.start = start;
	}

	/**
	 * Check whether the given node is a valid start node.
	 * 
	 * @param start
	 * 			The node to check.
	 * @return	True if and only if the given node is effective.
	 * 			| result == (start != null)
	 */
	protected boolean canHaveAsStart(N start) {
		return start != null;
	}

	private N start;

	/**
	 * Get the target node of this algorithm.
	 */
	@Basic
	public N getTarget() {
		return target;
	}

	/**
	 * Set the target node for this algorithm.
	 * 
	 * @param target
	 * 			The new target node.
	 * 
	 * @post	The target node is set to the given node.
	 * 			| new.getTarget() == target
	 * @throws	IllegalArgumentException
	 * 			If the given node is not a valid target node.
	 * 			| !canHaveAsTarget(start)
	 */
	protected void setTarget(N target) {
		if (!canHaveAsTarget(target))
			throw new IllegalArgumentException("Invalid target node.");
		this.target = target;
	}

	/**
	 * Check whether the given node is a valid target node.
	 * 
	 * @param target
	 * 			The node to check.
	 */
	protected boolean canHaveAsTarget(N target) {
		return true;
	}

	private N target;

	/**
	 * Check if the given node is the target.
	 * 
	 * @param node
	 * 			The node to check.
	 * @return	False if the given node is not effective.
	 * 			| if (node == null)
	 * 			|   result == false
	 */
	public abstract boolean isTarget(N node);

	/**
	 * Get the closed set of this algorithm.
	 */
	@Basic
	protected Set<Vector> getClosedSet() {
		return closedSet;
	}

	private Set<Vector> closedSet = new HashSet<Vector>();

	/**
	 * Get the open set of this algorithm.
	 */
	@Basic
	protected Queue<N> getOpenSet() {
		return openSet;
	}

	/**
	 * Set the open set of this algorithm.
	 * 
	 * @param openSet
	 * 			The new open set.
	 * 
	 * @post	The open set is set to the given set.
	 * 			| new.getOpenSet() == openSet
	 */
	protected void setOpenSet(Queue<N> openSet) {
		this.openSet = openSet;
	}

	private Queue<N> openSet;

	/**
	 * Get the map of nodes which have been visited
	 * during the execution of the algorithm.
	 */
	@Basic
	protected Map<Vector, N> getNodeMap() {
		return Collections.unmodifiableMap(nodeMap);
	}

	/**
	 * Get a node by its position.
	 * 
	 * @param position
	 * 			The position.
	 * 
	 * @return	The node from the node map at the given position,
	 * 			or null if the given position has never been visited
	 * 			during the execution of the algorithm.
	 * 			| result == getNodeMap().get(position)
	 */
	protected N getNodeByPosition(Vector position) {
		return getNodeMap().get(position);
	}

	private Map<Vector, N> nodeMap = new HashMap<Vector, N>();

}
