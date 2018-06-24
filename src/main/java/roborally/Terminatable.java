package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A terminatable object.
 * 
 * @author	Mattias Buelens
 * @author	Thomas Goossens
 * @version	3.0
 * 
 * @note	This class is part of the 2012 project for
 * 			the course Object Oriented Programming in
 * 			the second phase of the Bachelor of Engineering
 * 			at KU Leuven, Belgium.
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

	/**
	 * Variable registering whether this object is terminated.
	 */
	private boolean isTerminated = false;
}
