
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class ItemSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public char type;
	public boolean isMagneted;
	
	public double dx;
	public double dy;
	
	
	// CONSTRUCTOR
	
	public ItemSprite(double x, double y, char t)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		this.setRadius(10);
		this.isMagneted = false;
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
		
		// load our images
		
		imageURL = ItemSprite.class.getResource("graphics/PowerupsSheet.png");
		Image itemSheet = tk.getImage(imageURL);
		itemSheet = ColorFilters.setTransparentColor(itemSheet, new Color(0xFF00FF));
		
		// Power+
		
		img = ImageBlitter.crop(itemSheet,0,0,17,17); // 'P'
		il.addImage(img);
		focusTable.put("powerBig",new Point(8,8));
		imageTable.put("powerBig",img);
		
		img = ImageBlitter.crop(itemSheet,17,0,17,17); // 'F'
		il.addImage(img);
		focusTable.put("powerFull",new Point(8,8));
		imageTable.put("powerFull",img);
		
		img = ImageBlitter.crop(itemSheet,0,17,11,11); // 'p'
		il.addImage(img);
		focusTable.put("powerSmall",new Point(5,5));
		imageTable.put("powerSmall",img);
		
		// score points
		
		img = ImageBlitter.crop(itemSheet,11,17,11,11); // blue ghosty points 'S'
		il.addImage(img);
		focusTable.put("point1",new Point(5,5));
		imageTable.put("point1",img);
		
		img = ImageBlitter.crop(itemSheet,22,17,11,11); // off-green starry points 's'
		il.addImage(img);
		focusTable.put("point2",new Point(5,5));
		imageTable.put("point2",img);
		
		// bomb
		
		img = ImageBlitter.crop(itemSheet,34,0,17,17); // 'B'
		il.addImage(img);
		focusTable.put("bomb",new Point(8,8));
		imageTable.put("bomb",img);
		
		// 1up
		
		img = ImageBlitter.crop(itemSheet,51,0,17,17); // '1'
		il.addImage(img);
		focusTable.put("1up",new Point(8,8));
		imageTable.put("1up",img);
		
		System.out.println("loaded image data for ItemSprite");
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
	
	
	// LOGIC METHODS
		
		
	public boolean isValidType()
	{
		if(type == 'p')
			return true;
		if(type == 'P')
			return true;
		if(type == 'F')
			return true;
		if(type == 's')
			return true;
		if(type == 'S')
			return true;
		if(type == 'B')
			return true;
		if(type == '1')
			return true;
		
		return false;
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
		
		if(type == 'p') // small power up
		{
			String imgKey = "powerSmall";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 11;
			height = 11;
		}
		if(type == 'P') // Big power up
		{
			String imgKey = "powerBig";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
		}
		if(type == 'F') // Full power
		{
			String imgKey = "powerFull";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 11;
			height = 11;
		}
		if(type == 'S') // ghosty point
		{
			String imgKey = "point1";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 11;
			height = 11;
		}
		if(type == 's') // starry point
		{
			String imgKey = "point2";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 11;
			height = 11;
		}
		if(type == 'B') // Bomb
		{
			String imgKey = "bomb";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
		}
		if(type == '1') // 1up
		{
			String imgKey = "1up";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
		}
		
		//il.addImage(this.curImage);
	}
	
	
	
	
	
}
