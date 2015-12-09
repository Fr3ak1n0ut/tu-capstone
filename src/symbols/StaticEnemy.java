package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
public class StaticEnemy extends Symbol {
    
    private static final Terminal.Color color = Terminal.Color.RED;
    private static final char symbol = '\u2620';
    private static final int value = 3;
    
    public StaticEnemy(int x, int y)
    {
        super(x, y, value, color, symbol);
    }
}
