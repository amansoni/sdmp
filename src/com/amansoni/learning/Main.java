package com.amansoni.learning;

import java.text.SimpleDateFormat;
import java.util.Arrays;

public class Main {

    public static boolean debug = true;
    static int[] state = new int[10];
    static double discountFactor = 0.1d;
    static double learningParameter = 0.1d;
    static int numberOfEpisodes = 1;

    public static void main(String[] args) {
        log("Begin process with args " + Arrays.toString(args));
        Environment le = new Environment();

        //    Require: discount factor, learning parameter
        LearningAlgorithm la = new LearningAlgorithm(discountFactor, learningParameter);

        //    1: initialize Q arbitrarily (e.g. Q(a,s) = 0, 8s 2 S, 8a 2 A )
        la.initialise(true);

        //    2: for all episode do
        for (int i = 0; i < numberOfEpisodes; i++) {
            //    3: s is initialized as the starting state
            state = le.GetState();
            boolean isGoal = false;
            //    4: repeat
            while (!isGoal) {
                //    5: choose an action based on Q and an exploration strategy
                Action action = la.getAction();
                //    6: perform action a
                le.perform(action);
                //    7: observe new state s’
                int[] tempState = le.GetState();
                //       ..... and receive reward r
                la.GetReward(le.GetReward(i, Action.getIntValue()));
                //    8: update Q values
                la.updateLearning(state, tempState);
                //    9: Update state s := s’
                state = tempState;
                isGoal = la.IsGoalState(state);
                //    10: until s’ is a goal state
            }
            //    11: end for
        }
        log("End processing");
    }

    public static void log(String message) {
        if (debug)
            System.out.println(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())
                            + "\t" + Main.class.getSimpleName()
                            + "\t" + message);
    }

    public static void log(Object logger, String message) {
        if (debug)
            System.out.println(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())
                            + "\t" + logger.getClass().getSimpleName()
                            + "\t" + message);
    }
}
