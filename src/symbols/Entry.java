package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Entry extends Symbol {
    
    private static final Terminal.Color color = Terminal.Color.GREEN;
    private static final char symbol = '\u2691';
    private static final int value = 1;
    
    public Entry(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
    
}
