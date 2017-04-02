package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Card.Suit;
import org.gruppe2.game.Hand;

class StraightFlush implements HandCalculation{

    @Override
    public boolean isHand(List<Card> cards) {
    	if(cards == null || getBestHandCards(cards).isEmpty())
    		return false;
    	
    	return true;
    }

    @Override
    public List<Card> getBestCards(List<Card> cards) {
    	return getBestHandCards(cards);
    }

    @Override
    public boolean canGet(List<Card> cards) {
        if (cards == null || cards.size() == 2)
            return true;

        return Straight.canGetStraight(cards,true);
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.STRAIGHTFLUSH;
    }

    /**
     * Compares two StraightFlushes.
	 * If neither of the compared lists actually are StraightFlush it will return 0.
	 * This implies that a FullHouse compared to a Pair using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	List<Card> o1StraightFlush = getBestCards(o1), o2StraightFlush = getBestCards(o2);
    	
    	if(!o1StraightFlush.isEmpty() && o2StraightFlush.isEmpty())
    		return 1;
    	else if(o1StraightFlush.isEmpty() && !o2StraightFlush.isEmpty())
    		return -1;
    	else if(o1StraightFlush.isEmpty() && o2StraightFlush.isEmpty())
    		return 0;
    	
    	return Integer.compare(o1StraightFlush.get(0).getFaceValue(), o2StraightFlush.get(0).getFaceValue());
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		List<Card> cardsCopy = Generic.copyListOfCards(cards);
		
		List<Card> listOfCardsInStraightFlush = new ArrayList<>();
		ArrayList<Card> listOfCardsWithHighestSuit = new ArrayList<>();
		Straight straight = new Straight();
		int diamonds = 0, hearts = 0, spades = 0, clubs = 0;

		// Count all suits:
		for (Card c : cardsCopy) {
			switch (c.getSuit()) {
			case DIAMONDS:
				diamonds++;
				break;
			case HEARTS:
				hearts++;
				break;
			case SPADES:
				spades++;
				break;
			case CLUBS:
				clubs++;
				break;
			}
		}

		// Check if any of the suits will result in a flush:
		if (diamonds >= 5)
			listOfCardsWithHighestSuit = cardsOfOneSuit(cardsCopy, Suit.DIAMONDS);
		else if (hearts >= 5)
			listOfCardsWithHighestSuit = cardsOfOneSuit(cardsCopy, Suit.HEARTS);
		else if (spades >= 5)
			listOfCardsWithHighestSuit = cardsOfOneSuit(cardsCopy, Suit.SPADES);
		else if (clubs >= 5)
			listOfCardsWithHighestSuit = cardsOfOneSuit(cardsCopy, Suit.CLUBS);

		listOfCardsInStraightFlush = straight.getBestHandCards(listOfCardsWithHighestSuit);

		return listOfCardsInStraightFlush;
	}
	
	private ArrayList<Card> cardsOfOneSuit(List<Card> cards, Suit suit) {
		ArrayList<Card> flushWithSuit = new ArrayList<>();
		Collections.sort(cards);

		// Reversed loop, starts with highest value card.
		for (int i = cards.size() - 1; i >= 0; i--) {
			if (flushWithSuit.size() == 5)
				break;

			if (cards.get(i).getSuit() == suit)
				flushWithSuit.add(cards.get(i));
		}

		// Ignore this, it's never supposed to run, unless previous code has
		// bugs!
		if (flushWithSuit.size() < 5)
			flushWithSuit.clear();

		return flushWithSuit;
	}
}
