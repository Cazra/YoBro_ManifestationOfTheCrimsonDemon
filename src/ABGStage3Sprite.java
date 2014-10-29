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


public class ABGStage3Sprite extends AnimatedBackgroundSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();

	public Stage3Mode7BG mode7BG;
	private LinearGradientPaint fog;
	public Stage3Mode7BG2 mode7BG2;
	public Stage3SunsetBG sunsetBG;
	public Stage3Mode7CloudBG mode7Clouds;
	private WrapSpritePurpleBG1 purpleWrapBG;
	
	// CONSTRUCTOR
	
	public ABGStage3Sprite(MainLevel ml)
	{
		super(ml);
		numInstances++;
		
		mode7BG = new Stage3Mode7BG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG.cameraX = 128;
		mode7BG.cameraY = 100;
		mode7BG.horizonY = 50;
		mode7BG.camDist = 200;
		mode7BG.elevation = 250;
		mode7BG.cameraAngle = 270;
		mode7BG.scale(2.0,2.0);
		
		Point2D start = new Point2D.Float(0, 0);
		Point2D end = new Point2D.Float(0, ml.SCREENH);
		float[] dist = {0.0f, 1.0f};
		Color[] colors = {new Color(0xFF770022,true), new Color(0x00770022,true)};
		//Color[] colors = {new Color(0xFFffBB88,true), new Color(0x00ffBB88,true)};
		fog = new LinearGradientPaint(start, end, dist, colors);
	
		vars[0] = 0.5;
		
		mode7BG2 = new Stage3Mode7BG2(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG2.cameraX = 128;
		mode7BG2.cameraY = 100;
		mode7BG2.horizonY = 60;
		mode7BG2.camDist = 200;
		mode7BG2.elevation = 250;
		mode7BG2.cameraAngle = 270;
		mode7BG2.scale(2.0,2.0);
		
		sunsetBG = new Stage3SunsetBG(0,0);
		mode7Clouds = new Stage3Mode7CloudBG(0,ml.SCREENH/4,ml.SCREENW/2,ml.SCREENH/8);
		mode7Clouds.scale(2.0,2.0);
		
		purpleWrapBG = new WrapSpritePurpleBG1(0,0,ml.SCREENW/2,ml.SCREENH/2);
		purpleWrapBG.cameraY = 470/2;
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
		
		Stage3Mode7BG.clean();
		Stage3Mode7BG2.clean();
		Stage3SunsetBG.clean();
		Stage3Mode7CloudBG.clean();
		WrapSpritePurpleBG1.clean();
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
		
		Stage3Mode7BG.loadImages(il);
		Stage3Mode7BG2.loadImages(il);
		Stage3SunsetBG.loadImages(il);
		Stage3Mode7CloudBG.loadImages(il);
		WrapSpritePurpleBG1.loadImages(il);
		
		
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
			g.scale(2.0,2.0);
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
				purpleWrapBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 3)
			{
				purpleWrapBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				purpleWrapBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			purpleWrapBG.render(g);
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
			mode7BG.cameraY += vars[0];
		}
		if(curBGID == 1 || nextBGID == 1)
		{
			mode7BG2.animate(il);
			mode7BG2.cameraY-= 2;
		}
		if(curBGID == 2 || nextBGID == 2)
		{
			sunsetBG.animate(il);
			mode7Clouds.animate(il);
		}
		if(curBGID == 3 || nextBGID == 3)
		{
			purpleWrapBG.animate(il);
			purpleWrapBG.cameraX--;
		}
		
		
	}
}







class Stage3Mode7BG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	protected int[] pixels2;
	protected int imgOffset;
	
	int pixWidth2, pixHeight2;
	
	public Stage3Mode7BG(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
		imgOffset = 0;
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
		
		imageURL = Stage3Mode7BG.class.getResource("graphics/BGStage3Tile.png");
		Image roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("road",new Point(0,0));
		imageTable.put("road",roadImg);
		
		
		imageURL = Stage3Mode7BG.class.getResource("graphics/BGStage3Tile2.png");
		roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("road2",new Point(0,0));
		imageTable.put("road2",roadImg);
		
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
			
			
			pg = new PixelGrabber(imageTable.get("road2"), 0,0, -1, -1, false);
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
				int pixX2 = pix[0];
				int pixY = pix[1];
				
				pixX = pixX % pixWidth;
				pixY = pixY % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				
				int pixelColor =  pixels2[pixY*pixWidth2 + pixX];
				
				try
				{
						writePixels[(int)width*(j-1)+i] = pixelColor;
						if(pixX2 >= 0 && pixX2 < pixWidth)
						{
							int pixelColor2 = pixels[pixY*pixWidth + pixX2];
							writePixels[(int)width*(j-1)+i] = pixelColor2;
						}
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
		
		
		imgOffset++;
		
		
	}
	
}


class Stage3Mode7BG2 extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public Stage3Mode7BG2(double x, double y, double w, double h)
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
		
		imageURL = Stage3Mode7BG2.class.getResource("graphics/HorrorBG1.png");
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
				
				boolean isCamPoint = false;
				if(Math.abs(pixX - cameraX) < 10 && Math.abs(pixY - cameraY) < 10)//  || !(pixX >=0 && pixX <pixWidth && pixY >= 0 && pixY < pixHeight))
					isCamPoint = true;

				pixX = pixX % pixWidth;
				pixY = pixY % pixHeight;
				if(pixX < 0)
					pixX = pixWidth+pixX;
				if(pixY < 0)
					pixY = pixHeight+pixY;
				
				int pixelColor =  (pixels[pixY*pixWidth + pixX] & 0x00FFFFFF) | (((int)(255-this.semiTransparency*255) & 0xFF) << 24);
				
				try
				{
					if(!showCamPoint || !isCamPoint)
						writePixels[(int)width*(j-1)+i] = pixelColor;
					else
						writePixels[(int)width*(j-1)+i] = camColor;
						
					
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
	
	
	
	protected int[] getPixelAt(double i,double j,double camDx, double camDy, double rCos, double rSin, double horizonCenter, double aspectRatio)
	{

		double z = (elevation*elevation)/(j+horizonY);
		double rasterX = (horizonCenter - i)*z;
		
		int pixX = (int) (rasterX);// % pixWidth;
		int pixY = (int) (elevation*z*aspectRatio);// % pixHeight;
		
		Point2D rotated = new Point((int)(pixX*rCos - pixY*rSin), (int)(pixX*rSin - pixY*rCos)); //rotation.transform(new Point(pixX,pixY), null);
		
		pixX = (int) (rotated.getX() + camDx);
		pixY = (int) (rotated.getY() + camDy);
		int[] result = {pixX,pixY};
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
		
		
		curImage = imageTable.get("road");
		Point curFocus = focusTable.get("road");

		fx = curFocus.x;
		fy = curFocus.y;
		
		
	}
	
	
	
	
}


class Stage3Mode7CloudBG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public Stage3Mode7CloudBG(double x, double y, double w, double h)
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
		
		imageURL = Stage3Mode7CloudBG.class.getResource("graphics/SunsetCloudsBG.png");
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
				pixelColor = pixelColor + ((int) ( j*255.0/height ) << 24);
				
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


class Stage3SunsetBG extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	private int[] pixels;
	private int pixWidth,pixHeight;
	
	// CONSTRUCTOR
	
	public Stage3SunsetBG(double x, double y)
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
		
		imageURL = Stage3SunsetBG.class.getResource("graphics/SunsetBG.png");
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



