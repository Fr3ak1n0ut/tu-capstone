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
	public void interact(Menu caller)
	{
		String[] interactables = { "Music", "Zurück" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Options",x,y, !(caller instanceof PauseMenu));
		if (interactionResult == 1)
		{
			boolean musicOn = selectAnswer(x+20, y, "Do you want background music?");
			if (musicOn)
			{
				System.out.println("Turning music on");
				Game.music.start();
				this.interact(caller);

			} else
			{
				Game.music.stop();
				this.interact(caller);
			}
		} else
		{
			caller.interact(this);
		}
	}
}