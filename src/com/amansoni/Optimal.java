package com.amansoni;

/**
 * Created by Aman on 19/05/2016.
 */
public class Optimal extends LearningAlgorithm {

    protected Optimal(Environment environment, int seed) {
        super(environment, seed);
    }

    @Override
    public void learn(int totalSteps) {
        state = environment.getState();
        for (int i = 0; i < totalSteps; i++) {
            // select an action
            Action action = selectAction();
            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // accumulate the reward
            accumulatedReward += reward;
            State nextState = environment.getState();
            // update the learning policy
            state = nextState;
        }

    }

    @Override
    public Action selectAction() {
        Action action;
        if (state.center == 5)
            action = new Action(5);
        else {
            if (environment.getBias() > 15)
                action = new Action(0);
            else
                action = new Action(0);
//                action = new Action(-5);
        }
        return action;
    }

    @Override
    public void printPolicy() {

    }
}
