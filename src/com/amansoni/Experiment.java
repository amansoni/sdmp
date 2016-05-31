package com.amansoni;

import java.util.Random;

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
    enum Algorithm {
        QLearning, RPSO, QBEA, Optimal, EDO
    }

    static int seed = 0;
    Environment environment;
    LearningAlgorithm learningAlgorithm;

    public Experiment(int seed, int bias, Algorithm algorithm) {
        double[] params = new double[]{.7, 2.};
        environment = new Environment(bias, false);
        switch (algorithm) {
            case QLearning:
                learningAlgorithm = new QLearning(environment, seed, params);
                break;
            case RPSO:
                learningAlgorithm = new RPSO(environment, seed);
                break;
            case QBEA:
                learningAlgorithm = new QBEA(environment, seed, params);
                break;
            case Optimal:
                learningAlgorithm = new Optimal(environment, seed);
                break;
            case EDO:
                learningAlgorithm = new EDOAlgorithm(environment, seed);
                break;
        }
    }

    public static void main(String[] args) {
//        Experiment experiment = new Experiment(1, 100, Algorithm.RPSO);
//        experiment.learningAlgorithm.learn(1000);
//        System.out.println(experiment.learningAlgorithm.getAccumulatedReward());
//        experiment.learningAlgorithm.printPolicy();

//        experiment = new Experiment(1, 100, Algorithm.QBEA);
//        experiment.learningAlgorithm.learn(1000);
//        System.out.println(experiment.learningAlgorithm.getAccumulatedReward());
//        experiment.learningAlgorithm.printPolicy();

        int steps = 1000;
        int repeat = 30;

        compareAlgorithms(steps, repeat, 100);
        compareAlgorithms(steps, repeat, 15);
//        compareAlgorithms(steps, repeat, 25);
//        compareAlgorithms(steps, repeat, 0);

    }

    private static void compareAlgorithms(int steps, int repeat, int bias) {
        System.out.println("Running experiment " + " bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        String s = createExperimentRun(repeat, bias, steps, Algorithm.Optimal);
        s += createExperimentRun(repeat, bias, steps, Algorithm.EDO);
        s += createExperimentRun(repeat, bias, steps, Algorithm.QLearning);
        s += createExperimentRun(repeat, bias, steps, Algorithm.QBEA);
        System.out.println(s);
    }

    public static String createExperimentRun(int repeat, int bias, int steps, Algorithm algorithm) {
        Random random = new Random(seed);
//        System.out.println("Running experiment " + algorithm.name() + " bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            rewards += createExperiment(bias, steps, random.nextInt(), algorithm);
//            rewards += createExperimentRL(bias, steps, i);
        }
//        System.out.println("Averaged reward:\t" + rewards / (double) repeat + "\t" + algorithm.name());
        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
        return algorithm.name() + "\t" + rewards / (double) repeat + "\t" + (END - BEGIN) / 1000.0 + "\n";
    }

    public static double createExperiment(int bias, int steps, int seed, Algorithm algorithm) {
        Experiment experiment = new Experiment(seed, bias, algorithm);
        experiment.learningAlgorithm.learn(steps);
//        System.out.println(seed + "\t" + experiment.learningAlgorithm.getAccumulatedReward());
//        experiment.learningAlgorithm.printPolicy();
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

}