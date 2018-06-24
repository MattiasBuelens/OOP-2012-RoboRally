package roborally;

import be.kuleuven.cs.som.annotate.Basic;
import be.kuleuven.cs.som.annotate.Immutable;
import be.kuleuven.cs.som.annotate.Value;

/**
 * A value class of energy amounts.
 * 
 * @invar	This energy amount has a valid unit.
 * 			| canHaveAsUnit(getUnit())
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
@Value
public class EnergyAmount implements Comparable<EnergyAmount> {

	public static final EnergyAmount ZERO = new EnergyAmount(0.0d, Unit.WATTSECOND);

	/**
	 * Create a new energy amount with the given amount and unit.
	 * 
	 * @pre		The given unit must be valid.
	 * 			| canHaveAsUnit(unit)
	 * 
	 * @post	The amount of the new energy amount expressed
	 * 			in the given unit is equal to the given amount.
	 * 			| new.getAmount(unit) == amount
	 * @post	This energy amount's unit is set to the given unit.
	 * 			| new.getUnit() == unit
	 */
	public EnergyAmount(double amount, Unit unit) {
		assert canHaveAsUnit(unit);

		this.amount = amount;
		this.unit = unit;
	}

	/**
	 * Get the amount of this energy amount
	 * expressed in the unit of this energy amount.
	 */
	@Basic
	@Immutable
	public double getAmount() {
		return this.amount;
	}

	/**
	 * Variable representing the amount of this energy amount.
	 */
	private double amount;

	/**
	 * Get the amount of this energy amount
	 * expressed in the given unit.
	 * 
	 * @param unit
	 * 			The unit to express the amount in.
	 * 
	 * @return	The result is the amount of this energy amount
	 * 			converted from this energy amount's unit
	 * 			to the given unit.
	 * 			| result == getUnit().convertTo(getAmount(), unit)
	 */
	public double getAmount(Unit unit) {
		return getUnit().convertTo(getAmount(), unit);
	}

	/**
	 * Get the unit of this energy amount.
	 */
	@Basic
	@Immutable
	public Unit getUnit() {
		return this.unit;
	}

	/**
	 * Variable representing the unit of this energy amount.
	 * 
	 * @invar	The unit is effective.
	 * 			| unit != null
	 */
	private Unit unit;

	/**
	 * Check whether the given unit is valid for this energy amount.
	 * 
	 * @param unit
	 * 			The unit to validate.
	 * 
	 * @return	True if and only if the given unit is effective.
	 * 			| result == (unit != null)
	 */
	public boolean canHaveAsUnit(Unit unit) {
		return unit != null;
	}

	/**
	 * Convert this energy amount to the given unit.
	 * 
	 * @param unit
	 * 			The unit to convert to.
	 * 
	 * @pre		The given unit must be valid.
	 * 			| canHaveAsUnit(unit)
	 * 
	 * @return	The resulting amount has the given unit as its unit.
	 * 			| result.getUnit() == unit
	 * @return  The resulting amount has the amount of this energy
	 * 			amount expressed in the given unit as its amount.
	 * 			| result.getAmount() == this.getAmount(unit)
	 */
	public EnergyAmount convertTo(Unit unit) {
		return new EnergyAmount(getAmount(unit), unit);
	}

	@Override
	public String toString() {
		return getAmount() + " " + getUnit();
	}

	/**
	 * Add a term to this energy amount.
	 * 
	 * @param term
	 * 			The term to add.
	 * 
	 * @return	The resulting amount has the sum of this amount
	 * 			and the given term as its amount.
	 * 			| result.getAmount() == this.getAmount() + term
	 * @return	The resulting amount has the same unit as this amount.
	 * 			| result.getUnit() == this.getUnit()
	 */
	public EnergyAmount add(double term) {
		return new EnergyAmount(getAmount() + term, getUnit());
	}

	/**
	 * Add the given amount to this energy amount.
	 * 
	 * @param amount
	 * 			The energy amount to add.
	 * 
	 * @return	If the given amount is not effective,
	 * 			nothing is added and this amount is returned.
	 * 			| if (amount == null)
	 * 			|   result == this
	 * @return	Otherwise, the given amount converted to this amount's unit
	 * 			is added to this energy amount.
	 * 			| else
	 * 			|   result.equals( this.add(amount.getAmount(getUnit())) )
	 */
	public EnergyAmount add(EnergyAmount amount) {
		if (amount == null)
			return this;
		return add(amount.getAmount(getUnit()));
	}

	/**
	 * Substract the given term from this energy amount.
	 * 
	 * @param term
	 * 			The term to subtract.
	 * 
	 * @return	The opposite value of the term is added
	 * 			to this amount.
	 * 			| result == add(-term)
	 */
	public EnergyAmount subtract(double term) {
		return add(-term);
	}

	/**
	 * Subtract the given amount from this energy amount.
	 * 
	 * @param amount
	 * 			The energy amount to subtract.
	 * 
	 * @return	If the given amount is not effective,
	 * 			nothing is subtracted and this amount is returned.
	 * 			| if (amount == null)
	 * 			|   result == this
	 * @return	Otherwise, the opposite of the given amount converted
	 * 			to this amount's unit is added from this energy amount.
	 * 			| else
	 * 			|   result.equals( this.add(-amount.getAmount(getUnit())) )
	 */
	public EnergyAmount subtract(EnergyAmount amount) {
		if (amount == null)
			return this;
		return add(-amount.getAmount(getUnit()));
	}

	/**
	 * Multiply this energy amount with the given factor.
	 * 
	 * @param factor
	 * 			The factor to multiply with.
	 * 
	 * @return	The resulting amount has the product of this amount
	 * 			and the given factor as its amount.
	 * 			| result.getAmount() == this.getAmount() * term
	 * @return	The resulting amount has the same unit as this amount.
	 * 			| result.getUnit() == this.getUnit()
	 */
	public EnergyAmount multiply(double factor) {
		return new EnergyAmount(getAmount() * factor, getUnit());
	}

	/**
	 * Get this energy amount as a fraction of a given energy amount.
	 * 
	 * @param amount
	 * 			The amount where this amount is a fraction of.
	 * 
	 * @return	The result is the amount of this energy amount divided
	 * 			by the given amount expressed in this amount's unit.
	 * 			This double division is precise to at least two decimals.
	 * 			| result ==  this.getAmount() / amount.getAmount(this.getUnit())
	 * 
	 * @throws	IllegalArgumentException
	 * 			If the given amount is not effective or zero.
	 * 			| amount == null || amount.equals(EnergyAmount.ZERO)
	 */
	public double asFractionOf(EnergyAmount amount) {
		if (amount == null || amount.equals(EnergyAmount.ZERO))	
			throw new IllegalArgumentException("Amount must be effective and non-zero.");
		return getAmount() / amount.getAmount(getUnit());
	}

	/**
	 * Compare this energy amount with the given energy amount.
	 * 
	 * @param other
	 * 			The other energy amount to compare to.
	 * 
	 * @return	If the given amount is not effective,
	 * 			this amount is compared to the zero amount.
	 * 			| if (other == null)
	 * 			|   result == compareTo(EnergyAmount.ZERO)
	 * @return	Otherwise, the amount of this energy amount
	 * 			is compared to the amount of the other energy amount
	 * 			expressed in this amount's unit.
	 * 			| else
	 * 			|   result == Double.compare(
	 * 			|      this.getAmount(),
	 * 			|      other.getAmount(this.getUnit())
	 * 			|   )
	 * 
	 * @see Double#compare(double, double)
	 */
	@Override
	public int compareTo(EnergyAmount other) {
		if (other == null)
			return compareTo(EnergyAmount.ZERO);

		return Double.compare(getAmount(), other.getAmount(getUnit()));
	}

	/**
	 * Check whether this energy amount is less than the given energy amount.
	 * 
	 * @param other
	 * 			The other energy amount to compare to.
	 * 
	 * @return	True if and only if the comparison of this amount
	 * 			and the given amount gives a negative result.
	 * 			| result == (compareTo(other) < 0)
	 */
	public boolean isLessThan(EnergyAmount other) {
		return compareTo(other) < 0;
	}

	/**
	 * Check whether this energy amount is less than
	 * or equal to the given energy amount.
	 * 
	 * @param other
	 * 			The other energy amount to compare to.
	 * 
	 * @return	True if and only if the comparison of this amount
	 * 			and the given amount gives a non-positive result.
	 * 			| result == (compareTo(other) <= 0)
	 */
	public boolean isLessThanOrEqual(EnergyAmount other) {
		return compareTo(other) <= 0;
	}

	/**
	 * Check whether this energy amount is greater than the given energy amount.
	 * 
	 * @param other
	 * 			The other energy amount to compare to.
	 * 
	 * @return	True if and only if the comparison of this amount
	 * 			and the given amount gives a positive result.
	 * 			| result == (compareTo(other) > 0)
	 */
	public boolean isGreaterThan(EnergyAmount other) {
		return compareTo(other) > 0;
	}

	/**
	 * Check whether this energy amount is greater than
	 * or equal to the given energy amount.
	 * 
	 * @param other
	 * 			The other energy amount to compare to.
	 * 
	 * @return	True if and only if the comparison of this amount
	 * 			and the given amount gives a non-negative result.
	 * 			| result == (compareTo(other) >= 0)
	 */
	public boolean isGreaterThanOrEqual(EnergyAmount other) {
		return compareTo(other) >= 0;
	}

	/**
	 * Get the minimum of two energy amounts.
	 * 
	 * @return	If both amounts are not effective, null is returned.
	 * 			| if (first == null && second == null)
	 * 			|   result == null
	 * @return	The result equals one of the given amounts.
	 * 			| else
	 * 			|   ( result.equals(first) || result.equals(second) )
	 * @return	The result is less than or equal to both amounts.
	 * 		 	|   && ( result.isLessThanOrEqual(first)
	 * 			|         && result.isLessThanOrEqual(second) )
	 */
	public static EnergyAmount min(EnergyAmount first, EnergyAmount second) {
		if (first != null) {
			// First argument is effective
			return (first.isLessThan(second)) ? first : second;
		} else if (second != null) {
			// Second argument is effective
			return (second.isGreaterThan(first)) ? first : second;
		} else {
			// Both arguments are null
			return null;
		}
	}

	/**
	 * Get the maximum of two energy amounts.
	 * 
	 * @return	If both amounts are not effective, null is returned.
	 * 			| if (first == null && second == null)
	 * 			|   result == null
	 * @return	The result equals one of the given amounts.
	 * 			| else
	 * 			|   ( result.equals(first) || result.equals(second) )
	 * @return	The result is greater than or equal to both amounts.
	 * 		 	|   && ( result.isGreaterThanOrEqual(first)
	 * 			|         && result.isGreaterThanOrEqual(second) )
	 */
	public static EnergyAmount max(EnergyAmount first, EnergyAmount second) {
		if (first != null) {
			// First argument is effective
			return (first.isGreaterThan(second)) ? first : second;
		} else if (second != null) {
			// Second argument is effective
			return (second.isLessThan(first)) ? first : second;
		} else {
			// Both arguments are null
			return null;
		}
	}

	/**
	 * @return	True if the given object reference equals this object reference.
	 * 			| if (this == obj)
	 * 			|   result == true
	 * @return	False if the given object is not effective.
	 * 			| else if (obj == null)
	 * 			|   result == false
	 * @return	False if the given object is not an energy amount.
	 * 			| else if (getClass() != obj.getClass())
	 * 			|   result == false
	 * @return	Otherwise, true if and only if the amounts are equal.
	 * 			| else
	 * 			|   let
	 * 			|      other = (EnergyAmount) obj
	 * 			|   result == (this.compareTo(other) == 0)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EnergyAmount other = (EnergyAmount) obj;
		return (this.compareTo(other) == 0);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp = Double.doubleToLongBits(getAmount());
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * An enumeration of energy units.
	 */
	public static enum Unit {

		WATTSECOND("Ws") {
			@Override
			public double convertTo(double amount, Unit unit) {
				switch (unit) {
				case JOULE:
				case WATTSECOND:
					return amount;
				case KILOJOULE:
					return amount / 1000;
				default:
					throw new IllegalArgumentException(String.format("Cannot convert to %s", unit.getDimension()));
				}
			}
		},
		JOULE("J") {
			@Override
			public double convertTo(double amount, Unit unit) {
				return WATTSECOND.convertTo(amount, unit);
			}
		},
		KILOJOULE("kJ") {
			@Override
			public double convertTo(double amount, Unit unit) {
				switch (unit) {
				case KILOJOULE:
					return amount;
				case JOULE:
				case WATTSECOND:
					return amount * 1000;
				default:
					throw new IllegalArgumentException(String.format("Cannot convert to %s", unit.getDimension()));
				}
			}
		};

		private Unit(String dimension) {
			this.dimension = dimension;
		}

		/**
		 * Get the dimension of this unit.
		 */
		@Basic
		@Immutable
		public String getDimension() {
			return dimension;
		}

		/**
		 * Variable registering the dimension of this unit.
		 */
		private final String dimension;

		/**
		 * Convert a given amount expressed in this unit to the given unit.
		 * 
		 * @return	Conversions must be reflexive.
		 * 			If the result is converted again to this unit,
		 * 			the value must equal the given amount.
		 * 			| unit.convertTo(result, this) == amount
		 * 
		 * @throws	IllegalArgumentException
		 * 			If the conversion is undefined or illegal.
		 */
		public abstract double convertTo(double amount, Unit unit) throws IllegalArgumentException;

		public String toString() {
			return getDimension();
		}

	}

}
