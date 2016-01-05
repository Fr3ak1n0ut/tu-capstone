package menus;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.Game;
import core.KeyListener;
import symbols.Player;
import symbols.Wall;

public class OptionsMenu extends Menu {
	public OptionsMenu(int resX, int resY, Screen screen) {
		super(resX, resY, screen);
		listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu caller) {
		String[] interactables = { "Music", "Wall Color", "Zurück" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Options", x, y, !(caller instanceof PauseMenu));
		if (interactionResult == 1) {
			boolean musicOn = selectAnswer(x + 15, y, "Do you want background music?");
			if (musicOn) {
				System.out.println("Turning music on");
				Game.music.start();
				this.interact(caller);

			} else {
				Game.music.stop();
				this.interact(caller);
			}
		} else if (interactionResult == 2) {
			// color
			System.out.println("Color");
			Wall.foregroundColor = wallColor();
			this.interact(caller);
		} else {
			caller.interact(this);
		}
	}

	/**
	 * Lets the player decide which wall color to use in the game.
	 * 
	 * @return the color to use
	 */
	private Color wallColor() {
		int position = 0;
		Color[] colors = { Color.BLUE, Color.CYAN, Color.GREEN, Color.MAGENTA, Color.WHITE, Color.YELLOW, Color.RED };
		int x = getResolutionX() / 2 + 5;
		int y = getResolutionY() / 2 - 3;
		drawColoredString("\u25C4", Color.WHITE, Color.BLACK, null, x, y);
		drawColoredString("\u25BA", Color.WHITE, Color.BLACK, null, x + 4, y);
		getScreen().refresh();
		while (true) {
			drawColoredString(Player.symbol + "", colors[position], Color.BLACK, null, x + 2, y);
			Kind kind = listener.getKey();
			if (kind == Kind.ArrowLeft) {
				position--;
				if (position == -1) {
					position += colors.length;
				}
				System.out.println(position);

			} else if (kind == Kind.ArrowRight) {
				position++;
				position %= colors.length;
				System.out.println(position);
			} else if (kind == Kind.Enter) {
				return colors[position];
			}
			getScreen().refresh();
		}
	}
}