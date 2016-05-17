package com.amansoni;

import java.util.Random;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of the Restart Particle Swarm Optimisation technique from Clerc, Maurice, and James Kennedy.
 *         "The particle swarm-explosion, stability, and convergence in a multidimensional complex space."
 *         Evolutionary Computation, IEEE Transactions on 6.1 (2002): 58-73.
 */
public class RPSO extends LearningAlgorithm {
    final static boolean DEBUG = false;
    int noOfStates = 21;
    int noOfActions = 21;
    int offset = 10;
    Swarm swarm;

    private static int SWARM_SIZE = 10;
    private static int MAX_EVALUATIONS = 100;
    int evaluationCount = 0;

    public RPSO(Environment environment, int seed) {
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
        swarm = new Swarm();
        while (evaluationCount < MAX_EVALUATIONS) {
            for (Particle particle : swarm.getParticles()) {
                particle.move();
                // really only up to MAX_EVALUATIONS
                if (evaluationCount < MAX_EVALUATIONS)
                    break;
            }
        }
        return swarm.getFittest().getAction();
    }

    @Override
    public void printPolicy() {
    }

    public class Swarm {
        Particle[] particles;

        public Swarm() {
            particles = new Particle[SWARM_SIZE];
            for (int i = 0; i < SWARM_SIZE; i++) {
                particles[i] = new Particle(true);
            }
        }

        public Particle[] getParticles() {
            return particles;
        }

        public Particle getFittest() {
            Particle fittest = particles[0];
            for (int i = 1; i < SWARM_SIZE; i++) {
                if (fittest.getFitness() <= particles[i].getFitness()) {
                    fittest = particles[i];
                }
            }
            return fittest;
        }
    }

    public class Particle {
        int action = 0;
        double velocity;
        double fitness = Double.MIN_VALUE;

        public Particle(boolean init) {
            if (init) {
                action = random.nextInt(noOfActions) - offset;
            }
        }

        public Action getAction() {
            return new Action(action);
        }

        public double getFitness() {
            if (fitness == Double.MIN_VALUE) {
                fitness = environment.getReward(this.getAction());
                evaluationCount++;
            }
            return fitness;
        }

//        TODO finish the move method
        public void move() {
            // there is only a single dimension - peak center
            fitness = environment.getReward(this.getAction());
            evaluationCount++;
        }
    }
}
