package mainApp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Class: GameAdvancedListener
 * 
 * @goal Update the state of the game and redraw the screen
 *
 */
public class GameAdvanceListener implements ActionListener {

	private GameComponent gameComponent;

	public GameAdvanceListener(GameComponent gameComponent) throws IOException {
		this.gameComponent = gameComponent;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			advanceOneTick();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void advanceOneTick() throws IOException {
		this.gameComponent.updateState();
		this.gameComponent.drawScreen();
	}
}