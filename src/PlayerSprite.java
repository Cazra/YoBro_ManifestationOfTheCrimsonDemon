
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class PlayerSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private char type;
	private int frameNum;
	private char direction; 
	
	public boolean isBombing;
	public boolean isDead;
	public int respawnCounter;
	public int tempInvincibility;
	public final int BORDER_OF_LIFE_AND_DEATH = 10;
	public final double HIT_RADIUS = 2.0;
	public final double GRAZE_RADIUS = 30;
	public final double MAGNET_RADIUS = 32;
	
	
	// CONSTRUCTOR
	
	public PlayerSprite(double x, double y, char t)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		direction = 'N';
		
		if(type == 'r')
			this.setRadius(GRAZE_RADIUS);
		if(type == 'h')
		{
			this.setRadius(HIT_RADIUS);
		//	this.r = HIT_RADIUS;
		}
		
		isDead = false;
		respawnCounter = 0;
		tempInvincibility = 0;
		
		isBombing = false;
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
		
		// load our images
		
		imageURL = PlayerSprite.class.getResource("graphics/ReySheet.png");
		Image reySheet = tk.getImage(imageURL);
		reySheet = ColorFilters.setTransparentColor(reySheet, new Color(0xFF00FF));
		
		imageURL = PlayerSprite.class.getResource("graphics/PlayerBulletsSheet.png");
		Image hitBox = tk.getImage(imageURL);
		hitBox = ColorFilters.setTransparentColor(hitBox, new Color(0xFF00FF));
		img = ImageBlitter.crop(hitBox,1,20,5,5);
		il.addImage(img);
		focusTable.put("hitBox",new Point(2,2));
		imageTable.put("hitBox",img);
		
		// Right
		
		img = ImageBlitter.crop(reySheet,1,1,32,32);
		il.addImage(img);
		focusTable.put("ReyE1",new Point(16,21));
		imageTable.put("ReyE1",img);
		
		img = ImageBlitter.crop(reySheet,35,1,32,32);
		il.addImage(img);
		focusTable.put("ReyE2",new Point(16,21));
		imageTable.put("ReyE2",img);
		
		img = ImageBlitter.crop(reySheet,69,1,32,32);
		il.addImage(img);
		focusTable.put("ReyE3",new Point(16,21));
		imageTable.put("ReyE3",img);
		
		img = ImageBlitter.crop(reySheet,103,1,32,32);
		il.addImage(img);
		focusTable.put("ReyE4",new Point(16,21));
		imageTable.put("ReyE4",img);
		
		img = ImageBlitter.crop(reySheet,1,35,32,32);
		il.addImage(img);
		focusTable.put("ReyN1",new Point(16,21));
		imageTable.put("ReyN1",img);
		
		img = ImageBlitter.crop(reySheet,35,35,32,32);
		il.addImage(img);
		focusTable.put("ReyN2",new Point(16,21));
		imageTable.put("ReyN2",img);
		
		img = ImageBlitter.crop(reySheet,69,35,32,32);
		il.addImage(img);
		focusTable.put("ReyN3",new Point(16,21));
		imageTable.put("ReyN3",img);
		
		img = ImageBlitter.crop(reySheet,103,35,32,32);
		il.addImage(img);
		focusTable.put("ReyN4",new Point(16,21));
		imageTable.put("ReyN4",img);
		
		img = ImageBlitter.crop(reySheet,1,69,32,32);
		il.addImage(img);
		focusTable.put("ReyW1",new Point(16,21));
		imageTable.put("ReyW1",img);
		
		img = ImageBlitter.crop(reySheet,35,69,32,32);
		il.addImage(img);
		focusTable.put("ReyW2",new Point(16,21));
		imageTable.put("ReyW2",img);
		
		img = ImageBlitter.crop(reySheet,69,69,32,32);
		il.addImage(img);
		focusTable.put("ReyW3",new Point(16,21));
		imageTable.put("ReyW3",img);
		
		img = ImageBlitter.crop(reySheet,103,69,32,32);
		il.addImage(img);
		focusTable.put("ReyW4",new Point(16,21));
		imageTable.put("ReyW4",img);
		
		
		System.out.println("loaded image data for PlayerSprite");
		
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
	
	public void setDirection(char dir)
	{
		if(dir != 'N' && dir != 'E' && dir != 'W')
			return;
		else
			this.direction = dir;
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
		
		if(type == 'r') // Rey
		{
			if(!isDead)
			{
				String imgKey = "Rey" + this.direction + ((this.frameNum/7) + 1);
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 32;
				height = 32;

				this.frameNum++;
				if(this.frameNum >= 4*7)
					this.frameNum = 0;
			}
			else
			{
				String imgKey = "Rey" + this.direction + ((this.frameNum/7) + 1);
				curImage = imageTable.get(imgKey);
				
				if(respawnCounter < 20)
				{
					this.scale(this.scaleX -= 0.04, this.scaleY += 0.04);
					this.setSemiTransparency(this.semiTransparency+0.05);
				}
				if(respawnCounter == 20)
				{
					this.scale(2.0, 0.1);
					this.setSemiTransparency(0.9);
					this.isVisible = false;
				}
				if(respawnCounter > 30)
				{
					this.isVisible = true;
					this.scale(this.scaleX -= 0.09, this.scaleY +=0.09);
				}
				if(respawnCounter > 40)
				{
					this.respawnCounter = -1;
					this.isDead = false;
					this.scale(1.0,1.0);
					this.setSemiTransparency(1.0);
					this.tempInvincibility = 120;
				}
				this.respawnCounter++;
			}
		}
		if(type == 'h') // hitbox
		{
			String imgKey = "hitBox";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 5;
			height = 5;
		//	this.rotate(this.rotation + 1);
		}
	}
	
	
	protected void draw(Graphics2D g)
	{
		g.drawImage(this.curImage, null, null);
	}
	
	
}
