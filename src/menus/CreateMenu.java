package menus;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal.Color;

import core.Core;
import core.Generate;
import core.KeyListener;
import core.TestThreading;

public class CreateMenu extends Menu {
	private int width = 250;
	private int height = 250;
	private int entries = 3;
	private int exits = 5;
	private int keys = 5;
	private int staticEnemies = 5;
	private int dynamicEnemies = 5;
	private int density = 3;
	private TestThreading thread;
	public CreateMenu(int resX, int resY, Screen screen, TestThreading thread) {
		super(resX, resY, screen);
		this.thread=thread;
		listener = new KeyListener(screen);
	}

	@Override
	public void interact(Menu caller) {
		String[] interactables = { "Width               " + width, "Height              " + height,
				"Entries             " + entries, "Exits               " + exits, "Keys                " + keys,
				"Static Enemies      " + staticEnemies, "Dynamic Enemies     " + dynamicEnemies,
				"Density             " + density, "Erstellen", "Zurück" };
		int x = getResolutionX() / 2 - 10;
		int y = getResolutionY() / 2 - 5;
		int interactionResult = interaction(interactables, "Level erstellen", x, y, !(caller instanceof PauseMenu));
		switch (interactionResult) {
		case -2:
			caller.interact(this);
		case 1:
			width = selectParameter(width, 50, 500);
			break;
		case 2:
			height = selectParameter(height, 50, 500);
			break;
		case 3:
			entries = selectParameter(entries, 1, 10);
			break;
		case 4:
			exits = selectParameter(exits, 1, 10);
			break;
		case 5:
			keys = selectParameter(keys, 1, 10);
			break;
		case 6:
			staticEnemies = selectParameter(staticEnemies, 0, 30);
			break;
		case 7:
			dynamicEnemies = selectParameter(dynamicEnemies, 0, 30);
			break;
		case 8:
			density = selectParameter(density, 1, 5);
			break;
		case 9:
			//Write data to file
			PrintWriter writer;
			try {
				writer = new PrintWriter("parameters.txt", "UTF-8");
				writer.println("Width=" + width);
				writer.println("Height=" + height);
				writer.println("NrIn=" + entries);
				writer.println("NrOut=" + exits);
				writer.println("Keys=" + keys);
				writer.println("StaticTraps=" + staticEnemies);
				writer.println("DynamicTraps=" + dynamicEnemies);
				writer.println("Density=" + density);
				writer.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			boolean load = selectAnswer(x + 15, getScreen().getCursorPosition().getRow(), "Do you want to load the level automatically?");
			Generate.main(null);
			drawColoredString("Parameter File created and level generated.", Color.WHITE, Color.BLACK, null, x+15, getScreen().getCursorPosition().getRow()+2);
			getScreen().refresh();
			getScreen().getTerminal().setCursorVisible(false);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (load) {
				Core core = new Core(getScreen(), getResolutionX(), getResolutionY(), "level.properties", thread);
				core.start();
				return;
			}
			break;
		case 10:
			caller.interact(this);
			return;
		}
		this.interact(caller);
	}

	/**
	 * Lets the player adjust the parameters of a new level
	 * 
	 * @return the value of the parameter
	 */
	private int selectParameter(int start, int minVal, int maxVal) {
		int x = getResolutionX() / 2 + 8;
		int y = getScreen().getCursorPosition().getRow();
		drawColoredString("\u25C4 ", Color.WHITE, Color.BLACK, null, x, y);
		drawColoredString("\u25BA", Color.WHITE, Color.BLACK, null, x + 6, y);
		getScreen().refresh();
		while (true) {
			drawColoredString(start + " ", Color.WHITE, Color.BLACK, null, x + 2, y);
			getScreen().refresh();
			Kind kind = listener.getKey(true);
			if (kind == Kind.ArrowLeft && start > minVal) {
				start--;
			} else if (kind == Kind.ArrowRight && start < maxVal) {
				start++;
			} else if (kind == Kind.Enter) {
				return start;
			}
			getScreen().refresh();
		}
	}
}