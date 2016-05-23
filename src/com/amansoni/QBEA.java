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
    Map<Integer, StateTransition> map = new TreeMap<>();



    public QBEA(Environment environment, int seed) {
        super(environment, seed);
        initMap();
    }

    private void initMap() {

        // init the map with all possible
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                map.put(new Integer(1), new StateTransition(new State(i), new Action(j)));
            }
        }
        // Iterate over them
        for(Map.Entry<Integer,StateTransition> entry : map.entrySet()) {
            System.out.println(entry.getKey() + " => " + entry.getValue());
        }

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
//            if ((nextState.center != -test) && (state.center != test))
//                System.out.println("STATE MISMATCH");
            state = new State(environment.getState().center);
        }
    }

    private void searchRewardFunction(int timeStep) {
        EDOAlgorithm edoAlgorithm = new EDOAlgorithm(environment, seed);
//        for (int i = -10; i < 11; i++) {
//        System.out.println(" i:" + i);
        if (EA_UPDATES_ALL_ACTIONS) {
            for (Action evalAction : environment.getActions()) {
//                for (int i = 0; i < 2; i++) {
                    State probableState = estimateNextState(state, evalAction, timeStep);
                    updatePolicy(edoAlgorithm.environment.getState(), probableState, evalAction, timeStep);
//                }
            }
        } else {
            Action evalAction = edoAlgorithm.selectAction();
            State probableState = estimateNextState(state, evalAction, timeStep);
            updatePolicy(edoAlgorithm.environment.getState(), probableState, evalAction, timeStep);
        }
//        }
//        System.out.println(" state:" + state.center + " next state:" + probableState.center);
    }

    private State estimateNextState(State state, Action evalAction, int timeStep) {
//        for (int i = 0; i < noOfStates; i++) {
//            if ((i - offset) == 5 || (i - offset) == -5) {
//                System.out.print((i - offset) + "\t");
//                for (int j = 0; j < noOfActions; j++) {
//                    System.out.print(df.format(QValues[i][j]) + "\t");
//                }
//                System.out.println("");
//            }
//        }

//        State[] probableStates = new State[2];
        State a = new State();
//        if (evalAction.getValue() < 0) {
//            a.center = -5;
//        } else {
//            a.center = 5;
//        }
//        if (random.nextDouble() > 0.5) {
//        if (state.center == 5) {
        double probability = .85 + timeStep / 1000;
        if (evalAction.getValue() < 0) {
            if (random.nextDouble() < probability) {
                a.center = 5;
            } else {
                a.center = -5;
            }
        } else {
            if (random.nextDouble() > probability) {
                a.center = 5;
            } else {
                a.center = -5;
            }
        }
//        if (evalAction.getValue() < 0) {
//            a.center = -5;
//        } else {
//            a.center = 5;
//        }

        return a;

//        a.center = 5;
////        probableStates[0] = a;
//        State b = new State();
//        b.center = -5;
////        probableStates[1] = b;
//        if (random.nextDouble() > 0.5)
//            return a;
//        else
//            return b;
    }

    /**
     * Update the Q-values based on probable states for the EA search
     * Q(st, xi)   (1 − alpha)Q(st, xi) + alpha(ft(st, xi) + upsilon *  maxj Q(ˆs, xj));
     *
     * @param state
     * @param nextState
     * @param action
     * @param timestep
     */

    private void updatePolicy(State state, State nextState, Action action, int timestep) {
        int reward = environment.getReward(action);
        double learningRate = (200.0 / (300.0 + timestep));
        double currentQ = QValues[state.center + offset][action.getValue() + offset];
        double transitionQ = getBestQForState(nextState);
        if (DEBUG) {
            System.out.print(" time step:" + timestep);
            System.out.print(" learningRate:" + df.format(learningRate));
            System.out.print(" Change Q value:" + df.format(currentQ));
        }
        double updatedQValue =
                (1.0 - learningRate) * currentQ + learningRate * (reward + discountFactor * transitionQ);
        QValues[state.center + offset][action.getValue() + offset] = updatedQValue;
        if (DEBUG) {
            System.out.print(" to " + df.format(updatedQValue));
            System.out.println("");
        }
    }
}
