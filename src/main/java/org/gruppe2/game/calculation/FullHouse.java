package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class FullHouse implements HandCalculation {

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

        int amountSameFace = Generic.amountOfSameFace(cards);
        ArrayList<Integer> recurringCards = Generic.recurringFaceValues(cards);
        if(cards.size() <5)
            return true;
        if(cards.size() == 5 && amountSameFace >=2)
            return true;
        if(cards.size() == 6 && recurringCards.size() >=3)
            return true;
        if(cards.size() == 7 && recurringCards.size() >=5)
            return true;

        return false;
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.FULLHOUSE;
    }

    /**
     * Compares two FullHouses.
	 * If neither of the compared lists actually are FullHouse it will return 0.
	 * This implies that a RoyalFlush compared to a Pair using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	boolean o1FullHouse = isHand(o1), o2FullHouse = isHand(o2);
    	if(o1FullHouse && !o2FullHouse)
    		return 1;
    	else if(o2FullHouse && !o1FullHouse)
    		return -1;
    	else if(!o1FullHouse && !o2FullHouse)
    		return 0;
    	
    	List<Card> o1Copy = Generic.copyListOfCards(o1);
    	List<Card> o2Copy = Generic.copyListOfCards(o2);
    	ThreeOfAKind threeOfAKind = new ThreeOfAKind();
    	
		List<Card> bestHouse_1 = threeOfAKind.getBestHandCards(o1Copy);
		List<Card> bestHouse_2 = threeOfAKind.getBestHandCards(o2Copy);
		
		int compareThreeCards = threeOfAKind.compare(bestHouse_1, bestHouse_2);
		if(compareThreeCards != 0)
			return compareThreeCards;
		else{
			Pair pair = new Pair();
			
			o1Copy.removeAll(bestHouse_1);
			List<Card> copy_hand_1 = Generic.copyListOfCards(o1Copy);
			
			o2Copy.removeAll(bestHouse_2);
			List<Card> copy_hand_2 = Generic.copyListOfCards(o2Copy);
			
			return pair.compare(copy_hand_1, copy_hand_2);
		}
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		List<Card> cardsCopy = Generic.copyListOfCards(cards);
		
		ArrayList<Card> listOfCardsInFullHouse = new ArrayList<>();
		ThreeOfAKind threeOfAKind = new ThreeOfAKind();
		Pair pair = new Pair();

		List<Card> highestThreeOfAKind = threeOfAKind.getBestHandCards(cardsCopy);
		ArrayList<Card> cardsExcludingThreeOfAKind = new ArrayList<>(); // Mostly
																		// a
																		// dummy
																		// list
		for (Card c : cardsCopy)
			if (!highestThreeOfAKind.contains(c))
				cardsExcludingThreeOfAKind.add(c);
		List<Card> highestOnePair = pair.getBestHandCards(cardsExcludingThreeOfAKind);

		// If we found a full house, add it, if not, then we return an empty
		// list
		if (!highestThreeOfAKind.isEmpty() && !highestOnePair.isEmpty()) {
			listOfCardsInFullHouse.addAll(highestThreeOfAKind);
			listOfCardsInFullHouse.addAll(highestOnePair);
		}

		return listOfCardsInFullHouse;
	}
}
