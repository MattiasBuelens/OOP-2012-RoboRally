package roborally.util;

import java.util.*;

/**
 * A list wrapper class which enforces sorting on the wrapped list.
 * 
 * @param <E>
 * 			The element type.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class SortedList<E> extends AbstractList<E> {

	private final List<E> list;
	private final Comparator<E> comparator;

	protected SortedList(List<E> list, Comparator<E> comparator) {
		this.list = list;
		this.comparator = comparator;
	}

	public SortedList(Comparator<E> comparator) {
		this(new ArrayList<E>(), comparator);
	}

	@Override
	public boolean add(E e) {
		int index = Collections.binarySearch(list, e, comparator);
		// If element does not already exists in list,
		// get the insertion point
		if (index < 0) {
			index = -index - 1;
		}
		list.add(index, e);
		return true;
	}

	@Override
	public void add(int index, E e) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			add(e);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean contains(Object o) {
		return list.contains(o);
	}

	@Override
	public E get(int index) {
		return list.get(index);
	}

	@Override
	public E set(int index, E element) {
		E oldElement = remove(index);
		add(element);
		return oldElement;
	}

	@Override
	public E remove(int index) {
		return list.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return list.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}

	@Override
	public int indexOf(Object o) {
		return list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return list.lastIndexOf(o);
	}

	@Override
	public Iterator<E> iterator() {
		return list.iterator();
	}

	@Override
	public ListIterator<E> listIterator() {
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return new SortedList<E>(list.subList(fromIndex, toIndex), comparator);
	}

	@Override
	public Object[] toArray() {
		return list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return list.toArray(a);
	}

	@Override
	public int size() {
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		return list.isEmpty();
	}

	@Override
	public void clear() {
		list.clear();
	}

	@Override
	public boolean equals(Object o) {
		return list.equals(o);
	}

	@Override
	public int hashCode() {
		return list.hashCode();
	}
}
