package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

/**
 * Created by Åsmund on 26/04/2016.
 */
public class Generic {
    public static ArrayList<Integer> recurringFaceValues(List<Card> cards) {
        ArrayList<Integer> recurringFaceValues = new ArrayList<Integer>();

        HashMap<Integer, Integer> hashMapCards = recurringFaceValuesMap(cards);

        for(int i= 2; i < Card.ACE +1; i++){
            if(hashMapCards.containsKey(i)){
                if(hashMapCards.get(i) > 1)
                    for(int j = 0; j < hashMapCards.get(i); j++){
                        recurringFaceValues.add(i);
                    }
            }
        }
        return recurringFaceValues;
    }

    /**
     * Method to get a hashmap with the amount of the different facevalues.
     * @param cards
     * @return hashmap with different facevalues and their amount in the given list
     */
    public static HashMap<Integer, Integer> recurringFaceValuesMap(List<Card> cards) {
        HashMap<Integer, Integer> hashMapCards = new HashMap<Integer, Integer>();

        for (Card card: cards) {
            int faceValue = card.getFaceValue();
            if (hashMapCards.containsKey(faceValue))
                hashMapCards.put(faceValue, hashMapCards.get(faceValue) + 1);

            else hashMapCards.put(faceValue, 1);

        }
        return hashMapCards;
    }

    public static int amountOfSameFace(List<Card> cards){
        HashMap<Integer, Integer> amountCards = new HashMap<Integer, Integer>();
        int amountOfSameKind = 1;

        for(Card card: cards){
            int faceValue = card.getFaceValue();
            if(amountCards.containsKey(faceValue)){
                int amountOfCard = amountCards.get(faceValue) +1;
                amountCards.put(faceValue, amountOfCard);
                if(amountOfSameKind < amountOfCard)
                    amountOfSameKind = amountOfCard;
            }

            else
                amountCards.put(faceValue, 1);
        }
        return amountOfSameKind;
    }

    /**
     * A list over all types of hands to check probability and/or possibility.
     * List is sorted by hand rank
     * @return
     */
    public static ArrayList<HandCalculation> getAllHandTypes(){
        ArrayList<HandCalculation> hands = new ArrayList<>();
        hands.add(new RoyalFlush());
        hands.add(new StraightFlush());
        hands.add(new FourOfAKind());
        hands.add(new FullHouse());
        hands.add(new Flush());
        hands.add(new Straight());
        hands.add(new ThreeOfAKind());
        hands.add(new TwoPairs());
        hands.add(new Pair());
        hands.add(new HighCard());

        return hands;
    }

    public static HashMap<Card.Suit, Integer> numberOfEachSuit(List<Card> cards){
        HashMap<Card.Suit, Integer> numTypes = new HashMap<>();
        numTypes.put(Card.Suit.CLUBS, 0);
        numTypes.put(Card.Suit.DIAMONDS, 0);
        numTypes.put(Card.Suit.SPADES, 0);
        numTypes.put(Card.Suit.HEARTS, 0);

        for(Card c : cards)
            numTypes.put(c.getSuit(), numTypes.get(c.getSuit()) + 1);

        return numTypes;
    }

    public static Hand getBestHandForPlayer(List<Card> cards) {
        for (HandCalculation hand : getAllHandTypes())
            if (hand.canGet(cards))
                return hand.getType();
       return Hand.HIGHCARD;
    }

    public static HashMap<Hand, Boolean> getAllPossibleHandsForPlayer (List<Card> cards){
        HashMap<Hand, Boolean> types = new HashMap<>();

        return types;
    }
    
    public static int calculateFacevalueOfAllCards(List<Card> list) {
    	int value = 0;
    	for(Card c : list)
    		value += c.getFaceValue();
    	
    	return value;
    }
    
    public static List<Card> copyListOfCards(List<Card> cards) {
    	ArrayList<Card> copy = new ArrayList<>();
    	
    	for(Card c : cards)
    		copy.add(c);
    	
    	return copy;
    }

    public static double choose(int n, int k) {
        if (k < 0 || k > n) return 0;
        if (k > n/2) {
            // choose(n,k) == choose(n,n-k),
            // so this could save a little effort
            k = n - k;
        }

        double denominator = 1.0, numerator = 1.0;
        for (int i = 1; i <= k; i++) {
            denominator *= i;
            numerator *= (n + 1 - i);
        }
        return numerator / denominator;
    }
}
