package roborally.program.condition;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Raw;

/**
 * A composed condition with
 * a fixed amount of conditions.
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
public abstract class FixedSizeComposedCondition extends ComposedCondition {

	/**
	 * Create a new fixed size composed condition
	 * with the given size.
	 * 
	 * @param size
	 * 			The size for the new condition.
	 * 
	 * @post	The new condition's size is set to
	 * 			the absolute value of the given size.
	 * 			| new.getSize() == Math.abs(size)
	 */
	protected FixedSizeComposedCondition(int size) {
		this.size = Math.abs(size);
	}

	/**
	 * Get the size of this fixed size composed condition.
	 */
	@Basic
	@Raw
	@Immutable
	public int getSize() {
		return size;
	}

	/**
	 * Variable registering the size of
	 * this fixed size composed condition.
	 */
	private final int size;

	/**
	 * @return	True if and only if the number of conditions
	 * 			if this composed condition equals its size.
	 * 			| result == getNbConditions() == getSize()
	 */
	@Override
	public boolean isConstructed() {
		return getNbConditions() == getSize();
	}

	/**
	 * @return	False if the number of conditions in
	 * 			this composed condition exceeds its size.
	 * 			| if (getNbConditions() >= getSize())
	 * 			|   result == false
	 * @return	False if this composed condition cannot have
	 * 			the given condition.
	 * 			| else if (!canHaveAsCondition(condition))
	 * 			|   result == false
	 * @return	Otherwise, true if and only if this composed
	 * 			condition does not have the given condition
	 * 			as a sub statement.
	 * 			| else
	 * 			|   result == !hasAsSubStatement(condition)
	 */
	@Override
	public boolean canAddAsCondition(Condition condition) {
		// False if the number of conditions
		// exceeds the size
		if (getNbConditions() >= getSize())
			return false;

		return super.canAddAsCondition(condition);
	}

}
