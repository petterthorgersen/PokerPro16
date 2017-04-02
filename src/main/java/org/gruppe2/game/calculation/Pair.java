package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class Pair implements HandCalculation {

    @Override
    public boolean isHand(List<Card> cards) {
    	if(cards == null || getBestHandCards(cards).isEmpty())
    		return false;
    	
    	return true;
    }

    @Override
    public List<Card> getBestCards(List<Card> cards) {
    	List<Card> cardsCopy = Generic.copyListOfCards(cards);
    	
        ArrayList<Card> getBestCards = new ArrayList<>();
        HighCard highCard = new HighCard();
        List<Card> purePairCards = getBestHandCards(cardsCopy);
        if(purePairCards.isEmpty())
        	return getBestCards;
        
        cardsCopy.removeAll(purePairCards);
        
        List<Card> highCards = highCard.getBestCards(cardsCopy);
        
        getBestCards.addAll(purePairCards);
        for(int i = highCards.size()-1; i >= 0; i--) {
        	getBestCards.add(highCards.get(i));
        	if(getBestCards.size() >= 5)
        		break;
        }
        
        return getBestCards;
    }

    @Override
    public boolean canGet(List<Card> cards) {
        if (cards == null || cards.size() == 2)
            return true;

        int amountOfSameFace = Generic.amountOfSameFace(cards);
        if(cards.size() == 7 &&  amountOfSameFace < 2)
            return false;
        return true;
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
        HashMap<Integer, Integer> numberOfNumbers = Generic.recurringFaceValuesMap(cards);
        for (Integer i : numberOfNumbers.keySet()){
        	number = numberOfNumbers.get(i);
        	if (drawsLeft+number>=2){
        		probability+=HypergeometricCalculator.hypergeometricDistribution(52-cards.size(), 13-number, drawsLeft, 2-number);
        	}
        }
    	return probability;
    }

    @Override
    public Hand getType() {
        return Hand.PAIR;
    }

    /**
     * Compares Pair one with Pair two.
	 * If neither of the compared lists actually are a Pair it will return 0.
	 * This implies that a RoyalFlush compared to a Straight using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	List<Card> o1Pair = getBestHandCards(o1), o2Pair = getBestHandCards(o2);
    	int comparePairOneWithPairTwo = Integer.compare(Generic.calculateFacevalueOfAllCards(o1Pair), Generic.calculateFacevalueOfAllCards(o2Pair));
    	
    	if(comparePairOneWithPairTwo != 0)
    		return comparePairOneWithPairTwo;
    	else{
    		HighCard highcard = new HighCard();
    		List<Card> o1HighCards = getBestCards(o1), o2HighCards = getBestCards(o2);
    		o1HighCards.removeAll(o1Pair);
    		o2HighCards.removeAll(o2Pair);
    		
    		return highcard.compare(o1HighCards, o2HighCards);
    	}
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		List<Card> cardsCopy = Generic.copyListOfCards(cards);
		
		ArrayList<Card> listOfCardsInPair = new ArrayList<>();

		HashMap<Integer, Integer> recurringFaceValues = Generic.recurringFaceValuesMap(cardsCopy);
		int highestCardValue = -1;
		
		for(int i= Card.ACE; i >= 2; i--){
            if(recurringFaceValues.containsKey(i)){
                if(recurringFaceValues.get(i) > 1) {
                	highestCardValue = i;
                	break;
                }
                    
            }
        }
		
		if(highestCardValue > 1) {
			for(Card c : cardsCopy) {
				if(c.getFaceValue() == highestCardValue) {
					listOfCardsInPair.add(c);
					if(listOfCardsInPair.size() >= 2)
						break;
				}
			}
		}

		return listOfCardsInPair;
	}
}
