package com.amansoni;

import java.util.Random;

/**
 * Created by Aman on 16/05/2016.
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

    public double getAccumulatedReward() {
        return accumulatedReward;
    }

    public abstract void learn(int totalSteps);

    public abstract int step(int step);

    public abstract Action selectAction();

    public abstract void printPolicy();

    public void writeReward(int timeStep){
        System.out.println(timeStep + "\t" + accumulatedReward);
    }
}
