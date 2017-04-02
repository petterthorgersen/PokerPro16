package org.gruppe2.game.calculation;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by Mikal on 11.04.2016.
 */
public class FlushTest {
	
	@Test
	public void probabilityIsCorrectTest(){
		List<Card> cards = new ArrayList<Card>();
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.CLUBS));
        cards.add(new Card(14, Card.Suit.SPADES));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        Flush flush = new Flush();
        assertTrue(flush.probability(cards)==0.041628122109158);
	}
	
	@Test
	public void probabilityIsOneWhenAlreadyHavingFlushTest(){
		List<Card> cards = new ArrayList<Card>();
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.HEARTS));
        cards.add(new Card(14, Card.Suit.HEARTS));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        Flush flush = new Flush();
		assertTrue(flush.probability(cards)==1);
	}
	
	@Test
	public void probabilityIsZeroWhenFlushIsNotPossibleTest(){
		List<Card> cards = new ArrayList<Card>();
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.CLUBS));
        cards.add(new Card(14, Card.Suit.DIAMONDS));
        cards.add(new Card(8, Card.Suit.SPADES));
        cards.add(new Card(9, Card.Suit.HEARTS));
        Flush flush = new Flush();
		assertTrue(flush.probability(cards)==0);
	}

    @Test
    public void findsCorrectNumberOfSuitsTest() throws Exception {
        List<Card> cards = new ArrayList<Card>();
        cards.add(new Card(10, Card.Suit.HEARTS));
        cards.add(new Card(3, Card.Suit.CLUBS));
        cards.add(new Card(14, Card.Suit.SPADES));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));

        HashMap<Card.Suit, Integer> nt = Generic.numberOfEachSuit(cards);
        assertEquals(3, (int) nt.get(Card.Suit.HEARTS));
        assertEquals(1, (int) nt.get(Card.Suit.CLUBS));
        assertEquals(1, (int) nt.get(Card.Suit.SPADES));
        assertEquals(0, (int) nt.get(Card.Suit.DIAMONDS));
    }

    @Test
    public void canGetFlushTest() throws Exception {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card(10, Card.Suit.DIAMONDS));
        cards.add(new Card(4, Card.Suit.DIAMONDS));
        cards.add(new Card(11, Card.Suit.CLUBS));

        Flush flush = new Flush();
        assertTrue(flush.canGet(cards));
    }

    @Test
    public void cantGetFlushTest() throws Exception {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(10, Card.Suit.DIAMONDS));
        cards.add(new Card(4, Card.Suit.CLUBS));
        cards.add(new Card(11, Card.Suit.CLUBS));
        cards.add(new Card(8, Card.Suit.HEARTS));
        cards.add(new Card(9, Card.Suit.HEARTS));
        
        Flush flush = new Flush();
        assertFalse(flush.canGet(cards));
    }
}