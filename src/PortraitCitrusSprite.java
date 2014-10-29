
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


public class PortraitCitrusSprite extends DialogPortraitSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	
	// CONSTRUCTOR
	
	public PortraitCitrusSprite(double x, double y, String text)
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
		
		imageURL = PortraitCitrusSprite.class.getResource("graphics/CitrusPortraits.png");
		Image portraitSheet = tk.getImage(imageURL);
		portraitSheet = ColorFilters.setTransparentColor(portraitSheet, new Color(0xFF00FF));
		
		img = ImageBlitter.crop(portraitSheet,1,1,247,280);
		il.addImage(img);
		focusTable.put(":)",new Point(0,0));
		imageTable.put(":)",img);
		
		img = ImageBlitter.crop(portraitSheet,250,1,247,280);
		il.addImage(img);
		focusTable.put(">:(",new Point(0,0));
		imageTable.put(">:(",img);
		
		img = ImageBlitter.crop(portraitSheet,499,1,247,280);
		il.addImage(img);
		focusTable.put(":D",new Point(0,0));
		imageTable.put(":D",img);
		
		img = ImageBlitter.crop(portraitSheet,748,1,206,280);
		il.addImage(img);
		focusTable.put("attack",new Point(100,100));
		imageTable.put("attack",img);
		
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
			x = 450+100;
			y = 100+100;
			this.emote = "attack";
			this.setSemiTransparency(0.5);
		}
		
		if(this.vars[2] >= 1.9)
		{
			x -= this.vars[2];
		}
		else if(this.vars[2] < 0.5)
		{
			this.scale(1,1);
			this.setSemiTransparency(0.0);
			x = 450;
			y = 200;
			emote = ":)";
		}
		else
		{
			this.scale(scaleX+0.05,scaleY+0.05);
		//	this.setSemiTransparency(semiTransparency*1.05);
		}
		
		this.vars[2] *= .98;
		
	}
	
	
	
	
	
}
