package com.amansoni.rl;

import com.amansoni.ga.Individual;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Aman on 15/05/2016.
 */
public class RPSO {
    final static boolean DEBUG = false;
    DecimalFormat df = new DecimalFormat("#.00");
    Environment environment;
    Environment simulator;
    Random random;
    int noOfStates = 21;
    int noOfActions = 21;
    int stateOffset = 10;
    int accumulatedReward = 0;
    ArrayList<Individual> population;

    private static int SWARM_SIZE = 10;
    private static double ATTRACTION = 2.05;
    private static double CONSTRICTION_FACTOR = 0.729844;
    private static int VMAX = 20;
    private static int MAX_EVALUATION_COUNT = 100;
    private int evaluations = 0;

    public RPSO(Environment environment, int seed, Environment simulator) {
        random = new Random(seed);
        this.environment = environment;
        this.simulator = simulator;
        init();
    }

    private void init() {
        population = new ArrayList<>();
        for (int i = 0; i < SWARM_SIZE; i++) {
            population.add(new Individual(random));
        }
    }

    public void learn() {

    }

    class Individual {
        int action = 0;

        public Individual(Random random){
            action = random.nextInt(noOfActions) - stateOffset;
        }

        public int evaluateFitness(){
            return simulator.takeAction(new Action(action));
        }
    }
}
