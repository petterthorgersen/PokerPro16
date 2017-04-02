package org.gruppe2.ai;

import java.util.ArrayList;
import java.util.Random;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.RoundPlayer;

public class NewDumbAI implements AI {
    private static final String[] names = {"Alpha", "Bravo", "Charlie",
            "Delta", "Echo", "Foxtrot", "Golf", "Hotel", "India", "Juliet",
            "Kilo", "Lima", "Mike", "November", "Oscar", "Papa", "Quebec",
            "Romeo", "Sierra", "Tango", "Uniform", "Victor", "Whiskey",
            "X-Ray", "Yankee", "Zulu"};

    private static final Random rand = new Random();

    @Override
    public void doAction(Player model, RoundPlayer roundPlayer, GameInfo gameInfo) {
        if (!model.isBot())
            return;

        final int call = 0;
        final int check = 1;
        final int raise = 2;
        final int fold = 3;
        final int allIn = 4;

        PossibleActions options = gameInfo.getPossibleActions();
        Random rand = new Random();
        ArrayList<Integer> types = new ArrayList<>();

        if (options.canCall()) {
            for (int i = 0; i < 8; i++)
                types.add(call);
        }

        if (options.canCheck()) {
            for (int i = 0; i < 8; i++)
                types.add(check);
        }

        if (options.canRaise()) {
            for (int i = 0; i < 3; i++)
                types.add(raise);
        }

        if (options.canAllIn()) {
            for (int i = 0; i < 1; i++)
                types.add(allIn);
        }

        types.add(fold);

        switch (types.get(rand.nextInt(types.size()))) {
            case call:
                model.getAction().set(new Action.Call());
                return;

            case check:
                model.getAction().set(new Action.Check());
                return;

            case raise:
                if (options.getMinRaise() == options.getMaxRaise())
                    model.getAction().set(new Action.Raise(options.getMaxRaise()));
                int maxRaiseAmount = options.getMaxRaise();
                double smartRaise = rand.nextDouble();
                if (smartRaise <= 0.90)
                    maxRaiseAmount = (int) (Math.ceil(maxRaiseAmount * 0.05));
                else if (smartRaise > 0.90 && smartRaise <= 0.999)
                    maxRaiseAmount = (int) (Math.ceil(maxRaiseAmount * 0.20));
                if (maxRaiseAmount == options.getMaxRaise()
                        || maxRaiseAmount - options.getMinRaise() <= 1)
                    model.getAction().set(new Action.Raise(maxRaiseAmount));
                else
                    model.getAction().set(
                            new Action.Raise(rand.nextInt(maxRaiseAmount
                                    - options.getMinRaise())
                                    + options.getMinRaise()));
                return;

            case allIn:
                model.getAction().set(new Action.AllIn());
                return;

            default:
                model.getAction().set(new Action.Fold());
        }

        // model.getAction().set(new Action.Fold());
    }

    public static String randomName() {
        return names[rand.nextInt(names.length)];
    }
}
