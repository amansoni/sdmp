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
    Environment environment;
    LearningAlgorithm learningAlgorithm;
    int steps;
    int offlineTime;
    int seed;

    public Experiment(LearningAlgorithm learningAlgorithm, int steps, int offlineTime) {
        this.learningAlgorithm = learningAlgorithm;
        this.steps = steps;
        this.offlineTime = offlineTime;
    }

    public Experiment(Environment.ChangeType changeType, int bias, LearningAlgorithm learningAlgorithm, int steps, int offlineTime, int seed) {
        this.environment = new Environment(bias, changeType);
        this.learningAlgorithm = learningAlgorithm;
        this.learningAlgorithm.reset(environment, seed);
        this.steps = steps;
        this.offlineTime = offlineTime;
        this.seed = seed;
    }
    public int run() {
        learningAlgorithm.learn(steps, offlineTime);
        return learningAlgorithm.getAccumulatedReward();
    }
}