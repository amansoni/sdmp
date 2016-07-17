package com.amansoni;

/**
 * Created by Aman on 19/05/2016.
 */
public class Optimal extends LearningAlgorithm {

    protected Optimal(Environment environment, int seed) {
        super(environment, seed);
    }

    @Override
    public void learn(int totalSteps, int offlineTime) {
        for (int i = 0; i < totalSteps; i++) {
            // accumulate the reward
            accumulatedReward +=  step(i, offlineTime);
        }

    }

    @Override
    public int step(int step, int offlineTime) {
        state = new State(environment.getState().center);
        Action action = selectAction(offlineTime);
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        state = new State(environment.getState().center);
        return reward;
    }

    @Override
    public Action selectAction(int offlineTime) {
        if (state.center >= 0)
            return new Action(state.center);
        else
            return new Action(0);
    }

    @Override
    public void printPolicy() {

    }
}
