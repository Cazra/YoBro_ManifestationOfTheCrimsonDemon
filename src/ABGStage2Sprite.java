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


public class ABGStage2Sprite extends AnimatedBackgroundSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();

	private Stage2Mode7BG mode7BG;
	private LinearGradientPaint fog;
	//private Stage2Mode7BG2 mode7BG2;
	private IceWrapBG icyBG;
	private StormWrapBG stormyBG;
	private MathWrapBG mathBG;
	
	// CONSTRUCTOR
	
	public ABGStage2Sprite(MainLevel ml)
	{
		super(ml);
		numInstances++;
		
		mode7BG = new Stage2Mode7BG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG.cameraX = 400;
		mode7BG.cameraY = 100;
		mode7BG.horizonY = 100;
		mode7BG.camDist = 200;
		mode7BG.elevation = 500;
		mode7BG.cameraAngle = 270;
	//	mode7BG.blurTechnique = 2;
	/*
		mode7BG2 = new Stage2Mode7BG2(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mode7BG2.cameraX = 400;
		mode7BG2.cameraY = 100;
		mode7BG2.horizonY = 100;
		mode7BG2.camDist = 200;
		mode7BG2.elevation = 500;
		mode7BG2.cameraAngle = 270;*/
		
		stormyBG = new StormWrapBG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		icyBG = new IceWrapBG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mathBG = new MathWrapBG(0,0,ml.SCREENW/2,ml.SCREENH/2);
		mathBG.cameraX = 150;
		mathBG.cameraY = 150;
		
		Point2D start = new Point2D.Float(0, 0);
		Point2D end = new Point2D.Float(0, ml.SCREENH);
		float[] dist = {0.0f, 0.3f,1.0f};
		Color[] colors = {new Color(0xcc0099bb,true), new Color(0x00ffff00,true),new Color(0x66ffff00,true)};
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
		
		Stage2Mode7BG.clean();
	//	Stage2Mode7BG2.clean();
		IceWrapBG.clean();
		StormWrapBG.clean();
		MathWrapBG.clean();
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
		
		
		Stage2Mode7BG.loadImages(il);
	//	Stage2Mode7BG2.loadImages(il);
		IceWrapBG.loadImages(il);
		StormWrapBG.loadImages(il);
		MathWrapBG.loadImages(il);
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
				stormyBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 1)
			{
				stormyBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				stormyBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			stormyBG.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 2 || nextBGID == 2)
		{
			if(curBGID != 2)
			{
				icyBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 2)
			{
				icyBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				icyBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			icyBG.render(g);
			g.setTransform(curTrans);
			
		}
		if(curBGID == 3 || nextBGID == 3)
		{
			if(curBGID != 3)
			{
				mathBG.setSemiTransparency(1-this.transitionPercent);
			}
			else if(nextBGID != 3)
			{
				mathBG.setSemiTransparency(this.transitionPercent);
			}
			else
			{
				mathBG.setSemiTransparency(0.0);
			}
			AffineTransform curTrans = g.getTransform();
			g.scale(2.0,2.0);
			mathBG.render(g);
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
		//	mode7BG2.animate(il);
		}
		if(curBGID == 1 || nextBGID == 1)
		{
			stormyBG.animate(il);
			stormyBG.cameraY-= 2;
			stormyBG.cameraX+= 2;
		}
		if(curBGID == 2 || nextBGID == 2)
		{
			icyBG.animate(il);
			icyBG.cameraY+= 1;
		}
		if(curBGID == 3 || nextBGID == 3)
		{
			mathBG.animate(il);
			mathBG.cameraAngle-= 0.5;
		}
		
		
	}
}







class Stage2Mode7BG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	protected int[] pixels2;
	protected int imgOffset;
	
	public Stage2Mode7BG(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
		imgOffset = 0;
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
		
		imageURL = Stage1Mode7BG.class.getResource("graphics/Stage2Water1.png");
		Image roadImg = tk.getImage(imageURL);
		il.addImage(roadImg);
		focusTable.put("road",new Point(0,0));
		imageTable.put("road",roadImg);
		
		imageURL = Stage1Mode7BG.class.getResource("graphics/Stage2Water2.png");
		roadImg = tk.getImage(imageURL);
		
		Image bulletImg = ImageBlitter.crop(roadImg,0,0,300,300);
		Image alphaImg = ImageBlitter.crop(roadImg,300,0,300,300);
		roadImg = ColorFilters.applyAlphaMap(bulletImg, alphaImg, 300, 300);
		
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
				int pixX2 = pix[0] + imgOffset;
				int pixY = pix[1];
				
				pixX = (int)Math.abs(pixX) % pixWidth;
				pixX2 = (int)Math.abs(pixX2) % pixWidth;
				pixY = (int)Math.abs(pixY) % pixHeight;
				
				int pixelColor =  pixels[pixY*pixWidth + pixX];
				int pixelColor2 = pixels2[pixY*pixWidth + pixX2];
				
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
		cameraY+= 2;
		cameraX-= 0.5;
		
		
	}
	
	
}

class Stage2Mode7BG2 extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public Stage2Mode7BG2(double x, double y, double w, double h)
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
		
		imageURL = Stage1Mode7BG.class.getResource("graphics/Stage2Water2.png");
		Image roadImg = tk.getImage(imageURL);
		
		Image bulletImg = ImageBlitter.crop(roadImg,0,0,300,300);
		Image alphaImg = ImageBlitter.crop(roadImg,300,0,300,300);
		roadImg = ColorFilters.applyAlphaMap(bulletImg, alphaImg, 300, 300);
		
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
		
		cameraY+= 2;
		cameraX-= 0.5;
		
		
	}
}



class StormWrapBG extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public StormWrapBG(double x, double y, double w, double h)
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/StormBG1.png");
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




class IceWrapBG extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public IceWrapBG(double x, double y, double w, double h)
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/IcyBG1.png");
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

class MathWrapBG extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public MathWrapBG(double x, double y, double w, double h)
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
		
		imageURL = BerryWrapBG.class.getResource("graphics/MathBG1.png");
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



