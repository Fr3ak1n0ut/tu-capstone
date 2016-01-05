package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import java.io.File;

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.KeyListener;

public class LoadMenu extends Menu {
	public LoadMenu(int resX, int resY, Screen screen) {
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu caller) {
		String[] interactables = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Zurück" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Load Menu", x, y, !(caller instanceof PauseMenu));
		if (interactionResult == -2) {
			caller.interact(this);
			return;
		}
		if (interactionResult == interactables.length) {
			caller.interact(this);
			return;
		} else {
			String filename = "save" + interactionResult + ".properties";
			File f = new File(filename);
			if (f.exists()) {
				Core core = new Core(getScreen(), getResolutionX(), getResolutionY(), filename);
				core.start();
			} else {
				drawText("Slot does not exist.", 20, getScreen().getCursorPosition().getRow());
				System.out.println("Slot doesnt exist.");
				getScreen().refresh();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.interact(caller);
				return;
			}
		}
	}
}
