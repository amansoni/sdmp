package com.amansoni;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    final static int repeat = 100;
    static EvolutionaryAlgorithm.Strategy strategy = EvolutionaryAlgorithm.Strategy.RandomThenBest;

    final static int[] seeds = {1245097796, 1661198952, 122864260, -1728364941, -1610161142, -1553747733, -1514202174, 1222408005, -1578471556, 521614943, -389764704, -1649559921, 1994886919, 2034262993, 1507881027, -545858353, -654192656, 1185726362, -1836349758, -1022557879, -552194205, -1345255965, -1203435676, -1341362130, -472864820, -2051027606, -565293299, -1779308806, 1373421413, -1688259451, 849821684, 1298182941, -2055738353, 903731815, -1166050407, -1822845219, -827989914, 1621600417, 1009567734, 1778423930, -1967909361, 1444884716, -922348556, 1137581970, 2025531731, 2003346213, -611987680, -1528167225, -526263823, 920625048, -720533346, -1858766946, -133249745, 581003648, -1378875043, 108191402, -1846334158, -349618738, 805723531, 659101161, -1792384625, -577449431, 552042827, 2144448365, -900817631, 503529526, 1025615013, -1254614466, 832400668, -857677544, 386874046, -370551938, 2094200162, -1105797830, 1372670024, 1966474702, 1334093332, 457012299, -1189370565, 842873584, 804861548, 240927114, -1726536252, 1962410925, 1409611978, -910950333, -142025090, -1624917511, -1102128116, 247567512, -693307425, -1201487532, 299085346, -480093052, 505857268, 1454893426, -1866304544, -604107491, -1102114480, 1978324765};

    public Experiments() {

    }

    public double[] runAveragedExperiments(Environment.ChangeType changeType, int bias, int offlineTime, Algorithm algorithm) {
        double total = 0.;
        double[] totals = new double[repeat];
        for (int i = 0; i < repeat; i++) {
            totals[i] = 0.;
        }
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
            totals[r] = new Experiment(changeType, bias, learningAlgorithm, steps, offlineTime, seeds[r]).run();
            total += totals[r];
        }
        total = total / repeat;
        double[] squaredDifferences = new double[repeat];
        double meanDifferences = 0.;
        for (int i = 0; i < repeat; i++) {
            double difference = totals[i] - total;
//            System.out.println("differences\t" + difference);
            squaredDifferences[i] = Math.pow(difference, 2);
//            System.out.println("squaredDifferences\t" + squaredDifferences[i]);
            meanDifferences += squaredDifferences[i];
//            System.out.println("meanDifferences\t" + meanDifferences);
        }
//        System.out.println(meanDifferences);
        meanDifferences = (meanDifferences / repeat);
        meanDifferences = Math.sqrt(meanDifferences);
//        System.out.println(meanDifferences);
        double[] returnValues = new double[]{total, meanDifferences};
//        System.out.println(returnValues[0] + "\t" + returnValues[1]);
        return returnValues;
    }

    public List<String> run(Environment.ChangeType changeType, int bias) {
        List<String> lines = new ArrayList<>();
        System.out.println(changeType.name() + "\t" + bias);
        lines.add("Delta,\tOptimal,\tOptimalError,\tRandom,\tRandomError,\tQLearning,\tQLearningError,\tEDO,\tEDOError,\tQBEA,\tQBEAError");
        System.out.println("Delta,\tOptimal,\tOptimalError,\tRandom,\tRandomError,\tQLearning,\tQLearningError,\tEDO,\tEDOError,\tQBEA,\tQBEAError");
        for (int offlineTime = 0; offlineTime <= tauFMax; offlineTime++) {
            double[] optimalValues = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.Optimal);
            double[] randomValues = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.Random);
            double[] qLearningValues = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.QLearning);
            double[] EDOValues = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.EDO);
            double[] QBEAValues = runAveragedExperiments(changeType, bias, offlineTime, Algorithm.QBEA);

            double delta = (double) offlineTime / tauFMax;
            String s = delta + "\t";
            s += optimalValues[0] + ",\t" + optimalValues[1] + ",\t";
            s += randomValues[0] + ",\t" + randomValues[1] + ",\t";
            s += qLearningValues[0] + ",\t" + qLearningValues[1] + ",\t";
            s += EDOValues[0] + ",\t" + EDOValues[1] + ",\t";
            s += QBEAValues[0] + ",\t" + QBEAValues[1] + ",\t";
            System.out.println(s);
            lines.add(s);
        }
        return lines;
    }

    public static void main(String[] args) {
//        outputSeeds();
        writeBenchmarkFiles();
//        writeExplorationProbilities();
    }

    private static void outputSeeds() {
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
//            System.out.print(random.nextInt() +",");
            System.out.print("0.,");
        }
    }

    private static void writeExplorationProbilities() {
        for (int offlineTime = 0; offlineTime <= tauFMax; offlineTime++) {
            calcProbability(21, 21, offlineTime);
        }
    }

    public static void writeBenchmarkFiles() {
        String s = "C:\\Users\\Aman\\Documents\\writing\\5648303hjdksq\\data\\";
        writeOutput(new Experiments().run(Environment.ChangeType.Oscillate, 100), s + "cmpbo100.csv");
//        writeOutput(new Experiments().run(Environment.ChangeType.Oscillate, 15), s + "cmpbo15.csv");

//        writeOutput(new Experiments().run(Environment.ChangeType.Cyclic, 100), s + "cmpbc100.csv");
//        writeOutput(new Experiments().run(Environment.ChangeType.Cyclic, 15), s + "cmpbc15.csv");
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