package org.gruppe2.game.calculation;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HypergeometricCalculator {
	/**
	 * 
	 * @param N - The size of the population(ie. Card deck population is 52)
	 * @param K - The number of success states in the population (ie. 13 hearts in a deck of cards)
	 * @param n - The number of draws
	 * @param k - Number of observed successes (ie. I want 5 hearts)
	 * @return result with 15 decimals
	 */
	public static double hypergeometricDistribution(int N, int K, int n, int k){
		if (K>N || k>n || k>N || n>n){
			throw new IllegalArgumentException("Values of K,n and k cannot be larger than the population!");
		}
		if (k>K){
			throw new IllegalArgumentException("You cannot draw more successes than there are success states in the population. In other words k cannot be larger than K");
		}
		if (N<=0 || K<=0 || n<=0 || k<0){
			throw new IllegalArgumentException("Values of N,K and n cannot be less than 1, value of k cannot be less than zero");
		}
		double result = (new BigDecimal(BinomialCoefficient.binomialCoefficient(K, k))
				.multiply(new BigDecimal(BinomialCoefficient.binomialCoefficient(N-K, n-k))))
				.divide(new BigDecimal(BinomialCoefficient.binomialCoefficient(N,n)),15,RoundingMode.HALF_UP).doubleValue();
		return result;
	}
}
