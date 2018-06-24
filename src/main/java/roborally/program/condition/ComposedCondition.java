package roborally.program.condition;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Raw;

import roborally.program.Statement;

/**
 * A condition composed of multiple conditions.
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
public abstract class ComposedCondition implements Condition {

	@Override
	public boolean canApply(Object obj) {
		if (!(obj instanceof Condition))
			return false;
		return canApply((Condition) obj);
	}

	protected boolean canApply(Condition condition) {
		return canAddAsCondition(condition);
	}

	@Override
	public void apply(Object obj) {
		assert canApply(obj);
		addAsCondition((Condition) obj);
	}

	/**
	 * Get the number of conditions in this composed condition.
	 */
	@Basic
	@Raw
	public int getNbConditions() {
		return conditions.size();
	}

	/**
	 * Get the command at the given index in this composed command.
	 * 
	 * @param index
	 * 			The index of the command.
	 * 
	 * @throws	IndexOutOfBoundsException
	 * 			If the given index is non-positive or exceeds
	 * 			the number of conditions of this composed condition.
	 * 			| index <= 0 || index > getNbConditions()
	 */
	@Basic
	@Raw
	public Condition getConditionAt(int index) {
		if (index <= 0 || index > getNbConditions())
			throw new IndexOutOfBoundsException();
		return conditions.get(index - 1);
	}

	/**
	 * Check whether the given condition is valid
	 * for this composed condition.
	 *
	 * @param condition
	 *			The condition to validate.
	 *
	 * @return	False if the given condition is not effective.
	 *			| if (condition != null)
	 *			|   result == false
	 * @return	False if the given condition is not properly
	 *			constructed.
	 *			| else if (!condition.isConstructed())
	 *			|   result == false
	 * @return	Otherwise, true if and only if the given
	 * 			condition does not have has this composed
	 * 			condition as a sub statement.
	 * 			| else
	 * 			|   result == !condition.hasAsSubStatement(this)
	 */
	public boolean canHaveAsCondition(Condition condition) {
		if (condition == null)
			return false;
		if (!condition.isConstructed())
			return false;
		if (condition.hasAsSubStatement(this))
			return false;
		return true;
	}

	/**
	 * Check whether the given condition can be added
	 * to this composed condition.
	 * 
	 * @param condition
	 *			The condition to add.
	 *
	 * @return	False if this composed condition cannot have
	 * 			the given condition.
	 * 			| if (!canHaveAsCondition(condition))
	 * 			|   result == false
	 * @return	False if this composed condition has the
	 * 			given condition as a sub statement.
	 * 			| else if (hasAsSubStatement(condition))
	 * 			|   result == false
	 */
	public boolean canAddAsCondition(Condition condition) {
		return canHaveAsCondition(condition) && !hasAsSubStatement(condition);
	}

	/**
	 * Add the given condition to this composed condition.
	 * 
	 * @param condition
	 * 			The condition to add.
	 * 
	 * @post	The number of conditions in this composed
	 * 			condition is incremented by one.
	 * 			| new.getNbConditions() == this.getNbConditions() + 1
	 * @post	The given condition is added at the end
	 * 			of this composed condition.
	 * 			| new.getConditionAt(new.getNbConditions()) == condition
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given condition cannot be added to this
	 * 			composed condition.
	 * 			| !canAddAsCondition(condition)
	 */
	public void addAsCondition(Condition condition) {
		if (!canAddAsCondition(condition))
			throw new IllegalArgumentException("Invalid condition for composed condition: " + condition);
		conditions.add(condition);
	}

	/**
	 * List of conditions in this composed condition.
	 * 
	 * @invar	The list of conditions is effective.
	 * 			| conditions != null
	 * @invar	This composed condition can have each condition
	 * 			in the list.
	 * 			| for each condition in conditions:
	 * 			|   canHaveAsCondition(condition)
	 */
	private List<Condition> conditions = new ArrayList<Condition>();

	@Override
	public boolean hasAsSubStatement(Statement statement) {
		if (this == statement)
			return true;

		for (int i = 1; i <= getNbConditions(); ++i) {
			if (getConditionAt(i).hasAsSubStatement(statement))
				return true;
		}
		return false;
	}

}
