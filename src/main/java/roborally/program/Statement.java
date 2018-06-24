package roborally.program;

import be.kuleuven.cs.som.annotate.Raw;

/**
 * A statement in a program.
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
public interface Statement {

	/**
	 * Check if the given object can be applied to this statement.
	 * 
	 * @param obj
	 * 			The next object to apply.
	 * 
	 * @return	False if the given object is a statement
	 * 			and it has this statement as a sub statement.
	 * 			| if (obj instanceof Statement)
	 * 			|   if (((Statement) obj).hasAsSubStatement(this))
	 * 			|      result == false
	 */
	public abstract boolean canApply(Object obj);

	/**
	 * Apply the given object to this statement.
	 * 
	 * @param obj
	 * 			The next object to apply.
	 * 
	 * @pre		The given object must be applicable
	 * 			to this statement.
	 * 			| canApply(obj)
	 */
	public abstract void apply(Object obj);

	/**
	 * Check whether this statement equals the given statement
	 * or has it as a sub statement.
	 * 
	 * @param statement
	 * 			The statement to check.
	 * 
	 * @return	True if the given statement is this statement.
	 * 			| if (this == statement)
	 * 			|   result == true
	 */
	public abstract boolean hasAsSubStatement(Statement statement);

	/**
	 * Check whether this statement has been properly constructed.
	 */
	@Raw
	public abstract boolean isConstructed();

	/**
	 * Get the source representation of this statement.
	 * 
	 * @throws	IllegalStateException
	 * 			If this statement is not properly constructed.
	 * 			| !isConstructed()
	 */
	public abstract String toSource() throws IllegalStateException;

}
