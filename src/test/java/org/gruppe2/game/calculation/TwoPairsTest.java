package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by �smund on 12/04/2016.
 */
public class TwoPairsTest{
	public final TwoPairs twoPairs = new TwoPairs();
	public final int N = 10000;

	@Test
	public void canGetTwoPairReturnsTrueWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(2, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(14, Card.Suit.CLUBS));

		assertEquals(true, twoPairs.canGet(commCards));
	}

	@Test
	public void canGetTwoPairReturnsFalseWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));

		assertEquals(true, !twoPairs.canGet(commCards));
	}

	@Test
	public void isHandReturnsTrueWhenHandIsTwoPairs() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));

		assertEquals(true, twoPairs.isHand(commCards));
	}

	@Test
	public void isHandReturnsFalseWhenHandIsNotTwoPairs() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(2, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(14, Card.Suit.CLUBS));

		assertEquals(true, !twoPairs.isHand(commCards));
	}

	@Test
	public void compareShouldReturnNullWhenComparingSameHand() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(2, Card.Suit.SPADES));
			commCards.add(new Card(4, Card.Suit.CLUBS));
			commCards.add(new Card(6, Card.Suit.CLUBS));
			commCards.add(new Card(10, Card.Suit.CLUBS));
			commCards.add(new Card(10, Card.Suit.CLUBS));
			
			commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

			assertEquals(true, twoPairs.compare(commCards, commCards) == 0);
		}
	}

	@Test
	public void compareShouldReturnPositiveWhenComparingHigherHandWithLower() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(12, Card.Suit.CLUBS));
		commCards.add(new Card(13, Card.Suit.CLUBS));
		
		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(2, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(2, Card.Suit.SPADES));
		commCardsCompare.add(new Card(6, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(6, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(8, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(10, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(12, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(14, Card.Suit.CLUBS));
		
		assertEquals(true,
				twoPairs.compare(commCards, commCardsCompare) == 1);
	}

	@Test
	public void compareShouldReturnNegativeWhenComparingLowerHandWithHigher() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(6, Card.Suit.CLUBS));
		commCards.add(new Card(8, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.CLUBS));
		commCards.add(new Card(11, Card.Suit.CLUBS));
		
		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(5, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(5, Card.Suit.SPADES));
		commCardsCompare.add(new Card(8, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(6, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(8, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(10, Card.Suit.CLUBS));
		commCardsCompare.add(new Card(11, Card.Suit.CLUBS));
		
		assertEquals(true,
				twoPairs.compare(commCards, commCardsCompare) == -1);
	}

}