package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by �smund on 11/04/2016.
 */
public class ThreeOfAKindTest {

	public final ThreeOfAKind threeOfAKind = new ThreeOfAKind();
	public final int N = 10000;

	@Test
	public void canGetThreeOfAKindReturnsTrueWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(2, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(14, Card.Suit.CLUBS));

		assertEquals(true, threeOfAKind.canGet(commCards));
	}

	@Test
	public void canGetThreeOfAKindReturnsFalseWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));

		assertEquals(true, !threeOfAKind.canGet(commCards));
	}

	@Test
	public void isHandReturnsTrueWhenHandIsThreeOfAKind() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));

		assertEquals(true, threeOfAKind.isHand(commCards));
	}

	@Test
	public void isHandReturnsFalseWhenHandIsNotThreeOfAKind() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(2, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(14, Card.Suit.CLUBS));

		assertEquals(true, !threeOfAKind.isHand(commCards));
	}

	@Test
	public void compareShouldReturnNullWhenComparingSameHand() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(2, Card.Suit.SPADES));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(6, Card.Suit.CLUBS));
			commCards.add(new Card(8, Card.Suit.CLUBS));
			commCards.add(new Card(10, Card.Suit.CLUBS));
			
			commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

			assertEquals(true, threeOfAKind.compare(commCards, commCards) == 0);
		}
	}

	@Test
	public void compareShouldReturnPositiveWhenComparingHigherHandWithLower() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(3, Card.Suit.CLUBS));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		
		commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(2, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(2, Card.Suit.SPADES));
		commCardsCompare.add(new Card(2, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(6, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(8, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(10, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(12, Card.Suit.CLUBS));
		
		commCardsCompare.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		assertEquals(true,
				threeOfAKind.compare(commCards, commCardsCompare) == 1);
		}
	}

	@Test
	public void compareShouldReturnNegativeWhenComparingLowerHandWithHigher() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(3, Card.Suit.CLUBS));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		
		commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(5, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(5, Card.Suit.SPADES));
		commCardsCompare.add(new Card(5, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(6, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(8, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(10, Card.Suit.CLUBS));
		
		commCardsCompare.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		assertEquals(true,
				threeOfAKind.compare(commCards, commCardsCompare) == -1);
		}
	}
	
	@Test
	public void probabilityIsCorrectTest(){
		List<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(3, Card.Suit.CLUBS));
		commCards.add(new Card(5, Card.Suit.CLUBS));
		commCards.add(new Card(7, Card.Suit.CLUBS));
		ThreeOfAKind tk = new ThreeOfAKind();
		double prob = tk.probability(commCards);
		System.out.println(prob);
		assertTrue(prob==0.043478260869565);
	}
	@Test
	public void probabilityReturnsZeroWhenFourOfAKindIsUnaviable(){
		List<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(7, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(3, Card.Suit.DIAMONDS));
		commCards.add(new Card(5, Card.Suit.CLUBS));
		commCards.add(new Card(12,Card.Suit.HEARTS));
		ThreeOfAKind tk = new ThreeOfAKind();
		assertTrue(tk.probability(commCards)==0);
	}
	@Test
	public void probabilityReturnsOneWhenAlreadyHavingFourOfAKind(){
		List<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(4, Card.Suit.DIAMONDS));
		commCards.add(new Card(5, Card.Suit.HEARTS));
		ThreeOfAKind tk = new ThreeOfAKind();
		assertTrue(tk.probability(commCards)==1);
	}
}