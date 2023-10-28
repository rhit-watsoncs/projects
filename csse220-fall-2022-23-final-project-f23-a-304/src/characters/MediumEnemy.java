package characters;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Class: MediumEnemy
 * @goal  Create medium enemy
 * Set speed and time interval of egg state
 * Set color to yellow if enemy
 * set color to pink if egg
 */
public class MediumEnemy extends AbstractCharacter {
	int totalTime = 300;
	int recoveryTime = totalTime;
	double topSpeed = -6;
	final double gravity = 0.3;
	Image image;

	public MediumEnemy(int x, int y) throws IOException {
		super(x, y, Color.YELLOW, ImageIO.read(new File("medium.png")));
	}

	// @Override
	public void move() {
		if (isEgg) {
			recoveryTime--;
			if (recoveryTime <= 0) {
				isEgg = false;
				recoveryTime = totalTime;
			}
			color = Color.pink;
			xSpeed = 0;
			ySpeed += gravity;
			this.x += xSpeed;
			this.y += ySpeed;

		} else {
			color = Color.yellow;
			this.xSpeed--;

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

			//System.out.println(this.y + " " + this.x);
		}
	}

	@Override
	public void follow(int x, int y) {
		// TODO Auto-generated method stub
	}
}
