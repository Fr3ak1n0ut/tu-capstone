package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Entry extends Symbol {
    
    private static final Terminal.Color color = Terminal.Color.GREEN;
    public static final char symbol = 'I';
    private static final int value = 1;
    
    public Entry(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
    
}
