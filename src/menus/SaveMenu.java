package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.screen.Screen;
import core.IOProperties;
import core.KeyListener;

public class SaveMenu extends Menu
{

	public SaveMenu(int resX, int resY, Screen screen)
	{
		super(resX, resY, screen);
		this.listener = new KeyListener(screen);
	}

	@Override
	public void interact()
	{
		String[] interactables = { "Slot 1", "Slot 2", "Slot 3", "Slot 4", "Zurück" };
		int interactionResult = interaction(interactables, "Save Menu",5,5);
		IOProperties io = new IOProperties();
		switch (interactionResult)
		{
		case 5:
			io.saveLevel("save1.properties");
			break;
		case 7:
			io.saveLevel("save2.properties");
			break;
		case 9:
			io.saveLevel("save3.properties");
			break;
		case 11:
			io.saveLevel("save4.properties");
			break;
		case 13:
			StartMenu start = new StartMenu(getResolutionX(), getResolutionY(), getScreen());
			start.interact();
			break;
		}
	}
}
