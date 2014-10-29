import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.awt.event.KeyEvent;

import gameEngine.*;

public class ConfigYoBro implements Serializable
{
	// keyboard mappings
	
	public int VK_UP;
	public int VK_DOWN;
	public int VK_LEFT;
	public int VK_RIGHT;
	public int VK_SHOT;
	public int VK_BOMB;
	public int VK_SLOW;
	public int VK_SKIP;
	public int VK_PAUSE;
	
	// lives
	
	public int startingLives;
	
	// sound
	
	public double musicVol;
	public double soundVol;
	
	public ConfigYoBro()
	{
		try
		{
			FileInputStream fis = new FileInputStream("YoBro.config");
			ObjectInputStream ois = new ObjectInputStream(fis);
			ConfigYoBro readObj = (ConfigYoBro) ois.readObject();
			ois.close();
			fis.close();
			
			//  values for keyboard configuration. Comment this part out when keyboard configuration is implemented.
			VK_UP = readObj.VK_UP;
			VK_DOWN = readObj.VK_DOWN;
			VK_LEFT = readObj.VK_LEFT;
			VK_RIGHT = readObj.VK_RIGHT;
			VK_SHOT = readObj.VK_SHOT;
			VK_BOMB = readObj.VK_BOMB;
			VK_SLOW = readObj.VK_SLOW;
			VK_SKIP = readObj.VK_SKIP;
			VK_PAUSE = readObj.VK_PAUSE;
			
			//  starting lives
			
			startingLives = readObj.startingLives;
			
			//  sound volumes
			
			musicVol = readObj.musicVol;
			soundVol = readObj.soundVol;
			
			System.out.println("Config file read successful");
			
		}
		catch(Exception e)
		{
			restoreDefaults();
			System.out.println("Config file read failed: Initialized default configurations.");
		}
	}
	
	
	public ConfigYoBro(ConfigYoBro orig)
	{
		// temporary values for keyboard configuration. Comment this part out when keyboard configuration is implemented.
		VK_UP = orig.VK_UP;
		VK_DOWN = orig.VK_DOWN;
		VK_LEFT = orig.VK_LEFT;
		VK_RIGHT = orig.VK_RIGHT;
		VK_SHOT = orig.VK_SHOT;
		VK_BOMB = orig.VK_BOMB;
		VK_SLOW = orig.VK_SLOW;
		VK_SKIP = orig.VK_SKIP;
		VK_PAUSE = orig.VK_PAUSE;
		
		// temp starting lives
		
		startingLives = orig.startingLives;
		
		// temp sound volumes
		
		musicVol = orig.musicVol;
		soundVol = orig.soundVol;
	}
	
	public void restoreDefaults()
	{
		// temporary values for keyboard configuration. Comment this part out when keyboard configuration is implemented.
		VK_UP = KeyEvent.VK_UP;
		VK_DOWN = KeyEvent.VK_DOWN;
		VK_LEFT = KeyEvent.VK_LEFT;
		VK_RIGHT = KeyEvent.VK_RIGHT;
		VK_SHOT = KeyEvent.VK_Z;
		VK_BOMB = KeyEvent.VK_X;
		VK_SLOW = KeyEvent.VK_SHIFT;
		VK_SKIP = KeyEvent.VK_ENTER;
		VK_PAUSE = KeyEvent.VK_ESCAPE;
		
		// temp starting lives
		
		startingLives = 3;
		
		// temp sound volumes
		
		musicVol = 1.0;
		soundVol = 1.0;
	}
	
	public void saveConfig()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream("YoBro.config");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this);
			oos.close();
			System.out.println("Config file write success.");
		}
		catch(Exception e)
		{
			System.out.println("Config file write failed: \n" + e.getMessage() + "\n" + e.getStackTrace());
		}
	}
	
	public boolean isVKTaken(int keycode)
	{
		if(keycode == VK_SHOT)
			return true;
		if(keycode == VK_BOMB)
			return true;
		return false;
	}
	
	public boolean hasKeyConflicts()
	{
		if(VK_SHOT == VK_BOMB || VK_SHOT == VK_SLOW || VK_SHOT == VK_SKIP || VK_SHOT == VK_PAUSE)
			return true;
		if(VK_BOMB == VK_SHOT || VK_BOMB == VK_SLOW || VK_BOMB == VK_SKIP || VK_BOMB == VK_PAUSE)
			return true;
		if(VK_SLOW == VK_SHOT || VK_SLOW == VK_BOMB || VK_SLOW == VK_SKIP || VK_SLOW == VK_PAUSE)
			return true;	
		if(VK_SKIP == VK_SHOT || VK_SKIP == VK_BOMB || VK_SKIP == VK_SLOW || VK_SKIP == VK_PAUSE)
			return true;
		if(VK_PAUSE == VK_SHOT || VK_PAUSE == VK_BOMB || VK_PAUSE == VK_SLOW || VK_PAUSE == VK_SKIP)
			return true;
		
		return false;
	}
	
	
	
	public static void main(String[] args)
	{
		ConfigYoBro config = new ConfigYoBro();
		Fileinput input = new Fileinput();

		System.out.println("StartingLives:" + config.startingLives);
		
		int lives = 1;

		System.out.println("Enter new lives: ");
		lives = input.getInt();
			
		config.startingLives = lives;
		
		config.saveConfig();
				
		
	}
}
