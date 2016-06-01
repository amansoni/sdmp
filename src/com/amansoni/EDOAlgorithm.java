package com.amansoni;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of a very simple EA that maximises the current reward value. It is not aware of the
 *         time-linkage between states.
 */
public class EDOAlgorithm extends LearningAlgorithm {

    public EDOAlgorithm(Environment environment, int seed) {
        super(environment, seed);
    }

    @Override
    public void learn(int totalSteps) {
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
    public int step(int step) {
        // select an action
        Action action = selectAction();
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        // accumulate the reward
        accumulatedReward += reward;
        return reward;
    }

    @Override
    public Action selectAction() {
        state = environment.getState();
        int maxReward = Integer.MIN_VALUE;
        Action action = null;
        for (Action a : environment.getActions()) {
            int reward = environment.getReward(a);
            if (maxReward < reward) {
                action = a;
                maxReward = reward;
            }
        }
        return action;
    }

    @Override
    public void printPolicy() {
    }

}
