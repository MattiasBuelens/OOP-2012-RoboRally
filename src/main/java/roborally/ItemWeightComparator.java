package roborally;

import java.util.Comparator;

/**
 * A comparator which compares item objects on their weight.
 * 
 * @note	This comparator imposes orderings that are inconsistent with equals.
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
public class ItemWeightComparator<I extends Item> implements Comparator<I> {

	/**
	 * Compares two items for order.
	 * 
	 * @param item1
	 * 			The first item.
	 * @param item2
	 * 			The second item.
	 * 
	 * @return	If the item weights are equal, zero is returned.
	 * 			| if (item1.getWeight() == item2.getWeight())
	 * 			|   result == 0
	 * @return	If the weight of the first item is less than
	 * 			the weight of the second item, a negative value
	 * 			is returned.
	 * 			| else if (item1.getWeight() < item2.getWeight())
	 * 			|   result < 0
	 * @return	If the weight of the first item is greater than
	 * 			the weight of the second item, a positive value
	 * 			is returned.
	 * 			| else
	 * 			|   result > 0
	 */
	@Override
	public int compare(I item1, I item2) {
		// This subtraction won't overflow
		// because both weights are non-negative
		// as specified by Item.isValidWeight(int)
		return item1.getWeight() - item2.getWeight();
	}

}
