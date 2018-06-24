package roborally.program;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A token indicating the start of a statement.
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
public class OpenToken implements Token {

	/**
	 * Create a new open token with the given name.
	 * 
	 * @param name
	 * 			The name of the statement.
	 * 
	 * @post	The new open token's name is set
	 * 			to the given name.
	 * 			| new.getName() == name
	 */
	public OpenToken(String name) {
		this.name = name;
	}

	/**
	 * Get the name of the statement.
	 */
	@Basic
	@Immutable
	public String getName() {
		return name;
	}

	/**
	 * Variable registering the name of the statement.
	 */
	private final String name;

}
