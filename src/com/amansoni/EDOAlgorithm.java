package com.amansoni;

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

    public EDOAlgorithm(Environment environment, int seed) {
        super(environment, seed);
    }

    @Override
    public void learn(int totalSteps, int offlineTime) {
        for (int i = 0; i < totalSteps; i++) {
            step(i, offlineTime);
//            // select an action
//            Action action = selectAction(offlineTime);
//            // perform the action and get a reward
//            int reward = environment.takeAction(action);
//            // accumulate the reward
//            accumulatedReward += reward;
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
        int maxReward = Integer.MIN_VALUE;
        Action action = null;
        // no offline time, must select an action randomly
        if (offlineTime == 0) {
            action = environment.getActions()[random.nextInt(environment.getActions().length)];
        } else if (offlineTime >= environment.getActions().length) {
            for (Action a : environment.getActions()) {
                int reward = environment.getReward(a);
                if (maxReward < reward) {
                    action = a;
                    maxReward = reward;
                }
            }
        } else {
            // we need a strategy!
        }
        return action;
    }

    @Override
    public void printPolicy() {
    }

}
