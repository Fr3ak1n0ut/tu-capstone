package core;

import java.io.IOException;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

import symbols.Collectable;
import symbols.DynamicEnemy;
import symbols.Entry;
import symbols.Exit;
import symbols.Key;
import symbols.Path;
import symbols.Player;
import symbols.StaticEnemy;
import symbols.Symbol;
import symbols.Wall;

/**
 * @author Felix Wohnhaas
 *
 */
public class Core extends Window
{
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
	private Player player;
	private char[][] lvl;

	public Core(Screen screen, int resolutionX, int resolutionY)
	{
		super(resolutionX, resolutionY, screen);
		player = Game.player;
	}

	public void start(String level)
	{
		Game.io.loadProperties(level);
		Game.io.createLevelData();
		drawLevel(Game.io.getLevel());
		getScreen().refresh();
		lvl = Game.io.getLevel();
		update();
	}

	public void drawLevel(char[][] lvl)
	{
		getScreen().clear();
		Entry entry = null;
		getScreen().clear();

		for (int x = 0; x < getResolutionX(); x++)
		{
			for (int y = 0; y < getResolutionY(); y++)
			{
				int levelX = x + getResolutionX() * Game.io.getRegionX();
				int levelY = y + getResolutionY() * Game.io.getRegionY();

				if (levelY < Game.io.getHeight() && levelX < Game.io.getWidth())
				{
					System.out.println("Coordinates: " + levelX + "," + levelY);
					System.out.println(lvl.length);
					switch (lvl[levelX][levelY])
					{
					case idWall:
						drawSymbol(new Wall(levelX, levelY));
						break;
					case idIn:
						entry = new Entry(levelX, levelY);
						drawSymbol(entry);
						break;
					case idOut:
						drawSymbol(new Exit(levelX, levelY));
						break;
					case idStaticTrap:
						drawSymbol(new StaticEnemy(levelX, levelY));
						break;
					case idDynamicTrap:
						// Dont loop this yet, will add new enemy every time
						DynamicEnemy enemy = new DynamicEnemy(levelX, levelY);
						drawSymbol(enemy);
						// dynamicEnemyList.add(enemy);
						break;
					case idKey:
						drawSymbol(new Key(levelX, levelY));
						break;
					case empty:
						drawSymbol(new Path(levelX, levelY));
						break;
					/*
					 * case idPlayer: Game.player = new Player(levelX, levelY);
					 * drawSymbol(Game.player); break;
					 */
					case idCollectable:
						drawSymbol(new Collectable(levelX, levelY));
						break;
					default:
						drawSymbol(new Path(levelX, levelY));
						break;
					}
				}
			}
		}
		if (player == null)
		{
			player = new Player(entry.getPosition().getX(), entry.getPosition().getY());
		}
		getScreen().setCursorPosition(player.getPosition().getX(), player.getPosition().getY());
		drawSymbol(player);
		getScreen().refresh();
	}

	private void drawSymbol(Symbol symbol)
	{
		System.out.println(symbol.toString());
		char character = symbol.getSymbol();
		Terminal.Color background = symbol.getBackgroundColor();
		Terminal.Color foreground = symbol.getForegroundColor();
		Coordinates position = symbol.getPosition();
		drawColoredString(character + " ", foreground, background, null, position.getX(), position.getY());
		getScreen().getTerminal().applyBackgroundColor(Color.BLACK);
		getScreen().getTerminal().applyForegroundColor(Color.BLACK);
	}

	public void update()
	{
		while (true)
		{
			move();
		}
		// Movement
		// Game Logic
		// Update Lab
		// Menu Invoke
	}

	private void setPos(int x, int y) throws InterruptedException, IOException
	{
		int playerX = player.getPosition().getX();
		int playerY = player.getPosition().getY();
		Terminal terminal = getScreen().getTerminal();
		terminal.setCursorVisible(false);
		terminal.moveCursor(playerX, playerY);
		terminal.putCharacter(' ');
		player.getPosition().setX(playerX + x);
		player.getPosition().setY(playerY + y);
		terminal.applyBackgroundColor(Terminal.Color.BLACK);
		terminal.applyForegroundColor(Terminal.Color.WHITE);
		terminal.moveCursor(playerX + x, playerY + y);
		if (x == -1 && y == 0)
		{
			terminal.putCharacter('\u25C4');
		} else if (x == 1 && y == 0)
		{
			terminal.putCharacter('\u25BA');
		} else if (x == 0 && y == -1)
		{
			terminal.putCharacter('\u25B2');
		} else if (x == 0 && y == 1)
		{
			terminal.putCharacter('\u25BC');
		} else
		{
			terminal.putCharacter('~');
		}

		if (lvl[playerX][playerY] == idStaticTrap || lvl[playerX][playerY] == idDynamicTrap)
		{
			lvl[playerX][playerY] = '6';
			System.out.println("Enemy");
			// saveChar[posX][posY] = ' ';
			// writer.drawString(10 + 2 * lives, terminalHeight - 2, " ");
			// lives--;
		}

	}

	public boolean move()
	{
		KeyListener listener = new KeyListener(getScreen());
		Kind kind = listener.getKey();
		int playerX = player.getPosition().getX();
		int playerY = player.getPosition().getY();
		try
		{
			if (kind != null)
			{
				switch (kind)
				{
				case ArrowDown:
					if (lvl[playerX][playerY + 1] != idWall)
						setPos(0, 1);
					break;
				case ArrowUp:
					if (lvl[playerX][playerY - 1] != idWall)
						setPos(0, -1);
					break;
				case ArrowLeft:
					if (lvl[playerX - 1][playerY] != idWall)
						setPos(-1, 0);
					break;
				case ArrowRight:
					if (lvl[playerX + 1][playerY] != idWall)
						setPos(1, 0);
					break;
				default:
					setPos(0, 0);
					break;
				}
			}
		} catch (InterruptedException e)
		{
			System.out.println("Interrupted");
		} catch (IOException e1)
		{
			System.err.println("IOException");
		}
		return true;
	}

	public void finalize()
	{

	}

}
