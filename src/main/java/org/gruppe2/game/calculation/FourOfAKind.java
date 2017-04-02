package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class FourOfAKind implements HandCalculation {

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
        List<Card> pureFourOfAKindCards = getBestHandCards(cardsCopy);
        
        // Returns an empty list if this isn't actually a FourOfAKind.
        if(pureFourOfAKindCards.isEmpty())
        	return getBestCards;
        
        cardsCopy.removeAll(pureFourOfAKindCards);
        
        List<Card> highCards = highCard.getBestCards(cardsCopy);
        
        getBestCards.addAll(pureFourOfAKindCards);
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

        if(cards.size() < 5)
            return true;
        if(cards.size() == 5 &&  amountOfSameFace >=2)
            return true;
        if(cards.size() == 6 && amountOfSameFace >=3)
            return true;
        if (cards.size() == 7 && amountOfSameFace == 4)
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
        HashMap<Integer, Integer> numberOfNumbers = Generic.recurringFaceValuesMap(cards);
        for (Integer i : numberOfNumbers.keySet()){
        	number = numberOfNumbers.get(i);
        	if (drawsLeft+number>=4){
        		probability+=HypergeometricCalculator.hypergeometricDistribution(52-cards.size(), 4-number, drawsLeft, 4-number);
        	}
        }
    	return probability;
    }

    @Override
    public Hand getType() {
        return Hand.FOUROFAKIND;
    }

    /**
     * Compares two FourOfAKinds.
	 * If neither of the compared lists actually are FourOfAKind it will return 0.
	 * This implies that a RoyalFlush compared to a Pair using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	List<Card> o1CardsWithoutHighCard = getBestHandCards(o1);
    	List<Card> o2CardsWithoutHighCard = getBestHandCards(o2);
    	
    	int compareFourOfAKindValueFirst = Integer.compare(Generic.calculateFacevalueOfAllCards(o1CardsWithoutHighCard), Generic.calculateFacevalueOfAllCards(o2CardsWithoutHighCard));
    	
    	if(compareFourOfAKindValueFirst != 0)
    		return compareFourOfAKindValueFirst;
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
		
		ArrayList<Card> listOfCardsInFourOfAKind = new ArrayList<>();

		HashMap<Integer, Integer> recurringFaceValues = Generic.recurringFaceValuesMap(cardsCopy);
		int highestCardValue = -1;
		
		for(int i= Card.ACE; i >= 2; i--){
            if(recurringFaceValues.containsKey(i)){
                if(recurringFaceValues.get(i) > 3) {
                	highestCardValue = i;
                	break;
                }
                    
            }
        }
		
		if(highestCardValue > 1) {
			for(Card c : cardsCopy) {
				if(c.getFaceValue() == highestCardValue) {
					listOfCardsInFourOfAKind.add(c);
					if(listOfCardsInFourOfAKind.size() >= 4)
						break;
				}
			}
		}
		
		// Returns an empty list if this isn't actually a FourOfAKind.
		if(listOfCardsInFourOfAKind.size() != 4)
			listOfCardsInFourOfAKind.clear();

		return listOfCardsInFourOfAKind;
	}
}
