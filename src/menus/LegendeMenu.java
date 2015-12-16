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

public class LegendeMenu extends Menu
{

	public LegendeMenu(int resolutionX, int resolutionY, Screen screen)
	{
		super(resolutionX, resolutionY, screen);
		listener = new KeyListener(screen);
		// do stuff
	}

	void drawMenu(boolean clear)
	{
		if(clear)
		{
			getScreen().clear();
		}else
		{
			pauseReset();
		}
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2-7;
		System.out.println(x+","+y);
		getScreen().setCursorPosition(x-2, y+16);
		resetCursor(0, 0);
		drawColoredString("Legende", Color.BLUE, Color.BLACK, ScreenCharacterStyle.Underline, x, y);
		drawText("This is you.", x, y+2);
		drawText("This is a moving Enemy. ", x, y+4);
		drawText("This is a lazy Enemy.", x,
				y+6);
		drawText("This is a key.", x, y+8);
		drawText("This is the exit.", x,y+10);
		drawText(
				"This is the entry.",
				x,y+12);
		drawText("Those are the walls.", x,y+14);
		drawText("Back to Menu", x,y+16);
		getScreen().refresh();
		getScreen().getTerminal().setCursorVisible(false);
	}

	@Override
	public void interact(Menu caller)
	{
		drawMenu(!(caller instanceof PauseMenu));
		while (true)
		{
			Kind keyKind = listener.getKey();
			if (keyKind == Key.Kind.Enter)
			{
				caller.interact(this);
				return;
			}
		}
	}
}
