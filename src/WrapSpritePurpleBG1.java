import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.util.Hashtable;
import java.net.URL;
import gameEngine.*;

class WrapSpritePurpleBG1 extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public WrapSpritePurpleBG1(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
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
	
	public static void loadImages(ImageLoader imgLoader)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		
		// load our images
		
		imageURL = WrapSpritePurpleBG1.class.getResource("graphics/purpleBlurBG.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for WrapSpritePurpleBG1");
		
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
	
	public void animate(ImageLoader il)
	{
		super.animate(il);
		
		curImage = imageTable.get("testImg");
		Point curFocus = focusTable.get("testImg");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		
	}
	
	
	
	
	
}
