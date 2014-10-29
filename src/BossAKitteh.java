
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



public class BossAKitteh extends BossSprite
{
	private static int numInstances = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	// enemy vars
	
	public boolean isSlashing = false;
	public int slashingTimer = 0;
	public int slashingAngle = 0;
	BulletSprite swordCollider;
	private int blinkTimer = 0;
	
	AKittehSatellite[] satellites = new AKittehSatellite[3];
	
	
	// CONSTRUCTOR
	
	public BossAKitteh(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(20);
		this.frameNum = 0;
		this.lastAttackIndex = 6;
		portrait = new PortraitAKittehSprite(m.SCREENW +100,300,"Adventure Kitteh");
		portrait.vars[0] = m.SCREENW +100;
		portrait.vars[1] = 100;
		
		for(int i = 0; i < 3; i++)
		{
			satellites[i] = new AKittehSatellite(x,y,i,this);
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
		
		PortraitAKittehSprite.clean();
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
		
		imageURL = BossAKitteh.class.getResource("graphics/KittehSheet.png");
		Image slushSheet = tk.getImage(imageURL);
		slushSheet = ColorFilters.setTransparentColor(slushSheet, new Color(0xFF00FF));
		
		// Side
		
		img = ImageBlitter.crop(slushSheet,1,1,43,40);
		il.addImage(img);
		focusTable.put("side1",new Point(12,22));
		imageTable.put("side1",img);
		
		img = ImageBlitter.crop(slushSheet,46,1,43,40);
		il.addImage(img);
		focusTable.put("side2",new Point(12,22));
		imageTable.put("side2",img);
		
		img = ImageBlitter.crop(slushSheet,91,1,43,40);
		il.addImage(img);
		focusTable.put("side3",new Point(12,22));
		imageTable.put("side3",img);
		
		img = ImageBlitter.crop(slushSheet,136,1,43,40);
		il.addImage(img);
		focusTable.put("side4",new Point(12,22));
		imageTable.put("side4",img);
		
		img = ImageBlitter.crop(slushSheet,181,1,43,40);
		il.addImage(img);
		focusTable.put("side1blink",new Point(12,22));
		imageTable.put("side1blink",img);
		
		// Forward
		
		img = ImageBlitter.crop(slushSheet,1,43,39,45);
		il.addImage(img);
		focusTable.put("front1",new Point(11,22));
		imageTable.put("front1",img);
		
		img = ImageBlitter.crop(slushSheet,42,43,39,45);
		il.addImage(img);
		focusTable.put("front2",new Point(11,22));
		imageTable.put("front2",img);
		
		img = ImageBlitter.crop(slushSheet,83,43,39,45);
		il.addImage(img);
		focusTable.put("front3",new Point(11,22));
		imageTable.put("front3",img);
		
		img = ImageBlitter.crop(slushSheet,124,43,39,45);
		il.addImage(img);
		focusTable.put("front4",new Point(11,22));
		imageTable.put("front4",img);
		
		img = ImageBlitter.crop(slushSheet,165,43,39,45);
		il.addImage(img);
		focusTable.put("front1blink",new Point(11,22));
		imageTable.put("front1blink",img);
		
		// slash
		
		img = ImageBlitter.crop(slushSheet,226,1,56,53);
		il.addImage(img);
		focusTable.put("slash",new Point(11,40));
		imageTable.put("slash",img);
		
		img = ImageBlitter.crop(slushSheet,284,1,97,80);
		il.addImage(img);
		focusTable.put("slashBig",new Point(14,67));
		imageTable.put("slashBig",img);
		
		System.out.println("loaded image data for BossSwirlyKyu");
		
		PortraitAKittehSprite.loadImages(il);
		AKittehSatellite.loadImages(il);
	}
	
	// LOGIC METHODS
	
	
	
	
	
	// RENDERING METHODS
	
	
	public boolean render(Graphics2D g)
	{
		for(AKittehSatellite s: satellites)
		{
			s.render(g);
		}
		super.render(g);

		return true;
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
		String imgKey;
		
		this.rotate(slashingAngle);
		
		if(isSlashing)
		{
			imgKey = "slash";
			
			if(difficulty == "Hard" || difficulty == "Lunatic")
			{
				imgKey += "Big";
				this.setRadius(75);
				width = 160;
				height = 160;
			}
			else
			{
				this.setRadius(40);
				width = 90;
				height = 90;
			}
			
			slashingAngle -= 47;
			slashingTimer++;
			
			if(slashingTimer % 10 == 1)
				mainLevel.soundPlayer.play("sound/slash.wav");
		}
		else
		{
			slashingAngle = 0;
			slashingTimer = 0;
			
			setRadius(20);
			width = 43;
			height = 45;
			
			if(this.direction == 'S')
				imgKey = "front";
			else
				imgKey = "side";
			
			int imgNum = 1;
			
			if(frameNum > 2)
				imgNum++;
			if(frameNum > 4)
				imgNum++;
			if(frameNum > 6)
				imgNum++;
			
			imgKey += imgNum;
			
			if(imgNum == 1)
			{
				if(blinkTimer == 0)
					imgKey += "blink";
				else
					blinkTimer--;
			}
			if(imgNum == 2 && blinkTimer == 0)
				blinkTimer = mainLevel.rand.nextInt(10)+5;
			
			frameNum++;
			if(frameNum > 8)
				frameNum = 0;
		}
		
		curImage = imageTable.get(imgKey);
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
		
		for(AKittehSatellite s : satellites)
		{
			s.animate(il);
		}
	}
	
	
	//////////////////// Spell Card logic
	
	
	
	public void runSpellCardLogic(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		super.runSpellCardLogic(bullets, slaves, sfx);
		isActive = true;
		if(attackIndex == 0) // Smite Evil
		{	
			stageBG.nextBGID = 2;
			smitefulSmiting(bullets,slaves,sfx);
		//	swordOfPowah(bullets,slaves,sfx);
		}
		if(attackIndex == 1) // end midboss
		{
			aiMode = 3;
			isSlashing = false;
			setDirection('E');
		}
		if(attackIndex == 2) // nonspellcard 1
		{
			stageBG.nextBGID = 0;
			nsc1(bullets,slaves,sfx);
		}
		if(attackIndex == 3) // Triangular Wisdom / Banishment Triangle
		{
			stageBG.nextBGID = 3;
			if(difficulty == "Easy" || difficulty == "Normal")
				triangularWisdom(bullets,slaves,sfx);
			else
				banishmentTriangle(bullets,slaves,sfx);
		}
		if(attackIndex == 4)
		{
			stageBG.nextBGID = 0;
			nsc2(bullets,slaves,sfx);
		}
		if(attackIndex == 5) // Nyan dash / Pegasus Boots
		{	
			stageBG.nextBGID = 2;
			pegasusBoots(bullets,slaves,sfx);
		}
		if(attackIndex == 6) // SWORD OF POWAH!!! ~ I can has omens?
		{
			stageBG.nextBGID = 3;
			swordOfPowah(bullets,slaves,sfx);
		}
		
		
		// set health bar values and spell card text

		portrait.text = spellCardName;
		mainLevel.barBossHealth.caption2 = spellCardName;
		
		// increment timer
		
		timer++;
	}

	
	
	
	
	// Heroics ~ Smitefully Smiting Kitty
	
	private void smitefulSmiting(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = 100;
			vars[2] = 0;
			vars[3] = 0;
			isSlashing = false;
			
			if(difficulty == "Easy" || difficulty == "Normal")
				spellCardName = "Adorable Heroics ~ You Can Haz Smitings";
			else
				spellCardName = "Cuddly Crusader ~ Smiteful Smitings of Smitiness";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		if(timer < 100)
		{
			x+= (mainLevel.SCREENW/2 - x)/20;
			y+= (100 - y)/20;
		}
		
		if(timer == 100)
		{
			isSlashing = true;
			vars[2] = 2;
			vars[3] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y)-60;
		}
		
		int interval = 7;
		if(difficulty == "Easy")
			interval = 12;
		if(difficulty == "Hard")
			interval = 12;
		if(difficulty == "Lunatic")
			interval = 9;
		
		int slashingEndTime = 160;
		if(difficulty == "Hard" || difficulty == "Lunatic")
			slashingEndTime = 320;
		
		if(timer >= 100 && timer < slashingEndTime && timer % interval == 0)
		{	
			BulletSprite bullet = new BulletSprite(this.x,this.y, 'n', new Color(0x0000FF));
			bullet.setVelocity(vars[2], vars[3]);
			bullet.aiMode = -1;
			bullet.scale(0.1,0.1);
			enlistBullet(bullet, bullets);
			
			vars[2] += 0.3;
			vars[3] += 12;
			if(difficulty == "Easy")
				vars[3] += 8;
			if(difficulty == "Lunatic")
				vars[3] -= 3;
			
			
			
		/*	if(difficulty == "Hard")
			{
				bullet = new BulletSprite(this.x,this.y, 'e', new Color(0x00AAAA));
				bullet.setVelocity(vars[2]+1, 0-vars[3]*3);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(this.x,this.y, 'e', new Color(0x00AAAA));
				bullet.setVelocity(vars[2]+1, 0-vars[3]*3 + 10);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(this.x,this.y, 'e', new Color(0x00AAAA));
				bullet.setVelocity(vars[2]+1, 0-vars[3]*3 + 20);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(this.x,this.y, 'e', new Color(0x00AAAA));
				bullet.setVelocity(vars[2]+1, 0-vars[3]*3 + 30);
				enlistBullet(bullet, bullets);
			}*/
		}
		if(timer == slashingEndTime + 20)
			isSlashing = false;
		
		if(timer > 300)
		{
			if(difficulty == "Easy" || difficulty == "Normal")
			{
				this.vars[0] = 100 + mainLevel.rand.nextInt(mainLevel.SCREENW-200);
				this.vars[1] = mainLevel.rand.nextInt(30+100);
			}
			timer = 99;
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.screenKillable = false;
				if(bullet.x > 0 && bullet.x < mainLevel.SCREENW && bullet.y > 0 && bullet.y < mainLevel.SCREENH)
				{
					bullet.x += bullet.dx;
					bullet.y += bullet.dy;
				}
				else if(bullet.vars[0] == 0)
				{
					double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
					
					BulletSprite laser = new BulletSprite(bullet.x,bullet.y, 'l', new Color(0x0000FF));
					laser.aiMode = -2;
					laser.theta = angleToPlayer;
					laser.isActive = false;
					laser.rotate(angleToPlayer);
					laser.screenKillable = false;
					enlistBullet(laser, bullets);
					
					bullet.vars[0]++;
				}
				else
				{
					bullet.vars[0]++;
					if(bullet.vars[0] > 60)
						bullet.destroy();
				}
			}
			if(bullet.aiMode == -2)
			{	
				bullet.vars[0]++;
				if(bullet.vars[0] == 61)
				{
					mainLevel.soundPlayer.play("sound/Laser 5.wav");
					
				/*	for(int i = 0; i < 2; i++)
					{
						BulletSprite newBullet = new BulletSprite(bullet.x,bullet.y,'d',new Color(0x0000CC));
						newBullet.setVelocity(mainLevel.rand.nextDouble()*2 + 0.1,bullet.theta + mainLevel.rand.nextInt(90)-45);
						newBullet.screenKillable = false;
						newBullet.aiMode = -3;
						enlistBullet(newBullet,bullets);
					}
					for(int i = 0; i < 2; i++)
					{
						BulletSprite newBullet = new BulletSprite(bullet.x,bullet.y,'a',new Color(0x00AAAA));
						newBullet.setVelocity(mainLevel.rand.nextDouble() + 0.1,bullet.theta + mainLevel.rand.nextInt(90)-45);
						newBullet.screenKillable = false;
						newBullet.aiMode = -3;
						enlistBullet(newBullet,bullets);
					}*/
				}
				
				int laserDeathTime = 160;
				if(difficulty == "Hard" || difficulty == "Lunatic")
					laserDeathTime = 100;
				
				if(bullet.vars[0] > 60)
					bullet.isActive = true;
				if(bullet.vars[0] > laserDeathTime)
					bullet.isActive = false;
				if(bullet.vars[0] > laserDeathTime + 10)
					bullet.destroy();
			}
			if(bullet.aiMode == -3)
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				bullet.vars[0]++;
				if(bullet.vars[0] > 20)
					bullet.screenKillable = true;
			}
			
			
		}
		
		
		
		unEnlistDeadBullets();
		
		
		// movement
		if(Math.abs(x-vars[0]) < 1)
			this.setDirection('S');
		else
			this.setDirection('E');
		this.x += (this.vars[0] - this.x)/5.0;
		this.y += (this.vars[1] - this.y)/5.0;
	}
	
	
	
	// nsc1
	
	private void nsc1(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40 ) + 20;
			vars[1] = mainLevel.rand.nextInt(100) + 20;
			vars[2] = 0;
			vars[3] = 0;
			isSlashing = false;
			//portrait.vars[2] = 10;
		}
		//portrait.spellCardPortrait();
		
		if(timer == 1)
		{
			vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40 ) + 20;
			vars[1] = mainLevel.rand.nextInt(100) + 20;
			isSlashing  = true;
		}
		
		if(timer == 101)
		{
			vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40 ) + 20;
			vars[1] = mainLevel.rand.nextInt(100) + 20;
			isSlashing  = true;
		}
		
		int timing = 2;
		if(difficulty == "Easy")
		timing = 4;
		
		
		if(timer < 200 && timer % 2 == 0)
		{
			// movement
			if(Math.abs(x-vars[0]) < 1)
				this.setDirection('S');
			else
				this.setDirection('E');
			this.x += (this.vars[0] - this.x)/25.0;
			this.y += (this.vars[1] - this.y)/25.0;
			
			BulletSprite bullet = new BulletSprite(this.x,this.y, 'm', new Color(0xBB0055));
			bullet.setVelocity(3, vars[2]);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(this.x,this.y, 'm', new Color(0xBB0055));
			bullet.setVelocity(3, vars[2]+72);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(this.x,this.y, 'm', new Color(0xBB0055));
			bullet.setVelocity(3, vars[2]+144);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(this.x,this.y, 'm', new Color(0xBB0055));
			bullet.setVelocity(3, vars[2]+216);
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(this.x,this.y, 'm', new Color(0xBB0055));
			bullet.setVelocity(3, vars[2]+288);
			enlistBullet(bullet, bullets);
			
			vars[2]+= 5;
			if(difficulty == "Easy")
				vars[2] += 5;
			if(difficulty == "Hard" || difficulty == "Lunatic")
			{
				vars[2] += mainLevel.rand.nextInt(5);
			}
			
		}
		
		if(timer == 200)
		{
			isSlashing = false;
			
			double angleToPlayer = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y) + 20;
			
			for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
			{
				for(double j = 3.0; j <= 7.0; j += 0.7)
				{
					BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0xFF7700));
					bullet.setVelocity(j, i);
					bullet.aiMode = -1;
					enlistBullet(bullet, bullets);
				}
			}
			
			if(difficulty == "Hard" || difficulty == "Lunatic")
			{
				for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
				{
					for(double j = 3.0; j <= 7.0; j += 0.7)
					{
						BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0xFF7700));
						bullet.setVelocity(j, i);
						bullet.vars[1] = 20;
						bullet.aiMode = -1;
						enlistBullet(bullet, bullets);
					}
				}
				
				for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
				{
					for(double j = 3.0; j <= 7.0; j += 0.7)
					{
						BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0xFF7700));
						bullet.setVelocity(j, i);
						bullet.vars[1] = -20;
						bullet.aiMode = -1;
						enlistBullet(bullet, bullets);
					}
				}
			}
			
		}
		if(timer == 250)
		{
			isSlashing = false;
			
			double angleToPlayer = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
			
			for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
			{
				for(double j = 3.0; j <= 7.0; j += 0.7)
				{
					BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0x77FF00));
					bullet.setVelocity(j, i);
					bullet.vars[1] = 10;
					bullet.aiMode = -1;
					enlistBullet(bullet, bullets);
				}
			}
			
			for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
			{
				for(double j = 3.0; j <= 7.0; j += 0.7)
				{
					BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0x77FF00));
					bullet.setVelocity(j, i);
					bullet.vars[1] = -10;
					bullet.aiMode = -1;
					enlistBullet(bullet, bullets);
				}
			}
			
		}
		if(timer == 300)
		{
			timer = 0;
			
			if(difficulty == "Lunatic")
			{
				double angleToPlayer = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
				for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
				{
					for(double j = 3.0; j <= 7.0; j += 0.7)
					{
						BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0xFF7700));
						bullet.setVelocity(j, i);
						bullet.vars[1] = 10;
						bullet.aiMode = -1;
						enlistBullet(bullet, bullets);
					}
				}
				
				for(double i = angleToPlayer; i < angleToPlayer+360; i+=45)
				{
					for(double j = 3.0; j <= 7.0; j += 0.7)
					{
						BulletSprite bullet = new BulletSprite(this.x,this.y, 'b', new Color(0xFF7700));
						bullet.setVelocity(j, i);
						bullet.vars[1] = -10;
						bullet.aiMode = -1;
						enlistBullet(bullet, bullets);
					}
				}
			}
		}
		
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				bullet.dx *= 0.95;
				bullet.dy *= 0.95;
				
				if(Math.abs(bullet.dx) < 0.1 && Math.abs(bullet.dy) < 0.1)
					bullet.aiMode = -2;
			}
			if(bullet.aiMode == -2)
			{
				bullet.vars[0]++;
				if(bullet.vars[0] == 10)
				{
					double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
					bullet.setVelocity(3,angleToPlayer + bullet.vars[1]);
					bullet.aiMode = 0;
				}
			}
		}
		
		
		unEnlistDeadBullets();
		
		
		
	}
	
	
	// Triangular Wisdom ~ Evil-Sealing Polygons
	
	private void triangularWisdom(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
		//	vars[0] = mainLevel.SCREENW/2;
		//	vars[1] = 100;
			vars[3] = 0;
			isSlashing = false;
			
			spellCardName = "Triangular Wisdom ~ Evil-Sealing Polygons";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		if(timer < 100)
		{
			//x+= (mainLevel.SCREENW/2 - x)/20;
			//y+= (200 - y)/20;
		}
		
		if(timer == 100)
		{
			vars[3] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
			
			for(int i = 0; i < 3; i++) // loop for 3 triangles
			{
				vars[3] += 120;
				
				double cos = GameMath.cos(vars[3]);
				double sin = GameMath.sin(0-vars[3]);
				
				double forIncrement = 1;
				for(double j = 0; j <= 2; j+= forIncrement)
				{
					for(double k = 0; k <= j; k+= forIncrement)
					{
						double tx = j*-50 + 50;
						double ty = -25*j + 50*k;
						
						// rotate transformation
						
						double ttx = tx*cos - ty*sin;
						double tty = tx*sin + ty*cos;
						
						// create the bullet
						
						BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color((int)(100 + 40*j), (int) (100 + 40* k), 0));
						bullet.setVelocity(2, vars[3]);
						bullet.aiMode = -1;
						bullet.vars[0] = this.x + ttx;
						bullet.vars[1] = this.y + tty;
						enlistBullet(bullet, bullets);
					}
				}
			}
		}
		
		if(timer == 160)
		{
			this.vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW-40)+20;
			this.vars[1] = mainLevel.rand.nextInt(200) + 20;
		}
		
		if(timer % 100 == 1)
		{
			for(double i = vars[3]; i < vars[3] + 360; i += 20)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'm', new Color(0x00CC00));
				bullet.setVelocity(1.5, i);
				enlistBullet(bullet, bullets);
			}
		}
		if(timer % 100 == 2 && difficulty != "Easy")
		{
			int waveSize = 1;
			for(int j = 5; j > 5 - waveSize ; j--)
			{
				for(double i = vars[3]; i < vars[3] + 360; i += 20)
				{
					BulletSprite bullet = new BulletSprite(this.x,this.y, 'm', new Color(0x00CC00));
					bullet.setRotationalVelocity(1.4, i,5,0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
				for(double i = vars[3]; i < vars[3] + 360; i += 20)
				{
					BulletSprite bullet = new BulletSprite(this.x,this.y, 'm', new Color(0x00CC00));
					bullet.setRotationalVelocity(1.3, i,-5,0.98);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
		}

		
		if(timer > 400)
		{
			timer = 99;
		}
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.x += (bullet.vars[0] - bullet.x)/10;
				bullet.y += (bullet.vars[1] - bullet.y)/10;
				
				bullet.vars[2]++;
				if(bullet.vars[2] > 60)
					bullet.aiMode = -2;
				
			}
			if(bullet.aiMode == -2)
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				int bounceLimit = 4;
				
				if(bullet.vars[3] < bounceLimit && (bullet.x < 0 || bullet.x > mainLevel.SCREENW))
				{
					bullet.vars[3]++;
					bullet.dx *= -1;
				}
					
				if(bullet.vars[3] < bounceLimit && (bullet.y < 0 || bullet.y > mainLevel.SCREENH))
				{
					bullet.vars[3]++;
					bullet.dy *= -1;
				}
			}
			
			
			
		}
		
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('E');
		this.x += (this.vars[0] - this.x)/15.0;
		this.y += (this.vars[1] - this.y)/15.0;
	}
	
	
	private void banishmentTriangle(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
		//	vars[0] = mainLevel.SCREENW/2;
		//	vars[1] = 100;
			vars[3] = 0;
			isSlashing = false;
			
			spellCardName = "Ficticious Ward ~ Banishment Triangle";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		if(timer < 100)
		{
			//x+= (mainLevel.SCREENW/2 - x)/20;
			//y+= (200 - y)/20;
		}
		
		if(timer == 100)
		{
			vars[3] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y) - 180;
			
			for(int i = 0; i < 3; i++) // loop for 3 triangles
			{
				vars[3] += 120;
				
				double cos = GameMath.cos(vars[3]);
				double sin = GameMath.sin(0-vars[3]);
				
				double forIncrement = 0.6;
				
				for(double j = 0; j <= 2; j+= forIncrement)
				{
					for(double k = 0; k <= j; k+= forIncrement)
					{
						double tx = j*-50 + 180;
						double ty = -25*j + 50*k;
						
						// rotate transformation
						
						double ttx = tx*cos - ty*sin;
						double tty = tx*sin + ty*cos;
						
						// create the bullet
						
						BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color((int)(100 + 40*j), (int) (100 + 40* k), 0));
						bullet.setVelocity(3, vars[3]);
						bullet.aiMode = -1;
						bullet.vars[0] = this.x + ttx;
						bullet.vars[1] = this.y + tty;
						bullet.vars[3] = this.x;
						bullet.vars[4] = this.y;
						enlistBullet(bullet, bullets);
					}
				}
			}
		}
		
		if(timer == 160)
		{
			this.vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW-40)+20;
			this.vars[1] = mainLevel.rand.nextInt(100) + 20;
			
			isSlashing = true;
		}
		if(Math.abs(x-this.vars[0]) < 1 && Math.abs(y-this.vars[1]) < 1)
			isSlashing = false;
		
		if(timer >= 180 && timer % 60 == 0)
		{
			double angleToPlayer = GameMath.getAngleTo(x,y,mainLevel.player.x,mainLevel.player.y);
			for(double i = angleToPlayer; i < angleToPlayer + 360; i+= 30)
			{
				for(double j = 5; j >= 3; j-= 0.5)
				{
					BulletSprite bullet = new BulletSprite(x,y,'m',new Color(0x007700));
					bullet.setRotationalVelocity(j/5.0*1.5,i,j,0.95);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
		}
		
		
		if(timer > 500)
		{
			timer = 99;
		}
		
		LinkedList<Sprite> newBullets = new LinkedList<Sprite>();
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.screenKillable = false;
				bullet.x += (bullet.vars[0] - bullet.x)/10;
				bullet.y += (bullet.vars[1] - bullet.y)/10;
				
				bullet.vars[2]++;
				if(bullet.vars[2] > 60)
				{
					bullet.setCircularVelocity(bullet.vars[3], bullet.vars[4], GameMath.getAngleTo(bullet.vars[3],bullet.vars[4],bullet.x,bullet.y), GameMath.getDist(bullet.x,bullet.y,bullet.vars[3],bullet.vars[4]), 0, 0);
					bullet.aiMode = -3;
					bullet.dx = 0.2;
					bullet.vars[2] = 0;
					bullet.vars[5] = GameMath.getAngleTo(bullet.vars[3],bullet.vars[4],mainLevel.player.x,mainLevel.player.y);
					bullet.vars[6] = -3*GameMath.sin(bullet.vars[5]);
					bullet.vars[5] = 3*GameMath.cos(bullet.vars[5]);
				}
				
			}
			if(bullet.aiMode == -3)
			{
				bullet.x = bullet.cx + bullet.rad * GameMath.cos(bullet.theta);
				bullet.y = bullet.cy - bullet.rad * GameMath.sin(bullet.theta);
				
				bullet.theta += bullet.dx;
				
				bullet.rotate(bullet.theta+90);
				
				bullet.vars[2]++;
				if(bullet.vars[2] >= 200)
				{
					bullet.rad -= 1;
					bullet.dx *= 0.9;
				}
				else
				{
					bullet.cx += (mainLevel.player.x - bullet.cx)/25.0;
					bullet.cy += (mainLevel.player.y - bullet.cy)/25.0;
					bullet.dx *= 1.01;
					if(difficulty == "Lunatic")
						bullet.dx *= 1.01;
				}
				if(bullet.rad < 5)
				{
					double lastWave = 7;
					if(difficulty == "Lunatic")
						lastWave = 6;
					for(int j = 10; j >= lastWave; j--)
					{
						BulletSprite newBullet = new BulletSprite(bullet.x,bullet.y,'m',new Color(0xCC0000));
						newBullet.setRotationalVelocity(j/10.0*2,bullet.theta-180,j,0.95);
						newBullet.aiMode = 2;
						newBullets.add(newBullet);
					}
					bullet.destroy();
				}
			}
		}
		
		for(Sprite bullet: newBullets)
		{
			enlistBullet(bullet,bullets);
		}
		
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('E');
		this.x += (this.vars[0] - this.x)/15.0;
		this.y += (this.vars[1] - this.y)/15.0;
	}
	
	
	
	// Pegasus Boots ~ Dashing Rainbow
	
	private void pegasusBoots(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = 0;
			vars[3] = 0;
			isSlashing = false;
			
			if(difficulty == "Easy" || difficulty == "Normal")
				spellCardName = "Nyan Crash ~ Dashing Lightning";
			else
				spellCardName = "Pegasus Boots ~ Nyanbow Dash";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		int speedLimit = 6;
		if(difficulty == "Hard" || difficulty == "Lunatic")
			speedLimit = 8;
		
		
		if(timer == 100) // begin dashing
		{
			vars[0] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
			isSlashing = true;
		}
		
		if(timer == 160 && ( difficulty == "Hard" || difficulty == "Lunatic")) // ROYAL RAINBOW!! (Hard/Lunatic only)
		{
			vars[1] = speedLimit;
			createTrailingRainbow(bullets);
			
			int maxBulletSpeed = 2;
			if(difficulty == "Lunatic")
			{
				maxBulletSpeed = 3;
			}
			
			for(int i = 0; i < 360; i+= 28)
			{
				for(int k = 1; k <= maxBulletSpeed; k++)
				{
					int modAngle = i + k*16;
					int bulletColor = Color.HSBtoRGB((float)(modAngle/360.0), 1.0f, 0.5f);
							
					BulletSprite bullet = new BulletSprite(this.x ,this.y, 'd', new Color(bulletColor));
					bullet.scale(2.0,2.0);
					bullet.setVelocity(k, modAngle);
					enlistBullet(bullet, bullets);
					
					modAngle += 3;
					bulletColor = Color.HSBtoRGB((float)(modAngle/360.0), 1.0f, 0.5f);
					
					bullet = new BulletSprite(this.x ,this.y, 'd', new Color(bulletColor));
					bullet.scale(2.0,2.0);
					bullet.setVelocity(k*0.95, modAngle);
					enlistBullet(bullet, bullets);
					
					modAngle += 3;
					bulletColor = Color.HSBtoRGB((float)(modAngle/360.0), 1.0f, 0.5f);
					
					bullet = new BulletSprite(this.x ,this.y, 'd', new Color(bulletColor));
					bullet.scale(2.0,2.0);
					bullet.setVelocity(k*0.9, modAngle);
					enlistBullet(bullet, bullets);
					
					modAngle += 3;
					bulletColor = Color.HSBtoRGB((float)(modAngle/360.0), 1.0f, 0.5f);
					
					bullet = new BulletSprite(this.x ,this.y, 'd', new Color(bulletColor));
					bullet.scale(2.0,2.0);
					bullet.setVelocity(k*0.85, modAngle);
					enlistBullet(bullet, bullets);
				}
			}
		}
		
		if(timer >= 100 && timer < 500) // spawn bullets
		{
			
				
			if(vars[1] < speedLimit)
			{
				vars[1] += 0.1;
			}
			
			if(difficulty == "Normal" || difficulty == "Easy")
			{
				if(x > 0 && x < mainLevel.SCREENW && y > 0 && y < mainLevel.SCREENH && timer % 15 == 0)
				{
					double maxBulletSpeed = 2.5;
					if(difficulty == "Easy")
						maxBulletSpeed = 1.0;
					
					for(double i = 0.5; i <= maxBulletSpeed ; i++)
					{
						for(double j = vars[0]; j < vars[0] + 360; j += 90)
						{
							double alpha = i / maxBulletSpeed;
							int bulletColor = (int)(alpha*0x0000FF + (1-alpha)*0xCCCC00);
							BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color(bulletColor));
							bullet.setVelocity(i, j);
							enlistBullet(bullet, bullets);
						}
					}
				}
			}
			else
			{
				int maxBullets = 5;
				if(difficulty == "Lunatic")
					maxBullets = 7;
				
				if(x > 0 && x < mainLevel.SCREENW && y > 0 && y < mainLevel.SCREENH && timer % 5  == 0)
				{
					for(int i = 0; i < maxBullets;i++)
					{
						double angle = mainLevel.rand.nextInt(360);
						double rad = mainLevel.rand.nextInt(70);
						int bulletColor = Color.HSBtoRGB((float)(angle/360.0), 1.0f, 0.5f);
						
						BulletSprite bullet = new BulletSprite(this.x + rad*GameMath.cos(angle),this.y - rad*GameMath.sin(angle), 'd', new Color(bulletColor));
						bullet.setVelocity(0.02, angle);
						enlistBullet(bullet, bullets);
					}
				}
			}
		}
		
		if(timer == 300) // dash 2
		{
			if(mainLevel.player.x < mainLevel.SCREENW/2)
				x = mainLevel.SCREENW + 100;
			else
				x = -100;
			y = mainLevel.rand.nextInt(mainLevel.SCREENH);
			
			vars[0] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
			
			for(Object s : bullets)
			{
				BulletSprite bullet = (BulletSprite) s;
				
				if(bullet.aiMode == 0)
				{
					bullet.vars[0] = 1;
				}
			}
			
			if(difficulty == "Hard" || difficulty == "Lunatic")
			{
				createTrailingRainbow(bullets);
			}
			else
			{
				createTrailingZoom(bullets);
			}
		}
		
		if(timer == 400) // dash 3
		{
			if(mainLevel.player.x < mainLevel.SCREENW/2)
				x = mainLevel.SCREENW + 100;
			else
				x = -100;
			y = mainLevel.rand.nextInt(mainLevel.SCREENH);
			
			vars[0] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
			
			for(Object s : bullets)
			{
				BulletSprite bullet = (BulletSprite) s;
				
				if(bullet.aiMode == 0)
				{
					bullet.vars[0] = 1;
				}
			}
			
			if(difficulty == "Hard" || difficulty == "Lunatic")
			{
				createTrailingRainbow(bullets);
			}
			else
			{
				createTrailingZoom(bullets);
			}
		}
		
		if(timer == 500) // dash 4
		{
			if(mainLevel.player.x < mainLevel.SCREENW/2)
				x = mainLevel.SCREENW + 100;
			else
				x = -100;
			y = 50;
			
			vars[0] = GameMath.getAngleTo(x, y, 200, 100);
			
			for(Object s : bullets)
			{
				BulletSprite bullet = (BulletSprite) s;
				
				if(bullet.aiMode == 0)
				{
					bullet.vars[0] = 1;
				}
			}
		}
		
		
		
		if(timer >= 500 && vars[1] > 0) // slow down
		{
			vars[1] -= 0.1;
		}
		if(timer == 600) // do something crazy with the remaining bullets
		{
			isSlashing = false;
			
			if(difficulty == "Normal" || difficulty == "Easy")
			{
				LinkedList<BulletSprite> newBullets = new LinkedList<BulletSprite>();
				
				for(Object s : bullets)
				{
					BulletSprite oldBullet = (BulletSprite) s;
					
					BulletSprite bullet = new BulletSprite(oldBullet.x, oldBullet.y, 'a', new Color(0xAA22AA));
					bullet.setProjectileVelocity(1, mainLevel.rand.nextInt(90)+45, 0.01);
					bullet.aiMode = 3;
					newBullets.add(bullet);
					
					bullet = new BulletSprite(oldBullet.x, oldBullet.y, 'a', new Color(0xAA22AA));
					bullet.setProjectileVelocity(1, mainLevel.rand.nextInt(90)+45, 0.01);
					bullet.aiMode = 3;
					newBullets.add(bullet);
					
					oldBullet.destroy();
					
				}
				
				for(Object s : newBullets)
				{
					BulletSprite bullet = (BulletSprite) s;
					enlistBullet(bullet, bullets);
				}
			}
			else
			{
				for(Object s : bullets)
				{
					BulletSprite bullet = (BulletSprite) s;
					
					if(bullet.aiMode == 0)
					{
						bullet.setVelocity(0.05, GameMath.getAngleTo(this.x, this.y, bullet.x, bullet.y));
						bullet.vars[0] = 2;
					}
				}
				
				
			}
		}
		
		int burstIntveral = 30;
		if(difficulty == "Lunatic")
			burstIntveral = 15;
		
		if(timer >= 600 &&  timer % burstIntveral == 0 && (difficulty == "Hard" || difficulty == "Lunatic"))
		{
			vars[0] = GameMath.getAngleTo(x, y, mainLevel.player.x, mainLevel.player.y);
			int randColorIndex = mainLevel.rand.nextInt(3);
			Color[] randColors = {new Color(0xFF0000), new Color(0x00FF00), new Color(0x0000FF)};
			
			for(double i = vars[0]; i <  vars[0] + 360; i += 20)
			{
				BulletSprite bullet = new BulletSprite(this.x, this.y, 'a', randColors[randColorIndex]);
				bullet.setVelocity(3, i);
				enlistBullet(bullet,bullets);
			}
		}
		
		
		
		if(timer > 700 && (difficulty == "Normal" || difficulty == "Easy"))
		{
			timer = 99;
		}
		
		if(timer > 760 && (difficulty == "Hard" || difficulty == "Lunatic"))
		{
			timer = 99;
		}
		
		
		LinkedList<BulletSprite> newBullets = new LinkedList<BulletSprite>();
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == 0)
			{
				if(difficulty == "Hard" || difficulty == "Lunatic")
				{
					if(this.collision(bullet) && bullet.vars[0] == 1)
					{
						BulletSprite oldBullet = bullet;
					
						bullet = new BulletSprite(oldBullet.x, oldBullet.y, 'a', new Color(0xAA22AA));
						bullet.setProjectileVelocity(1, mainLevel.rand.nextInt(90)+45, 0.01);
						bullet.aiMode = 3;
						newBullets.add(bullet);
						
						oldBullet.destroy();
					}
					if(bullet.vars[0] == 2)
					{
						bullet.dx *= 1.03;
						bullet.dy *= 1.03;
					}
				}
			}
			if(bullet.aiMode == -2)
			{	
				bullet.vars[2]++;
				if(bullet.vars[2] > 100)
					bullet.isActive = false;
				if(bullet.vars[2] > 110)
					bullet.destroy();
					
				if(x > -40 && x < mainLevel.SCREENW + 40 && y > -40 && y < mainLevel.SCREENH + 40)
				{
					bullet.x = this.x + bullet.vars[0];
					bullet.y = this.y + bullet.vars[1];
				}
			}
		}
		
		for(Object s : newBullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			enlistBullet(bullet, bullets);
		}
		
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
		this.x += this.vars[1]*GameMath.cos(this.vars[0]);
		this.y -= this.vars[1]*GameMath.sin(this.vars[0]);
	}
	
	
	private void createTrailingRainbow(SpriteList bullets)
	{
		double cos = GameMath.cos(vars[0]);
		double sin = GameMath.sin(0-vars[0]);
		
		for(int i = 0; i <= 360; i+=60)
		{
			int bulletColor = Color.HSBtoRGB((float)(i/360.0), 1.0f, 0.35f);
			
			int dx = 0;
			int dy = -60 + i/3;
			
			int tdx = (int) (cos*dx - sin*dy);
			int tdy = (int) (sin*dx + cos*dy);
			
			BulletSprite laser = new BulletSprite(this.x,this.y, 'l', new Color(bulletColor));
			laser.aiMode = -2;
			laser.isActive = true;
			laser.vars[0] = tdx;
			laser.vars[1] = tdy;
			laser.rotate(vars[0]+180);
			laser.screenKillable = false;
			enlistBullet(laser, bullets);
			
		}
	}
	
	private void createTrailingZoom(SpriteList bullets)
	{
		double cos = GameMath.cos(vars[0]);
		double sin = GameMath.sin(0-vars[0]);

		BulletSprite laser = new BulletSprite(this.x,this.y, 'l', new Color(0xCCCC00));
		laser.aiMode = -2;
		laser.isActive = true;
		laser.vars[0] = 0;
		laser.vars[1] = 0;
		laser.rotate(vars[0]+180);
		laser.screenKillable = false;
		enlistBullet(laser, bullets);
	}
	
	
	// nsc2
	
	private void nsc2(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[2] = 0;
			vars[3] = 0;
			isSlashing = false;
			//portrait.vars[2] = 10;
		}
		//portrait.spellCardPortrait();
		
		int interval = 10;
		if(difficulty == "Hard" || difficulty == "Lunatic")
			interval = 7;
		
		if(timer >= 50 && timer <= 100 && timer % interval == 0)
		{
			AKittehSatellite spawner = satellites[(int)(vars[2] % 3)];
			
			BulletSprite bullet = new BulletSprite(spawner.x,spawner.y,'i',new Color(0xAA0000));
			bullet.setVelocity(2.0,spawner.bigAngle);
			bullet.aiMode = -2;
			bullet.vars[0] = 0;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(spawner.x,spawner.y,'e',new Color(0xFF0000));
			bullet.setVelocity(0.5 + mainLevel.rand.nextDouble(),spawner.bigAngle+mainLevel.rand.nextInt(30)-15);
			bullet.aiMode = -2;
			bullet.scale(1.5,1.5);
			bullet.vars[0] = 1;
			enlistBullet(bullet, bullets);
			
			bullet = new BulletSprite(spawner.x,spawner.y,'e',new Color(0xFF0000));
			bullet.setVelocity(0.5 + mainLevel.rand.nextDouble(),spawner.bigAngle+mainLevel.rand.nextInt(30)-15);
			bullet.aiMode = -2;
			bullet.scale(1.5,1.5);
			bullet.vars[0] = 1;
			enlistBullet(bullet, bullets);
			
			vars[2]++;
		}
		
		if(timer == 100)
		{
			isSlashing = true;
			vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40) + 20;
			vars[1] = mainLevel.rand.nextInt(200) + 20;
		}
		
		if(timer == 200)
		{
			isSlashing = false;
			
			for(int i = 0; i < 360; i += 30)
			{
				for(int j= 0; j < 3; j++)
				{
					BulletSprite bullet = new BulletSprite(x,y,'m',new Color(255,50+100*j,0));
					bullet.setProjectileVelocity(1+0.5*j,i,0,0.01);
					bullet.aiMode = 3;
					enlistBullet(bullet,bullets);
				}
			}
		}
		if(timer == 230 && (difficulty == "Hard" || difficulty == "Lunatic"))
		{
			vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW - 40) + 20;
			vars[1] = mainLevel.rand.nextInt(200) + 20;
		}
		if(timer == 250 && (difficulty == "Hard" || difficulty == "Lunatic"))
		{
			for(int i = 15; i < 360+15; i += 30)
			{
				for(int j= 0; j < 3; j++)
				{
					BulletSprite bullet = new BulletSprite(x,y,'m',new Color(255,50+100*j,0));
					bullet.setProjectileVelocity(1+0.5*j,i,0,0.01);
					bullet.aiMode = 3;
					enlistBullet(bullet,bullets);
				}
			}
		}
		
		if(timer == 300)
			timer = 49;
		
		
		LinkedList<BulletSprite> newBullets = new LinkedList<BulletSprite>();
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -2)
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				int bounceLimit = 1;
				
				if(bullet.vars[3] < bounceLimit && (bullet.x < 0 || bullet.x > mainLevel.SCREENW))
				{
					bullet.vars[3]++;
					bullet.dx *= -1;
				}
					
				if(bullet.vars[3] < bounceLimit && (bullet.y < 0 || bullet.y > mainLevel.SCREENH))
				{
					bullet.vars[3]++;
					bullet.dy *= -1;
				}
				
				mainLevel.quadTree.insert(bullet);
			}
		}
		
		ArrayList<Sprite> cList = mainLevel.quadTree.query(this);
			
		for(Sprite s : cList)
		{
			if(s instanceof BulletSprite)
			{
				BulletSprite bullet = (BulletSprite) s;
				if(bullet.aiMode == -2)
				{
					if(this.isSlashing && bullet.collision(this) && bullet.vars[0] == 0)
					{
						BulletSprite oldBullet = bullet;
						
						for(int i = 0; i < 360; i+=40)
						{
							for(int j=0; j < 3; j++)
							{
								bullet = new BulletSprite(oldBullet.x,oldBullet.y,'d',new Color(0xAA0088));
							//	bullet.setVelocity(1+0.2*j,i+10*j);
								bullet.setRotationalVelocity(1+0.2*j,i+10*j,3,0.98);
								bullet.aiMode = 2;
								newBullets.add(bullet);
								
								if(difficulty == "Lunatic")
								{
									bullet = new BulletSprite(oldBullet.x,oldBullet.y,'d',new Color(0xAA0088));
								//	bullet.setVelocity(1.5+0.2*j,i+10*j);
									bullet.setRotationalVelocity(1.5+0.2*j,i+10*j,3,0.98);
									bullet.aiMode = 2;
									newBullets.add(bullet);
								}
							}
						}
						oldBullet.destroy();
					}
					
					if(this.isSlashing && bullet.collision(this) && bullet.vars[0] == 1)
					{
						BulletSprite oldBullet = bullet;
						
						for(int i = 0; i < 360; i+=40)
						{
							for(int j=0; j < 3; j++)
							{
								bullet = new BulletSprite(oldBullet.x,oldBullet.y,'a',new Color(0x550088));
							//	bullet.setVelocity(1.4+0.2*j,i+10*j);
								bullet.setRotationalVelocity(1.4+0.2*j,i+10*j,3,0.98);
								bullet.aiMode = 2;
								newBullets.add(bullet);
							}
						}
						oldBullet.destroy();
					}
				}
			}
		}
		
		for(Object s : newBullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			enlistBullet(bullet, bullets);
		}
		
		unEnlistDeadBullets();
		
		
		// movement
		if(Math.abs(x-vars[0]) < 1)
			this.setDirection('S');
		else
			this.setDirection('E');
		this.x += (this.vars[0] - this.x)/10.0;
		this.y += (this.vars[1] - this.y)/10.0;
	}
	
	
	// SWORD OF POWAH!!! ~ I can has Omens?
	
	private void swordOfPowah(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = 45;
			vars[3] = 0;
			vars[4] = 0;
			isSlashing = false;
			

			spellCardName = "SWORD of POWAH!!! ~ Blade of Ominous Foresight";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		if(timer <= 100)
		{
			this.vars[0] = mainLevel.SCREENW/2;
			this.vars[1] = 150;
		}
		else
		{
			int burstPeriod = 60;
			int burstSpacing = 15;
			
			int burstMinSpeed = 1;
			double burstMaxSpeed = 1.5;
			
			
			if(difficulty == "Easy")
			{
				burstPeriod = 80;
				burstSpacing = 20;
			}
			
			
			if(timer % burstPeriod == 0)
			{
				for(int i = 0; i < 360; i+= burstSpacing)
				{
					double alpha = Math.max(0,Math.abs(i - 180)/180.0);
					alpha *= alpha*alpha;
					
					int highColor = (int) Math.min(255, 220*alpha + (1-alpha)*50);
					int lowColor = highColor/5;
					
					BulletSprite bullet = new BulletSprite(x,y,'b',new Color(highColor,lowColor,0));
					bullet.setVelocity(burstMinSpeed + burstMaxSpeed*alpha,i + this.vars[2]);
					enlistBullet(bullet,bullets);
					
					bullet = new BulletSprite(x,y,'b',new Color(highColor,lowColor,0));
					bullet.setVelocity(burstMinSpeed + burstMaxSpeed*alpha,i+180 + this.vars[2]);
					enlistBullet(bullet,bullets);
				}
			}
			
			if(timer % burstPeriod == burstPeriod / 2)
			{
				for(int i = 0; i < 360; i+= burstSpacing)
				{
					double alpha = Math.max(0,Math.abs(i - 180)/180.0);
					alpha *= alpha*alpha;
					
					int highColor = (int) Math.min(255, 220*alpha + (1-alpha)*50);
					int lowColor = highColor/5;
					
					BulletSprite bullet = new BulletSprite(x,y,'b',new Color(0,lowColor,highColor));
					bullet.setVelocity(burstMinSpeed + burstMaxSpeed*alpha,i + this.vars[2] + 90);
					enlistBullet(bullet,bullets);
					
					bullet = new BulletSprite(x,y,'b',new Color(0,lowColor,highColor));
					bullet.setVelocity(burstMinSpeed + burstMaxSpeed*alpha,i+ 270 + this.vars[2]);
					enlistBullet(bullet,bullets);
				}
				
				this.vars[2] += 13;
			}
			
			this.vars[4] += 1;
			
			int laserTimes = 60;
			if(difficulty == "Easy")
				laserTimes = 45;
			if(difficulty == "Hard")
				laserTimes = 75;
			
			if(this.vars[4] > 100 && this.vars[4] % 15 == 0)
			{
				int laserStartX = mainLevel.rand.nextInt(mainLevel.SCREENW);
				
				BulletSprite laser = new BulletSprite(laserStartX,-20, 'l', new Color(0x222288));
				
				int laserAngleOffset = 0;
				if(difficulty == "Hard")
					laserAngleOffset = mainLevel.rand.nextInt(20) - 10;
				if(difficulty == "Lunatic")
					laserAngleOffset = mainLevel.rand.nextInt(30) - 15;
				
				double angleToPlayer = GameMath.getAngleTo(laser.x, laser.y, mainLevel.player.x, mainLevel.player.y) + laserAngleOffset;
				
				laser.setVelocity(0.2,angleToPlayer);
				laser.rotate(laser.theta + 180);
				laser.aiMode = -1;
				laser.isActive = true;
				laser.screenKillable = false;
				enlistBullet(laser, bullets);
			}
			if(this.vars[4] > 100 + laserTimes)
			{
				this.vars[4] = 0;
				if(difficulty == "Lunatic")
					this.vars[4] = 50;
			}
			
			this.vars[3] += 1;
			
			this.vars[0] = mainLevel.SCREENW/2 + 40*GameMath.cos(this.vars[3]);
			this.vars[1] = 150 + 20*GameMath.sin(this.vars[3]*2);
		}
		
		
		for(Object s : bullets)
		{
			BulletSprite bullet = (BulletSprite) s;
			
			if(bullet.aiMode == -1)
			{
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				bullet.dx *= 1.03;
				bullet.dy *= 1.03;
				
				if(bullet.y > mainLevel.SCREENH + 20)
					bullet.aiMode = -2;
				
				if(bullet.dx > 0 && bullet.x > mainLevel.SCREENW + 20)
					bullet.aiMode = -2;
					
				if(bullet.dx < 0 && bullet.x < -20)
					bullet.aiMode = -2;
			}
			if(bullet.aiMode == -2)
			{
				bullet.vars[0]++;
				
				if(bullet.vars[0] > 40)
					bullet.isActive = false;
				if(bullet.vars[0] > 60)
					bullet.destroy();
			}
		}
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
		this.x += (this.vars[0] - this.x)/20.0;
		this.y += (this.vars[1] - this.y)/20.0;
	}
	
	
	private void nsc01(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = 0;
			vars[1] = 0;
			vars[2] = 45;
			vars[3] = 0;
			isSlashing = false;
			

			spellCardName = "SWORD of POWAH!!! ~ I can haz omens?";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		
		if(timer <= 100)
		{
			this.vars[0] = mainLevel.SCREENW/2;
			this.vars[1] = 150;
		}
		else
		{
			int burstPeriod = 80;
			int burstSpacing = 20;
			
			if(timer % burstPeriod == 0)
			{
				for(int i = 0; i < 360; i+= burstSpacing)
				{
					double alpha = GameMath.sin(i/2.0);
					alpha *= alpha;
					
					BulletSprite bullet = new BulletSprite(x,y,'b',new Color((int)(220*alpha + (1-alpha)*50),(int)(50*alpha),0));
					bullet.setVelocity(0.2+1*alpha,i + this.vars[2]);
					enlistBullet(bullet,bullets);
					
					bullet = new BulletSprite(x,y,'b',new Color((int)(220*alpha + (1-alpha)*50),(int)(50*alpha),0));
					bullet.setVelocity(0.2+1*alpha,i+180 + this.vars[2]);
					enlistBullet(bullet,bullets);
				}
			}
			
			if(timer % burstPeriod == burstPeriod / 2)
			{
				for(int i = 0; i < 360; i+= burstSpacing)
				{
					double alpha = GameMath.sin(i/2.0);
					alpha *= alpha;
					
					BulletSprite bullet = new BulletSprite(x,y,'b',new Color(0,(int)(50*alpha), (int)(220*alpha + (1-alpha)*50)));
					bullet.setVelocity(0.2+1*alpha,i + this.vars[2] + 90);
					enlistBullet(bullet,bullets);
					
					bullet = new BulletSprite(x,y,'b',new Color(0,(int)(50*alpha),(int)(220*alpha + (1-alpha)*50)));
					bullet.setVelocity(0.2+1*alpha,i+ 270 + this.vars[2]);
					enlistBullet(bullet,bullets);
				}
			}
		}
		
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
		this.x += (this.vars[0] - this.x)/20.0;
		this.y += (this.vars[1] - this.y)/20.0;
	}
	
	
		
}







class AKittehSatellite extends Sprite
{
	public int id;
	public BossSprite boss;
	public double smallAngle, bigAngle;
	
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	
	public AKittehSatellite(double x, double y, int idd, BossSprite aKitteh)
	{
		super(x,y);

		id = idd;
		boss = aKitteh;
		
		bigAngle = 120* id;
		smallAngle = 0;
	}
	
	public static void loadImages(ImageLoader il)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		Image img;
		
		// load our images
		
		imageURL = AKittehSatellite.class.getResource("graphics/SatellitesSheet.png");
		Image satSheet = tk.getImage(imageURL);
		satSheet = ColorFilters.setTransparentColor(satSheet, new Color(0xFF00FF));
		
		img = ImageBlitter.crop(satSheet,1,1,31,31);
		il.addImage(img);
		focusTable.put("satellite",new Point(16,16));
		imageTable.put("satellite",img);
		
		System.out.println("loaded image data for AKittehSatellite");
	}
	
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		
		// movement
		
		x = 80*GameMath.cos(smallAngle);
		y = -40*GameMath.sin(smallAngle);
		
		x = x*GameMath.cos(bigAngle) - y*GameMath.sin(0-bigAngle);
		y = x*GameMath.sin(0-bigAngle) + y*GameMath.cos(bigAngle);
		
		x += boss.x;
		y += boss.y;
		
		this.rotate(smallAngle);
		double sc = 0.75 + GameMath.cos(smallAngle*(id+1))*0.25;
		this.scale(sc,sc);
		this.setSemiTransparency(0.5);
		
		smallAngle += 5;
		bigAngle += 3;
		
		// Animate current frame
		
		String imgKey = "satellite";
		curImage = imageTable.get(imgKey);
		Point curFocus = focusTable.get(imgKey);
		fx = curFocus.x;
		fy = curFocus.y;
	}
}






