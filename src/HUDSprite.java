
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class HUDSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private char type;
	private int frameNum;
	public int value; // the number of bombs or lives
	
	// CONSTRUCTOR
	
	public HUDSprite(double x, double y, char t)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		value = 0;
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
		
		// enemy robot 1
		
		imageURL = HUDSprite.class.getResource("graphics/LevelInterfaceIcons.png");
		Image hudSheet = tk.getImage(imageURL);
		hudSheet = ColorFilters.setTransparentColor(hudSheet, new Color(0xFF00FF));
		
		imageURL = HUDSprite.class.getResource("graphics/levelInterfaceMask.png");
		Image hudMask = tk.getImage(imageURL);
		hudMask = ColorFilters.setTransparentColor(hudMask, new Color(0xFF00FF));
		il.addImage(hudMask);
		focusTable.put("hudMask",new Point(0,0));
		imageTable.put("hudMask",hudMask);
		
		
		
		
		img = ImageBlitter.crop(hudSheet,1,1,60,20);
		il.addImage(img);
		focusTable.put("lives:",new Point(0,0));
		imageTable.put("lives:",img);
		
		img = ImageBlitter.crop(hudSheet,63,1,60,20);
		il.addImage(img);
		focusTable.put("bombs:",new Point(0,0));
		imageTable.put("bombs:",img);
		
		img = ImageBlitter.crop(hudSheet,125,1,60,20);
		il.addImage(img);
		focusTable.put("power:",new Point(0,0));
		imageTable.put("power:",img);
		
		img = ImageBlitter.crop(hudSheet,187,1,60,20);
		il.addImage(img);
		focusTable.put("graze:",new Point(0,0));
		imageTable.put("graze:",img);
		
		img = ImageBlitter.crop(hudSheet,249,1,60,20);
		il.addImage(img);
		focusTable.put("score:",new Point(0,0));
		imageTable.put("score:",img);
		
		// 
		
		img = ImageBlitter.crop(hudSheet,1,23,16,16);
		il.addImage(img);
		focusTable.put("life",new Point(0,0));
		imageTable.put("life",img);
		
		img = ImageBlitter.crop(hudSheet,19,23,16,16);
		il.addImage(img);
		focusTable.put("bomb",new Point(0,0));
		imageTable.put("bomb",img);
		
		// enemy here icon
		
		img = ImageBlitter.crop(hudSheet,37,23,40,16);
		il.addImage(img);
		focusTable.put("enemyHere",new Point(20,0));
		imageTable.put("enemyHere",img);
		
		System.out.println("loaded image data for HUDSprite");
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
		
		if(type == 'a') // lives
		{
			String imgKey = "lives:";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 60;
			height = 20;
		}
		if(type == 'b') // bombs
		{
			String imgKey = "bombs:";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 60;
			height = 20;
		}
		if(type == 'c') // power
		{
			String imgKey = "power:";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 60;
			height = 20;
		}
		if(type == 'd') // graze
		{
			String imgKey = "graze:";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 60;
			height = 20;
		}
		if(type == 'e') // lives
		{
			String imgKey = "score:";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 60;
			height = 20;
		}
		

		if(type == 'A') // HUD mask
		{
			String imgKey = "hudMask";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 640;
			height = 480;
		}
		
		
		if(type == 'B') // life icon
		{
			String imgKey = "life";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 16;
			height = 16;

		}
		
		
		if(type == 'C') // bomb icon
		{
			String imgKey = "bomb";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 16;
			height = 16;
		}
		
		if(type == 'E') // Enemy here marker
		{
			String imgKey = "enemyHere";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 40;
			height = 16;
		}

	}
	
	
	
	/**
	*	Overridden draw(Graphics2D g) method
	**/
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		if(this.type == 'B' || this.type == 'C')
		{
			for(int i = 0; i < this.value; i++)
			{
				g.drawImage(this.curImage, null, null);
				g.translate(16,0);
			}
		}
		else
			super.draw(g);
		
		
		
	}
	
	
	
}
