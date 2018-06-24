package roborally;

import java.util.Comparator;

/**
 * A comparator which compares item objects on their weight.
 * 
 * @note	This comparator imposes orderings that are inconsistent with equals.
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public class ItemWeightComparator implements Comparator<Item> {

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
	 * 			|   result < 0
	 */
	@Override
	public int compare(Item item1, Item item2) {
		Integer weight1 = Integer.valueOf(item1.getWeight());
		Integer weight2 = Integer.valueOf(item2.getWeight());
		return weight1.compareTo(weight2);
	}

}
