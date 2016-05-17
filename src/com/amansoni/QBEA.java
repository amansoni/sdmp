package com.amansoni;

/**
 * @author Aman
 * Fu, Haobo, Peter R. Lewis, and Xin Yao. "A Q-learning Based Evolutionary Algorithm for Sequential Decision Making Problems."
 */
public class QBEA extends QLearning {
    int seed;

    public QBEA(Environment environment, int seed) {
        super(environment, seed);
    }

    public void learn(int totalSteps) {
        state = environment.getState();
        for (int i = 0; i < totalSteps; i++) {
            // employ ea to search on reward space
            searchRewardFunction(i);
            // select an action
            Action action = selectAction();
            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // accumulate the reward
            accumulatedReward += reward;
            nextState = environment.getState();
            // update the learning policy
            super.updatePolicy(state, nextState, action, reward, i);
            state = nextState;
        }
    }

    private void searchRewardFunction(int i) {
        RPSO rpso = new RPSO(environment, seed);
        Action evalAction = rpso.selectAction();
        State probableState = estimateNextState(state);
        updatePolicy(state, probableState, evalAction, i);
    }

    private State estimateNextState(State state) {
        return state;
    }

    /**
     * Update the Q-values based on probable states for the EA search
     * Q(st, xi)   (1 − alpha)Q(st, xi) + alpha(ft(st, xi) + upsilon *  maxj Q(ˆs, xj));
     * @param state
     * @param nextState
     * @param action
     * @param timestep
     */
    private void updatePolicy(State state, State nextState, Action action, int timestep) {
        int reward = environment.getReward(action);
        double learningRate = (200.0 / (300.0 + timestep));
        double currentQ = QValues[state.center + offset][action.getValue() + offset];
        double transitionQ = getMaxRewardForState(nextState);
        if (DEBUG) {
            System.out.print(" time step:" + timestep);
            System.out.print(" learningRate:" + df.format(learningRate));
            System.out.print(" Change Q value:" + df.format(currentQ));
        }
        double updatedQValue =
                (1.0 - learningRate) * currentQ + learningRate * (reward + discountFactor * transitionQ);
        QValues[this.state.center + offset][action.getValue() + offset] = updatedQValue;
        if (DEBUG) {
            System.out.print(" to " + df.format(updatedQValue));
            System.out.println("");
        }
    }


}
