package com.amansoni;

import java.util.ArrayList;
import java.util.List;
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
        QLearning, RPSO, QBEA, Optimal, EDO, Random;
    }

    static int seed = 0;

    LearningAlgorithm learningAlgorithm;
    List<LearningAlgorithm> learningAlgorithms = new ArrayList<>();
    int steps;
    int repeat;
    int offlineTime;

    public Experiment(int seed, int bias, Algorithm algorithm) {
        double[] params = new double[]{.7, 3.};
        Environment environment = new Environment(bias, false);
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
            case Random:
                learningAlgorithm = new RandomAlgorithm(environment, seed);
                break;
        }
    }


    public Experiment(LearningAlgorithm learningAlgorithm, int steps, int repeat, int offlineTime) {
        this.learningAlgorithm = learningAlgorithm;
        this.steps = steps;
        this.repeat = repeat;
        this.offlineTime = offlineTime;
    }

    private double run() {
        Random random = new Random(seed);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            learningAlgorithm.reset(random.nextInt());
            rewards += this.runOnce();
        }
        rewards = rewards / repeat;
        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
        return rewards;
    }

    private int runOnce() {
        for (int i = 0; i < steps; i++) {
            learningAlgorithm.step(i, offlineTime);
            if (false)
                System.out.println("step:" + i + "\treward:" + learningAlgorithm.getAccumulatedReward());
        }
        return learningAlgorithm.getAccumulatedReward();
    }

    public static void main(String[] args) {
//        initialComparisons();
        compareEDO();
    }

    public static void compareEDO() {
        int steps = 1000;
        int repeat = 30;
        int bias = 100;

        Environment environment;
        LearningAlgorithm algorithm;
        Experiment experiment;
        environment = new Environment(bias);

        algorithm = new RandomAlgorithm(environment, seed);
        experiment = new Experiment(algorithm, steps, repeat, 0);
        System.out.println("Random Bias:	" + bias + "\t reward\t" + experiment.run());

//        compareEDOVariations(steps, repeat, 21, bias, EDOAlgorithm.Strategy.Full);

//        compareQBEAEDOVariations(steps, repeat, 21, bias, QBEA.Strategy.RandomThenBest);


        for (int i = 0; i <= 21; i++) {
//            System.out.println("Offline time\t" + i);
//            compareEDOVariations(steps, repeat, i, bias, EDOAlgorithm.Strategy.OnePlusOne);
//            compareEDOVariations(steps, repeat, i, bias, EDOAlgorithm.Strategy.RandomThenBest);
            compareQBEAEDOVariations(steps, repeat, i, bias, QBEA.Strategy.RandomThenBest);
//            compareEDOVariations(steps, repeat, i, 15);
        }
    }

    private static void compareEDOVariations(int steps, int repeat, int offlineTime, int bias, EDOAlgorithm.Strategy strategy) {
        Environment environment;
        LearningAlgorithm algorithm;
        Experiment experiment;
        environment = new Environment(bias);
        algorithm = new EDOAlgorithm(environment, seed, strategy);
        experiment = new Experiment(algorithm, steps, repeat, offlineTime);
        System.out.println("EDO:\t" + strategy.name() + "\tbias\t" + bias + "\t" + offlineTime + "\t reward\t" + experiment.run());

    }

    private static void compareQBEAEDOVariations(int steps, int repeat, int offlineTime, int bias, QBEA.Strategy strategy) {
        Environment environment;
        LearningAlgorithm algorithm;
        Experiment experiment;
        environment = new Environment(bias);
        algorithm = new QBEA(environment, seed, strategy);
        experiment = new Experiment(algorithm, steps, repeat, offlineTime);
        System.out.println("QBEA:\t" + strategy.name() + "\tbias\t" + bias + "\t" + offlineTime + "\t reward\t" + experiment.run());

    }


    public static void initialComparisons() {
        int interval = 1;
        int steps = 1000;
        int repeat = 30;
        int offlineTimeAllowed = 21;

        int bias = 100;
        compareAlgorithms(steps, repeat, bias, 0, 10000);
        compareAlgorithms(steps, repeat, bias, offlineTimeAllowed, 10000);

        bias = 15;
        compareAlgorithms(steps, repeat, bias, 0, 1000);
        compareAlgorithms(steps, repeat, bias, offlineTimeAllowed, 1000);

        steps = 20;
        bias = 100;
        compareSteps(steps, repeat, bias, interval, 0, 1);
        compareSteps(steps, repeat, bias, interval, offlineTimeAllowed, 1);

        bias = 15;
        compareSteps(steps, repeat, bias, interval, 0, 1);
        compareSteps(steps, repeat, bias, interval, offlineTimeAllowed, 1);
    }

    private static void compareSteps(int steps, int repeat, int bias, int interval, int offlineTime, int resultsMultiplier) {
        System.out.println("\nRunning experiment " + " bias: " + bias + " repeated " + steps + " averaged over " + repeat
                + " Offline time:" + offlineTime + " multiplier:" + resultsMultiplier);
        // optimal
        String s = "Optimal\t";
        for (int i = 0; i <= steps; i += interval) {
            s += createExperimentRun(1, bias, i, Algorithm.Optimal, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "EDO\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.EDO, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "QLearning\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.QLearning, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "QBEA\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.QBEA, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "Random\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.Random, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);
    }

    private static void compareAlgorithms(int steps, int repeat, int bias, int offlineTime, int resultsMultiplier) {
        System.out.println("\nRunning experiment " + " bias: " + bias + " steps:" + steps + " averaged over:" + repeat
                + " Offline time:" + offlineTime + " multiplier:" + resultsMultiplier);
        System.out.println("Optimal\t" + createExperimentRun(1, bias, steps, Algorithm.Optimal, offlineTime, resultsMultiplier));
        System.out.println("EDO\t" + createExperimentRun(repeat, bias, steps, Algorithm.EDO, offlineTime, resultsMultiplier));
        System.out.println("QLearning\t" + createExperimentRun(repeat, bias, steps, Algorithm.QLearning, offlineTime, resultsMultiplier));
        System.out.println("QBEA\t" + createExperimentRun(repeat, bias, steps, Algorithm.QBEA, offlineTime, resultsMultiplier));
        System.out.println("Random\t" + createExperimentRun(repeat, bias, steps, Algorithm.Random, offlineTime, resultsMultiplier));
    }

    public static String createExperimentRun(int repeat, int bias, int steps, Algorithm algorithm, int offlineTime, int resultsMultiplier) {
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
        Double ret = rewards / (double) (repeat * resultsMultiplier);
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
//        experiment.learningAlgorithm.printPolicy();
        return experiment.learningAlgorithm.getAccumulatedReward();
    }
}