package core;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal.Color;

/**
 *
 * @author Felix Wohnhaas
 */
public class Window
{
	private int resolutionX;
	private int resolutionY;
	private Screen screen;
	private ScreenWriter writer;

	public Window(int resolutionX, int resolutionY, Screen screen)
	{
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.screen = screen;
		screen.getTerminal().setCursorVisible(false);
		writer = new ScreenWriter(screen);
	}

	public Screen getScreen()
	{
		return screen;
	}

	public ScreenWriter getWriter()
	{
		return writer;
	}

	public int getResolutionX()
	{
		return resolutionX;
	}

	public int getResolutionY()
	{
		return resolutionY;
	}

	public void drawColoredString(String txt, Color foregroundColor, Color backgroundColor,
			ScreenCharacterStyle style, int x, int y)
	{
		writer.setBackgroundColor(backgroundColor);
		writer.setForegroundColor(foregroundColor);
		if (style != null)
		{
			writer.drawString(x, y, txt, style);
		} else
		{
			writer.drawString(x, y, txt);
		}
		screen.refresh();
	}

}
