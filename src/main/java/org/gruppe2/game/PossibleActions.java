package org.gruppe2.game;

/**
 * Created by Mikal on 14.03.2016.
 */
public class PossibleActions {
    private boolean call;
    private boolean check;
    private boolean raise;
    private boolean allIn;
    private int callAmount;
    private int minRaise;
    private int maxRaise;

    public PossibleActions() {
        call = false;
        check = false;
        raise = false;
        allIn = false;
    }

    public void setCall(int callAmount) {
        this.callAmount = callAmount;
        call = true;
    }

    public void setCheck() {
        check = true;
    }

    public void setAllIn() {
        this.allIn = true;
    }

    public void setRaise(int minRaise, int maxRaise) {
        raise = true;
        this.minRaise = minRaise;
        this.maxRaise = maxRaise;
    }

    public boolean canAllIn() {
        return allIn;
    }

    public boolean canCall() {
        return call;
    }

    public boolean canCheck() {
        return check;
    }

    public boolean canRaise() {
        return raise;
    }

    public int getMinRaise() {
        return minRaise;
    }

    public int getMaxRaise() {
        return maxRaise;
    }

    public int getCallAmount() {
        return callAmount;
    }

    @Override
    public String toString() {
        String options = "> (fold";

        if (call)
            options += String.format(", call (%d)", callAmount);
        if (check)
            options += ", check";
        if (raise)
            options += String.format(", raise (%d to %d)", minRaise, maxRaise);
        if (allIn)
            options += ", allin";

        options += ")";

        return options;
    }
}
