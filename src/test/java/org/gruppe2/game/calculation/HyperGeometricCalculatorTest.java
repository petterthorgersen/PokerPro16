package org.gruppe2.game.calculation;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HyperGeometricCalculatorTest {

	@Test
	public void testIfCalcutlationReturnCorrectValue() {
		assertTrue(HypergeometricCalculator.hypergeometricDistribution(50, 5, 10, 4)==0.003964583058015);
		assertTrue(HypergeometricCalculator.hypergeometricDistribution(50, 13, 5, 3)==0.089899752685533);		
		assertTrue(HypergeometricCalculator.hypergeometricDistribution(1000, 317, 218, 79)==0.017421580734361);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testIfExceptionIsThrown(){
		HypergeometricCalculator.hypergeometricDistribution(-1, 8, 100, -10);
	}
	
}
