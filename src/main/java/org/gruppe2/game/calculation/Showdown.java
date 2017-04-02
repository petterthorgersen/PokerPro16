package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.RoundPlayer;

public class Showdown {

	public List<RoundPlayer> getWinnersOfRound(List<RoundPlayer> players, List<Card> communityCards) {
		ArrayList<RoundPlayer> winners = new ArrayList<>();
		List<List<Card>> playerHandsWithCommunityCards = new ArrayList<>();
		
		for(int i = 0; i < players.size(); i++) {
			ArrayList<Card> listOfCards = new ArrayList<>();
			listOfCards.add(players.get(i).getCards()[0]);
			listOfCards.add(players.get(i).getCards()[1]);
			
			listOfCards.addAll(communityCards);
			playerHandsWithCommunityCards.add(listOfCards);
		}
		
		int leadingPlayer = compare(0, playerHandsWithCommunityCards);
		
		// Finds the player with the best hand.
		winners.add(players.get(leadingPlayer));
		
		// Add all players with equally good hands.
		for(int i = 0; i < playerHandsWithCommunityCards.size(); i++) {
			if(i != leadingPlayer)
				if(comparePlayers(playerHandsWithCommunityCards.get(leadingPlayer), playerHandsWithCommunityCards.get(i)) == 0)
					winners.add(players.get(i));
		}
		
		return winners;
	}

	public int compare(int leadingPlayer, List<List<Card>> playerHands) {
		for(int i = 0; i < playerHands.size(); i++) {
			if(leadingPlayer != i)
				if(comparePlayers(playerHands.get(leadingPlayer), playerHands.get(i)) == -1) {
					return compare(i, playerHands);
				}
		}
		
		return leadingPlayer;
	}
	
	public int comparePlayers(List<Card> o1, List<Card> o2) {
		HandCalculation highestHand1 = getHandType(o1);;
		HandCalculation highestHand2 = getHandType(o2);;
		
		if(highestHand1.getType().compareTo(highestHand2.getType()) < 0)
			return 1;
		else if(highestHand2.getType().compareTo(highestHand1.getType()) < 0)
			return -1;
		else
			return highestHand1.compare(o1, o2);
	}
	
	public HandCalculation getHandType(List<Card> cards) {
		ArrayList<HandCalculation> calculations = Generic.getAllHandTypes();
		
		for (HandCalculation calc : calculations) {
			if(calc.isHand(cards))
				return calc;
		}
		
		return null; // Should not happen.
	}

}
