package core;

import java.util.Random;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import menus.PauseMenu;
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
	private int minX;
	private int maxX;
	private int minY;
	private int maxY;
	public DynamicEnemy[] dynamicEnemies;
	int enemies = 0;
	private TestThreading thread;
	KeyListener listener = new KeyListener(getScreen());
	public Coordinates region;

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
	public Core(Screen screen, int resolutionX, int resolutionY, String filename, TestThreading thread, int minX,
			int maxX, int minY, int maxY) {
		super(resolutionX, resolutionY, screen);
		this.thread = thread;
		thread.io.loadProperties(filename, this);
		thread.io.createLevelData();
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
	}

	public Core(Screen screen, int resolutionX, int resolutionY, String filename, TestThreading thread) {
		super(resolutionX, resolutionY, screen);
		this.thread = thread;
		thread.io.loadProperties(filename, this);
		thread.io.createLevelData();
		this.minX = getResolutionX() / 2;
		this.maxX = getResolutionX();
		this.minY = 0;
		this.maxY = getResolutionY() - 3;
	}

	/**
	 * Sets region and draws level. Starts the core of the game by calling the
	 * update() Method.
	 */
	public void start() {
		// getScreen().clear();
		if (region == null) {
			region = region(thread.io.getLvl());
		}
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
		int width = thread.io.getWidth();
		int height = thread.io.getHeight();
		if (thread.io.getWidth() > maxX || thread.io.getHeight() > maxY) {
			for (int i = minX; i < lvl[0].length; i++) {
				if (lvl[0][i] == idIn) {
					return new Coordinates(0, i / maxY);
				}
				if (lvl[width - 1][i] == idIn) {
					return new Coordinates(width / maxX, i / maxY);
				}
			}
			for (int i = minY; i < height; i++) {
				if (lvl[i][0] == idIn) {
					return new Coordinates(i / maxX, 0);

				}
				if (lvl[i][height - 1] == idIn) {
					return new Coordinates(i / (maxX), height / (maxY));
				}
			}
		}
		return new Coordinates(0, 0);
	}

	/**
	 * Iterates through all dynamic enemies
	 */
	public void dynamicEnemy() {
		for (int i = 0; i < dynamicEnemies.length; i++) {
			if (dynamicEnemies[i] != null) {
				dynamicEnemies[i] = moveEnemy(dynamicEnemies[i]);
				drawSymbol(dynamicEnemies[i]);
			}
		}
		getScreen().refresh();
		getScreen().getTerminal().setCursorVisible(false);
	}

	/**
	 * Moves the dynamic enemies
	 * 
	 * @param enemy
	 *            the enemy to move
	 * @return the new, moved enemy
	 */
	public DynamicEnemy moveEnemy(DynamicEnemy enemy) {
		Random rand = new Random();
		int direction = rand.nextInt(4);
		Coordinates pos = enemy.getPosition();
		int offsetX = 0;
		int offsetY = 0;
		switch (direction) {
		case 0:
			offsetX = -1;
			break;
		case 1:
			offsetX = 1;
			break;
		case 2:
			offsetY = -1;
			break;
		case 3:
			offsetY = 1;
			break;
		}
		int x = pos.getX() + region.getX() * maxX;
		int y = pos.getY() + region.getY() * maxY;
		if (x < thread.io.getWidth() && y < thread.io.getHeight()) {
			char lvlPos = thread.io.getLvl()[x + offsetX][y + offsetY];
			if (lvlPos != idIn && lvlPos != idOut && lvlPos != idWall) {
				thread.io.getLvl()[x][y] = empty;
				thread.io.getLvl()[x + offsetX][y + offsetY] = idDynamicTrap;
				drawColoredString(" ", Color.BLACK, Color.BLACK, null, pos.getX(), pos.getY());
				return new DynamicEnemy(pos.getX() + offsetX, pos.getY() + offsetY);
			}
		}
		return enemy;
	}

	/**
	 * Draws the border to separate the GUI from the game
	 */
	public void drawBorder() {
		for (int x = minX; x < maxX; x++) {
			drawColoredString("\u2550", Color.GREEN, Color.BLACK, null, x, maxY);
			drawColoredString(" ", Color.BLACK, Color.BLACK, null, x, maxY + 1);
		}
		for (int i = minX; i < thread.player.getLives(); i++) {
			drawColoredString("\u2665", Color.RED, Color.BLACK, null, 2 * i + 3, maxY + 1);
		}
		if (thread.player.hasKey()) {
			drawColoredString("\u06DE", Color.CYAN, Color.BLACK, null, minX + 15, maxY + 1);
		}
		getScreen().refresh();
	}

	/**
	 * Draws a specific region of the level
	 */
	public void drawLevel() {
		enemies = 0;
		dynamicEnemies = new DynamicEnemy[50];
		int levelX = minX;
		int levelY = minY;
		Entry entry = null;
		int posX = 0;
		int posY = 0;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				levelX = (x - minX) + (maxX - minX) * region.getX();
				levelY = (y - minY) + (maxY - minY) * region.getY();
				if (levelY < thread.io.getHeight() && levelX < thread.io.getWidth()) {
					switch (thread.io.getLvl()[levelX][levelY]) {
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
		if (thread.player == null) {
			thread.player = new Player(posX, posY);
		}
		getScreen().setCursorPosition(thread.player.getPosition().getX(), thread.player.getPosition().getY());
		drawSymbol(thread.player);
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
		setPos(0, 0);
		while (game) {
			getScreen().getTerminal().setCursorVisible(false);
			long newTime = System.currentTimeMillis();
			if (newTime - time > 1000) {
				dynamicEnemy();
				time = System.currentTimeMillis();
			}
			game = move(false);
			game = check();
			drawBorder();
		}
		System.exit(0);
	}

	/**
	 * Sets the position of the player after checking for possible obstacles
	 * 
	 * @param x
	 *            the amount of units to move in x direction
	 * @param y
	 *            the amount of units to move in y direction
	 */
	private void setPos(int x, int y) {
		int playerX = thread.player.getPosition().getX();
		int playerY = thread.player.getPosition().getY();
		Terminal terminal = getScreen().getTerminal();
		boolean redraw = false;
		if (playerX == minX && x == -1) {
			char obstacle = thread.io.getLvl()[region.getX() * (maxX - minX) - 1][playerY - minY
					+ region.getY() * (maxY - minY)];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setX(region.getX() - 1);
				thread.player.getPosition().setX(maxX - 1);
				x = 0;
				playerX = maxX - 1;
				redraw = true;
			} else {
				return;
			}
		} else if (playerX == maxX - 1 && x == 1) {
			char obstacle = thread.io.getLvl()[(maxX - minX) + region.getX() * (maxX - minX)][playerY - minY
					+ region.getY() * (maxY - minY)];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setX(region.getX() + 1);
				thread.player.getPosition().setX(0);
				x = 0;
				playerX = minX;
				redraw = true;
			} else {
				return;
			}
		} else if (playerY == minY && y == -1) {
			char obstacle = thread.io.getLvl()[playerX - minX + region.getX() * (maxX - minX)][region.getY()
					* (maxY - minY) - 1];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setY(region.getY() - 1);
				thread.player.getPosition().setY(maxY - 1);
				y = 0;
				playerY = maxY - 1;
				redraw = true;
			} else {
				return;
			}
		} else if (playerY == maxY - 1 && y == 1) {
			char obstacle = thread.io.getLvl()[playerX - minX + region.getX() * (maxX - minX)][(maxY - minY)
					+ region.getY() * (maxY - minY)];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setY(region.getY() + 1);
				thread.player.getPosition().setY(0);
				y = 0;
				playerY = minY;
				redraw = true;
			} else {
				return;
			}
		}

		int levelX = playerX - minX + (maxX - minX) * region.getX();
		int levelY = playerY - minY + (maxY - minY) * region.getY();
		if (levelX == 0 && x == -1 || levelY == 0 && y == -1 || levelX == thread.io.getWidth() && x == 1
				|| levelY == thread.io.getHeight() && y == 1) {
			return;
		}
		if ((thread.io.getLvl()[levelX + x][levelY + y] != idWall && thread.io.getLvl()[levelX + x][levelY + y] != idIn)
				|| (thread.io.getLvl()[levelX + x][levelY + y] == idOut && thread.player.hasKey())) {
			terminal.setCursorVisible(false);
			terminal.moveCursor(playerX, playerY);
			if (thread.io.getLvl()[levelX][levelY] == idIn) {
				terminal.applyForegroundColor(Color.GREEN);
				terminal.putCharacter(Entry.symbol);
				terminal.applyForegroundColor(Color.WHITE);
			} else {
				terminal.putCharacter(' ');
			}

			thread.player.getPosition().setX(playerX + x);
			thread.player.getPosition().setY(playerY + y);
			terminal.applyForegroundColor(Terminal.Color.WHITE);
			terminal.moveCursor(playerX + x, playerY + y);
			if (x == -1 && y == 0) {
				terminal.putCharacter('\u25C4');
			} else if (x == 1 && y == 0) {
				terminal.putCharacter('\u25BA');
			} else if (x == 0 && y == -1) {
				terminal.putCharacter('\u25B2');
			} else if (x == 0 && y == 1) {
				terminal.putCharacter('\u25BC');
			} else {
				terminal.putCharacter(thread.player.getSymbol());
			}
		}
		if (redraw) {
			getScreen().clear();
			drawLevel();
		}
	}

	/**
	 * Checks for possible events in the game - Player walks into trap - Player
	 * collects key - Player finds exit - Player finds collectible
	 * 
	 * @return true if game goes on, false if current game state prevents game
	 *         from going on
	 */
	public boolean check() {
		int playerX = thread.player.getPosition().getX();
		int playerY = thread.player.getPosition().getY();
		int levelX = (playerX - minX) + (maxX - minX) * region.getX();
		int levelY = (playerY - minY) + (maxY - minY) * region.getY();
		char pos = thread.io.getLvl()[levelX][levelY];
		switch (pos) {
		case idStaticTrap:
			thread.io.getLvl()[levelX][levelY] = empty;
			thread.player.died();
			if (thread.player.getLives() <= 0) {
				return false;
			}
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
			thread.io.getLvl()[levelX][levelY] = empty;
			thread.player.died();
			if (thread.player.getLives() <= 0) {
				getScreen().clear();
				drawColoredString("You lost!", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Bold, maxX / 2 - 4,
						maxY / 2);
				getScreen().refresh();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {

				}
				return false;
			}
			break;
		case idKey:
			thread.io.getLvl()[levelX][levelY] = empty;
			thread.player.setHasKey(true);
			break;
		case idOut:
			thread.io.getLvl()[levelX][levelY] = empty;
			getScreen().clear();
			drawColoredString("You won!", Color.GREEN, Color.BLACK, ScreenCharacterStyle.Bold, maxX / 2 - 4, maxY / 2);
			getScreen().refresh();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ex) {

			}
			return false;
		case idCollectible:
			thread.io.getLvl()[levelX][levelY] = empty;
			thread.player.addScore();
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * Determines the direction to move and starts the Pause Menu if necessary
	 * 
	 * @return true if a movement is encountered, false if the Pause Menu is
	 *         started.
	 */
	public boolean move(boolean multiplayer) {
		Kind kind = listener.getKey(false);
		if (multiplayer) {
			if (kind != null) {
				switch (kind) {
				case ArrowDown:
					setPos(0, 1);
					break;
				case ArrowUp:
					setPos(0, -1);
					break;
				case ArrowLeft:
					setPos(-1, 0);
					break;
				case ArrowRight:
					setPos(1, 0);
					break;
				case Escape:
					getScreen().putString(thread.player.getPosition().getX(), thread.player.getPosition().getY(), " ",
							Color.BLACK, Color.BLACK, ScreenCharacterStyle.Bold);
					PauseMenu pause = new PauseMenu(getResolutionX(), getResolutionY(), getScreen(), this, thread);
					pause.interact(null);
					return false;
				default:
					setPos(0, 0);
					break;
				}
			}
		} else {
			if (kind != null) {
				System.out.println(kind);
				switch (kind) {
				case ArrowDown:
					setPos(0, 1);
					break;
				case ArrowUp:
					setPos(0, -1);
					break;
				case ArrowLeft:
					setPos(-1, 0);
					break;
				case ArrowRight:
					setPos(1, 0);
					break;
				case Escape:
					getScreen().putString(thread.player.getPosition().getX(), thread.player.getPosition().getY(), " ",
							Color.BLACK, Color.BLACK, ScreenCharacterStyle.Bold);
					PauseMenu pause = new PauseMenu(getResolutionX(), getResolutionY(), getScreen(), this, thread);
					pause.interact(null);
					return false;
				default:
					setPos(0, 0);
					break;
				}
			}
		}
		return true;
	}
}
