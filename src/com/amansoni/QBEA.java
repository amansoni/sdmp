package com.amansoni;

import java.util.Map;
import java.util.TreeMap;
import com.amansoni.EvolutionaryAlgorithm.Strategy;

/**
 * @author Aman
 *         Implemented from Fu, Haobo, Peter R. Lewis, and Xin Yao.
 *         "A Q-learning Based Evolutionary Algorithm for Sequential Decision Making Problems."
 */
public class QBEA extends QLearning {
    // Flag to select only a single random action
    static boolean RANDOM_SELECT_SINGLE_ACTION = false;
    double priorA = 3.;
    int seed;
    Map<State, StateTransition> map = new TreeMap<>();
    Strategy strategy = Strategy.Full;

    public QBEA(Environment environment, int seed) {
        super(environment, seed);
        initMap();
    }

    public QBEA(Environment environment, int seed, Strategy strategy) {
        this(environment, seed);
        this.strategy = strategy;
    }

    public QBEA(Environment environment, int seed, double[] params) {
        this(environment, seed);
        discountFactor = params[0];
        priorA = params[1];
    }

    private void initMap() {
        // init the map with all possible
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                map.put(new State(i - offset), new StateTransition(new State(i - offset), priorA));
            }
        }
        // Iterate over them
//        for (Map.Entry<State, StateTransition> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }
    }

    public void learn(int totalSteps, int offlineTime) {
        for (int i = 0; i < totalSteps; i++) {
            step(i, offlineTime);
        }
//        state = new State(environment.getState().center);
//        for (int i = 0; i < totalSteps; i++) {
//            // employ ea to search on reward space
//            searchRewardFunction(i, offlineTime);
//            // select an action
//            Action action = new Action(getActionForMaxRewardForState(state) - offset);
//            // perform the action and get a reward
//            int reward = environment.takeAction(action);
//            // accumulate the reward
//            accumulatedReward += reward;
//            State nextState = new State(environment.getState().center);
//            // update the learning policy
////            super.updatePolicy(state, nextState, action, reward, i);
//            updateStateTransition(state, nextState, action);
//            state = new State(environment.getState().center);
//        }
    }

    @Override
    public int step(int step, int offlineTime) {
        state = new State(environment.getState().center);
        // employ ea to search on reward space
        searchRewardFunction(step, offlineTime);
        // select an action
        Action action = new Action(getActionForMaxRewardForState(state) - offset);
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        // accumulate the reward
        accumulatedReward += reward;
        State nextState = new State(environment.getState().center);
        // update the learning policy
        super.updatePolicy(state, nextState, action, reward, step);
        updateStateTransition(state, nextState, action);
        state = new State(environment.getState().center);
        return reward;
    }

    private void updateStateTransition(State state, State nextState, Action action) {
        StateTransition stateTransition = map.get(state);
        stateTransition.updateValues(action, nextState);
    }

    private State estimateNextState(State state, Action evalAction) {
        StateTransition stateTransition = map.get(state);
        return stateTransition.getProbableState(evalAction);
    }

    private void searchRewardFunction(int timeStep, int offlineTime) {
        double learningRate = (200.0 / (300.0 + timeStep));
        State currentState = new State(environment.getState().center);
        double discountFactor = 0.7;

        Action[] actions;
        if (RANDOM_SELECT_SINGLE_ACTION){
            actions = new Action[] {environment.getActions()[random.nextInt(environment.getActions().length)]};
        } else {
            actions = new EvolutionaryAlgorithm(environment, strategy, random, offlineTime, false).getActions();
        }
//        actions = environment.getActions();
//        System.out.println("searchRewardFunction:" + timeStep + "\taction length:\t" + actions.length + "\toffline\t" + offlineTime);
        for (Action evalAction : actions) {
            State probableState = estimateNextState(state, evalAction);
            int reward = environment.getReward(evalAction, probableState, evalAction);
            updatePolicy(learningRate, currentState, probableState, evalAction, reward, discountFactor);
        }
    }

    /**
     * Update the Q-values based on probable states for the EA search
     * Q(st, xi)   (1 − alpha)Q(st, xi) + alpha(ft(st, xi) + upsilon *  maxj Q(ˆs, xj));
     *
     * @param state
     * @param nextState
     * @param action
     */

    private void updatePolicy(double learningRate, State state, State nextState, Action action, int reward, double discountFactor) {
        double currentQ = QValues[state.center + offset][action.getValue() + offset];
        double transitionQ = getBestQForState(nextState);
        double updatedQValue =
                (1.0 - learningRate) * currentQ + learningRate * (reward + discountFactor * transitionQ);
        QValues[state.center + offset][action.getValue() + offset] = updatedQValue;
        if (DEBUG) {
            System.out.print(" learningRate:" + df.format(learningRate));
            System.out.print(" state:" + state.center);
            System.out.print(" action:" + action.getValue());
            System.out.print(" next state:" + nextState.center);
            System.out.print(" Change Q value:" + df.format(currentQ));
            System.out.print(" to " + df.format(updatedQValue));
            System.out.println("");
        }
    }


}