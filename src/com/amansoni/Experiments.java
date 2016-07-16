package com.amansoni;

/**
 * @author Aman
 *         <p>
 *         Experiments for algorithms based on work in the following papers.
 *         Fu, Haobo, et al. "What are dynamic optimization problems?." Evolutionary Computation (CEC), 2014 IEEE Congress on. IEEE, 2014.
 *         Fu, Haobo, Peter R. Lewis, and Xin Yao. "A Q-learning Based Evolutionary Algorithm for Sequential Decision Making Problems."
 *         <p>
 *         Runs all required experiments averaged over 30 runs and reports the accumulated rewards for CPMB 1 & 2.
 */
public class Experiments {
    enum Algorithm {
        QLearning, RPSO, QBEA, Optimal, EDO, Random
    }

    final static int steps = 1000;
    final static int repeat = 30;
    final static int[] seeds = {-1155484576, -723955400, 1033096058, -1690734402, -1557280266, 1327362106, -1930858313,
            502539523, -1728529858, -938301587, 1431162155, 1085665355, 1654374947, -1661998771, -65105105, -73789608,
            -518907128, 99135751, -252332814, 755814641, 1180918287, 1344049776, 553609048, 1580443894, 629649304,
            -1266264776, 99807007, 5955764, -1946737912, 39620447};


    public static void runAveragedEDOExperiments(int offlineTime, int steps, EvolutionaryAlgorithm.Strategy strategy, Environment.ChangeType changeType) {
        double total100 = 0.;
        double total15 = 0.;
        LearningAlgorithm algorithm;
        for (int r = 0; r < repeat; r++) {
            algorithm = new EDOAlgorithm(new Environment(100, seeds[r], changeType), seeds[r], strategy);
            total100 += new Experiment(algorithm, steps, offlineTime).run();
        }
        for (int r = 0; r < repeat; r++) {
            algorithm = new EDOAlgorithm(new Environment(15, seeds[r], changeType), seeds[r], strategy);
            total15 += new Experiment(algorithm, steps, offlineTime).run();
        }
        total100 = total100 / repeat;
        total15 = total15 / repeat;
//        System.out.println(strategy.name() + "\tbias\t" + bias + "\tofflineTime\t"+ offlineTime + "\t reward\t" + total);
        System.out.println("EDO\t" + offlineTime + "\t" + strategy.name() + "\t" + changeType.name() + "\t" + total100 + "\t" + total15);
    }
/*
    public static void runAveragedQBEAExperiments(int offlineTime, EvolutionaryAlgorithm.Strategy strategy) {
        double total100 = 0.;
        double total15 = 0.;
        LearningAlgorithm algorithm;
        for (int r = 0; r < repeat; r++) {
            algorithm = new QBEA(new Environment(100), seeds[r], strategy);
            total100 += new Experiment(algorithm, steps, offlineTime).run();
//            algorithm.printPolicy();
        }
        for (int r = 0; r < repeat; r++) {
            algorithm = new QBEA(new Environment(15), seeds[r], strategy);
            total15 += new Experiment(algorithm, steps, offlineTime).run();
//            algorithm.printPolicy();
        }
        total100 = total100 / repeat;
        total15 = total15 / repeat;
//        System.out.println(strategy.name() + "\tbias\t" + bias + "\tofflineTime\t" + offlineTime + "\t reward\t" + total);
//        System.out.println(offlineTime + "\t" + total);
        System.out.println("QBEA\t" + offlineTime + "\t" + strategy.name() + "\t" + total100 + "\t" + total15);
    }*/

    public static void runAveragedQLearning(int steps, Environment.ChangeType changeType) {
        double total100 = 0.;
        double total15 = 0.;
        LearningAlgorithm algorithm;
        for (int r = 0; r < repeat; r++) {
            algorithm = new QLearning(new Environment(100, seeds[r], changeType), seeds[r]);
            total100 += new Experiment(algorithm, steps, 0).run();
//            algorithm.printPolicy();
        }
        for (int r = 0; r < repeat; r++) {
            algorithm = new QLearning(new Environment(15, seeds[r], changeType), seeds[r]);
            total15 += new Experiment(algorithm, steps, 0).run();
//            algorithm.printPolicy();
        }
        total100 = total100 / repeat;
        total15 = total15 / repeat;
//        System.out.println(strategy.name() + "\tbias\t" + bias + "\tofflineTime\t" + offlineTime + "\t reward\t" + total);
//        System.out.println(offlineTime + "\t" + total);
        System.out.println("QLearning\t" + "\t" + changeType.name() + "\t" + total100 + "\t" + total15);
    }

    public static void runAveragedRandomExperiments(int steps, Environment.ChangeType changeType) {
        double total100 = 0.;
        double total15 = 0.;
        int offlineTime = 0;

        LearningAlgorithm algorithm;
        for (int r = 0; r < repeat; r++) {
            algorithm = new RandomAlgorithm(new Environment(100, seeds[r], changeType), seeds[r]);
            total100 += new Experiment(algorithm, steps, offlineTime).run();
        }
        total100 = total100 / repeat;

        for (int r = 0; r < repeat; r++) {
            algorithm = new RandomAlgorithm(new Environment(15, seeds[r], changeType), seeds[r]);
            total15 += new Experiment(algorithm, steps, offlineTime).run();
        }
        total15 = total15 / repeat;
        System.out.println("Random\t" + "\t" + changeType.name() + "\t" + total100 + "\t" + total15);
    }

    public static void runAveragedQBEAExperiments(int offlineTime, int steps, EvolutionaryAlgorithm.Strategy strategy, Environment.ChangeType changeType) {
        double total100 = 0.;
        double total15 = 0.;
        LearningAlgorithm algorithm;
        for (int r = 0; r < repeat; r++) {
            algorithm = new QBEA(new Environment(100, seeds[r], changeType), seeds[r], strategy);
            total100 += new Experiment(algorithm, steps, offlineTime).run();
//            algorithm.printPolicy();
        }
        for (int r = 0; r < repeat; r++) {
            algorithm = new QBEA(new Environment(15, seeds[r], changeType), seeds[r], strategy);
            total15 += new Experiment(algorithm, steps, offlineTime).run();
//            algorithm.printPolicy();
        }
        total100 = total100 / repeat;
        total15 = total15 / repeat;
//        System.out.println(strategy.name() + "\tbias\t" + bias + "\tofflineTime\t" + offlineTime + "\t reward\t" + total);
//        System.out.println(offlineTime + "\t" + total);
        System.out.println("QBEA\t" + offlineTime + "\t" + strategy.name() + "\t" + changeType.name() + "\t" + total100 + "\t" + total15);
    }

    public static void main(String[] args) {
//        initialComparisons();
//        runAveragedExperiments(bias, 21, EvolutionaryAlgorithm.Strategy.Full);
//        runAveragedExperiments(bias, 21);
//        runAveragedQBEAExperiments(bias, 21, QBEA.Strategy.Full);

//        runAveragedQBEAExperiments(21, EvolutionaryAlgorithm.Strategy.Full);
//        runAveragedExperiments(21, EvolutionaryAlgorithm.Strategy.Full);

//        runAveragedQBEAExperiments(0, EvolutionaryAlgorithm.Strategy.RandomThenBest);
//        runAveragedQBEAExperiments(1, EvolutionaryAlgorithm.Strategy.OnePlusOne);


/*
        runAveragedRandomExperiments(Environment.ChangeType.Original);
        runAveragedRandomExperiments(Environment.ChangeType.Cyclic);

        runAveragedQLearning(Environment.ChangeType.Original);
        runAveragedQLearning(Environment.ChangeType.Cyclic);
*/

//        runAveragedRandomExperiments(200, Environment.ChangeType.Cyclic);
//        runAveragedRandomExperiments(400, Environment.ChangeType.Cyclic);
//        runAveragedRandomExperiments(600, Environment.ChangeType.Cyclic);
//        runAveragedRandomExperiments(800, Environment.ChangeType.Cyclic);
//        runAveragedRandomExperiments(1000, Environment.ChangeType.Cyclic);
//
//        runAveragedQLearning(200, Environment.ChangeType.Cyclic);
//        runAveragedQLearning(400, Environment.ChangeType.Cyclic);
//        runAveragedQLearning(600, Environment.ChangeType.Cyclic);
//        runAveragedQLearning(800, Environment.ChangeType.Cyclic);
//        runAveragedQLearning(1000, Environment.ChangeType.Cyclic);

/*
        int offlineTime = 0;
        runAveragedEDOExperiments(offlineTime, 200, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedEDOExperiments(offlineTime, 400, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedEDOExperiments(offlineTime, 600, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedEDOExperiments(offlineTime, 800, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedEDOExperiments(offlineTime, 1000, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);

        runAveragedQBEAExperiments(offlineTime, 200, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedQBEAExperiments(offlineTime, 400, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedQBEAExperiments(offlineTime, 600, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedQBEAExperiments(offlineTime, 800, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
        runAveragedQBEAExperiments(offlineTime, 1000, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);
*/

/*
        runAveragedQBEAExperiments(0, 200, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Original);
        runAveragedQBEAExperiments(0, 400, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Original);
        runAveragedQBEAExperiments(0, 600, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Original);
        runAveragedQBEAExperiments(0, 800, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Original);
        runAveragedQBEAExperiments(0, 1000, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Original);
*/

        for (int offlineTime = 0; offlineTime <= 21; offlineTime++) {
//            runAveragedEDOExperiments(offlineTime, 1000, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);

//            runAveragedQBEAExperiments(offlineTime, 1000, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);

//            runAveragedEDOExperiments(offlineTime, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Original);
//            runAveragedEDOExperiments(offlineTime, EvolutionaryAlgorithm.Strategy.RandomThenBest, Environment.ChangeType.Cyclic);

//            runAveragedExperiments(offlineTime, EvolutionaryAlgorithm.Strategy.OnePlusOne);
//            runAveragedExperiments(offlineTime, EvolutionaryAlgorithm.Strategy.RandomThenBest);
//            runAveragedQBEAExperiments(offlineTime, EvolutionaryAlgorithm.Strategy.RandomThenBest);
//            runAveragedQBEAExperiments(offlineTime, EvolutionaryAlgorithm.Strategy.OnePlusOne);

//            runAveragedQBEAExperiments(bias, offlineTime, EvolutionaryAlgorithm.Strategy.SplitTime);
//            runAveragedExperiments(bias, offlineTime, EvolutionaryAlgorithm.Strategy.OnePlusOne);
        }
        calcProbability();
    }

    public static void calcProbability() {
        int actions = 21;
        int state = 2;
//        double actionstate = actions * state;
//        int n = 2;
        double t = 0;

        for (int s = 0; s <= state; s++) {
            double actionstate = actions * s;
            for (int i = 0; i < actionstate; i++) {
                double p = actionstate / (actionstate - i);
                t += p;
            }
            System.out.println(s + "\tmean time\t" + t);
        }
    }

    public static void calcProbabilityMultiply() {
        int actions = 21;
        int state = 21;
        double actionstate = actions * state;
        int n = 2;
        double t = 0;

        for (int s = 0; s < state; s++) {
            for (int i = 0; i < actions; i++) {
                double p = actionstate / (actionstate - i);
//            System.out.println(i + "\t" + p);
                t += p;
            }
//        System.out.println("\tmean time\t" + t);
            System.out.println(s + "\tmean time\t" + t);
        }
    }

    public static void compareEDO() {
        int steps = 1000;
        int repeat = 30;
        int bias = 100;

        Environment environment;
        LearningAlgorithm algorithm;
        Experiments experiment;
        environment = new Environment(bias);

//        algorithm = new RandomAlgorithm(environment, seed);
//        experiment = new Experiments(algorithm, steps, repeat, offlineTime);
//        System.out.println("Random Bias:	" + bias + "\t reward\t" + experiment.run());

//        compareEDOVariations(steps, repeat, 21, bias, EDOAlgorithm.Strategy.Full);

//        compareQBEAEDOVariations(steps, repeat, 21, bias, QBEA.Strategy.RandomThenBest);


        for (int i = 0; i <= 21; i++) {
//            System.out.println("Offline time\t" + i);
            compareEDOVariations(steps, repeat, i, bias, EvolutionaryAlgorithm.Strategy.OnePlusOne);
//            compareEDOVariations(steps, repeat, i, bias, EDOAlgorithm.Strategy.RandomThenBest);
//            compareQBEAEDOVariations(steps, repeat, i, bias, QBEA.Strategy.RandomThenBest);
//            compareEDOVariations(steps, repeat, i, 15);
        }
    }

    private static void compareEDOVariations(int steps, int repeat, int offlineTime, int bias, EvolutionaryAlgorithm.Strategy strategy) {
        Environment environment = new Environment(bias);
        LearningAlgorithm algorithm;
        Experiments experiment;
//        algorithm = new EDOAlgorithm(environment, seed, strategy);
//        experiment = new Experiments(algorithm, steps, repeat, offlineTime);
//        System.out.println("EDO:\t" + strategy.name() + "\tbias\t" + bias + "\t" + offlineTime + "\t reward\t" + experiment.run());

    }

    private static void compareQBEAEDOVariations(int steps, int repeat, int offlineTime, int bias, EvolutionaryAlgorithm.Strategy strategy) {
        Environment environment;
        LearningAlgorithm algorithm;
        Experiments experiment;
        environment = new Environment(bias);
//        algorithm = new QBEA(environment, seed, strategy);
//        experiment = new Experiments(algorithm, steps, repeat, offlineTime);
//        System.out.println("QBEA:\t" + strategy.name() + "\tbias\t" + bias + "\t" + offlineTime + "\t reward\t" + experiment.run());

    }


    public static void initialComparisons() {
        int interval = 1;
        int steps = 1000;
        int repeat = 30;
        int offlineTimeAllowed = 21;

        int bias = 100;
        compareAlgorithms(steps, repeat, bias, 0, 10000);
        compareAlgorithms(steps, repeat, bias, offlineTimeAllowed, 10000);

        bias = 15;
        compareAlgorithms(steps, repeat, bias, 0, 1000);
        compareAlgorithms(steps, repeat, bias, offlineTimeAllowed, 1000);

//        steps = 20;
//        bias = 100;
//        compareSteps(steps, repeat, bias, interval, 0, 1);
//        compareSteps(steps, repeat, bias, interval, offlineTimeAllowed, 1);
//
//        bias = 15;
//        compareSteps(steps, repeat, bias, interval, 0, 1);
//        compareSteps(steps, repeat, bias, interval, offlineTimeAllowed, 1);
    }

    private static void compareSteps(int steps, int repeat, int bias, int interval, int offlineTime, int resultsMultiplier) {
        System.out.println("\nRunning experiment " + " bias: " + bias + " repeated " + steps + " averaged over " + repeat
                + " Offline time:" + offlineTime + " multiplier:" + resultsMultiplier);
        // optimal
        String s = "Optimal\t";
        for (int i = 0; i <= steps; i += interval) {
            s += createExperimentRun(1, bias, i, Algorithm.Optimal, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "EDO\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.EDO, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "QLearning\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.QLearning, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "QBEA\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.QBEA, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);

        s = "Random\t";
        for (int i = 0; i <= steps; i++) {
            s += createExperimentRun(repeat, bias, i, Algorithm.Random, offlineTime, resultsMultiplier) + "\t";
        }
        System.out.println(s);
    }

    private static void compareAlgorithms(int steps, int repeat, int bias, int offlineTime, int resultsMultiplier) {
        System.out.println("\nRunning experiment " + " bias: " + bias + " steps:" + steps + " averaged over:" + repeat
                + " Offline time:" + offlineTime + " multiplier:" + resultsMultiplier);
        System.out.println("Optimal\t" + createExperimentRun(1, bias, steps, Algorithm.Optimal, offlineTime, resultsMultiplier));
        System.out.println("EDO\t" + createExperimentRun(repeat, bias, steps, Algorithm.EDO, offlineTime, resultsMultiplier));
        System.out.println("QLearning\t" + createExperimentRun(repeat, bias, steps, Algorithm.QLearning, offlineTime, resultsMultiplier));
        System.out.println("QBEA\t" + createExperimentRun(repeat, bias, steps, Algorithm.QBEA, offlineTime, resultsMultiplier));
        System.out.println("Random\t" + createExperimentRun(repeat, bias, steps, Algorithm.Random, offlineTime, resultsMultiplier));
    }

    public static String createExperimentRun(int repeat, int bias, int steps, Algorithm algorithm, int offlineTime, int resultsMultiplier) {
//        Random random = new Random(seed);
//        System.out.println("Running experiment " + algorithm.name() + " bias: " + bias + " repeated " + steps + " averaged over " + repeat);
        long BEGIN = System.currentTimeMillis();
        double rewards = 0;
        for (int i = 0; i < repeat; i++) {
//            rewards += createExperiment(bias, steps, random.nextInt(), algorithm, offlineTime);
//            rewards += createExperimentRL(bias, steps, i);
        }
//        System.out.println("Averaged reward:\t" + rewards / (double) repeat + "\t" + algorithm.name());
        long END = System.currentTimeMillis();
//        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
//        return algorithm.name() + "\t" + rewards / (double) (repeat) + "\t" + (END - BEGIN) / 1000.0 + "\n";
        Double ret = rewards / (double) (repeat * resultsMultiplier);
//        System.out.println(rewards  + "\t" + ret.toString());
//        System.out.println((rewards / 100) / (double) (repeat));
        return ret.toString();
    }

    public static double createExperiment(int bias, int steps, int seed, Algorithm algorithm, int offlineTime) {
//        Experiments experiment = new Experiments(seed, bias, algorithm);
////        for (int i = 0; i < steps; i++) {
////            experiment.learningAlgorithm.step(i, offlineTime);
////        }
//        experiment.learningAlgorithm.learn(steps, offlineTime);
////        experiment.learningAlgorithm.printPolicy();
//        return experiment.learningAlgorithm.getAccumulatedReward();
        return 0;
    }
}