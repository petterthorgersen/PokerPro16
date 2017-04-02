package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;

public class HighCard implements HandCalculation {

	@Override
	public boolean isHand(List<Card> cards) {
		if (cards == null || cards.isEmpty())
			return false;

		return true;
	}

	/**
	 * Returns a list of 5 highcards in sorted order where list.get(0) = lowest card.
	 */
	@Override
	public List<Card> getBestCards(List<Card> cards) {
		List<Card> cardsCopy = Generic.copyListOfCards(cards);
		
		ArrayList<Card> highestCard = new ArrayList<>();

		Collections.sort(cardsCopy);
		for (int i = 0; i < 5; i++) {
			if ((cardsCopy.size() - i) > 0)
				highestCard.add(cardsCopy.get(cardsCopy.size() - 1 - i));
		}

		return highestCard;
	}

	@Override
	public boolean canGet(List<Card> cards) {
		return true;
	}

	@Override
	public double probability(List<Card> cards) {
		return 1.0;
	}

	@Override
	public Hand getType() {
		return Hand.HIGHCARD;
	}

	/**
	 * Compares two hands with HighCards.
	 * @return int (1, 0, -1).
	 */
	@Override
	public int compare(List<Card> o1, List<Card> o2) {
		List<Card> o1Sorted = getBestCards(o1);
		List<Card> o2Sorted = getBestCards(o2);
		
		for (int i = 0; i < o1Sorted.size() && i < o2Sorted.size(); i++) {
			int comp = o1Sorted.get(i).compareTo(o2Sorted.get(i));
			if (comp != 0)
				return comp;
		}

		return 0;
	}

	@Override
	public List<Card> getBestHandCards(List<Card> cards) {
		List<Card> getBestCards = getBestCards(cards);
		ArrayList<Card> getBestHandCards = new ArrayList<>();
		getBestHandCards.add(getBestCards.get(0));
		
		return getBestHandCards;
	}

}
