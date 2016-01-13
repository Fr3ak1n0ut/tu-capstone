package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.KeyListener;
import core.TestThreading;

public class StartMenu extends Menu {

	private boolean multiplayer = true;
	private TestThreading thread;

	public StartMenu(int resolutionX, int resolutionY, Screen screen) {
		super(resolutionX, resolutionY, screen);
		listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu caller) {
		String[] interactables = { "Neues Spiel starten", "Level erstellen", "Spiel laden", "Legende", "Optionen",
				"Spiel beenden" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Start", x, y, true);
		switch (interactionResult) {
		case 1:
			// Edit to try other levels
			getScreen().clear();
			if (multiplayer) {
				TestThreading t1 = new TestThreading(getScreen(), getResolutionX(), getResolutionY(), 0,
						getResolutionX() / 2-1, 0, getResolutionY()-3);
				TestThreading t2 = new TestThreading(getScreen(), getResolutionX(), getResolutionY(),
						getResolutionX() / 2+1, getResolutionX(), 0, getResolutionY()-3);
				t1.start();
				t2.start();
			} else {
				TestThreading t1 = new TestThreading(getScreen(), getResolutionX(), getResolutionY(), 0,
						getResolutionX(), 0, getResolutionY()-3);
				t1.start();
			}
			return;
		case 2:
			CreateMenu creater = new CreateMenu(getResolutionX(), getResolutionY(), getScreen(), thread);
			creater.interact(this);
			return;
		case 3:
			LoadMenu loadMenu = new LoadMenu(getResolutionX(), getResolutionY(), getScreen(), thread);
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
			boolean end = selectAnswer(x + 20, y + 8, "Wirklich beenden?");
			System.out.println(end);
			if (end) {
				System.exit(0);
			} else {
				StartMenu startMenu = new StartMenu(getResolutionX(), getResolutionY(), getScreen());
				startMenu.interact(null);
			}
			break;
		}
	}
}