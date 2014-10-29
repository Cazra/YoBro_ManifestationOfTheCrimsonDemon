
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Hashtable;
import java.net.URL;
import gameEngine.*;

public class MenuBackgroundsSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public char type;
	
	// CONSTRUCTOR
	
	public MenuBackgroundsSprite(double x, double y, char t)
	{
		super(x,y);
		numInstances++;
		
		type = t;
	}
	
	public MenuBackgroundsSprite(double x, double y)
	{
		this(x,y,'1');
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
	
	public static void loadImages(ImageLoader imgLoader)
	{
		// get our default toolkit

		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		Image img;

		imageURL = MenuBackgroundsSprite.class.getResource("graphics/TitleMenuBG.png");
		Image titleImg = tk.getImage(imageURL);
		imgLoader.addImage(titleImg);
		focusTable.put("menuBg",new Point(320,240));
		imageTable.put("menuBg",titleImg);
		
		imageURL = MenuBackgroundsSprite.class.getResource("graphics/TitleMenuBG2.png");
		titleImg = tk.getImage(imageURL);
		imgLoader.addImage(titleImg);
		focusTable.put("menuBg2",new Point(320,240));
		imageTable.put("menuBg2",titleImg);
		
		imageURL = MenuBackgroundsSprite.class.getResource("graphics/BGCredits.png");
		titleImg = tk.getImage(imageURL);
		imgLoader.addImage(titleImg);
		focusTable.put("creditsBg",new Point(320,240));
		imageTable.put("creditsBg",titleImg);
		
		System.out.println("loaded image data for MenuBackgroundsSprite");
		
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
		
		if(type == '1')
		{
			curImage = imageTable.get("menuBg");
			Point curFocus = focusTable.get("menuBg");
			
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 640;
			height = 480;
		}
		if(type == '2')
		{
			curImage = imageTable.get("menuBg2");
			Point curFocus = focusTable.get("menuBg2");
			
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 640;
			height = 480;
		}
		if(type == 'C')
		{
			curImage = imageTable.get("creditsBg");
			Point curFocus = focusTable.get("creditsBg");
			
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 640;
			height = 480;
		}
	}
	
	
	
}
