package com.amansoni;

/**
 * @author Aman
 *         <p>
 *         Provides the Conceptual Moving Peaks Benchmarks from Fu, Haobo, et al.
 *         "What are dynamic optimization problems?." Evolutionary Computation (CEC), 2014 IEEE Congress on. IEEE, 2014.
 */
public class Environment {
    private boolean debug = false;
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

    public Environment(int bias, boolean debug) {
        this(bias);
        this.debug = debug;
    }

    public Action[] getActions() {
        return actions;
    }

    public int takeAction(Action x) {
        timeStep++;
        currentReward = getReward(x);// state.height - state.width * Math.abs(x.getValue() - state.center) + g();
        accumulatedReward += currentReward;
        if (debug)
            System.out.println(timeStep + "\tstate:" + state.center + " \taction:" + x.getValue() + "\treward:" + currentReward + "\ttotal:" + accumulatedReward);
        state.center = -state.center;
        previousAction = x;
        return currentReward;
    }

    public int getReward(Action x) {
        int reward = state.height - state.width * Math.abs(x.getValue() - state.center) + g();
//        System.out.println(reward);
        return reward;
    }

    public int getReward(Action x, State probableState, Action probableAction) {
        int bias = (x.getValue() >= 0 ? b : -b);
        int reward = probableState.height - probableState.width * Math.abs(probableAction.getValue() - probableState.center) + bias;
//        System.out.println(reward);
        return reward;
    }

    public int g() {
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
