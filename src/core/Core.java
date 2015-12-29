package core;

import java.io.IOException;
import java.util.Random;
import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;
import menus.PauseMenu;
import symbols.*;

/**
 * @author Felix Wohnhaas
 *
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
	final char idCollectable = '8';
	final int realWidth = getResolutionX();
	final int realHeight = getResolutionY() - 3;
	public static DynamicEnemy[] dynamicEnemies;
	int enemies = 0;

	KeyListener listener = new KeyListener(getScreen());
	public static Coordinates region;

	public Core(Screen screen, int resolutionX, int resolutionY, String filename) {
		super(resolutionX, resolutionY, screen);
		Game.io.loadProperties(filename);
		Game.io.createLevelData();
	}

	public void start() {
		getScreen().clear();
		if (region == null) {
			System.out.println("region null");
			region = region(Game.io.getLvl());
		}
		drawLevel();
		getScreen().refresh();
//		Runnable enemyThread = new Movement(getScreen(), getResolutionX(), getResolutionY(), this);
//		Thread t = new Thread(enemyThread);
//		t.start();
		update();
	}

	/**
	 * Für diese Methode muss überprüft werden, ob es auch für ungleiche
	 * Level-Größen funktioniert
	 * 
	 * @param lvl
	 * @return
	 */
	public Coordinates region(char[][] lvl) {
		int width = Game.io.getWidth();
		int height = Game.io.getHeight();
		if (Game.io.getWidth() > realWidth || Game.io.getHeight() > realHeight) {
			for (int i = 0; i < lvl[0].length; i++) {
				if (lvl[0][i] == idIn) {
					System.out.println("Found: 0," + i);
					return new Coordinates(0, i / realHeight);
				}
				if (lvl[width - 1][i] == idIn) {
					System.out.println("Found: " + (realWidth - 1) + "," + i);
					return new Coordinates(width / realWidth, i / realHeight);
				}
			}
			for (int i = 0; i < height; i++) {
				if (lvl[i][0] == idIn) {
					System.out.println("Found: " + i + ",0");
					return new Coordinates(i / realWidth, 0);

				}
				if (lvl[i][height - 1] == idIn) {
					System.out.println("Found: " + i + ", 499");
					return new Coordinates(i / (realWidth), height / (realHeight));
				}
			}
		}
		return new Coordinates(0, 0);
	}

	public void dynamicEnemy() {
		for (int i = 0; i < dynamicEnemies.length; i++) {
			if (dynamicEnemies[i] != null) {
				System.out.println(dynamicEnemies[i].getPosition().toString());
				dynamicEnemies[i] = moveEnemy(dynamicEnemies[i]);
				System.out.println(dynamicEnemies[i].getPosition().toString());
				drawSymbol(dynamicEnemies[i]);
			}
		}
		getScreen().refresh();
		getScreen().getTerminal().setCursorVisible(false);
	}

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
		int x = pos.getX() + region.getX() * realWidth;
		int y = pos.getY() + region.getY() * realHeight;
		System.out.println(x + "," + y);
		if (x < Game.io.getWidth() && y < Game.io.getHeight()) {
			char lvlPos = Game.io.getLvl()[x + offsetX][y + offsetY];
			if (lvlPos != idIn && lvlPos != idOut && lvlPos != idWall) {
				System.out.println("move");
				Game.io.getLvl()[x][y] = empty;
				Game.io.getLvl()[x + offsetX][y + offsetY] = idDynamicTrap;
				drawColoredString(" ", Color.BLACK, Color.BLACK, null, pos.getX(), pos.getY());
				return new DynamicEnemy(pos.getX() + offsetX, pos.getY() + offsetY);
			}
		}
		System.out.println("no move");
		return enemy;
	}

	public void drawBorder() {
		for (int x = 0; x < realWidth; x++) {
			drawColoredString("\u2550", Color.GREEN, Color.BLACK, null, x, realHeight);
			drawColoredString(" ", Color.BLACK, Color.BLACK, null, x, realHeight + 1);
		}
		System.out.println("Lives: " + Game.player.getLives());
		for (int i = 0; i < Game.player.getLives(); i++) {
			drawColoredString("\u2665", Color.RED, Color.BLACK, null, 2 * i + 3, realHeight + 1);
		}
		if (Game.player.hasKey()) {
			drawColoredString("\u06DE", Color.CYAN, Color.BLACK, null, 15, realHeight + 1);
		}
		getScreen().refresh();
	}

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
					case idCollectable:
						drawSymbol(new Collectable(x, y));
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

	public void drawSymbol(Symbol symbol) {
		char character = symbol.getSymbol();
		Terminal.Color background = symbol.getBackgroundColor();
		Terminal.Color foreground = symbol.getForegroundColor();
		drawColoredString(character + "", foreground, background, null, symbol.getPosition().getX(),
				symbol.getPosition().getY());
	}

	public void update() {
		drawBorder();
		boolean game = true;
		while (game) {
			getScreen().getTerminal().setCursorVisible(false);
			 dynamicEnemy();
			game = move();
			game = check();
			drawBorder();
		}
		System.out.println("Over.");
		System.exit(0);
	}

	private void setPos(int x, int y) throws InterruptedException, IOException {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		System.out.println("Region: " + region.toString());
		Terminal terminal = getScreen().getTerminal();
		boolean redraw = false;
		if (playerX == 0 && x == -1) {
			char obstacle = Game.io.getLvl()[region.getX() * realWidth - 1][playerY + region.getY() * realHeight];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setX(region.getX() - 1);
				Game.player.getPosition().setX(realWidth - 1);
				x = 0;
				playerX = realWidth - 1;
				redraw = true;
			} else {
				return;
			}
		} else if (playerX == realWidth - 1 && x == 1) {
			char obstacle = Game.io.getLvl()[realWidth + region.getX() * realWidth][playerY
					+ region.getY() * realHeight];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setX(region.getX() + 1);
				Game.player.getPosition().setX(0);
				x = 0;
				playerX = 0;
				redraw = true;
			} else {
				return;
			}
		} else if (playerY == 0 && y == -1) {
			System.out.println(region.getY());
			char obstacle = Game.io.getLvl()[playerX + region.getX() * realWidth][region.getY() * realHeight - 1];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setY(region.getY() - 1);
				Game.player.getPosition().setY(realHeight - 1);
				y = 0;
				playerY = realHeight - 1;
				redraw = true;
			} else {
				return;
			}
		} else if (playerY == realHeight - 1 && y == 1) {
			char obstacle = Game.io.getLvl()[playerX + region.getX() * realWidth][realHeight
					+ region.getY() * realHeight];
			if (obstacle != idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				region.setY(region.getY() + 1);
				Game.player.getPosition().setY(0);
				y = 0;
				playerY = 0;
				redraw = true;
			} else {
				return;
			}
		}

		int levelX = playerX + realWidth * region.getX();
		int levelY = playerY + realHeight * region.getY();
		if (levelX == 0 && x == -1 || levelY == 0 && y == -1 || levelX == Game.io.getWidth() && x == 1
				|| levelY == Game.io.getHeight() && y == 1) {
			return;
		}
		if ((Game.io.getLvl()[levelX + x][levelY + y] != idWall && Game.io.getLvl()[levelX + x][levelY + y] != idIn)
				|| (Game.io.getLvl()[levelX + x][levelY + y] == idOut && Game.player.hasKey())) {
			terminal.setCursorVisible(false);
			terminal.moveCursor(playerX, playerY);
			if (Game.io.getLvl()[levelX][levelY] == idIn) {
				terminal.applyForegroundColor(Color.GREEN);
				terminal.putCharacter(Entry.symbol);
				terminal.applyForegroundColor(Color.WHITE);
			} else {
				terminal.putCharacter(' ');
			}

			Game.player.getPosition().setX(playerX + x);
			Game.player.getPosition().setY(playerY + y);
			// terminal.applyBackgroundColor(Terminal.Color.BLACK);
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
				terminal.putCharacter('~');
			}
		}
		if (redraw)

		{
			getScreen().clear();
			drawLevel();
		}
		System.out.println("Player pos: " + playerX + "," + playerY);
	}

	public boolean check() {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		char pos = Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight];
		switch (pos) {
		case idStaticTrap:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.died();
			System.out.println(Game.player.getLives());
			if (Game.player.getLives() <= 0) {
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
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.died();
			System.out.println(Game.player.getLives());
			if (Game.player.getLives() <= 0) {
				return false;
			}
			break;
		case idKey:
			System.out.println("Key");
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.setHasKey(true);
			break;
		case idOut:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			System.out.println("WON");
			return false;
		case idCollectable:
			Game.io.getLvl()[playerX + region.getX() * realWidth][playerY + region.getY() * realHeight] = empty;
			Game.player.addScore();
		default:
			break;
		}
		return true;
	}

	public boolean move() {
		Kind kind = listener.getKey();

		try {
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
					PauseMenu pause = new PauseMenu(getResolutionX(), getResolutionY(), getScreen(), this);
					pause.interact(null);
					return false;
				default:
					setPos(0, 0);
					break;
				}
			}
		} catch (InterruptedException e) {
			System.out.println("Interrupted");
		} catch (IOException e1) {
			System.err.println("IOException");
		}
		return true;
	}

	public void end() {

	}

}
