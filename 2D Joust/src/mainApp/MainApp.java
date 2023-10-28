package mainApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import characters.UserCharacter;

/**
 * Class: MainApp
 * 
 * @author 304 <br>
 *         Purpose: Top level class for CSSE220 Project containing main method
 *         <br>
 *         Restrictions: None
 */
public class MainApp {
	URL url = null;
	Image image = null;
	private KeyBoardPressListener keyBoardPressListener;
	public static final int DELAY = 2;
	public boolean pause = false;

	/**
	 * ensures: runs the application
	 * 
	 * @param args unused
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		MainApp mainApp = new MainApp();
		mainApp.runApp();

	}

	/**
	 * runApp loads the starting menu which contains pixel art that is read. Adds
	 * the panel of buttons: New Game and Close
	 *
	 */

	private void runApp() throws IOException {
		JFrame menu = new JFrame("Joust!");
		menu.setSize(1290, 430);
		// window icon
		try {
			url = new URL(
					"https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/c6d62ab234526d2.png");
			image = ImageIO.read(url);
		} catch (MalformedURLException ex) {
			System.out.println("Malformed URL");
		} catch (IOException iox) {
			System.out.println("Can not load file");
		}
		menu.setIconImage(image);

		BufferedImage titleImage = ImageIO.read(new File("title_screen.png"));
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(titleImage, 0, 0, null);
			}
		};
		menu.add(panel, BorderLayout.CENTER);

		/**
		 * Class: CloseListener
		 * 
		 * @goal If the close button is pressed, dispose frame
		 *
		 */
		class CloseListener implements ActionListener { // CLOSE GAME BUTTON
			private JFrame frame;

			public CloseListener(JFrame mainFrame) {
				this.frame = mainFrame;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Sorry to see you go! Terminating window...");
				this.frame.dispose();
			}
		}
		/**
		 * Class: NewGameListener
		 * 
		 * @goal If New Game button is pressed, generate hero and level Sets timer and
		 *       frame Loads pixel art Initializes listeners
		 *
		 */
		class NewGameListener implements ActionListener { // NEW GAME BUTTON

			private UserCharacter hero = new UserCharacter(387, 500, Color.WHITE, image); // VARIABLES FOR HERO SQUARE
			private Level level = new Level(hero);
			private JFrame frame;
			private GameComponent newGameComponent;
			private Timer time;

			public NewGameListener(JFrame mainFrame) throws IOException {
				this.frame = mainFrame;

			}

			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				System.out.println("New game! Loading level 1...");

				JFrame frame = new JFrame("Joust!");
				frame.setSize(800, 600);

				// SIZE 800 X 600
				// window icon
				try {
					url = new URL(
							"https://pixelartmaker-data-78746291193.nyc3.digitaloceanspaces.com/image/c6d62ab234526d2.png");
					image = ImageIO.read(url);
				} catch (MalformedURLException ex) {
					System.out.println("Malformed URL");
				} catch (IOException iox) {
					System.out.println("Can not load file");
				}

				frame.setIconImage(image);
				frame.getContentPane().setBackground(Color.CYAN);
				try {
					newGameComponent = new GameComponent(level, hero, frame);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

				try {
					time = new Timer(DELAY, new GameAdvanceListener(newGameComponent));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				time.start();

				frame.add(newGameComponent);
				keyBoardPressListener = new KeyBoardPressListener(frame, newGameComponent, level, time);
				frame.addKeyListener(keyBoardPressListener);

				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		}

		CloseListener closeListener = new CloseListener(menu);
		NewGameListener newGameListener = new NewGameListener(menu);

		JButton newGame = new JButton("New Game");
		JButton exit = new JButton("Close");

		panel.add(newGame, BorderLayout.SOUTH);
		panel.add(exit, BorderLayout.SOUTH);

		exit.addActionListener(closeListener);
		newGame.addActionListener(newGameListener);

		menu.setVisible(true);
		menu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}