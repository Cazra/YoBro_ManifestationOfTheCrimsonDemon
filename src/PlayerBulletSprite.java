
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class PlayerBulletSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private String curImgKey;
	protected int blurLength;
	protected int blurSkip;
	protected AffineTransform[] blurTransforms;
	protected Image[] blurFrames;
	protected String[] blurFrameKeys;
	
	public char type;
	private int frameNum;
	public int damage;
	public double targetX, targetY;
	public Sprite targetSprite;
	public int id;
	public PlayerBulletSprite spawner;
	
	public boolean permanent = false;
	public boolean isBomb = false;
	
	
	// CONSTRUCTOR
	
	public PlayerBulletSprite(double x, double y, char t, int dmg)
	{
		super(x,y);
		numInstances++;
		
		this.type = t;
		this.damage = dmg;
		this.targetX = 0;
		this.targetY = 0;
		this.id = 0;
		this.spawner = null;
		
		Random rand = new Random();
		this.frameNum = rand.nextInt(50);
		
		this.setRadius();
	}
	
	private void setRadius()
	{
		if(this.type == 'a')
			this.setRadius(9);
		if(this.type == 'b')
			this.setRadius(17);
		if(this.type == 'c')
			this.setRadius(9);
		if(this.type == 'd')
			this.setRadius(13);
		if(this.type == 'i')
			this.setRadius(9);
		if(this.type == 'j')
			this.setRadius(15);
		if(this.type == 'k')
			this.setRadius(48);
		if(this.type == 'l')
			this.setRadius(64);
		if(this.type == 'm')
			this.setRadius(48);
		if(this.type == 'n')
			this.setRadius(16);
	}
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		super.destroy();
		targetSprite = null;
		spawner = null;
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
		Image bulletImg;
		
		// load our images
		
		imageURL = PlayerBulletSprite.class.getResource("graphics/PlayerBulletsSheet.png");
		Image bulletSheet = tk.getImage(imageURL);
		bulletSheet = ColorFilters.setTransparentColor(bulletSheet, new Color(0xFF00FF));
		
		// Homing Yoyo
		
		bulletImg = ImageBlitter.crop(bulletSheet,1,1,17,17);
		il.addImage(bulletImg);
		focusTable.put("yoyo1",new Point(8,8));
		imageTable.put("yoyo1",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,20,1,33,33);
		il.addImage(bulletImg);
		focusTable.put("yoyo2",new Point(16,16));
		imageTable.put("yoyo2",bulletImg);
		
		// Illusion Beatdown small
		
		bulletImg = ImageBlitter.crop(bulletSheet,55,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr1",new Point(8,8));
		imageTable.put("ddr1",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,74,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr2",new Point(8,8));
		imageTable.put("ddr2",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,93,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr3",new Point(8,8));
		imageTable.put("ddr3",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,112,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr4",new Point(8,8));
		imageTable.put("ddr4",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,131,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr5",new Point(8,8));
		imageTable.put("ddr5",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,55,20,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr6",new Point(8,8));
		imageTable.put("ddr6",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,74,20,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr7",new Point(8,8));
		imageTable.put("ddr7",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,93,20,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr8",new Point(8,8));
		imageTable.put("ddr8",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,112,20,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr9",new Point(8,8));
		imageTable.put("ddr9",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,131,20,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("ddr10",new Point(8,8));
		imageTable.put("ddr10",bulletImg);
		
		// illusion beatdown large
		
		bulletImg = ImageBlitter.crop(bulletSheet,1,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR1",new Point(12,12));
		imageTable.put("DDR1",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,28,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR2",new Point(12,12));
		imageTable.put("DDR2",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,55,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR3",new Point(12,12));
		imageTable.put("DDR3",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,82,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR4",new Point(12,12));
		imageTable.put("DDR4",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,109,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR5",new Point(12,12));
		imageTable.put("DDR5",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,1,66,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR6",new Point(12,12));
		imageTable.put("DDR6",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,28,66,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR7",new Point(12,12));
		imageTable.put("DDR7",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,55,66,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR8",new Point(12,12));
		imageTable.put("DDR8",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,82,66,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR9",new Point(12,12));
		imageTable.put("DDR9",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,109,66,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("DDR10",new Point(12,12));
		imageTable.put("DDR10",bulletImg);
		
		// Illusion Beatdown spawners
		
		bulletImg = ImageBlitter.crop(bulletSheet,150,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .3);
		il.addImage(bulletImg);
		focusTable.put("ddrSpawner",new Point(8,8));
		imageTable.put("ddrSpawner",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,136,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .3);
		il.addImage(bulletImg);
		focusTable.put("DDRSpawner",new Point(12,12));
		imageTable.put("DDRSpawner",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,169,1,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .3);
		il.addImage(bulletImg);
		focusTable.put("ddrMultiSpawner",new Point(8,8));
		imageTable.put("ddrMultiSpawner",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,163,39,25,25);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .3);
		il.addImage(bulletImg);
		focusTable.put("DDRMultiSpawner",new Point(12,12));
		imageTable.put("DDRMultiSpawner",bulletImg);
		
		// Persuasion Fro-pick
		
		bulletImg = ImageBlitter.crop(bulletSheet,150,20,17,17);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .4);
		il.addImage(bulletImg);
		focusTable.put("fro1",new Point(8,8));
		imageTable.put("fro1",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,136,66,29,29);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .5);
		il.addImage(bulletImg);
		focusTable.put("fro2",new Point(14,14));
		imageTable.put("fro2",bulletImg);
		
		// Homing Yoyo bomb
		
		bulletImg = ImageBlitter.crop(bulletSheet,99,97,128,128);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .3);
		il.addImage(bulletImg);
		focusTable.put("yoyoBomb1",new Point(64,64));
		imageTable.put("yoyoBomb1",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,229,97,96,96);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .6);
		il.addImage(bulletImg);
		focusTable.put("yoyoBomb2",new Point(48,48));
		imageTable.put("yoyoBomb2",bulletImg);
		
		// Persuasion Fro-pick bomb
		
		bulletImg = ImageBlitter.crop(bulletSheet,1,93,96,96);
		bulletImg = ColorFilters.setSemiTransparency(bulletImg, .3);
		il.addImage(bulletImg);
		focusTable.put("froBomb",new Point(48,48));
		imageTable.put("froBomb",bulletImg);
		
		System.out.println("loaded image data for PlayerBulletSprite");
		
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
	
	public void setBlur(int length, int skip)
	{
		blurLength = length;
		blurSkip = skip;
		blurFrameKeys = new String[length];
		blurFrames = new Image[length];
		blurTransforms = new AffineTransform[length];
	}
	
	
	// RENDERING METHODS
	
	
	/**
	*	draw(Graphics2D g)
	*	Helper method draws the image of this Sprite after transformations are readied. Called by render(Graphics2D g).
	*	If you need to do something fancy while rendering this sprite's image, override this method.
	*	Preconditions: g is the Graphics2D object passed in by render(Graphcis2D g).
	*	Postconditions: this sprite is drawn, applying the transformations readied in render(Graphics2D g).
	**/
	
	protected void draw(Graphics2D g)
	{
		AffineTransform origTrans = g.getTransform();
		
		// draw any blurred images
		
		for(int i = blurLength-1; i >= 0 ; i--)
		{
			if(blurFrameKeys[i] != null && (i+1) % blurSkip == 0)
			{	
				g.setTransform(blurTransforms[i]);
				g.drawImage(blurFrames[i], null, null);
			}
		}
		g.setTransform(origTrans);
		
		// draw current frame's image
		if(type == 'n')
		{
			Point2D center = new Point2D.Float(16, 16);
			float radius = 16;
			float[] dist = {0.0f, 0.9f};
			Color[] colors = { new Color(1.0f, 1.0f, 1.0f, (float) (1-this.semiTransparency)), new Color(0.0f, 0.0f, 1.0f, (float) (0.7 * (1-this.semiTransparency)))};
			RadialGradientPaint p = new RadialGradientPaint(center, radius, dist, colors);
			g.setPaint(p);
			g.fillOval(0,0,32,32);
		}
		else
			g.drawImage(this.curImage, null, null);
		
		// process blurs for next frame.
		for(int i = blurLength-1; i >= 0; i--)
		{
			if(i == 0)
			{
				blurFrameKeys[i] = this.curImgKey;
				blurTransforms[i] = origTrans;
			}
			else
			{
				blurFrameKeys[i] = blurFrameKeys[i-1];
				blurTransforms[i] = blurTransforms[i-1];
			}
		}
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
		if(type == 'a') // small yoyo
		{
			curImage = imageTable.get("yoyo1");
			curImgKey = "yoyo1";
			Point curFocus = focusTable.get("yoyo1");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
		}
		if(type == 'b') // large yoyo
		{
			curImage = imageTable.get("yoyo2");
			curImgKey = "yoyo2";
			Point curFocus = focusTable.get("yoyo2");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 33;
			height = 33;
		}
		if(type == 'c') // small ddr
		{
			String imgKey = "ddr" + ((this.frameNum/5)+1);
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
			
			this.frameNum++;
			if(this.frameNum >= 10*5)
				this.frameNum = 0;
		}
		if(type == 'd') // large ddr
		{
			String imgKey = "DDR" + ((this.frameNum/5)+1);
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 25;
			height = 25;
			
			this.frameNum++;
			if(this.frameNum >= 10*5)
				this.frameNum = 0;
		}
		if(type == 'e') // small DDR spawner
		{
			curImage = imageTable.get("ddrSpawner");
			
			Point curFocus = focusTable.get("ddrSpawner");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
		}
		if(type == 'f') // large DDR spawner
		{
			curImage = imageTable.get("DDRSpawner");
			
			Point curFocus = focusTable.get("DDRSpawner");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 25;
			height = 25;

		}
		if(type == 'g') // small MultiDDR spawner
		{
			curImage = imageTable.get("ddrMultiSpawner");
			
			Point curFocus = focusTable.get("ddrMultiSpawner");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;
		}
		if(type == 'h') // large MuliDDR spawner
		{
			curImage = imageTable.get("DDRMultiSpawner");
			
			Point curFocus = focusTable.get("DDRMultiSpawner");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 25;
			height = 25;
		}
		if(type == 'i') // small fropick
		{
			curImage = imageTable.get("fro1");
			
			Point curFocus = focusTable.get("fro1");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 17;
			height = 17;

		}
		if(type == 'j') // large fropick
		{
			curImage = imageTable.get("fro2");
			
			Point curFocus = focusTable.get("fro2");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 29;
			height = 29;
		}
		if(type == 'k') // fropick bomb
		{
			curImage = imageTable.get("froBomb");
			
			Point curFocus = focusTable.get("froBomb");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 96;
			height = 96;
		}
		if(type == 'l') // yoyo bomb
		{
			curImage = imageTable.get("yoyoBomb1");
			
			Point curFocus = focusTable.get("yoyoBomb1");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 128;
			height = 128;
		}
		if(type == 'm') // yoyo bomb burst
		{
			curImage = imageTable.get("yoyoBomb2");
			
			Point curFocus = focusTable.get("yoyoBomb2");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 96;
			height = 96;

		}
		if(type == 'n')	// beatdown laser (scaled gradient oval)
		{
			width = 32;
			height = 32;
			fx = 16;
			fy = 16;
		}
		
		// animateBlurs(il);
		
		//il.addImage(this.curImage);
	}
	
	
	/*
	private void animateBlurs(ImageLoader il)
	{
		Image origCurImage = curImage;
		double origSemiTrans = this.semiTransparency;
		
		for(int i = 0; i < blurLength; i++)
		{
			if(blurFrameKeys[i] != null)
			{
				this.semiTransparency = i*1.0/blurLength;
				animateSemiTransparencyFrame(il, blurFrameKeys[i]);
				blurFrames[i] = curImage;
			}
		}
		
		curImage = origCurImage;
		this.semiTransparency = origSemiTrans;
	}
	
	
	*/
	
	
	
}
