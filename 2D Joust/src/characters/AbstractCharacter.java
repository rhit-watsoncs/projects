package characters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

/**
 * Class: AbstractCharacter
 * @goal  Create bounds of characters, set speed, gravity,
 * 	draw characters, get and set position
 *
 */
public abstract class AbstractCharacter {
	protected int x;
	protected int y;
	protected Color color;
	private int height;
	protected int width;

	public boolean keyLeft;
	public boolean keyUp;
	public boolean keyRight;
	public boolean keyDown;
	public boolean isEgg = false;
	Image image;

	double xSpeed;
	double ySpeed;

	double topSpeed = 5;

	// protected GameComponent gameComponent;

	public AbstractCharacter(int x, int y, Color color, Image image) {
		this.x = x;
		// this.ySpeed = 0;
		this.y = y;
		this.width = 25;
		this.height = 35;
		this.color = color;
		this.image = image;
	}

	public void drawOn(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(this.color);
		g2.fillRect(x, y, width, height);
		g2.drawImage(image, x, y, null);

	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int x) {
		this.x = x;

	}

	public void setY(int y) {
		this.y = y;
	}

	public double getYSpeed() {
		return ySpeed;
	}

	public double getXSpeed() {
		return xSpeed;
	}

	public void setYSpeed(double b) {
		this.ySpeed = b;
	}

	public void setXSpeed(double c) {
		this.xSpeed = c;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Rectangle2D.Double getBoundingBox() {
		return new Rectangle2D.Double(this.x, this.y, getWidth(), getHeight());
	}

	public boolean insideBox(Rectangle b) {
		return b.intersects(x, y, width, height);
	}

	public boolean overlaps(AbstractCharacter other) {
		return getBoundingBox().intersects(other.getBoundingBox());
	}

	public abstract void move();

	public abstract void follow(int x, int y);
}
