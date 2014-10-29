
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class PortraitSwirlyKyuSprite extends DialogPortraitSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	
	// CONSTRUCTOR
	
	public PortraitSwirlyKyuSprite(double x, double y, String text)
	{
		super(x,y, text);
		numInstances++;
	}
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		super.destroy();
		numInstances--;
	}
	
	/**
	*	loadImages()
	*	loads and stores image data for this Sprite.
	**/
	
	public static void loadImages(ImageLoader il)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		Image img;
		
		// 
		
		imageURL = PortraitSwirlyKyuSprite.class.getResource("graphics/SwirlyKyuPortraits.png");
		Image portraitSheet = tk.getImage(imageURL);
		portraitSheet = ColorFilters.setTransparentColor(portraitSheet, new Color(0xFF00FF));
		
		img = ImageBlitter.crop(portraitSheet,1,1,247,300);
		il.addImage(img);
		focusTable.put(":)",new Point(0,0));
		imageTable.put(":)",img);
		
		img = ImageBlitter.crop(portraitSheet,250,1,247,300);
		il.addImage(img);
		focusTable.put(">:(",new Point(0,0));
		imageTable.put(">:(",img);
		
		img = ImageBlitter.crop(portraitSheet,499,1,247,300);
		il.addImage(img);
		focusTable.put(":D",new Point(0,0));
		imageTable.put(":D",img);
		
		img = ImageBlitter.crop(portraitSheet,748,1,247,300);
		il.addImage(img);
		focusTable.put("kyu",new Point(0,0));
		imageTable.put("kyu",img);
		
		img = ImageBlitter.crop(portraitSheet,1,303,281,212);
		il.addImage(img);
		focusTable.put("attack",new Point(100,100));
		imageTable.put("attack",img);
		
		// preload semi-transparencies
		
		for(double i = 0.0; i < 1.0 ; i+= 0.05)
		{
			double keyTag = (Math.round(i*256)/256.0);
			String result = "attack" + "st" + keyTag;
			
			Image curImage = imageTable.get("attack");
			if(!imageTable.containsKey(result))
			{
				Image semicurImage = ColorFilters.setSemiTransparency(curImage, i);
				imageTable.put(result, semicurImage);
				il.addImage(semicurImage);
			}
		}
		
		
		System.out.println("loaded image data for PortraitCitrusSprite");

	}
	
	
	/**
	*	clean()
	*	Used to unload image data and cleans up static members of this Sprite extension 
	*		when the parent component is done with it.
	**/
	
	public static void clean()
	{
		numInstances = 0;
		focusTable.clear();
		imageTable.clear();
	}
	
	
	
	
	// RENDERING METHODS
	
	
	
	/**
	*	animate()
	*	Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
	*	Preconditions: none.
	*	Postconditions: curImage is set to the image of this Sprite's current animation frame 
	*		and necessary values for computing the next frame of animation are prepared.
	**/
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		if(!isVisible)
			return;
		
		String imgKey = emote;
		curImage = imageTable.get(imgKey);
		
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
		
		width = 100;
		height = 33;
	}
	
	
	
	
	public void spellCardPortrait()
	{
		if(this.vars[2] == 10)
		{
			x = 240;
			y = 100;
			vars[3] = 0.0;
			this.emote = "attack";
		}
		
		if(this.vars[2] >= 1.9)
		{
			if(vars[3] < 0.7)
				vars[3] += 0.05;
			y+=0.5;
		}
		else if(this.vars[2] >= 0.5)
		{
			y+=0.5;
			vars[3] -= 0.05;
		}
		else
		{
			this.scale(1,1);
			x = 450;
			y = 200;
			emote = ":)";
		}
		
		this.setSemiTransparency(1-vars[3]);
		this.vars[2] *= .98;
		
	}
	
	
	
	
	
}
