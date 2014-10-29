import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

import gameEngine.*;
import java.util.Hashtable;
import java.net.URL;


public class StageTextMakerLevel extends Level
{	
	// sprites
	
	private StageTextSprite txt;


	public StageTextMakerLevel(Component parent)
	{
		super(parent);
	}
	
	/**
	*	loadData()
	*	loads image and sound data for this level into memory. This method should be called before 
	*	running the running the level's timer loop and render methods for the first time. 
	**/
	
	public void loadData()
	{
		// load image data
		
		StageTextSprite.loadImages(imgLoader);
		txt = new StageTextSprite(320,240,'t',"Unknown Executive Floor");
		imgLoader.waitForAll();
	}
	
	/**
	*	clean()
	*	This method unloads the level's image and sound data from memory. Any other memory management clean-up for
	*	the level is also taken care of here.
	*
	**/
	
	public void clean()
	{
		super.clean();
		StageTextSprite.clean();
	}
	
	/**
	*	timerLoop()
	*	This method steps through one animation frame for the level. 
	*	This method should be called by the game's timer event handler before rendering the level and after loadData is used.
	*
	**/
	
	public void timerLoop()
	{
		txt.animate(imgLoader);
		imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
	}
	
	/**
	*	render(Graphics2D g2D
	*
	*
	**/
	
	public void render(Graphics2D g2D)
	{
		g2D.setColor(new Color(0xff00ff));
		g2D.fillRect(0,0,640,480);
		// Background graphic
		
		txt.render(g2D);
		
		// text
		
		g2D.setColor(new Color(0x000000));
		
	
		
		
	}
	
}

