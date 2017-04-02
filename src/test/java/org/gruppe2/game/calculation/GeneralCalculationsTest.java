package org.gruppe2.game.calculation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.gruppe2.game.Card;
import org.junit.Test;

/**
 * Created by �smund on 11/04/2016.
 */
public class GeneralCalculationsTest{

    @Test
    public void amountOfSameFaceReturnsProperValues(){
        ArrayList<Card> commCards = new ArrayList<Card>();
        commCards.add(new Card(4, Card.Suit.HEARTS));
        commCards.add(new Card(7, Card.Suit.SPADES));
        commCards.add(new Card(8, Card.Suit.CLUBS));
        commCards.add(new Card(4, Card.Suit.CLUBS));
        commCards.add(new Card(4, Card.Suit.CLUBS));

        assertEquals(3, Generic.amountOfSameFace(commCards));
    }

    @Test
    public void recurrigFaceValuesReturnsProperList(){
        ArrayList<Card> commCards = new ArrayList<Card>();
        commCards.add(new Card(2, Card.Suit.HEARTS));
        commCards.add(new Card(4, Card.Suit.SPADES));
        commCards.add(new Card(8, Card.Suit.CLUBS));
        commCards.add(new Card(4, Card.Suit.CLUBS));
        commCards.add(new Card(5, Card.Suit.SPADES));

        ArrayList<Integer> recurringValues = Generic.recurringFaceValues(commCards);

        assertEquals(4, recurringValues.get(0).intValue());
        assertEquals(2, recurringValues.size());

        commCards.add(new Card(5, Card.Suit.HEARTS));

        recurringValues = Generic.recurringFaceValues(commCards);

        assertEquals(5, recurringValues.get(3).intValue());
        assertEquals(4, recurringValues.size());
    }
}