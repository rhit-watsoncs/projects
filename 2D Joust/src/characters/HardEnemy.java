package characters;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class: HardEnemy
 * @goal  Create hard enemy
 * Set speed and time interval of egg state
 * Set color to red if enemy
 * set color to pink if egg
 * follow function
 */
public class HardEnemy extends AbstractCharacter {

	private double topSpeed = 2;
	int totalTime = 300;
	int recoveryTime = totalTime;
	final double gravity = 0.3;
	final int trackingDistance = 10;
	Image image;

	public HardEnemy(int x, int y) throws IOException {
		super(x, y, Color.RED, ImageIO.read(new File("hard.png")));
	}

	@Override
	public void follow(int x, int y) {
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
			color = Color.RED;
			if (this.x < x - trackingDistance)
				xSpeed++;
			else if (this.x > x + trackingDistance)
				xSpeed--;
			else
				xSpeed = 0;

			if (this.y < y - trackingDistance)
				ySpeed++;
			else if (this.y > y + trackingDistance)
				ySpeed--;
			else
				ySpeed = 0;

			if (xSpeed > topSpeed)
				xSpeed = topSpeed;
			if (xSpeed < -topSpeed)
				xSpeed = -topSpeed;

			if (ySpeed > topSpeed)
				ySpeed = topSpeed;
			if (ySpeed < -topSpeed)
				ySpeed = -topSpeed;

			this.x += xSpeed;
			this.y += ySpeed;
		}
	}

	@Override
	public void move() {
		
	}
}
