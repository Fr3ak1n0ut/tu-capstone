package core;

import com.googlecode.lanterna.screen.Screen;

public class Level extends Window
{


	public Level(Screen screen, int resolutionX, int resolutionY)
	{
		super(resolutionX, resolutionY, screen);
	}


	/*
	 * private boolean draw(Screen screen, boolean start) {
	 * screen.getTerminal().setCursorVisible(false); screen.clear(); for (int i
	 * = 0; i < getResolutionX(); i++) { // Height-4 because of info at bottom
	 * for (int ii = 0; ii < getResolutionY() - 4; ii++) { switch (lvl[regionX *
	 * i][regionY * ii]) { case idWall: Wall wall = new Wall(i, ii); break; case
	 * idIn: Entry entry = new Entry(i, ii); if (start) { player = new Player(i,
	 * ii); } break; case idOut: Exit exit = new Exit(i, ii); break; case
	 * idStaticTrap: StaticEnemy staticEnemy = new StaticEnemy(i, ii); break;
	 * case idDynamicTrap: // Add some game logic here DynamicEnemy dynamicEnemy
	 * = new DynamicEnemy(i, ii); case idKey: Key key = new Key(i, ii); break;
	 * case idPlayer: player = new Player(i, ii); break; default: break; } } }
	 * screen.refresh(); return true;
	 * 
	 * }
	 */

	
}
