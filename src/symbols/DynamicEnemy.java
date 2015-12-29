package symbols;

import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author Felix Wohnhaas
 */
public class DynamicEnemy extends Symbol {

	private static final Terminal.Color color = Terminal.Color.RED;
	public static final char symbol = '\u25CF';
	private static final int value = 4;

	public DynamicEnemy(int x, int y) {
		super(x, y, value, color, symbol);
	}


}
