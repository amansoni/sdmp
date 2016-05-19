package com.amansoni;

import java.text.DecimalFormat;

/**
 * @author Aman
 *         <p>
 *         An implement of Q Learning from Watkins, Christopher John Cornish Hellaby.
 *         Learning from delayed rewards. Diss. University of Cambridge, 1989.
 *         http://www.cs.rhul.ac.uk/~chrisw/thesis.html
 */
public class QLearning extends LearningAlgorithm {
    DecimalFormat df = new DecimalFormat("#.00");
    final static double discountFactor = 0.7;
    final static double epsilon = 0.1;
    int noOfStates = 21;
    int offset = 10;
    double[][] QValues;

    public QLearning(Environment environment, int seed) {
        super(environment, seed);
        initPolicy();
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
            State nextState = environment.getState();
            // update the learning policy
            updatePolicy(state, nextState, action, reward, i);
            state = nextState;

//            if (accumulatedReward <= 0)
//                printPolicy();
        }
    }

    public Action selectAction() {
        int noOfActions = environment.getActions().length;
        // check for random exploration
        Action action;
        if (random.nextDouble() <= epsilon) {
            action = environment.getActions()[random.nextInt(noOfActions)];
            if (DEBUG)
                System.out.print("Rand action:" + action.getValue());

        } else {
            action = environment.getActions()[getActionForMaxRewardForState(state)];
            if (DEBUG)
                System.out.print("Best action:" + action.getValue());
        }
        return action;
    }

    // Q(st, xt)  <-- (1 − alpha)Q(st, xt) + alpha(r + discountFactor( maxj Q(st+1, xj ));
    protected void updatePolicy(State state, State nextState, Action action, int reward, int timestep) {
        double learningRate = (200.0 / (300.0 + timestep));
        double currentQ = QValues[state.center + offset][action.getValue() + offset];
        double transitionQ = getMaxRewardForState(nextState);
        double updatedQValue =
                (1.0 - learningRate) * currentQ + learningRate * (reward + discountFactor * transitionQ);
        QValues[state.center + offset][action.getValue() + offset] = updatedQValue;
        if (DEBUG) {
            System.out.print(" time step:" + timestep);
            System.out.print(" learningRate:" + df.format(learningRate));
//            System.out.print(" discountFactor:" + discountFactor);
//            System.out.print(" action:" + action.getValue());
            System.out.print(" Change Q value:" + df.format(currentQ));
            System.out.print(" to " + df.format(updatedQValue));
            System.out.println("");
        }
    }

    protected double getMaxRewardForState(State state) {
        double bestValue = 0.;
        for (int j = 0; j < environment.getActions().length; j++) {
            if (QValues[state.center + offset][j] >= bestValue) {
                bestValue = QValues[state.center + offset][j];
            }
        }
        return bestValue;
    }

    private int getActionForMaxRewardForState(State state) {
        double bestValue = 0.;
        int bestAction = 0;
        for (int j = 0; j < environment.getActions().length; j++) {
            if (QValues[state.center + offset][j] >= bestValue) {
                bestAction = j;
                bestValue = QValues[state.center + offset][j];
            }
        }
        return bestAction;
    }

    private double[] getBestActionFromQValues(State state) {
        // select the best action from the learning policy
        double bestValue = 0;
        int bestAction = 0;
        for (int j = 0; j < environment.getActions().length; j++) {
            if (QValues[state.center + offset][j] >= bestValue) {
                bestValue = QValues[state.center + offset][j];
                bestAction = j;
            }
        }
        // select a random action to start
        if (false && bestValue == 0.){
            bestAction = random.nextInt(noOfStates);
        }
        return new double[]{bestAction, bestValue};
    }

    void initPolicy() {
        int noOfActions = environment.getActions().length;
        QValues = new double[noOfStates][noOfActions];
        for (int i = 0; i < noOfStates; i++) {
            for (int j = 0; j < noOfActions; j++) {
                QValues[i][j] = 0;
            }
        }
    }

    public void printPolicy() {
        int noOfActions = environment.getActions().length;
        System.out.print("  \t");
        for (int i = 0; i < noOfStates; i++) {
            System.out.print((i - offset) + "  \t");
        }
        System.out.println("");
        for (int i = 0; i < noOfStates; i++) {
            if ((i - offset) == 5 || (i - offset) == -5) {
                System.out.print((i - offset) + "\t");
                for (int j = 0; j < noOfActions; j++) {
                    System.out.print(df.format(QValues[i][j]) + "\t");
                }
                System.out.println("");
            }
        }
    }

}
