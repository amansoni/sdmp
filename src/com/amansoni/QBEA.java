package com.amansoni;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * @author Aman
 *         Implemented from Fu, Haobo, Peter R. Lewis, and Xin Yao.
 *         "A Q-learning Based Evolutionary Algorithm for Sequential Decision Making Problems."
 */
public class QBEA extends QLearning {
    double priorA = 3.;
    int seed;
    Map<State, StateTransition> map = new TreeMap<>();
    static int noOfActions = 10;



    enum Strategy {
        OnePlusOne, Full, SizeOverDelta, RandomThenBest;
    }
    Strategy strategy = Strategy.Full;

    public QBEA(Environment environment, int seed) {
        super(environment, seed);
        initMap();
    }


    public QBEA(Environment environment, int seed, Strategy strategy) {
        this(environment, seed);
        this.strategy = strategy;
    }

    public QBEA(Environment environment, int seed, double[] params) {
        this(environment, seed);
        discountFactor = params[0];
        priorA = params[1];
    }

    private void initMap() {
        // init the map with all possible
        for (int i = 0; i < 21; i++) {
            for (int j = 0; j < 21; j++) {
                map.put(new State(i - offset), new StateTransition(new State(i - offset), priorA));
            }
        }
        // Iterate over them
//        for (Map.Entry<State, StateTransition> entry : map.entrySet()) {
//            System.out.println(entry.getKey() + " => " + entry.getValue());
//        }
    }

    public void learn(int totalSteps, int offlineTime) {
        state = new State(environment.getState().center);
        for (int i = 0; i < totalSteps; i++) {
            // employ ea to search on reward space
            searchRewardFunction(i, offlineTime);
            // select an action
            Action action = new Action(getActionForMaxRewardForState(state) - offset);
            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // accumulate the reward
            accumulatedReward += reward;
            State nextState = new State(environment.getState().center);
            // update the learning policy
            super.updatePolicy(state, nextState, action, reward, i);
            updateStateTransition(state, nextState, action);
            state = new State(environment.getState().center);
        }
    }

    @Override
    public int step(int step, int offlineTime) {
        state = new State(environment.getState().center);
        // employ ea to search on reward space
        searchRewardFunction(step, offlineTime);
        // select an action
        Action action = new Action(getActionForMaxRewardForState(state) - offset);
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        // accumulate the reward
        accumulatedReward += reward;
        State nextState = new State(environment.getState().center);
        // update the learning policy
        super.updatePolicy(state, nextState, action, reward, step);
        updateStateTransition(state, nextState, action);
        state = new State(environment.getState().center);
        return reward;
    }

    private void updateStateTransition(State state, State nextState, Action action) {
        StateTransition stateTransition = map.get(state);
        stateTransition.updateValues(action, nextState);
    }

    private State estimateNextState(State state, Action evalAction) {
        StateTransition stateTransition = map.get(state);
        return stateTransition.getProbableState(evalAction);
    }

    private void searchRewardFunction(int timeStep, int offlineTime) {
//        EDOAlgorithm edoAlgorithm = new EDOAlgorithm(environment, seed);
        double learningRate = (200.0 / (300.0 + timeStep));
        State currentState = new State(environment.getState().center);
        double discountFactor = 0.7;
        // if no offline time then cannot perform any offline evaluations
        if (offlineTime == 0) {

        } else if (offlineTime >= environment.getActions().length) {
            for (Action evalAction : environment.getActions()) {
                State probableState = estimateNextState(state, evalAction);
                int reward = environment.getReward(evalAction, probableState, evalAction);
                updatePolicy(learningRate, currentState, probableState, evalAction, reward, discountFactor);
            }
        } else {
            Action[] actions = null;
            if (this.strategy == Strategy.OnePlusOne) {
                actions = new EvolutionaryAlgorithm().getActions(environment, random, 1, offlineTime);
            } else if (this.strategy == Strategy.RandomThenBest) {
                actions = new EvolutionaryAlgorithm().getActions(environment, random, offlineTime, 1);
            }
            // we need a strategy
            for (Action evalAction : actions) {
                State probableState = estimateNextState(state, evalAction);
                int reward = environment.getReward(evalAction, probableState, evalAction);
                updatePolicy(learningRate, currentState, probableState, evalAction, reward, discountFactor);
            }
        }
    }

    /**
     * Update the Q-values based on probable states for the EA search
     * Q(st, xi)   (1 − alpha)Q(st, xi) + alpha(ft(st, xi) + upsilon *  maxj Q(ˆs, xj));
     *
     * @param state
     * @param nextState
     * @param action
     */

    private void updatePolicy(double learningRate, State state, State nextState, Action action, int reward, double discountFactor) {
        double currentQ = QValues[state.center + offset][action.getValue() + offset];
        double transitionQ = getBestQForState(nextState);
        double updatedQValue =
                (1.0 - learningRate) * currentQ + learningRate * (reward + discountFactor * transitionQ);
        QValues[state.center + offset][action.getValue() + offset] = updatedQValue;
        if (DEBUG) {
            System.out.print(" learningRate:" + df.format(learningRate));
            System.out.print(" state:" + state.center);
            System.out.print(" action:" + action.getValue());
            System.out.print(" next state:" + nextState.center);
            System.out.print(" Change Q value:" + df.format(currentQ));
            System.out.print(" to " + df.format(updatedQValue));
            System.out.println("");
        }
    }


    private class EvolutionaryAlgorithm {
        public int DEGREE_OF_CHANGE = 5;

        public Action[] getActions(Environment environment, Random random, int populationSize, int numberOfGenerations) {
            int bestFitness = Integer.MIN_VALUE;
            Action[] best = new Action[populationSize * numberOfGenerations];
            int index = 0;
            for (int i = 0; i < numberOfGenerations; i++) {
                Action[] population = initialisePopulation(random, populationSize);
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

        private Action[] initialisePopulation(Random random, int populationSize) {
            Action[] population = new Action[populationSize];
            for (int i = 0; i < populationSize; i++) {
                population[i] = environment.getActions()[random.nextInt(environment.getActions().length)];
            }
            return population;
        }
    }
}