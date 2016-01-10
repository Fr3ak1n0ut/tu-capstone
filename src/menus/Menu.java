package menus;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.KeyListener;
import core.Window;

/**
 * 
 * @author Felix Wohnhaas This class is the base of all Menus
 */
public abstract class Menu extends Window {

	public KeyListener listener;

	/**
	 * The constructor of a Menu
	 * 
	 * @param resolutionX
	 *            the resolution of the screen in x direction
	 * @param resolutionY
	 *            the resolution of the screen in y direction
	 * @param screen
	 *            the screen to use
	 */
	public Menu(int resolutionX, int resolutionY, Screen screen) {
		super(resolutionX, resolutionY, screen);
	}

	/**
	 * Clears the inner area of the screen for the pause menu and leave the
	 * outer area to have the game shine through in the background
	 */
	public void pauseReset() {
		int left = getResolutionX() / 4;
		int right = 3 * getResolutionX() / 4;
		int up = 3 * getResolutionY() / 4;
		int down = getResolutionY() / 4;
		drawColoredString("\u255A", Color.GREEN, Color.BLACK, null, left, up);
		drawColoredString("\u255D", Color.GREEN, Color.BLACK, null, right, up);
		drawColoredString("\u2554", Color.GREEN, Color.BLACK, null, left, down);
		drawColoredString("\u2557", Color.GREEN, Color.BLACK, null, right, down);

		for (int i = left + 1; i <= right - 1; i++) {
			for (int ii = down + 1; ii < up; ii++) {
				drawText(" ", i, ii);
				drawColoredString("\u2551", Color.GREEN, Color.BLACK, null, left, ii);
				drawColoredString("\u2551", Color.GREEN, Color.BLACK, null, right, ii);
			}
			drawColoredString("\u2550", Color.GREEN, Color.BLACK, null, i, up);
			drawColoredString("\u2550", Color.GREEN, Color.BLACK, null, i, down);
		}
	}

	/**
	 * Represents all basic interaction with the menu
	 * 
	 * @param interactables
	 *            all the possible interactions with its name, as strings
	 * @param heading
	 *            the heading of the corresponding menu
	 * @param x
	 *            the initial x position
	 * @param y
	 *            the initial y position
	 * @param clear
	 *            clears the screen if true and doesn´t clear the screen if
	 *            false
	 * @return the selected interactable as integer
	 */
	public int interaction(String[] interactables, String heading, int x, int y, boolean clear) {
		int pos = 1;
		if (clear) {
			getScreen().clear();
		} else {
			pauseReset();
		}
		drawColoredString(heading, Color.BLUE, Color.BLACK, ScreenCharacterStyle.Underline, x, y - 2);
		int startY = y;
		int endY = y - 2 + 2 * interactables.length;
		getScreen().setCursorPosition(x - 2, y);
		resetCursor(0, 0);
		while (true) {
			for (int i = 0; i < interactables.length; i++) {
				drawText(interactables[i], x, 2 * i + y);
			}
			getScreen().refresh();
			getScreen().getTerminal().setCursorVisible(false);
			Kind keyKind = listener.getKey(true);
			if (keyKind != null) {
				switch (keyKind) {
				case ArrowUp:
					if (getScreen().getCursorPosition().getRow() >= startY + 2) {
						pos--;
						resetCursor(0, -2);
					}
					break;
				case ArrowDown:
					if (getScreen().getCursorPosition().getRow() <= endY - 2) {
						pos++;
						resetCursor(0, 2);
					}
					break;
				case Enter:
					return pos;
				case Escape:
					if(this instanceof StartMenu) {
						break;
					}
					if (this instanceof PauseMenu) {
						return -1;
					} else {
						return -2;
					}
				default:
					break;
				}

			}
		}
	}

	/**
	 * Specifies the advanced yes/no selection after a main component has been
	 * selected by the interaction() Method
	 * 
	 * @param posX
	 *            the x position to draw the selection
	 * @param posY
	 *            the y position to draw the selection
	 * @param message
	 *            the message to display
	 * @return true if yes is selected, no in any other case
	 */
	public boolean selectAnswer(int posX, int posY, String message) {
		drawText(message, posX, posY);
		getScreen().setCursorPosition(posX - 2, posY + 2);
		resetCursor(0, 0);
		while (true) {
			drawText("Ja", posX, posY + 2);
			drawText("Nein", posX + 10, posY + 2);
			getScreen().refresh();
			getScreen().getTerminal().setCursorVisible(false);
			Kind keyKind = listener.getKey(true);
			if (keyKind != null) {
				switch (keyKind) {
				case ArrowLeft:
					if (getScreen().getCursorPosition().getRow() == posY + 2
							&& getScreen().getCursorPosition().getColumn() > posX + 5) {
						resetCursor(-10, 0);
					}
					break;
				case ArrowRight:
					if (getScreen().getCursorPosition().getRow() == posY + 2
							&& getScreen().getCursorPosition().getColumn() < posX + 5) {
						resetCursor(10, 0);
					}
					break;
				case Enter:
					if (getScreen().getCursorPosition().getColumn() == posX - 2) {
						return true;
					} else {
						return false;
					}
				case Escape:
					this.interact(null);
				default:
					break;
				}
			}
		}
	}

	/**
	 * Resets the cursor to a new position with the given offset
	 * 
	 * @param offsetX
	 *            the offset in x direction
	 * @param offsetY
	 *            the offset in y direction
	 */
	public void resetCursor(int offsetX, int offsetY) {
		drawColoredString(" ", Color.BLACK, Color.BLACK, null, getScreen().getCursorPosition().getColumn(),
				getScreen().getCursorPosition().getRow());
		getScreen().setCursorPosition(getScreen().getCursorPosition().getColumn() + offsetX,
				getScreen().getCursorPosition().getRow() + offsetY);
		drawColoredString(">", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Blinking,
				getScreen().getCursorPosition().getColumn(), getScreen().getCursorPosition().getRow());
		getScreen().refresh();
	}

	/**
	 * Defines the method for all subclasses of this class to use when using
	 * interaction
	 * 
	 * @param menu
	 */
	public abstract void interact(Menu menu);

	/**
	 * Draws text to the specified position and underlines it if the cursor has
	 * the same height
	 * 
	 * @param txt
	 *            the text to draw
	 * @param x
	 *            the position in x direction
	 * @param y
	 *            the position in y direction
	 */
	public void drawText(String txt, int x, int y) {
		if (getScreen().getCursorPosition().getRow() == y && getScreen().getCursorPosition().getColumn() == x - 2) {
			drawColoredString(txt, Color.GREEN, Color.BLACK, ScreenCharacterStyle.Underline, x, y);
		} else {
			drawColoredString(txt, Color.WHITE, Color.BLACK, null, x, y);
		}
	}

}
