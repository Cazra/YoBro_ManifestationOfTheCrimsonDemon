
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.RadialGradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.net.URL;
import gameEngine.*;



public class BossSwirlyKyu extends BossSprite
{
	private static int numInstances = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	// enemy vars
	
	public KyuArmSprite leftClaw;
	public KyuArmSprite rightClaw;
	public LinkedList<KyuArmSprite> leftArm;
	public LinkedList<KyuArmSprite> rightArm;
	
	public double fogRadius;
	public double fogTargetRadius;
	public boolean fogOn;
	public Color fogColor;
	
	
	// CONSTRUCTOR
	
	public BossSwirlyKyu(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(20);
		this.frameNum = 0;
		this.lastAttackIndex = 5;
		portrait = new PortraitSwirlyKyuSprite(m.SCREENW +100,300,"Dr. Swirly ~ & Kyu-bot");
		portrait.vars[0] = m.SCREENW +100;
		portrait.vars[1] = 100;
		
		fogRadius = 200;
		fogTargetRadius = 400;
		fogOn = false;
		fogColor = new Color(0x00BBBBFF,true);
		
		leftClaw = new KyuArmSprite(x - 45,y + 10, 'l', this);
		rightClaw = new KyuArmSprite(x + 45, y+ 10, 'r', this);
		
		leftArm = new LinkedList<KyuArmSprite>();
		rightArm = new LinkedList<KyuArmSprite>();
		
		int numSegments = 5;
		
		for(int i = 0; i < numSegments; i++)
		{
			KyuArmSprite leftSegment = new KyuArmSprite(x - 25, y, 'a', this);
			leftSegment.targetClaw = leftClaw;
			leftSegment.harmonicTimer = 180 - 180/numSegments*i;
			leftArm.add(leftSegment);
			
			KyuArmSprite rightSegment = new KyuArmSprite(x + 25, y, 'a', this);
			rightSegment.targetClaw = rightClaw;
			rightSegment.harmonicTimer = 180 - 180/numSegments*i;
			rightArm.add(rightSegment);
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
		
		PortraitSwirlyKyuSprite.clean();
	}
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/

	public void destroy()
	{
		super.destroy();
		numInstances--;
		leftClaw.destroy();
		rightClaw.destroy();
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
		
		imageURL = BossSwirlyKyu.class.getResource("graphics/SwirlyAndKyubotSheet.png");
		Image slushSheet = tk.getImage(imageURL);
		slushSheet = ColorFilters.setTransparentColor(slushSheet, new Color(0xFF00FF));
		
		// Forward
		
		img = ImageBlitter.crop(slushSheet,1,1,47,41);
		il.addImage(img);
		focusTable.put("bodyS1",new Point(23,20));
		imageTable.put("bodyS1",img);
		
		img = ImageBlitter.crop(slushSheet,50,1,47,41);
		il.addImage(img);
		focusTable.put("bodyS2",new Point(23,20));
		imageTable.put("bodyS2",img);
		
		img = ImageBlitter.crop(slushSheet,99,1,47,41);
		il.addImage(img);
		focusTable.put("bodyS3",new Point(23,20));
		imageTable.put("bodyS3",img);
		
		img = ImageBlitter.crop(slushSheet,50,1,47,41);
		il.addImage(img);
		focusTable.put("bodyS4",new Point(23,20));
		imageTable.put("bodyS4",img);

		
		// Right 

		img = ImageBlitter.crop(slushSheet,1,44,47,41);
		il.addImage(img);
		focusTable.put("bodyE1",new Point(23,20));
		imageTable.put("bodyE1",img);
		
		img = ImageBlitter.crop(slushSheet,50,44,47,41);
		il.addImage(img);
		focusTable.put("bodyE2",new Point(23,20));
		imageTable.put("bodyE2",img);
		
		img = ImageBlitter.crop(slushSheet,99,44,47,41);
		il.addImage(img);
		focusTable.put("bodyE3",new Point(23,20));
		imageTable.put("bodyE3",img);
		
		img = ImageBlitter.crop(slushSheet,50,44,47,41);
		il.addImage(img);
		focusTable.put("bodyE4",new Point(23,20));
		imageTable.put("bodyE4",img);
		
		// Left

		img = ImageBlitter.crop(slushSheet,1,87,47,41);
		il.addImage(img);
		focusTable.put("bodyW1",new Point(23,20));
		imageTable.put("bodyW1",img);
		
		img = ImageBlitter.crop(slushSheet,50,87,47,41);
		il.addImage(img);
		focusTable.put("bodyW2",new Point(23,20));
		imageTable.put("bodyW2",img);
		
		img = ImageBlitter.crop(slushSheet,99,87,47,41);
		il.addImage(img);
		focusTable.put("bodyW3",new Point(23,20));
		imageTable.put("bodyW3",img);
		
		img = ImageBlitter.crop(slushSheet,50,87,47,41);
		il.addImage(img);
		focusTable.put("bodyW4",new Point(23,20));
		imageTable.put("bodyW4",img);
		
		System.out.println("loaded image data for BossSwirlyKyu");
		
		PortraitSwirlyKyuSprite.loadImages(il);
	}
	
	// LOGIC METHODS
	
	
	
	
	
	// RENDERING METHODS
	
	
	public boolean render(Graphics2D g)
	{
		super.render(g);
		
		for(KyuArmSprite arm : leftArm)
			arm.render(g);
		for(KyuArmSprite arm : rightArm)
			arm.render(g);
			
		leftClaw.render(g);
		rightClaw.render(g);
		
		return true;
	}
	
	
	public void renderFog(Graphics2D g, double x, double y)
	{
		float[] dist = {0.5f, 1.0f};
		if(fogOn && fogColor.getAlpha() < 255)
			fogColor = new Color(fogColor.getRed(), fogColor.getGreen(), fogColor.getBlue(), fogColor.getAlpha() + 5);
		else if(!fogOn && fogColor.getAlpha() > 0)
			fogColor = new Color(fogColor.getRed(), fogColor.getGreen(), fogColor.getBlue(), fogColor.getAlpha() - 5);
			
		fogRadius += (fogTargetRadius - fogRadius)/10.0;
			
		Color[] colors = {new Color(0x00BBBBFF, true),fogColor};
		RadialGradientPaint fogGradient = new RadialGradientPaint(new Point((int)x,(int)y), (int)fogRadius, dist, colors);
		g.setPaint(fogGradient);
		g.fillRect(-20,-20,mainLevel.SCREENW + 40, mainLevel.SCREENH + 40);
		
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
		
		// Animate current frame
		
		String imgKey = "body" + this.direction;
		imgKey += (this.frameNum/10 + 1) + "";
		
		curImage = imageTable.get(imgKey);
		
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
		
		width = 47;
		height = 41;

		this.frameNum++;
		if(this.frameNum >= 40)
			this.frameNum = 0;
		
		leftClaw.animate(il);
		rightClaw.animate(il);
		
		for(KyuArmSprite arm : leftArm)
			arm.animate(il);
		for(KyuArmSprite arm : rightArm)
			arm.animate(il);
	//	animateBlurs(il);
	}
	
	
	//////////////////// Spell Card logic
	
	
	
	public void runSpellCardLogic(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		super.runSpellCardLogic(bullets, slaves, sfx);
		isActive = true;
		if(attackIndex == 0) // nonSpellcard 1
		{	
			nsc1(bullets,slaves,sfx);
			//nsc02(bullets,slaves,sfx);
		}
		if(attackIndex == 1) // Cryo-Tech - Not a Freeze Ray, It's an Ice Beam (EN) /Omega Tech - Perfect Freeze Ray (It's an Ice Beam) (HL)
		{
			stageBG.nextBGID = 2;
			notAFreezeRayItsAnIceBeam(bullets,slaves,sfx);
		}
		if(attackIndex == 2) // nonSpellcard 2 (icicle fall)
		{
			stageBG.nextBGID = 0;
			nsc2(bullets,slaves,sfx);
		}
		if(attackIndex == 3) // Math-Tech ~ Shivering Fractal / Perfect Math Class ~ Fractal Snowflake
		{
			stageBG.nextBGID = 3;
			fractalSnowflake(bullets,slaves,sfx);
		}
		if(attackIndex == 4) // arms explode off, begin expelling mist
		{
			nsc3(bullets,slaves,sfx);
		}
		if(attackIndex == 5) // Emergency-Tech ~ Cryo-fog (EN) / Contigency ~ Blizzard Mist (HL) 
		{
			stageBG.nextBGID = 1;
			cryoFog(bullets,slaves,sfx);
		}
		
		
		// set health bar values and spell card text

		portrait.text = spellCardName;
		mainLevel.barBossHealth.caption2 = spellCardName;
		
		// increment timer
		
		timer++;
	}

	
	// NSC0 used to test Kyubot's arms
	
	private void nsc0(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 400;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "";
		}
		
		
		if(timer % 180 == 0)
			leftClaw.aiType = 1;
		if((timer + 90) % 180 == 0)
			rightClaw.aiType = 1;
		
		
		unEnlistDeadBullets();
		
		// Citrus movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		x += vars[0];
		vars[2] += vars[1];
		
		vars[0] *= 0.95;
		vars[1] *= 0.95;
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
		
		if(x < 0)
			vars[0] = 5;
		if(x > mainLevel.SCREENW)
			vars[0] = -5;
		if(y < 0)
			vars[1] = 5;
		if(y > 200)
			vars[1] = -5;
	}
	
	// NSC 1
	
	private void nsc1(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 400;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			timer = 100;
			
		//	fogTargetRadius = 200;
		//	fogOn = true;
			
			spellCardName = "";
		}
		
		/*
		if(timer % 180 == 0)
			leftClaw.aiType = 1;
		if((timer + 90) % 180 == 0)
			rightClaw.aiType = 1;
			*/
		
		if(timer == 100 || (timer == 110 && difficulty != "Easy") || timer == 120 || (timer == 130 && difficulty != "Easy"))
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			int angleSpacing = 20;
			if(difficulty.equals("Hard"))
				angleSpacing = 12;
			if(difficulty == "Lunatic")
				angleSpacing = 10;
			
			mainLevel.soundPlayer.play("sound/Grapling.wav");
			
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=angleSpacing)
			{
				BulletSprite bullet = new BulletSprite(x,y,'d',new Color(0x7777FF));
				bullet.setVelocity(2.0,i);
				enlistBullet(bullet, bullets);
				
				if(difficulty == "Lunatic")
				{
					bullet = new BulletSprite(x,y,'d',new Color(0x7777FF));
					bullet.setVelocity(2.0,i+3);
					enlistBullet(bullet, bullets);
				
				}
			}
			
			
		}
		
		if(timer == 140)
			leftClaw.aiType = 1;
		if(timer == 230)
			rightClaw.aiType = 1;
		
		if(timer == 320)
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			vars[4] = angleToPlayer - 180;
		}
		
		if(timer > 320)
		{
			if(timer % 5 == 0)
			{
				mainLevel.soundPlayer.play("sound/Grapling.wav");
			}
			BulletSprite bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
			bullet.setVelocity(3.0,vars[4]);
			enlistBullet(bullet, bullets);
			if(difficulty == "Easy" || difficulty == "Lunatic")
				vars[4] += 20;
			else
				vars[4] += 10;
			
			if(difficulty == "Hard")
			{
				bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
				bullet.setVelocity(5.0,vars[4]+5);
				enlistBullet(bullet, bullets);
			}
			else if(difficulty == "Lunatic")
			{
				bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
				bullet.setVelocity(5.0,vars[4]+2);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
				bullet.setVelocity(5.0,vars[4]-2);
				enlistBullet(bullet, bullets);
			}
			
			bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
			bullet.setVelocity(3.0,vars[4]);
			enlistBullet(bullet, bullets);
			if(difficulty == "Easy" || difficulty == "Lunatic")
				vars[4] += 20;
			else
				vars[4] += 10;
				
			if(difficulty == "Hard")
			{
				bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
				bullet.setVelocity(5.0,vars[4]+5);
				enlistBullet(bullet, bullets);
			}
			else if(difficulty == "Lunatic")
			{
				bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
				bullet.setVelocity(5.0,vars[4]+2);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'m',new Color(0x0000FF));
				bullet.setVelocity(5.0,vars[4]-2);
				enlistBullet(bullet, bullets);
			}
		}
			
		if(timer >= 360)
			timer = 99;
		
		
		unEnlistDeadBullets();
		
		// Citrus movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		x += vars[0];
		vars[2] += vars[1];
		
		vars[0] *= 0.95;
		vars[1] *= 0.95;
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
		
		if(x < 0)
			vars[0] = 5;
		if(x > mainLevel.SCREENW)
			vars[0] = -5;
		if(y < 0)
			vars[1] = 5;
		if(y > 200)
			vars[1] = -5;
	}
	
	
	// notAFreezeRayItsAnIceBeam
	
	private void notAFreezeRayItsAnIceBeam(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			if(difficulty == "Easy" || difficulty == "Normal")
				spellCardName = "Cryo-Tech ~ Not a Freeze Ray, It's an Ice Beam";
			else
				spellCardName = "Omega-Tech ~ Perfect Freeze Ray (It's an Ice Beam)";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (80 - x)/10.0;
			vars[2] += (80 - vars[2])/10.0;
		}
		
		
		
		if(timer == 100)
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			mainLevel.soundPlayer.play("sound/HITSNTH4.WAV");
			
			if(difficulty == "Easy" || difficulty == "Normal")
			{
				BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(0x00FFFF));
				bullet.aiMode = -1;
				bullet.theta = angleToPlayer;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer);
				enlistBullet(bullet, bullets);
			}
			else
			{
				BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(0x00FFFF));
				bullet.aiMode = -1;
				bullet.theta = angleToPlayer-45;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer-45);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'l', new Color(0x00FFFF));
				bullet.aiMode = -1;
				bullet.theta = angleToPlayer+45;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer+45);
				enlistBullet(bullet, bullets);
			}
		}
		
		
		int waveTiming = 19;
		double waveOffset = 0;
		if(difficulty == "Hard")
			waveTiming = 11;
		if(difficulty == "Lunatic")
		{
			vars[4] += 3;
			waveTiming = 4;
			waveOffset = 10*GameMath.cos(vars[4]);
		}
			
		
			
		if(difficulty != "Easy" && timer >100 && timer < 160 && timer % waveTiming == 0)
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer - 90 + waveOffset; i < angleToPlayer + 90 + waveOffset; i+= 20)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'd', new Color(0x77AAFF));
				bullet.setVelocity(2.0,i);
				enlistBullet(bullet, bullets);
			}
		}
		
		if(timer >= 160 && timer < 300 &&  x < mainLevel.SCREENW - 80)
			vars[0] = 2.0;
			
		if(timer == 300)
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			mainLevel.soundPlayer.play("sound/HITSNTH4.WAV");
			
			if(difficulty == "Easy" || difficulty == "Normal")
			{
				BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(0x00FFFF));
				bullet.aiMode = -1;
				bullet.theta = angleToPlayer;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer);
				enlistBullet(bullet, bullets);
			}
			else
			{
				BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(0x00FFFF));
				bullet.aiMode = -1;
				bullet.theta = angleToPlayer-45;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer-45);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'l', new Color(0x00FFFF));
				bullet.aiMode = -1;
				bullet.theta = angleToPlayer+45;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer+45);
				enlistBullet(bullet, bullets);
			}
		}
		
		if(difficulty != "Easy" && timer >300 && timer < 360 && timer % waveTiming == 0)
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer - 90 + waveOffset; i < angleToPlayer + 90 + waveOffset; i+= 20)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'd', new Color(0x77AAFF));
				bullet.setVelocity(2.0,i);
				enlistBullet(bullet, bullets);
			}
		}
		
		if(timer >= 360 && timer < 500 && x > 80)
			vars[0] = -2.0;
		
		if(timer > 500)
			timer = 99;
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.x = this.x + 32*GameMath.cos(bullet.getRotate());
				bullet.y = this.y - 32*GameMath.sin(bullet.getRotate());
				
				bullet.vars[0]++;
				if(bullet.vars[0] == 61)
					mainLevel.soundPlayer.play("sound/Laser 5.wav");
				if(bullet.vars[0] > 60)
				{
					bullet.isActive = true;
					
					int timing = 2;
					if(difficulty == "Easy")
						timing = 3;
					if(difficulty == "Hard")
						timing = 3;
					
					if(bullet.vars[0] % timing == 0)
					{
						Point vector = new Point((int) (300*GameMath.cos(bullet.theta)), (int) (300*GameMath.sin(bullet.theta)));
						
						double randDouble = mainLevel.rand.nextDouble();
						
						BulletSprite ice = new BulletSprite(bullet.x + randDouble*vector.x, bullet.y - randDouble*vector.y, 'm', new Color(0xAAFFFF));
						ice.scale(2.0,2.0);
						ice.aiMode = -2;
						ice.theta = mainLevel.rand.nextInt(360);
						ice.dx = 2*GameMath.cos(ice.theta);
						ice.dy = -2*GameMath.sin(ice.theta);
						ice.rotate(ice.theta);
						enlistBullet(ice, bullets);
					}
				}
				if(bullet.vars[0] > 200)
					bullet.isActive = false;
				if(bullet.vars[0] > 210)
					bullet.destroy();
			}
			
			if(bullet.aiMode == -2)
			{
				bullet.vars[0]++;
				bullet.screenKillable = true;
				
			//	if(bullet.vars[0] == 61)
			//		mainLevel.soundPlayer.play("sound/Glass smash 1.wav");
				if(bullet.vars[0] > 60)
				{
					bullet.x += bullet.vars[1]*bullet.dx;
					bullet.y += bullet.vars[1]*bullet.dy;
					
					bullet.vars[1] += 0.03;
					if(bullet.vars[1] > 1.0)
						bullet.vars[1] = 1.0;
				}
			}
			
			
		}
		
		
		unEnlistDeadBullets();
		
		// Citrus movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		x += vars[0];
		vars[2] += vars[1];
		
		vars[0] *= 0.8;
		vars[1] *= 0.8;
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
		
		if(x < 0)
			vars[0] = 5;
		if(x > mainLevel.SCREENW)
			vars[0] = -5;
		if(y < 0)
			vars[1] = 5;
		if(y > 200)
			vars[1] = -5;
	}
	
	
	// nsc2 (icicle fall)
	
	private void nsc2(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			vars[4] = 0;
			vars[5] = 0;
			
			
		//	fogTargetRadius = 200;
		//	fogOn = true;
			
			spellCardName = "";
		}
		
		
		if(timer < 100)
		{
			x += (mainLevel.SCREENW/2 - x)/10.0;
			vars[2] += (60 - vars[2])/10.0;
		}
		
		
		if(timer >= 100 && timer < 350 && timer % 30 == 0)
		{
			mainLevel.soundPlayer.play("sound/Grapling.wav");
			for(int i = 1; i < 4; i++)
			{
				BulletSprite bullet = new BulletSprite(x,y,'m',new Color(0x7777FF));
				bullet.setVelocity(1.8*i,20-vars[4]);
				bullet.aiMode = -1;
				bullet.vars[0] = -1;
				bullet.screenKillable = false;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'m',new Color(0x7777FF));
				bullet.setVelocity(1.8*i,160+vars[4]);
				bullet.aiMode = -1;
				bullet.vars[0] = 1;
				bullet.screenKillable = false;
				enlistBullet(bullet, bullets);
				
				if(difficulty == "Hard" || difficulty == "Lunatic")
				{
					bullet = new BulletSprite(x,y,'m',new Color(0x7777FF));
					bullet.setVelocity(2*i,20-vars[4]);
					bullet.aiMode = -1;
					bullet.vars[0] = -1;
					bullet.screenKillable = false;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y,'m',new Color(0x7777FF));
					bullet.setVelocity(2*i,160+vars[4]);
					bullet.aiMode = -1;
					bullet.vars[0] = 1;
					bullet.screenKillable = false;
					enlistBullet(bullet, bullets);
				}
			}
				
			vars[4] += 8;
		}
		
		if(timer >= 100 && timer < 351 && timer % 50 == 0 && difficulty != "Easy")
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
			
			int waveSpacing = 20;
			if(difficulty == "Lunatic")
				waveSpacing = 10;
			
			for(double i = angleToPlayer - 40; i <= angleToPlayer + 40; i+= waveSpacing)
			{
				BulletSprite bullet = new BulletSprite(x,y,'d',new Color(0xCCFF00));
				bullet.setVelocity(2.5,i);
				enlistBullet(bullet, bullets);
			}
		}
		
		
		
		
		if(timer == 480)
		{
			double a = x - mainLevel.player.x;
			double b = y - mainLevel.player.y;
			double distToPlayer = Math.sqrt(a*a + b*b);
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			if(difficulty == "Normal")
			{
				leftClaw.vars[2] = angleToPlayer-45;
				leftClaw.vars[4] = 1;
			}
			else
			{
				leftClaw.vars[2] = angleToPlayer - 90;
				leftClaw.vars[4] = 2;
			}
			leftClaw.vars[3] = distToPlayer;
			leftClaw.aiType = 1;
		}
		if((timer >= 480 && timer < 520) || (timer >= 560 && timer < 610))
		{
			int waveTimer = 19;
			int waveSpacing = 15;
			
			if(difficulty == "Easy")
				waveTimer = 30;
			if(difficulty == "Hard")
				waveSpacing = 10;
			if(difficulty == "Lunatic")
				waveSpacing = 5;
			
			if(timer% waveTimer == 0)
			{
				mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
				
				double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
				for(double i = angleToPlayer; i < angleToPlayer + 360; i += waveSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y,'d',new Color(0xCCFF00));
					bullet.setVelocity(2.0,i);
					enlistBullet(bullet, bullets);
				}
			}
		}
		
		if(timer >= 400 && leftClaw.aiType == 1)
		{
			if(difficulty != "Easy")
			{
				leftClaw.vars[0] = x + leftClaw.vars[3]*GameMath.cos(leftClaw.vars[2]);
				leftClaw.vars[1] = y - leftClaw.vars[3]*GameMath.sin(leftClaw.vars[2]);
			}
			
			leftClaw.vars[2] += leftClaw.vars[4];
		}
		
		
		
		if(timer == 560)
		{
			double a = x - mainLevel.player.x;
			double b = y - mainLevel.player.y;
			double distToPlayer = Math.sqrt(a*a + b*b);
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			if(difficulty == "Normal")
			{
				rightClaw.vars[2] = angleToPlayer+45;
				rightClaw.vars[4] = 1;
			}
			else
			{
				rightClaw.vars[2] = angleToPlayer + 90;
				rightClaw.vars[4] = 2;
			}
			rightClaw.vars[3] = distToPlayer;
			rightClaw.aiType = 1;
		}
		
		
		if(timer >= 400 && rightClaw.aiType == 1)
		{
			if(difficulty != "Easy")
			{
				rightClaw.vars[0] = x + rightClaw.vars[3]*GameMath.cos(rightClaw.vars[2]);
				rightClaw.vars[1] = y - rightClaw.vars[3]*GameMath.sin(rightClaw.vars[2]);
			}
			
			rightClaw.vars[2] -= rightClaw.vars[4];
		}
		
			
		if(timer >= 620)
		{
			timer = 99;
			vars[4] = 0;
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1 )
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				if(bullet.vars[1] > 30 && bullet.vars[0] != 0)
				{
					bullet.dx *= 0.95;
					bullet.dy *= 0.95;
				}
				if(bullet.vars[1] > 60 && bullet.vars[0] != 0)
				{
					bullet.setVelocity(1.7, bullet.theta+90*bullet.vars[0]);
					bullet.vars[0] = 0;
				}
				bullet.vars[1]++;
				
				if(bullet.y > mainLevel.SCREENH+10)
					bullet.destroy();
			}
			
			
			
			
		}
		
		
		
		unEnlistDeadBullets();
		
		// Citrus movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		x += vars[0];
		vars[2] += vars[1];
		
		vars[0] *= 0.95;
		vars[1] *= 0.95;
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
		
		if(x < 0)
			vars[0] = 5;
		if(x > mainLevel.SCREENW)
			vars[0] = -5;
		if(y < 0)
			vars[1] = 5;
		if(y > 200)
			vars[1] = -5;
	}
	
	// Perfect Math Class ~ Fractal Snowflake
	
	private void fractalSnowflake(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 800;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			leftClaw.vars[2] = 0;
			leftClaw.vars[3] = 0;
			rightClaw.vars[2] = 180;
			rightClaw.vars[3] = 0;

			if(difficulty == "Easy" || difficulty == "Normal")
				spellCardName = "Natural Math ~ Shivering Fractal";
			else
				spellCardName = "Perfect Math Class ~ Fractal Snowflake";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		
		double armRotateSpeed = 0.5;
		int fractalDepth = ((int) timer - 200) / 100;
		int maxFractalDepth = 3;
		int fractalSpeed = 2;
		int fractalAngle = 90;
		int fractalLength = 80;
		
		if(difficulty == "Easy")
		{
			fractalSpeed = 4;
			maxFractalDepth = 2;
			fractalAngle = 60;
			armRotateSpeed = 0.4;
		}
		if(difficulty == "Normal")
		{
			fractalSpeed = 6;
		//	maxFractalDepth = 3;
			fractalAngle = 60;
			fractalLength = 40;
		}
		if(difficulty == "Hard")
		{
			fractalSpeed = 5;
			maxFractalDepth = 2;
			fractalAngle = 90;
			fractalLength = 30;
		}
		if(difficulty == "Lunatic")
		{
			fractalSpeed = 8;
			maxFractalDepth = 3;
			fractalAngle = 60;
			fractalLength = 20;
		}
		
		
		if(fractalDepth > maxFractalDepth)
			fractalDepth = maxFractalDepth;
		
		if(timer < 100)
		{
			x += (mainLevel.SCREENW/2 - x)/10.0;
			vars[2] += (mainLevel.SCREENH/2 - vars[2])/10.0;
		}
		
		if(timer == 100)
		{
			EnemySprite grenadeSpawner = new EnemySprite(x,y,'e',9999);
			grenadeSpawner.vars[0] = leftClaw.vars[2] + 90;
			enlistBullet(grenadeSpawner, slaves);
			
			grenadeSpawner = new EnemySprite(x,y,'e',9999);
			grenadeSpawner.vars[0] = leftClaw.vars[2] - 90;
			enlistBullet(grenadeSpawner, slaves);
			
			if(difficulty == "Hard" || difficulty == "Lunatic")
			{
				grenadeSpawner = new EnemySprite(x,y,'e',9999);
				grenadeSpawner.vars[0] = leftClaw.vars[2] + 45;
				enlistBullet(grenadeSpawner, slaves);
				
				grenadeSpawner = new EnemySprite(x,y,'e',9999);
				grenadeSpawner.vars[0] = leftClaw.vars[2] - 45;
				enlistBullet(grenadeSpawner, slaves);
				
				grenadeSpawner = new EnemySprite(x,y,'e',9999);
				grenadeSpawner.vars[0] = rightClaw.vars[2] + 45;
				enlistBullet(grenadeSpawner, slaves);
				
				grenadeSpawner = new EnemySprite(x,y,'e',9999);
				grenadeSpawner.vars[0] = rightClaw.vars[2] - 45;
				enlistBullet(grenadeSpawner, slaves);
			}
		}
		
		if(timer >= 100)
		{
			if(timer % 100 == 0)
				mainLevel.soundPlayer.play("sound/Craft land.wav");
			leftClaw.aiType = -1;
			leftClaw.targetX = this.x + leftClaw.vars[3]*GameMath.cos(leftClaw.vars[2]);
			leftClaw.targetY = this.y - leftClaw.vars[3]*GameMath.sin(leftClaw.vars[2]);
			
			rightClaw.aiType = -1;
			rightClaw.targetX = this.x + rightClaw.vars[3]*GameMath.cos(rightClaw.vars[2]);
			rightClaw.targetY = this.y - rightClaw.vars[3]*GameMath.sin(rightClaw.vars[2]);
			
			if(leftClaw.vars[3] < mainLevel.SCREENW/2 - 10)
			{
				leftClaw.vars[3]++;
				rightClaw.vars[3]++;
			}
			
			leftClaw.vars[2]+= armRotateSpeed;
			rightClaw.vars[2]+= armRotateSpeed;
			
			for(Object s : slaves)
			{
				Sprite spawner = (Sprite) s;
				spawner.vars[0] += armRotateSpeed;
				
				spawner.x = this.x + leftClaw.vars[3]*GameMath.cos(spawner.vars[0]);
				spawner.y = this.y - leftClaw.vars[3]*GameMath.sin(spawner.vars[0]);
				
				if(leftClaw.vars[3] == mainLevel.SCREENW/2 - 10 && timer % 5 == 0)
				{
					double angleToBoss = GameMath.getAngleTo(spawner.x, spawner.y, this.x, this.y);
				
					BulletSprite bullet = new BulletSprite(spawner.x,spawner.y, 'd', new Color(0xAAAAFF));
					bullet.setVelocity(fractalSpeed, angleToBoss);
					bullet.aiMode = -2;
					bullet.vars[1] = fractalLength;
					bullet.vars[2] = fractalAngle;
					bullet.vars[3] = fractalDepth;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(spawner.x,spawner.y, 'm', new Color(0x6666CC));
					bullet.setVelocity(3, spawner.vars[0]);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(spawner.x,spawner.y, 'd', new Color(0x6666CC));
					bullet.setVelocity(1, spawner.vars[0]);
					bullet.aiMode = -1;
					bullet.vars[0] = 0;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(spawner.x,spawner.y, 'a', new Color(0x6666CC));
					bullet.setVelocity(2, spawner.vars[0]-60);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(spawner.x,spawner.y, 'a', new Color(0x6666CC));
					bullet.setVelocity(2, spawner.vars[0]+60);
					enlistBullet(bullet, bullets);
					
				}
				/*
				if(leftClaw.vars[3] == mainLevel.SCREENW/2 - 10 && timer % 60 == 0 && difficulty == "Lunatic")
				{
					double angleToPlayer = GameMath.getAngleTo(spawner.x, spawner.y, mainLevel.player.x,  mainLevel.player.y);
					
					BulletSprite bullet = new BulletSprite(spawner.x,spawner.y, 'd', new Color(0x00FFFF));
					bullet.setVelocity(2, angleToPlayer);
					enlistBullet(bullet, bullets);
				}*/
			}
		
		}
		
		
		if(leftClaw.vars[3] == mainLevel.SCREENW/2 - 10 && timer % 5 == 0)
		{
			// leftClaw spray
		
			double angleToBoss = GameMath.getAngleTo(leftClaw.x, leftClaw.y, this.x, this.y);
			
			BulletSprite bullet;
		
			bullet = new BulletSprite(leftClaw.x,leftClaw.y, 'd', new Color(0xAAAAFF));
			bullet.setVelocity(fractalSpeed, angleToBoss);
			bullet.aiMode = -2;
			bullet.vars[1] = fractalLength;
			bullet.vars[2] = fractalAngle;
			bullet.vars[3] = fractalDepth;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(leftClaw.x,leftClaw.y, 'm', new Color(0x6666CC));
			bullet.setVelocity(3, leftClaw.vars[2]);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(leftClaw.x,leftClaw.y, 'd', new Color(0x6666CC));
			bullet.setVelocity(1, leftClaw.vars[2]);
			bullet.aiMode = -1;
			bullet.vars[0] = 0;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(leftClaw.x,leftClaw.y, 'a', new Color(0x6666CC));
			bullet.setVelocity(2, leftClaw.vars[2]+60);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(leftClaw.x,leftClaw.y, 'a', new Color(0x6666CC));
			bullet.setVelocity(2, leftClaw.vars[2]-60);
			enlistBullet(bullet, bullets);
			
			// rightClaw spray
			
			angleToBoss = GameMath.getAngleTo(rightClaw.x, rightClaw.y, this.x, this.y);
			
			bullet = new BulletSprite(rightClaw.x,rightClaw.y, 'd', new Color(0xAAAAFF));
			bullet.setVelocity(fractalSpeed, angleToBoss);
			bullet.aiMode = -2;
			bullet.vars[1] = fractalLength;
			bullet.vars[2] = fractalAngle;
			bullet.vars[3] = fractalDepth;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(rightClaw.x,rightClaw.y, 'm', new Color(0x6666CC));
			bullet.setVelocity(3, rightClaw.vars[2]);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(rightClaw.x,rightClaw.y, 'd', new Color(0x6666CC));
			bullet.setVelocity(1, rightClaw.vars[2]);
			bullet.aiMode = -1;
			bullet.vars[0] = 0;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(rightClaw.x,rightClaw.y, 'a', new Color(0x6666CC));
			bullet.setVelocity(2, rightClaw.vars[2]-60);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(rightClaw.x,rightClaw.y, 'a', new Color(0x6666CC));
			bullet.setVelocity(2, rightClaw.vars[2]+60);
			enlistBullet(bullet, bullets);
			
			
			
			
		}
		
		if(leftClaw.vars[3] == mainLevel.SCREENW/2 - 10 && timer % 60 == 0 && difficulty == "Normal")
		{
			double angleToPlayer = GameMath.getAngleTo(leftClaw.x, leftClaw.y, mainLevel.player.x,  mainLevel.player.y);
			mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
			
			BulletSprite bullet = new BulletSprite(leftClaw.x,leftClaw.y, 'd', new Color(0x00FFFF));
			bullet.setVelocity(0.5, angleToPlayer);
			enlistBullet(bullet, bullets);
			
			angleToPlayer = GameMath.getAngleTo(rightClaw.x, rightClaw.y, mainLevel.player.x,  mainLevel.player.y);
			
			bullet = new BulletSprite(rightClaw.x,rightClaw.y, 'd', new Color(0x00FFFF));
			bullet.setVelocity(0.5, angleToPlayer);
			enlistBullet(bullet, bullets);
		}
		
		if(leftClaw.vars[3] == mainLevel.SCREENW/2 - 10 && timer % 50 == 0 && difficulty == "Hard")
		{
			BulletSprite bullet;
			mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x,  mainLevel.player.y);
			
			for(double i = leftClaw.vars[2] ; i < leftClaw.vars[2] + 360; i += 60)
			{
				bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x00FFFF));
				bullet.setRotationalVelocity(2.0, angleToPlayer+i, 10.0, 0.98);
				bullet.aiMode = 2;
				enlistBullet(bullet, bullets);
			}
		}
		
		if(leftClaw.vars[3] == mainLevel.SCREENW/2 - 10 && timer % 30 == 0 && difficulty == "Lunatic")
		{
			BulletSprite bullet;
			mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x,  mainLevel.player.y);
			
			for(double i = leftClaw.vars[2] ; i < leftClaw.vars[2] + 360; i += 60)
			{
				bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x00FFFF));
				bullet.setRotationalVelocity(2.0, angleToPlayer+i, 10.0, 0.98);
				bullet.aiMode = 2;
				enlistBullet(bullet, bullets);
			}
		}
		

		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.x += bullet.dx*bullet.vars[0];
				bullet.y += bullet.dy*bullet.vars[0];
				bullet.vars[0]+= 0.01;
				bullet.screenKillable = true;
			}
			if(bullet.aiMode == -2)
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				if((bullet.vars[0] == ((int)bullet.vars[1])/2 && bullet.vars[3] > 0) || (difficulty == "Hard" && bullet.vars[3] == fractalDepth && bullet.vars[3] > 0))
				{
					char bulletType = 'd';
					if(bullet.vars[3] <= 2)
						bulletType = 'c';
					if(bullet.vars[3] <= 1)
						bulletType = 'm';
						
					BulletSprite fractalBullet = new BulletSprite(bullet.x, bullet.y, bulletType, new Color(0x6666CC));
					fractalBullet.setVelocity(fractalSpeed, bullet.theta + bullet.vars[2]);
					fractalBullet.aiMode = -2;
					fractalBullet.vars[1] = bullet.vars[1]/2;
					fractalBullet.vars[2] = bullet.vars[2];
					fractalBullet.vars[3] = bullet.vars[3]-1;
					enlistBullet(fractalBullet, bullets);
					
					fractalBullet = new BulletSprite(bullet.x, bullet.y, bulletType, new Color(0x6666CC));
					fractalBullet.setVelocity(fractalSpeed, bullet.theta);
					fractalBullet.aiMode = -2;
					fractalBullet.vars[1] = ((int) bullet.vars[1])/2;
					fractalBullet.vars[2] = bullet.vars[2];
					fractalBullet.vars[3] = bullet.vars[3]-1;
					enlistBullet(fractalBullet, bullets);
					
					fractalBullet = new BulletSprite(bullet.x, bullet.y, bulletType, new Color(0x6666CC));
					fractalBullet.setVelocity(fractalSpeed, bullet.theta - bullet.vars[2]);
					fractalBullet.aiMode = -2;
					fractalBullet.vars[1] = ((int) bullet.vars[1])/2;
					fractalBullet.vars[2] = bullet.vars[2];
					fractalBullet.vars[3] = bullet.vars[3]-1;
					enlistBullet(fractalBullet, bullets);
					
					bullet.destroy();
				}
				if(bullet.vars[0] == ((int)bullet.vars[1])/2)
					bullet.destroy();
				
				bullet.vars[0]++;
			}
			
			
		}
		
		
		unEnlistDeadBullets();
		
		// Citrus movement
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
	}
	
	
	
	// NSC3
	
	private void nsc3(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 99999;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			timer += 400;
			
			spellCardName = "";
		}
		
		leftClaw.aiType = 0;
		rightClaw.aiType = 0;
		
		if(timer % 30 == 0)
		{
			if(leftArm.size() > 0)
			{
				KyuArmSprite leftArmPart = leftArm.remove(0);
				FXSprite bulletDie = FXSprite.makeSimpleOrb1(leftArmPart.x,leftArmPart.y);
				sfx.add(bulletDie);
				leftArmPart.destroy();
				
				KyuArmSprite rightArmPart = rightArm.remove(0);
				bulletDie = FXSprite.makeSimpleOrb1(rightArmPart.x,rightArmPart.y);
				sfx.add(bulletDie);
				leftArmPart.destroy();
				
				fogOn = true;
				fogTargetRadius -= 30;
				
				mainLevel.soundPlayer.play("sound/EXPLOD05.WAV");
			}
			else if(!leftClaw.isDestroyed)
			{
				FXSprite bulletDie = FXSprite.makeSimpleOrb1(leftClaw.x,leftClaw.y);
				sfx.add(bulletDie);
				leftClaw.destroy();
				
				bulletDie = FXSprite.makeSimpleOrb1(rightClaw.x,rightClaw.y);
				sfx.add(bulletDie);
				rightClaw.destroy();
				
				mainLevel.soundPlayer.play("sound/EXPLOD05.WAV");
				
				fogTargetRadius = 150;
			}
			else
			{
				mainLevel.numberSCTimer.value = 0;
			}
		}
		
		
		unEnlistDeadBullets();
		
		// Citrus movement
		y = vars[2] + 2*GameMath.sin(vars[3]);
	}
	
	// Cryo-fog / Blizzard Mist
	
	private void cryoFog(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 800;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			leftClaw.vars[2] = 0;
			leftClaw.vars[3] = 0;
			rightClaw.vars[2] = 180;
			rightClaw.vars[3] = 0;

			
			spellCardName = "Cold Contingency ~ Blizzard Mist";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (mainLevel.SCREENW/2 - x)/40.0;
			vars[2] += (60 - vars[2])/40.0;
		}
		
		if(difficulty == "Hard" || difficulty == "Lunatic")
			fogTargetRadius = 80;
		
		
		if(timer >= 100)
		{
			if(timer % 10 == 0)
				mainLevel.soundPlayer.play("sound/Grapling.wav");
			if(timer % 6 == 0)
			{
				
				BulletSprite bullet = new BulletSprite(x,y,'e',new Color(0xCCCCFF));
				bullet.setVelocity(1.5,vars[4]);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'e',new Color(0xCCCCFF));
				bullet.setVelocity(1.5,vars[4]+120);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'e',new Color(0xCCCCFF));
				bullet.setVelocity(1.5,vars[4]+240);
				enlistBullet(bullet, bullets);
				
				
				bullet = new BulletSprite(x,y,'a',new Color(0xCCCCFF));
				bullet.setVelocity(0.8,vars[4]+2);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'a',new Color(0xCCCCFF));
				bullet.setVelocity(0.8,vars[4]+122);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y,'a',new Color(0xCCCCFF));
				bullet.setVelocity(0.8,vars[4]+242);
				enlistBullet(bullet, bullets);
				
				
				vars[4]+=7;
			}
			
			
			int shardTimer = 40;
			if(difficulty == "Lunatic")
				shardTimer = 20;
				
			if(difficulty != "Easy" && timer % shardTimer == 0)
			{
				double bulletAngle = mainLevel.rand.nextDouble()*180;
				
				BulletSprite bullet = new BulletSprite(mainLevel.player.x + (fogRadius+10)*GameMath.cos(bulletAngle),
																	mainLevel.player.y - (fogRadius+10)*GameMath.sin(bulletAngle),'m',new Color(0x00CCCC));
				bullet.setVelocity(0.65,bulletAngle-180);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(mainLevel.player.x + (fogRadius+10)*GameMath.cos(bulletAngle),
																	mainLevel.player.y - (fogRadius+10)*GameMath.sin(bulletAngle),'m',new Color(0x00CCCC));
				bullet.setVelocity(0.65,bulletAngle+30);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(mainLevel.player.x + (fogRadius+10)*GameMath.cos(bulletAngle),
																	mainLevel.player.y - (fogRadius+10)*GameMath.sin(bulletAngle),'m',new Color(0x00CCCC));
				bullet.setVelocity(0.65,bulletAngle-30);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(mainLevel.player.x + (fogRadius+10)*GameMath.cos(bulletAngle),
																	mainLevel.player.y - (fogRadius+10)*GameMath.sin(bulletAngle),'m',new Color(0x00CCCC));
				bullet.setVelocity(0.65,bulletAngle+100);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(mainLevel.player.x + (fogRadius+10)*GameMath.cos(bulletAngle),
																	mainLevel.player.y - (fogRadius+10)*GameMath.sin(bulletAngle),'m',new Color(0x00CCCC));
				bullet.setVelocity(0.65,bulletAngle-100);
				enlistBullet(bullet, bullets);
			}
			
			if(timer % 100 == 0)
			{
				vars[0] = mainLevel.rand.nextDouble()*6 - 3;
				vars[1] = mainLevel.rand.nextDouble()*2 - 1;
			}
		}
		
		unEnlistDeadBullets();
		
		// Citrus movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		x += vars[0];
		vars[2] += vars[1];
		
		vars[0] *= 0.95;
		vars[1] *= 0.95;
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
		
		if(x < 0)
			vars[0] = 5;
		if(x > mainLevel.SCREENW)
			vars[0] = -5;
		if(y < 0)
			vars[1] = 5;
		if(y > 200)
			vars[1] = -5;// Citrus movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		x += vars[0];
		vars[2] += vars[1];
		
		vars[0] *= 0.95;
		vars[1] *= 0.95;
		
		vars[3]+= 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		y = vars[2] + 2*GameMath.sin(vars[3]);
		
		if(x < 0)
			vars[0] = 5;
		if(x > mainLevel.SCREENW)
			vars[0] = -5;
		if(y < 0)
			vars[1] = 5;
		if(y > 200)
			vars[1] = -5;
	}	
	
}
