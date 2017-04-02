package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by �smund on 12/04/2016.
 */
public class FourOfAKindTest {

	public final FourOfAKind fourOfAKind = new FourOfAKind();
	public final int N = 10000;

	@Test
	public void canGetFourOfAKindReturnsTrueWhenItShould() {
		ArrayList<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(4, Card.Suit.HEARTS));
		commCards.add(new Card(7, Card.Suit.SPADES));
		commCards.add(new Card(8, Card.Suit.CLUBS));

		FourOfAKind fourOfAKind = new FourOfAKind();

		assertEquals(true, fourOfAKind.canGet(commCards));
	}

	@Test
	public void canGetFourOfAKindReturnsFalseWhenItShould() {
		Random random = new Random();

		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(4, Card.Suit.HEARTS));
			commCards.add(new Card(7, Card.Suit.SPADES));
			commCards.add(new Card(8, Card.Suit.CLUBS));
			commCards.add(new Card(10, Card.Suit.CLUBS));
			commCards.add(new Card(11, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(13) + 2, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(13) + 2, Card.Suit.CLUBS));

			assertEquals(true, !fourOfAKind.canGet(commCards));
		}
	}
	
	@Test
	public void isHandReturnsTrueWhenHandIsFourOfAKind() {
		Random random = new Random();
		
		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(2, Card.Suit.SPADES));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(5, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, fourOfAKind.isHand(commCards));
		}
	}
	
	@Test
	public void isHandReturnsFalseWhenHandIsNotFourOfAKind() {
		Random random = new Random();
		
		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(4, Card.Suit.SPADES));
			commCards.add(new Card(6, Card.Suit.CLUBS));
			commCards.add(new Card(8, Card.Suit.CLUBS));
			commCards.add(new Card(10, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(13) + 2, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(13) + 2, Card.Suit.CLUBS));

			assertEquals(true, !fourOfAKind.isHand(commCards));
		}
	}
	
	@Test
	public void compareShouldReturnNullWhenComparingSameHand() {
		Random random = new Random();
		
		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(2, Card.Suit.SPADES));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(5, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, fourOfAKind.compare(commCards, commCards) == 0);
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
			commCards.add(new Card(3, Card.Suit.CLUBS));
			commCards.add(new Card(5, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			
			ArrayList<Card> commCardsCompare = new ArrayList<Card>();
			commCardsCompare.add(new Card(2, Card.Suit.HEARTS));
			commCardsCompare.add(new Card(2, Card.Suit.SPADES));
			commCardsCompare.add(new Card(2, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(2, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(5, Card.Suit.CLUBS));

			commCardsCompare.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, fourOfAKind.compare(commCards, commCardsCompare) == 1);
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
			commCards.add(new Card(3, Card.Suit.CLUBS));
			commCards.add(new Card(5, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			
			ArrayList<Card> commCardsCompare = new ArrayList<Card>();
			commCardsCompare.add(new Card(5, Card.Suit.HEARTS));
			commCardsCompare.add(new Card(5, Card.Suit.SPADES));
			commCardsCompare.add(new Card(5, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(5, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(5, Card.Suit.CLUBS));

			commCardsCompare.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, fourOfAKind.compare(commCards, commCardsCompare) == -1);
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
		FourOfAKind fk = new FourOfAKind();
		double prob = fk.probability(commCards);
		System.out.println(prob);
		assertTrue(prob==0.000925069380204);
	}
	@Test
	public void probabilityReturnsZeroWhenFourOfAKindIsUnaviable(){
		List<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(7, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(3, Card.Suit.CLUBS));
		commCards.add(new Card(5, Card.Suit.CLUBS));
		FourOfAKind fk = new FourOfAKind();
		assertTrue(fk.probability(commCards)==0);
	}
	@Test
	public void probabilityReturnsOneWhenAlreadyHavingFourOfAKind(){
		List<Card> commCards = new ArrayList<Card>();
		commCards.add(new Card(2, Card.Suit.HEARTS));
		commCards.add(new Card(4, Card.Suit.SPADES));
		commCards.add(new Card(4, Card.Suit.CLUBS));
		commCards.add(new Card(4, Card.Suit.DIAMONDS));
		commCards.add(new Card(4, Card.Suit.HEARTS));
		FourOfAKind fk = new FourOfAKind();
		assertTrue(fk.probability(commCards)==1);
	}
}