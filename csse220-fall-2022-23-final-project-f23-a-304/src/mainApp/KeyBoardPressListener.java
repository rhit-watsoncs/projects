package mainApp;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.Timer;
import characters.UserCharacter;

/**
 * Class: KeyBoardPressListener
 * @Goal Establish keyboard movements and transitions
 * Goes to the next level if U is pressed. Goes back if D is pressed
 * Arrow keys for movement
 * Level does not exist exception
 */
public class KeyBoardPressListener implements KeyListener {
	private UserCharacter hero;
	private Level level;

	public KeyBoardPressListener(JFrame frame, GameComponent gameComponent, Level level, Timer time) {
		this.hero = level.getHero();
		this.level = level;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			hero.keyUp = true;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			hero.keyDown = true;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			hero.keyRight = true;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			hero.keyLeft = true;
		
		//next level using key "U"
		if (e.getKeyCode() == KeyEvent.VK_U) {
			try {
				if (level.getLevelNumber() < 4) {
					System.out.println("---------------");
					level.setLevelNumber(level.getLevelNumber() + 1);
					System.out.println("Level: " + level.getLevelNumber());
				}
				if (level.getLevelNumber() >= 5) {
					throw new LevelDoesntExistException();
				}
				level.setEnemy();
			}

			catch (LevelDoesntExistException e1) {
				e1.getMessage();
				System.exit(0);
			} // exit
		}
		//previous level using key "D"
		if (e.getKeyCode() == KeyEvent.VK_D) {
			try {
				if (level.getLevelNumber() > 1) {
					System.out.println("---------------");
					level.setLevelNumber(level.getLevelNumber() - 1);
					System.out.println("Level: " + level.getLevelNumber());
				} else if (level.getLevelNumber() <= 1) {
					throw new LevelDoesntExistException();
				}
				level.setEnemy();
			} catch (LevelDoesntExistException e1) {
				e1.getMessage();
				System.exit(0);
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_UP)
			hero.keyUp = false;
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			hero.keyDown = false;
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			hero.keyRight = false;
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			hero.keyLeft = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}