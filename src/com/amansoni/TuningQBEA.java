package com.amansoni;

import java.util.ArrayList;
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
public class TuningQBEA {
    enum Algorithm {
        QLearning, RPSO, QBEA, Optimal, EDO
    }
    static boolean run15Only = true;
    static int seed = 1;
    Environment environment;
    LearningAlgorithm learningAlgorithm;
    static Random random;

    public TuningQBEA() {

    }

    public TuningQBEA(int seed, int bias, Algorithm algorithm, double[] params) {
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
        int steps = 1000;
        int repeat = 30;
        random = new Random(seed);
        System.out.println(new TuningQBEA().getTunedSettings(steps, repeat));
    }

    public Individual getTunedSettings(int steps, int repeat) {
        // for the fitness function
        double optimal100 = createExperimentRun(1, 100, steps, Algorithm.Optimal, null);
        double optimal15 = createExperimentRun(1, 15, steps, Algorithm.Optimal, null);
        System.out.println("Optimal\t100:" + optimal100 + "\t15:" + optimal15);
        int size = 6;
        ArrayList<Individual> population = initPopulation(random, size);
        Individual bestOfGeneration = null;
        Individual nextBestOfGeneration = null;
        for (int i = 0; i < 6; i++) {
            System.out.println("Generation:" + i + "\t population:" + population.size());
            long BEGIN = System.currentTimeMillis();
            for (Individual individual : population) {
                double fitness = individual.getFitness(steps, repeat, optimal100, optimal15);
                if (bestOfGeneration == null || fitness < bestOfGeneration.fitness) {
                    nextBestOfGeneration = bestOfGeneration;
                    bestOfGeneration = individual;
                }
            }
            population = reproduce(random, size, bestOfGeneration, nextBestOfGeneration);
            long END = System.currentTimeMillis();
            System.out.print("\tTime:" + (END - BEGIN) / 1000.0 + " sec.");
            System.out.println("\tBest:" + bestOfGeneration);
            System.out.println("\tNext:" + nextBestOfGeneration);
        }
        return bestOfGeneration;
    }

    private ArrayList<Individual> reproduce(Random random, int size, Individual bestOfGeneration, Individual nextBestOfGeneration) {
        ArrayList<Individual> population = initPopulation(random, 2); // maintain some diversity
        population.add(bestOfGeneration);
        population.add(nextBestOfGeneration);
        int mutuatecount = ((size - 2) / 2);
        while (population.size() < mutuatecount + 2) {
            population.add(mutate(random, bestOfGeneration));
        }
        while (population.size() < size) {
            population.add(mutate(random, bestOfGeneration));
        }
        return population;
    }

    private Individual mutate(Random random, Individual bestOfGeneration) {
        Individual offspring = new Individual(bestOfGeneration.getParams());
        // mutate discount factor
        int multiplier = random.nextInt(5);
        double check = random.nextDouble();
        if (check < 0.5) {
            double increase = random.nextDouble();
            if (increase > 0.5) {
                offspring.discountFactor += 0.05 * multiplier;
            } else
                offspring.discountFactor -= 0.05 * multiplier;
        }
//        // mutate prior
        check = random.nextDouble();
        if (check < 0.5) {
            double increase = random.nextDouble();
            if (increase > 0.5)
                offspring.priorConstant += multiplier;
            else
                offspring.priorConstant -= multiplier;
        }
//
//        if (check < 0.25) {
//            offspring.discountFactor += 0.05;
//            offspring.priorConstant--;
//        }
//        if (check > 0.25 && check < 0.5) {
//            offspring.discountFactor -= 0.05;
//            offspring.priorConstant++;
//        }
//        if (check > 0.5 && check < 0.75) {
//            offspring.discountFactor += 0.05;
//            offspring.priorConstant++;
//        }
//        if (check > 0.75) {
//            offspring.discountFactor -= 0.05;
//            offspring.priorConstant--;
//        }
        if (offspring.discountFactor < 0 || offspring.discountFactor >= 1)
            offspring.discountFactor = random.nextDouble();

        if (offspring.priorConstant <= 0)
            offspring.priorConstant = random.nextInt(50);

        return offspring;
    }

    private ArrayList<Individual> initPopulation(Random random, int size) {
        ArrayList<Individual> population = new ArrayList<>();
        while (population.size() < size) {
            population.add(new Individual(random.nextDouble(), (double) random.nextInt(20)));
        }
        return population;
    }

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
        TuningQBEA experiment = new TuningQBEA(seed, bias, algorithm, params);
        experiment.learningAlgorithm.learn(steps, 0);
        return experiment.learningAlgorithm.getAccumulatedReward();
    }

    class Individual {
        double discountFactor;
        double priorConstant;
        double fitness = Double.MIN_VALUE;

        public Individual(double discountFactor, double priorConstant) {
            this.discountFactor = discountFactor;
            this.priorConstant = priorConstant;
        }

        public Individual(double[] params) {
            this.discountFactor = params[0];
            this.priorConstant = params[1];
        }

        public String toString() {
            return "Discount Factor:" + discountFactor + "\tPrior:" + priorConstant + "\tFitness:" + fitness;
        }

        public double[] getParams() {
            return new double[]{discountFactor, priorConstant};
        }

        public double getFitness(int steps, int repeat, double optimal100, double optimal15) {
            if (fitness == Double.MIN_VALUE) {
                if (!run15Only) {
                    double fitness100 = createExperimentRun(repeat, 100, steps, Algorithm.QBEA, getParams());
                    double fitness15 = createExperimentRun(repeat, 15, steps, Algorithm.QBEA, getParams());
                    fitness = ((optimal100 - fitness100) / optimal100 + (optimal15 - fitness15) / optimal15);
                    System.out.println(this + "\t100:" + fitness100 + "\t15:" + fitness15);
                } else {
                    double fitness15 = createExperimentRun(repeat, 15, steps, Algorithm.QBEA, getParams());
                    fitness = (optimal15 - fitness15) / optimal15;
                    System.out.println(this + "\t15:" + fitness15);
                }
            } else
                System.out.println("(previous calc)\t" + this);
            return fitness;
        }
    }
}