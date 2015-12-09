package symbols;

import com.googlecode.lanterna.terminal.Terminal;

/**
 *
 * @author Felix Wohnhaas
 */
public class Player extends Symbol
{

	private final static Terminal.Color color = Terminal.Color.CYAN;
	private static final int value = 7;
	private static final char symbol = '~';

	public Player(int x, int y)
	{
		super(x, y, value, color, symbol);
	}
}
