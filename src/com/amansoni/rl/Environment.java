package com.amansoni.rl;

/**
 * Created by Aman on 14/05/2016.
 */
public class Environment {
    final static boolean DEBUG = false;
    private int timestep;
    private int currentReward;
    private State currentState;
    private State previousState;
    private Action[] actions;
    // bias
    private int b = -100;

    public Environment(int bias) {
        init();
        b = -bias;
        previousState = new State();
        previousState.center = 5;
        step(); // init values to starting state
    }

    public void step() {
        timestep++;
        currentState = previousState;
        currentState.center = -currentState.center;
        b = -b;
        if (DEBUG)
            System.out.println("Timestep: " + timestep + " State:" + currentState.center);
    }

    public State observe() {
        return currentState;
    }

    public State getState() {
        return currentState;
    }

    public Action[] getActions() {
        return actions;
    }

    public int takeAction(Action x) {
        this.step();
        currentReward = currentState.height - currentState.width * Math.abs(x.getValue() - currentState.center) + g();
        return currentReward;
    }

    private int g() {
        b = -b;
        return b;
    }

    void init() {
        timestep = 0;
        // actions
        int noOfActions = 21;
        actions = new Action[noOfActions];
        int val = -10;
        for (int i = 0; i < noOfActions; i++) {
            actions[i] = new Action(val++);
        }
    }
}
