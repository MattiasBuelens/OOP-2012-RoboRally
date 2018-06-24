package roborally;

import roborally.EnergyAmount.Unit;

/**
 * A class which carries a mutable amount of energy storage.
 * 
 * @invar	The stored energy is effective.
 * 			| getEnergy() != null
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
public interface EnergyCarrier {

	/**
	 * Get the stored energy of this energy carrier.
	 */
	public abstract EnergyProperty getEnergy();

	/**
	 * Get the amount of energy stored by this energy carrier.
	 * 
	 * @return	The result is the amount of stored energy.
	 * 			| result == getEnergy().getAmount()
	 */
	public abstract EnergyAmount getEnergyAmount();

	/**
	 * Get the amount of energy stored by this energy carrier
	 * expressed in the given energy unit.
	 * 
	 * @param unit
	 * 			The energy unit.
	 * 
	 * @return	The result is the amount of stored energy
	 * 			expressed in the given unit.
	 * 			| result == getEnergyAmount().getAmount(unit)
	 */
	public abstract double getEnergyAmount(Unit unit);

	/**
	 * Check whether the given amount of energy is a valid amount
	 * for this energy carrier.
	 * 
	 * @param amount
	 * 			The amount of energy to validate.
	 * 
	 * @return	False if the amount of energy is not valid
	 * 			for this carrier's stored energy.
	 * 			| if (!getEnergy().isValidAmount(amount))
	 * 			|   result == false
	 * @see EnergyProperty#isValidAmount(EnergyAmount)
	 */
	public abstract boolean isValidEnergy(EnergyAmount amount);

	/**
	 * Transfer the given amount of energy from this energy carrier
	 * to the given receiving energy carrier.
	 * 
	 * @param receiver
	 * 			The receiving energy carrier.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @pre		The given amount can be transferred from this
	 * 			energy carrier to the receiving energy carrier.
	 * 			| canTransfer(receiver, amount)
	 * 
	 * @throws	UnsupportedOperationException
	 * 			If transferring energy is illegal
	 * 			for this energy carrier.
	 */
	public abstract void transfer(EnergyCarrier receiver, EnergyAmount amount) throws UnsupportedOperationException;

	/**
	 * Check whether given amount of energy can be transferred
	 * from this energy carrier to the given energy carrier.
	 * 
	 * @return	False if the receiving carrier is not effective.
	 * 			| if (receiver == null)
	 * 			|   result == false
	 * @return	False if the given amount cannot be transferred
	 * 			from this carrier's energy property to
	 * 			the receiving carrier's energy property.
	 * 			| else if (!getEnergy().canTransfer(receiver.getEnergy(), amount))
	 * 			|   result == false
	 */
	public abstract boolean canTransfer(EnergyCarrier receiver, EnergyAmount amount);

}
