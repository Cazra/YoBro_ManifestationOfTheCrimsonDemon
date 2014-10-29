
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class KyuArmSprite extends EnemySprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public char type;
	private String curImgKey; 
	
	public BossSwirlyKyu kyuBotBody;
	public KyuArmSprite targetClaw;
	
	public double socketX, socketY, targetX, targetY;
	public double harmonicTimer;
	
	// CONSTRUCTOR
	
	public KyuArmSprite(double x, double y, char t, BossSwirlyKyu kyu)
	{
		super(x,y);
		numInstances++;
		kyuBotBody = kyu;
		
		hp = 0;
		aiType = 0;
		harmonicTimer = 0;
		
		dependents = new ArrayList<Sprite>();
		itemDrops = new ArrayList<ItemSprite>();
		
		socketX = targetX = x;
		socketY = targetY = y;
		
		type = t;

		initHitRadius();		
	}
	
	
	private void initHitRadius()
	{
		if(this.type == 'l')
			this.setRadius(10);
		if(this.type == 'r')
			this.setRadius(10);
		if(this.type == 'a')
			this.setRadius(2);
		
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
	}
	
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
		
		imageURL = KyuArmSprite.class.getResource("graphics/SwirlyAndKyubotSheet.png");
		Image slushSheet = tk.getImage(imageURL);
		slushSheet = ColorFilters.setTransparentColor(slushSheet, new Color(0xFF00FF));
		
		// Right claw
		
		img = ImageBlitter.crop(slushSheet,148,1,23,23);
		il.addImage(img);
		focusTable.put("clawRight",new Point(11,11));
		imageTable.put("clawRight",img);
		
		// Left claw
		
		img = ImageBlitter.crop(slushSheet,148,26,23,23);
		il.addImage(img);
		focusTable.put("clawLeft",new Point(11,11));
		imageTable.put("clawLeft",img);
		
		// Arm segment
		
		img = ImageBlitter.crop(slushSheet,148,51,7,7);
		il.addImage(img);
		focusTable.put("arm",new Point(3,3));
		imageTable.put("arm",img);
		
		System.out.println("loaded image data for KyuArmSprite");

	}
	
	
	
	
	
	// LOGIC METHODS
	
	
	/**
	*	boolean damage(int dmg)
	*	Causes this enemy to take damage.
	*	Preconditions: dmg is how much damage is dealt.
	*	Postconditions: This enemy's hp drops by dmg. 
	*		If its HP after taking the damage is <= 0, returns true. Else returns false.
	**/
	
	public boolean damage(int dmg)
	{
		return kyuBotBody.damage(dmg);
	}
	
	
	
	public void move()
	{
		scale(2,2);
		
		if(type == 'a')
		{
			targetX = targetClaw.x;
			targetY = targetClaw.y;
			
			if(targetClaw.type == 'l')
				socketX = kyuBotBody.x - 25;
			else
				socketX = kyuBotBody.x + 25;
				
			socketY = kyuBotBody.y;
			
			Point vector = new Point((int) (targetX - socketX)/2, (int) (targetY - socketY)/2);
			//Point normal = new Point(0-vector.y, vector.x);
			double r = Math.sqrt(vector.x*vector.x + vector.y*vector.y);
			double sin = GameMath.sin(this.harmonicTimer);
			double cos = GameMath.cos(this.harmonicTimer);
			double sinParent = GameMath.sin(targetClaw.harmonicTimer);
			
			this.x = socketX + vector.x + vector.x*cos; 
			this.y = socketY + vector.y + vector.y*cos - (1-sinParent)*(r*sin); 
		}
		else
		{
			if(type == 'l')
			{
				this.socketX = kyuBotBody.x - 45;
			}
			else if(type == 'r')
			{
				this.socketX = kyuBotBody.x + 45;
			}
			this.socketY = kyuBotBody.y + 10;
			
			if(this.aiType == 0) // idle mode: claws stay close to body, pointed in player's direction.
			{
				this.lookingAngle = GameMath.getAngleTo(this.x, this.y, kyuBotBody.mainLevel.player.x, kyuBotBody.mainLevel.player.y);
				this.targetX = this.socketX + 10*GameMath.cos(this.lookingAngle);
				this.targetY = this.socketY - 10*GameMath.sin(this.lookingAngle);
				
				this.vars[0] = kyuBotBody.mainLevel.player.x;
				this.vars[1] = kyuBotBody.mainLevel.player.y;
			}
			if(this.aiType == 1) // launch toward a point and come back. 
			{
				// vars[0] and vars[1] will store the coordinates of the point being attacked by the claw launch. 
				
				if(this.harmonicTimer == 0)
					kyuBotBody.mainLevel.soundPlayer.play("sound/kyuArm.wav");
				
				double sin = GameMath.sin(harmonicTimer);
				this.targetX = this.socketX*(1-sin) + this.vars[0]*sin;
				this.targetY = this.socketY*(1-sin) + this.vars[1]*sin;
				
				this.harmonicTimer += 2; // harmonic motion timer
				
				if(this.harmonicTimer > 180)
				{
					this.harmonicTimer = 0;
					this.aiType = 0;
				}
			}
			
			this.x += (this.targetX - this.x)/5.0;
			this.y += (this.targetY - this.y)/5.0;
			this.rotate(lookingAngle);
		}
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
		g.drawImage(this.curImage, null, null);	
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
		
		if(type == 'a') 
		{
			curImgKey = "arm";
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 7;
			height = 7;
		}
		if(type == 'l') 
		{
			curImgKey = "clawLeft";
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 23;
			height = 23;
		}
		if(type == 'r') 
		{
			curImgKey = "clawRight";
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 23;
			height = 23;
		}
		
		//il.addImage(this.curImage);
	}
}
