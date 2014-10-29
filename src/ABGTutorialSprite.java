import java.awt.GraphicsConfiguration;

import java.awt.LinearGradientPaint;
import java.awt.geom.Point2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
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


public class ABGTutorialSprite extends AnimatedBackgroundSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();

	private TutorialMode7BG mode7BG;
	private LinearGradientPaint fog;
	
	// CONSTRUCTOR
	
	public ABGTutorialSprite(MainLevel ml)
	{
		super(ml);
		numInstances++;
		
		mode7BG = new TutorialMode7BG(0,ml.SCREENH/3,ml.SCREENW/2,(ml.SCREENH*2/3));
		mode7BG.cameraX = 500;
		mode7BG.cameraY = 100;
		mode7BG.horizonY = 20;
		mode7BG.camDist = 200;
		mode7BG.elevation = 200;
		mode7BG.cameraAngle = 270;
		mode7BG.blurTechnique = 0;
		mode7BG.scale(2.0,2.0);
		
		Point2D start = new Point2D.Float(0, ml.SCREENH/3);
		Point2D end = new Point2D.Float(0, ml.SCREENH);
		float[] dist = {0.0f, 1.0f};
		Color[] colors = {new Color(0xFFAAAAFF,true), new Color(0x00AAAAFF,true)};
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
		
		
		TutorialMode7BG.loadImages(il);
	}
	

	// LOGIC METHODS

	// RENDERING METHODS
	
	public boolean render(Graphics2D g)
	{	
		super.render(g);
		mode7BG.render(g);
		
		g.setPaint(fog);
		g.fillRect(0,mainLevel.SCREENH/3,mainLevel.SCREENW,mainLevel.SCREENH*2/3);
		
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
	}
}







class TutorialMode7BG extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public TutorialMode7BG(double x, double y, double w, double h)
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
		
		imageURL = TutorialMode7BG.class.getResource("graphics/BGTutorialTiles.png");
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
		
		cameraY+= 2;
	}
}










