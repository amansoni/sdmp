package com.amansoni;

import java.util.Random;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of a very simple EA that maximises the current reward value. It is not aware of the
 *         time-linkage between states.
 */
public class EDOAlgorithm extends LearningAlgorithm {
    enum Strategy {
        OnePlusOne, Full, SizeOverDelta
    }

    static int noOfActions = 21;
    Strategy strategy = Strategy.Full;

    public EDOAlgorithm(Environment environment, int seed) {
        super(environment, seed);
    }

    public EDOAlgorithm(Environment environment, int seed, Strategy strategy) {
        this(environment, seed);
        this.strategy = strategy;
    }

    @Override
    public void learn(int totalSteps, int offlineTime) {
        for (int i = 0; i < totalSteps; i++) {
            step(i, offlineTime);
        }
    }

    @Override
    public int step(int step, int offlineTime) {
        // select an action
        Action action = selectAction(offlineTime);
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        // accumulate the reward
        accumulatedReward += reward;
        return reward;
    }

    @Override
    public Action selectAction(int offlineTime) {
        state = environment.getState();
        Action action = null;
        // no offline time, must select an action randomly
        if (offlineTime == 0) {
            action = environment.getActions()[random.nextInt(environment.getActions().length)];
        } else if (offlineTime >= environment.getActions().length || this.strategy == Strategy.Full) {
            int maxReward = Integer.MIN_VALUE;
            for (Action a : environment.getActions()) {
                int reward = environment.getReward(a);
                if (maxReward < reward) {
                    action = a;
                    maxReward = reward;
                }
            }
        } else {
            if (this.strategy == Strategy.OnePlusOne) {
                // we need a strategy!
                int numberOfGenerations = 10;
                int populationSize = 1;
                action = Evolution.getAction(environment, random, populationSize, numberOfGenerations);
            }
        }
        return action;
    }

    @Override
    public void printPolicy() {
    }

    private static class Evolution {
        public static int DEGREE_OF_CHANGE;

        public static Action getAction(Environment environment, Random random, int populationSize, int numberOfGenerations) {
            int bestFitness = Integer.MIN_VALUE;
            Action best = null;
            for (int i = 0; i < numberOfGenerations; i++) {
                Action[] population = initialisePopulation(random, populationSize);
                for (Action action : population) {
                    int fitness = environment.getReward(action);
                    if (fitness > bestFitness) {
                        bestFitness = fitness;
                        best = action;
                    }
                    population = mutate(random, population);
                }
            }
            return best;
        }

        private static Action[] mutate(Random random, Action[] population) {
            for (int i = 0; i < population.length; i++) {
                double direction = random.nextDouble();
                if (direction > 0.5) {
                    population[i] = new Action(population[i].getValue() + random.nextInt(DEGREE_OF_CHANGE));
                    if (population[i].getValue() > noOfActions)
                        population[i] = new Action(noOfActions);
                } else {
                    population[i] = new Action(population[i].getValue() - random.nextInt(DEGREE_OF_CHANGE));
                    if (population[i].getValue() < -noOfActions)
                        population[i] = new Action(-noOfActions);
                }
            }
            return population;
        }

        private static Action[] initialisePopulation(Random random, int populationSize) {
            Action[] population = new Action[populationSize];
            for (int i = 0; i < populationSize; i++) {
                population[i] = new Action(random.nextInt(noOfActions));
            }
            return population;
        }
    }
}
