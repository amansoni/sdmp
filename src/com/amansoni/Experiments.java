package com.amansoni;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
        Optimal, Random, QLearning, EDO, QBEA
    }

    final static int tauFMax = 21;
    final static int steps = 1000;
    final static int repeat = 30;
    static EvolutionaryAlgorithm.Strategy strategy = EvolutionaryAlgorithm.Strategy.RandomThenBest;

    final static int[] seeds = {-1155484576, -723955400, 1033096058, -1690734402, -1557280266, 1327362106, -1930858313,
            502539523, -1728529858, -938301587, 1431162155, 1085665355, 1654374947, -1661998771, -65105105, -73789608,
            -518907128, 99135751, -252332814, 755814641, 1180918287, 1344049776, 553609048, 1580443894, 629649304,
            -1266264776, 99807007, 5955764, -1946737912, 39620447};

    public Experiments() {

    }

    public double runAveragedExperiments(Environment.ChangeType changeType, int bias, int offlineTime, Algorithm algorithm) {
        double total = 0.;
        LearningAlgorithm learningAlgorithm;
        for (int r = 0; r < repeat; r++) {
            Environment environment = new Environment(bias, changeType);
            switch (algorithm) {
                case Random:
                    learningAlgorithm = new RandomAlgorithm(environment, seeds[r]);
                    break;
                case QLearning:
                    learningAlgorithm = new QLearning(environment, seeds[r]);
                    break;
                case EDO:
                    learningAlgorithm = new EDOAlgorithm(environment, seeds[r], strategy);
                    break;
                case QBEA:
                    learningAlgorithm = new QBEA(environment, seeds[r], strategy);
                    break;
                default: //Optimal
                    learningAlgorithm = new Optimal(environment, seeds[r]);
                    break;
            }
            total += new Experiment(changeType, bias, learningAlgorithm, steps, offlineTime, seeds[r]).run();
        }
        total = total / repeat;
        return total;
    }

    public List<String> run(Environment.ChangeType changeType, int bias) {
        List<String> lines = new ArrayList<>();
        System.out.println(changeType.name() + "\t" + bias);
        lines.add("Delta,\tOptimal,\tRandom,\tQLearning,\tEDO,\tQBEA");
        System.out.println("Optimal,\tRandom,\tQLearning,\tEDO,\tQBEA,");
        for (int offlineTime = 0; offlineTime <= tauFMax; offlineTime++) {
            double[] values = new double[]{0., 0., 0., 0., 0.};

            values[0] = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.Optimal);
            values[1] = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.Random);
            values[2] = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.QLearning);
            values[3] = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.EDO);
            values[4] = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.QBEA);

            System.out.println(offlineTime + "\t" + values[0] + "\t" + values[1] + "\t" + values[2] + "\t" + values[3] + "\t" + values[4]);
            double delta = (double) offlineTime / tauFMax;
            lines.add(delta + ",\t" + values[0] + ",\t" + values[1] + ",\t" + values[2] + ",\t" + values[3] + ",\t" + values[4]);
        }
        return lines;
    }

    public static void main(String[] args){
//        writeBenchmarkFiles();
        writeExplorationProbilities();
    }

    private static void writeExplorationProbilities() {
        for (int offlineTime = 0; offlineTime <= tauFMax; offlineTime++) {
            calcProbability(21, 21, offlineTime);
        }
    }

    public static void writeBenchmarkFiles() {
        String s = "C:\\Users\\Aman\\Documents\\writing\\5648303hjdksq\\data\\";
        writeOutput(new Experiments().run(Environment.ChangeType.Oscillate, 100), s + "cmpbo100.csv");
        writeOutput(new Experiments().run(Environment.ChangeType.Oscillate, 15), s + "cmpbo15.csv");

        writeOutput(new Experiments().run(Environment.ChangeType.Cyclic, 100), s + "cmpbc100.csv");
        writeOutput(new Experiments().run(Environment.ChangeType.Cyclic, 15), s + "cmpbc15.csv");
    }

    public static void writeOutput(List<String> lines, String path) {
        Path file = Paths.get(path);
        try {
            Files.write(file, lines, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void calcProbability(int numOfActions, int noOfStates, double numOfEvalsPerTimeStep) {
        double t = 0;
        for (int s = 0; s <= noOfStates; s++) {
            double actionstate = numOfActions * s;
            for (int i = 0; i < actionstate; i++) {
                double p = (actionstate / (actionstate - i)) / numOfEvalsPerTimeStep;
                t += p;
            }
            System.out.println(s + "\tmean time\t" + t);
        }
    }
}