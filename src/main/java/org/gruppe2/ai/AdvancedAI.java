package org.gruppe2.ai;

import java.util.Random;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.RoundPlayer;

public class AdvancedAI implements AI {

	@Override
	public void doAction(Player player, RoundPlayer roundPlayer, GameInfo gameInfo) {
		int iterations;
		switch (gameInfo.difficulty){
		case EASY :
			iterations=1;
		case NORMAL :
			iterations=20;
			break;
		case HARD :
		default :
			iterations=1000;
		
		}
		AITurnSimulator turnSim = new AITurnSimulator();
		PossibleActions possibleActions = gameInfo.getPossibleActions();
		double bank, handStrength, handStrengthExponential, toRaise, raiseRatio = 1, risk, 
		exponentialMax, exponentialMin, rateOfReturn = 0;
		
		bank = player.getBank();
		toRaise = gameInfo.getHighestBet() - roundPlayer.getBet();
		risk = Math.max(Math.min(toRaise / bank, 0.7), 0.3);
		
		if (roundPlayer.getBet() != 0) {
			raiseRatio = Math.max(toRaise / roundPlayer.getBet(), 0.5);
		}
		
		exponentialMax = 1.1;
		exponentialMin = 1 - (exponentialMax - 1.0);
		handStrength = turnSim.getHandStregth(roundPlayer, gameInfo.getCommunityCards(), iterations,
				gameInfo.getActivePlayers().size());
		handStrengthExponential = (1 / (exponentialMax - handStrength)) - exponentialMin;
		
		rateOfReturn = handStrengthExponential / risk;
		rateOfReturn *= (1 / raiseRatio);
//		rateOfReturn = rateOfReturn + 1;
		
		Action action = chooseAction(rateOfReturn, risk, handStrengthExponential, possibleActions, bank, handStrength, gameInfo);
		player.getAction().set(action);
	}

	public Action chooseAction(double rateOfReturn, double risk, double handStrengthExponential, PossibleActions actions, double bank, double handStrength,
			GameInfo gameInfo) {
		Random r = new Random();
		int random = r.nextInt(100) + 1;
		
		/*
		if(actions.canCheck()) {
			if(handStrengthExponential > 0.99)
				return new Action.Raise((int)(gameInfo.getBigBlind()*(handStrengthExponential)));
			else
				return new Action.Check();
		}else if(actions.canCall()) {
			return new Action.Call();
		}else
			return new Action.Fold();
		
//		return new Action.Fold();
		*/
		
		if (rateOfReturn < 0.1) {
			if (handStrength > 0.2) {
				if (actions.canRaise()) {
					return new Action.Raise(gameInfo.getBigBlind());
				} else if (actions.canCall())
					return new Action.Call();
				else if (actions.canAllIn())
					return new Action.AllIn();
				else if (actions.canCheck())
					return new Action.Check();
			} else {
				if (actions.canCheck())
					return new Action.Check();
			}
			return new Action.Fold();
		}

		if (rateOfReturn < 0.6) {
			if (actions.canCheck())
				return new Action.Check();
			if (random > 95) {
				if (actions.canRaise()) {
					if (actions.getMaxRaise() > 10 * gameInfo.getBigBlind())
						return new Action.Raise(gameInfo.getBigBlind());
				}
			} else
				return new Action.Fold();

		}
		if (rateOfReturn < 1.2) {
			if (actions.canCheck()) {
				return new Action.Check();
			}
			if (random > 80 && random <= 95) {
				if (actions.canRaise() && actions.getMaxRaise() > 8 * gameInfo.getBigBlind()) {
					return new Action.Raise(gameInfo.getBigBlind());
				}
			}
			if (random > 95) {
				if (actions.canCall())
					return new Action.Call();
			}
			return new Action.Fold();

		}
		if (rateOfReturn <= 6.0) {
			if (random <= 60 && actions.canCall()) {
				return new Action.Call();
			} else if (actions.canRaise() && actions.getMaxRaise() > gameInfo.getBigBlind() * 3) {
				return new Action.Raise(gameInfo.getBigBlind() * 2);
			}
			else if (actions.canCheck())
				return new Action.Check();

		}
		if (rateOfReturn > 6.0) {
			if (random <= 30) {
				if (actions.canCall())
					return new Action.Call();
				else if (actions.canCheck())
					return new Action.Check();
			} else {
				if (actions.canRaise()) {
					int numberOfBigBlinds = actions.getMaxRaise() / gameInfo.getBigBlind();
					int randomBlinds = 0;
					if (numberOfBigBlinds>0)
					randomBlinds = r.nextInt(numberOfBigBlinds) + 1;
					if (randomBlinds==0){
						if (actions.canCall())
							return new Action.Call();
						if (actions.canCheck())
							return new Action.Check();
						return new Action.Fold();
					}
					return new Action.Raise(gameInfo.getBigBlind() * randomBlinds);
				} else if (actions.canCall())
					return new Action.Call();
				else if (actions.canCheck()) {
					return new Action.Check();
				}
				return new Action.Fold();
			}
		}
		return new Action.Fold();
	}

}
