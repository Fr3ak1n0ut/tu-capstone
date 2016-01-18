package menus;

import com.googlecode.lanterna.screen.Screen;
import core.Core;
import core.KeyListener;

/**
 * 
 * @author Felix Wohnhaas
 *
 */
public class PauseMenu extends Menu {
	Core core;

	public PauseMenu(int resolutionX, int resolutionY, Screen screen, Core core) {
		super(resolutionX, resolutionY, screen);
		this.listener = new KeyListener(screen);
		this.core = core;
	}

	@Override
	public void interact(Menu caller) {

		getScreen().refresh();
		String[] interactables = { "Spiel fortsetzen", "Spiel speichern", "Spiel laden", "Legende", "Optionen", "Spiel beenden" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Pause Menu", x, y, false);
		switch (interactionResult) {
		case -1:
			core.start();
			return;
		case 1:
			core.start();
			return;
		case 2:
			SaveMenu saveMenu = new SaveMenu(getResolutionX(), getResolutionY(), getScreen(), false);
			saveMenu.interact(this);
			return;
		case 3:
			LoadMenu loadMenu = new LoadMenu(getResolutionX(), getResolutionY(), getScreen());
			loadMenu.interact(this);
			return;
		case 4:
			LegendeMenu legende = new LegendeMenu(getResolutionX(), getResolutionY(), getScreen());
			legende.interact(this);
			return;
		case 5:
			OptionsMenu options = new OptionsMenu(getResolutionX(), getResolutionY(), getScreen());
			options.interact(this);
			return;
		case 6:
			boolean save = selectAnswer(x + 15, y + 10, "Speichern vor dem Beenden?");
			if (save) {
				saveMenu = new SaveMenu(getResolutionX(), getResolutionY(), getScreen(), true);
				saveMenu.interact(this);
				System.exit(0);
			} else {
				System.exit(0);
			}
			return;
		}
	}

}
