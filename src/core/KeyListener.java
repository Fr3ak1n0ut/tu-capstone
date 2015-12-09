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
		Key key = screen.readInput();
		try
		{
			Thread.sleep(50);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		if (key != null)
		{
			Kind keyKind = key.getKind();
			System.out.println(keyKind);
			return keyKind;
		} else
			return null;
	}
}
