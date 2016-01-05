package core;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.screen.ScreenWriter;
import com.googlecode.lanterna.terminal.Terminal.Color;

/**
 *
 * @author Felix Wohnhaas This class is the base of all window-based classes
 */
public class Window {
	private int resolutionX;
	private int resolutionY;
	private Screen screen;
	private ScreenWriter writer;

	/**
	 * Constructs a window with the specified dimensions
	 * 
	 * @param resolutionX
	 *            the resolution in x direction
	 * @param resolutionY
	 *            the resolution in y direction
	 * @param screen
	 *            the screen the window applies to
	 */
	public Window(int resolutionX, int resolutionY, Screen screen) {
		this.resolutionX = resolutionX;
		this.resolutionY = resolutionY;
		this.screen = screen;
		screen.getTerminal().setCursorVisible(false);
		writer = new ScreenWriter(screen);
	}

	public Screen getScreen() {
		return screen;
	}

	public ScreenWriter getWriter() {
		return writer;
	}

	public int getResolutionX() {
		return resolutionX;
	}

	public int getResolutionY() {
		return resolutionY;
	}

	/**
	 * Draws a String with the specified color to the specified position with
	 * the given ScreenCharacterStyle.
	 * 
	 * @param txt
	 *            the text to draw
	 * @param foregroundColor
	 *            the color of the text
	 * @param backgroundColor
	 *            the background of the text
	 * @param style
	 *            the style to apply to the text
	 * @param x
	 *            the position in x direction
	 * @param y
	 *            the position in y direction
	 */
	public void drawColoredString(String txt, Color foregroundColor, Color backgroundColor, ScreenCharacterStyle style,
			int x, int y) {
		writer.setBackgroundColor(backgroundColor);
		writer.setForegroundColor(foregroundColor);
		if (style != null) {
			writer.drawString(x, y, txt, style);
		} else {
			writer.drawString(x, y, txt);
		}
	}

}
