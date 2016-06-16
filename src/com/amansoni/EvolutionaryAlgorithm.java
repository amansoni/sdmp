package com.amansoni;

import java.util.Random;

/**
 * Created by Aman on 13/06/2016.
 */
public class EvolutionaryAlgorithm {

    public enum Strategy {
        OnePlusOne, Full, SizeOverDelta, RandomThenBest, RandomNoRepeats, IndependantRandom;
    }

    private static int DEGREE_OF_CHANGE = 5;
    private static int NO_OF_ACTIONS = 10;
    Environment environment;
    Strategy strategy;
    Random random;
    int offlineTime;
    int populationSize;
    int numberOfGenerations;
    private boolean doRandomSelection = false;
    private boolean allowRepeats = false;

    public EvolutionaryAlgorithm(Environment environment, Strategy strategy, Random random, int offlineTime) {
        this.environment = environment;
        this.strategy = strategy;
        this.random = random;
        this.offlineTime = offlineTime;
        init();
    }

    private void init() {
        // can only do 1 fitness evaluation for any time step
        if (offlineTime <= 1) {
            doRandomSelection = true;
        }
        switch (strategy) {
            case OnePlusOne:
                populationSize = 1;
                numberOfGenerations = offlineTime;
                break;
            case RandomThenBest:
                populationSize = offlineTime;
                numberOfGenerations = 1;
                break;
            case Full:
                populationSize = environment.getActions().length;
                numberOfGenerations = 1;
        }
    }

    // return a single action only
    public Action getAction() {
        Action best = null;
        // can only select a single action
        if (doRandomSelection) {
            best = environment.getActions()[random.nextInt(environment.getActions().length)];
        } else {
            int bestFitness = Integer.MIN_VALUE;
            for (int i = 0; i < numberOfGenerations; i++) {
                Action[] population = initialisePopulation(environment, random, populationSize);
                for (Action action : population) {
                    int fitness = environment.getReward(action);
                    if (fitness > bestFitness) {
                        bestFitness = fitness;
                        best = action;
                    }
                    population = mutate(random, population);
                }
            }
        }
        return best;
    }

    public Action[] getActions() {
        int bestFitness = Integer.MIN_VALUE;
        Action[] best = new Action[populationSize * numberOfGenerations];
        int index = 0;
        for (int i = 0; i < numberOfGenerations; i++) {
            Action[] population = initialisePopulation(environment, random, populationSize);
            for (Action action : population) {
                int fitness = environment.getReward(action);
                best[index++] = action;
                if (fitness > bestFitness) {
                    bestFitness = fitness;
                }
                population = mutate(random, population);
            }
        }
        return best;
    }

    private Action[] mutate(Random random, Action[] population) {
        for (int i = 0; i < population.length; i++) {
            double direction = random.nextDouble();
            if (direction > 0.5) {
                population[i] = new Action(population[i].getValue() + random.nextInt(DEGREE_OF_CHANGE));
                if (population[i].getValue() > NO_OF_ACTIONS)
                    population[i] = new Action(NO_OF_ACTIONS);
            } else {
                population[i] = new Action(population[i].getValue() - random.nextInt(DEGREE_OF_CHANGE));
                if (population[i].getValue() < -NO_OF_ACTIONS)
                    population[i] = new Action(-NO_OF_ACTIONS);
            }
        }
        return population;
    }

    private Action[] initialisePopulation(Environment environment, Random random, int populationSize) {
        Action[] population = new Action[populationSize];
        for (int i = 0; i < populationSize; i++) {
            if (strategy == Strategy.Full || populationSize >= environment.getActions().length){
                population[i] = environment.getActions()[i];
            } else {
                population[i] = environment.getActions()[random.nextInt(environment.getActions().length)];
            }
        }
        return population;
    }
}
