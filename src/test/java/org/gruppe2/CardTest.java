package org.gruppe2;

import static org.junit.Assert.assertEquals;

import org.gruppe2.game.Card;
import org.junit.Test;


public class CardTest {

    @Test
    public void cardSuitIsCorrectTest() {
        Card card = new Card(3, Card.Suit.HEARTS);
        assertEquals("New card is not of correct suit", Card.Suit.HEARTS, card.getSuit());
    }

    @Test
    public void cardFaceValueIsCorrectTest() {
        Card card = new Card(3, Card.Suit.HEARTS);
        assertEquals("New card does not have the correct faceValue", 3, card.getFaceValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalFaceValueShouldThrowExceptionTest() {
        new Card(17, Card.Suit.HEARTS);
    }
}
