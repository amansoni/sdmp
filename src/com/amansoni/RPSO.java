package com.amansoni;

/**
 * @author Aman Soni
 *         <p>
 *         Implementation of the Restart Particle Swarm Optimisation technique from Clerc, Maurice, and James Kennedy.
 *         "The particle swarm-explosion, stability, and convergence in a multidimensional complex space."
 *         Evolutionary Computation, IEEE Transactions on 6.1 (2002): 58-73.
 */
public class RPSO extends LearningAlgorithm {
    int noOfActions = 21;
    int offset = 10;
    Swarm swarm;

    private static int SWARM_SIZE = 10;
    private static int MAX_EVALUATIONS = 100;
    private static int V_MAX = 20;
    private static double ATTRACTION_FACTOR = 2.05;
    private static double CONSTRICTION_FACTOR = 0.729844;


    int evaluationCount = 0;
    public RPSO(Environment environment, int seed) {
        super(environment, seed);
    }

    @Override
    public void learn(int totalSteps, int offlineTime) {
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
    public int step(int step, int offlineTime) {
        return 0;
    }

    @Override
    public Action selectAction() {
        state = environment.getState();
        swarm = new Swarm();
        while (evaluationCount < MAX_EVALUATIONS) {
            Particle fittest = swarm.getFittest();
            for (Particle particle : swarm.getParticles()) {
                particle.move(fittest);
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
        Particle globalBest = null;

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
            if (globalBest == null)
                globalBest = particles[0];
            for (int i = 1; i < SWARM_SIZE; i++) {
                if (globalBest.getFitness() <= particles[i].getFitness()) {
                    globalBest = particles[i];
                }
            }
            return globalBest;
        }
    }

    public class Particle {
        int action = 0;
        int velocity;
        double fitness = Double.MIN_VALUE;
        int localBest = 0;

        public Particle(boolean init) {
            if (init) {
                action = random.nextInt(noOfActions) - offset;
                velocity = random.nextInt(V_MAX) - V_MAX;
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

        /**
         * Updates the particle's action based on velocity and global fitness. the neighbourhood fitness is the same
         * as the global fitness.
         * @param fittest
         */
        public void move(Particle fittest) {
            // there is only a single dimension - peak center
            velocity = (int) (CONSTRICTION_FACTOR * (velocity + ATTRACTION_FACTOR * (fittest.getAction().getValue() - this.getAction().getValue())));
//            System.out.println(velocity);
            action = action + (int) velocity;
            fitness = environment.getReward(this.getAction());
            evaluationCount++;
        }
    }
}
