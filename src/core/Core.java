package core;

import java.io.IOException;

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
	// private List<DynamicEnemy> dynamicEnemyList = new ArrayList<>();

	final char idWall = '0';
	final char idIn = '1';
	final char idOut = '2';
	final char idStaticTrap = '3';
	final char idDynamicTrap = '4';
	final char idKey = '5';
	final char empty = '6';
	final char idPlayer = '7';
	final char idCollectable = '8';
	private char[][] lvl;
	KeyListener listener = new KeyListener(getScreen());
	private Coordinates region;

	public Core(Screen screen, int resolutionX, int resolutionY, String filename) {
		super(resolutionX, resolutionY, screen);
		Game.io.loadProperties(filename);
		Game.io.createLevelData();
		lvl = Game.io.getLvl();
	}

	public void start() {
		if (Game.player == null) {
			System.out.println("Player null");
			region = region(lvl);
		} else {
			if(region == null)
			{
				//Add region from io
				region = new Coordinates(Game.player.getPosition().getX() / getResolutionX(),
				Game.player.getPosition().getY() / getResolutionY());
			}
			System.out.println("Player existing");

		}
		drawLevel();
		getScreen().refresh();
		update();
	}

	/**
	 * Für diese Methode muss überprüft werden, ob es auch für ungleiche Level-Größen funktioniert
	 * @param lvl
	 * @return
	 */
	public Coordinates region(char[][] lvl) {
		int width = Game.io.getWidth();
		int height = Game.io.getHeight();
		if (Game.io.getWidth() > getResolutionX() || Game.io.getHeight() > getResolutionY()) {
			for (int i = 0; i < lvl[0].length; i++) {
				if (lvl[0][i] == idIn) {
					System.out.println("Found: 0," + i);
					return new Coordinates(0, i / getResolutionY());
				}
				if (lvl[width - 1][i] == idIn) {
					System.out.println("Found: " + (getResolutionX() - 1) + "," + i);
					return new Coordinates(width / getResolutionX(), i / getResolutionY());
				}
			}
			for (int i = 0; i < height; i++) {
				if (lvl[i][0] == idIn) {
					System.out.println("Found: " + i + ",0");
					return new Coordinates(i / getResolutionX(), 0);

				}
				if (lvl[i][height - 1] == idIn) {
					System.out.println("Found: " + i + ", 499");
					return new Coordinates(i / (getResolutionX()), height / (getResolutionY()));
				}
			}
		}
		return new Coordinates(0, 0);
	}

	public void drawLevel() {
		Entry entry = null;
		int posX = 0;
		int posY = 0;
		for (int x = 0; x < getResolutionX(); x++) {
			for (int y = 0; y < getResolutionY(); y++) {
				int levelX = x + getResolutionX() * region.getX();
				int levelY = y + getResolutionY() * region.getY();
				System.out.println("Level Coordinates: "+levelX+","+levelY);
				if (levelY < Game.io.getHeight() && levelX < Game.io.getWidth()) {
					switch (lvl[levelX][levelY]) {
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
						// Don´t loop this yet, will add new enemy every
						// time
						DynamicEnemy enemy = new DynamicEnemy(x, y);
						drawSymbol(enemy);
						// dynamicEnemyList.add(enemy);
						break;
					case idKey:
						drawSymbol(new Key(x, y));
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
		/*
		 * if(!entryFound && !load){ if(regionX*terminalHeight>=levelHeight){
		 * regionY++; regionX--; } regionX++; setLab((terminalWidth) * regionX,
		 * (terminalHeight-3) * regionY);
		 *
		 * if (Game.io.getWidth() > Game.io.getRegionX() * getResolutionX())
		 * System.out.println("Y"); Game.io.addRegionY(1); }
		 */
		if (Game.player == null) {
				Game.player = new Player(posX, posY);
		}
		getScreen().setCursorPosition(Game.player.getPosition().getX(), Game.player.getPosition().getY());
		drawSymbol(Game.player);
		getScreen().refresh();
	}

	private void drawSymbol(Symbol symbol) {
		char character = symbol.getSymbol();
		Terminal.Color background = symbol.getBackgroundColor();
		Terminal.Color foreground = symbol.getForegroundColor();
		drawColoredString(character + "", foreground, background, null, symbol.getPosition().getX(),
				symbol.getPosition().getY());
		getScreen().getTerminal().applyBackgroundColor(Color.BLACK);
		getScreen().getTerminal().applyForegroundColor(Color.BLACK);
	}

	public void update() {
		getScreen().getTerminal().setCursorVisible(false);
		boolean game = true;
		while (game) {
			game = move();
			game = check();
		}
		System.out.println("Lost.");
		// Movement
		// Game Logic
		// Update Lab
		// Menu Invoke
	}

	private void setPos(int x, int y) throws InterruptedException, IOException {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		boolean redraw = false;
		if(playerX == 0 && x == -1)
		{
			region.setX(region.getX()-1);
			redraw = true;
		}
		else if(playerX == getResolutionX()-1 && x == 1)
		{
			region.setX(region.getX()+1);
			drawLevel();
			redraw = true;
		}
		else if(playerY == 0 && y == -1)
		{
			region.setY(region.getX()-1);
			drawLevel();
			redraw = true;
		}
		else if(playerY == getResolutionY()-1 && y == 1)
		{
			region.setY(region.getY()+1);
			drawLevel();
			redraw = true;
		}
		int levelX = playerX + getResolutionX()*region.getX();
		int levelY = playerY +getResolutionY()*region.getY();
		if(levelX == 0 && x == -1 || levelY == 0 && y == -1)
		{
			return;
		}
		if ((lvl[levelX + x][levelY + y] != idWall && lvl[levelX + x][levelY + y] != idIn)
				|| (lvl[levelX + x][levelY + y] == idOut && Game.player.getHasKey())) {
			
			Terminal terminal = getScreen().getTerminal();
			terminal.setCursorVisible(false);
			terminal.moveCursor(playerX, playerY);
			if (lvl[levelX][levelY] == idIn) {
				terminal.applyForegroundColor(Color.GREEN);
				terminal.putCharacter('\u2691');
				terminal.applyForegroundColor(Color.WHITE);
			} else {
				terminal.putCharacter(' ');
			}

			Game.player.getPosition().setX(playerX + x);
			Game.player.getPosition().setY(playerY + y);
			terminal.applyBackgroundColor(Terminal.Color.BLACK);
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
		if(redraw)
		{
			drawLevel();
		}
	}

	public boolean check() {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		if (lvl[playerX][playerY] == idStaticTrap || lvl[playerX][playerY] == idDynamicTrap) {
			lvl[playerX][playerY] = empty;
			System.out.println("Enemy");
			Game.player.died();
			System.out.println(Game.player.getLives());
			if (Game.player.getLives() <= 0) {
				return false;
			}
			// writer.drawString(10 + 2 * lives, terminalHeight - 2, " ");
		} else if (lvl[playerX][playerY] == idKey) {
			System.out.println("Key");
			lvl[playerX][playerY] = empty;
			Game.player.setHasKey(true);
		} else if (lvl[playerX][playerY] == idCollectable) {
			Game.player.addScore();
			// not implemented yet
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
