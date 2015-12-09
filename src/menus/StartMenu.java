package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.KeyListener;

public class StartMenu extends Menu
{

	public StartMenu(int resolutionX, int resolutionY, Screen screen)
	{
		super(resolutionX, resolutionY, screen);
		listener = new KeyListener(screen);
	}

	@Override
	public void interact()
	{
		String[] interactables = { "Neues Spiel starten", "Spiel laden", "Legende", "Optionen",
				"Spiel beenden" };
		int interactionResult = interaction(interactables, "Start Menu");
		switch (interactionResult)
		{
		case 5:
			Core core = new Core(getScreen(),getResolutionX(), getResolutionY());
			core.start("level.properties");
			return;
		case 7:
			LoadMenu loadMenu = new LoadMenu(getResolutionY(), getResolutionY(), getScreen());
			loadMenu.interact();
			return;
		case 9:
			HelpMenu legende = new HelpMenu(getResolutionX(), getResolutionY(), getScreen());
			legende.interact();
			return;
		case 11:
			OptionsMenu options = new OptionsMenu(getResolutionX(), getResolutionY(), getScreen());
			options.interact();
			return;
		case 13:
			boolean save = selectAnswer(30, 13, "Wirklich beenden?");
			System.out.println(save);
			if (save)
			{
				System.exit(0);
			} else
			{
				StartMenu start = new StartMenu(getResolutionX(), getResolutionY(), getScreen());
				start.interact();
			}
			break;
		}
	}
}