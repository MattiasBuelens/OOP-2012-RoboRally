package roborally.util;

/**
 * A function which takes a single input
 * and produces an output value.
 * 
 * @param <F>
 * 			The input type.
 * @param <T>
 * 			The output type.
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
public interface Function<F, T> {

	/**
	 * Apply this function to the given input.
	 * 
	 * @param input
	 * 			The input.
	 */
	T apply(F input);

}
