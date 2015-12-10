package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import java.io.File;

import com.googlecode.lanterna.screen.Screen;

import core.Core;
import core.Game;
import core.KeyListener;

public class LoadMenu extends Menu
{
	public LoadMenu(int resX, int resY, Screen screen)
	{
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu menu)
	{
		String[] interactables = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Zurück" };
		int interactionResult = interaction(interactables, "Load Menu",5, 5);
		int slot = (interactionResult / 2) - 1;
		if (interactionResult == 13)
		{
			menu.interact(this);
			return;
		} else
		{
			String filename = "save" + slot + ".properties";
			File f = new File(filename);
			if (f.exists())
			{
				Core core = new Core(getScreen(), getResolutionX(), getResolutionY(), filename);
				core.start();
			} else
			{
				drawText("Slot does not exist.", 20, getScreen().getCursorPosition().getRow());
				System.out.println("Slot doesnt exist.");
				getScreen().refresh();
				try
				{
					Thread.sleep(1000);
				} catch (InterruptedException e1)
				{
					e1.printStackTrace();
				}
				this.interact(menu);
				return;
			}
		}
	}
}
