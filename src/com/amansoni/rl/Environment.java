package com.amansoni.rl;

/**
 * Created by Aman on 14/05/2016.
 */
public class Environment {
    final static boolean DEBUG = true;
    private int timeStep;
    private int currentReward;
    private State state = new State();
    private Action[] actions;
    private Action previousAction;
    // bias
    private int b;
    private int accumulatedReward = 0;

    public Environment(int bias) {
        b = bias;
        init();
    }

    public Action[] getActions() {
        return actions;
    }

    public int takeAction(Action x) {
        timeStep++;
        currentReward = getReward(x);// state.height - state.width * Math.abs(x.getValue() - state.center) + g();
        accumulatedReward += currentReward;
        if (DEBUG)
            System.out.println(timeStep + "\tstate:" + state.center + " \taction:" + x.getValue() + "\treward:" + currentReward + "\ttotal:" + accumulatedReward);
        state.center = -state.center;
        previousAction = x;
        return currentReward;
    }

    public int getReward(Action x) {
        return state.height - state.width * Math.abs(x.getValue() - state.center) + g();
    }

    private int g() {
        if (previousAction == null || previousAction.getValue() >= 0)
            return b;
        else
            return -b;
    }

    void init() {
        timeStep = 0;
        initActions();
    }

    public void initActions() {
        // actions
        int noOfActions = 21;
        actions = new Action[noOfActions];
        int val = -10;
        for (int i = 0; i < noOfActions; i++) {
            actions[i] = new Action(val++);
        }
    }

    public int getBias() {
        return b;
    }

    public int getTimeStep() {
        return timeStep;
    }

    public State getState() {
        return state;
    }
}
