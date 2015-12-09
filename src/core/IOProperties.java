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
public class IOProperties
{
	private Properties props;
	private int regionX;
	private int regionY;
	private int lives;
	private int score;
	private boolean hasKey;
	private char[][] level;
	private int width;
	private int height;
	private int dynamicEnemies;
	private int exits;

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

	public char[][] getLevel()
	{
		return level;
	}

	public int getLives()
	{
		return lives;
	}

	public Properties getLvlData()
	{
		return props;
	}

	public int getRegionX()
	{
		return regionX;
	}

	public int getRegionY()
	{
		return regionY;
	}

	public int getScore()
	{
		return score;
	}

	public boolean saveLevel(String filename)
	{
		Properties saveProp = new Properties();
		for (int i = 0; i < width; i++)
		{
			for (int ii = 0; ii < height; ii++)
			{
				saveProp.setProperty(i + "," + ii, level[i][ii] + "");
			}
		}
		saveProp.setProperty("Width", width + "");
		saveProp.setProperty("Height", height + "");
		saveProp.setProperty("posX", Game.player.getPosition().getX() + "");
		saveProp.setProperty("posY", Game.player.getPosition().getY() + "");
		saveProp.setProperty("hasKey", hasKey + "");
		saveProp.setProperty("regionX", regionX + "");
		saveProp.setProperty("regionY", regionY + "");
		saveProp.setProperty("score", score + "");
		saveProp.setProperty("lives", lives + "");
		try
		{
			FileOutputStream out = new FileOutputStream(new File(filename));
			saveProp.store(out, "savedLevel");
			out.close();
		} catch (IOException ex)
		{
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

	public boolean createLevelData()
	{
		System.out.println("Width / Height: " + width + " / " + height);
		level = new char[width][height];
		Enumeration<?> keyEnum = props.propertyNames();
		while (keyEnum.hasMoreElements())
		{
			String key = (String) keyEnum.nextElement();
			if (key.contains(","))
			{
				System.out.println("Key: " + key);
				String val = props.getProperty(key);
				String[] keyCoordinates = key.split(",");
				int x = Integer.parseInt(keyCoordinates[0]);
				int y = Integer.parseInt(keyCoordinates[1]);
				if (val.charAt(0) == '2')
				{
					exits++;
				} else if (val.charAt(0) == '4')
				{
					dynamicEnemies++;
				}
				level[x][y] = val.charAt(0);
			}
		}
		return true;
	}

	public boolean loadProperties(String filename)
	{
		props = new Properties();
		try
		{
			FileInputStream in = new FileInputStream(filename);
			props.load(in);
			in.close();
			width = Integer.parseInt(props.getProperty("Width"));
			height = Integer.parseInt(props.getProperty("Height"));
			if (props.contains("regionX") && props.contains("regionY"))
			{
				int posX = Integer.parseInt(props.getProperty("posX"));
				int posY = Integer.parseInt(props.getProperty("posY"));
				Game.player = new Player(posX, posY);
				regionX = Integer.parseInt(props.getProperty("regionX"));
				regionY = Integer.parseInt(props.getProperty("regionY"));
				lives = Integer.parseInt(props.getProperty("lives"));
				score = Integer.parseInt(props.getProperty("score"));
				hasKey = Boolean.parseBoolean(props.getProperty("hasKey"));
			}
		} catch (IOException ex)
		{
			ex.printStackTrace();
			return false;
		} catch (Exception ex)
		{
			System.err.println(ex.getMessage());
			return false;
		}
		return true;
	}

}
