package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.Game;
import core.KeyListener;
import core.TestThreading;

public class SaveMenu extends Menu {

	boolean end;
	TestThreading thread;
	private Core core;
	public SaveMenu(int resX, int resY, Screen screen, boolean end, TestThreading thread, Core core) {
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
		this.end = end;
		this.thread = thread;
		this.core = core;
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
			thread.io.saveLevel("save" + interactionResult + ".properties", core);
			if (end) {
				System.exit(0);
			}
		}
		caller.interact(this);
	}
}
