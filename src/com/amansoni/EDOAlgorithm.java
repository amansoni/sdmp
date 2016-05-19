package com.amansoni;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of the Restart Particle Swarm Optimisation technique from Clerc, Maurice, and James Kennedy.
 *         "The particle swarm-explosion, stability, and convergence in a multidimensional complex space."
 *         Evolutionary Computation, IEEE Transactions on 6.1 (2002): 58-73.
 */
public class EDOAlgorithm extends LearningAlgorithm {
    int noOfActions = 21;
    int offset = 10;

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
