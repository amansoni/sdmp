package com.amansoni;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Created by sonia2 on 23/05/2016.
 */
public class StateTransition {
    State current;
    Map<Action, Integer> numberExperienced = new TreeMap<>();
    private int[][][] numberObserved = new int[21][21][1];
    double PRIOR_A = 4.;
    public final static double POSSIBLE_STATES = 21.; // this may need to be a count?
    int offset = 10;
    int seed = 1;

    public StateTransition(State current) {
        this.current = current;
        for (int i = 0; i < 21; i++) {
            numberObserved[i][i][0] = 0; // number observed (state, action) => next state
        }
    }

    public StateTransition(State current, double priorA) {
        this(current);
        PRIOR_A = priorA;
    }

    public State getProbableState(Action action) {
        double maxProbability = 0.;
        int[] nextStates = new int[21];
        int count = 0;
        for (int i = 0; i < 21; i++) {
            State nextState = new State(i - offset);
            double probability = getStateProbability(nextState, action);
            if (probability == maxProbability) {
                maxProbability = probability;
                nextStates[count++] = i - offset;
            }
            if (probability > maxProbability) {
                count = 0;
                maxProbability = probability;
                nextStates[count++] = i - offset;
            }
        }
        int selected = new Random().nextInt(count);
//        System.out.println("State:" + this.current.center + " " + nextStates[selected]);
        return new State(nextStates[selected]);
    }

    /**
     * the state transition probability distribution is
     * p(s_{i+1}| s_{i}, x) = (a + num_observed) /  ( b*a + num_experienced),
     * where 'a' is a fixed prior parameter, say 4.
     * 'b' is the number of possible states after execute action 'x' in state 's_{i}'.
     * 'num_experienced' is the number of times the pair (s_{i}, x) happened so far.
     * 'num_observed' is the number of times (s_{i}, x) happened and followed by the next state s_{i+1} so far.
     *
     * @param nextState
     * @param action
     * @return
     */
    public double getStateProbability(State nextState, Action action) {
        Integer experienced = numberExperienced.get(action);
        if (experienced == null)
            experienced = 0;
        Integer observed = numberObserved[action.getValue() + offset][nextState.center + offset][0];

        double probability = PRIOR_A + observed / (POSSIBLE_STATES * PRIOR_A + experienced);
        String s = "Current state:" + current.center;
        s += " next state:" + nextState.center;
        s += " action:" + action.getValue();
        s += " probability:" + probability;
//        System.out.println(s);
        return probability;
    }

    public String toString() {
        String s = "Current state:" + current.center;
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                s += " Action:" + (i - offset) + " State:" + (j - offset) + " count:" + numberObserved[i][j][0];
            }
        }
        return s;
    }

    public void updateValues(Action action, State nextState) {
        Integer count = numberExperienced.get(action);
        if (count == null) {
            count = 0;
        } else {
            count++;
        }
        numberExperienced.put(action, count);
        numberObserved[action.getValue() + offset][nextState.center + offset][0]++;
    }
}
