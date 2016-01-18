package core;

import java.util.Random;

import com.googlecode.lanterna.input.Key.Kind;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.Terminal.Color;

import menus.PauseMenu;
import symbols.DynamicEnemy;
import symbols.Entry;
/**
 * 
 * @author Felix Wohnhaas This class is responsible for all movement-related actions in the game.
 *
 */
public class Movement extends Window {

	Core core;

	public Movement(Screen screen, int x, int y, Core core) {
		super(x, y, screen);
		this.core = core;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Sets the position of the player after checking for possible obstacles
	 *
	 * @param x
	 *            the amount of units to move in x direction
	 * @param y
	 *            the amount of units to move in y direction
	 */
	public void setPos(int x, int y) {
		int playerX = Game.player.getPosition().getX();
		int playerY = Game.player.getPosition().getY();
		Terminal terminal = getScreen().getTerminal();
		boolean redraw = false;
		// Check for screen wrap
		if ((playerX == 0 && x == -1) || (playerX == core.realWidth - 1 && x == 1) || (playerY == 0 && y == -1)
				|| (playerY == core.realHeight - 1 && y == 1)) {
			char obstacle = Game.io.getLvl()[playerX + x + Core.region.getX() * core.realWidth][playerY + y
					+ Core.region.getY() * core.realHeight];
			if (obstacle != core.idWall) {
				terminal.moveCursor(playerX, playerY);
				terminal.putCharacter(' ');
				Core.region.setX(Core.region.getX() + x);
				Core.region.setY(Core.region.getY() + y);
				if (x != 0) {
					Game.player.getPosition().setX(core.realWidth - 1 - playerX);
					playerX = core.realWidth - 1 - playerX;
				} else {
					Game.player.getPosition().setY(core.realHeight - 1 - playerY);
					playerY = core.realHeight - 1 - playerY;
				}
				x = 0;
				y = 0;
				redraw = true;
			} else {
				return;
			}
		}

		int levelX = playerX + core.realWidth * Core.region.getX();
		int levelY = playerY + core.realHeight * Core.region.getY();
		// Only necessary for preventing first movement in the wrong direction
		// before entering the lab
		if (levelX == 0 && x == -1 || levelY == 0 && y == -1 || levelX == Game.io.getWidth() && x == 1
				|| levelY == Game.io.getHeight() && y == 1) {
			return;
		}

		if ((Game.io.getLvl()[levelX + x][levelY + y] != core.idWall
				&& Game.io.getLvl()[levelX + x][levelY + y] != core.idIn)) {
			// Player allowed to exit?
			if (Game.io.getLvl()[levelX + x][levelY + y] == core.idOut && !Game.player.hasKey()) {
				return;
			}
			terminal.setCursorVisible(false);
			terminal.moveCursor(playerX, playerY);
			if (Game.io.getLvl()[levelX][levelY] == core.idIn) {
				terminal.applyForegroundColor(Color.GREEN);
				terminal.putCharacter(Entry.symbol);
				terminal.applyForegroundColor(Color.WHITE);
			} else {
				terminal.putCharacter(' ');
			}

			Game.player.getPosition().setX(playerX + x);
			Game.player.getPosition().setY(playerY + y);
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
				terminal.putCharacter(Game.player.getSymbol());
			}
		}
		if (redraw) {
			getScreen().clear();
			core.drawLevel();
		}
	}
	/**
	 * Determines the direction to move and starts the Pause Menu if necessary
	 *
	 * @return true if a movement is encountered, false if the Pause Menu is
	 *         started.
	 */
	public boolean move() {
		Kind kind = core.listener.getKey(false);
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
				getScreen().putString(Game.player.getPosition().getX(), Game.player.getPosition().getY(), " ",
						Color.BLACK, Color.BLACK, ScreenCharacterStyle.Bold);
				PauseMenu pause = new PauseMenu(getResolutionX(), getResolutionY(), getScreen(), core);
				pause.interact(null);
				return false;
			default:
				setPos(0, 0);
				break;
			}
		}
		return true;
	}
	
	/**
	 * Iterates through all dynamic enemies
	 */
	public void dynamicEnemy() {
		for (int i = 0; i < core.dynamicEnemies.length; i++) {
			if (core.dynamicEnemies[i] != null) {
				core.dynamicEnemies[i] = moveEnemy(core.dynamicEnemies[i]);
				core.drawSymbol(core.dynamicEnemies[i]);
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
		int x = pos.getX() + Core.region.getX() * core.realWidth;
		int y = pos.getY() + Core.region.getY() * core.realHeight;
		if (x < Game.io.getWidth() && y < Game.io.getHeight()) {
			char lvlPos = Game.io.getLvl()[x + offsetX][y + offsetY];
			if (lvlPos != core.idIn && lvlPos != core.idOut && lvlPos != core.idWall) {
				Game.io.getLvl()[x][y] = core.empty;
				Game.io.getLvl()[x + offsetX][y + offsetY] = core.idDynamicTrap;
				drawColoredString(" ", Color.BLACK, Color.BLACK, null, pos.getX(), pos.getY());
				return new DynamicEnemy(pos.getX() + offsetX, pos.getY() + offsetY);
			}
		}
		return enemy;
	}

}
