package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.screen.Screen;

import core.Game;
import core.IOProperties;
import core.KeyListener;

public class SaveMenu extends Menu {

	public SaveMenu(int resX, int resY, Screen screen) {
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu menu) {
		String[] interactables = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Zurück" };
		int interactionResult = interaction(interactables, "Save Menu", 5, 5);
		int slot = (interactionResult / 2) - 1;
		if (interactionResult == 13)
		{
			menu.interact(this);
			return;
		}
		else
		{
			Game.io.saveLevel("save"+slot+".properties");
		}
		menu.interact(this);
	}
}
