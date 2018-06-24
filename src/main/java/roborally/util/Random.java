package roborally.util;

/**
 * An extension of the default random number generator.
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
public class Random extends java.util.Random {

	/**
	 * Returns a pseudorandom, uniformly distributed {@code long} value
	 * between 0 (inclusive) and the specified value (exclusive),
	 * drawn from this random number generator's sequence.
	 * 
	 * @param n
	 * 			The bound on the random number to be returned. Must be positive.
	 * 
	 * @return	The next pseudorandom, uniformly distributed <code>long</code> value
	 * 			between 0 (inclusive) and n (exclusive) from this random number
	 * 			generator's sequence.
	 * 
	 * @note	Original by KennyTM on Stack Overflow.
	 * 			http://stackoverflow.com/a/2546186
	 */
	public long nextLong(long n) {
		if (n <= 0)
			throw new IllegalArgumentException("n must be positive");

		long bits, val;
		do {
			bits = (nextLong() << 1) >>> 1;
			val = bits % n;
		} while (bits - val + (n - 1) < 0L);
		return val;
	}

	private static final long serialVersionUID = 1L;

}
