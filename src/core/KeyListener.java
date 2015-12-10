package core;

/**
*
* @author Felix Wohnhaas
*/
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;

public class KeyListener
{
	private Screen screen;

	public KeyListener(Screen screen)
	{
		this.screen = screen;
	}

	public Kind getKey()
	{
		Key key = null;
		while (key == null)
		{
			key = screen.readInput();
		}
		Kind keyKind = null;
		keyKind = key.getKind();
		return keyKind;
	}
}
