package com.amansoni;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of a very simple EA that maximises the current reward value. It is not aware of the
 *         time-linkage between states.
 */
public class EDOAlgorithm extends LearningAlgorithm {
    // set default strategy to FULL
    EvolutionaryAlgorithm.Strategy strategy = EvolutionaryAlgorithm.Strategy.Full;

    public EDOAlgorithm(Environment environment, int seed) {
        super(environment, seed);
    }

    public EDOAlgorithm(Environment environment, int seed, EvolutionaryAlgorithm.Strategy strategy) {
        this(environment, seed);
        this.strategy = strategy;
    }

    @Override
    public void learn(int totalSteps, int offlineTime) {
        for (int i = 0; i < totalSteps; i++) {
            // accumulate the reward
            accumulatedReward +=  step(i, offlineTime);
        }
    }

    @Override
    public int step(int step, int offlineTime) {
        // select an action
        Action action = selectAction(offlineTime);
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        return reward;
    }

    @Override
    public Action selectAction(int offlineTime) {
        return new EvolutionaryAlgorithm(environment, strategy, random, offlineTime, false).getAction();
    }

    @Override
    public void printPolicy() {
    }
}
