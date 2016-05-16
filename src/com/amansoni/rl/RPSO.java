package com.amansoni.rl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aman on 15/05/2016.
 */
public class RPSO extends LearningAlgorithm {
    final static boolean DEBUG = false;
    DecimalFormat df = new DecimalFormat("#.00");
    Environment environment;
    Random random;
    int noOfStates = 21;
    int noOfActions = 21;
    int stateOffset = 10;
    ArrayList<Individual> population;

    private static int SWARM_SIZE = 10;
    private static double ATTRACTION = 2.05;
    private static double CONSTRICTION_FACTOR = 0.729844;
    private static int VMAX = 20;
    private static int MAX_EVALUATION_COUNT = 100;
    Action action = null;

    public RPSO(Environment environment, int seed) {
        random = new Random(seed);
        this.environment = environment;
    }

    public void learn(int totalSteps) {
        state = environment.getState();
        for (int i = 0; i < totalSteps; i++) {
            // select an action
            Action action = selectAction();
            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // accumulate the reward
            accumulatedReward += reward;
        }
    }

    @Override
    public Action selectAction() {
        initPopulation();
        int evaluationCount=0;
        Individual bestOfGeneration;
        double bestFitness;
        if (action != null) {
            bestOfGeneration = new Individual(action.getValue());
        } else {
            bestOfGeneration = new Individual(10);
        }
        bestFitness = environment.getReward(bestOfGeneration.getAction());
        while (true){
            for (Individual i: population) {
                double fitness = environment.getReward(i.getAction());
                evaluationCount++;
                if (fitness >= bestFitness){
                    bestFitness = fitness;
                    bestOfGeneration = i;
                }
            }
            if (evaluationCount >= MAX_EVALUATION_COUNT)
                break;
        }
        action = bestOfGeneration.getAction();
        return action;
    }

    private void initPopulation() {
        population = new ArrayList<>();
        for (int i = 0; i < SWARM_SIZE; i++) {
            population.add(new Individual(random));
        }
    }

    public void printPolicy() {}

        class Individual {
        int action = 0;

        public Individual(int action) {this.action = action;}
        public Individual(Random random) {
            action = random.nextInt(noOfActions) - stateOffset;
        }

        public Action getAction() {
            return new Action(action);
        }
    }
}
