
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.net.URL;
import gameEngine.*;

public class BulletSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private char type;
	public Color color;
	private String curImgKey;
	
/*	protected int blurLength;
	protected int blurSkip;
	protected AffineTransform[] blurTransforms;
	protected Image[] blurFrames;
	protected String[] blurFrameKeys;*/
	
	public int aiMode;
	public boolean isGrazed;
	public boolean isActive;
	public boolean killable = true;
	public boolean isInverted = false;
	
	public double dx;
	public double dy;
	
	public double rad, rad2;
	public double theta;
	public double cx, cy;
	
	public double scaleTarget = -1;
	
	// CONSTRUCTOR
	
	public BulletSprite(double x, double y, char t, Color c)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		color = c;
		
	//	blurLength = 0;
	//	blurSkip = 0;
		
		dx = 0;
		dy = 0;
		isGrazed = false;
		isActive = true;
		aiMode = 0;
		
		if(t == 'l')
		{
			collisionType = 1;
			scale(1.0,0.1);
		}
	}
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		if(!isDestroyed)
		{
			super.destroy();
			numInstances--;
		}
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
		
		imageURL = BulletSprite.class.getResource("graphics/BulletsSmallSheet.png");
		Image bulletSheet = tk.getImage(imageURL);
		bulletSheet = ColorFilters.setTransparentColor(bulletSheet, new Color(0xFF00FF));
		
		imageURL = BulletSprite.class.getResource("graphics/BulletInverse.png");
		Image invertBullet = tk.getImage(imageURL);
		invertBullet = ColorFilters.setTransparentColor(invertBullet, new Color(0xFF00FF));
		
		imageURL = BulletSprite.class.getResource("graphics/BulletLaser.png");
		Image laser1 = tk.getImage(imageURL);
		laser1 = ColorFilters.setTransparentColor(laser1, new Color(0xFF00FF));
		laser1 = ColorFilters.setSemiTransparency(laser1, 0.1);
		
		imageURL = BulletSprite.class.getResource("graphics/BulletHighRes.png");
		Image giantSheet = tk.getImage(imageURL);
		giantSheet = ColorFilters.setTransparentColor(giantSheet, new Color(0x000000));
		
		
		bulletImg = ImageBlitter.crop(bulletSheet,1,1,9,9);
		il.addImage(bulletImg);
		focusTable.put("bullet1",new Point(4,4));
		imageTable.put("bullet1",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,12,1,13,13);
		il.addImage(bulletImg);
		focusTable.put("bullet2",new Point(6,6));
		imageTable.put("bullet2",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,27,1,13,13);
		il.addImage(bulletImg);
		focusTable.put("bullet3",new Point(6,6));
		imageTable.put("bullet3",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,42,1,15,15);
		il.addImage(bulletImg);
		focusTable.put("bullet4",new Point(7,7));
		imageTable.put("bullet4",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,59,1,15,15);
		il.addImage(bulletImg);
		focusTable.put("bullet5",new Point(7,7));
		imageTable.put("bullet5",bulletImg);
		
		bulletImg = ImageBlitter.crop(bulletSheet,76,1,14,9);
		il.addImage(bulletImg);
		focusTable.put("bullet6",new Point(7,4));
		imageTable.put("bullet6",bulletImg);
		
		
		bulletImg = laser1;
		il.addImage(bulletImg);
		focusTable.put("laser1",new Point(15,15));
		imageTable.put("laser1",bulletImg);
		
		
		bulletImg = ImageBlitter.crop(invertBullet,0,0,63,63);
		Image alphaImg = ImageBlitter.crop(invertBullet,63,0,63,63);
		bulletImg = ColorFilters.applyAlphaMap(bulletImg, alphaImg, 63, 63);
		il.addImage(bulletImg);
		focusTable.put("bulletInv1",new Point(31,31));
		imageTable.put("bulletInv1",bulletImg);
		
		bulletImg = giantSheet;
		bulletImg = ColorFilters.applyAlphaMap(bulletImg, bulletImg, 321, 321);
		il.addImage(bulletImg);
		focusTable.put("bulletHiRes",new Point(160,160));
		imageTable.put("bulletHiRes",bulletImg);
		
		
		System.out.println("loaded image data for BulletSprite");
		
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
	
	
/*	public void setBlur(int length, int skip)
	{
		blurLength = length;
		blurSkip = skip;
		blurFrameKeys = new String[length];
		blurFrames = new Image[length];
		blurTransforms = new AffineTransform[length];
	}*/
	
	
	
	public void move()
	{
		if(aiMode == 0) // simple pattern, moves according to current dx,dy.
		{
			x += dx;
			y += dy;
			screenKillable = true;
		}
		if(aiMode == 1) // circular pattern, with constant speed and constant change in radius.
		{
			x = cx + rad * GameMath.cos(theta);
			y = cy - rad * GameMath.sin(theta);
			
			rad += dy;
			theta += dx / rad;
			
			this.rotate(theta+90);
			
			screenKillable = false;
		}
		if(aiMode == 2) // rotating pattern. Constant speed but with changing angle.
		{
			x += dx*GameMath.cos(theta);
			y -= dx*GameMath.sin(theta);
			this.rotate(theta);
			theta += dy;
			dy*= rad;
			
			if(Math.abs(dy/dx)< 0.01)
				screenKillable = true;
			else
				screenKillable = false;
		}
		if(aiMode == 3) // projectile pattern. Like simple pattern, but with gravity(or anti-gravity) affecting its y-velocity.
		{
			x += dx;
			y += dy;
			dx += cx;
			dy += cy;
			
			screenKillable = false;
			if(dx*cx >= 0 && dy*cy >= 0)
				screenKillable = true;
				
			this.rotate(GameMath.getAngleTo(x,y,x+dx,y+dy));
		}
		if(aiMode == 4) { // fading pattern. Can be used in groups to create short-lived explosions. 
			x += dx;
			y += dy;
			
			cx++;
			double alpha = cx/cy;
			this.setSemiTransparency(alpha);
			this.scale(rad*alpha/2 + (1-alpha)*rad, rad*alpha/2 + (1-alpha)*rad);
			
			if(cx > cy) this.destroy();
			
			screenKillable = false;
		}
		if(aiMode == 5) { // swerving pattern. 
			x += dx;
			y += dy;
			
			dx = rad * GameMath.cos(theta);
			dy = -1* rad * GameMath.sin(theta);
			
			theta += cx*GameMath.cos(cy);
			this.rotate(theta);
			
			cy += rad2;
			
			screenKillable = true;
		}
	}
	
	
	public void setVelocity(double speed, double angle)
	{
		dx = speed * GameMath.cos(angle);
		dy = -1* speed * GameMath.sin(angle);
		theta = angle;
		this.rotate(angle);
	}
	
	public void setRotationalVelocity(double speed, double initAngle, double dTheta, double thetaDecay)
	{
		dx = speed;
		dy = dTheta;
		rad = thetaDecay;
		theta = initAngle;
		
		aiMode = 2;
	}
	
	public void setCircularVelocity(double centerX, double centerY, double initAngle, double initRad,double circumferenceSpeed, double radialSpeed)
	{
		dx = 360*circumferenceSpeed/(2*Math.PI);
		dy = radialSpeed;
		theta = initAngle;
		rad = initRad;
		cx = centerX;
		cy = centerY;
		
		aiMode = 1;
	}
	
	public void setProjectileVelocity(double speed, double angle, double gravityY)
	{
		dx = speed*GameMath.cos(angle);
		dy = 0-speed*GameMath.sin(angle);
		cy = gravityY;
		cx = 0;
		
		aiMode = 3;
	}
	
	public void setProjectileVelocity(double speed, double angle, double gravityX, double gravityY)
	{
		dx = speed*GameMath.cos(angle);
		dy = 0-speed*GameMath.sin(angle);
		cy = gravityY;
		cx = gravityX;
		
		aiMode = 3;
	}
	
	public void setFadingVelocity(double speed, double angle, double life, double initScale)
	{
		dx = speed * GameMath.cos(angle);
		dy = -1* speed * GameMath.sin(angle);
		theta = angle;
		this.rotate(angle);
		
		cx = 0;
		cy = life;
		
		rad = initScale;
		
		aiMode = 4;
	}
	
	public void setSwervingVelocity(double speed, double angle, double dTheta, double dTheta2) {
		dx = speed * GameMath.cos(angle);
		dy = -1* speed * GameMath.sin(angle);
		
		rad = speed;
		theta = angle;
		
		cx = dTheta;
		cy = 0;
		
		rad2 = dTheta2;
		
		aiMode = 5;
	}
	
	
	public static int getCount()
	{
		return numInstances;
	}
	
	public char getType()
	{
		return type;
	}
	
	// RENDERING METHODS
	
	/*
	public boolean render(Graphics2D g)
	{	
		Rectangle bbox = getBBox(true);
		if(bbox.x+bbox.width >= 0 && bbox.y+bbox.height >= 0 && bbox.x <= 400 && bbox.y <= 448)
			return super.render(g);
		else
			return false;
	}*/
	
	/**
	*	draw(Graphics2D g)
	*	Helper method draws the image of this Sprite after transformations are readied. Called by render(Graphics2D g).
	*	If you need to do something fancy while rendering this sprite's image, override this method.
	*	Preconditions: g is the Graphics2D object passed in by render(Graphcis2D g).
	*	Postconditions: this sprite is drawn, applying the transformations readied in render(Graphics2D g).
	**/
	
	protected void draw(Graphics2D g)
	{
	/*	AffineTransform origTrans = g.getTransform();
		
		// draw any blurred images
		
		for(int i = blurLength-1; i >= 0 ; i--)
		{
			if(blurFrameKeys[i] != null && (i+1) % blurSkip == 0)
			{	
				g.setTransform(blurTransforms[i]);
				g.drawImage(blurFrames[i], null, null);
			}
		}
		g.setTransform(origTrans);*/
		
		// draw current frame's image
		g.drawImage(this.curImage, null, null);
	//	g.setColor(new Color(0x0000FF));
	//	g.fillOval((int)(0-r+fx),(int)(0-r+fy),(int)(r*2),(int)(r*2));
		// process blurs for next frame.
	/*	for(int i = blurLength-1; i >= 0; i--)
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
		}*/
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
		if(type == 'a') // tiny bullet
		{
			curImage = imageTable.get("bullet1");
			
			Point curFocus = focusTable.get("bullet1");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 9;
			height = 9;
			this.setRadius(2);
			
			String frameName = animateColorFrame(il,"bullet1");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'b') // small wave bullet
		{
			curImage = imageTable.get("bullet2");
			
			Point curFocus = focusTable.get("bullet2");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 13;
			height = 13;
			this.setRadius(4);
			
			String frameName = animateColorFrame(il,"bullet2");
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'c') // plain small bullet
		{
			curImage = imageTable.get("bullet3");
			
			Point curFocus = focusTable.get("bullet3");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 13;
			height = 13;
			this.setRadius(4);
			
			String frameName = animateColorFrame(il,"bullet3");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'd') // plain medium bullet
		{
			curImage = imageTable.get("bullet4");
			
			Point curFocus = focusTable.get("bullet4");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 15;
			height = 15;
			this.setRadius(5);
			
			String frameName = animateColorFrame(il,"bullet4");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'e') // ring medium bullet
		{
			curImage = imageTable.get("bullet5");
			
			Point curFocus = focusTable.get("bullet5");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 13;
			height = 13;
			this.setRadius(5);
			
			String frameName = animateColorFrame(il,"bullet4");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'i') // large inverted bullet
		{
			curImage = imageTable.get("bulletInv1");
			
			Point curFocus = focusTable.get("bulletInv1");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 63;
			height = 63;
			this.setRadius(18);
			
			String frameName = animateColorFrame(il,"bulletInv1");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'l') // standard laser
		{
			curImage = imageTable.get("laser1");
			
			Point curFocus = focusTable.get("laser1");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 500;
			height = 31;
			this.setRadius(12);
			
			double targetScale;
			if(isActive)
				targetScale = 1.0;
			else
				targetScale = 0.1;
			this.scale(1.0,this.getScaleY() + (targetScale - this.getScaleY())/8.0);
			
			String frameName = animateColorFrame(il,"laser1");
			curImgKey = frameName;
		//	animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'm') // shard bullet
		{
			curImage = imageTable.get("bullet6");
			
			Point curFocus = focusTable.get("bullet6");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 14;
			height = 9;
			this.setRadius(3);
			
			String frameName = animateColorFrame(il,"bullet6");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		if(type == 'n') // hi-res aplpha bullet
		{
			curImage = imageTable.get("bulletHiRes");
			
			Point curFocus = focusTable.get("bulletHiRes");
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 321;
			height = 321;
			this.setRadius(140);
			
			String frameName = animateColorFrame(il,"bulletHiRes");
			curImgKey = frameName;
			animateSemiTransparencyFrame(il,frameName);
		}
		
		if(scaleTarget != -1) {
			double newScaleX = this.getScaleX() + (scaleTarget - this.getScaleX())/20.0;
			double newScaleY = this.getScaleY() + (scaleTarget - this.getScaleY())/20.0;
			this.scale(newScaleX, newScaleY);
		}
		
		rx = r*this.getScaleX();
		ry = r*this.getScaleY();
		
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
		
	//	System.out.print(result);
		
		if(isInverted)
			result += "inv";
		
		if(imageTable.containsKey(result))
		{
		//	System.out.println("\t cached");
			curImage = imageTable.get(result);
		}
		else
		{
			Image semicurImage = curImage;
			if(isInverted)
				semicurImage = ColorFilters.invert(curImage);
			imageTable.put(result, semicurImage);
			
			curImage = semicurImage;
			il.addImage(curImage);
			
		//	System.out.println("\t not cached");
		}
	}
	
	/**
	*	animateColorFrame(ImageLoader il, String imgKey)
	*	Applies current color-add to a frame of animation and stores the modified frame for later use.
	*	Preconditions: il is the ImgLoader passed in by animate(ImageLoader il).
	*		imgKey is the normal unmodified key name for the current frame of animation.
	*	Postconditions: the current image is modified with the current color-add. 
	*		If this color-add frame isn't already in the imageTable, it is added in.
	**/
	
	protected String animateColorFrame(ImageLoader il, String imgKey)
	{
		String keyTag = this.color.getRed() + "," + this.color.getGreen() + "," + this.color.getBlue();
		String result = imgKey + "c" + keyTag;
		
		if(imageTable.containsKey(result))
		{
			curImage = imageTable.get(result);
		}
		else
		{
			Image colorAddImg = ColorFilters.addColor(curImage,this.color);
			imageTable.put(result, colorAddImg);
			
			curImage = colorAddImg;
			il.addImage(curImage);
		}
		
		return result;
	}
	
	
	
}
