package core;

import java.util.Random;

/**
*
* @author Felix Wohnhaas
*/
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal.Color;

import symbols.DynamicEnemy;

public class Movement extends Window implements Runnable {
	Core core;
	final char idWall = '0';
	final char idIn = '1';
	final char idOut = '2';
	final char idDynamicTrap = '4';
	final char empty = '6';

	public Movement(Screen screen, int resolutionX, int resolutionY, Core core) {
		super(resolutionX, resolutionY, screen);
		this.core = core;
	}

	public void run() {
		while (true) {
			dynamicEnemy();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void dynamicEnemy() {
		for (int i = 0; i < Core.dynamicEnemies.length; i++) {
			if (Core.dynamicEnemies[i] != null) {
				System.out.println("Enemy before movement: " + Core.dynamicEnemies[i].getPosition().toString());
				if (Game.player.getPosition().getX() == Core.dynamicEnemies[i].getPosition().getX()
						&& Game.player.getPosition().getY() == Core.dynamicEnemies[i].getPosition().getY()) {
					System.out.println("Player hit");
					Core.dynamicEnemies[i] = null;
				} else {
					Core.dynamicEnemies[i] = moveEnemy(Core.dynamicEnemies[i]);
					System.out.println("Enemy after movement: " + Core.dynamicEnemies[i].getPosition().toString());
					core.drawSymbol(Core.dynamicEnemies[i]);
				}
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
		int x = pos.getX() + Core.region.getX() * getResolutionX();
		int y = pos.getY() + Core.region.getY() * getResolutionY() - 3;
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
}
