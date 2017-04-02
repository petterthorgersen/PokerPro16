package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Random;

import org.gruppe2.game.Card;
import org.junit.Test;


/**
 * Created by Åsmund on 12/04/2016.
 */
public class FullHouseTest{
	FullHouse fullHouse = new FullHouse();
	public final int N = 10000;
	
    @Test
    public void canGetFullHouseReturnsTrueWhenItShould(){
        ArrayList<Card> commCards = new ArrayList<Card>();

        commCards.add(new Card(4, Card.Suit.HEARTS));
        commCards.add(new Card(3, Card.Suit.DIAMONDS));
        commCards.add(new Card(7, Card.Suit.SPADES));
        commCards.add(new Card(8, Card.Suit.CLUBS));

        assertEquals(true, fullHouse.canGet(commCards));
    }
    
    @Test
    public void canGetFullHouseReturnsFalseWhenItShould(){
        ArrayList<Card> commCards = new ArrayList<Card>();

        commCards.add(new Card(4, Card.Suit.HEARTS));
        commCards.add(new Card(3, Card.Suit.DIAMONDS));
        commCards.add(new Card(7, Card.Suit.SPADES));
        commCards.add(new Card(8, Card.Suit.CLUBS));
        commCards.add(new Card(10, Card.Suit.CLUBS));

        assertEquals(true, !fullHouse.canGet(commCards));
    }
    
    @Test
	public void isHandReturnsTrueWhenHandIsFullHouse() {
		Random random = new Random();
		
		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(2, Card.Suit.SPADES));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(3, Card.Suit.CLUBS));
			commCards.add(new Card(3, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, fullHouse.isHand(commCards));
		}
	}
    
    @Test
	public void isHandReturnsFalseWhenHandIsFullHouse() {
		Random random = new Random();
		
		for (int i = 0; i < N; i++) {
			ArrayList<Card> commCards = new ArrayList<Card>();
			commCards.add(new Card(2, Card.Suit.HEARTS));
			commCards.add(new Card(4, Card.Suit.SPADES));
			commCards.add(new Card(6, Card.Suit.CLUBS));
			commCards.add(new Card(8, Card.Suit.CLUBS));
			commCards.add(new Card(10, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, !fullHouse.isHand(commCards));
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
			commCards.add(new Card(3, Card.Suit.CLUBS));
			commCards.add(new Card(3, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));

			assertEquals(true, fullHouse.compare(commCards, commCards) == 0);
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
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			
			ArrayList<Card> commCardsCompare = new ArrayList<Card>();
			commCardsCompare.add(new Card(2, Card.Suit.HEARTS));
			commCardsCompare.add(new Card(2, Card.Suit.SPADES));
			commCardsCompare.add(new Card(2, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(3, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(3, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(11) + 4, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(11) + 4, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(random.nextInt(11) + 4, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(random.nextInt(11) + 4, Card.Suit.CLUBS));

			assertEquals(true, fullHouse.compare(commCards, commCardsCompare) == 1);
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
			commCards.add(new Card(2, Card.Suit.CLUBS));
			commCards.add(new Card(2, Card.Suit.CLUBS));
			
			ArrayList<Card> commCardsCompare = new ArrayList<Card>();
			commCardsCompare.add(new Card(4, Card.Suit.HEARTS));
			commCardsCompare.add(new Card(4, Card.Suit.SPADES));
			commCardsCompare.add(new Card(4, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(3, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(3, Card.Suit.CLUBS));

			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCards.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			commCardsCompare.add(new Card(random.nextInt(12) + 3, Card.Suit.CLUBS));
			
			assertEquals(true, fullHouse.compare(commCards, commCardsCompare) == -1);
		}
	}
}