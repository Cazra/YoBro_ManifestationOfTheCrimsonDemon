
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



public class BossShurbert extends BossSprite
{
	private static int numInstances = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	
	// enemy vars
	
	int hoverAngle;
	
	// CONSTRUCTOR
	
	public BossShurbert(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(15);
		this.lastAttackIndex = 1;
		portrait = null;
		hoverAngle = 0;
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
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		Image img;
		
		// load our images
		
		imageURL = BossShurbert.class.getResource("graphics/ShurbertSheet.png");
		Image shurbertSheet = tk.getImage(imageURL);
		shurbertSheet = ColorFilters.setTransparentColor(shurbertSheet, new Color(0xFF00FF));
		
		// Forward
		
		img = ImageBlitter.crop(shurbertSheet,1,1,33,37);
		il.addImage(img);
		focusTable.put("ShurbertS1",new Point(16,23));
		imageTable.put("ShurbertS1",img);
		
		img = ImageBlitter.crop(shurbertSheet,36,1,33,37);
		il.addImage(img);
		focusTable.put("ShurbertS2",new Point(16,23));
		imageTable.put("ShurbertS2",img);
		
		img = ImageBlitter.crop(shurbertSheet,71,1,33,37);
		il.addImage(img);
		focusTable.put("ShurbertS3",new Point(16,23));
		imageTable.put("ShurbertS3",img);
		
		// Right 
		
		img = ImageBlitter.crop(shurbertSheet,1,40,33,37);
		il.addImage(img);
		focusTable.put("ShurbertE1",new Point(16,23));
		imageTable.put("ShurbertE1",img);
		
		img = ImageBlitter.crop(shurbertSheet,36,40,33,37);
		il.addImage(img);
		focusTable.put("ShurbertE2",new Point(16,23));
		imageTable.put("ShurbertE2",img);
		
		img = ImageBlitter.crop(shurbertSheet,71,40,33,37);
		il.addImage(img);
		focusTable.put("ShurbertE3",new Point(16,23));
		imageTable.put("ShurbertE3",img);
		
		// Left
		
		img = ImageBlitter.crop(shurbertSheet,1,79,33,37);
		il.addImage(img);
		focusTable.put("ShurbertW1",new Point(16,23));
		imageTable.put("ShurbertW1",img);
		
		img = ImageBlitter.crop(shurbertSheet,36,79,33,37);
		il.addImage(img);
		focusTable.put("ShurbertW2",new Point(16,23));
		imageTable.put("ShurbertW2",img);
		
		img = ImageBlitter.crop(shurbertSheet,71,79,33,37);
		il.addImage(img);
		focusTable.put("ShurbertW3",new Point(16,23));
		imageTable.put("ShurbertW3",img);
		
		System.out.println("loaded image data for BossShurbert");
		
	}
	
	// LOGIC METHODS
	
	
	
	
	
	// RENDERING METHODS
	
	protected void draw(Graphics2D g)
	{
		g.translate(0,2*GameMath.sin(hoverAngle));
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
		
		// Animate current frame
		
		String imgKey = "Shurbert" + this.direction;
		int imgKeyNum = 1;
		imgKeyNum += frameNum/4;
			
		
		imgKey += imgKeyNum + "";
		
		
		curImage = imageTable.get(imgKey);
		
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
		
		width = 33;
		height = 35;

		this.frameNum++;
		if(this.frameNum >= 12)
			this.frameNum = 0;
		
		hoverAngle += 5;
		
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
		if(attackIndex == 1) // Cryo-Science ~ Blizzard Grenade
		{
			stageBG.nextBGID = 1;
			blizzardGrenade(bullets,slaves,sfx);
		}
		
		
		// set health bar values and spell card text

	//	portrait.text = spellCardName;
		mainLevel.barBossHealth.caption2 = spellCardName;
		
		// increment timer
		
		timer++;
	}

	
	// NSC1
	
	private void nsc1(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 600;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = 0;
			
			spellCardName = "";
		}
		
		
		double angleToPlayer = GameMath.getAngleTo(this.x, this.y, (mainLevel.player.x + this.x)/2, (mainLevel.player.y +this.y)/2);
		
		if(vars[2] == 0)
		{
			vars[0] = -2;
			if(timer % 10 == 0)
			{
				mainLevel.soundPlayer.play("sound/Grapling.wav");
			}
			if (timer % 3 == 0)
			{
				BulletSprite bullet;
				
				
				if(difficulty == "Easy" && timer % 6 == 0)
				{
					bullet = new BulletSprite(x,y, 'm', new Color(0x0000FF));
					bullet.setVelocity(2.0, angleToPlayer);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty != "Easy")
				{
					bullet = new BulletSprite(x,y, 'm', new Color(0x0000FF));
					bullet.setVelocity(4.0, angleToPlayer);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Hard")
				{
					bullet = new BulletSprite(x,y, 'd', new Color(0x0000FF));
					bullet.setVelocity(0.5, angleToPlayer);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Lunatic")
				{
					bullet = new BulletSprite(x,y, 'd', new Color(0x0000FF));
					bullet.setVelocity(0.5, angleToPlayer+20);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'd', new Color(0x0000FF));
					bullet.setVelocity(0.5, angleToPlayer-20);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
			}
			
			if(x < 50)
				vars[2] = 1;
		}
		
		int bulletSpacing = 20;
		double bulletSpeed = 2.5;
		if(difficulty == "Easy")
		{
			bulletSpacing = 30;
		}
		if(difficulty == "Hard")
		{
			bulletSpacing = 15;
			bulletSpeed = 3;
		}
		if(difficulty == "Lunatic")
		{
			bulletSpacing = 15;
			bulletSpeed = 4;
		}
		
		if(vars[2] >= 1 && vars[2] < 130)
		{
			if(vars[2] == 20)
			{
				mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
				for(int i = 0; i < 360; i+= bulletSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
					
					bullet.setRotationalVelocity(bulletSpeed, i, 6.0, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					if(difficulty == "Hard" || difficulty == "Lunatic")
					{
						bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
						bullet.setVelocity(4.0, angleToPlayer + i);
						enlistBullet(bullet, bullets);
					}
				}
			}
			if(vars[2] == 50)
			{
				mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
				for(int i = 0; i < 360; i+= bulletSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
					
					bullet.setRotationalVelocity(bulletSpeed-1, i, -6.0, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					if(difficulty == "Lunatic")
					{
						bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
						bullet.setVelocity(4.0, angleToPlayer + i + 10);
						enlistBullet(bullet, bullets);
					}
				}
			}
			if(vars[2] == 80)
			{
				mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
				for(int i = 0; i < 360; i+=bulletSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'e', new Color(0x000099));
					
					bullet.setRotationalVelocity(bulletSpeed+1, i, 6.0, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					if(difficulty == "Hard" || difficulty == "Lunatic")
					{
						bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
						bullet.setVelocity(4.0, angleToPlayer + i);
						enlistBullet(bullet, bullets);
					}
				}
			}
			
			vars[2]++;
		}
		if(vars[2] == 130)
		{
			vars[0] = 2;
			if(timer % 10 == 0)
			{
				mainLevel.soundPlayer.play("sound/Grapling.wav");
			}
			if (timer % 3 == 0)
			{
				BulletSprite bullet;
				if(difficulty == "Easy" && timer % 6 == 0)
				{
					bullet = new BulletSprite(x,y, 'm', new Color(0x0000FF));
					bullet.setVelocity(2.0, angleToPlayer);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty != "Easy")
				{
					bullet = new BulletSprite(x,y, 'm', new Color(0x0000FF));
					bullet.setVelocity(4.0, angleToPlayer);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Hard")
				{
					bullet = new BulletSprite(x,y, 'd', new Color(0x0000FF));
					bullet.setVelocity(0.5, angleToPlayer);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Lunatic")
				{
					bullet = new BulletSprite(x,y, 'd', new Color(0x0000FF));
					bullet.setVelocity(0.5, angleToPlayer+20);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(x,y, 'd', new Color(0x0000FF));
					bullet.setVelocity(0.5, angleToPlayer-20);
					bullet.rotate(angleToPlayer);
					enlistBullet(bullet, bullets);
				}
			}
			
			if(this.x > mainLevel.SCREENW - 50)
				vars[2] = 131;
		}
		if(vars[2] >= 131 && vars[2] < 260)
		{
			if(vars[2] == 150)
			{
				mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
				for(int i = 0; i < 360; i+= bulletSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
					
					bullet.setRotationalVelocity(bulletSpeed, i, -6.0, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
			if(vars[2] == 180)
			{
				mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
				for(int i = 0; i < 360; i+= bulletSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'm', new Color(0x4400FF));
					
					bullet.setRotationalVelocity(bulletSpeed-1, i, 6.0, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
			if(vars[2] == 210)
			{
				mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
				for(int i = 0; i < 360; i+= bulletSpacing)
				{
					BulletSprite bullet = new BulletSprite(x,y, 'e', new Color(0x000099));
					
					bullet.setRotationalVelocity(bulletSpeed+1, i, -6.0, 0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
			vars[2]++;
		}
		if(vars[2] >= 260)
		{
			vars[2] = 0;
		}
		
		
		unEnlistDeadBullets();
		
		// Shurbert movement
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		if(this.x < 0)
			this.vars[0] = 4;
		if(this.x > mainLevel.SCREENW)
			this.vars[0] = -4;
		if(this.y < 0)
			this.vars[1] = 4;
			
		this.x += this.vars[0];
		this.y += this.vars[1];
		
		this.vars[0] *= 0.9;
		this.vars[1] *= 0.9;
	}
	
	
	// Blizzard Grenade
	
	private void blizzardGrenade(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 800;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 45;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = y;
			vars[3] = 0;
			
			spellCardName = "Cryo-Science ~ Blizzard Grenade";
		//	portrait.vars[2] = 10;
		}
	//	portrait.spellCardPortrait();
		
		if(timer < 100)
		{
			x += (mainLevel.SCREENW/2 - x)*0.05;
			y += (80 - y)*0.05;
			vars[2] = x;
			vars[3] = y;
		}
		else
		{
			if(timer == 100)
			{
				timer = 550;
			}
			
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
			if(timer % 550 == 0)
			{
				EnemySprite grenadeSpawner = new EnemySprite(x,y,'e',9999);
				grenadeSpawner.vars[0] = mainLevel.player.x;
				grenadeSpawner.vars[1] = mainLevel.player.y;
				enlistBullet(grenadeSpawner, slaves);
			}
			if((timer - 300) % 550 == 0)
			{
				this.vars[2] = mainLevel.rand.nextInt(mainLevel.SCREENW - 100) +50;
				this.vars[3] = mainLevel.rand.nextInt(100) +50;
			}
			if((timer - 300) % 550 < 200 && timer % 100 == 0)
			{
				EnemySprite grenadeSpawner = new EnemySprite(x,y,'e',9999);
				grenadeSpawner.vars[0] = mainLevel.player.x;
				grenadeSpawner.vars[1] = mainLevel.player.y;
				grenadeSpawner.scale(0.5,0.5);
				grenadeSpawner.aiType = 1;
				enlistBullet(grenadeSpawner, slaves);
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
				if(bullet.vars[0] > 60)
					bullet.isActive = true;
				if(bullet.vars[0] > 200)
					bullet.isActive = false;
				if(bullet.vars[0] > 210)
					bullet.destroy();
			}
			
		}
		
		for(Object s : slaves)
		{
			EnemySprite minion = (EnemySprite) s;
			
			if(minion.aiType == 0)
			{
				if(minion.vars[2] < 180)
				{
					double alpha = (GameMath.cos(minion.vars[2]) + 1)/2.0;
					
					minion.x = (alpha)*this.x + (1-alpha)*minion.vars[0];
					minion.y = (alpha)*this.y + (1-alpha)*minion.vars[1];
					
					minion.vars[2] += 2;
				}
				else
				{
					minion.vars[2]++;
					BulletSprite bullet;
					if(minion.vars[2] == 181)
						mainLevel.soundPlayer.play("sound/Craft land.wav");
					
					if(minion.vars[2] % 3 == 0)
					{
						bullet = new BulletSprite(minion.x, minion.y, 'd', new Color(0x9999FF));
						bullet.setVelocity(2.0,minion.vars[2]*31);
						enlistBullet(bullet, bullets);
						
						bullet = new BulletSprite(minion.x, minion.y, 'd', new Color(0x9999FF));
						bullet.setVelocity(1.0,minion.vars[2]*31-90);
						enlistBullet(bullet, bullets);

						bullet = new BulletSprite(minion.x, minion.y, 'c', new Color(0x9999FF));
						bullet.scale(2,2);
						bullet.setVelocity(0.5,minion.vars[2]*31-45);
						enlistBullet(bullet, bullets);
					}
					
					
					if(minion.vars[2] % 50 == 0)
					{
						double angleToPlayer = GameMath.getAngleTo(minion.x, minion.y, mainLevel.player.x, mainLevel.player.y);
						
						for(double i = angleToPlayer ; i < angleToPlayer + 360; i+= 40)
						{
							if(difficulty == "Hard")
							{
								bullet = new BulletSprite(minion.x,minion.y, 'a', new Color(0x9999FF));
								bullet.setVelocity(3.0,i);
								enlistBullet(bullet, bullets);
								
								bullet = new BulletSprite(minion.x,minion.y, 'm', new Color(0x9999FF));
								bullet.setVelocity(2.0,i+20);
								enlistBullet(bullet, bullets);
							}
							else if(difficulty == "Lunatic")
							{
								bullet = new BulletSprite(minion.x,minion.y, 'a', new Color(0x9999FF));
								bullet.setVelocity(3.0,i);
								enlistBullet(bullet, bullets);
								
								bullet = new BulletSprite(minion.x,minion.y, 'a', new Color(0x9999FF));
								bullet.setVelocity(2.0,i+20);
								enlistBullet(bullet, bullets);
								
								bullet = new BulletSprite(minion.x,minion.y, 'm', new Color(0x9999FF));
								bullet.setVelocity(4.0,i+30);
								enlistBullet(bullet, bullets);
								
								bullet = new BulletSprite(minion.x,minion.y, 'm', new Color(0x9999FF));
								bullet.setVelocity(1.5,i+10);
								enlistBullet(bullet, bullets);
							}
							else if(difficulty == "Normal")
							{
								bullet = new BulletSprite(minion.x,minion.y, 'm', new Color(0x9999FF));
								bullet.scale(2,2);
								bullet.setVelocity(3.0,i);
								enlistBullet(bullet, bullets);
							}
						}
					}
					
					if(minion.vars[2] > 300)
						minion.destroy();
				}
			}
			else if(minion.aiType == 1)
			{
				if(minion.vars[2] < 180)
				{
					double alpha = (GameMath.cos(minion.vars[2]) + 1)/2.0;
					
					minion.x = (alpha)*this.x + (1-alpha)*minion.vars[0];
					minion.y = (alpha)*this.y + (1-alpha)*minion.vars[1];
					
					minion.vars[2] += 2;
				}
				else
				{
					int spacing = 60;
					if(difficulty == "Hard" || difficulty == "Lunatic")
					{
						spacing = 40;
					}
					for(int i = 0 ; i < 360; i+= spacing)
					{
						BulletSprite bullet = new BulletSprite(minion.x,minion.y, 'a', new Color(0x9999FF));
						bullet.scale(2,2);
						bullet.setRotationalVelocity(3.0,i,5,0.99);
						bullet.aiMode = 2;
						enlistBullet(bullet, bullets);
						
						if(difficulty == "Lunatic")
						{
							bullet = new BulletSprite(minion.x,minion.y, 'a', new Color(0x9999FF));
							bullet.setRotationalVelocity(2.0,i,-5,0.99);
							bullet.aiMode = 2;
							enlistBullet(bullet, bullets);
							
						/*	bullet = new BulletSprite(minion.x,minion.y, 'a', new Color(0x9999FF));
							bullet.setRotationalVelocity(1.0,i,4,0.99);
							bullet.aiMode = 2;
							enlistBullet(bullet, bullets);*/
						}
					}
					mainLevel.soundPlayer.play("sound/WHOOSH06.WAV");
					minion.destroy();
				}
			}
		}
		
		unEnlistDeadBullets();
		
		// Shurbert movement
		
		if(Math.abs(this.x - this.vars[2]) > 10)
		{
			vars[0] = 2*(this.vars[2] - this.x)/Math.abs(this.vars[2] - this.x);
		}
		
		if(Math.abs(this.y - this.vars[3]) > 10)
		{
			vars[1] = 2*(this.vars[3] - this.y)/Math.abs(this.vars[3] - this.y);
		}
		
		if(vars[0] < -0.5)
			setDirection('W');
		else if(vars[0] > 0.5)
			setDirection('E');
		else
			setDirection('S');
			
		if(this.x < 0)
			this.vars[0] = 4;
		if(this.x > mainLevel.SCREENW)
			this.vars[0] = -4;
		if(this.y < 0)
			this.vars[1] = 4;
			
		this.x += this.vars[0];
		this.y += this.vars[1];
		
		this.vars[0] *= 0.9;
		this.vars[1] *= 0.9;
	}
	
	
	
}
