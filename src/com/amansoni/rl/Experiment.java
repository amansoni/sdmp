package com.amansoni.rl;

import java.util.Random;

/**
 * Created by Aman on 14/05/2016.
 */
public class Experiment {
    static int seed = 5;
    Environment environment;
    LearningAlgorithm learningAlgorithm;

    public Experiment(int seed, int bias) {
        environment = new Environment(bias);
        learningAlgorithm = new LearningAlgorithm(environment, 0.7, seed);
    }

    public static void main(String[] args) {
        createExperimentRunRL(30, 100, 1000);
        createExperimentRunRL(30, 15, 1000);

//        createExperimentRunEA(30, 100, 2000);
//        createExperimentRunEA(30, 15, 2000);

    }

    public static void createExperimentRunRL(int repeat, int bias, int steps) {
        Random random = new Random(seed);
        System.out.println("Running experiment with bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            rewards += createExperimentRL(bias, steps, random.nextInt());
        }
        System.out.println("Averaged reward: " + rewards / (double) repeat);
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }

    public static double createExperimentRL(int bias, int steps, int seed) {
        Experiment experiment = new Experiment(seed, bias);
        experiment.learningAlgorithm.learn(steps);
        //experiment.learningAlgorithm.printPolicy();
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

    public static void createExperimentRunEA(int repeat, int bias, int steps) {
        Random random = new Random(seed);
        System.out.println("Running experiment with bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            rewards += createExperimentEA(bias, steps, random.nextInt());
        }
        System.out.println("Averaged reward: " + rewards / (double) repeat);
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }

    public static double createExperimentEA(int bias, int steps, int seed) {
        Experiment experiment = new Experiment(seed, bias);
        experiment.learningAlgorithm.learn(steps);
        //experiment.learningAlgorithm.learn(steps, seed, bias);
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

}