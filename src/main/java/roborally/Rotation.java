package roborally;

import be.kuleuven.cs.som.annotate.Basic;

/**
 * An enumeration of rotation directions.
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
public enum Rotation {
	/**
	 * Clockwise rotation.
	 */
	CLOCKWISE("clockwise"),
	/**
	 * Counterclockwise rotation
	 */
	COUNTERCLOCKWISE("counterclockwise");

	private Rotation(String name) {
		this.name = name;
	}

	/**
	 * Get the name of this rotation direction.
	 */
	@Basic
	public String getName() {
		return name;
	}

	/**
	 * Variable registering the name of this rotation direction.
	 */
	private final String name;

	/**
	 * Get a rotation direction by its name.
	 * 
	 * @param name
	 * 			The name.
	 * 
	 * @return	The rotation direction whose name equals
	 * 			the given name (ignoring case), or null
	 * 			if no such rotation direction exists.
	 * 			| if (for some rotation in values() :
	 * 			|      rotation.getName().equalsIgnoreCase(name))
	 * 			|	result == rotation
	 * 			| else
	 * 			|	result == null
	 */
	public static Rotation getByName(String name) {
		for (Rotation rotation : values()) {
			if (rotation.getName().equalsIgnoreCase(name))
				return rotation;
		}
		return null;
	}
}
