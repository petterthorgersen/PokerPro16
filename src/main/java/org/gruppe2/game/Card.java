package org.gruppe2.game;

import java.io.Serializable;

public class Card implements Comparable<Card>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4393004343420861807L;
	// to simplify things
	public static final int JACK = 11;
	public static final int QUEEN = 12;
	public static final int KING = 13;
	public static final int ACE = 14;
	private Suit suit;
	private int faceValue;

	public Card(int faceValue, Suit suit) {
		if (faceValue < 2 || faceValue > 14) {
			throw new IllegalArgumentException(
					"faceValue can't be less than 2 or bigger than 14");
		} else {
			this.faceValue = faceValue;
			this.suit = suit;
		}
	}

	public Suit getSuit() {
		return suit;
	}

	public int getFaceValue() {
		return faceValue;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Card card = (Card) o;

		return faceValue == card.faceValue && suit == card.suit;

	}

	@Override
	public int hashCode() {
		int result = suit != null ? suit.hashCode() : 0;
		result = 31 * result + faceValue;
		return result;
	}

	/**
	 * CompareTo used for sorting a list of cards. We don't care about suit only
	 * faceValue
	 *
	 * @param other
	 *            card to compare
	 * @return usual compareTo result
	 */
	@Override
	public int compareTo(Card other) {
		return Integer.compare(this.getFaceValue(), other.getFaceValue());
	}

	@Override
	public String toString() {
		String face = "";
		String suit = "";

		switch (getFaceValue()) {
		case JACK:
			face = "J";
			break;

		case QUEEN:
			face = "Q";
			break;

		case KING:
			face = "K";
			break;

		case ACE:
			face = "A";
			break;

		case 10:
			face = "1";
			break;

		default:
			face = String.valueOf(getFaceValue());
			break;
		}

		switch (getSuit()) {
		case CLUBS:
			suit = "C";
			break;

		case DIAMONDS:
			suit = "D";
			break;

		case HEARTS:
			suit = "H";
			break;

		case SPADES:
			suit = "S";
			break;

		}
		return face+suit;
	}

	public String toPrettyString() {
		String face;

		switch (getFaceValue()) {
		case JACK:
			face = "J";
			break;

		case QUEEN:
			face = "Q";
			break;

		case KING:
			face = "K";
			break;

		case ACE:
			face = "A";
			break;

		default:
			face = String.valueOf(getFaceValue());
			break;
		}

		return face + getSuit().getUnicodeSymbol();
	}

	public enum Suit {
		CLUBS('\u2663'), DIAMONDS('\u2666'), HEARTS('\u2665'), SPADES('\u2660');

		private char symbol;

		Suit(char symbol) {
			this.symbol = symbol;
		}

		public char getUnicodeSymbol() {
			return symbol;
		}
	}
}
