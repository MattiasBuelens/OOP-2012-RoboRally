package roborally.util;

import java.util.Iterator;

/**
 * An iterator which filters the elements of another iterator.
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
public class FilteredIterator<T> extends AbstractIterator<T> {

	/**
	 * Create a new filtered iterator which filters
	 * the given iterator using the given filter.
	 * 
	 * @param iterator
	 * 			The unfiltered iterator.
	 * @param filter
	 * 			The filter.
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given iterator or filter is not effective.
	 */
	public FilteredIterator(Iterator<T> iterator, Predicate<? super T> filter) throws IllegalArgumentException {
		if (iterator == null)
			throw new IllegalArgumentException("Iterator must be effective.");
		if (filter == null)
			throw new IllegalArgumentException("Filter must be effective.");

		this.iterator = iterator;
		this.filter = filter;
	}

	/**
	 * Variable registering the unfiltered iterator
	 * of this filtered iterator.
	 * 
	 * @invar	The unfiltered iterator is effective.
	 * 			| iterator != null
	 */
	private final Iterator<T> iterator;

	/**
	 * Variable registering the filter
	 * of this filtered iterator.
	 * 
	 * @invar	The filter is effective.
	 * 			| filter != null
	 */
	private final Predicate<? super T> filter;

	@Override
	protected T computeNext() {
		// Loop while there are elements
		while (iterator.hasNext()) {
			// Get the next element in the iterator
			T element = iterator.next();
			// If this element satisfies the filter,
			// return it as next element
			if (filter.apply(element))
				return element;
		}
		// No more elements
		return endOfData();
	}

}
