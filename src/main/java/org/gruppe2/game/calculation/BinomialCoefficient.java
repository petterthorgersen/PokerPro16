package org.gruppe2.game.calculation;

import java.math.BigInteger;

public class BinomialCoefficient {
	/**
	 * Calculates the binomialcoefficient from the values n and k (nCk).
	 * @param n
	 * @param k
	 * @return result - The binomialcoefficient as a BigInteger.
	 */
	public static BigInteger binomialCoefficient(int n, int k){
		if (n<k){
			throw new IllegalArgumentException("Value of k can not be larger than n");
		}
		if (n<=0 || k<0){
			throw new IllegalArgumentException("Values of k and n must be larger than zero");
		}
		BigInteger result=BigInteger.valueOf(1);
		result=Factorial.factorial(n).divide((Factorial.factorial(k).multiply(Factorial.factorial(n-k))));
		return result;
		
	}
}
