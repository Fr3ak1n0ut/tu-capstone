package core;

import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import symbols.*;

/**
 * @author Felix Wohnhaas This class implements the core functionality of the
 *         game
 */
public class Core extends Window {

	final char idWall = '0';
	final char idIn = '1';
	final char idOut = '2';
	final char idStaticTrap = '3';
	final char idDynamicTrap = '4';
	final char idKey = '5';
	final char empty = '6';
	final char idPlayer = '7';
	final char idCollectible = '8';
	final int realWidth = getResolutionX();
	final int realHeight = getResolutionY() - 3;
	public DynamicEnemy[] dynamicEnemies;
	int enemies = 0;
	private Movement movement;

	KeyListener listener = new KeyListener(getScreen());
	public static Coordinates region;

	/**
	 * Constructor of the core
	 *
	 * @param screen
	 *            the screen to use
	 * @param resolutionX
	 *            the resolution in x direction to use
	 * @param resolutionY
	 *            the resolution in y direction to use
	 * @param filename
	 *            the filename to determine the level
	 */
	public Core(Screen screen, int resolutionX, int resolutionY, String filename) {
		super(resolutionX, resolutionY, screen);
		Game.io.loadProperties(filename);
		Game.io.createLevelData();
	}

	/**
	 * Sets region and draws level. Starts the core of the game by calling the
	 * update() Method.
	 */
	public void start() {
		getScreen().clear();
		if (region == null) {
			region = region(Game.io.getLvl());
		}
		movement = new Movement(getScreen(), getResolutionX(), getResolutionY(), this);
		drawLevel();
		update();
	}

	/**
	 * This method checks the level for entries to the Labyrith to find the
	 * right region to draw when a new game starts.
	 *
	 * @param lvl
	 *            the level to check for the right region
	 * @return the found region as a Coordinates Object
	 */
	public Coordinates region(char[][] lvl) {
		int width = Game.io.getWidth();
		int height = Game.io.getHeight();
		if (Game.io.getWidth() > realWidth || Game.io.getHeight() > realHeight) {
			for (int i = 0; i < lvl[0].length; i++) {
				if (lvl[0][i] == idIn) {
					return new Coordinates(0, i / realHeight);
				}
				if (lvl[width - 1][i] == idIn) {
					return new Coordinates(width / realWidth, i / realHeight);
				}
			}
			for (int i = 0; i < height; i++) {
				if (lvl[i][0] == idIn) {
					return new Coordinates(i / realWidth, 0);

				}
				if (lvl[i][height - 1] == idIn) {
					return new Coordinates(i / (realWidth), height / (realHeight));
				}
			}
		}
		return new Coordinates(0, 0);
	}


	/**
	 * Draws the border to separate the GUI from the game
	 */
	public void drawBorder() {
		for (int x = 0; x < realWidth; x++) {
			drawColoredString("\u2550", Color.GREEN, Color.BLACK, null, x, realHeight);
			drawColoredString(" ", Color.BLACK, Color.BLACK, null, x, realHeight + 1);
		}
		drawColoredString("Lives: ", Color.WHITE, Color.BLACK, ScreenCharacterStyle.Bold, 3, realHeight + 1);
		for (int i = 0; i < Game.player.getLives(); i++) {
			drawColoredString("\u2665", Color.RED, Color.BLACK, null, 2 * i + 10, realHeight + 1);
		}
		drawColoredString("Key: ", Color.WHITE, Color.BLACK, ScreenCharacterStyle.Bold, 20, realHeight + 1);
		if (Game.player.hasKey()) {
			drawColoredString("\u06DE", Color.CYAN, Color.BLACK, null, 26, realHeight + 1);
		}
		drawColoredString("Score: " + Game.player.getScore(), Color.WHITE, Color.BLACK, ScreenCharacterStyle.Bold, 30,
				realHeight + 1);
		getScreen().refresh();
	}

	/**
	 * Draws a specific region of the level
	 */
	public void drawLevel() {
		enemies = 0;
		dynamicEnemies = new DynamicEnemy[50];
		int levelX = 0;
		int levelY = 0;
		Entry entry = null;
		int posX = 0;
		int posY = 0;
		for (int x = 0; x < realWidth; x++) {
			for (int y = 0; y < realHeight; y++) {
				levelX = x + realWidth * region.getX();
				levelY = y + realHeight * region.getY();
				if (levelY < Game.io.getHeight() && levelX < Game.io.getWidth()) {
					switch (Game.io.getLvl()[levelX][levelY]) {
					case idWall:
						drawSymbol(new Wall(x, y));
						break;
					case idIn:
						entry = new Entry(x, y);
						posX = x;
						posY = y;
						drawSymbol(entry);
						break;
					case idOut:
						drawSymbol(new Exit(x, y));
						break;
					case idStaticTrap:
						drawSymbol(new StaticEnemy(x, y));
						break;
					case idDynamicTrap:
						DynamicEnemy enemy = new DynamicEnemy(x, y);
						drawSymbol(enemy);
						dynamicEnemies[enemies] = enemy;
						enemies++;
						break;
					case idKey:
						drawSymbol(new Keys(x, y));
						break;
					case empty:
						drawSymbol(new Path(x, y));
						break;
					case idCollectible:
						drawSymbol(new Collectible(x, y));
						break;
					default:
						drawSymbol(new Path(x, y));
						break;
					}
				}
			}
		}
		if (Game.player == null) {
			Game.player = new Player(posX, posY);
		}
		getScreen().setCursorPosition(Game.player.getPosition().getX(), Game.player.getPosition().getY());
		drawSymbol(Game.player);
		getScreen().refresh();
		getScreen().getTerminal().setCursorVisible(false);
	}

	/**
	 * Draws a character at a specific position
	 *
	 * @param symbol
	 *            the object that specifies the position and the character to
	 *            draw
	 */
	public void drawSymbol(Symbol symbol) {
		char character = symbol.getSymbol();
		Terminal.Color background = symbol.getBackgroundColor();
		Terminal.Color foreground = symbol.getForegroundColor();
		drawColoredString(character + "", foreground, background, null, symbol.getPosition().getX(),
				symbol.getPosition().getY());
	}

	/**
	 * Updates the game cycle and calls all relevant methods
	 */
	public void update() {
		drawBorder();
		boolean game = true;
		long time = System.currentTimeMillis();
		movement.setPos(0, 0);
		while (game) {
			getScreen().getTerminal().setCursorVisible(false);
			long newTime = System.currentTimeMillis();
			if (newTime - time > 1000) {
				movement.dynamicEnemy();
				time = System.currentTimeMillis();
			}
			game = movement.move();
			game = check();
			drawBorder();
		}
		System.exit(0);
	}

	/**
	 * Checks for possible events in the game - Player walks into trap - Player
	 * collects key - Player finds exit - Player finds collectible
	 *
	 * @return true if game goes on, false if current game state prevents game
	 *         from going on
	 */
	public boolean check() {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		char pos = Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight];
		switch (pos) {
		case idStaticTrap:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.died();
			break;
		case idDynamicTrap:
			for (int i = 0; i < dynamicEnemies.length; i++) {
				DynamicEnemy e = dynamicEnemies[i];
				if (e != null) {
					if (e.getPosition().getX() == playerX && e.getPosition().getY() == playerY) {
						dynamicEnemies[i] = null;
					}
				}
			}
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.died();
			break;
		case idKey:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.setHasKey(true);
			break;
		case idOut:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			getScreen().clear();
			drawColoredString("You won with a score of " + Game.player.getScore() + "!", Color.GREEN, Color.BLACK,
					ScreenCharacterStyle.Bold, realWidth / 2 - 4, realHeight / 2);
			getScreen().refresh();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException ex) {

			}
			return false;
		case idCollectible:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.addScore();
			break;
		default:
			break;
		}
		if (Game.player.getLives() <= 0) {
			getScreen().clear();
			drawColoredString("You lost!", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Bold, realWidth / 2 - 4,
					realHeight / 2);
			getScreen().refresh();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {

			}
			return false;
		}
		return true;
	}
}
