package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * Represents a terminatable object.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public abstract class Terminatable {

	/**
	 * Check whether this object is terminated.
	 */
	@Basic
	@Raw
	public boolean isTerminated() {
		return isTerminated;
	}

	/**
	 * Terminate this object.
	 * 
	 * @post	The object becomes terminated.
	 * 			| new.isTerminated()
	 */
	public void terminate() {
		this.isTerminated = true;
	}

	private boolean isTerminated = false;
}
