package com.amansoni;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of a very simple EA that maximises the current reward value. It is not aware of the
 *         time-linkage between states.
 */
public class RandomAlgorithm extends LearningAlgorithm {

    public RandomAlgorithm(Environment environment, int seed) {
        super(environment, seed);
    }

    @Override
    public void learn(int totalSteps, int offlineTime) {
        for (int i = 0; i < totalSteps; i++) {
            // select an action
            Action action = selectAction(offlineTime);
            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // accumulate the reward
            accumulatedReward += reward;
        }
    }

    @Override
    public int step(int step, int offlineTime) {
        // select an action
        Action action = environment.getActions()[random.nextInt(environment.getActions().length)];
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        // accumulate the reward
        accumulatedReward += reward;
        return reward;
    }

    @Override
    public Action selectAction(int offlineTime) {
        return  environment.getActions()[random.nextInt(environment.getActions().length)];
    }

    @Override
    public void printPolicy() {
    }

}
