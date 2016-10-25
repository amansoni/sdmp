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

import java.io.IOException;
import java.util.Random;

/** An extremely simple cart-pole system controller.
  * Created for an illustrational purpose, how to use the <code>Controller</code> predecessor.
  * @see Controller
  */
public class RandomController extends Controller {
	/** Random generator. */
	private Random rnd = new Random();

	/** Suggests a random action.
	  */
	public double act() {
		//STDOUT is reserved for messages passing
		System.err.print(getPosition());
		System.err.print(' ');
		System.err.print(getVelocity());
		System.err.print(' ');
		System.err.print(getAngle());
		System.err.print(' ');
		System.err.println(getAngularVelocity());

		return rnd.nextDouble() * 2.0 - 1.0;
	}

	/** Handle the failure.
	  */
	public void failed() {
		System.err.println("failed");
	}

	/** Starts the random controller.
	  */
	public static void main(String[] args) throws IOException {
		Controller controller = new RandomController();
		controller.run();
	}
}
