package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Wall extends Symbol {
    
    public static Terminal.Color foregroundColor = Terminal.Color.BLUE;
    private static final Terminal.Color backgroundColor = Terminal.Color.WHITE;
    public static final char symbol = '\u0020';
    private static final int value = 0;
    
    public Wall(int x, int y)
    {
        super(x, y, value, foregroundColor, backgroundColor, symbol);
    }
}
