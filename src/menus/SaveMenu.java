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
		int interactionResult = interaction(interactables, "Save Menu", x, y, !(caller instanceof PauseMenu));
		if (interactionResult == interactables.length) {
			caller.interact(this);
			return;
		} else {
			Game.io.saveLevel("save" + interactionResult + ".properties");
			if(end)
			{
				System.exit(0);
			}
		}
		caller.interact(this);
	}
}
