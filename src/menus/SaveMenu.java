package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.screen.Screen;

import core.Game;
import core.KeyListener;

public class SaveMenu extends Menu {

	boolean end;

	public SaveMenu(int resX, int resY, Screen screen, boolean end) {
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
		this.end = end;
	}

	@Override
	public void interact(Menu caller) {
		String[] interactables = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Zurück" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Speichern", x, y, !(caller instanceof PauseMenu));
		if (interactionResult == interactables.length || interactionResult == -2) {
			caller.interact(this);
			return;
		} else {
			Game.io.saveLevel("save" + interactionResult + ".properties");
			drawText("Spiel gespeichert.", x+10, getScreen().getCursorPosition().getRow());
			getScreen().refresh();
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if (end) {
				System.exit(0);
			}
		}
		caller.interact(this);
	}
}
