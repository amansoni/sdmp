package com.amansoni;

/**
 * Created by sonia2 on 23/05/2016.
 */
public class StateTransition {
    State current;
    Action action;
    State next;
    private int numberExperienced;
    private int[][] nextStateCount = new int[21][2];
    public final static int PRIOR_A = 4;
    public final static int POSSIBLE_STATES = 10;

    public StateTransition(State current, Action action){
        this.current = current;
        this.action = action;
        numberExperienced = 0;
        for (int i = 0; i < 21; i++) {
            nextStateCount[i][0] = i;
            nextStateCount[i][1] = 0;
        }
    }

    public String toString(){
        String s = "Current state:" + current.center + " action:" + action.getValue();
        for (int i = 0; i < 21; i++) {
            s += " " + nextStateCount[i][0] + ":" + nextStateCount[i][1];
        }
        return s;
    }

}
