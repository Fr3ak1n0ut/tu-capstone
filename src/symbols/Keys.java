package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Keys extends Symbol {
    
    private static final Terminal.Color color = Terminal.Color.CYAN;
    public static final char symbol = '\u06DE';
    private static final int value = 5;
    
    public Keys(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
}
