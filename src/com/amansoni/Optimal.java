package com.amansoni;

/**
 * Created by Aman on 19/05/2016.
 */
public class Optimal extends LearningAlgorithm {

    protected Optimal(Environment environment, int seed) {
        super(environment, seed);
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
        state = new State(environment.getState().center);
        Action action = selectAction(offlineTime);
        // perform the action and get a reward
        int reward = environment.takeAction(action);
        state = new State(environment.getState().center);
        return reward;
    }

    @Override
    public Action selectAction(int offlineTime) {
        Action action;
        // always choose 5 when state = -5
        if (state.center == 5)
            action = new Action(5);
        else {
            if (environment.getBias() >= 15)
                action = new Action(0);
            else
                action = new Action(-5);
//            switch (environment.getBias()){
//                case 0 :
//                    action = new Action(-5);
//                    break;
//                case 15 :
//                    action = new Action(0);
//                    break;
//                case 100 :
//                    action = new Action(0);
//                    break;
//            }
        }
        return action;
    }

    @Override
    public void printPolicy() {

    }
}
