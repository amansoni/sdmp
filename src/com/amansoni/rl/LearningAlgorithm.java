package com.amansoni.rl;

import java.text.DecimalFormat;
import java.util.Random;

/**
 * Created by Aman on 14/05/2016.
 */
public class LearningAlgorithm {
    final static boolean DEBUG = false;
    final static boolean WRITE_FILE = true;
    DecimalFormat df = new DecimalFormat("#.00");
    final static double epsilon = 0.1;
    int noOfStates = 21;
    int stateOffset = 10;
    double[][] QValues;
    Environment environment;
    Random random;
    int accumulatedReward= 0;
    double discountFactor=0;
    State state;


    public LearningAlgorithm (Environment environment, double discountFactor, int seed){
        random = new Random(seed);
        this.environment = environment;
        this.discountFactor = discountFactor;
        initPolicy();
    }

    public void learn(int totalSteps){
        for(int i=0; i< totalSteps; i++){
            // select an action
            Action action = selectAction();
            // perform the action and get a reward
            int reward = environment.takeAction(action);
            // observe the state
            state = environment.getState();
            // update the learning policy
            updatePolicy(state, action, reward, i);
            // accumulate the reward
            accumulatedReward += reward;
        }
    }

    public double getAccumulatedReward(){ return  accumulatedReward;}

    private Action selectAction() {
        int noOfActions = environment.getActions().length;
        // check for random exploration
        Action action;
        if (random.nextDouble() <= epsilon){
            action = environment.getActions()[random.nextInt(noOfActions)];
            if (DEBUG)
                System.out.print("Select random action:" + action.getValue());

        } else {
            action = environment.getActions()[getActionForMaxRewardForCurrentState()];
            if (DEBUG)
                System.out.print("Select best action:" + action.getValue());
        }
        return action;
    }

    // Q(st, xt)  <-- (1 − alpha)Q(st, xt) + alpha(r + discountFactor( maxj Q(st+1, xj ));
    private void updatePolicy(State state, Action action, int reward, int timestep) {
        double learningRate =  (200.0 / (300.0 + timestep));
        double currentQ = QValues[state.center + stateOffset][action.getValue() + stateOffset];
        double transitionQ = getMaxRewardForCurrentState();
        if (DEBUG) {
            System.out.print(" learningRate:" + df.format(learningRate));
            System.out.print(" timestep:" + timestep);
            System.out.print(" discountFactor:" + discountFactor);
            System.out.print(" action:" + action.getValue());
            System.out.print(" Updated Q value:" + df.format(currentQ));
        }
        double updatedQValue=
                (1-learningRate) * currentQ + learningRate * (reward + discountFactor * transitionQ);
        QValues[state.center + stateOffset][action.getValue() + stateOffset] = updatedQValue;
        if (DEBUG){
            System.out.print(" to " + df.format(updatedQValue));
            System.out.println("");
        }
    }

    private double getMaxRewardForCurrentState() {
        return  getBestActionFromQValues()[1];
    }

    private int getActionForMaxRewardForCurrentState() {
        return  (int) getBestActionFromQValues()[0];
    }

    private double[] getBestActionFromQValues() {
        // select the best action from the learning policy
        double bestValue = 0;
        int bestAction = 0;
        for(int j=0; j< environment.getActions().length; j++){
            if (QValues[state.center + stateOffset][j] >= bestValue){
                bestValue = QValues[state.center + stateOffset][j];
                bestAction = j;
            }
        }
        return new double[] {bestAction, bestValue};
    }

    void initPolicy(){
        int noOfActions = environment.getActions().length;
        QValues = new double[noOfStates][noOfActions];
        for(int i=0; i< noOfStates; i++){
            for(int j=0; j< noOfActions; j++){
                QValues[i][j] = 0;
            }
        }
    }

    void printPolicy(){
        int noOfActions = environment.getActions().length;
        for(int i=0; i< noOfStates; i++){
            System.out.print((i - stateOffset)  + "\t");
            for(int j=0; j< noOfActions; j++){
                System.out.print(df.format(QValues[i][j]) + "\t");
            }
            System.out.println("");
        }
    }

}
