package com.amansoni.rl;

import java.util.Random;

/**
 * Created by Aman on 14/05/2016.
 */
public class Experiment {
    enum Algorithm {
        QLearning, RPSO, QBEA
    }

    static int seed = 3;
    Environment environment;
    LearningAlgorithm learningAlgorithm;

    public Experiment(int seed, int bias, Algorithm algorithm) {
        environment = new Environment(bias);
        switch (algorithm) {
            case QLearning:
                learningAlgorithm = new QLearning(environment, seed);
                break;
            case RPSO:
                learningAlgorithm = new RPSO(environment, seed);
                break;

        }
    }

    public static void main(String[] args) {
//        Experiment experiment = new Experiment(1, 100);
//        experiment.learningAlgorithm.learn(2000);
//        System.out.println(experiment.learningAlgorithm.getAccumulatedReward());
//        experiment.learningAlgorithm.printPolicy();

        createExperimentRun(30, 100, 2000, Algorithm.QLearning);
        createExperimentRun(30, 15, 2000, Algorithm.QLearning);

        createExperimentRun(30, 100, 2000, Algorithm.RPSO);
        createExperimentRun(30, 15, 2000, Algorithm.RPSO);

    }

    public static void createExperimentRun(int repeat, int bias, int steps, Algorithm algorithm) {
        Random random = new Random(seed);
        System.out.println("Running experiment with bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            rewards += createExperiment(bias, steps, random.nextInt(), algorithm);
//            rewards += createExperimentRL(bias, steps, i);
        }
        System.out.println("Averaged reward: " + rewards / (double) repeat);
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }

    public static double createExperiment(int bias, int steps, int seed, Algorithm algorithm) {
        Experiment experiment = new Experiment(seed, bias, algorithm);
        experiment.learningAlgorithm.learn(steps);
//        System.out.println(seed + "\t" + experiment.learningAlgorithm.getAccumulatedReward());
        //experiment.learningAlgorithm.printPolicy();
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

}