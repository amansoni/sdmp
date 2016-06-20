package com.amansoni;

import java.util.Random;

/**
 * @author Aman
 *         <p>
 *         Provides the Conceptual Moving Peaks Benchmarks from Fu, Haobo, et al.
 *         "What are dynamic optimization problems?." Evolutionary Computation (CEC), 2014 IEEE Congress on. IEEE, 2014.
 */
public class Environment {
    // TODO Expand change types using EDO dynamics paper
    public enum ChangeType {
        Original, Random, Cyclic
    }

    private boolean debug = false;
    private int timeStep;
    private int currentReward;
    private State state = new State();
    private Action[] actions;
    private Action previousAction;
    static int NUMBER_OF_ACTIONS = 21;
    static int ACTION_RANGE = 10;

    // bias
    private int b;
    private int accumulatedReward = 0;
    private ChangeType changeType = ChangeType.Original;
    private int seed = 0;
    Random random;

    public Environment(int bias) {
        b = bias;
        init();
    }

    public Environment(int bias, int seed, ChangeType changeType) {
        this(bias);
        this.seed = seed;
        random = new Random(seed);
        this.changeType = changeType;
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
        changeState();
        previousAction = x;
        return currentReward;
    }

    private void changeState() {
        switch (this.changeType) {
            case Random:
                state.center = random.nextInt(NUMBER_OF_ACTIONS) - ACTION_RANGE;
                break;
            case Cyclic:
                state.center++;
                if (state.center > ACTION_RANGE)
                    state.center = -ACTION_RANGE;
                break;
            default: // Starting CMPB from QBEA paper
                state.center = -state.center;
                break;

        }
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
        actions = new Action[NUMBER_OF_ACTIONS];
        int val = -ACTION_RANGE;
        for (int i = 0; i < NUMBER_OF_ACTIONS; i++) {
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