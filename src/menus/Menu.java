package menus;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.KeyListener;
import core.Window;

public abstract class Menu extends Window {

	public KeyListener listener;

	public Menu(int resolutionX, int resolutionY, Screen screen) {
		super(resolutionX, resolutionY, screen);
	}

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

	public int interaction(String[] interactables, String heading, int x, int y, boolean clear) {
		int pos = 1;
		if (clear) {
			System.out.println("Clearing");
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
			Kind keyKind = listener.getKey();
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
					if (this instanceof PauseMenu) {
						return -1;
					}
				default:
					break;
				}

			}
		}
	}

	public boolean selectAnswer(int posX, int posY, String message) {
		drawText(message, posX, posY);
		getScreen().setCursorPosition(posX - 2, posY + 2);
		resetCursor(0, 0);
		while (true) {
			drawText("Yes", posX, posY + 2);
			drawText("No", posX + 10, posY + 2);
			getScreen().refresh();
			getScreen().getTerminal().setCursorVisible(false);
			Kind keyKind = listener.getKey();
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
				default:
					break;
				}
			}
		}
	}

	public void resetCursor(int offsetX, int offsetY) {
		drawColoredString(" ", Color.BLACK, Color.BLACK, null, getScreen().getCursorPosition().getColumn(),
				getScreen().getCursorPosition().getRow());
		getScreen().setCursorPosition(getScreen().getCursorPosition().getColumn() + offsetX,
				getScreen().getCursorPosition().getRow() + offsetY);
		drawColoredString(">", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Blinking,
				getScreen().getCursorPosition().getColumn(), getScreen().getCursorPosition().getRow());
		getScreen().refresh();
	}

	public abstract void interact(Menu menu);

	public void drawText(String txt, int column, int row) {
		if (getScreen().getCursorPosition().getRow() == row
				&& getScreen().getCursorPosition().getColumn() == column - 2) {
			drawColoredString(txt, Color.GREEN, Color.BLACK, ScreenCharacterStyle.Underline, column, row);
		} else {
			drawColoredString(txt, Color.WHITE, Color.BLACK, null, column, row);
		}
	}

}
