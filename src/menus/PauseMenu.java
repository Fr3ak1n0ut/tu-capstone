package menus;

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.KeyListener;

public class PauseMenu extends Menu {
	Core core;

	public PauseMenu(int resolutionX, int resolutionY, Screen screen, Core core) {
		super(resolutionX, resolutionY, screen);
		this.listener = new KeyListener(screen);
		this.core = core;
	}

	@Override
	public void interact(Menu menu) {
		for (int i = getScreen().getTerminalSize().getColumns() / 4; i < 3 * getScreen().getTerminalSize().getColumns()
				/ 4; i++) {
			for (int ii = getScreen().getTerminalSize().getRows() / 4; ii < 3 * getScreen().getTerminalSize().getRows()
					/ 4; ii++) {
				drawText(" ", i, ii);
			}
		}
		getScreen().refresh();
		String[] interactables = { "Spiel fortsetzen", "Spiel speichern", "Spiel laden", "Legende", "Optionen",
				"Spiel beenden" };
		int interactionResult = interaction(interactables, "Pause Menu", 5, 5);
		switch (interactionResult) {
		case 5:
			core.start();
			return;
		case 7:
			SaveMenu saveMenu = new SaveMenu(getResolutionX(), getResolutionY(), getScreen());
			saveMenu.interact(this);
			return;
		case 9:
			LoadMenu loadMenu = new LoadMenu(getResolutionX(), getResolutionY(), getScreen());
			loadMenu.interact(this);
			return;
		case 11:
			LegendeMenu legende = new LegendeMenu(getResolutionX(), getResolutionY(), getScreen());
			legende.interact(this);
			return;
		case 13:
			OptionsMenu options = new OptionsMenu(getResolutionX(), getResolutionY(), getScreen());
			options.interact(this);
			return;
		case 15:
			boolean save = selectAnswer(30, 13, "Wirklich beenden?");
			System.out.println(save);
			if (save) {
				System.exit(0);
			} else {
				this.interact(menu);
			}
			return;
		}
	}

}
