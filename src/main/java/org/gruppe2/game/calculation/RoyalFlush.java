package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class RoyalFlush implements HandCalculation {
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

        if (getHighestAmountOfRoyalCardsInSameSuit(cards) >= cards.size() -2)
            return true;

        return false;
    }

    private int getHighestAmountOfRoyalCardsInSameSuit(List<Card> cards){
        List<Card> allCards = royalCardFilter(cards);

        int highest = 0;

        HashMap<Card.Suit, Integer> numTypes = Generic.numberOfEachSuit(allCards);

        for (int i : numTypes.values())
            if (i > highest)
                highest = i;

        return highest;
    }

    private List<Card> royalCardFilter(List<Card> cards){
        List<Card> newList = new ArrayList<>();
        for (Card c : cards)
            if (cardIsRoyal(c))
                newList.add(c);
        return newList;
    }

    private boolean cardIsRoyal(Card c) {
        return c.getFaceValue() >= 10 && c.getFaceValue() <= 14;
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.ROYALFLUSH;
    }

    /**
     * Compares two RoyalFlushes.
	 * If neither of the compared lists actually are RoyalFlush it will return 0.
	 * This implies that a Straight compared to a Pair using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
        boolean o1Royal = isHand(o1), o2Royal = isHand(o2);
        if(o1Royal && !o2Royal)
        	return 1;
        else if(o2Royal && !o1Royal)
        	return -1;
        else
        	return 0;
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		StraightFlush straightFlush = new StraightFlush();
		HighCard highCard = new HighCard();
		List<Card> listOfCardsInRoyalFlush = straightFlush.getBestHandCards(cards);

		// Check if the straight flush has an Ace High
		if (!listOfCardsInRoyalFlush.isEmpty()
				&& highCard.getBestHandCards(listOfCardsInRoyalFlush).get(0).getFaceValue() != 14)
			listOfCardsInRoyalFlush.clear();

		return listOfCardsInRoyalFlush;
	}
}
