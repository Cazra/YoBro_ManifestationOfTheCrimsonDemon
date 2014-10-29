
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


public class EnemySprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public char type;
	public int skinID = 1; // used for palette swaps of same enemy types.
	private int frameNum;
	private String curImgKey;
	private char direction; 
	
	protected int blurLength;
	protected int blurSkip;
	protected AffineTransform[] blurTransforms;
	protected Image[] blurFrames;
	protected String[] blurFrameKeys;
	
	// for bladebot
	
	private Image part1, part2, part3;
	public double lookingAngle;
	private double bladeAngle;
	
	// enemy vars
	
	public long timer;
	public double hp;
	public int aiType;
	
	public boolean isSpawner; // spawners are not affected by player bullets. They do not hurt the player either.
	
	public ArrayList<Sprite> dependents; // bullets, bullet spawners, and SFX associated with this enemy. These will be destroyed when their parent is destroyed.
	public ArrayList<ItemSprite> itemDrops;
	
	
	
	// CONSTRUCTOR
	
	public EnemySprite(double x, double y, char t, double hitPoints)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		direction = 'S';
		hp = hitPoints;
		
		dependents = new ArrayList<Sprite>();
		itemDrops = new ArrayList<ItemSprite>();
		
		isSpawner = false;
		initHitRadius();
		
		lookingAngle = 270;
		bladeAngle = 0;
		
		
	}
	
	public EnemySprite(double x, double y)
	{
		super(x,y);
	}
	
	
	private void initHitRadius()
	{
		if(this.type == 'a')
			this.setRadius(9);
		if(this.type == 'b')
			this.setRadius(12);
		if(this.type == 'c')
			this.setRadius(12);
		if(this.type == 'd')
		{
			this.width = 48;
			this.height = 48;
			this.setRadius(22);
		}
		if(this.type == 'e')
			this.isSpawner = true;
		if(this.type == 'f')
		{
			this.setRadius(20);
			this.width = 48;
			this.height = 48;
		}
		if(this.type == 'g')
		{
			this.setRadius(45);
			this.width = 104;
			this.height = 100;
		}
		if(this.type == 'h')
		{
			this.setRadius(10);
			this.width = 31;
			this.height = 19;
		}
		if(this.type == 'i')
		{
			this.isSpawner = true;
		}
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
		for(Sprite s : dependents)
		{
			s.destroy();
		}
		dependents.clear();
		itemDrops.clear();
		
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
		
		// enemy robot 1
		
		imageURL = EnemySprite.class.getResource("graphics/EnemyBot.png");
		Image botSheet = tk.getImage(imageURL);
		botSheet = ColorFilters.setTransparentColor(botSheet, new Color(0xFF00FF));

			// skin 1
			
			img = ImageBlitter.crop(botSheet,1,1,23,23);
			il.addImage(img);
			focusTable.put("botE1",new Point(11,11));
			imageTable.put("botE1",img);
			
			img = ImageBlitter.crop(botSheet,26,1,23,23);
			il.addImage(img);
			focusTable.put("botE2",new Point(11,11));
			imageTable.put("botE2",img);
			
			img = ImageBlitter.crop(botSheet,1,26,23,23);
			il.addImage(img);
			focusTable.put("botS1",new Point(11,11));
			imageTable.put("botS1",img);
			
			img = ImageBlitter.crop(botSheet,26,26,23,23);
			il.addImage(img);
			focusTable.put("botS2",new Point(11,11));
			imageTable.put("botS2",img);
			
			img = ImageBlitter.crop(botSheet,1,51,23,23);
			il.addImage(img);
			focusTable.put("botW1",new Point(11,11));
			imageTable.put("botW1",img);
			
			img = ImageBlitter.crop(botSheet,26,51,23,23);
			il.addImage(img);
			focusTable.put("botW2",new Point(11,11));
			imageTable.put("botW2",img);
			
			// skin 2
			img = ImageBlitter.crop(botSheet,1,1+75,23,23);
			il.addImage(img);
			focusTable.put("botE1sk2",new Point(11,11));
			imageTable.put("botE1sk2",img);
			
			img = ImageBlitter.crop(botSheet,26,1+75,23,23);
			il.addImage(img);
			focusTable.put("botE2sk2",new Point(11,11));
			imageTable.put("botE2sk2",img);
			
			img = ImageBlitter.crop(botSheet,1,26+75,23,23);
			il.addImage(img);
			focusTable.put("botS1sk2",new Point(11,11));
			imageTable.put("botS1sk2",img);
			
			img = ImageBlitter.crop(botSheet,26,26+75,23,23);
			il.addImage(img);
			focusTable.put("botS2sk2",new Point(11,11));
			imageTable.put("botS2sk2",img);
			
			img = ImageBlitter.crop(botSheet,1,51+75,23,23);
			il.addImage(img);
			focusTable.put("botW1sk2",new Point(11,11));
			imageTable.put("botW1sk2",img);
			
			img = ImageBlitter.crop(botSheet,26,51+75,23,23);
			il.addImage(img);
			focusTable.put("botW2sk2",new Point(11,11));
			imageTable.put("botW2sk2",img);
		
		// enemy robot 2
		
		img = ImageBlitter.crop(botSheet,51,1,24,24);
		il.addImage(img);
		focusTable.put("bladebotCore",new Point(12,12));
		imageTable.put("bladebotCore",img);
		
		img = ImageBlitter.crop(botSheet,77,1,24,24);
		il.addImage(img);
		focusTable.put("bladebotSpecular",new Point(12,12));
		imageTable.put("bladebotSpecular",img);
		
		img = ImageBlitter.crop(botSheet,51,27,22,17);
		il.addImage(img);
		focusTable.put("bladebotBlade",new Point(9,24));
		imageTable.put("bladebotBlade",img);
		
		img = ImageBlitter.crop(botSheet,75,27,10,10);
		il.addImage(img);
		focusTable.put("bladebotEye",new Point(2,4));
		imageTable.put("bladebotEye",img);
		
		// missiles 
		
		img = ImageBlitter.crop(botSheet,51,46,31,19);
		il.addImage(img);
		focusTable.put("missile1",new Point(16,10));
		imageTable.put("missile1",img);
		
		img = ImageBlitter.crop(botSheet,51,67,31,19);
		il.addImage(img);
		focusTable.put("missile2",new Point(16,10));
		imageTable.put("missile2",img);
		
		// eye 1
		
		imageURL = EnemySprite.class.getResource("graphics/EnemyEye.png");
		Image eyeSheet = tk.getImage(imageURL);
		eyeSheet = ColorFilters.setTransparentColor(eyeSheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(eyeSheet,1,1,25,25);
		il.addImage(img);
		focusTable.put("eye1",new Point(12,12));
		imageTable.put("eye1",img);
		
		img = ImageBlitter.crop(eyeSheet,28,1,25,25);
		il.addImage(img);
		focusTable.put("eye2",new Point(12,12));
		imageTable.put("eye2",img);
		
		img = ImageBlitter.crop(eyeSheet,55,1,25,25);
		il.addImage(img);
		focusTable.put("eye3",new Point(12,12));
		imageTable.put("eye3",img);
		
		img = ImageBlitter.crop(eyeSheet,82,1,25,25);
		il.addImage(img);
		focusTable.put("eye4",new Point(12,12));
		imageTable.put("eye4",img);
		
		img = ImageBlitter.crop(eyeSheet,109,1,25,25);
		il.addImage(img);
		focusTable.put("eye5",new Point(12,12));
		imageTable.put("eye5",img);
		
		img = ImageBlitter.crop(eyeSheet,136,1,25,25);
		il.addImage(img);
		focusTable.put("eye6",new Point(12,12));
		imageTable.put("eye6",img);
		
		// smiley 1
		
		imageURL = EnemySprite.class.getResource("graphics/EnemyXD.png");
		Image smileySheet = tk.getImage(imageURL);
		smileySheet = ColorFilters.setTransparentColor(smileySheet, new Color(0xFF00FF));
			
			// skin 1
			img = ImageBlitter.crop(smileySheet,1,1,25,25);
			il.addImage(img);
			focusTable.put("smiley1",new Point(12,12));
			imageTable.put("smiley1",img);
			
			img = ImageBlitter.crop(smileySheet,28,1,25,25);
			il.addImage(img);
			focusTable.put("smiley2",new Point(12,12));
			imageTable.put("smiley2",img);
			
			img = ImageBlitter.crop(smileySheet,55,1,25,25);
			il.addImage(img);
			focusTable.put("smiley3",new Point(12,12));
			imageTable.put("smiley3",img);
			
			img = ImageBlitter.crop(smileySheet,82,1,25,25);
			il.addImage(img);
			focusTable.put("smiley4",new Point(12,12));
			imageTable.put("smiley4",img);
			
			// skin 2
			img = ImageBlitter.crop(smileySheet,1,28,25,25);
			il.addImage(img);
			focusTable.put("smiley1sk2",new Point(12,12));
			imageTable.put("smiley1sk2",img);
			
			img = ImageBlitter.crop(smileySheet,28,28,25,25);
			il.addImage(img);
			focusTable.put("smiley2sk2",new Point(12,12));
			imageTable.put("smiley2sk2",img);
			
			img = ImageBlitter.crop(smileySheet,55,28,25,25);
			il.addImage(img);
			focusTable.put("smiley3sk2",new Point(12,12));
			imageTable.put("smiley3sk2",img);
			
			img = ImageBlitter.crop(smileySheet,82,28,25,25);
			il.addImage(img);
			focusTable.put("smiley4sk2",new Point(12,12));
			imageTable.put("smiley4sk2",img);
		
		// spawner magic circle
		
		imageURL = EnemySprite.class.getResource("graphics/EnemySpawnerCircle.png");
		Image spawnerSheet = tk.getImage(imageURL);
		spawnerSheet = ColorFilters.setTransparentColor(spawnerSheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(spawnerSheet,0,0,48,48);
		il.addImage(img);
		focusTable.put("spawnerCircle1",new Point(24,24));
		imageTable.put("spawnerCircle1",img);
		
		img = ImageBlitter.crop(spawnerSheet,48,0,48,48);
		il.addImage(img);
		focusTable.put("spawnerCircle2",new Point(24,24));
		imageTable.put("spawnerCircle2",img);
		
		// crosshair
			
		imageURL = EnemySprite.class.getResource("graphics/crosshairSheet.png");
		spawnerSheet = tk.getImage(imageURL);
		spawnerSheet = ColorFilters.setTransparentColor(spawnerSheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(spawnerSheet,1,1,41,41);
		il.addImage(img);
		focusTable.put("crosshair1",new Point(20,20));
		imageTable.put("crosshair1",img);
		
		img = ImageBlitter.crop(spawnerSheet,44,1,41,41);
		il.addImage(img);
		focusTable.put("crosshair2",new Point(20,20));
		imageTable.put("crosshair2",img);
		
		img = ImageBlitter.crop(spawnerSheet,87,1,41,41);
		il.addImage(img);
		focusTable.put("crosshair3",new Point(20,20));
		imageTable.put("crosshair3",img);
		
		// mine 1: toxic barrel
		
		imageURL = EnemySprite.class.getResource("graphics/EnemyMine.png");
		Image mineSheet = tk.getImage(imageURL);
		mineSheet = ColorFilters.setTransparentColor(mineSheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(mineSheet,1,1,27,27);
		il.addImage(img);
		focusTable.put("barrel1",new Point(13,13));
		imageTable.put("barrel1",img);
		
		img = ImageBlitter.crop(mineSheet,30,1,27,27);
		il.addImage(img);
		focusTable.put("barrel2",new Point(13,13));
		imageTable.put("barrel2",img);
		
		img = ImageBlitter.crop(mineSheet,59,1,27,27);
		il.addImage(img);
		focusTable.put("barrel3",new Point(13,13));
		imageTable.put("barrel3",img);
		
		// giantSmiley
		
		imageURL = EnemySprite.class.getResource("graphics/Midboss3Sheet.png");
		Image bigSmileySheet = tk.getImage(imageURL);
		bigSmileySheet = ColorFilters.setTransparentColor(bigSmileySheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(bigSmileySheet,1,1,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley1",new Point(52,50));
		imageTable.put("bigSmiley1",img);
		
		img = ImageBlitter.crop(bigSmileySheet,107,1,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley2",new Point(52,50));
		imageTable.put("bigSmiley2",img);
		
		img = ImageBlitter.crop(bigSmileySheet,213,1,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley3",new Point(52,50));
		imageTable.put("bigSmiley3",img);
		
		img = ImageBlitter.crop(bigSmileySheet,319,1,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley4",new Point(52,50));
		imageTable.put("bigSmiley4",img);
		
		img = ImageBlitter.crop(bigSmileySheet,425,1,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley5",new Point(52,50));
		imageTable.put("bigSmiley5",img);
		
		img = ImageBlitter.crop(bigSmileySheet,1,103,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley6",new Point(52,50));
		imageTable.put("bigSmiley6",img);
		
		img = ImageBlitter.crop(bigSmileySheet,107,103,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley7",new Point(52,50));
		imageTable.put("bigSmiley7",img);
		
		img = ImageBlitter.crop(bigSmileySheet,213,103,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley8",new Point(52,50));
		imageTable.put("bigSmiley8",img);
		
		img = ImageBlitter.crop(bigSmileySheet,319,103,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley9",new Point(52,50));
		imageTable.put("bigSmiley9",img);
		
		img = ImageBlitter.crop(bigSmileySheet,425,103,104,100);
		il.addImage(img);
		focusTable.put("bigSmiley10",new Point(52,50));
		imageTable.put("bigSmiley10",img);
		
		System.out.println("loaded image data for EnemySprite");

	}
	
	
	
	
	
	// LOGIC METHODS
	
	public void setDirection(char dir)
	{
		if(dir != 'S' && dir != 'E' && dir != 'W')
			return;
		else
			this.direction = dir;
	}
	
	
	public void setBlur(int length, int skip)
	{
		blurLength = length;
		blurSkip = skip;
		blurFrameKeys = new String[length];
		blurFrames = new Image[length];
		blurTransforms = new AffineTransform[length];
	}
	
	
	
	/**
	*	boolean damage(int dmg)
	*	Causes this enemy to take damage.
	*	Preconditions: dmg is how much damage is dealt.
	*	Postconditions: This enemy's hp drops by dmg. 
	*		If its HP after taking the damage is <= 0, returns true. Else returns false.
	**/
	
	public boolean damage(int dmg)
	{
		this.hp -= dmg;
		if(this.hp <= 0)
		{
			this.hp = 0;
			return true;
		}
		return false;
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
		
		if(this.type == 'd')
		{
			Point curFocus;
			
			g.setTransform(origTrans);	
			curFocus = focusTable.get("bladebotBlade");
			g.translate(this.fx, this.fy);
			g.rotate(GameMath.d2r(0-bladeAngle));
			g.translate(0-curFocus.x, 0-curFocus.y);
			g.drawImage(this.part2, null, null);
			
			g.setTransform(origTrans);	
			g.translate(this.fx, this.fy);
			g.rotate(GameMath.d2r(120-bladeAngle));
			g.translate(0-curFocus.x, 0-curFocus.y);
			g.drawImage(this.part2, null, null);
			
			g.setTransform(origTrans);	
			g.translate(this.fx, this.fy);
			g.rotate(GameMath.d2r(240-bladeAngle));
			g.translate(0-curFocus.x, 0-curFocus.y);
			g.drawImage(this.part2, null, null);
			
			g.setTransform(origTrans);	
			curFocus = focusTable.get("bladebotCore");
			g.translate(curFocus.x,curFocus.y);
			g.drawImage(this.curImage, null, null);
			
			g.setTransform(origTrans);	
			curFocus = focusTable.get("bladebotEye");
			g.translate(this.fx, this.fy+2);
			g.rotate(GameMath.d2r(0-lookingAngle));
			g.translate(0-curFocus.x, 0-curFocus.y);
			g.drawImage(this.part3, null, null);
			
			g.setTransform(origTrans);
			curFocus = focusTable.get("bladebotSpecular");
			g.translate(curFocus.x,curFocus.y);
			g.drawImage(this.part1,null,null);
		}
		else if(this.type == 'e') { // Magic Octagrams 
			g.translate(fx,fy);
			g.rotate(0-GameMath.d2r(this.rotation*0.5));
			g.scale(0.3,0.3);
			g.translate(0-fx,0-fy);
			g.drawImage(this.curImage, null, null);
			g.setTransform(origTrans);
			
			g.translate(fx,fy);
			g.rotate(GameMath.d2r(this.rotation*2));
			g.scale(0.75,0.75);
			g.translate(0-fx,0-fy);
			g.drawImage(this.curImage, null, null);
			g.setTransform(origTrans);
			
			g.drawImage(this.curImage, null, null);
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
		
		if(type == 'a') // robot
		{
			curImgKey = "bot" + this.direction + ((this.frameNum/2) + 1);
			if(skinID > 1) curImgKey += "sk" + skinID;
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 23;
			height = 23;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 2*2)
				this.frameNum = 0;
		}
		if(type == 'b') // eye 1
		{
			curImgKey = "eye" + ((this.frameNum/5) + 1);
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 25;
			height = 25;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 6*5)
				this.frameNum = 0;
		}
		if(type == 'c') // smiley 1
		{
			curImgKey = "smiley" + ((this.frameNum/5) + 1);
			if(skinID > 1) curImgKey += "sk" + skinID;
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 25;
			height = 25;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 4*5)
				this.frameNum = 0;
		}
		if(type == 'd') // robot 2: blade bot
		{
			curImgKey = "bladebotSpecular";
			part1 = imageTable.get(curImgKey);
			animateSemiTransparencyFrame(il,curImgKey);
				
			curImgKey = "bladebotBlade";
			part2 = imageTable.get(curImgKey);
			animateSemiTransparencyFrame(il,curImgKey);	
			
			curImgKey = "bladebotEye";
			part3 = imageTable.get(curImgKey);
			animateSemiTransparencyFrame(il,curImgKey);	
			
			curImgKey = "bladebotCore";
			curImage = imageTable.get(curImgKey);
			animateSemiTransparencyFrame(il,curImgKey);
			
			bladeAngle+= 15;
			
			fx = 24;
			fy = 24;	
			width = 48;
			height = 48;
		}
		if(type == 'e') // magic circle spawner
		{
			curImgKey = "spawnerCircle" + ((this.frameNum/2) + 1);
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 48;
			height = 48;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 2*2)
				this.frameNum = 0;
				
			this.rotate(this.getRotate()+2);
		}
		if(type == 'f') // barrel mine
		{
			int imgNum = 1;
			if(frameNum >= 5)
				imgNum = 2;
			if(frameNum >= 10)
				imgNum = 3;
			if(frameNum >= 15)
				imgNum = 3;
			if(frameNum >= 20)
				imgNum = 2;
			if(frameNum >= 25)
				imgNum = 1;
			
			curImgKey = "barrel" + imgNum;
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 27;
			height = 27;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 30)
				this.frameNum = 0;
		}
		if(type == 'g') // giant smiley
		{
			curImgKey = "bigSmiley";
			int imgNum = 1;
			int spd = 3;
			
			if(this.frameNum >= spd)
				imgNum++;
			if(this.frameNum >= spd*2)
				imgNum++;
			if(this.frameNum >= spd*3)
				imgNum++;
			if(this.frameNum >= spd*4)
				imgNum++;
			if(this.frameNum >= spd*5)
				imgNum++;
			if(this.frameNum >= spd*9)
				imgNum++;
			if(this.frameNum >= spd*10)
				imgNum++;
			if(this.frameNum >= spd*11)
				imgNum++;
			if(this.frameNum >= spd*12)
				imgNum++;
			
			curImgKey += imgNum;
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 13*spd)
				this.frameNum = 0;
		}
		if(type == 'h') // missile (usually paired with a crosshair)
		{
			curImgKey = "missile" + ((this.frameNum/2) + 1);
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 31;
			height = 19;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 2*2)
				this.frameNum = 0;
		}
		if(type == 'i') // crosshair (usually paired with a missile)
		{
			curImgKey = "crosshair" + ((this.frameNum/2) + 1);
			curImage = imageTable.get(curImgKey);
			
			Point curFocus = focusTable.get(curImgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 41;
			height = 41;
			
			animateSemiTransparencyFrame(il,curImgKey);
			this.frameNum++;
			if(this.frameNum >= 2*3)
				this.frameNum = 0;
				
			this.rotate(this.getRotate()-3);
		}

		if(semiTransparency > 0.0) // apply semi-transparency if necessary
		{
			colorTransformChanged = false;
		}	
		
		//il.addImage(this.curImage);
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
		String result = imgKey;
		if(skinID > 1) result += "sk" + skinID;
		
		if(imageTable.containsKey(result))
		{
			curImage = imageTable.get(result);
		}
		else
		{
			Image semicurImage = curImage;
			imageTable.put(result, semicurImage);
			
			curImage = semicurImage;
			il.addImage(curImage);
		}
	}
	
	
	
	
	
}
