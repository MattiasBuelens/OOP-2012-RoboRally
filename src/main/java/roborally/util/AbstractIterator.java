package roborally.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An abstract implementation for iterators.
 * 
 * <p>Loosely based on AbstractIterator from the
 * <a href="http://code.google.com/p/guava-libraries/">Guava libraries</a>.</p>
 * </p>
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
public abstract class AbstractIterator<T> implements Iterator<T> {

	protected AbstractIterator() {
	}

	/**
	 * Variable registering the state of this iterator.
	 */
	private State state = State.NOT_READY;

	/**
	 * Variable registering the next element to be returned.
	 */
	private T nextElement;

	/**
	 * Get the next element to return.
	 * 
	 * <p>The implementation must call {@link #endOfData()}
	 * when there are no elements left in the iteration.
	 * If not, the iteration may end up in an infinite loop.</p>
	 * 
	 * <p>Any exceptions thrown are propagated to the original
	 * {@link #hasNext()} or {@link #next()} method and
	 * any subsequent call to these methods will result
	 * in an {@code IllegalStateException}.</p>
	 */
	protected abstract T computeNext();

	/**
	 * Implementation of {@link #computeNext} <b>must</b>
	 * invoke this method to signal that there are
	 * no elements left in the iteration.
	 * 
	 * @return	<code>null</code>
	 */
	protected final T endOfData() {
		state = State.DONE;
		return null;
	}

	@Override
	public final boolean hasNext() {
		if (state == State.FAILED)
			throw new IllegalStateException();

		if (state == State.DONE) {
			// No more elements
			return false;
		} else if (state == State.READY) {
			// Already computed the next element
			return true;
		}

		return tryComputeNext();
	}

	private boolean tryComputeNext() {
		// Assume that the computation fails
		state = State.FAILED;
		// Compute the next element
		nextElement = computeNext();
		// If not done, set state to ready
		if (state != State.DONE) {
			state = State.READY;
			return true;
		}
		return false;
	}

	@Override
	public final T next() {
		if (!hasNext())
			throw new NoSuchElementException();

		// A new element will have to be computed
		state = State.NOT_READY;
		return nextElement;
	}

	@Override
	public void remove() throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	private enum State {
		/**
		 * A new element was computed and is ready to be returned.
		 */
		READY,

		/**
		 * A new element needs to be computed.
		 */
		NOT_READY,

		/**
		 * All elements have been iterated and returned.
		 */
		DONE,

		/**
		 * An error has occured in {@link AbstractIterator#computeNext()}.
		 */
		FAILED
	};
}
