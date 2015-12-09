package menus;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.KeyListener;
import core.Window;

public abstract class Menu extends Window
{

	public KeyListener listener;

	public Menu(int resolutionX, int resolutionY, Screen screen)
	{
		super(resolutionX, resolutionY, screen);
	}

	public int interaction(String[] interactables, String heading)
	{
		getScreen().clear();
		drawColoredString(heading, Color.BLUE, Color.BLACK, ScreenCharacterStyle.Underline, 5, 3);
		int startY = 5;
		int endY = 3 + 2 * interactables.length;
		getScreen().setCursorPosition(3, 5);
		resetCursor(0, 0);
		while (true)
		{
			for (int i = 0; i < interactables.length; i++)
			{
				drawText(interactables[i], 5, 2 * i + 5);
			}
			getScreen().getTerminal().setCursorVisible(false);
			Kind keyKind = listener.getKey();
			if (keyKind != null)
			{
				switch (keyKind)
				{
				case ArrowUp:
					if (getScreen().getCursorPosition().getRow() >= startY + 2)
					{
						resetCursor(0, -2);
					}
					break;
				case ArrowDown:
					if (getScreen().getCursorPosition().getRow() <= endY - 2)
					{
						resetCursor(0, 2);
					}
					break;
				case Enter:
					return getScreen().getCursorPosition().getRow();
				default:
					break;
				}

			}
			getScreen().refresh();
		}
	}

	public boolean selectAnswer(int posX, int posY, String message)
	{
		drawText(message, posX, posY);
		getScreen().setCursorPosition(posX - 2, posY + 2);
		resetCursor(0, 0);
		while (true)
		{
			drawText("Yes", posX, posY + 2);
			drawText("No", posX + 10, posY + 2);
			getScreen().getTerminal().setCursorVisible(false);
			Kind keyKind = listener.getKey();
			if (keyKind != null)
			{
				switch (keyKind)
				{
				case ArrowLeft:
					if (getScreen().getCursorPosition().getRow() == posY + 2
							&& getScreen().getCursorPosition().getColumn() > posX + 5)
					{
						resetCursor(-10, 0);
					}
					break;
				case ArrowRight:
					if (getScreen().getCursorPosition().getRow() == posY + 2
							&& getScreen().getCursorPosition().getColumn() < posX + 5)
					{
						resetCursor(10, 0);
					}
					break;
				case Enter:
					if (getScreen().getCursorPosition().getColumn() == posX - 2)
					{
						return true;
					} else
					{
						return false;
					}
				default:
					break;
				}
			}
			getScreen().refresh();
		}
	}

	public void resetCursor(int offsetX, int offsetY)
	{
		drawColoredString(" ", Color.BLACK, Color.BLACK, null, getScreen().getCursorPosition().getColumn(),
				getScreen().getCursorPosition().getRow());
		getScreen().setCursorPosition(getScreen().getCursorPosition().getColumn() + offsetX,
				getScreen().getCursorPosition().getRow() + offsetY);
		drawColoredString(">", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Blinking,
				getScreen().getCursorPosition().getColumn(), getScreen().getCursorPosition().getRow());
		getScreen().refresh();
	}


	public abstract void interact();

	public void drawText(String txt, int column, int row)
	{
		if (getScreen().getCursorPosition().getRow() == row
				&& getScreen().getCursorPosition().getColumn() == column - 2)
		{
			drawColoredString(txt, Color.GREEN, Color.BLACK, ScreenCharacterStyle.Underline, column, row);
		} else
		{
			drawColoredString(txt, Color.WHITE, Color.BLACK, null, column, row);
		}
	}

}
