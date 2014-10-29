
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



public class BossCitrus extends BossSprite
{
	private static int numInstances = 0;
	int soundSpacer = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	
	// enemy vars
	
	// CONSTRUCTOR
	
	public BossCitrus(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(13);
		this.lastAttackIndex = 5;
		portrait = new PortraitCitrusSprite(m.SCREENW +100,300,"Citrus,~Lord of Fruit");
		portrait.vars[0] = m.SCREENW +100;
		portrait.vars[1] = 100;
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
		
		PortraitCitrusSprite.clean();
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
		System.out.println("Loading BossCitrus");
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		Image img;
		
		// load our images
		
		imageURL = BossCitrus.class.getResource("graphics/CitrusSheet.png");
		Image citrusSheet = tk.getImage(imageURL);
		citrusSheet = ColorFilters.setTransparentColor(citrusSheet, new Color(0xFF00FF));
		
		// Forward
		
		img = ImageBlitter.crop(citrusSheet,1,1,33,35);
		il.addImage(img);
		focusTable.put("CitrusS1",new Point(16,21));
		imageTable.put("CitrusS1",img);
		
		img = ImageBlitter.crop(citrusSheet,36,1,33,35);
		il.addImage(img);
		focusTable.put("CitrusS2",new Point(16,21));
		imageTable.put("CitrusS2",img);
		
		// Right 
		
		img = ImageBlitter.crop(citrusSheet,1,38,33,35);
		il.addImage(img);
		focusTable.put("CitrusE1",new Point(16,21));
		imageTable.put("CitrusE1",img);
		
		img = ImageBlitter.crop(citrusSheet,36,38,33,35);
		il.addImage(img);
		focusTable.put("CitrusE2",new Point(16,21));
		imageTable.put("CitrusE2",img);
		
		// Left
		
		img = ImageBlitter.crop(citrusSheet,1,75,33,35);
		il.addImage(img);
		focusTable.put("CitrusW1",new Point(16,21));
		imageTable.put("CitrusW1",img);
		
		img = ImageBlitter.crop(citrusSheet,36,75,33,35);
		il.addImage(img);
		focusTable.put("CitrusW2",new Point(16,21));
		imageTable.put("CitrusW2",img);
		
		System.out.println("loaded image data for BossCitrus");
		
		PortraitCitrusSprite.loadImages(il);
		
		
	}
	
	// LOGIC METHODS
	
	
	
	
	
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
		
		// Animate current frame
		
		String imgKey = "Citrus" + this.direction;
		int imgKeyNum = 1;
		if(frameNum >= 10)
			imgKeyNum = 2;
		
		imgKey += imgKeyNum + "";
		
		
		curImage = imageTable.get(imgKey);
		
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
		
		width = 33;
		height = 35;

		this.frameNum++;
		if(this.frameNum >= 100)
			this.frameNum = 0;
		
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
		if(attackIndex == 1) // Juice Sign ~ Citric Acid Bath (HL)
		{
			stageBG.nextBGID = 2;
			if(difficulty.equals("Easy"))
			{
				attackIndex++;
				return;
			}
			else if(difficulty.equals("Normal"))
			{
				attackIndex++;
				return;
			}
			else if(difficulty.equals("Hard"))
				lemonRave(bullets,slaves,sfx);
			else
				lemonRave(bullets,slaves,sfx);
		}
		if(attackIndex == 2) // nonSpellcard 2
		{
			nsc2(bullets,slaves,sfx);
		}
		if(attackIndex == 3) // Fruit Sign - Assassin Grape Vine (EN) // Vine Control ~ Assassin Vineyard (HL)
		{
			stageBG.nextBGID = 1;
			
			if(difficulty.equals("Easy"))
				assassinGrapeVineE(bullets, slaves, sfx);
			else if(difficulty.equals("Normal"))
				assassinGrapeVine(bullets, slaves, sfx);
			else if(difficulty.equals("Hard"))
				assassinVineyardH(bullets, slaves, sfx);
			else
				assassinVineyardL(bullets, slaves, sfx);
		}
		if(attackIndex == 4) // nonSpellcard 3
		{
			nsc3(bullets, slaves, sfx);
			stageBG.nextBGID = 0;
		}
		if(attackIndex == 5) // Berry Abjuration ~ Demarcation Berrier (EN) // Edible Abjuration ~ Vengeful Apples (More Like Large Cherries) (HL)
		{
			stageBG.nextBGID = 3;
			
			if(difficulty.equals("Easy"))
				berryBarrier(bullets, slaves, sfx);
			else if(difficulty.equals("Normal"))
				berryBarrier(bullets, slaves, sfx);
			else if(difficulty.equals("Hard"))
				vengefulApplesHL(bullets, slaves, sfx);
			else
				vengefulApplesHL(bullets, slaves, sfx);
		}
		if(attackIndex == 7) // Last Spell: Cornucopia Art ~ Deliciously Deadly Bouquet (NHL) // Edible Abjuration ~ Vengeful Apples (More Like Large Cherries) (HL)
		{
			if(difficulty.equals("Easy"))
			{
				attackIndex++;
				return;
			}
			else if(difficulty.equals("Normal"))
				deliciouslyDeadlyBouquet(bullets, slaves, sfx);
			else if(difficulty.equals("Hard"))
				deliciouslyDeadlyBouquet(bullets, slaves, sfx);
			else
				deliciouslyDeadlyBouquet(bullets, slaves, sfx);
		}
		
		// set health bar values and spell card text

		portrait.text = spellCardName;
		mainLevel.barBossHealth.caption2 = spellCardName;
		
		// increment timer
		
		timer++;
	}

	// NSC0 (used to test rotational bullets).
	
	private void nsc0(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 150;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "";
		}
		
		
		
		
		if((timer + 50) % 80 == 0) // create a burst of bullets
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=20)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(3, i+40, 5.0,0.98);
			//	bullet.setCircularVelocity(this.x, this.y, i, 0.0, 3.0, 1.0);
				enlistBullet(bullet, bullets);
				
			
			}
			
			// move Citrus in random direction.
			
			vars[0] = (mainLevel.rand.nextInt(2)-0.5)*2*3; 
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*2; 
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
	
	// NSC01 (used to test laser bullets).
	
	private void nsc01(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 150;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 999;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "";
		}
		
		
		
		
		if(timer == 150) // create laser
		{
			BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(0x5555FF));
		//	bullet.scale(1.2,1.0);
			bullet.aiMode = -1;
			bullet.isActive = false;
			bullet.rotate(-90);
		//	bullet.setRotationalVelocity(3, i+40, 5.0,0.98);
		//	bullet.setCircularVelocity(this.x, this.y, i, 0.0, 3.0, 1.0);
			enlistBullet(bullet, bullets);
		}
		
		
		
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.rotate(bullet.getRotate() + 1);
				bullet.x = this.x + 32*GameMath.cos(bullet.getRotate());
				bullet.y = this.y - 32*GameMath.sin(bullet.getRotate());
				
				if((timer - 270) % 300 == 0)
					bullet.isActive = true;
			//	if((timer - 149) % 300 == 0)
			//		bullet.isActive = false;
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
	
	// NSC02 (used to test ellipticial bullets).
	
	private void nsc02(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 150;
			dmgShield = 0.01;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 999;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "";
		}
		
		
		double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
		
		if(timer % 20 == 0) // create burst
		{
			BulletSprite bullet;
			
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=30)
			{
				bullet = new BulletSprite(x,y, 'd', new Color(0x5555FF));
				bullet.scale(4.0,2.0);
				bullet.aiMode = 0;
				bullet.setVelocity(2.0,i);
				enlistBullet(bullet, bullets);
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
	
	
	// NSC1
	
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
			
			spellCardName = "";
		}
		
		
		
		
		if((timer + 50) % 80 == 0) // create a burst of bullets
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
			
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=40)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(2.0, i);
				bullet.rotate(i);
				enlistBullet(bullet, bullets);
				
				if(!difficulty.equals("Easy"))
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(3.0, i+10);
					bullet.rotate(i+10);
					enlistBullet(bullet, bullets);
				}
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(1.0, i+5);
				bullet.rotate(i+5);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(4.0, i+20);
				bullet.rotate(i+20);
				enlistBullet(bullet, bullets);
				
				if(difficulty.equals("Hard"))
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(1.5, i, 1, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);

					bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(2.5, i, 1, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(0.5, i, 1, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(3.5, i, 1, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty.equals("Lunatic"))
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(1.5, i, 8, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);

					bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(2.5, i, 8, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(4.5, i, 8, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setRotationalVelocity(3.5, i, 8, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
			
			// move Citrus in random direction.
			
			vars[0] = (mainLevel.rand.nextInt(2)-0.5)*2*2; 
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*2; 
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
	
	// Citric Acid Bath (Unused)
	
	private void citricAcidBath(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 300;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "Dangerous Cocktail ~ Lemon Rave";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (mainLevel.SCREENW/2 - x)*0.05;
			y += (80 - y)*0.05;
			vars[2] = y;
		}
		else if((timer + 50) %60 == 0) // orange grenades
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);

			BulletSprite bullet = new BulletSprite(x,y, 'd', new Color(0xFF5500));
		//	bullet.scale(1.2,1.0);
			bullet.setVelocity(4.5, angleToPlayer-20);
			bullet.vars[1] = -1;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(x,y, 'd', new Color(0xFF5500));
		//	bullet.scale(1.2,1.0);
			bullet.setVelocity(5, angleToPlayer+20);
			bullet.vars[1] = -1;
			enlistBullet(bullet, bullets);

		}
		else if((timer + 50+30) %60 == 0) // acidic wedges
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			BulletSprite bullet;
			
			if(difficulty.equals("Hard"))
			{
				bullet = new BulletSprite(x,y, 'd', new Color(0xFF5500));
			//	bullet.scale(1.2,1.0);
				bullet.setVelocity(3.5, angleToPlayer);
				bullet.vars[1] = -1;
				enlistBullet(bullet, bullets);
			}
			
			// Acidic orbs
			if(difficulty.equals("Lunatic"))
			{
				int dTheta = 30;
			
				bullet = new BulletSprite(x,y, 'd', new Color(0x009900));
			//	bullet.scale(1.2,1.0);
				bullet.setVelocity(3, angleToPlayer-dTheta);
				enlistBullet(bullet, bullets);
				
				for(double i = 0.2; i < 3.0 ; i+= 0.4)
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0x009900));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(i, angleToPlayer-dTheta);
					enlistBullet(bullet, bullets);
				}
				
				bullet = new BulletSprite(x,y, 'd', new Color(0x009900));
			//	bullet.scale(1.2,1.0);
				bullet.setVelocity(3, angleToPlayer+dTheta);
				enlistBullet(bullet, bullets);
				
				for(double i = 0.2; i < 3.0 ; i+= 0.4)
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0x009900));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(i, angleToPlayer+dTheta);
					enlistBullet(bullet, bullets);
				}
				
				bullet = new BulletSprite(x,y, 'd', new Color(0x009900));
			//	bullet.scale(1.2,1.0);
				bullet.setVelocity(3, angleToPlayer+dTheta);
				enlistBullet(bullet, bullets);
				
				for(double i = 0.2; i < 3.0 ; i+= 0.4)
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0x009900));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(i, angleToPlayer);
					enlistBullet(bullet, bullets);
				}
			}
			

		}
		if((timer + 50) %120 == 0)
		{
			// move Citrus in random direction.
			if(this.x < mainLevel.player.x)
				vars[0] = mainLevel.rand.nextInt(3); 
			else
				vars[0] = mainLevel.rand.nextInt(3)*-1;
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*1.5; 
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
			
			if(bullet.vars[1] == -1)
			{
				if(bullet.vars[0] > 40)
				{
					int di = 40;
					if(difficulty.equals("Lunatic"))
						di = 30;
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i += di)
					{
						BulletSprite newbullet = new BulletSprite(bullet.x,bullet.y, 'c', new Color(0x00CC00));
						newbullet.scale(1.2,1.0);
						newbullet.setVelocity(2, i);
						newbullet.rotate(i);
						enlistBullet(newbullet, bullets);
					}
					bullet.destroy();
				}
				bullet.vars[0]++;
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
	
	// Lemon Rave
	
	private void lemonRave(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 300;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "Dangerous Cocktail ~ Lemon Rave";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (mainLevel.SCREENW/2 - x)*0.05;
			y += (80 - y)*0.05;
			vars[2] = y;
		}
		else
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			if((timer - 100) % 120 == 0)
			{
				mainLevel.soundPlayer.play("sound/HITSNTH4.WAV");
				BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(mainLevel.rand.nextInt(255),mainLevel.rand.nextInt(255),mainLevel.rand.nextInt(255)));
			//	bullet.scale(1.2,1.0);
				bullet.aiMode = -1;
				bullet.isActive = false;
				bullet.rotate(angleToPlayer);
				enlistBullet(bullet, bullets);
			}
			if(difficulty.equals("Lunatic"))
			{
				if((timer - 120) % 120 == 0)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(mainLevel.rand.nextInt(255),mainLevel.rand.nextInt(255),mainLevel.rand.nextInt(255)));
				//	bullet.scale(1.2,1.0);
					bullet.aiMode = -1;
					bullet.isActive = false;
					bullet.rotate(angleToPlayer+25);
					enlistBullet(bullet, bullets);
				}
				if((timer - 140) % 120 == 0)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'l', new Color(mainLevel.rand.nextInt(255),mainLevel.rand.nextInt(255),mainLevel.rand.nextInt(255)));
				//	bullet.scale(1.2,1.0);
					bullet.aiMode = -1;
					bullet.isActive = false;
					bullet.rotate(angleToPlayer-25);
					enlistBullet(bullet, bullets);
				}
			}
			
			if(timer % 20 == 0) // lemon bursts
			{
				double di = 20;
				mainLevel.soundPlayer.play("sound/ZAP2.WAV");
				if(difficulty.equals("Lunatic"))
					di = 15;
				for(double i = angleToPlayer; i < angleToPlayer +360; i+= di)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(3.0,i);
					enlistBullet(bullet, bullets);
				}
			}
		}
		
		
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
					bullet.isActive = true;
				if(bullet.vars[0] > 200)
					bullet.isActive = false;
				if(bullet.vars[0] > 210)
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
	
	
	
	// NSC2
	
	private void nsc2(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 500;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "";
		}
		
		
		if((timer + 50) %120 == 0) // blast of bullets
		{
			mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			double di = 0.4;
			if(difficulty.equals("Easy"))
				di = 0.8;
			
				
			double maxSpeed = 3.5;
			if(difficulty.equals("Hard"))
				maxSpeed = 4.0;
			if(difficulty.equals("Lunatic"))
				maxSpeed = 4.5;
			
			for(double i = 1.0 ; i < maxSpeed ; i += di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(i, angleToPlayer);
				bullet.vars[1] = i;
				bullet.rotate(angleToPlayer);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(i, angleToPlayer-10);
				bullet.vars[1] = i;
				bullet.rotate(angleToPlayer-10);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(i, angleToPlayer+10);
				bullet.vars[1] = i;
				bullet.rotate(angleToPlayer+10);
				enlistBullet(bullet, bullets);
				
				if(difficulty.equals("Lunatic"))
				{
					bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(i, angleToPlayer-60);
					bullet.vars[1] = i;
					bullet.rotate(angleToPlayer-10);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
					bullet.scale(1.2,1.0);
					bullet.setVelocity(i, angleToPlayer+60);
					bullet.vars[1] = i;
					bullet.rotate(angleToPlayer+10);
					enlistBullet(bullet, bullets);
				}
				
				
			}

			// move Citrus in random direction.
			
			vars[0] = (mainLevel.rand.nextInt(2)-0.5)*2*3; 
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*1.5; 
		}
		
		
		
		if(difficulty.equals("Lunatic"))
		{
			if((timer + 50 + 60) %120 == 0 && timer >= 100)
			mainLevel.soundPlayer.play("sound/PULSE07.WAV");
			
			for(Object s : bullets)
			{
				BulletSprite bullet = (BulletSprite) s;
				double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, (0.25*mainLevel.player.x + 0.75*bullet.x), (0.25*mainLevel.player.y + 0.75*bullet.y));
				
				if(bullet.vars[0] == 60)
				{
					bullet.setVelocity(bullet.vars[1], angleToPlayer);
				}
				bullet.vars[0]++;
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
		
		vars[0] *= 0.98;
		vars[1] *= 0.98;
		
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
	
	
	// Assassin GrapeVine
	
	private void assassinGrapeVineE(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = 80;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "Loom Sign - Assassin Grape Vine";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (vars[0] - x)*.05;
			y += (vars[1] - y)*.05;
			vars[2] = y;
		}
		else
		{
			
			if((timer - 101) % 300 == 0)
			{
			//	mainLevel.soundPlayer.play("sound/Overloaded.wav");
				BulletSprite spawnerGrape = new BulletSprite(x,y,'d', new Color(0x8800FF));
				spawnerGrape.aiMode = -1;
				
				double alpha = (mainLevel.player.y - this.y)/(mainLevel.SCREENH - 40 - this.y);
				
				if(mainLevel.player.y > this.y)
				{
					spawnerGrape.vars[0] = (mainLevel.player.x - this.x)/alpha + this.x;
					spawnerGrape.vars[1] = mainLevel.SCREENH - 40;
				}
				else
				{
					spawnerGrape.vars[0] = mainLevel.player.x;
					spawnerGrape.vars[1] = mainLevel.player.y;
				}
				
				if(spawnerGrape.vars[0] < 0)
				{
					alpha = (mainLevel.player.x - this.x)/(20 - this.x);
					spawnerGrape.vars[0] = 20;
					spawnerGrape.vars[1] = (mainLevel.player.y - this.y)/alpha + this.y;
				}
				if(spawnerGrape.vars[0] > mainLevel.SCREENW)
				{
					alpha = (mainLevel.player.x - this.x)/(mainLevel.SCREENW - 20 - this.x);
					spawnerGrape.vars[0] = mainLevel.SCREENW - 20;
					spawnerGrape.vars[1] = (mainLevel.player.y - this.y)/alpha + this.y;
				}
				
				enlistBullet(spawnerGrape,bullets);
				
				
			}
			if((timer - 101+100) % 150 == 0)
			{
				vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40)  + 20;
				vars[1] = mainLevel.rand.nextInt(100) + 30;
			}
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
			
			if(bullet.aiMode == -1)
			{
				if(timer % 4 == 0)
				{	
					if(soundSpacer % 5 == 0)
						mainLevel.soundPlayer.play("sound/boop02.wav");
					soundSpacer++;
					BulletSprite grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					double angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(3, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(2, angle);
					enlistBullet(grape,bullets);
				}
				
				bullet.x += (bullet.vars[0] - bullet.x)*0.02;
				bullet.y += (bullet.vars[1] - bullet.y)*0.02;
				
				if(Math.abs(bullet.x - bullet.vars[0]) < 30.0 && Math.abs(bullet.y - bullet.vars[1]) < 30.0)
				{
					bullet.destroy();
				}
			}
			if(bullet.vars[2] == 20)
				bullet.setVelocity(0.0,0);
			if(bullet.vars[2] == 100)
			{
			//	mainLevel.soundPlayer.play("sound/volum.wav");
				bullet.setVelocity(2.5,angleToPlayer);
			}
			bullet.vars[2]++;
			
		}
		unEnlistDeadBullets();
		
		
		
		// Citrus movement
		
		if(vars[0]-this.x < -0.5)
			setDirection('W');
		else if(vars[0]-this.x > 0.5)
			setDirection('E');
		else
			setDirection('S');
		
		x += (vars[0] - x)*0.05;
		vars[2] += (vars[1] - vars[2])*.05;
		y = vars[2] + 2*GameMath.sin(vars[3]);
		vars[3] += 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		
		
		
	}
	
	
	private void assassinGrapeVine(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = 80;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "Loom Sign - Assassin Grape Vine";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (vars[0] - x)*.05;
			y += (vars[1] - y)*.05;
			vars[2] = y;
		}
		else
		{
			
			if((timer - 101) % 300 == 0)
			{
			//	mainLevel.soundPlayer.play("sound/Overloaded.wav");
				BulletSprite spawnerGrape = new BulletSprite(x,y,'d', new Color(0x8800FF));
				spawnerGrape.aiMode = -1;
				
				double alpha = (mainLevel.player.y - this.y)/(mainLevel.SCREENH - 40 - this.y);
				
				if(mainLevel.player.y > this.y)
				{
					spawnerGrape.vars[0] = (mainLevel.player.x - this.x)/alpha + this.x;
					spawnerGrape.vars[1] = mainLevel.SCREENH - 40;
				}
				else
				{
					spawnerGrape.vars[0] = mainLevel.player.x;
					spawnerGrape.vars[1] = mainLevel.player.y;
				}
				
				if(spawnerGrape.vars[0] < 0)
				{
					alpha = (mainLevel.player.x - this.x)/(20 - this.x);
					spawnerGrape.vars[0] = 20;
					spawnerGrape.vars[1] = (mainLevel.player.y - this.y)/alpha + this.y;
				}
				if(spawnerGrape.vars[0] > mainLevel.SCREENW)
				{
					alpha = (mainLevel.player.x - this.x)/(mainLevel.SCREENW - 20 - this.x);
					spawnerGrape.vars[0] = mainLevel.SCREENW - 20;
					spawnerGrape.vars[1] = (mainLevel.player.y - this.y)/alpha + this.y;
				}
				
				enlistBullet(spawnerGrape,bullets);
				
				
			}
			if((timer - 101+100) % 150 == 0)
			{
				vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40)  + 20;
				vars[1] = mainLevel.rand.nextInt(100) + 30;
			}
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
			
			if(bullet.aiMode == -1)
			{
				if(timer % 4 == 0)
				{
					if(soundSpacer % 5 == 0)
						mainLevel.soundPlayer.play("sound/boop02.wav");
					soundSpacer++;
					BulletSprite grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					double angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(3, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(1, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(2, angle);
					enlistBullet(grape,bullets);
				}
				
				bullet.x += (bullet.vars[0] - bullet.x)*0.02;
				bullet.y += (bullet.vars[1] - bullet.y)*0.02;
				
				if(Math.abs(bullet.x - bullet.vars[0]) < 20.0 && Math.abs(bullet.y - bullet.vars[1]) < 20.0)
				{
					bullet.destroy();
				}
			}
			if(bullet.vars[2] == 20)
				bullet.setVelocity(0.0,0);
			if(bullet.vars[2] == 100)
			{
				bullet.setVelocity(2.5,angleToPlayer);
			//	mainLevel.soundPlayer.play("sound/volum.wav");
			}
			
			bullet.vars[2]++;
			
		}
		unEnlistDeadBullets();
		
		
		
		// Citrus movement
		
		if(vars[0]-this.x < -0.5)
			setDirection('W');
		else if(vars[0]-this.x > 0.5)
			setDirection('E');
		else
			setDirection('S');
		
		x += (vars[0] - x)*0.05;
		vars[2] += (vars[1] - vars[2])*.05;
		y = vars[2] + 2*GameMath.sin(vars[3]);
		vars[3] += 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		
		
		
	}
	
	private void assassinVineyardH(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = 80;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "Vine Control ~ Assassin Vineyard";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (vars[0] - x)*.05;
			y += (vars[1] - y)*.05;
			vars[2] = y;
		}
		else
		{
			
			if((timer - 101) % 300 == 0)
			{
				mainLevel.soundPlayer.play("sound/Overloaded.wav");
				BulletSprite spawnerGrape = new BulletSprite(x,y,'d', new Color(0x8800FF));
				spawnerGrape.aiMode = -1;
				
				double alpha = (mainLevel.player.y - this.y)/(mainLevel.SCREENH - 40 - this.y);
				
				if(mainLevel.player.y > this.y)
				{
					spawnerGrape.vars[0] = (mainLevel.player.x - this.x)/alpha + this.x;
					spawnerGrape.vars[1] = mainLevel.SCREENH - 40;
				}
				else
				{
					spawnerGrape.vars[0] = mainLevel.player.x;
					spawnerGrape.vars[1] = mainLevel.player.y;
				}
				
				if(spawnerGrape.vars[0] < 0)
				{
					alpha = (mainLevel.player.x - this.x)/(20 - this.x);
					spawnerGrape.vars[0] = 20;
					spawnerGrape.vars[1] = (mainLevel.player.y - this.y)/alpha + this.y;
				}
				if(spawnerGrape.vars[0] > mainLevel.SCREENW)
				{
					alpha = (mainLevel.player.x - this.x)/(mainLevel.SCREENW - 20 - this.x);
					spawnerGrape.vars[0] = mainLevel.SCREENW - 20;
					spawnerGrape.vars[1] = (mainLevel.player.y - this.y)/alpha + this.y;
				}
				
				enlistBullet(spawnerGrape,bullets);
				
				
			}
			if((timer - 101+100) % 150 == 0)
			{
				vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40)  + 20;
				vars[1] = mainLevel.rand.nextInt(100) + 30;
			}
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
			
			if(bullet.aiMode == -1)
			{
				if(timer % 4 == 0)
				{
					if(soundSpacer % 5 == 0)
						mainLevel.soundPlayer.play("sound/boop02.wav");
					soundSpacer++;
					BulletSprite grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					double angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(3, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(1, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(2, angle);
					enlistBullet(grape,bullets);
				}
				
				bullet.x += (bullet.vars[0] - bullet.x)*0.02;
				bullet.y += (bullet.vars[1] - bullet.y)*0.02;
				
				if(Math.abs(bullet.x - bullet.vars[0]) < 5.0 && Math.abs(bullet.y - bullet.vars[1]) < 5.0)
				{
					bullet.destroy();
				}
			}
			if(bullet.vars[2] == 20)
				bullet.setVelocity(0.0,0);
			if(bullet.vars[2] == 100)
			{
			//	mainLevel.soundPlayer.play("sound/volum.wav");
				bullet.setVelocity(2.5,angleToPlayer);
			}
			
			bullet.vars[2]++;
			
		}
		unEnlistDeadBullets();
		
		
		
		// Citrus movement
		
		if(vars[0]-this.x < -0.5)
			setDirection('W');
		else if(vars[0]-this.x > 0.5)
			setDirection('E');
		else
			setDirection('S');
		
		x += (vars[0] - x)*0.05;
		vars[2] += (vars[1] - vars[2])*.05;
		y = vars[2] + 2*GameMath.sin(vars[3]);
		vars[3] += 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		
		
		
	}
	
	private void assassinVineyardL(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = 80;
			vars[2] = y;
			vars[3] = 0;
			
			timer += 300;
			
			spellCardName = "Vine Control ~ Assassin Vineyard";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (vars[0] - x)*.05;
			y += (vars[1] - y)*.05;
			vars[2] = y;
		}
		else
		{
			
			if((timer - 101) % 300 == 0)
			{
				//mainLevel.soundPlayer.play("sound/Overloaded.wav");
				double alpha = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
				
				BulletSprite spawnerGrape = new BulletSprite(x,y,'d', new Color(0x8800FF));
				spawnerGrape.aiMode = 2;
				spawnerGrape.setRotationalVelocity(6,alpha+10,0.1,1.2);
				enlistBullet(spawnerGrape,bullets);
				
				spawnerGrape = new BulletSprite(x,y,'d', new Color(0x8800FF));
				spawnerGrape.aiMode = 2;
				spawnerGrape.setRotationalVelocity(6,alpha-10,-0.1,1.2);
				enlistBullet(spawnerGrape,bullets);
			}
			if((timer - 101+100) % 150 == 0)
			{
				this.vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40)  + 20;
				this.vars[1] = mainLevel.rand.nextInt(100) + 30;
			}
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
			
			if(bullet.aiMode == 2)
			{
				if(timer % 4 == 0)
				{
					if(soundSpacer % 5 == 0)
						mainLevel.soundPlayer.play("sound/boop02.wav");
					soundSpacer++;
					BulletSprite grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					double angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(3, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(3.5, angle);
					enlistBullet(grape,bullets);
					
					grape = new BulletSprite(bullet.x, bullet.y, 'd', new Color(0x8800FF));
					angle = mainLevel.rand.nextInt(360);
					grape.setVelocity(2.5, angle);
					enlistBullet(grape,bullets);
				}
				
				if(bullet.vars[0] > 200)
					bullet.destroy();
				bullet.vars[0]++;
				
				if(Math.abs(bullet.x - bullet.vars[0]) < 5.0 && Math.abs(bullet.y - bullet.vars[1]) < 5.0)
				{
					bullet.destroy();
				}
			}
			if(bullet.vars[2] == 30)
				bullet.setVelocity(0.0,0);
			if(bullet.vars[2] == 100)
			{
			//	mainLevel.soundPlayer.play("sound/volum.wav");
				bullet.setVelocity(2.5,angleToPlayer);
			}
			
			bullet.vars[2]++;
			
		}
		unEnlistDeadBullets();
		
		
		
		// Citrus movement
		
		if(vars[0]-this.x < -0.5)
			setDirection('W');
		else if(vars[0]-this.x > 0.5)
			setDirection('E');
		else
			setDirection('S');
		
		x += (vars[0] - x)*0.05;
		vars[2] += (vars[1] - vars[2])*.05;
		y = vars[2] + 2*GameMath.sin(vars[3]);
		vars[3] += 5;
		if(vars[3] > 360)
			vars[3] -= 360;
		
		
		
		
	}
	
	// NSC3
	
	private void nsc3(SpriteList bullets, SpriteList slaves, SpriteList sfx)
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
			timer += 400;
			
			spellCardName = "";
		}
		
		if((timer +100) % 150 == 0) // move Citrus in random direction.
		{
			vars[0] = (mainLevel.rand.nextInt(2)-0.5)*2*3; 
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*1.5; 
		}
		
		int di = 40;
		if(difficulty.equals("Normal"))
			di = 20;
		if(difficulty.equals("Hard"))
			di = 15;
		if(difficulty.equals("Lunatic"))
			di = 10;
		
		if((timer - 50) %400 == 0) // orange circle
		{
			mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i += di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'e', new Color(0xFF5500));
				bullet.setVelocity(2.5, i);
				bullet.rotate(i);
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 70) %400 == 0) // lime circle
		{
			mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			for(double i = angleToPlayer+10 ; i < angleToPlayer + 10 + 360 ; i +=  di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
				bullet.scale(1.2,1.0);
				bullet.setVelocity(3.5, i);
				bullet.rotate(i);
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 90) %400 == 0)  // orange circle
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
			for(double i = angleToPlayer+5 ; i < angleToPlayer+5 + 360 ; i +=  di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'e', new Color(0xFF5500));
				bullet.setVelocity(3, i);
				bullet.rotate(i);
				enlistBullet(bullet, bullets);
			}
		}
		
		int interval = 0;
		if(difficulty.equals("Normal"))
			interval = 60;
		if(difficulty.equals("Hard"))
			interval = 120;
		if(difficulty.equals("Lunatic"))
			interval = 240;
		
		if((timer - 150) %400 < interval && timer % 10 == 0) // cherry stream
		{
			mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			
			BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFF0077));
			bullet.setVelocity(3, angleToPlayer);
			bullet.rotate(angleToPlayer);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(x,y, 'c', new Color(0x770000));
			bullet.setVelocity(2, angleToPlayer);
			bullet.rotate(angleToPlayer);
			bullet.scale(1.2,1.0);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(x,y, 'c', new Color(0xFF0077));
			bullet.setVelocity(1, angleToPlayer);
			bullet.rotate(angleToPlayer);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(x,y, 'c', new Color(0x770000));
			bullet.setVelocity(0.5, angleToPlayer);
			bullet.rotate(angleToPlayer);
			bullet.scale(1.2,1.0);
			enlistBullet(bullet, bullets);
			
		}
		
		if((timer - 230) %400 < 60 && timer % 5 == 0) // lime wave
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
			
			BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0x00FF00));
			bullet.setVelocity(3, angleToPlayer-60 + ((timer - 230) %400)*2);
			bullet.rotate(angleToPlayer);
			bullet.scale(1.2,1.0);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(x,y, 'c', new Color(0xFFFF00));
			bullet.setVelocity(2.7, angleToPlayer + 60 - ((timer - 230) %400)*2);
			bullet.rotate(angleToPlayer);
			bullet.scale(1.2,1.0);
			enlistBullet(bullet, bullets);
			
			
			
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
		
		vars[0] *= 0.98;
		vars[1] *= 0.98;
		
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
	
	// Demarcation Berrier
	
	private void berryBarrier(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			timer = 150;
			
			spellCardName = "Berry Abjuration ~ Demarcation Berrier";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if((timer - 50) % 150 == 0) // create a burst of bullets
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=20)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFF0000));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 1;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 75) % 150 == 0) // create a burst of bullets
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=20)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFF0000));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 1;
				bullet.vars[2] = 1.5;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 1.5;
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 100) % 150 == 0 && difficulty.equals("Normal")) // create a burst of bullets
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+=20)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0xFF0000));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 1;
				bullet.vars[2] = 1.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 1.0;
				enlistBullet(bullet, bullets);
			}
			
			// move Citrus in random direction.
			
			vars[0] = (mainLevel.rand.nextInt(2)-0.5)*2*3; 
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*1.5; 
		}
		
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.vars[0] == 1)
			{
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, 4.0, 0.98);
				bullet.vars[1]++;
			}
			if(bullet.vars[0] == 2)
			{
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, -4.0, 0.98);
				bullet.vars[1]++;
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
	
	private void vengefulApplesHL(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 90;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			timer = 150;
			
			spellCardName = "Edible Abjuration ~ Vengeful Apples (More Like Large Cherries)";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		int di = 20;
		if(difficulty.equals("Lunatic"))
			di = 15;
				
		if((timer - 50) % 150 == 0) // create a burst of bullets
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);

			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'd', new Color(0xAA0000));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 1;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 3;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 75) % 150 == 0) // create a burst of bullets
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'd', new Color(0xAA0000));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 1;
				bullet.vars[2] = 1.5;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 3;
				bullet.vars[2] = 1.5;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 1.5;
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 100) % 150 == 0) // create a burst of bullets
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= di)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'd', new Color(0xAA0000));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 1;
				bullet.vars[2] = 1.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 3;
				bullet.vars[2] = 1.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 1.0;
				enlistBullet(bullet, bullets);
			}
			
			// move Citrus in random direction.
			
			vars[0] = (mainLevel.rand.nextInt(2)-0.5)*2*3; 
			vars[1] = (mainLevel.rand.nextInt(2)-0.5)*2*1.5; 
		}
		
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.vars[0] == 0)
			{
				bullet.setVelocity(bullet.vars[1],bullet.theta);
				if(difficulty.equals("Lunatic"))
					bullet.vars[1] *= 1.05;
				else
					bullet.vars[1] *= 1.02;
			}
			if(bullet.vars[0] == 1)
			{
				
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, 4.0, 0.98);
				if(bullet.vars[1] == 120)
				{
					if(soundSpacer % 10 == 0)
						mainLevel.soundPlayer.play("sound/PULSE07.WAV");
					soundSpacer++;
					double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
					bullet.vars[0] = 0;
					bullet.aiMode = 0;
					bullet.vars[1] = 3;
					bullet.setVelocity(3,angleToPlayer);
				}
				bullet.vars[1]++;
			}
			if(bullet.vars[0] == 2)
			{
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, -4.0, 0.98);
				bullet.vars[1]++;
			}
			if(bullet.vars[0] == 3)
			{
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, 4.0, 0.98);
				bullet.vars[1]++;
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
	
	private void deliciouslyDeadlyBouquet(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 1000;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			timer = 150;
			
			spellCardName = "Extra Spell: Cornocopia Art ~ Deliciously Deadly Bouquet";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		if(timer < 250)
		{
			x += (mainLevel.SCREENW/2 - x)*.95;
			y += (100 - y)*.95;
			vars[2] = y;
		}
				
		if(timer % 150 == 0 && timer >= 250) // create a burst of bullets
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);

			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 30)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 3;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
			}
		}
		if((timer - 50) % 150 == 0 && timer >= 250) // create a burst of bullets
		{
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);

			for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 30)
			{
				BulletSprite bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i, 0.0, 0);
				bullet.vars[0] = 3;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(x,y, 'c', new Color(0x770077));
				bullet.scale(1.0,1.0);
				bullet.aiMode = 2;
				bullet.setRotationalVelocity(2.0, i+10, 0.0, 0);
				bullet.vars[0] = 2;
				bullet.vars[2] = 2.0;
				enlistBullet(bullet, bullets);
			}
		}
		if((timer == 350)) // create stalker bullet
		{
			BulletSprite stalkerBullet = new BulletSprite(x,y,'d', new Color(0xCCFF00));
			stalkerBullet.aiMode = -1;
			stalkerBullet.vars[1] = x;
			stalkerBullet.vars[2] = y;
			enlistBullet(stalkerBullet, bullets);
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.vars[0] == 2)
			{
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, -4.0, 0.98);
				bullet.vars[1]++;
			}
			if(bullet.vars[0] == 3)
			{
				if(bullet.vars[1] < 30)
					bullet.dx *= 0.9;
				if(bullet.vars[1] == 60)
					bullet.setRotationalVelocity(bullet.vars[2], bullet.theta, 4.0, 0.98);
				bullet.vars[1]++;
			}
			if(bullet.aiMode == -1)
			{
				bullet.x += Math.min(3,(bullet.vars[1] - bullet.x)*0.05);
				bullet.y += Math.min(3,(bullet.vars[2] - bullet.y)*0.05);
				if((timer - 150)% 150 == 0)
				{
					bullet.vars[1] = mainLevel.player.x;
					bullet.vars[2] = mainLevel.player.y;
				}
				if((timer - 100)% 150 == 0)
				{
					double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite petal;
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 60)
					{
						petal = new BulletSprite(bullet.x,bullet.y, 'e', new Color(0xFF0000));
						petal.scale(1.0,1.0);
						petal.aiMode = 2;
						petal.setRotationalVelocity(3.0, i, 10.0, 0.98);
						enlistBullet(petal, bullets);
					}
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 45)
					{
						petal = new BulletSprite(bullet.x,bullet.y, 'c', new Color(0xFFFF00));
						petal.scale(1.2,1.0);
						petal.aiMode = 2;
						petal.setRotationalVelocity(2.0, i, 20.0, 0.98);
						enlistBullet(petal, bullets);
					}
				}
				if((timer - 110)% 150 == 0 && (difficulty.equals("Hard") || difficulty.equals("Lunatic")))
				{
					double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite petal;
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 60)
					{
						petal = new BulletSprite(bullet.x,bullet.y, 'e', new Color(0xFF8800));
						petal.scale(1.0,1.0);
						petal.aiMode = 2;
						petal.setRotationalVelocity(3.0, i, 10.0, 0.98);
						enlistBullet(petal, bullets);
					}
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 45)
					{
						petal = new BulletSprite(bullet.x,bullet.y, 'c', new Color(0x00FF00));
						petal.scale(1.2,1.0);
						petal.aiMode = 2;
						petal.setRotationalVelocity(2.0, i, 20.0, 0.98);
						enlistBullet(petal, bullets);
					}
				}
				if((timer - 120)% 150 == 0 && difficulty.equals("Lunatic"))
				{
					double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite petal;
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 60)
					{
						petal = new BulletSprite(bullet.x,bullet.y, 'e', new Color(0xFFCC00));
						petal.scale(1.0,1.0);
						petal.aiMode = 2;
						petal.setRotationalVelocity(3.0, i, 10.0, 0.98);
						enlistBullet(petal, bullets);
					}
					for(double i = angleToPlayer ; i < angleToPlayer + 360 ; i+= 45)
					{
						petal = new BulletSprite(bullet.x,bullet.y, 'c', new Color(0xFFFF00));
						petal.scale(1.2,1.0);
						petal.aiMode = 2;
						petal.setRotationalVelocity(2.0, i, 20.0, 0.98);
						enlistBullet(petal, bullets);
					}
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
