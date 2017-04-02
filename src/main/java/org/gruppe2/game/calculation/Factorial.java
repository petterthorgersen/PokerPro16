package org.gruppe2.game.calculation;

import java.math.BigInteger;

public class Factorial {
	/**
	 * Calculates Factorial
	 * @param n - The number to calculate factorial of.
	 * @return result - Factorial of n as a BigInteger.
	 */
	public static BigInteger factorial(int n){
		if (n<0){
			throw new IllegalArgumentException("n must be positive!");
		}
		BigInteger result = BigInteger.valueOf(1);
		while (n>1){
			result = result.multiply(BigInteger.valueOf(n));
			n--;
		}
		return result;
	}
}
