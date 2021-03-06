package com.amansoni;

import java.util.Random;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation for learning algorithms used for comparison experiments.
 */
public abstract class LearningAlgorithm {
    final static boolean DEBUG = false;
    Environment environment;
    Random random;
    State state;
    int seed;
    int accumulatedReward = 0;

    protected LearningAlgorithm(Environment environment, int seed) {
        random = new Random(seed);
        this.environment = environment;
        this.seed = seed;
    }

    public int getAccumulatedReward() {
        return accumulatedReward;
    }

    public void reset(Environment environment, int seed){
        this.seed = seed;
        random = new Random(seed);
        accumulatedReward = 0;
        this.environment = environment;
    }

    public abstract void learn(int totalSteps, int offlineTime);

    public abstract int step(int step, int offlineTime);

    public abstract Action selectAction(int offlineTime);

    public abstract void printPolicy();

}
