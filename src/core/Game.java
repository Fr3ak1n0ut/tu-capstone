package core;
/**
 * @author Felix Wohnhaas
 *
 */

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import menus.StartMenu;
import symbols.Player;

public class Game
{
	public static Player player = null;
	public static IOProperties io;
	public static Audio music;

	public static void main(String[] args)
	{
		io = new IOProperties();
		music = new Audio("music.wav");
//		music.start();
		Screen screen = TerminalFacade.createScreen();
		StartMenu start = new StartMenu(200, 60, screen);
		screen.startScreen();
		start.interact();
	}
}
