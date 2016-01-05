package core;
/**
 * @author Felix Wohnhaas
 * Starts the game and initializes all components
 */

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import menus.StartMenu;
import symbols.Player;

public class Game {
	public static Player player = null;
	public static IOProperties io;
	public static Audio music;

	public static void main(String[] args) {
		io = new IOProperties();
		music = new Audio("music.wav");
		music.start();
		Terminal terminal = TerminalFacade.createSwingTerminal(150, 60);
		Screen screen = TerminalFacade.createScreen(terminal);
		StartMenu start = new StartMenu(screen.getTerminalSize().getColumns(), screen.getTerminalSize().getRows(),
				screen);
		screen.startScreen();
		start.interact(null);
	}
}
