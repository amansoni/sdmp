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

//package cz.matfyz.isa.jiri.CartPole;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
/** The cart-pole system simulation.
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
public class CartPole {
	/* Parameters for simulation */
	private final static double GRAVITY = 9.8d;
	private final static double MASSCART = 1.0d;
	private final static double MASSPOLE = 0.1d;
	private final static double TOTAL_MASS = (MASSPOLE + MASSCART);
	private final static double LENGTH = 0.5d;  /* actually half the pole's length */
	private final static double POLEMASS_LENGTH = (MASSPOLE * LENGTH);
	private final static double FORCE_MAG = 10.0d;
	private final static double TAU = 0.02; //0.02;     /* seconds between state updates */
	private final static double WIDTH = 4.8;	/* size of the area in meters */
	private final static double HALF_WIDTH = WIDTH / 2; /* the area coordinates are from -HALF_WIDTH to +HALF_WIDTH */
	private final static double MAX_START_VELOCITY = 4.0;
	private final static double MAX_START_ANGULAR_VELOCITY = 3.0;
	private final static double TWELVE_DEGREES = 0.2094384d;


	/** The index of the position coordinate in the configuration.
	 * @see getConfiguration
	 * @see setConfiguration
	 */
	private final static int POSITION_INDEX = 0;

	/** The index of the velocity in the configuration.
	 * @see getConfiguration
	 * @see setConfiguration
	 */
	private final static int VELOCITY_INDEX = 1;

	/** The index of the angle in the configuration.
	 * @see getConfiguration
	 * @see setConfiguration
	 */
	private final static int ANGLE_INDEX = 2;

	/** The index of the angular velocity in the configuration.
	 * @see getConfiguration
	 * @see setConfiguration
	 */
	private final static int ANGULAR_VELOCITY_INDEX = 3;

	/* Variables for the cart-pole system configuration. */
	/** Cart position [meters] */
	private double x;
	/** Cart velocity [m/s] */
	private double velocity;
	/** Pole angle [radians] */
	private double theta;
	/** Pole angular velocity [radian/s] */
	private double angularVelocity;

	/** Creates a new instance of CartPole */
	public CartPole() {

	}

	/** Returns an array with the position and velocity description.
	 * The first item is the cart position, the second is the cart velocity,
	 * the third the pole angle, and the fourth the pole angular velocity.
	 * @param return An array with the position and velocity description.
	 * @return The current cart-pole system configuration.
	 * @see getPosition
	 * @see getVelocity
	 * @see getAngle
	 * @see getAngularVelocity
	 */
	public double[] getConfiguration(){
		double[] result = new double[4];

		result[POSITION_INDEX] = x;
		result[VELOCITY_INDEX] = velocity;
		result[ANGLE_INDEX] = theta;
		result[ANGULAR_VELOCITY_INDEX] = angularVelocity;

		return result;
	}

	/** Sets this cart-pole system to the given configuration.
	 * The first item is the cart position, the second is the cart velocity,
	 * the third the pole angle, and the fourth the pole angular velocity.
	 * @param configuration THe requested configuration.
	 * @see setPosition
	 * @see setVelocity
	 * @see setAngle
	 * @see setAngularVelocity
	 */
	public void setConfiguration(double[] configuration) {
		x = configuration[POSITION_INDEX];
		velocity = configuration[VELOCITY_INDEX];
		theta = configuration[ANGLE_INDEX];
		angularVelocity = configuration[ANGULAR_VELOCITY_INDEX];
	}

	/** Gets the current cart position [meters].
	 * @return The current cart position.
	 */
	public double getPosition() {
		return x;
	}

	/** Sets the current cart position.
	 * @param x The requested cart position [meters].
	 */
	public void setPosition(double x) {
		this.x = x;
	}

	/** Gets the current cart velocity ([meters/second].
	 * @return The cart velocity.
	 */
	public double getVelocity() {
		return velocity;
	}

	/** Sets the cart velocity [meter/second].
	 * @param velocity THe equested velocity.
	 */
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}

	/** Gets the pole angle [rad].
	 * @return The pole angle.
	 */
	public double getAngle() {
		return theta;
	}

	/** Sets the pole angle.
	 * @param angle The requested pole angle [rad].
	 */
	public void setAngle(double angle) {
		this.theta = angle;
	}

	/** Gets the pole anglar velocity [rad/s].
	 * @return The pole angular velocity.
	 */
	public double getAngularVelocity() {
		return angularVelocity;
	}

	/** Sets the pole angular velocity.
	 * @param angularVelocity The requested angular velocity [rad/s].
	 */
	public void setAngularVelocity(double angularVelocity) {
		this.angularVelocity = angularVelocity;
	}

	/** Checks if the pole failed or the cart is out of the area.
	 *  @return True if failure conditions satisfied
	 */
	public boolean failed(){
		return (x < -HALF_WIDTH ||
				x > HALF_WIDTH  ||
				theta < -TWELVE_DEGREES ||
				theta > TWELVE_DEGREES);

	}

	/** Changes the cart position according to the world model and the action.
	 *  @param Action Requested move (-1 ... left; 1 ... right)
	 *  @return True if the pole falled or the cart is out of the area.
	 *  @see failed
	 */
	public boolean actAndCheck(double action){
		act(action);
		return failed();
	}

	/** Performs the simulation step.
	 * Takes an action [-1 to 1] and the current values of the
	 * four state variables and updates their values by estimating the state
	 * TAU seconds later.
	 * @param action The cart action suggested by a controller.
	 * @see TAU
	 * @see getConfiguration
	 */
	public void act(double action) {
		if(Math.abs(action) > 1.0) {
			System.err.println("Action out of range: " + action);
			action = action / Math.abs(action); //signum is in Java only since 1.5 :-/
		}

		double force = action * FORCE_MAG;
		double costheta = java.lang.Math.cos(theta);
		double sintheta = java.lang.Math.sin(theta);

		double temp = (force + POLEMASS_LENGTH * angularVelocity * angularVelocity * sintheta) / TOTAL_MASS;

		double thetaacc = (GRAVITY * sintheta - costheta * temp)
			/ (LENGTH * (4.0/3.0 - MASSPOLE * costheta * costheta
						/ TOTAL_MASS));

		double xacc  = temp - POLEMASS_LENGTH * thetaacc * costheta / TOTAL_MASS;

		/* Update the four state variables, using Euler's method. */
		x  += TAU * velocity;
		velocity += TAU * xacc;
		theta += TAU * angularVelocity;
		angularVelocity += TAU * thetaacc;
	}

	/** Creates the string representation of the current cart-pole system configuration.
	  * @return A space separated list of configuration values. The first item is the cart position,
	  * the second is the cart velocity,
	  * the third the pole angle, and the fourth the pole angular velocity.
	  */
	public String toString() {
		StringBuffer sb = new StringBuffer();

		sb.append(getPosition());
		sb.append(' ');
		sb.append(getVelocity());
		sb.append(' ');
		sb.append(getAngle());
		sb.append(' ');
		sb.append(getAngularVelocity());

		return sb.toString();
	}

	/** Starts the simulation.
	  * Prints the current configuration to STDOUT, receives actions at STDIN.
	  */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		CartPole cart = new CartPole();

		System.out.println(cart); //the initial information so that the controller can react
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			double action = Double.parseDouble(line);
			if(cart.actAndCheck(action)) {
				System.out.println("failed " + cart);
				cart = new CartPole();
				System.out.println(cart); //the initial information so that the controller can react
			} else {
				System.out.println(cart);
			}
		}
	}
}
