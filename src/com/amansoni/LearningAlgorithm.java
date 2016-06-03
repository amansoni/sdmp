package com.amansoni;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

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
    Map<Integer, Integer> rewards = new TreeMap<>();


    protected LearningAlgorithm(Environment environment, int seed) {
        random = new Random(seed);
        this.environment = environment;
        this.seed = seed;
    }

    public double getAccumulatedReward() {
        return accumulatedReward;
    }

    public abstract void learn(int totalSteps, int offlineTime);

    public abstract int step(int step, int offlineTime);

    public abstract Action selectAction(int offlineTime);

    public abstract void printPolicy();

    public void writeReward(int timeStep){
        System.out.println(timeStep + "\t" + accumulatedReward);
    }
}
