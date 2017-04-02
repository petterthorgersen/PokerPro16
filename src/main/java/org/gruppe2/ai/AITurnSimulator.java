package org.gruppe2.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.gruppe2.game.Card;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.calculation.Showdown;

public class AITurnSimulator {
	Random random = new Random();

	/**
	 * Simulates a number of rounds with the current AI hand. Returns a hand
	 * strength which can be used to decide which actions to perform
	 * 
	 * @param roundPlayer
	 * @param communityCards
	 * @param numberOfRounds
	 *            number of rounds to simulate
	 * @return
	 */
	public double getHandStregth(RoundPlayer roundPlayer,
			List<Card> communityCards, int numberOfRounds, int numOtherPlayers) {
		double numberOfWins = 0;
		Showdown sd = new Showdown();
		for (int i = 0; i < numberOfRounds; i++) {
			List<Card> communityCopy = new ArrayList<Card>(communityCards);
			Collections.copy(communityCopy, communityCards);
			List<RoundPlayer> roundPlayers = new ArrayList<RoundPlayer>();
			
			List<Card> deck = getDeck();
			deck.removeAll(communityCopy);
			deck.remove(roundPlayer.getCards()[0]);
			deck.remove(roundPlayer.getCards()[1]);
			
			while (communityCopy.size() < 5) {
				communityCopy.add(this.drawCard(deck));
			}
			
			for (int j = 0; j < numOtherPlayers; j++) {
				RoundPlayer rp = new RoundPlayer(null, this.drawCard(deck),
						this.drawCard(deck));
				roundPlayers.add(rp);
			}
			
			roundPlayers.add(roundPlayer);
			List<RoundPlayer> winners = sd.getWinnersOfRound(roundPlayers,
					communityCopy);
			if (winners.contains(roundPlayer))
				numberOfWins++;
		}
		
		if (numberOfRounds == 0)
			return 0.0;
		
		return numberOfWins / numberOfRounds;
	}

	private List<Card> getDeck() {
		List<Card> cards = new ArrayList<Card>();
		for (Card.Suit suit : Card.Suit.values()) {
			for (int face = 2; face <= 14; face++) {
				cards.add(new Card(face, suit));
			}
		}
		Collections.shuffle(cards);
		return cards;
	}

	private Card drawCard(List<Card> cards) {
		int cardNumber = random.nextInt(cards.size());
		Card card = cards.get(cardNumber);
		cards.remove(card);
		return card;
	}

}
