package roborally;

import roborally.EnergyAmount.Unit;

/**
 * A class which carries a mutable amount of energy capacity.
 * 
 * @invar	The stored capacity is effective.
 * 			| getCapacity() != null
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
public interface CapacityCarrier {

	/**
	 * Get the stored capacity of this capacity carrier.
	 */
	public abstract EnergyProperty getCapacity();

	/**
	 * Get the amount of capacity stored by this capacity carrier.
	 * 
	 * @return	The result is the amount of stored capacity.
	 * 			| result == getCapacity().getAmount()
	 */
	public abstract EnergyAmount getCapacityAmount();

	/**
	 * Get the amount of capacity stored by this capacity carrier
	 * expressed in the given energy unit.
	 * 
	 * @param unit
	 * 			The energy unit.
	 * 
	 * @return	The result is the amount of stored capacity
	 * 			expressed in the given unit.
	 * 			| result == getCapacityAmount().getAmount(unit)
	 */
	public abstract double getCapacityAmount(Unit unit);

	/**
	 * Check whether the given amount of capacity is a valid amount
	 * for this capacity carrier.
	 * 
	 * @param capacity
	 * 			The amount of capacity to validate.
	 * 
	 * @return	False if the amount of capacity is not valid
	 * 			for this carrier's stored capacity.
	 * 			| if (!getCapacity().isValidAmount(amount))
	 * 			|   result == false
	 * @see EnergyProperty#isValidAmount(EnergyAmount)
	 */
	public abstract boolean isValidCapacity(EnergyAmount capacity);

	/**
	 * Transfer the given amount of capacity from this capacity carrier
	 * to the given receiving capacity carrier.
	 * 
	 * @param receiver
	 * 			The receiving capacity carrier.
	 * @param amount
	 * 			The amount of capacity to transfer.
	 * 
	 * @pre		The given amount can be transferred from this
	 * 			capacity carrier to the receiving capacity carrier.
	 * 			| canTransfer(receiver, amount)
	 * 
	 * @throws	UnsupportedOperationException
	 * 			If transferring energy is illegal
	 * 			for this capacity carrier.
	 */
	public abstract void transfer(CapacityCarrier receiver, EnergyAmount amount) throws UnsupportedOperationException;

	/**
	 * Check whether given amount of capacity can be transferred
	 * from this capacity carrier to the given capacity carrier.
	 * 
	 * @return	False if the receiving carrier is not effective.
	 * 			| if (receiver == null)
	 * 			|   result == false
	 * @return	False if the given amount cannot be transferred
	 * 			from this carrier's capacity property to
	 * 			the receiving carrier's capacity property.
	 * 			| else if (!getCapacity().canTransfer(receiver.getCapacity(), amount))
	 * 			|   result == false
	 */
	public abstract boolean canTransfer(CapacityCarrier receiver, EnergyAmount amount);

}
