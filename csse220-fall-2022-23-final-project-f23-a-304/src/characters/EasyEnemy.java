package characters;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class: EasyEnemy
 * @goal  Create easy enemy
 * Set speed and time interval of egg state
 * Set color to green if enemy
 * set color to pink if egg
 */
public class EasyEnemy extends AbstractCharacter {
	int totalTime = 300;
	int recoveryTime = totalTime;
	final double gravity = 0.3;
	double topSpeed = 3;
	Image image;

	public EasyEnemy(int x, int y) throws IOException  {
		super(x, y, Color.GREEN, ImageIO.read(new File("easy.png")));
	}

	public void move() {
		if (isEgg) {
			recoveryTime--;
			if (recoveryTime <= 0) {
				isEgg = false;
				color = Color.GREEN;
				recoveryTime = totalTime;
			}

			color = Color.pink;
			xSpeed = 0;
			ySpeed += gravity;
			this.x += xSpeed;
			this.y += ySpeed;
		} else {
			color = Color.green;
			this.xSpeed++;

			if (xSpeed > topSpeed)
				xSpeed = topSpeed;
			if (xSpeed < -topSpeed)
				xSpeed = -topSpeed;

			if (this.x > 800) { // change 800 to actually get screen width.
				this.x = 0 - this.width; // change to actually get player width
			}

			if (this.x < -this.width) {
				this.x = 800; // change 800 to actually get screen width.
			}

			this.x += xSpeed;
			this.y += ySpeed;
		}
	}

	@Override
	public void follow(int x, int y) {

	}
}
