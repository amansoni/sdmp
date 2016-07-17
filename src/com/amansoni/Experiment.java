package com.amansoni;

/**
 * @author Aman
 *         <p>
 *         Experiments for algorithms based on work in the following papers.
 *         Fu, Haobo, et al. "What are dynamic optimization problems?." Evolutionary Computation (CEC), 2014 IEEE Congress on. IEEE, 2014.
 *         Fu, Haobo, Peter R. Lewis, and Xin Yao. "A Q-learning Based Evolutionary Algorithm for Sequential Decision Making Problems."
 *         <p>
 *         Runs all required experiments averaged over 30 runs and reports the accumulated rewards for CPMB 1 & 2.
 */
public class Experiment {
    LearningAlgorithm learningAlgorithm;
    int steps;
    int offlineTime;

    public Experiment(LearningAlgorithm learningAlgorithm, int steps, int offlineTime) {
        this.learningAlgorithm = learningAlgorithm;
        this.steps = steps;
        this.offlineTime = offlineTime;
    }

    public int run() {
        learningAlgorithm.learn(steps, offlineTime);
        return learningAlgorithm.getAccumulatedReward();
    }
}