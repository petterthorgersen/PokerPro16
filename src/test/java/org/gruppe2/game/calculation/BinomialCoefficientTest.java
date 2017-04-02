package org.gruppe2.game.calculation;

import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Test;

public class BinomialCoefficientTest {

	@Test
	public void testIfNOneBiggerThanKReturnsN() {
		for (int i =1;i<20;i++){
			assertTrue(BinomialCoefficient.binomialCoefficient(i, i-1).equals(BigInteger.valueOf(i)));
		}
	}
	
	@Test
	public void testCoefficietForHighValues(){
		long start = System.currentTimeMillis();
		BinomialCoefficient.binomialCoefficient(52, 7);
		System.out.println(System.currentTimeMillis()-start);
	}

}
