package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import symbols.Player;

/**
 *
 * @author Felix Wohnhaas
 */
public class IOProperties {
	private Properties props;

	private int width;
	private int height;
//	private int regionX;
//	private int regionY;

	private char[][] lvl;

	public char[][] getLvl() {
		return lvl;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

//	public int getRegionX() {
//		return regionX;
//	}

//	public int getRegionY() {
//		return regionY;
//	}
	
//	public void addRegionX(int amount) {
//		this.regionX += amount;
//	}
//	public void addRegionY(int amount) {
//		this.regionY += amount;
//	}

	public boolean saveLevel(String filename) {
		Properties saveProp = new Properties();
		for (int i = 0; i < width; i++) {
			for (int ii = 0; ii < height; ii++) {
				if (!(lvl[i][ii] == '\u0000')) {
					saveProp.setProperty(i + "," + ii, lvl[i][ii] + "");
				}
			}
		}
		saveProp.setProperty("Width", width + "");
		saveProp.setProperty("Height", height + "");
		saveProp.setProperty("posX", Game.player.getPosition().getX() + "");
		saveProp.setProperty("posY", Game.player.getPosition().getY() + "");
		saveProp.setProperty("hasKey", Game.player.getHasKey() + "");
//		saveProp.setProperty("regionX", regionX + "");
//		saveProp.setProperty("regionY", regionY + "");
		saveProp.setProperty("score", Game.player.getScore() + "");
		saveProp.setProperty("lives", Game.player.getLives() + "");
		try {
			FileOutputStream out = new FileOutputStream(new File(filename));
			saveProp.store(out, "savedLevel");
			out.close();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean createLevelData() {
		lvl = new char[width][height];
		Enumeration<?> keyEnum = props.propertyNames();
		while (keyEnum.hasMoreElements()) {
			String key = (String) keyEnum.nextElement();
			if (key.contains(",")) {
				String val = props.getProperty(key);
				String[] keyCoordinates = key.split(",");
				int x = Integer.parseInt(keyCoordinates[0]);
				int y = Integer.parseInt(keyCoordinates[1]);
				if (val.charAt(0) == '2') {
					// exits++;
				} else if (val.charAt(0) == '4') {
					// dynamicEnemies++;
				}
				lvl[x][y] = val.charAt(0);
			}
		}
		return true;
	}

	public boolean loadProperties(String filename) {
		props = new Properties();
		try {
			FileInputStream in = new FileInputStream(filename);
			props.load(in);
			width = Integer.parseInt(props.getProperty("Width"));
			height = Integer.parseInt(props.getProperty("Height"));
//			if (props.contains("regionX") && props.contains("regionY")) {
				int posX = Integer.parseInt(props.getProperty("posX"));
				int posY = Integer.parseInt(props.getProperty("posY"));
				Game.player = new Player(posX, posY);
//				regionX = Integer.parseInt(props.getProperty("regionX"));
//				regionY = Integer.parseInt(props.getProperty("regionY"));
				Game.player.setLives(Integer.parseInt(props.getProperty("lives")));
				Game.player.setScore(Integer.parseInt(props.getProperty("score")));
				Game.player.setHasKey(Boolean.parseBoolean(props.getProperty("hasKey")));
//			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

}
