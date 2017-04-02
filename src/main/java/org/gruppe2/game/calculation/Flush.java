package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Card.Suit;
import org.gruppe2.game.Hand;

class Flush implements HandCalculation {

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

        HashMap<Card.Suit, Integer> numTypes = Generic.numberOfEachSuit(cards);

        for (Card.Suit suit : numTypes.keySet())
            if (numTypes.get(suit) >= cards.size() -2)
                return true;

        return false;
    }

    @Override
    public double probability(List<Card> cards) {
        if (isHand(cards))
        	return 1;
        if (!canGet(cards))
        	return 0;
        int drawsLeft = 7-cards.size();
        double probability = 0;
        int number;
        HashMap<Card.Suit, Integer> numberOfEachSuit = Generic.numberOfEachSuit(cards);
        for (Card.Suit suit : numberOfEachSuit.keySet()){
        	number=numberOfEachSuit.get(suit);
        	if (drawsLeft+number>=5){
        		probability+=HypergeometricCalculator.hypergeometricDistribution(52-cards.size(), 13-number, drawsLeft, 5-number);
        	}
        }
        return probability;
    }

    @Override
    public Hand getType() {
        return Hand.FLUSH;
    }

    /**
     * Assumes o1 and o2 are already sorted and only includes the 5 Flush cards!
     */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	HighCard highCard = new HighCard();
    	
    	return highCard.compare(o1, o2);
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		ArrayList<Card> listOfCardsInFlush = new ArrayList<>();
		int diamonds = 0, hearts = 0, spades = 0, clubs = 0;

		// Count all suits:
		for (Card c : cards) {
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
			listOfCardsInFlush = flushCardsFromSuit(cards, Suit.DIAMONDS);
		else if (hearts >= 5)
			listOfCardsInFlush = flushCardsFromSuit(cards, Suit.HEARTS);
		else if (spades >= 5)
			listOfCardsInFlush = flushCardsFromSuit(cards, Suit.SPADES);
		else if (clubs >= 5)
			listOfCardsInFlush = flushCardsFromSuit(cards, Suit.CLUBS);

		return listOfCardsInFlush;
	}
	
	private ArrayList<Card> flushCardsFromSuit(List<Card> cards, Suit suit) {
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
