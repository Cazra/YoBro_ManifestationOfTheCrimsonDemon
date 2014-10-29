
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


/**
*		BossSprite.java
*
*		This abstract class is what all other bosses will be produced from.
*		Since boss characters are more complex than generic enemies, each boss should have its own class.
*		The boss class is expected to also provide the logic for its spell card and non spell card attacks.
*		
*		
**/


public abstract class BossSprite extends Sprite
{
//	private static int numInstances = 0;
//	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
//	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public int aiMode;
	protected int frameNum;
	protected String curImgKey;
	protected char direction; 
	
	protected int blurLength;
	protected int blurSkip;
	protected AffineTransform[] blurTransforms;
	protected Image[] blurFrames;
	protected String[] blurFrameKeys;
	
	public DialogPortraitSprite portrait;
	
	// enemy vars
	
	public long timer;
	public double hp;
	public double dmgShield;
	protected int deathTimer;
	public int lastAttackIndex;

	public MainLevel mainLevel;
	public AnimatedBackgroundSprite stageBG;
	protected String difficulty;
	
	public boolean isActive;
	public int attackIndex;
	public String spellCardName;
	
	public ArrayList<Sprite> dependents; // bullets, bullet spawners, and SFX associated with this enemy. These will be destroyed when their parent is destroyed.
	public ArrayList<ItemSprite> itemDrops;
	
	
	
	// CONSTRUCTOR
	
	public BossSprite(double x, double y, MainLevel m)
	{
		super(x,y);
	//	numInstances++;
		
		direction = 'S';
		isActive = false;
		
		dependents = new ArrayList<Sprite>();
		itemDrops = new ArrayList<ItemSprite>();
		
		attackIndex = 0;
		hp = 0;
		dmgShield = 0.0;
		
		mainLevel = m;
		difficulty = mainLevel.difficulty;
		spellCardName = "";
	}
	
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	clean()
	*	Used to unload image data and cleans up static members of this Sprite extension 
	*		when the parent component is done with it.
	**/
	
/*	public static void clean()
	{
		numInstances = 0;
		focusTable.clear();
		imageTable.clear();
	}*/
	
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
		mainLevel = null;
	}
	
	/**
	*	loadImages()
	*	loads and stores image data for this Sprite.
	**/
	
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
		dmg *= Math.min(1.0, dmgShield);
		this.hp -= dmg;
		if(this.hp <= 0 && this.hp > -9999)
		{
			this.hp = -9999;
			if(this.attackIndex != this.lastAttackIndex)
			{
				for(Sprite s : dependents)
				{
					if(s instanceof BulletSprite)
					{
						mainLevel.createBulletPoint((BulletSprite) s);
					}
					
					s.destroy();
				}
				dependents.clear();
				
				return true;
			}
			else if(this.isActive)
			{
				this.deathTimer++;
				this.isActive = false;
			}
			
			
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
	
/*	protected void animate(ImageLoader il)
	{
		super.animate(il);
		
		// Animate current frame
		
		animateBlurs(il);
	}*/
	
	
	
/*	protected void animateBlurs(ImageLoader il)
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
	
	
	/**
	*	animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	*	Applies current semi-transparency to a frame of animation and stores the modified frame for later use.
	*	Preconditions: il is the ImgLoader passed in by animate(ImageLoader il).
	*		imgKey is the normal unmodified key name for the current frame of animation.
	*	Postconditions: the current image is modified with the current semitransparency. 
	*		If this semitransparent frame isn't already in the imageTable, it is added in.
	**/
	
/*	protected void animateSemiTransparencyFrame(ImageLoader il, String imgKey)
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
	}*/
	
	
	
	
	/////////////// Spell Card Logic
	
	
	public void runSpellCardLogic(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		mainLevel.iconEnemyHere.x = this.x + mainLevel.ORIGINX;
		if(dmgShield < 1.0)
			dmgShield *= 1.005;
		
		if(timer % 60 == 59)
		{
			mainLevel.numberSCTimer.value--;
			if(mainLevel.numberSCTimer.value == -1)
			{
				timer = 0;
				if(this.attackIndex != this.lastAttackIndex)
				{
					mainLevel.numberSCTimer.value = 60;
					attackIndex++;
				//	System.out.println("skipped due to timer");
					for(Sprite s : dependents)
					{
						sfx.add(FXSprite.makeSimpleOrb1(s.x,s.y));
						s.destroy();
					}
					dependents.clear();
				}
				else if(this.isActive)
				{
					deathTimer++;
					this.isActive = false;
				}
			}
		}
		if(deathTimer > 0)
		{
			deathTimer++;
			if(deathTimer % 2 == 0)
			{
				sfx.add(FXSprite.makeSimpleOrb3(this.x,this.y));
			}
			if(deathTimer %10 == 0)
			{
				mainLevel.soundPlayer.play("sound/WHOOSH01.WAV");
			}
		}
		if(deathTimer > 150)
		{
			attackIndex = lastAttackIndex + 1;
			deathTimer = 0;
			this.isVisible = false;
			
			mainLevel.soundPlayer.play("sound/EXPLOD13.WAV");
			
			for(Sprite s : dependents)
			{
				if(s instanceof BulletSprite)
				{
					mainLevel.createBulletPoint((BulletSprite) s);
				}
				
				s.destroy();
			}
			dependents.clear();
		}
		
		if(timer == 0)
			dmgShield = 0.1;
	}
	
	
	protected void enlistBullet(Sprite b, SpriteList bullets)
	{
		bullets.add(b);
		dependents.add(b);
	}
	
	protected void unEnlistDeadBullets()
	{
		ArrayList<Sprite> dependentsCopy = new ArrayList<Sprite>(dependents);
		for(Sprite s : dependentsCopy)
		{
			if(s.isDestroyed)
			{
				dependents.remove(s);
			}
		}
		
	}
	
	
	
	
}
