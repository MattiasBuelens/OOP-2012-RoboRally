package roborally.util;

import java.util.*;

/**
 * A last-in-first-out stack wrapping around a deque.
 * 
 * @param <E>
 * 			The element type.
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
public class Stack<E> extends AbstractQueue<E> {

	public Stack(Deque<E> deque) {
		if (deque == null)
			throw new IllegalArgumentException("Deque must be effective.");
		this.deque = deque;
	}

	public Stack() {
		this(new ArrayDeque<E>());
	}

	private final Deque<E> deque;

	@Override
	public boolean offer(E e) {
		return deque.offerLast(e);
	}

	@Override
	public E peek() {
		return deque.peekLast();
	}

	@Override
	public E poll() {
		return deque.pollLast();
	}

	@Override
	public Iterator<E> iterator() {
		return deque.descendingIterator();
	}

	@Override
	public int size() {
		return deque.size();
	}

}
