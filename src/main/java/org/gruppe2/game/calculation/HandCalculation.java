package org.gruppe2.game.calculation;

import java.util.Comparator;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

interface HandCalculation extends Comparator<List<Card>> {
    /**
     * Check if the given list of cards is a certain hand (ie. if it's a Pair)
     * @param cards a collection of 7 cards
     * @return true if it's a valid hand, false otherwise
     */
    boolean isHand(List<Card> cards);

    /**
     * Get the best 5 cards out of the 7 available (including Highcards).
     * @param cards
     * @return
     */
    List<Card> getBestCards(List<Card> cards);
    
    /**
     * Get the best 5 cards out of the 7 available (excluding Highcards)
     * @param cards a list with 7 cards
     * @return A list with 5 elements or null if it's impossible to get the hand
     */
    List<Card> getBestHandCards(List<Card> cards);

    /**
     * Checks if its possible to get a certain hand when there are &lt; 7 cards
     * @param cards a collection of [5, 7) cards to evaluate
     * @return true if a hand is possible to get, false otherwise
     */
    boolean canGet(List<Card> cards);

    /**
     * Calculates the probability that player p gets a specific hand
     * @return The probability as a double in the range 0..1
     */
    double probability(List<Card> cards);

    /**
     * @return The hand type
     */
    Hand getType();

    /**
     * Compare two hands of the same type
     * @param cards
     * @param t1
     * @return returns 1 if cards is better than ti, returns -1 if ti is better than cards. 0 if they are equally good.
     */
    @Override
    int compare(List<Card> cards, List<Card> t1);
}
