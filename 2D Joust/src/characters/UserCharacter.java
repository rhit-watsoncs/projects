package characters;

import java.awt.Color;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Class: UserCharacter
 * @goal  Move the user character 
 * read image of user character
 * Set position to start if hit
 */
public class UserCharacter extends AbstractCharacter  {
	double slowDownMultiplier = 0.8;
	double stoppingSpeed = 0.75;
	double jumpHeight = 5;
	public int lives = 3;
	final double gravity = 0.3;
	final int xSpawn = 380;
	final int ySpawn = 460;
	Image image = ImageIO.read(new File("hero.png"));

	public UserCharacter(int x, int y, Color color, Image image) throws IOException {
		super(x, y, color, image);
	}

	public void move()  {
		if (keyLeft && keyRight || !keyLeft && !keyRight)
			xSpeed *= slowDownMultiplier;
		else if (keyLeft && !keyRight)
			xSpeed--;
		else if (keyRight && !keyLeft)
			xSpeed++;

		if ((xSpeed > 0 && xSpeed < stoppingSpeed) || (xSpeed < 0 && xSpeed > -stoppingSpeed))
			xSpeed = 0;

		if (xSpeed > topSpeed)
			xSpeed = topSpeed;
		if (xSpeed < -topSpeed)
			xSpeed = -topSpeed;

		if (keyUp) { // jumping
			ySpeed = -jumpHeight;
			keyUp = false;
		}

		if (this.x > 800) { // change 800 to actually get screen width.
			this.x = 0 - this.width; // change to actually get player width
		}

		if (this.x < -this.width) {
			this.x = 800; // change 800 to actually get screen width.
		}

		ySpeed += gravity;

		this.x += xSpeed;
		this.y += ySpeed;
	}
	
	public int getLives() {
		return this.lives;
	}

	@Override
	public void follow(int x, int y) {
		// TODO Auto-generated method stub
	}
	//reset position
	public void isHit() {
		this.setX(xSpawn); 
		this.setY(ySpawn);
		this.lives = lives - 1;
	}
	

}
