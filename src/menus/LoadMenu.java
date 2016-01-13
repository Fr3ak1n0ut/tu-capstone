package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import java.io.File;

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.KeyListener;
import core.TestThreading;

public class LoadMenu extends Menu {
	private TestThreading thread;
	public LoadMenu(int resX, int resY, Screen screen,TestThreading thread) {
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
		this.thread = thread;
	}

	@Override
	public void interact(Menu caller) {
		String[] interactables = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Zurück" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Laden", x, y, !(caller instanceof PauseMenu));
		if (interactionResult == interactables.length || interactionResult == -2) {
			caller.interact(this);
			return;
		} else {
			String filename = "save" + interactionResult + ".properties";
			File f = new File(filename);
			if (f.exists()) {
				Core core = new Core(getScreen(), getResolutionX(), getResolutionY(), filename, thread);
				core.start();
			} else {
				drawText("Slot existiert nicht.", x+10, getScreen().getCursorPosition().getRow());
				getScreen().refresh();
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				this.interact(caller);
				return;
			}
		}
	}
}
