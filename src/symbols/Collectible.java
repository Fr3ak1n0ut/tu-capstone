package symbols;

import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author Felix Wohnhaas
 */
public class Collectible extends Symbol {
	private static final Terminal.Color color = Terminal.Color.YELLOW;
	public static final char symbol = '\u2666';
	private static final int value = 8;

	public Collectible(int x, int y) {
		super(x, y, value, color, symbol);
	}
}
