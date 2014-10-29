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


public class ABGStage4Sprite extends AnimatedBackgroundSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();

	public Stage4Mode7City mode7BG;
	private LinearGradientPaint fog;
	
	public Stage4CloudyBG mode7BG2;
	
	public Stage4MonolithBG sunsetBG;
	public Stage4Mode7CloudBG mode7Clouds;
	
	public GravityBG1 gravityBG;
	public GravityBG2 gravityBG2;
	
	public SpaceWarpBG spaceWarpBG;
	
	// CONSTRUCTOR
	
	public ABGStage4Sprite(MainLevel ml)
	{
		super(ml);
		numInstances++;
		
		mode7BG = new Stage4Mode7City(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG.cameraX = 128;
		mode7BG.cameraY = 100;
		mode7BG.horizonY = 50;
		mode7BG.camDist = 200;
		mode7BG.elevation = 250;
		mode7BG.cameraAngle = 90;
		mode7BG.scale(2.0,2.0);
		
		mode7BG2 = new Stage4CloudyBG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG2.horizonY = 50;
		mode7BG2.camDist = 200;
		mode7BG2.elevation = 250;
		mode7BG2.cameraAngle = 90;
		mode7BG2.scale(2.0,2.0);
		
		sunsetBG = new Stage4MonolithBG(0,0);
		mode7Clouds = new Stage4Mode7CloudBG(0,ml.SCREENH/4,ml.SCREENW/2,ml.SCREENH/7);
		mode7Clouds.scale(2.0,2.0);
		
		Point2D start = new Point2D.Float(0, 0);
		Point2D end = new Point2D.Float(0, ml.SCREENH);
		float[] dist = {0.0f, 1.0f};
		Color[] colors = {new Color(0xFF552288,true), new Color(0x00552288,true)};
		fog = new LinearGradientPaint(start, end, dist, colors);
	
		gravityBG = new GravityBG1(0,0,ml.SCREENW/2,ml.SCREENH/2);
		gravityBG2 = new GravityBG2(0,0,ml.SCREENW/3,ml.SCREENH/3, ml.player);
	
		spaceWarpBG = new SpaceWarpBG(0,0,ml.SCREENW/3,ml.SCREENH/3, ml.player);
	
		vars[0] = 0.5;
		
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
		
		Stage4Mode7City.clean();
		Stage4CloudyBG.clean();
		Stage4MonolithBG.clean();
		Stage4Mode7CloudBG.clean();
		GravityBG1.clean();
		GravityBG2.clean();
		SpaceWarpBG.clean();
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
		
		Stage4Mode7City.loadImages(il);
		Stage4CloudyBG.loadImages(il);
		Stage4MonolithBG.loadImages(il);
		Stage4Mode7CloudBG.loadImages(il);
		GravityBG1.loadImages(il);
		GravityBG2.loadImages(il);
		SpaceWarpBG.loadImages(il);
	}
	

	// LOGIC METHODS

	// RENDERING METHODS
	
	public boolean render(Graphics2D g)
	{	
		super.render(g);
		if(curBGID == 0 || nextBGID == 0)
		{
			mode7BG.render(g);
			g.setPaint(fog);
			g.fillRect(0,0,mainLevel.SCREENW,mainLevel.SCREENH);
		
		}
		if(curBGID == 1 || nextBGID == 1)
		{
			if(curBGID != 1)
			{
				mode7BG2.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 1)
			{
				mode7BG2.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				mode7BG2.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			mode7BG2.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 2 || nextBGID == 2)
		{
			if(curBGID != 2)
			{
				sunsetBG.setSemiTransparency(1-this.transitionPercent);
				mode7Clouds.setSemiTransparency(1-this.transitionPercent);
				
			}
			else if(nextBGID != 2)
			{
				sunsetBG.setSemiTransparency(this.transitionPercent);
				mode7Clouds.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				sunsetBG.setSemiTransparency(0.0);
				mode7Clouds.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			sunsetBG.render(g);
			mode7Clouds.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 3 || nextBGID == 3)
		{
			if(curBGID != 3)
			{
				gravityBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 3)
			{
				gravityBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				gravityBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			gravityBG.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 4 || nextBGID == 4)
		{
			if(curBGID != 4)
			{
				gravityBG2.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 4)
			{
				gravityBG2.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				gravityBG2.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(3.0,3.0);
			gravityBG2.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 5 || nextBGID == 5)
		{
			if(curBGID != 5)
			{
				spaceWarpBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 5)
			{
				spaceWarpBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				spaceWarpBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(3.0,3.0);
			spaceWarpBG.render(g);
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
			mode7BG.cameraY -= vars[0];
		}
		if(curBGID == 1 || nextBGID == 1)
		{
			mode7BG2.animate(il);
			mode7BG2.cameraY -= 3;
		} 
		if(curBGID == 2 || nextBGID == 2)
		{
			sunsetBG.animate(il);
			mode7Clouds.animate(il);
		}
		if(curBGID == 3 || nextBGID == 3)
		{
			gravityBG.animate(il);
		}
		if(curBGID == 4 || nextBGID == 4)
		{
			gravityBG2.animate(il);
		}
		if(curBGID == 5 || nextBGID == 5)
		{
			spaceWarpBG.animate(il);
		}
		
		
	}
}







class Stage4Mode7City extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	protected int[] pixels2;
	int pixWidth2, pixHeight2;
	
	public Stage4Mode7City(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
		this.vars[0] = 0.5;
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
		
		imageURL = Stage3Mode7BG.class.getResource("graphics/BGCity.png");
		Image roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("city",new Point(0,0));
		imageTable.put("city",roadImg);
		
		
		imageURL = Stage3Mode7BG.class.getResource("graphics/BGClouds1.png"); 
		roadImg = tk.getImage(imageURL);
		Image alphaImg = ImageBlitter.crop(roadImg,300,0,300,300);
		Image colorImg = ImageBlitter.crop(roadImg,0,0,300,300);
		roadImg = ColorFilters.applyAlphaMap(colorImg,alphaImg,300,300);
		il.addImage(roadImg);
		focusTable.put("clouds",new Point(0,0));
		imageTable.put("clouds",roadImg);
		
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
	
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height - 1,BufferedImage.TYPE_INT_ARGB);
		
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
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
			
			
			pg = new PixelGrabber(imageTable.get("clouds"), 0,0, -1, -1, false);
			try
			{
				pg.grabPixels();
			}
			catch(Exception ex)
			{
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}
			
			pixWidth2 = pg.getWidth();
			pixHeight2 = pg.getHeight();
			
			pixels2 = (int[]) pg.getPixels();
		}
		
		
		// test code...
		
		double camDx = cameraX - camDist*GameMath.cos(cameraAngle);
		double camDy = cameraY + camDist*GameMath.sin(cameraAngle);
		
		double rCos = GameMath.cos(180-cameraAngle+90);
		double rSin = GameMath.sin(180-cameraAngle+90);
		
		//AffineTransform rotation = AffineTransform.getRotateInstance(GameMath.d2r(180-cameraAngle+90));
		double aspectRatio = width/height;
		double horizonCenter = width/2.0;
		
		for(int j = 1; j < height; j++)
		{
			
			for(int i = 0; i < width; i++)
			{	
				int[] pix = getPixelAt(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
				int[] cloudPix = getPixelAtClouds(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
				
				int pixX = pix[0];
				int pixY = pix[1];
				
				pixX = pixX % pixWidth;
				pixY = (pixY)% pixHeight;
				
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				
				
				int pixX2 = cloudPix[0];
				int pixY2 = cloudPix[1];

				pixX2 = pixX2 % pixWidth2;
				pixY2 = pixY2 % pixHeight2;
				
				if(pixX2 < 0)
					pixX2 = pixWidth2+pixX2;
				if(pixY2 < 0)
					pixY2 = pixHeight2+pixY2;
				
				
				
				int pixelColor =  pixels[pixY*pixWidth + pixX];
				int pixelColor2 = pixels2[pixY2*pixWidth2 + pixX2];
				
				int pix2Alpha = (pixelColor2 >> 24) & 0xff;
				int compR = (int) (pix2Alpha/255.0 * ((pixelColor2 >> 16) & 0xff) + (255-pix2Alpha)/255.0 * ((pixelColor >> 16) & 0xff)) ;
				int compG = (int) (pix2Alpha/255.0 * ((pixelColor2 >> 8) & 0xff) + (255-pix2Alpha)/255.0 * ((pixelColor >> 8) & 0xff));
				int compB = (int) (pix2Alpha/255.0 * ((pixelColor2) & 0xff)  + (255-pix2Alpha)/255.0 * ((pixelColor) & 0xff));
				pixelColor = (0xff << 24) + (compR << 16) + (compG << 8) + compB;
				
				try
				{
						writePixels[(int)width*(j-1)+i] = pixelColor;
				}
				catch(Exception ex)
				{
					System.out.println("Mode7 error: \n" + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
		
		// draw the resulting mode7 image onto the scene.
		
		g.drawImage(mode7Img, null, null);
	}
	
	
	protected int[] getPixelAtClouds(double i,double j,double camDx, double camDy, double rCos, double rSin, double horizonCenter, double aspectRatio)
	{
		double origElev = elevation;
		
		elevation -= 150;
		double z = (elevation)/(j+horizonY);
		double rasterX = (horizonCenter - i)*z;
		
		int pixX = (int) (rasterX);// % pixWidth;
		int pixY = (int) (elevation*z*aspectRatio);// % pixHeight;
		
		int rpixX = (int)(pixX*rCos - pixY*rSin + camDx);
		int rpixY = (int)(pixX*rSin + pixY*rCos + camDy);
		int[] result = {rpixX,rpixY};
		
		elevation = origElev;
		return result;
	}
	
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
		
		
		curImage = imageTable.get("city");
		Point curFocus = focusTable.get("city");

		fx = curFocus.x;
		fy = curFocus.y;
		
	}
}




class GravityBG1 extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public GravityBG1(double x, double y, double w, double h)
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/gravityBG1.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for GravityBG1");
		
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
		
		// obtain the raster that we will write the pixels of our resulting image to
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height,BufferedImage.TYPE_INT_ARGB);
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
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
		
		// compute any values that will be reused for each pixel.
		
		double camDx = cameraX;
		double camDy = cameraY;
		
		double rCos = GameMath.cos(cameraAngle);
		double rSin = GameMath.sin(cameraAngle);

		// process each pixel in the sprite's viewport.
		
		for(int j = 0; j < height; j++)
		{
			for(int i = 0; i < width; i++)
			{	
				Point pix = getPixelAt(i,j,camDx,camDy,rCos,rSin);
				
				int pixX = pix.x % pixWidth;
				int pixY = pix.y % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				int pixelColor =  pixels[pixY*pixWidth + pixX];
				
				float brightness = (pixelColor & 0x000000ff) / 255.0f;
				double colorOffset = brightness * 360 + j + vars[0];
				int hsbColor = Color.HSBtoRGB((float)(colorOffset/720.0), 1.0f, 0.4f * brightness + 0.1f);
				
				int alpha = (pixelColor >> 24) & 0xff;
				if(alpha > 0)
					alpha = (int)(alpha*(1-semiTransparency));
				pixelColor = (hsbColor & 0x00ffffff) + (alpha << 24);
				
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
	
	public void animate(ImageLoader il)
	{
		super.animate(il);
		
		curImage = imageTable.get("testImg");
		Point curFocus = focusTable.get("testImg");
		
		vars[0] += 5;
		cameraY--;
	//	cameraZoom = 2.0;
		cameraX = 150;
		
		fx = curFocus.x;
		fy = curFocus.y;	
	}
	
	
	
	
	
}


// Tunnel background effect
class GravityBG2 extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	Sprite cPoint;
	double centerX;
	double centerY;
	
	public GravityBG2(double x, double y, double w, double h, Sprite centerSprite)
	{
		super(x, y, w, h);
		centerX = w/2;
		centerY = h/2;
		cPoint = centerSprite;
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/gravityBG1.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for GravityBG1");
		
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
		
		// obtain the raster that we will write the pixels of our resulting image to
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height,BufferedImage.TYPE_INT_ARGB);
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
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
		
		// compute any values that will be reused for each pixel.
		
		double camDx = cameraX;
		double camDy = cameraY;
		
		double rCos = 1; //GameMath.cos(cameraAngle);
		double rSin = 0; //GameMath.sin(cameraAngle);

		// process each pixel in the sprite's viewport.
		
		for(int j = 0; j < height; j++)
		{
			for(int i = 0; i < width; i++)
			{	
				Point pix = getPixelAt(i,j,camDx,camDy,rCos,rSin);
				
				int pixX = pix.x % pixWidth;
				int pixY = pix.y % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				int pixelColor =  pixels[pixY*pixWidth + pixX];
				
				float brightness = (pixelColor & 0x000000ff) / 255.0f;
				double colorOffset = brightness * 360 + j + vars[0];
				int hsbColor = Color.HSBtoRGB((float)(colorOffset/720.0), 1.0f, 0.4f * brightness + 0.1f);
				
				int alpha = (pixelColor >> 24) & 0xff;
				if(alpha > 0)
					alpha = (int)(alpha*(1-semiTransparency));
				pixelColor = (hsbColor & 0x00ffffff) + (alpha << 24);
				
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
	
	/* // forms ripples all over the image
	protected Point getPixelAt(double i,double j,double camDx, double camDy, double rCos, double rSin)
	{
		double dist = (i-width/2)*(i-width/2) + (j-height/2)*(j-height/2);
		
		i-=width/2;
		j-=height/2 + dist;
		
		
		Point2D rotated = new Point((int)(i*rCos - j*rSin), (int)(i*rSin + j*rCos));
		return new Point((int)((rotated.getX()*cameraZoom + camDx)), (int)((rotated.getY()*cameraZoom + camDy)));
	}
	*/
	
	/* // ripples 2
	protected Point getPixelAt(double i,double j,double camDx, double camDy, double rCos, double rSin)
	{
		double dist = (i-width/2)*(i-width/2) + (j-height/2)*(j-height/2);
		
		i-=width/2 + dist;
		j-=height/2 + dist;
		
		
		Point2D rotated = new Point((int)(i*rCos - j*rSin), (int)(i*rSin + j*rCos));
		return new Point((int)((rotated.getX()*cameraZoom + camDx)), (int)((rotated.getY()*cameraZoom + camDy)));
	}
	*/
	
	protected Point getPixelAt(double i,double j,double camDx, double camDy, double rCos, double rSin)
	{	
		double cx = centerX; //width/2;
		double cy = centerY; //height/2;
		double distSqr = pixHeight*pixHeight*5/((i-cx)*(i-cx) + (j-cy)*(j-cy));
		double angle = GameMath.getAngleTo(cx,cy,i,j) + camDx;
		
		int ii = (int)(angle/360.0*pixWidth*4 + camDx);
		int jj = (int)(distSqr + camDy);
		
		return new Point(ii,jj);
	}
	
	
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
		
		vars[0] += 5;
		cameraY--;
	//	cameraZoom = 2.0;
		cameraX++;
		
		fx = curFocus.x;
		fy = curFocus.y;
		centerX += (cPoint.x/3 - centerX)/5.0;
		centerY += (cPoint.y/3 - centerY)/5.0;
	}
}



// Tunnel background effect
class SpaceWarpBG extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	Sprite cPoint;
	double centerX;
	double centerY;
	boolean isWarped = false;
	double warpSpeed = 0.0;
	
	
	public SpaceWarpBG(double x, double y, double w, double h, Sprite centerSprite)
	{
		super(x, y, w, h);
		centerX = w/2;
		centerY = h/2;
		cPoint = centerSprite;
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/BGSpace1.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for GravityBG1");
		
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
		
		// obtain the raster that we will write the pixels of our resulting image to
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height,BufferedImage.TYPE_INT_ARGB);
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
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
		
		// compute any values that will be reused for each pixel.
		
		double camDx = cameraX;
		double camDy = cameraY;
		
		// process each pixel in the sprite's viewport.
		
		for(int j = 0; j < height; j++)
		{
			for(int i = 0; i < width; i++)
			{	
				Point pix = getPixelAt(i,j,camDx,camDy);

				int pixX = pix.x % pixWidth;
				int pixY = pix.y % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				int pixelColor =  pixels[pixY*pixWidth + pixX];

				int alpha = (pixelColor >> 24) & 0xff;
				if(alpha > 0)
					alpha = (int)(alpha*(1-semiTransparency));
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
	
	
	protected Point getPixelAt(double i,double j,double camDx, double camDy)
	{	
		double cx = centerX; //width/2;
		double cy = centerY; //height/2;
		double distSqr = ((i-cx)*(i-cx) + (j-cy)*(j-cy))/pixHeight; // pixHeight*pixHeight*5/((i-cx)*(i-cx) + (j-cy)*(j-cy));
		double angle = GameMath.getAngleTo(cx,cy,i,j) + camDx;
		
		int ii = (int)(angle/360.0*pixWidth*2 + camDx);
		int jj = (int)(distSqr + camDy);
		
		return new Point(ii,jj);
	}

	
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
		
		vars[0] += 5;
		cameraY -= 1;
		cameraX += 0.3;
		
		if(isWarped) {
			cameraX += warpSpeed;
			cameraY -= warpSpeed;
			warpSpeed += 0.1;
		}
		else warpSpeed = (0-warpSpeed)/60.0;
		
		fx = curFocus.x;
		fy = curFocus.y;
		centerX += (cPoint.vars[8]/3 - centerX)/5.0;
		centerY += (cPoint.vars[9]/3 - centerY)/5.0;
		
		
	}
	
	
	
	
	
}


class Stage4CloudyBG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();

	public Stage4CloudyBG(double x, double y, double w, double h)
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
		
		imageURL = Stage4CloudyBG.class.getResource("graphics/StormBG2.png");
		Image roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("road",new Point(0,0));
		imageTable.put("road",roadImg);
		
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
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height - 1,BufferedImage.TYPE_INT_ARGB);
		
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
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
		
		
		// test code...
		
		double camDx = cameraX - camDist*GameMath.cos(cameraAngle);
		double camDy = cameraY + camDist*GameMath.sin(cameraAngle);
		
		double rCos = GameMath.cos(180-cameraAngle+90);
		double rSin = GameMath.sin(180-cameraAngle+90);
		
		int camColor = camColors[curCamColor];
		curCamColor++;
		
		//AffineTransform rotation = AffineTransform.getRotateInstance(GameMath.d2r(180-cameraAngle+90));
		double aspectRatio = width/height;
		double horizonCenter = width/2.0;
		
		if(curCamColor > 2)
			curCamColor = 0;
		
		for(int j = 1; j < height; j++)
		{
			
			for(int i = 0; i < width; i++)
			{	
				int[] pix = getPixelAt(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
				
				int pixX = pix[0];
				int pixY = pix[1];
				
				boolean isCamPoint = false;
				if(Math.abs(pixX - cameraX) < 10 && Math.abs(pixY - cameraY) < 10)//  || !(pixX >=0 && pixX <pixWidth && pixY >= 0 && pixY < pixHeight))
					isCamPoint = true;

				pixX = pixX % pixWidth;
				pixY = pixY % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				
				int pixelColor =  pixels[pixY*pixWidth + pixX];

				int alpha = (pixelColor >> 24) & 0xff;
				if(alpha > 0)
					alpha = (int)(alpha*(1-semiTransparency));
				pixelColor = (pixelColor & 0x00ffffff) + (alpha << 24);
				
				try
				{
					writePixels[(int)width*(j-1)+i] = pixelColor;
				}
				catch(Exception ex)
				{
					System.out.println("Mode7 error: \n" + ex.getMessage());
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
	
	public void animate(ImageLoader il)
	{
		super.animate(il);
		
		curImage = imageTable.get("road");
		Point curFocus = focusTable.get("road");

		fx = curFocus.x;
		fy = curFocus.y;
	}
}




class Stage4Mode7CloudBG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public Stage4Mode7CloudBG(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
		
		horizonY = 50;
		cameraAngle = 270;
		elevation = 200;
		camDist = 100;
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
		
		imageURL = Stage3Mode7CloudBG.class.getResource("graphics/StormBG2.png");
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
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		
		BufferedImage mode7Img = new BufferedImage((int) width,(int) height - 1,BufferedImage.TYPE_INT_ARGB);
		
		int[] writePixels = ((DataBufferInt)(mode7Img.getRaster().getDataBuffer())).getData(); 
		
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
		
		
		// test code...
		
		double camDx = cameraX - camDist*GameMath.cos(cameraAngle);
		double camDy = cameraY + camDist*GameMath.sin(cameraAngle);
		
		double rCos = GameMath.cos(180-cameraAngle+90);
		double rSin = GameMath.sin(180-cameraAngle+90);
		
		int camColor = camColors[curCamColor];
		curCamColor++;
		
		//AffineTransform rotation = AffineTransform.getRotateInstance(GameMath.d2r(180-cameraAngle+90));
		double aspectRatio = width/height;
		double horizonCenter = width/2.0;
		
		if(curCamColor > 2)
			curCamColor = 0;
		
		for(int j = 1; j < height; j++)
		{
			
			for(int i = 0; i < width; i++)
			{	
				int[] pix = getPixelAt(i,j,camDx,camDy,rCos,rSin,horizonCenter,aspectRatio);
				
				int pixX = pix[0];
				int pixY = pix[1];

				pixX = pixX % pixWidth;
				pixY = pixY % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				
				int pixelColor =  pixels[pixY*pixWidth + pixX] & 0x00FFFFFF;
				pixelColor = pixelColor + ((int) ( Math.min(1.5*j*255.0/height, 255) ) << 24);
				
				try
				{
					writePixels[(int)width*(j-1)+i] = pixelColor;
				}
				catch(Exception ex)
				{
					System.out.println("Mode7 error: \n" + ex.getMessage());
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
	
	public void animate(ImageLoader il)
	{
		super.animate(il);

		curImage = imageTable.get("road");
		Point curFocus = focusTable.get("road");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		cameraY += 2;
		
		
	}
	
	
}


class Stage4MonolithBG extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	private int[] pixels;
	private int pixWidth,pixHeight;
	
	// CONSTRUCTOR
	
	public Stage4MonolithBG(double x, double y)
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
		
		imageURL = Stage3SunsetBG.class.getResource("graphics/BGMonolith1.png");
		Image bullet1Img = tk.getImage(imageURL);
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
					alpha = (int)(alpha*(1-semiTransparency));
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
	}
}






