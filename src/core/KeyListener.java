package core;

/**
*
* @author Felix Wohnhaas
*/
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;

public class KeyListener {
	private Screen screen;

	public KeyListener(Screen screen) {
		this.screen = screen;
	}

	/**
	 * Captures a key input and returns it
	 * 
	 * @return the kind of the key that has been put in
	 */
	public Kind getKey() {
		Key key = null;
		key = screen.readInput();
		screen.getTerminal().setCursorVisible(false);
		Kind keyKind = null;
		if (key != null) {
			keyKind = key.getKind();

		}
		return keyKind;
	}
}
