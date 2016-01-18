package core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Random;

import symbols.Player;

/**
 *
 * @author Felix Wohnhaas
 */
public class IOProperties {
	private Properties props;

	private int width;
	private int height;

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

	/**
	 * Saves the level to a properties file
	 * 
	 * @param filename
	 *            the filename of the save
	 * @return true if the save was successful, false if not
	 */
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
		saveProp.setProperty("hasKey", Game.player.hasKey() + "");
		saveProp.setProperty("regionX", Core.region.getX() + "");
		saveProp.setProperty("regionY", Core.region.getY() + "");
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

	/**
	 * Creates the level data out of the loaded properties file
	 * 
	 * @return true if the creation was successful
	 */
	public boolean createLevelData() {
        lvl = new char[width][height];
        for (int i = 0; i < width; i++) {
            for (int ii = 0; ii < height; ii++) {
                if (props.containsKey(i + "," + ii)) {
                    char c = props.getProperty(i + "," + ii).charAt(0);
                    lvl[i][ii] = c;
                } else {
                    Random rand = new Random();
                    int spawn = rand.nextInt(100);
                    if (spawn < 1) {
                        lvl[i][ii] = '8';
                    } else {
                        lvl[i][ii] = '6';
                    }
                }
            }
        }
        return true;
    }

	/**
	 * Loads the properties into the program
	 * 
	 * @param filename
	 *            the filename of the properties to load in
	 * @return true if the loading was successful, false if not
	 */
	public boolean loadProperties(String filename) {
		props = new Properties();
		try {
			FileInputStream in = new FileInputStream(filename);
			props.load(in);
			width = Integer.parseInt(props.getProperty("Width"));
			height = Integer.parseInt(props.getProperty("Height"));
			if (props.containsKey("posX")) {
				int posX = Integer.parseInt(props.getProperty("posX"));
				int posY = Integer.parseInt(props.getProperty("posY"));
				Game.player = new Player(posX, posY);
				Core.region = new Coordinates(Integer.parseInt(props.getProperty("regionX")),
						Integer.parseInt(props.getProperty("regionY")));
				Game.player.setLives(Integer.parseInt(props.getProperty("lives")));
				Game.player.setScore(Integer.parseInt(props.getProperty("score")));
				Game.player.setHasKey(Boolean.parseBoolean(props.getProperty("hasKey")));
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			// Exception occurs when specific property is not found
			System.err.println("Invalid number format");
		}
		return true;
	}

}
