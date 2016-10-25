/*
   Cart-pole system - The well-known reinforcement learning testbed.
   Copyright (C) 2007  Jiri Isa

   This program is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License
   as published by the Free Software Foundation; either version 2
   of the License, or (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program; if not, write to the Free Software
   Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package cartpole;

import java.util.Random;

/** The cart-pole reinforcement learning.
 * Heavily based on the original Barto, Sutton, Anderson C code.
 * The C version is available at http://www-anw.cs.umass.edu/rlr/domains.html.
 * This system is described in Barto, Sutton, and Anderson,
 * "Neuronlike Adaptive Elements That Can Solve Difficult Learning
 * Control Problems," IEEE Trans. Syst., Man, Cybern., Vol. SMC-13,
 * pp. 834--846, Sept.--Oct. 1983, and in Sutton, "Temporal
 * Aspects of Credit Assignment in Reinforcement Learning", PhD
 * Dissertation, Department of Computer and Information Science, University
 * of Massachusetts, Amherst, 1984.
 *
 * @author  jiri.isaATmatfyz.cz
 */
public class RL {
	/* Parameters for reinforcement algorithm.  */                          

	/** Number of disjoint boxes of state space. */
	private final static int N_BOXES = 162;

	/** Learning rate for action weights, w. */
	private final static double ALPHA = 1000d;

	/** Learning rate for critic weights, v. */
	private final static double BETA = 0.5d;

	/** Discount factor for critic. */
	private final static double GAMMA = 0.95d;

	/** Decay rate for w eligibility trace. */
	private final static double LAMBDAw = 0.9d;

	/** Decay rate for v eligibility trace. */
	private final static double LAMBDAv = 0.8d;

	/** Termination criterion. */
	public final static long MAX_FAILURES = 30000000;

	/** Maximal number of steps in a run. If the pole has been sucesfully
	  * balanced for this amount od steps, the controller succeeded.
	  */
	public final static long MAX_STEPS = 100000;

	/** The number of learning runs performed.
	  */
	private final static int RUNS = 1;

	/** A randomness generator. */
	private Random random;

	/** The cart pole simulation system. */
	private CartPole cartPole = new CartPole();

	double prob_push_right(double s){
		return (1.0d / (1.0d + Math.exp(-java.lang.Math.max(-50.0d, java.lang.Math.min(s, 50.0d)))));
	}


	/**
	 * @param args the command line arguments
	 */
	public void run(String[] args) {
//		if(args.length < 1) {
//			System.err.println("The random seed argument was omitted.");
//			return;
//		}

//		random = new java.util.Random(Integer.parseInt(args[0]));

		random = new java.util.Random();

		for (int i = 0; i < RUNS; i++) {
			run_trial(i);
		}
	}



	private void run_trial(long run) {
		double[] w = new double[N_BOXES],   /* vector of action weights */
			v = new double[N_BOXES],   /* vector of critic weights */
			e = new double[N_BOXES],   /* vector of action weight eligibilities */
			xbar = new double[N_BOXES];   /* vector of critic weight eligibilities */
		double p, oldp, rhat, r;
		int tsteps = 0, box, i;
		double y;
		int steps = 0, failures=0;
		boolean failed = false;

		/*--- Find box in state space containing start state ---*/
		box = get_box();

		System.out.println(cartPole);

		/*--- Iterate through the action-learn loop. ---*/
		while (steps++ < MAX_STEPS && failures < MAX_FAILURES) {
			/*--- Choose action randomly, biased by current weight. ---*/
			y = (random.nextDouble() < prob_push_right(w[box])) ? 1.0d : -1.0d;
			//y = (w[box] > 0.0 ? 1.0 : -1.0);

			tsteps++;  /* total number of steps */

			/*--- Update traces. ---*/
			e[box] += (1.0d - LAMBDAw) * (y/* - 0.5d*/);
			xbar[box] += (1.0d - LAMBDAv);

			/*--- Remember prediction of failure for current state ---*/
			oldp = v[box];

			/*--- Apply action to the simulated cart-pole ---*/
			cartPole.act(y);

			System.out.println(cartPole);

			/*--- Get box of state space containing the resulting state. ---*/
			box = get_box();

			if (box < 0){
				/*--- Failure occurred. ---*/
				failed = true;
				failures++;
				System.err.println("Trial " + failures + " was " + steps + " steps.");
				System.out.println("failed");
				steps = 0;

				/*--- Reset state to (0 0 0 0).  Find the box. ---*/
				cartPole = new CartPole();
				box = get_box();

				/*--- Reinforcement upon failure is -1. Prediction of failure is 0. ---*/
				r = -1.0d;
				p = 0.0d;
			} else {
				/*--- Not a failure. ---*/
				failed = false;

				/*--- Reinforcement is 0. Prediction of failure given by v weight. ---*/
				r = 0.0d;
				p= v[box];
			}

			/*--- Heuristic reinforcement is:   current reinforcement
			  + gamma * new failure prediction - previous failure prediction ---*/
			rhat = r + GAMMA * p - oldp;

			for (i = 0; i < N_BOXES; i++) {
				/*--- Update all weights. ---*/
				w[i] += ALPHA * rhat * e[i];
				v[i] += BETA * rhat * xbar[i];
				if (v[i] < -1.0d) {
					v[i] = v[i];
				}

				if (failed) {
					/*--- If failure, zero all traces. ---*/
					e[i] = 0.0d;
					xbar[i] = 0.0d;
				} else {
					/*--- Otherwise, update (decay) the traces. ---*/       
					e[i] *= LAMBDAw;
					xbar[i] *= LAMBDAv;
				}
			}
		}

		if (failures == MAX_FAILURES) {
			System.err.println("run:" + run + " Pole not balanced. Stopping after " + failures + " trials and " + tsteps + " steps.");
		} else {
			System.err.println("Run:" + run + " Pole balanced successfully for at least " + steps + " steps after " + failures + " trials and " + tsteps + " steps.");
		}
	}


	/*
	 * get_box:  Given the current state, returns a number from 1 to 162
	 * designating the region of the state space encompassing the current state.
	 * Returns a value of -1 if a failure state is encountered.
	 */
	private final static double one_degree = 0.0174532d; /* 2pi/360 */
	private final static double six_degrees = 0.1047192d;
	private final static double twelve_degrees = 0.2094384d;
	private final static double fifty_degrees = 0.87266d;

	private int get_box() {
		int box=0;

		if(cartPole.failed()) {
			return -1; /* to signal failure */

		}

		double x = cartPole.getPosition();
		if (x < -0.8d) {
			box = 0;
		} else if (x < 0.8d){
			box = 1;
		} else {
			box = 2;
		}

		double x_dot = cartPole.getVelocity();
		if (x_dot < -0.5d) {
			;
		} else if (x_dot < 0.5) {
			box += 3;
		} else {
			box += 6;
		}

		double theta = cartPole.getAngle();
		if (theta < -six_degrees) {
			;
		} else if (theta < -one_degree) {
			box += 9;
		} else if (theta < 0) {
			box += 18;
		} else if (theta < one_degree) {
			box += 27;
		} else if (theta < six_degrees) {
			box += 36;
		} else {
			box += 45;
		}

		double theta_dot = cartPole.getAngularVelocity();
		if (theta_dot < -fifty_degrees) {
			;
		} else if (theta_dot < fifty_degrees) {
			box += 54;
		} else {
			box += 108;
		}

		return(box);
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		RL rl = new RL();
		rl.run(args);
	}


}
