package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class ThreeOfAKind implements HandCalculation {

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
        List<Card> pureThreeOfAKindCards = getBestHandCards(cardsCopy);
        
        cardsCopy.removeAll(pureThreeOfAKindCards);
        
        List<Card> highCards = highCard.getBestCards(cardsCopy);
        
        getBestCards.addAll(pureThreeOfAKindCards);
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

        if(amountOfSameFace >= 3)
            return true;
        if (cards.size() <= 5)
            return true;
        if(cards.size() == 6){
            if(amountOfSameFace == 2)
                return true;
        }

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
        HashMap<Integer, Integer> numberOfNumbers = Generic.recurringFaceValuesMap(cards);
        for (Integer i : numberOfNumbers.keySet()){
        	number = numberOfNumbers.get(i);
        	if (drawsLeft+number>=3){
        		probability+=HypergeometricCalculator.hypergeometricDistribution(52-cards.size(), 4-number, drawsLeft, 3-number);
        	}
        }
    	return probability;
    }

    @Override
    public Hand getType() {
        return Hand.THREEOFAKIND;
    }

    /**
     * Compares two ThreeOfAKinds.
	 * If neither of the compared lists actually are ThreeOfAKind it will return 0.
	 * This implies that a RoyalFlush compared to a Pair using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	List<Card> o1CardsWithoutHighCard = getBestHandCards(o1);
    	List<Card> o2CardsWithoutHighCard = getBestHandCards(o2);
    	
    	int compareThreeOfAKindValueFirst = Integer.compare(Generic.calculateFacevalueOfAllCards(o1CardsWithoutHighCard), Generic.calculateFacevalueOfAllCards(o2CardsWithoutHighCard));
    	
    	if(compareThreeOfAKindValueFirst != 0)
    		return compareThreeOfAKindValueFirst;
    	else{
    		List<Card> o1CardsWithHighCard = getBestCards(o1);
    		List<Card> o2CardsWithHighCard = getBestCards(o2);
    		
    		o1CardsWithHighCard.removeAll(o1CardsWithoutHighCard);
    		o2CardsWithHighCard.removeAll(o2CardsWithoutHighCard);
    		
    		HighCard highcard = new HighCard();
    		return highcard.compare(o1CardsWithHighCard, o2CardsWithHighCard);
    	}
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		List<Card> cardsCopy = Generic.copyListOfCards(cards);
		
		ArrayList<Card> listOfCardsInThreeOfAKind = new ArrayList<>();

		HashMap<Integer, Integer> recurringFaceValues = Generic.recurringFaceValuesMap(cardsCopy);
		int highestCardValue = -1;
		
		for(int i= Card.ACE; i >= 2; i--){
            if(recurringFaceValues.containsKey(i)){
                if(recurringFaceValues.get(i) > 2) {
                	highestCardValue = i;
                	break;
                }
                    
            }
        }
		
		if(highestCardValue > 1) {
			for(Card c : cardsCopy) {
				if(c.getFaceValue() == highestCardValue) {
					listOfCardsInThreeOfAKind.add(c);
					if(listOfCardsInThreeOfAKind.size() >= 3)
						break;
				}
			}
		}

		return listOfCardsInThreeOfAKind;
	}
}
