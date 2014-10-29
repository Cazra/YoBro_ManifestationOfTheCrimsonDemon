import java.awt.GraphicsConfiguration;

import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.awt.Image;
import java.awt.image.*;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class ABGStage1Sprite extends AnimatedBackgroundSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();

	private Stage1Mode7BG mode7BG;
	private WrapSpritePurpleBG1 purpleWrapBG;
	private ABGstage1Grapevine grapevineBG;
	private LemonRaveWrapBG lemonRaveBG;
	private BerryWrapBG berryBG;
	
	private LinearGradientPaint fog;
	
	// CONSTRUCTOR
	
	public ABGStage1Sprite(MainLevel ml)
	{
		super(ml);
		numInstances++;
		
		mode7BG = new Stage1Mode7BG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG.cameraX = 400;
		mode7BG.cameraY = 100;
		mode7BG.horizonY = 100;
		mode7BG.camDist = 200;
		mode7BG.elevation = 500;
		mode7BG.cameraAngle = 270;
	//	mode7BG.blurTechnique = 2;
	
		purpleWrapBG = new WrapSpritePurpleBG1(0,0,ml.SCREENW/2,ml.SCREENH/2);
		purpleWrapBG.cameraY = 470/2;
		
		grapevineBG = new ABGstage1Grapevine(0,0);
		
		lemonRaveBG = new LemonRaveWrapBG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		
		berryBG = new BerryWrapBG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		
		Point2D start = new Point2D.Float(0, 0);
		Point2D end = new Point2D.Float(0, ml.SCREENH);
		float[] dist = {0.0f, 1.0f};
		Color[] colors = {new Color(0xAA220055,true), new Color(0x00220055,true)};
		fog = new LinearGradientPaint(start, end, dist, colors);
	}
	
	
	// MEM MANAGEMENT METHODS
	
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
		
		Stage1Mode7BG.clean();
		WrapSpritePurpleBG1.clean();
		ABGstage1Grapevine.clean();
	}
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		super.destroy();
		numInstances--;
		mainLevel = null;
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
		/*
		imageURL = ABGStage1Sprite.class.getResource("graphics/BGRoadNight.png");
		Image roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("road",new Point(0,0));
		imageTable.put("road",roadImg);*/
		
		
		Stage1Mode7BG.loadImages(il);
		WrapSpritePurpleBG1.loadImages(il);
		ABGstage1Grapevine.loadImages(il);
		LemonRaveWrapBG.loadImages(il);
		BerryWrapBG.loadImages(il);
	}
	

	// LOGIC METHODS

	// RENDERING METHODS
	
	public boolean render(Graphics2D g)
	{	
		super.render(g);
		if(curBGID == 0 || nextBGID == 0)
		{
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			mode7BG.render(g);
			g.setTransform(curTrans);
			
			g.setPaint(fog);
			g.fillRect(0,0,mainLevel.SCREENW,mainLevel.SCREENH);
		}
		if(curBGID == 1 || nextBGID == 1)
		{
			if(curBGID != 1)
			{
				purpleWrapBG.setSemiTransparency(1-this.transitionPercent);
				grapevineBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 1)
			{
				purpleWrapBG.setSemiTransparency(this.transitionPercent);
				grapevineBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				purpleWrapBG.setSemiTransparency(0.0);
				grapevineBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			purpleWrapBG.render(g);
			grapevineBG.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 2 || nextBGID == 2)
		{
			if(curBGID != 2)
			{
				lemonRaveBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 2)
			{
				lemonRaveBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				lemonRaveBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			lemonRaveBG.render(g);
			g.setTransform(curTrans);
		}
		if(curBGID == 3 || nextBGID == 3)
		{
			if(curBGID != 3)
			{
				berryBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 3)
			{
				berryBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				berryBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			berryBG.render(g);
			g.setTransform(curTrans);
		}
		
		return true;
	}
	
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		runTransition();
		if(curBGID == 0 || nextBGID == 0)
		{
			mode7BG.animate(il);
		}
		purpleWrapBG.animate(il);
		purpleWrapBG.cameraX+=3;
		grapevineBG.animate(il);
		lemonRaveBG.animate(il);
		lemonRaveBG.cameraAngle+=2;
		berryBG.animate(il);
		berryBG.cameraX+=1;
		berryBG.cameraY-=2;
		
	}
	
}







class Stage1Mode7BG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public Stage1Mode7BG(double x, double y, double w, double h)
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
	
	public static void loadImages(ImageLoader il)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		
		// load our images
		
		imageURL = Stage1Mode7BG.class.getResource("graphics/BGRoadNight.png");
		Image roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("road",new Point(0,0));
		imageTable.put("road",roadImg);
		
		System.out.println("loaded image data for Mode7");
		
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
		
		
		curImage = imageTable.get("road");
		Point curFocus = focusTable.get("road");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		
		
		cameraY+= 2;
		
		
	}
	
	
	
	
	
}




class ABGstage1Grapevine extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	private int[] pixels;
	private int pixWidth,pixHeight;
	
	// CONSTRUCTOR
	
	public ABGstage1Grapevine(double x, double y)
	{
		super(x,y);
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
		
		imageURL = ABGstage1Grapevine.class.getResource("graphics/AssassinGrapeVineBG1.png");
		Image bullet1Img = tk.getImage(imageURL);
		bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("bullet1",new Point(0,0));
		
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
	*	void draw(Graphics2D g)
	*	Preconditions: g is the parent context's graphics handle.
	*	Postconditions: the sprite is drawn in a width*height rectangular viewport such that the sprite 
	*		image wraps around infinitely and can be zoomed, panned, and rotated.
	**/
	
	protected void draw(Graphics2D g)
	{
		// obtain the source image's pixels
		
		if(pixels == null)
		{
			PixelGrabber pg = new PixelGrabber(this.curImage, 0,0, -1, -1, false);
			try
			{
				pg.grabPixels();
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
			
			pixWidth = pg.getWidth();
			pixHeight = pg.getHeight();
			
			pixels = (int[]) pg.getPixels();
		}
	
		// obtain the raster that we will write the pixels of our resulting image to
		
		width = pixWidth;
		height = pixHeight;
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height,BufferedImage.TYPE_INT_ARGB);
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 

		// process each pixel in the sprite's viewport.
		
		for(int j = 0; j < height; j++)
		{
			for(int i = 0; i <width; i++)
			{	
				int pixX = i % pixWidth;
				int pixY = j % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				int pixelColor =  pixels[pixY*pixWidth + pixX];
				int alpha = (pixelColor >> 24) & 0xff;
				if(alpha > 0)
					alpha = (int)(alpha*(1-semiTransparency)*0.5);
				pixelColor = (pixelColor & 0x00ffffff) + (alpha << 24);
				
				try
				{
					writePixels[(int)width*j+i] = pixelColor;
				}
				catch(Exception ex)
				{
					System.out.println("WrappingImage error: \n" + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		
		// draw the resulting mode7 image onto the scene.
		
		g.drawImage(mode7Img, null, null);
	}
	
	
	
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
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		
		
	}
}


class LemonRaveWrapBG extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public LemonRaveWrapBG(double x, double y, double w, double h)
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
		
		imageURL = LemonRaveWrapBG.class.getResource("graphics/LemonRaveBG.png");
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



class BerryWrapBG extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public BerryWrapBG(double x, double y, double w, double h)
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/BerryBerryerBG.png");
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




