package org.gruppe2.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cards {

	public static List<Card> asList(String cards) {
		List<Card> cardList = new ArrayList<>();

		String[] individualCards = cards.split("\\s+");

		for (int i = 0; i < individualCards.length; i++) {
			int value;
			Card.Suit suit;
			char charVal = individualCards[i].charAt(0);

			if (charVal >= '2' && charVal <= '9') {
				value = Character.getNumericValue(charVal);
			} else if (charVal == '1') {
				value = 10;
			} else if (charVal == 'J') {
				value = Card.JACK;
			} else if (charVal == 'Q') {
				value = Card.QUEEN;
			} else if (charVal == 'K') {
				value = Card.KING;
			} else if (charVal == 'A') {
				value = Card.ACE;
			} else {
				throw new RuntimeException("Invalid card value: " + charVal);
			}

			switch (individualCards[i].charAt(1)) {
			case 'D':
				suit = Card.Suit.DIAMONDS;
				break;
			case 'C':
				suit = Card.Suit.CLUBS;
				break;
			case 'H':
				suit = Card.Suit.HEARTS;
				break;
			case 'S':
				suit = Card.Suit.SPADES;
				break;
			default:
				throw new RuntimeException("Weird suit of card " + (i + 1)
						+ ". received input: " + individualCards[i].charAt(1)
						+ ". If your value is 10, use 1(10 Hearths = 1H)");
			}

			Card card = new Card(value, suit);
			cardList.add(card);
		}

		return cardList;
	}

	public static Map<Integer, Integer> faceFrequency(Collection<Card> cards) {
		HashMap<Integer, Integer> map = new HashMap<>();

		for (Card c : cards) {
			int face = c.getFaceValue();
			int freq = map.get(face) == null ? 0 : map.get(face);
			map.put(face, freq + 1);
		}

		return map;
	}
}
