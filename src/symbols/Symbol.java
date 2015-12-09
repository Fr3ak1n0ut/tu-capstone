package symbols;

import com.googlecode.lanterna.terminal.Terminal;
/**
 *
 * @author Felix Wohnhaas
 */
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.Coordinates;
import core.Window;
public class Symbol {
    
    private Coordinates position;
    private final Terminal.Color backgroundColor;
    private final Terminal.Color foregroundColor;
    private final char symbol;
    
    public Symbol(int x, int y, int value, Terminal.Color backgroundColor, Terminal.Color foregroundColor, char symbol)
    {
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
        this.symbol = symbol;
        position = new Coordinates(x, y);
    }
    public Symbol(int x, int y, int value, Terminal.Color foregroundColor, char symbol)
    {
        this.foregroundColor = foregroundColor;
        this.backgroundColor = Color.BLACK;
        this.symbol = symbol;
        position = new Coordinates(x, y);
    }
    
    public Terminal.Color getBackgroundColor()
    {
        return backgroundColor;
    }
    public Terminal.Color getForegroundColor()
	{
		return foregroundColor;
	}
    
    public char getSymbol()
    {
        return symbol;
    }
    
    public Coordinates getPosition()
    {
        return position;
    }
    @Override
    public String toString()
    {
    	// TODO Auto-generated method stub
    	return symbol+"";
    }
}
