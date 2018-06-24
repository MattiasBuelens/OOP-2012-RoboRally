package roborally.program;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;

/**
 * A token indicating a value argument.
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
public class ValueToken implements Token {

	/**
	 * Create a new value token with the given value.
	 * 
	 * @param value
	 * 			The value.
	 * 
	 * @post	The new value token's value is set
	 * 			to the given value.
	 * 			| new.getValue() == value
	 */
	public ValueToken(String value) {
		this.value = value;
	}

	/**
	 * Get the value of this token.
	 */
	@Basic
	@Immutable
	public String getValue() {
		return value;
	}

	/**
	 * Variable registering the value of this token.
	 */
	private final String value;

}
