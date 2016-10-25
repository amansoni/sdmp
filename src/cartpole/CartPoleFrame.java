package cartpole;

import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CartPoleFrame extends JFrame {
	private final static int BOTTOM = 50;
	private final static int BORDER = 50;
	private final static int WALL_HEIGHT = 10;
	private final static int CART_HEIGHT = 30;
	private final static int CART_WIDTH = 60;
	private final static double AREA_WIDTH = 4.8;

	private CartPolePanel cpp = new CartPolePanel();
	public double cartX, cartAngle;
	private int round;

	public CartPoleFrame() {
		getRootPane().getContentPane().add(cpp);
	}

	private class CartPolePanel extends JPanel {
		public void paint(Graphics g) {
			super.paint(g);

			// draw the walls
			int y = cpp.getHeight() - BOTTOM;
			int left = BORDER;
			int right = cpp.getWidth() - BORDER;
			int wallTop = y - WALL_HEIGHT + 1;

			g.drawLine(left, y, right, y);
			g.fillRect(0, wallTop, BORDER, WALL_HEIGHT);
			g.fillRect(right, wallTop, BORDER, WALL_HEIGHT);

			// draw the cartbody
			int x_mult = cpp.getWidth() / 3;
			int cartCenter = (int) (cpp.getWidth() / 2 - cartX * x_mult / (AREA_WIDTH / 2));
			int cartLeft = cartCenter - CART_WIDTH / 2;
			int cartTop = y - CART_HEIGHT;
			g.fillRect(cartLeft, cartTop, CART_WIDTH, CART_HEIGHT / 2);

			//draw the cart wheels
			int diameter = CART_HEIGHT / 2;
			int wheelTop = y - diameter;
			int wheelLeft1 = cartLeft;
			int wheelLeft2 = cartLeft + CART_WIDTH - diameter;
			g.fillOval(wheelLeft1, wheelTop, diameter, diameter);
			g.fillOval(wheelLeft2, wheelTop, diameter, diameter);

			int poleLength = x_mult * 2;
			int poleTopX = cartCenter + (int) (poleLength * Math.sin(cartAngle));
			int poleTopY = cartTop - (int) (poleLength * Math.cos(cartAngle));
			g.drawLine(cartCenter, cartTop, poleTopX, poleTopY);

			g.drawString(Integer.toString(round), 10, 20);
		}
	}

	public void refresh() {
		round++;
		cpp.invalidate();
		cpp.repaint();
	}

	public void failed() {
		round = 0;
	}

	public static void main(String[] args) throws IOException {
		long delay = 0;
		if(args.length > 0) {
			delay = Long.parseLong(args[0]);
		}

		CartPoleFrame cpf = new CartPoleFrame();

		cpf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cpf.setSize(400, 400);
		cpf.setVisible(true);

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(String line = br.readLine(); line != null; line = br.readLine()) {
			System.out.println(line); //resend
			if(line.startsWith("failed")) {
				cpf.failed();
			} else {
				String[] items = line.split("\\s");

				cpf.cartX = Double.parseDouble(items[0]);
				cpf.cartAngle = Double.parseDouble(items[2]);
				cpf.refresh();
			}

			if(delay > 0) {
				try {
					Thread.currentThread().sleep(delay);
				} catch (Exception ex) {
					;
				}
			}
		}

		System.exit(0);
	}
}
