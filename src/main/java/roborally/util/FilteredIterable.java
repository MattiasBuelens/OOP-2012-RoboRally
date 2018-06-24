package roborally.util;

import java.util.Iterator;

/**
 * An iterable class which can also produce a filtered iterator.
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
public interface FilteredIterable<T> extends Iterable<T> {

	/**
	 * Get an iterator over a set of elements of type T
	 * which fulfill a given predicate.
	 * 
	 * @param filter
	 * 			The predicate to filter with.
	 * 
	 * @return	A filtered iterator.
	 */
	Iterator<T> iterator(Predicate<? super T> filter);

}
