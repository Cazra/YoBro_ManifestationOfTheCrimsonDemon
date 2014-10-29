
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class FXSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private char type;	// 1: simple orb
	private Color color;
	private Color color2;
	private double initSemiTrans;
	private double dx,dy,ddx,ddy;	// governs velocity and acceleration
	private int lifespan, fadespan;	// governs how long the effect lives, and how long it fades out when it dies
	private double rad, shrinkRate; // governs the radius of the special effect
	
	// CONSTRUCTOR
	
	public FXSprite(double x, double y, char t , Color c, double semiTrans)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		color = c;
		color2 = c;
		dx = 0;
		dy = 0;
		ddx = 0;
		ddy = 0;
		lifespan = 30;
		fadespan = 30;
		rad = 16;
		shrinkRate = 1.0;
		setSemiTransparency(semiTrans);
		initSemiTrans = semiTrans;
	}
	
	/**
	*	Produces a non-moving, fast-fading white orb with a radius of 16.
	**/
	
	public static FXSprite makeSimpleOrb1(double x, double y)
	{
		FXSprite result = new FXSprite(x,y,'1',new Color(0xFFFFFF), 0.5);
	//	Random rand = new Random();
	//	result.setVelocity(rand.nextDouble()*4-2,rand.nextDouble()*4-2,0.95,0.95);
		result.setLifespan(0,20);
		result.setRadius(16,0.95);
		return result;
	}
	
	/**
	*	Produces a randomly-moving, fast-fading white orb with a radius of 8.
	**/
	
	public static FXSprite makeSimpleOrb2(double x, double y)
	{
		FXSprite result = new FXSprite(x,y,'1',new Color(0xFFFFFF), 0.5);
		Random rand = new Random();
		result.setVelocity(rand.nextDouble()*4-2, rand.nextDouble()*4-2, 0.95, 0.95);
		result.setLifespan(0,20);
		result.setRadius(8,0.95);
		return result;
	}
	
	/**
	*	Produces a randomly-moving, fast-fading white orb with a radius of 32.
	**/
	
	public static FXSprite makeSimpleOrb3(double x, double y)
	{
		FXSprite result = new FXSprite(x,y,'1',new Color(0xFFFFFF), 0.7);
		Random rand = new Random();
		result.setVelocity(rand.nextDouble()*6-3, rand.nextDouble()*6-3, 0.95, 0.95);
		result.setLifespan(0,20);
		result.setRadius(32,0.95);
		return result;
	}
	
	/**
	*	Produces a non-moving, fast-fading white orb with a radius of 16.
	**/
	
	public static FXSprite makeBurst1(double x, double y)
	{
		FXSprite result = new FXSprite(x,y,'1',new Color(0xFFFFFF), 0.7);
		result.setColors(new Color(0x000000FF,true), new Color(0xAA0000FF,true));
		result.setLifespan(0,40);
		result.setRadius(16,1.01);
		return result;
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
		
		// stage 1, stage 2, stage 3
		
		/*
		imageURL = StageTextSprite.class.getResource("graphics/StageTitleSheet.png");
		Image hudSheet = tk.getImage(imageURL);
		hudSheet = ColorFilters.setTransparentColor(hudSheet, new Color(0xFF00FF));
		
		img = ImageBlitter.crop(hudSheet,1,1,100,33);
		il.addImage(img);
		focusTable.put("stage1",new Point(0,0));
		imageTable.put("stage1",img);
		*/
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
	
	public void setColors(Color c1, Color c2)
	{
		color = c1;
		color2 = c2;
	}
	
	public void setVelocity(double dx, double dy, double ddx, double ddy)
	{
		this.dx = dx;
		this.dy = dy;
		this.ddx = ddx;
		this.ddy = ddy;
	}
	
	public void setLifespan(int life, int fade)
	{
		lifespan = life;
		fadespan = fade;
	}
	
	public void setRadius(double radius, double shrink)
	{
		rad = radius;
		shrinkRate = shrink;
	}
	
	public void move()
	{
		x+=dx;
		y+=dy;
		
		if(type == '1') // ddx and ddy are used as scalars for the current velocity. This will cause the particle to slow to a stop or slowly speed up.
		{
			dx*=ddx;
			dy*=ddy;
			
			rad *= shrinkRate;
		}
		
		if(lifespan < 0)
		{
			double alpha = (lifespan*-1.0/fadespan);
			setSemiTransparency((1-alpha)*initSemiTrans + alpha); 
		}
		if(lifespan < -1*fadespan)
		{
			this.destroy();
		}
		
		lifespan--;
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
			fx = 0;
			fy = 0;
			
			width = r*2;
			height = r*2;
		}
	/*	if(type == '2')
		{
			String imgKey = "stage1";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 100;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}*/
		
		
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
		String result = imgKey + "st" + keyTag;
		
		if(imageTable.containsKey(result))
		{
			curImage = imageTable.get(result);
		}
		else
		{
			Image semicurImage = ColorFilters.setSemiTransparency(curImage, this.semiTransparency);
			imageTable.put(result, semicurImage);
			
			curImage = semicurImage;
			il.addImage(curImage);
		}
	}
	

	/**
	*	Overridden draw(Graphics2D g) method
	**/
	
	protected void draw(Graphics2D g)
	{
		if(this.type == '1')
		{
			Color semiTransCenterColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(color.getAlpha()*(1-semiTransparency)));
			Color semiTransEdgeColor = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), (int)(color2.getAlpha()*(1-semiTransparency)));
			
			Color[] colors = {semiTransCenterColor, semiTransEdgeColor};
			float[] dists = {0.0f, 1.0f};
			
			RadialGradientPaint  rgp = new RadialGradientPaint(0, 0, (float) rad, dists, colors);
			
			g.setPaint(rgp);
			g.fillOval((int)(0-rad) ,(int)(0-rad), (int)(rad*2), (int)(rad*2));
		}
		else
			super.draw(g);
	}
	
	
	
}
