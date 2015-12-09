package menus;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.KeyListener;

public class HelpMenu extends Menu
{

	public HelpMenu(int resolutionX, int resolutionY, Screen screen)
	{
		super(resolutionX, resolutionY, screen);
		listener = new KeyListener(screen);
		// do stuff
	}

	void drawMenu()
	{
		getScreen().clear();
		getScreen().setCursorPosition(3, 19);
		resetCursor(0, 0);
		drawColoredString("Legende", Color.BLUE, Color.BLACK, ScreenCharacterStyle.Underline, 5, 3);
		drawText("This is you. Yes, all of them", 7, 5);
		drawText("This is a moving Enemy. Caution! He will hurt you as soon as he can.", 7, 7);
		drawText("This is a lazy Enemy. Caution! He is very lazy but he will hurt you if you touch him.", 7,
				9);
		drawText("This is a key. Collect one of them and find the exit before the enemies kill you!", 7, 11);
		drawText("This is the exit. Find it, as fast as possible!", 7, 13);
		drawText(
				"This is the entry. As soon as you enter a Labyrinth, you will not be able to exit through it!",
				7, 15);
		drawText("Those are the walls. You cant walk onto them.", 7, 17);
		drawText("Back to Menu", 5, 19);
		getScreen().refresh();
		getScreen().getTerminal().setCursorVisible(false);
	}

	@Override
	public void interact()
	{
		drawMenu();
		while (true)
		{
			Kind keyKind = listener.getKey();
			if (keyKind == Key.Kind.Enter)
			{
				getScreen().clear();
				StartMenu menu = new StartMenu(getResolutionX(), getResolutionY(), getScreen());
				menu.interact();
				return;
			}
		}
	}
}
