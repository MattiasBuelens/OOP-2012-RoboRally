package roborally;

/**
 * Represents an object which carries energy in a game of RoboRally.
 * 
 * <p>Amounts of energy are expressed in Watt seconds (Ws) or Joules (J).</p>
 * 
 * @author Mattias Buelens
 * @author Thomas Goossens
 * @version 2.0
 */
public interface EnergyCarrier {

	/**
	 * Get the amount of energy stored by this energy carrier.
	 */
	public double getEnergy();

	/**
	 * Set the amount of energy stored by this energy carrier.
	 * 
	 * @param energy
	 * 			The new amount of energy.
	 * 
	 * @post	The new amount of energy equals the given amount.
	 * 			| new.getEnergy() == energy
	 */
	public void setEnergy(double energy);

	/**
	 * Get the maximum amount of energy that can be stored by this energy carrier.
	 */
	public double getMaximumEnergy();

	/**
	 * Recharge this energy carrier with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @pre		This energy carrier can be recharged with the given amount.
	 * 			| canRecharge(amount)
	 * @effect	This carrier's energy is increased with the given amount.
	 * 			| setEnergy(getEnergy() + amount)
	 * @see #setEnergy(double)
	 */
	public void recharge(double amount);

	/**
	 * Check whether this energy carrier can be recharged
	 * with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @return	False if the extra energy amount is strictly negative.
	 * 			| if (amount < 0)
	 * 			|   result == false
	 * @return	False if the new amount of energy would be invalid.
	 * 			| else if (!isValidEnergy(getEnergy() + amount))
	 * 			|   result == false
	 */
	public boolean canRecharge(double amount);

	/**
	 * Drain this energy carrier with the given amount of energy.
	 * 
	 * @param amount
	 * 			The amount of energy to drain.
	 * 
	 * @pre		This energy carrier can be drained with the given amount.
	 * 			| canDrain(amount)
	 * @effect	This carrier's energy is reduced with the given amount.
	 * 			| setEnergy(getEnergy() - amount)
	 * @see #setEnergy(double)
	 */
	public void drain(double amount);

	/**
	 * Check whether this energy carrier can be drained
	 * with the given amount of energy.
	 * 
	 * @param amount
	 * 			The extra amount of energy.
	 * 
	 * @return	False if the extra energy amount is strictly negative.
	 * 			| if (amount < 0)
	 * 			|   result == false
	 * @return	False if the new amount of energy would be invalid.
	 * 			| else if (!isValidEnergy(getEnergy() - amount))
	 * 			|   result == false
	 */
	public boolean canDrain(double amount);

	/**
	 * Transfer the given amount of energy from this energy carrier
	 * to the given receiving carrier.
	 * 
	 * @param receivingCarrier
	 * 			The receiving energy carrier.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @pre		The given amount can be transferred
	 * 			from this energy carrier to the receiving carrier.
	 * 			| canTransfer(receivingCarrier, amount)
	 */
	public void transfer(EnergyCarrier receivingCarrier, double amount);

	/**
	 * Check whether given amount of energy can be transferred
	 * from this energy carrier to the given receiving carrier.
	 * 
	 * @param receivingCarrier
	 * 			The receiving energy carrier.
	 * @param amount
	 * 			The amount of energy to transfer.
	 * 
	 * @return	False if the receiving carrier is not effective.
	 * 			| if (receivingCarrier == null)
	 * 			|   result == false
	 * @return	False if the given amount is strictly negative.
	 * 			| if (amount < 0)
	 * 			|   result == false
	 */
	public boolean canTransfer(EnergyCarrier receivingCarrier, double amount);

}
