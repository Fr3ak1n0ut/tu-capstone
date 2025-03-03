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
import symbols.Collectible;
import symbols.DynamicEnemy;
import symbols.Entry;
import symbols.Exit;
import symbols.Keys;
import symbols.Player;
import symbols.StaticEnemy;

/**
 * 
 * @author Felix Wohnhaas This Menu is special because it requires almost no
 *         interaction
 */
public class LegendeMenu extends Menu {

	public LegendeMenu(int resolutionX, int resolutionY, Screen screen) {
		super(resolutionX, resolutionY, screen);
		listener = new KeyListener(screen);
	}

	/**
	 * Draws all important icons and descriptions
	 * 
	 * @param clear
	 *            clears the screen if true, doesn�t clear it if false
	 */
	void drawMenu(boolean clear) {
		if (clear) {
			getScreen().clear();
		} else {
			pauseReset();
		}
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 7;
		System.out.println(x + "," + y);
		getScreen().setCursorPosition(x - 2, y + 16);
		resetCursor(0, 0);
		drawColoredString("Legende", Color.BLUE, Color.BLACK, ScreenCharacterStyle.Underline, x, y);
		drawColoredString(Player.symbol + "", Color.WHITE, Color.BLACK, null, x, y + 2);
		drawText("This is you.", x + 2, y + 2);
		drawColoredString(DynamicEnemy.symbol + "", Color.RED, Color.BLACK, null, x, y + 4);
		drawText("This is a moving Enemy. ", x + 2, y + 4);
		drawColoredString(StaticEnemy.symbol + "", Color.RED, Color.BLACK, null, x, y + 6);
		drawText("This is a lazy Enemy.", x + 2, y + 6);
		drawColoredString(Keys.symbol + "", Color.CYAN, Color.BLACK, null, x, y + 8);
		drawText("This is a key.", x + 2, y + 8);
		drawColoredString(Collectible.symbol + "", Color.YELLOW, Color.BLACK, null, x, y + 10);
                drawText("This is a collectible.", x + 2, y + 10);
		drawColoredString(Entry.symbol + "", Color.GREEN, Color.BLACK, null, x, y + 12);
		drawText("This is the entry.", x + 2, y + 12);
		drawColoredString(Exit.symbol + "", Color.BLUE, Color.BLACK, null, x, y + 14);
		drawText("This is the exit.", x + 2, y + 14);
		drawText("Back to Menu", x, y + 16);
		getScreen().refresh();
		getScreen().getTerminal().setCursorVisible(false);
	}

	@Override
	public void interact(Menu caller) {
		drawMenu(!(caller instanceof PauseMenu));
		while (true) {
			Kind keyKind = listener.getKey(true);
			if (keyKind == Key.Kind.Enter || keyKind == Key.Kind.Escape) {
				caller.interact(this);
				return;
			}
		}
	}
}
