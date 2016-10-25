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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

/** The abstract controller using the STDIN and STDOUT
  * for the interaction with the simulation system.
  *
  * @author jiri.isaATmatfyz.cz
  */
public abstract class Controller {
	/** The index of the position coordinate in the configuration.
	 */
	private final static int POSITION_INDEX = 0;
	/** The index of the velocity in the configuration.
	 */
	private final static int VELOCITY_INDEX = 1;
	/** The index of the angle in the configuration.
	 */
	private final static int ANGLE_INDEX = 2;
	/** The index of the angular velocity in the configuration.
	 */
	private final static int ANGULAR_VELOCITY_INDEX = 3;

	/** Cart position [meters] */
	private double position;
	/** Cart velocity [m/s] */
	private double velocity;
	/** Pole angle [radians] */
	private double angle;
	/** Pole angular velocity [radian/s] */
	private double angularVelocity;

	/** Cart position [meters] */
	public double getPosition() {
		return position;
	}

	/** Cart velocity [m/s] */
	public double getVelocity() {
		return velocity;
	}

	/** Pole angle [radians] */
	public double getAngle() {
		return angle;
	}

	/** Pole angular velocity [radian/s] */
	public double getAngularVelocity() {
		return angularVelocity;
	}

	/** Invoked to figure out the cuggested action.
	 *  @return The proposed action [-1,1] value.
	 */
	public abstract double act();

	/** The default failure handling - no action.
	  */
	public void failed() {
		;
	}

	/** Updates the cart-pole system information based on the configuration description.
	  * @param line The configuration description.
	  */
	private void update(String line) {
		String[] items = line.split("\\s");

		position = Double.parseDouble(items[POSITION_INDEX]);
		velocity = Double.parseDouble(items[VELOCITY_INDEX]);
		angle = Double.parseDouble(items[ANGLE_INDEX]);
		angularVelocity = Double.parseDouble(items[ANGULAR_VELOCITY_INDEX]);
	}

	/** Runs the simulation loop.
	  * @throws IOException When the input reading fails.
	  */
	public void run() throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			if(line.startsWith("failed")) {
				failed();
			} else {
				this.update(line);
				System.out.println(act());
			}
		}
	}
}
