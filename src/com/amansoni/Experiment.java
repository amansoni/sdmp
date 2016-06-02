package com.amansoni;

import java.util.Map;
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
        double[] params = new double[]{.7, 3.};
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

        int interval = 1;
        int bias = 15;

        int steps = 1000;
        int repeat = 30;

        compareAlgorithms(steps, repeat, 100, 0);
        compareAlgorithms(steps, repeat, 100, 1);
        compareAlgorithms(steps, repeat, 100, 3);
        compareAlgorithms(steps, repeat, 100, 5);

        compareAlgorithms(steps, repeat, 15, 0);
        compareAlgorithms(steps, repeat, 15, 1);
        compareAlgorithms(steps, repeat, 15, 3);
        compareAlgorithms(steps, repeat, 15, 5);


//        compareSteps(steps, repeat, bias, interval, 0);
//        compareSteps(steps, repeat, bias, interval, 1);
//        compareSteps(steps, repeat, bias, interval, 3);
//        compareSteps(steps, repeat, bias, interval, 5);

//        compareSteps(steps, repeat, 15, interval, 0);

//        Experiment experiment = getResults(repeat, 100, steps, Algorithm.EDO, 0);
//        // Iterate over them
//        for (Map.Entry<Integer, Integer> entry : experiment.learningAlgorithm.rewards.entrySet()) {
////            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }
//
//        experiment = getResults(repeat, 100, steps, Algorithm.EDO, 5);
//        // Iterate over them
//        for (Map.Entry<Integer, Integer> entry : experiment.learningAlgorithm.rewards.entrySet()) {
//        }

//        System.out.println(createExperimentRun(repeat, 100, steps, Algorithm.EDO, 0));
//        System.out.println(createExperimentRun(repeat, 100, steps, Algorithm.EDO, 5));

//        compareSteps(steps, repeat, 100, interval, 1);
//        compareSteps(steps, repeat, 15, interval, 1);

//            compareAlgorithms(steps, repeat, 100);
//            compareAlgorithms(steps, repeat, 15);

//        compareAlgorithms(steps, repeat, 25);
//        compareAlgorithms(steps, repeat, 0);

    }

    private static void compareSteps(int steps, int repeat, int bias, int interval, int offlineTime) {
        System.out.println("Running experiment " + " bias: " + bias + " repeated " + steps + " averaged over " + repeat
                + " Offline time:" + offlineTime);
        // optimal
        String s = "Optimal\t";
        for (int i = 0; i <= steps; i += interval) {
            s += createExperimentRun(1, bias, i, Algorithm.Optimal, offlineTime) + "\t";
        }
        System.out.println(s);

        s = "EDO\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.EDO, offlineTime) + "\t";
        }
        System.out.println(s);

        s = "QLearning\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.QLearning, offlineTime) + "\t";
        }
        System.out.println(s);

        s = "QBEA\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.QBEA, offlineTime) + "\t";
        }
        System.out.println(s);

//        String s = createExperimentRun(1, bias, steps, Algorithm.Optimal);
//        s += createExperimentRun(repeat, bias, steps, Algorithm.EDO);
//        s += createExperimentRun(repeat, bias, steps, Algorithm.QLearning);
//        s += createExperimentRun(repeat, bias, steps, Algorithm.QBEA);
    }

    private static void compareAlgorithms(int steps, int repeat, int bias, int offlineTime) {
        System.out.println("Running experiment " + " bias: " + bias + " steps:" + steps + " averaged over:" + repeat
                + " Offline time:" + offlineTime);
        System.out.println("Optimal\t" + createExperimentRun(1, bias, steps, Algorithm.Optimal, offlineTime));
        System.out.println("EDO\t" + createExperimentRun(repeat, bias, steps, Algorithm.EDO, offlineTime));
        System.out.println("QLearning\t" + createExperimentRun(repeat, bias, steps, Algorithm.QLearning, offlineTime));
        System.out.println("QBEA\t" + createExperimentRun(repeat, bias, steps, Algorithm.QBEA, offlineTime));
    }

    public static String createExperimentRun(int repeat, int bias, int steps, Algorithm algorithm, int offlineTime) {
        Random random = new Random(seed);
//        System.out.println("Running experiment " + algorithm.name() + " bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            rewards += createExperiment(bias, steps, random.nextInt(), algorithm, offlineTime);
//            rewards += createExperimentRL(bias, steps, i);
        }
//        System.out.println("Averaged reward:\t" + rewards / (double) repeat + "\t" + algorithm.name());
        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
//        return algorithm.name() + "\t" + rewards / (double) (repeat) + "\t" + (END - BEGIN) / 1000.0 + "\n";
        Double ret = rewards / (double) (repeat * 100);
//        System.out.println(rewards  + "\t" + ret.toString());
//        System.out.println((rewards / 100) / (double) (repeat));
        return ret.toString();
    }

    public static double createExperiment(int bias, int steps, int seed, Algorithm algorithm, int offlineTime) {
        Experiment experiment = new Experiment(seed, bias, algorithm);
//        for (int i = 0; i < steps; i++) {
//            experiment.learningAlgorithm.step(i, offlineTime);
//        }
        experiment.learningAlgorithm.learn(steps, offlineTime);
//        System.out.println(seed + "\t" + experiment.learningAlgorithm.getAccumulatedReward());
//        experiment.learningAlgorithm.printPolicy();
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

    public static Experiment getResults(int bias, int steps, int seed, Algorithm algorithm, int offlineTime) {
        Experiment experiment = new Experiment(seed, bias, algorithm);
        experiment.learningAlgorithm.learn(steps, offlineTime);
        return experiment;
    }

}