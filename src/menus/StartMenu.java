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
	public void interact(Menu menu)
	{
		String[] interactables = { "Neues Spiel starten", "Spiel laden", "Legende", "Optionen",
				"Spiel beenden" };
		int interactionResult = interaction(interactables, "Start Menu", 5, 5);
		switch (interactionResult)
		{
		case 5:
			Core core = new Core(getScreen(),getResolutionX(), getResolutionY(),"level_big_dense.properties");
			core.start();
			return;
		case 7:
			LoadMenu loadMenu = new LoadMenu(getResolutionX(), getResolutionY(), getScreen());
			loadMenu.interact(this);
			return;
		case 9:
			LegendeMenu legende = new LegendeMenu(getResolutionX(), getResolutionY(), getScreen());
			legende.interact(this);
			return;
		case 11:
			OptionsMenu options = new OptionsMenu(getResolutionX(), getResolutionY(), getScreen());
			options.interact(this);
			return;
		case 13:
			boolean save = selectAnswer(30, 13, "Wirklich beenden?");
			System.out.println(save);
			if (save)
			{
				System.exit(0);
			} else
			{
				StartMenu startMenu = new StartMenu(getResolutionX(), getResolutionY(), getScreen());
				startMenu.interact(null);
			}
			break;
		}
	}
}