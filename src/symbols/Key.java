package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Key extends Symbol {
    
    private static final Terminal.Color color = Terminal.Color.YELLOW;
    private static final char symbol = '\u2628';
    private static final int value = 5;
    
    public Key(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
}
