package menus;

import com.googlecode.lanterna.screen.Screen;

import core.Game;
import core.KeyListener;

public class OptionsMenu extends Menu
{
	public OptionsMenu(int resX, int resY, Screen screen)
	{
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
	}

	@Override
	public void interact()
	{
		String[] interactables = { "Music", "Zurück" };
		int interactionResult = interaction(interactables, "Options");
		if (interactionResult == 5)
		{
			boolean musicOn = selectAnswer(30, 5, "Do you want background music?");
			if (musicOn)
			{
				System.out.println("Turning music on");
				Game.music.start();
				this.interact();

			} else
			{
				Game.music.stop();
				this.interact();
			}
		} else
		{
			StartMenu menu = new StartMenu(getResolutionX(), getResolutionY(), getScreen());
			menu.interact();
		}
	}
}