package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class Exit extends Symbol{
    
    private static final Terminal.Color color = Terminal.Color.BLUE;
    public static final char symbol = 'O';
    private static final int value = 2;
    
    public Exit(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
}
