package com.amansoni;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created by Aman on 13/06/2016.
 */
public class EvolutionaryAlgorithm {

    public enum Strategy {
        OnePlusOne, Full, SizeOverDelta, RandomThenBest, RandomNoRepeats, IndependantRandom;
    }

    private static int DEGREE_OF_CHANGE = 5;
    private int noOfActions;
    Environment environment;
    Strategy strategy;
    Random random;
    int offlineTime;
    int populationSize;
    int numberOfGenerations;
    private boolean doRandomSelection = false;
    private boolean allowRepeats = true;

    public EvolutionaryAlgorithm(Environment environment, Strategy strategy, Random random, int offlineTime) {
        this.environment = environment;
        this.strategy = strategy;
        this.random = random;
        this.offlineTime = offlineTime;
        noOfActions = 10;
        init();
    }

    public EvolutionaryAlgorithm(Environment environment, Strategy strategy, Random random, int offlineTime, boolean allowRepeats) {
        this(environment, strategy, random, offlineTime);
        this.allowRepeats = allowRepeats;
    }

    public EvolutionaryAlgorithm(Environment environment, Strategy strategy, Random random, int offlineTime, boolean allowRepeats, int noOfActions) {
        this(environment, strategy, random, offlineTime);
        this.allowRepeats = allowRepeats;
        this.noOfActions = noOfActions;
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
                doRandomSelection = false;
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
//                System.out.println(action.getValue() + "\t" + fitness);
                best[index++] = action;
                if (fitness > bestFitness) {
                    bestFitness = fitness;
                }
                population = mutate(random, population);
            }
        }
        return best;
    }

    private Action[] initialisePopulation(Environment environment, Random random, int populationSize) {
        if (strategy == Strategy.Full || populationSize >= environment.getActions().length) {
            return environment.getActions();
        }
        Action[] population = new Action[populationSize];
        for (int i = 0; i < populationSize; i++) {
            population[i] = addIndividual(population);
        }
        return population;
    }

    private Action addIndividual(Action[] population) {
        if (allowRepeats) {
            return environment.getActions()[random.nextInt(environment.getActions().length)];
        } else {
            return getNonRepeatedAction(population);
        }
    }

    private Action[] mutate(Random random, Action[] population) {
        // If the strategy is full then there's no need to mutate, we use the entire population
        if (strategy == Strategy.Full || populationSize >= environment.getActions().length) {
            population = environment.getActions();
        } else {
            // TODO use getNonRepeatedAction
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
        }
        return population;
    }

    private Action getNonRepeatedAction(Action[] population) {
        ArrayList<Action> complete = new ArrayList<>(Arrays.asList(environment.getActions()));
        for (Action a : population) {
            if (a != null) {
                complete.remove(a);
            }
        }
        return complete.get(random.nextInt(complete.size()));
    }
}
