package org.gruppe2.game.session;

class MessageEntry {
    private final String name;
    private final Object[] args;
    private final Query<Boolean> returnVal;

    MessageEntry(String name, Object[] args) {
        this.name = name;
        this.args = args;
        returnVal = new Query<>();
    }

    public String getName() {
        return name;
    }

    Object[] getArgs() {
        return args;
    }

    Query<Boolean> getReturnVal() {
        return returnVal;
    }
}
