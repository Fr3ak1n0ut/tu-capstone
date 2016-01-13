package core;

import com.googlecode.lanterna.screen.Screen;

import symbols.Player;

public class TestThreading extends Thread {
	public Player player = null;
	public IOProperties io;
	private Screen screen;
	int minX, maxX, minY, maxY;

	public TestThreading(Screen screen, int resolutionX, int resolutionY, int minX, int maxX, int minY, int maxY) {
		this.screen = screen;
		this.minX = minX;
		this.maxX = maxX;
		this.minY = minY;
		this.maxY = maxY;
		io = new IOProperties(this);
	}

	public void run() {
		Core core = new Core(screen, screen.getTerminalSize().getColumns(), screen.getTerminalSize().getRows(),
				"level.properties", this, minX, maxX, minY, maxY);
		core.start();

	}
}
