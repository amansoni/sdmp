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
public class TuningQLearning {
    enum Algorithm {
        QLearning, RPSO, QBEA, Optimal, EDO
    }

    static int seed = 0;
    Environment environment;
    LearningAlgorithm learningAlgorithm;

    public TuningQLearning(int seed, int bias, Algorithm algorithm, double[] params) {
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


//        compareAlgorithms(steps, repeat, 100);
//        compareAlgorithms(steps, repeat, 25);
//        compareAlgorithms(steps, repeat, 15);
//        compareAlgorithms(steps, repeat, 0);

        double maxReward = Double.MIN_VALUE;
        double best = 0.;
        double best100 = 0.;
        double best15 = 0.;
        double reward100 = 0.;
        double reward15 = 0.;
        double[] params = new double[]{0.7, 0.5};

        double optimal100 = createExperimentRun(1, 100, steps, Algorithm.Optimal, params);
        double optimal15 = createExperimentRun(1, 15, steps, Algorithm.Optimal, params);

        Algorithm algorithm = Algorithm.QBEA;
        for (int i = 0; i < 30; i++) {
            System.out.println("Testing:" + i);


            reward100 = createExperimentRun(repeat, 100, steps, algorithm, params);
            reward15 = createExperimentRun(repeat, 15, steps, algorithm, params);

//            double reward0 = createExperimentRun(repeat, 0, steps, Algorithm.QLearning);
            if (((optimal15 - reward15) / optimal15 + (optimal100 - reward100) / optimal100) > maxReward) {
                maxReward = ((optimal15 - reward15) / optimal15 + (optimal100 - reward100) / optimal100);
                if (algorithm == Algorithm.QBEA)
                    best = params[1];
                else
                    best = params[0];
            }
            if (reward100 > best100) {
                best100 = reward100;
                System.out.println("Better 100 :" + best100 + " discount factor:" + params[0]);
            }
            if (reward15 > best15) {
                best15 = reward15;
                System.out.println("Better 15 :" + best15 + " discount factor:" + params[0]);
            }
            if (algorithm == Algorithm.QBEA) {
                params[1] = i * 0.5;
                if (params[1] == 0)
                    params[1] = 1.;
            } else {
                params[0] = i * 0.5;
            }
        }
        System.out.println("Best param:" + best);
        System.out.println("Reward 100 :" + reward100);
        System.out.println("Reward 15 :" + reward15);
    }

//    private static void compareAlgorithms(int steps, int repeat, int bias) {
//        System.out.println("Running experiment " + " bias: " + bias + " repeated " + steps + " averaged over " + repeat);
//        String s = createExperimentRun(repeat, bias, steps, Algorithm.Optimal);
//        s += createExperimentRun(repeat, bias, steps, Algorithm.EDO);
//        s += createExperimentRun(repeat, bias, steps, Algorithm.QLearning);
//        s += createExperimentRun(repeat, bias, steps, Algorithm.QBEA);
//        System.out.println(s);
//    }

    public static double createExperimentRun(int repeat, int bias, int steps, Algorithm algorithm, double[] params) {
        Random random = new Random(seed);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
            rewards += createExperiment(bias, steps, random.nextInt(), algorithm, params);
        }
        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
        return rewards / (double) repeat;
    }

    public static double createExperiment(int bias, int steps, int seed, Algorithm algorithm, double[] params) {
        TuningQLearning experiment = new TuningQLearning(seed, bias, algorithm, params);
        experiment.learningAlgorithm.learn(steps);
//        System.out.println(seed + "\t" + experiment.learningAlgorithm.getAccumulatedReward());
//        experiment.learningAlgorithm.printPolicy();
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

}