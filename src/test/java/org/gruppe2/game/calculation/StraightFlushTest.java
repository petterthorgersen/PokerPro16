package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by Mikal on 12.04.2016.
 */
public class StraightFlushTest {

	public final StraightFlush straightFlush = new StraightFlush();
	public final int N = 1;

	@Test
	public void canGetStraightFlushReturnsTrueWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.CLUBS));
		commCards.add(new Card(3, Card.Suit.CLUBS));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(5, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.HEARTS));
		commCards.add(new Card(10, Card.Suit.SPADES));

		assertEquals(true, straightFlush.canGet(commCards));
	}

	@Test
	public void canGetStraightFlushReturnsFalseWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(5, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));

		assertEquals(true, !straightFlush.canGet(commCards));
	}

	@Test
	public void isHandReturnsTrueWhenHandIsStraightFlush() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.HEARTS));
		commCards.add(new Card(5, Card.Suit.HEARTS));
		commCards.add(new Card(14, Card.Suit.HEARTS));
		commCards.add(new Card(10, Card.Suit.SPADES));
		commCards.add(new Card(7, Card.Suit.CLUBS));
		commCards.add(new Card(9, Card.Suit.CLUBS));

		assertEquals(true, straightFlush.isHand(commCards));
	}

	@Test
	public void isHandReturnsFalseWhenHandIsNotStraight() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.HEARTS));
		commCards.add(new Card(6, Card.Suit.HEARTS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(14, Card.Suit.CLUBS));

		assertEquals(true, !straightFlush.isHand(commCards));
	}

	@Test
	public void compareShouldReturnNullWhenComparingSameHand() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(3, Card.Suit.HEARTS));
			commCards.add(new Card(4, Card.Suit.HEARTS));
			commCards.add(new Card(5, Card.Suit.HEARTS));
			commCards.add(new Card(6, Card.Suit.HEARTS));
			commCards.add(new Card(10, Card.Suit.CLUBS));
			
			commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

			assertEquals(true, straightFlush.compare(commCards, commCards) == 0);
		}
	}

	@Test
	public void compareShouldReturnPositiveWhenComparingHigherHandWithLower() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.HEARTS));
		commCards.add(new Card(5, Card.Suit.HEARTS));
		commCards.add(new Card(6, Card.Suit.HEARTS));
//		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		
		Card randomCard = new Card(5, Card.Suit.DIAMONDS);
//		Card randomCard = new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS);
		commCards.add(randomCard);
		System.out.println(randomCard);

		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(2, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(3, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(4, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(5, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(14, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(10, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(12, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(8, Card.Suit.CLUBS));
		
		int compare = straightFlush.compare(commCards, commCardsCompare);
		
		System.out.println(compare);
		
		assertEquals(true,
				compare == 1);
		}
	}

	@Test
	public void compareShouldReturnNegativeWhenComparingLowerHandWithHigher() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.HEARTS));
		commCards.add(new Card(5, Card.Suit.HEARTS));
		commCards.add(new Card(6, Card.Suit.HEARTS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		
		commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(4, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(5, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(6, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(7, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(8, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(10, Card.Suit.CLUBS));
		
		commCardsCompare.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		assertEquals(true,
				straightFlush.compare(commCards, commCardsCompare) == -1);
		}
	}
}