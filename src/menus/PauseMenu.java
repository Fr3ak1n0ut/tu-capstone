package menus;
import com.googlecode.lanterna.screen.Screen;

import core.KeyListener;

public class PauseMenu extends Menu
{
	private KeyListener listener;
	public PauseMenu(int resolutionX, int resolutionY, Screen screen)
	{
		super(resolutionX, resolutionY, screen);
		KeyListener listener = new KeyListener(getScreen());
	}
	
	@Override
	public void interact()
	{
		
	}
}
