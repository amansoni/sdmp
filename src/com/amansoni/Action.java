package com.amansoni;

/**
 * Created by Aman on 14/05/2016.
 */
public class Action implements Comparable<Action> {
    private int value;

    public Action() {
    }

    public Action(int action) {
        value = action;
    }

    public int getValue() {
        return value;
    }

    @Override
    public int compareTo(Action action) {
        return new Integer(action.getValue()).compareTo(this.value);
    }

}
