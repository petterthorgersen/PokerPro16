package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

class Straight implements HandCalculation{

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

        return canGetStraight(cards, false);
    }

    static boolean canGetStraight(List<Card> cards, boolean sameSuit){
        for (int i = 0; i < cards.size(); i++){
            int length = checkWithOtherCards(cards, i,cards.get(i).getFaceValue(), sameSuit);

            if (length >= cards.size()-2)
                return true;
            else if (cards.get(i).getFaceValue() == 14){
                length = checkWithOtherCards(cards, i, 1, sameSuit);
                if (length >= cards.size()-2)
                    return true;
            }
        }

        return false;
    }

    private static int checkWithOtherCards(List<Card> allCards, int i, int faceValue, boolean sameSuit){
        ArrayList<Integer> straight = new ArrayList<>();
        straight.add(faceValue);
        int high = faceValue + 4;
        int low = faceValue - 4;

        for (int j = 0; j < allCards.size(); j++){
            int v2 = allCards.get(j).getFaceValue();

            if (faceValue == v2 || straight.contains(v2))
                continue;
            if (sameSuit && allCards.get(i).getSuit() != allCards.get(j).getSuit())
                continue;

            if (v2 < faceValue && v2 >= low){
                straight.add(v2);
                high -= (faceValue - v2);
            }
            if (v2 > faceValue && v2 <= high) {
                straight.add(v2);
                low += (v2 - faceValue);
            }

            if (v2 == 14){
                v2 = 1;

                if (v2 < faceValue && v2 >= low){
                    straight.add(v2);
                    high -= (faceValue - v2);
                }
                if (v2 > faceValue && v2 <= high) {
                    straight.add(v2);
                    low += (v2 - faceValue);
                }
            }
        }

        return straight.size();
    }

    @Override
    public double probability(List<Card> cards) {
        return 0;
    }

    @Override
    public Hand getType() {
        return Hand.STRAIGHT;
    }

    /**
     * Compares two Straights.
	 * If neither of the compared lists actually are Straight it will return 0.
	 * This implies that a FullHouse compared to a Pair using this compare methode
	 * will result in 0.
	 * @return int (1, 0, -1).
	 */
    @Override
    public int compare(List<Card> o1, List<Card> o2) {
    	List<Card> o1Straight = getBestCards(o1), o2Straight = getBestCards(o2);
    	
    	if(!o1Straight.isEmpty() && o2Straight.isEmpty())
    		return 1;
    	else if(!o2Straight.isEmpty() && o1Straight.isEmpty())
    		return -1;
    	else if(o1Straight.isEmpty() && o2Straight.isEmpty())
    		return 0;
    	
    	HighCard highCard = new HighCard();
    	boolean o1ace=false;
    	boolean o1king=false;
    	Card ace1 = null;
    	for (Card c : o1Straight){
    		if (c.getFaceValue()==14){
    			ace1=c;
    			o1ace=true;
    		}
    		if (c.getFaceValue()==13)
    			o1king=true;
    	}
    	Card ace2 = null;
    	boolean o2ace=false;
    	boolean o2king=false;
    	for (Card c : o2Straight){
    		if (c.getFaceValue()==14){
    			ace2=c;
    			o2ace=true;
    		}
    		if (c.getFaceValue()==13)
    			o2king=true;
    	}
    	if (o1ace && !o1king && ace1!=null)
    		o1Straight.remove(ace1);
    	if (o2ace && !o2king && ace2!=null)
    		o2Straight.remove(ace2);
    	
    	
    	return highCard.compare(o1Straight, o2Straight);
    }

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		List<Card> cardsCopy = Generic.copyListOfCards(cards);
		
		ArrayList<Card> listOfCardsInStraight = new ArrayList<>();
		if (cardsCopy.size() >= 1) {
			Collections.sort(cardsCopy);
			int cardsInARow = 1; // Starter card always counts.

			// Face value of highest card
			int lastCardValue = cardsCopy.get(cardsCopy.size() - 1).getFaceValue();
			listOfCardsInStraight.add(cardsCopy.get(cardsCopy.size() - 1));

			// Simple reversed loop, skipping the highest card.
			for (int i = (cardsCopy.size() - 2); (i + 1) > 0; i--) {
				if (listOfCardsInStraight.size() == 5)
					break;

				// If the next card is 1 less than the previous, then it counts!
				if ((cardsCopy.get(i).getFaceValue() + 1) == lastCardValue) {
					cardsInARow++;
					listOfCardsInStraight.add(cardsCopy.get(i));
				} else if ((cardsCopy.get(i).getFaceValue() + 1) < lastCardValue) {
					listOfCardsInStraight.clear();
					cardsInARow = 1;
					listOfCardsInStraight.add(cardsCopy.get(i));
				}

				lastCardValue = cardsCopy.get(i).getFaceValue();
			}

			// Special case for Ace as it can also count as a "1"
			if (lastCardValue == 2 && cardsInARow == 4
					&& cardsCopy.get(cardsCopy.size() - 1).getFaceValue() == 14)
				listOfCardsInStraight.add(cardsCopy.get(cardsCopy.size() - 1));

			if (listOfCardsInStraight.size() != 5)
				listOfCardsInStraight.clear();

		}

		return listOfCardsInStraight;

	}
}
