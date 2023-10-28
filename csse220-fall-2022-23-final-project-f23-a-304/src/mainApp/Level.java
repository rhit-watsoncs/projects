package mainApp;
import java.awt.Color;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import characters.AbstractCharacter;
import characters.EasyEnemy;
import characters.MediumEnemy;
import characters.HardEnemy;
import characters.UserCharacter;
import mainApp.Level;

/**
 * Class: Level
 * 
 * @goal Level that creates new enemies and platforms(blocks) Sets number of
 *       enemies, enemy type, location, and image
 */
public class Level {

	public int levelNumber;
	public int totalLevelNum = 4; // number of levels
	private UserCharacter hero;
	private ArrayList<AbstractCharacter> enemies;
	private ArrayList<AbstractCharacter> enemies1 = new ArrayList<AbstractCharacter>();
	private ArrayList<AbstractCharacter> enemies2 = new ArrayList<AbstractCharacter>();
	private ArrayList<AbstractCharacter> enemies3 = new ArrayList<AbstractCharacter>();
	private ArrayList<AbstractCharacter> enemies4 = new ArrayList<AbstractCharacter>();
	private ArrayList<Rectangle> blocks;

	// default lava location for each stage
	public Rectangle lava = new Rectangle(-5, 595, 805, 5);

	public Level(UserCharacter hero2) throws IOException {
		this.levelNumber = 1;
		this.hero = hero2;
		this.blocks = new ArrayList<Rectangle>();
		makeEnemies1();
		makeEnemies2();
		makeEnemies3();
		makeEnemies4();
		this.enemies = enemies1;
		
	}

	public void drawOn(Graphics2D g) {

		blocks = new ArrayList<Rectangle>();
		Graphics2D g2 = (Graphics2D) g;
		FileReader f1 = null;

		try {
			f1 = new FileReader("Level" + levelNumber + ".txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
// loops through the integers on the text files to create the level
		try (Scanner scanner = new Scanner(f1)) {
			int i = 5;
			int blockTopLeftX = 0;
			int blockTopLeftY = 0;
			int blockWidth = 0;
			int blockHeight = 0;

			while (scanner.hasNextLine()) {

				if (i % 5 == 0) {
					blockTopLeftX = scanner.nextInt();

				} else if (i % 5 == 1) {
					blockTopLeftY = scanner.nextInt();

				} else if (i % 5 == 2) {
					blockWidth = scanner.nextInt();

				} else if (i % 5 == 3) {
					blockHeight = scanner.nextInt();

				} else if (i % 5 == 4) {

					scanner.nextInt();

					Rectangle block = new Rectangle(blockTopLeftX, blockTopLeftY, blockWidth, blockHeight);
					blocks.add(block);
				}
				i++;
			}
		}
		Color color = g2.getColor();
		g2.setColor(color.gray);
		for (Rectangle block : blocks) {
			g2.fill(block);
		}

	}

	public ArrayList<AbstractCharacter> getEnemies() {
		return enemies;
	}

	// set enemy list depending on the level number
	public void setEnemy() {
		if (this.levelNumber == 1) {
			this.enemies = enemies1;
		}
		if (this.levelNumber == 2) {
			this.enemies = enemies2;
		}
		if (this.levelNumber == 3) {
			this.enemies = enemies3;
		}
		if (this.levelNumber == 4) {
			this.enemies = enemies4;
		}

	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public ArrayList<Rectangle> getBlocks() {
		return blocks;
	}

	public void setBlocks(ArrayList<Rectangle> blocks) {
		this.blocks = blocks;
	}

	public UserCharacter getHero() {
		return hero;
	}

	public void addEnemy(AbstractCharacter enemy) {
		this.enemies.add(enemy);
	}

//level 1
	private void makeEnemies1() throws IOException {
		enemies1.add(new EasyEnemy(100, 325));
		enemies1.add(new EasyEnemy(500, 225));
		enemies1.add(new EasyEnemy(200, 55));

	}

// level 2
	private void makeEnemies2() throws IOException {
		enemies2.add(new EasyEnemy(200, 325));
		enemies2.add(new MediumEnemy(100, 100));
		enemies2.add(new MediumEnemy(500, 100));

	}

// level 3
	private void makeEnemies3() throws IOException {
		enemies3.add(new HardEnemy(700, 100));
		enemies3.add(new HardEnemy(100, 100));
	}

// level 4
	private void makeEnemies4() throws IOException {
		enemies4.add(new HardEnemy(75, 100));
		enemies4.add(new HardEnemy(675, 100));
		enemies4.add(new EasyEnemy(450, 325));
		enemies4.add(new EasyEnemy(50, 325));
		enemies4.add(new MediumEnemy(300, 175));
		enemies4.add(new MediumEnemy(600, 250));

	}
}
