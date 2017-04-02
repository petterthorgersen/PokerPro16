package org.gruppe2.game.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.gruppe2.game.Card;
import org.gruppe2.game.Card.Suit;
import org.gruppe2.game.RoundPlayer;
import org.junit.Assert;
import org.junit.Test;

public class ShowdownTest {
	public static final int N = 4;
	private static final Showdown showdown = new Showdown();
	
	@Test
	public void everyoneWinsWhenRoyalFlushIsInCommunityCards() {
		List<Card> commcards = new ArrayList<>();
		List<RoundPlayer> players = new ArrayList<>();
		
		Random r = new Random();
		for(int i = 0; i < N; i++) {
			Card c1 = new Card(r.nextInt(13)+2,Suit.DIAMONDS);
			Card c2 = new Card(r.nextInt(13)+2,Suit.SPADES);
			
			players.add(new RoundPlayer(null, c1, c2));
		}
		
		commcards.add(new Card(14, Suit.HEARTS));
		commcards.add(new Card(13, Suit.HEARTS));
		commcards.add(new Card(12, Suit.HEARTS));
		commcards.add(new Card(11, Suit.HEARTS));
		commcards.add(new Card(10, Suit.HEARTS));
		
		List<RoundPlayer> winners = showdown.getWinnersOfRound(players, commcards);
		
		Assert.assertTrue(winners.size() == N);
	}
	
	@Test
	public void peopleWithRoyalFlushShouldBeWinnersAndOthersShouldBeLosers() {
		List<Card> commcards = new ArrayList<>();
		List<RoundPlayer> players = new ArrayList<>();
		
		Random r = new Random();
		for(int i = 0; i < N; i++) {
			Card c1 = new Card(r.nextInt(13)+2,Suit.DIAMONDS);
			Card c2 = new Card(r.nextInt(13)+2,Suit.SPADES);
			
			players.add(new RoundPlayer(null, c1, c2));
		}
		
		RoundPlayer winner = new RoundPlayer(null, new Card(10, Suit.HEARTS), new Card(2, Suit.CLUBS));
		players.add(winner);
		
		commcards.add(new Card(14, Suit.HEARTS));
		commcards.add(new Card(13, Suit.HEARTS));
		commcards.add(new Card(12, Suit.HEARTS));
		commcards.add(new Card(11, Suit.HEARTS));
		
		List<RoundPlayer> winners = showdown.getWinnersOfRound(players, commcards);
		
		Assert.assertTrue(winners.size() == 1);
		if(winners.size() > 0)
			Assert.assertTrue(winners.get(0).equals(winner));
	}
	
	@Test
	public void straightBeatsPair() {
		List<Card> commcards = new ArrayList<>();
		List<RoundPlayer> players = new ArrayList<>();
		
		// Guy with straight
		RoundPlayer winner = new RoundPlayer(null, new Card(7, Suit.DIAMONDS), new Card(5, Suit.DIAMONDS));
		players.add(winner);
		
		// Pair
		players.add(new RoundPlayer(null, new Card(8, Suit.DIAMONDS), new Card(11, Suit.CLUBS)));
		// Highcard
		players.add(new RoundPlayer(null, new Card(14, Suit.DIAMONDS), new Card(13, Suit.CLUBS)));
		
		commcards.add(new Card(8, Suit.SPADES));
		commcards.add(new Card(6, Suit.HEARTS));
		commcards.add(new Card(4, Suit.CLUBS));
		commcards.add(new Card(2, Suit.HEARTS));
		
		List<RoundPlayer> winners = showdown.getWinnersOfRound(players, commcards);
		
		Assert.assertTrue(winners.size() == 1);
		if(winners.size() > 0)
			Assert.assertTrue(winners.get(0).equals(winner));
	}
	
	@Test
	public void personWithBestPairWinsRoundAlone() {
		List<Card> commcards = new ArrayList<>();
		List<RoundPlayer> players = new ArrayList<>();
		
		// Pair of Kings
		RoundPlayer winner = new RoundPlayer(null, new Card(13, Suit.DIAMONDS), new Card(13, Suit.DIAMONDS));
		players.add(winner);
		
		// Pair of 8
		players.add(new RoundPlayer(null, new Card(8, Suit.DIAMONDS), new Card(11, Suit.CLUBS)));
		// Highcard
		players.add(new RoundPlayer(null, new Card(14, Suit.DIAMONDS), new Card(13, Suit.CLUBS)));
		
		commcards.add(new Card(8, Suit.SPADES));
		commcards.add(new Card(6, Suit.HEARTS));
		commcards.add(new Card(4, Suit.CLUBS));
		commcards.add(new Card(2, Suit.HEARTS));
		
		List<RoundPlayer> winners = showdown.getWinnersOfRound(players, commcards);
		
		Assert.assertTrue(winners.size() == 1);
		if(winners.size() > 0)
			Assert.assertTrue(winners.get(0).equals(winner));
	}
}
