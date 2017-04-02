package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by Mikal on 12.04.2016.
 */
public class RoyalFlushTest {

	public final RoyalFlush straightFlush = new RoyalFlush();
	public final int N = 10000;

	@Test
	public void canGetRoyalFlushReturnsTrueWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.CLUBS));
		commCards.add(new Card(3, Card.Suit.CLUBS));
		commCards.add(new Card(10, Card.Suit.HEARTS));
		commCards.add(new Card(12, Card.Suit.HEARTS));
		commCards.add(new Card(13, Card.Suit.HEARTS));
		commCards.add(new Card(14, Card.Suit.HEARTS));

		assertEquals(true, straightFlush.canGet(commCards));
	}

	@Test
	public void canGetRoyalFlushReturnsFalseWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(14, Card.Suit.HEARTS));
		commCards.add(new Card(5, Card.Suit.HEARTS));
		commCards.add(new Card(10, Card.Suit.HEARTS));
		commCards.add(new Card(12, Card.Suit.HEARTS));

		assertEquals(true, !straightFlush.canGet(commCards));
	}

	@Test
	public void isHandReturnsTrueWhenHandIsRoyalFlush() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(13, Card.Suit.HEARTS));
		commCards.add(new Card(14, Card.Suit.HEARTS));
		commCards.add(new Card(10, Card.Suit.HEARTS));
		commCards.add(new Card(11, Card.Suit.HEARTS));
		commCards.add(new Card(12, Card.Suit.HEARTS));

		assertEquals(true, straightFlush.isHand(commCards));
	}

	@Test
	public void isHandReturnsFalseWhenHandIsNotRoyalFlush() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(3, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.HEARTS));
		commCards.add(new Card(11, Card.Suit.CLUBS));
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
			commCards.add(new Card(13, Card.Suit.HEARTS));
			commCards.add(new Card(12, Card.Suit.HEARTS));
			commCards.add(new Card(14, Card.Suit.HEARTS));
			commCards.add(new Card(11, Card.Suit.HEARTS));
			commCards.add(new Card(10, Card.Suit.HEARTS));
			
			commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

			assertEquals(true, straightFlush.compare(commCards, commCards) == 0);
		}
	}

	@Test
	public void compareShouldReturnNullWhenComparingHigherHandWithLower() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(8, Card.Suit.DIAMONDS));
		commCards.add(new Card(9, Card.Suit.DIAMONDS));
		commCards.add(new Card(10, Card.Suit.HEARTS));
		commCards.add(new Card(11, Card.Suit.HEARTS));
		commCards.add(new Card(12, Card.Suit.HEARTS));
		commCards.add(new Card(13, Card.Suit.HEARTS));
		commCards.add(new Card(14, Card.Suit.HEARTS));
		
		commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(2, Card.Suit.DIAMONDS));
		commCardsCompare.add(new Card(3, Card.Suit.DIAMONDS));
		commCardsCompare.add(new Card(10, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(11, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(12, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(13, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(14, Card.Suit.HEARTS));
		
		commCardsCompare.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		assertEquals(true,
				straightFlush.compare(commCards, commCardsCompare) == 0);
		}
	}

	@Test
	public void compareShouldReturnNullWhenComparingLowerHandWithHigher() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.DIAMONDS));
		commCards.add(new Card(10, Card.Suit.HEARTS));
		commCards.add(new Card(11, Card.Suit.HEARTS));
		commCards.add(new Card(12, Card.Suit.HEARTS));
		commCards.add(new Card(13, Card.Suit.HEARTS));
		commCards.add(new Card(14, Card.Suit.HEARTS));
		
		commCards.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		ArrayList<Card> commCardsCompare = new ArrayList<Card>();
		commCardsCompare.add(new Card(14, Card.Suit.DIAMONDS));
		commCardsCompare.add(new Card(10, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(11, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(12, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(13, Card.Suit.HEARTS));
		commCardsCompare.add(new Card(14, Card.Suit.HEARTS));
		
		commCardsCompare.add(new Card(random.nextInt(12)+2,Card.Suit.DIAMONDS));

		assertEquals(true,
				straightFlush.compare(commCards, commCardsCompare) == 0);
		}
	}
}