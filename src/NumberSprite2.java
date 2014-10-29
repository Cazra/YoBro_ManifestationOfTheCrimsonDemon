
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class NumberSprite2 extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private char type;
	private int frameNum;
	public int value;
	private String animString;
	
	// CONSTRUCTOR
	
	public NumberSprite2(double x, double y, char t)
	{
		super(x,y);
		numInstances++;
		
		this.type = t;
		
		//this.value = initVal;
		animString = "";
	}
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		this.isDestroyed = true;
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
		
		// digits
		
		imageURL = NumberSprite2.class.getResource("graphics/LevelInterfaceIcons.png");
		Image hudSheet = tk.getImage(imageURL);
		hudSheet = ColorFilters.setTransparentColor(hudSheet, new Color(0xFF00FF));
		
		img = ImageBlitter.crop(hudSheet,1,41,10,16);
		il.addImage(img);
		focusTable.put("0",new Point(0,0));
		imageTable.put("0",img);
		
		img = ImageBlitter.crop(hudSheet,13,41,8,16);
		il.addImage(img);
		focusTable.put("1",new Point(0,0));
		imageTable.put("1",img);
		
		img = ImageBlitter.crop(hudSheet,23,41,10,16);
		il.addImage(img);
		focusTable.put("2",new Point(0,0));
		imageTable.put("2",img);
		
		img = ImageBlitter.crop(hudSheet,35,41,10,16);
		il.addImage(img);
		focusTable.put("3",new Point(0,0));
		imageTable.put("3",img);
		
		img = ImageBlitter.crop(hudSheet,47,41,10,16);
		il.addImage(img);
		focusTable.put("4",new Point(0,0));
		imageTable.put("4",img);
		
		img = ImageBlitter.crop(hudSheet,59,41,10,16);
		il.addImage(img);
		focusTable.put("5",new Point(0,0));
		imageTable.put("5",img);
		
		img = ImageBlitter.crop(hudSheet,71,41,10,16);
		il.addImage(img);
		focusTable.put("6",new Point(0,0));
		imageTable.put("6",img);
		
		img = ImageBlitter.crop(hudSheet,83,41,10,16);
		il.addImage(img);
		focusTable.put("7",new Point(0,0));
		imageTable.put("7",img);
		
		img = ImageBlitter.crop(hudSheet,95,41,10,16);
		il.addImage(img);
		focusTable.put("8",new Point(0,0));
		imageTable.put("8",img);
		
		img = ImageBlitter.crop(hudSheet,107,41,10,16);
		il.addImage(img);
		focusTable.put("9",new Point(0,0));
		imageTable.put("9",img);
		
		img = ImageBlitter.crop(hudSheet,119,41,10,16);
		il.addImage(img);
		focusTable.put("-",new Point(0,0));
		imageTable.put("-",img);

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
	*	Special animation 
	**/
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		String imgKey = "" + type;
		curImage = imageTable.get(imgKey);
		
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
		
		width = 60;
		height = 20;
	}
	
	
	
	
	
}
