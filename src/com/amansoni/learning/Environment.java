package com.amansoni.learning;

import java.util.Arrays;

/**
 * Created by Aman on 25/03/2016.
 */
public class Environment {

    private int[] state = new int[3];
    private int xminus1;
    private int c;
    private int bias;

    public Environment(){
        Main.log(this, "Initialise...");
        c=5;
        bias = 100;
        state = new int[] {0,0,0};
        updateState();
    }

    public int[] GetState() {
        Main.log(this, "GetState() + " + Arrays.toString(state));
        return state;
    }

    public void perform(Action action) {
        Main.log(this, "perform action " + action.toString());
        updateState();
    }

    public int GetReward(int s, int x){
        c = c * -1;
        int reward = 30 - 2 * Math.abs(x - c);
        if (xminus1 >= 0){
            reward+=bias;
        } else {
            reward-=bias;
        }
        xminus1 = x;
        Main.log(this, "Return reward " + reward);
        return reward ;
    }

    private void updateState(){
        for (int i = 0; i < state.length; i++) {
            state[i] = getChangedState(state[i]);
        }
    }

    private int getChangedState(int thistate) {
        return thistate+1;
    }

}
