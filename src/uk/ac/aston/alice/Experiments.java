package uk.ac.aston.alice;

import com.amansoni.*;

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
    static int seed = 0;
    static int[] seeds = {-1155484576,-723955400,1033096058,-1690734402,-1557280266,1327362106,-1930858313,502539523,-1728529858,-938301587,1431162155,1085665355,1654374947,-1661998771,-65105105,-73789608,-518907128,99135751,-252332814,755814641,1180918287,1344049776,553609048,1580443894,629649304,-1266264776,99807007,5955764,-1946737912,39620447};



    public static void main(String[] args) {
        Random random = new Random(seed);
        for (int i = 0; i < 30; i++) {
            System.out.print(random.nextInt() +",");
        }

    }

}