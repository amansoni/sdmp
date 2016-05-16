package com.amansoni.rl;

import java.util.Random;

/**
 * Created by Aman on 14/05/2016.
 */
public class Experiment {
    Environment environment;
    LearningAlgorithm learningAlgorithm;

    public Experiment(int seed, int bias) {
        environment = new Environment(bias);
        learningAlgorithm = new LearningAlgorithm(environment, 0.7, seed);
    }

    public static void main(String[] args) {
        createExperimentRun(30, 100, 1000);
        createExperimentRun(30, 15, 1000);
    }

    public static void createExperimentRun(int repeat, int bias, int steps) {
        Random random = new Random(1);
        System.out.println("Running experiment with bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i=0; i< repeat; i++){
            rewards += createExperiment(bias, steps, random.nextInt());
        }
        System.out.println("Averaged reward: " + rewards/ (double)repeat);
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }

    public static double createExperiment(int bias, int steps, int seed) {
        Experiment experiment = new Experiment(seed, bias);
        experiment.learningAlgorithm.learn(steps);
        return experiment.learningAlgorithm.getAccumulatedReward();
    }
}