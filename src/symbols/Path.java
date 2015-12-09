package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Path extends Symbol {
    private static final Terminal.Color color = Terminal.Color.BLACK;
    private static final char symbol = '\u0020';
    private static final int value = 6;
    
    public Path(int x, int y)
    {
        super(x, y, value, color, color, symbol);
    }
}





