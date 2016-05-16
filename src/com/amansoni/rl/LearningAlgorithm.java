package com.amansoni.rl;

/**
 * Created by Aman on 16/05/2016.
 */
public abstract class LearningAlgorithm {
    int accumulatedReward = 0;
    State state;

    public double getAccumulatedReward() {
        return accumulatedReward;
    }

    public abstract void learn(int totalSteps);

    public abstract Action selectAction();

    public abstract void printPolicy();

}
