package com.amansoni;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author Aman
 *         Implemented from Fu, Haobo, Peter R. Lewis, and Xin Yao.
 *         "A Q-learning Based Evolutionary Algorithm for Sequential Decision Making Problems."
 */
public class QBEA extends QLearning {
    static boolean EA_UPDATES_ALL_ACTIONS = true;
    int seed;
    Map<State, StateTransition> map = new TreeMap<>();


    public QBEA(Environment environment, int seed) {
        super(environment, seed);
        initMap();
    }

    private void initMap() {
        // init the map with all possible
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                map.put(new State(i - offset), new StateTransition(new State(i - offset)));
            }
        }
        // Iterate over them
//        for (Map.Entry<State, StateTransition> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }

    }

    public void learn(int totalSteps) {
        state = new State(environment.getState().center);
        for (int i = 0; i < totalSteps; i++) {
            // employ ea to search on reward space
//            int test = environment.getState().center;
            searchRewardFunction(i);
            // select an action
            Action action = selectAction();
//            if (environment.getState().center != test)
//                System.out.println("STATE MISMATCH");

            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // accumulate the reward
            accumulatedReward += reward;
            State nextState = new State(environment.getState().center);
            // update the learning policy
            super.updatePolicy(state, nextState, action, reward, i);
            updateStateTransition(state, nextState, action);
//            if ((nextState.center != -test) && (state.center != test))
//                System.out.println("STATE MISMATCH");
            state = new State(environment.getState().center);
        }
    }

    private void updateStateTransition(State state, State nextState, Action action) {
        StateTransition stateTransition = map.get(state);
        stateTransition.updateValues(action, nextState);
    }

    private State estimateNextState(State state, Action evalAction) {
        StateTransition stateTransition = map.get(state);
        return stateTransition.getProbableState(evalAction);
    }

    private void searchRewardFunction(int timeStep) {
//        EDOAlgorithm edoAlgorithm = new EDOAlgorithm(environment, seed);
        double learningRate = (200.0 / (300.0 + timeStep));
        State currentState = new State(environment.getState().center);
        double discountFactor = 0.7;
        for (Action evalAction : environment.getActions()) {
            State probableState = estimateNextState(state, evalAction);
            int reward = environment.getReward(evalAction, probableState, evalAction);
            updatePolicy(learningRate, currentState, probableState, evalAction, reward, discountFactor);
        }
//        Action evalAction = edoAlgorithm.selectAction();
//        State probableState = estimateNextState(state, evalAction);
//        int reward = environment.getReward(evalAction);
//        updatePolicy(learningRate, currentState, probableState, evalAction, reward);
    }

    public int getReward(State state, Action x) {
        int reward = state.height - state.width * Math.abs(x.getValue() - state.center) + environment.g();
//        System.out.println(reward);
        return reward;
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
