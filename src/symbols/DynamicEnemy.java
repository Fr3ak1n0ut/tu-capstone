package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class DynamicEnemy extends Symbol {
    
    private static final Terminal.Color color = Terminal.Color.RED;
    private static final char symbol = '\u2620';
    private static final int value = 4;
    
    public DynamicEnemy(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
}
