package roborally.util;

/**
 * A predicate function which produces a boolean value
 * for a given input.
 * 
 * @param <T>
 * 			The input type.
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
public interface Predicate<T> {

	/**
	 * Apply this predicate to the given input.
	 * 
	 * @param input
	 * 			The input.
	 */
	boolean apply(T input);

}
