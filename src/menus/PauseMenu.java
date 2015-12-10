package menus;

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.KeyListener;

public class PauseMenu extends Menu
{

	public PauseMenu(int resolutionX, int resolutionY, Screen screen)
	{
		super(resolutionX, resolutionY, screen);
		this.listener = new KeyListener(screen);
	}

	@Override
	public void interact()
	{
		for (int i = getScreen().getTerminalSize().getColumns() / 4; i < 3
				* getScreen().getTerminalSize().getColumns() / 4; i++)
		{
			for (int ii = getScreen().getTerminalSize().getRows() / 4; ii < 3
					* getScreen().getTerminalSize().getRows() / 4; ii++)
			{
				drawText(" ", i, ii);
			}
		}
		getScreen().refresh();
		String[] interactables = { "Spiel fortsetzen", "Spiel speichern", "Spiel laden", "Legende",
				"Optionen", "Spiel beenden" };
		int interactionResult = interaction(interactables, "Pause Menu", 5, 5);
		switch (interactionResult)
		{
		case 5:
			Core core = new Core(getScreen(), getResolutionX(), getResolutionY());
			core.start("level.properties");
			return;
		case 7:
			SaveMenu saveMenu = new SaveMenu(getResolutionY(), getResolutionY(), getScreen());
			saveMenu.interact();
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
