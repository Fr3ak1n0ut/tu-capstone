package menus;

import com.googlecode.lanterna.screen.Screen;

import core.Game;
import core.KeyListener;

public class OptionsMenu extends Menu
{
	public OptionsMenu(int resX, int resY, Screen screen)
	{
		super(resX, resY, screen);
		listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu menu)
	{
		String[] interactables = { "Music", "Zurück" };
		int interactionResult = interaction(interactables, "Options",5,5);
		if (interactionResult == 5)
		{
			boolean musicOn = selectAnswer(30, 5, "Do you want background music?");
			if (musicOn)
			{
				System.out.println("Turning music on");
				Game.music.start();
				this.interact(menu);

			} else
			{
				Game.music.stop();
				this.interact(menu);
			}
		} else
		{
			menu.interact(this);
		}
	}
}