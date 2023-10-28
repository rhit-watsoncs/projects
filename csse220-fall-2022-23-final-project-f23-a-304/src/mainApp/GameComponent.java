package mainApp;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;

import characters.UserCharacter;
import characters.AbstractCharacter;

/**
 * Class: GameComponent
 * 
 * @Goal Main game workings Background image Draw hero and level Sound effects
 *       Draws score, level count, and lives left Win and loss screens
 *       Collisions
 */
public class GameComponent extends JComponent {

	private UserCharacter hero;
	private Level level = new Level(hero);
	private int score = 0;
	private int enemiesLeft = level.getEnemies().size();
	private boolean heroWon;
	private boolean heroLost;

	Sound sound = new Sound();

	public GameComponent(Level level, UserCharacter hero, JFrame frame) throws IOException {
		this.level = level; // change it to creating here instead of passing it in
		this.hero = hero;
		playMusic(0);
	}

	public void drawScreen() {
		this.repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, 800, 600);

		BufferedImage background = null;
		try {
			background = ImageIO.read(new File("bg.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		g2.drawImage(background, 0, 0, null);

		g2.setFont(new Font("TimesRoman", Font.PLAIN, 20));
		g2.setColor(Color.PINK);
		g2.drawString("Lives left: " + hero.getLives(), 30, 30);
		g2.drawString("Score: " + score, 30, 60);
		g2.drawString("Level: " + level.getLevelNumber() + " / " + level.totalLevelNum, 680, 30);

		g2.setColor(Color.RED);
		g2.fillRect(0, 550, 800, 50);

		level.drawOn(g2); // DRAW LEVEL
		hero.drawOn(g2); // DRAW HERO
		hero.move();

		for (AbstractCharacter en : level.getEnemies()) {
			en.drawOn(g2);
		}
		if (heroWon) { // hero won scene
			BufferedImage wonImage = null;
			try {
				wonImage = ImageIO.read(new File("you_win_image (1).png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g2.drawImage(wonImage, 250, 250, null);
		}

		if (heroLost) { // hero lost scene
			BufferedImage loseImage = null;
			try {
				loseImage = ImageIO.read(new File("you_lose_image (6).png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g2.drawImage(loseImage, 250, 250, null);
		}
	}

	public void updateState() throws IOException {
		if (!heroWon && !heroLost) {
			updateEnemies();
			handleCollision();
		}
	}

	public void updateEnemies() throws IOException {
		for (AbstractCharacter en : level.getEnemies()) {
			if (en.getClass().getName() == "characters.HardEnemy") {
				en.follow(hero.getX(), hero.getY());
			}
			en.move();
		}
	}

	/**
	 * Class: handleCollisions
	 * 
	 * @throws IOException
	 * @goal handle collisions and increase score and decrease number of lives
	 *       accordingly Egg Increment through levels
	 */
	public void handleCollision() throws IOException {
		// collisions with platforms
		for (Rectangle block : level.getBlocks()) {
			if (block.intersects(hero.getBoundingBox()) && hero.getY() < block.getY()) { // hero above
				hero.setYSpeed(0.0);
				hero.setY((int) block.getY() - hero.getHeight());
			}

			if (block.intersects(hero.getBoundingBox()) && hero.getY() > block.getY()) { // horizontal
				hero.setYSpeed(1.0);
			}

			if (block.intersects(hero.getBoundingBox()) && hero.getX() < block.getX()) {
				System.out.println("hit left");
				hero.setXSpeed(-1.0);
				hero.setX((int) block.getX() - hero.getWidth());
			}

			if (block.intersects(hero.getBoundingBox()) && hero.getX() > block.getX()) {
				hero.setXSpeed(1.0);
			}
			// collisions with enemies
			for (AbstractCharacter en : level.getEnemies()) {
				if (hero.getBoundingBox().intersects(en.getBoundingBox()) && hero.getY() == en.getY()
						&& en.isEgg == false) {
					en.setXSpeed(en.getXSpeed() * -1);
					en.setX(en.getX() - 200);
				}

				if (hero.getBoundingBox().intersects(en.getBoundingBox()) && hero.getY() < en.getY()) {
					en.isEgg = true;

					if (hero.getBoundingBox().intersects(en.getBoundingBox()) && en.getBoundingBox().intersects(block)
							&& en.isEgg == true) {
						en.setY(600); // where the egg get moved to
						score = score + 100;

						System.out.println(enemiesLeft);
						enemiesLeft--;
						System.out.println(enemiesLeft + " " + "new count");
					}
				}

				if (block.intersects(en.getBoundingBox())) {
					en.setY((int) block.getY() - en.getHeight());
					en.setYSpeed(0);
				}

				if (hero.getBoundingBox().intersects(en.getBoundingBox()) && hero.getY() >= en.getY() // resets the hero
																										// position
						&& en.isEgg == false) {
					hero.isHit();
				}

				if (hero.getY() >= 550) { // resets the hero position when the hero falls into lava
					hero.isHit();
				}

				if (level.lava.intersects(en.getBoundingBox())) {
					enemiesLeft--;
					System.out.println(enemiesLeft);
					en.setY(1000);
				}
			}
			// death sound
			if (hero.getLives() == 0) {
				System.out.println("Game Over!");
				this.heroLost = true;

			} // changes level if both enemies are killed.

			if (enemiesLeft == 0) {
				level.setLevelNumber(level.getLevelNumber() + 1); // moves the level up by 1
				level.setEnemy();
				hero.setX(380);
				hero.setY(460);
				enemiesLeft = level.getEnemies().size(); // gets the new enemy level count

				if (level.getLevelNumber() > level.totalLevelNum) { // ends game if
					System.out.println("Game Over!");
					level.setLevelNumber(level.totalLevelNum);

					this.heroWon = true;
				}
			} // end game
		}
	}

	public void playMusic(int i) {
		sound.setFile(i);
		sound.play();
		sound.loop();
	}

	public void stopMusic() {
		sound.stop();
	}

	public void playSE(int i) {
		sound.setFile(i);
		sound.play();
	}
}
