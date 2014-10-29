
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Hashtable;
import java.net.URL;
import gameEngine.*;

public class BasicBulletSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	public double semiAngle;
	
	// CONSTRUCTOR
	
	public BasicBulletSprite(double x, double y)
	{
		super(x,y);
		numInstances++;
		semiAngle = 0;
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
		
		imageURL = BasicBulletSprite.class.getResource("graphics/basicBullet.png");
		Image bullet1Img = tk.getImage(imageURL);
		bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("bullet1",new Point(8,8));
		
		// store them into our hashtables
		
		imageTable.put("bullet1",bullet1Img);
		
		System.out.println("loaded image data for BasicBulletSprite");
		
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
		curImage = imageTable.get("bullet1");
		Point curFocus = focusTable.get("bullet1");
		
		
		this.semiAngle += Math.PI/64;
			if(this.semiAngle > Math.PI * 2)
				this.semiAngle -= Math.PI * 2;
		setSemiTransparency(Math.sin(semiAngle));
		
		animateSemiTransparencyFrame(il, "bullet1");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		width = 16;
		height = 16;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		
		
	}
	
	
	/**
	*	animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	*	Applies current semi-transparency to a frame of animation and stores the modified frame for later use.
	*	Preconditions: il is the ImgLoader passed in by animate(ImageLoader il).
	*		imgKey is the normal unmodified key name for the current frame of animation.
	*	Postconditions: the current image is modified with the current semitransparency. 
	*		If this semitransparent frame isn't already in the imageTable, it is added in.
	**/
	
	protected void animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	{
		double keyTag = (Math.round(this.semiTransparency*256)/256.0);
		if(imageTable.containsKey(imgKey + "semiTrans" + keyTag))
		{
			curImage = imageTable.get(imgKey + "semiTrans" + keyTag);
		}
		else
		{
			Image semicurImage = ColorFilters.setSemiTransparency(curImage, this.semiTransparency);
			imageTable.put(imgKey + "semiTrans" + keyTag, semicurImage);
			
			curImage = semicurImage;
			il.addImage(curImage);
		}
	}
	
	
	
}
