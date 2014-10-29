
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



public class BossSmiley extends BossSprite
{
	private static int numInstances = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public boolean isRoaring = false;
	public int roarTimer = 0;
	
	// enemy vars
	
	int hoverAngle;
	
	// CONSTRUCTOR
	
	public BossSmiley(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(45);
		this.width = 104;
		this.height = 100;
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
		
		// Spinning
		
		imageURL = EnemySprite.class.getResource("graphics/Midboss3Sheet.png");
		Image bigSmileySheet = tk.getImage(imageURL);
		bigSmileySheet = ColorFilters.setTransparentColor(bigSmileySheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(bigSmileySheet,1,1,104,100);
		il.addImage(img);
		focusTable.put("spin1",new Point(52,50));
		imageTable.put("spin1",img);
		
		img = ImageBlitter.crop(bigSmileySheet,107,1,104,100);
		il.addImage(img);
		focusTable.put("spin2",new Point(52,50));
		imageTable.put("spin2",img);
		
		img = ImageBlitter.crop(bigSmileySheet,213,1,104,100);
		il.addImage(img);
		focusTable.put("spin3",new Point(52,50));
		imageTable.put("spin3",img);
		
		img = ImageBlitter.crop(bigSmileySheet,319,1,104,100);
		il.addImage(img);
		focusTable.put("spin4",new Point(52,50));
		imageTable.put("spin4",img);
		
		img = ImageBlitter.crop(bigSmileySheet,425,1,104,100);
		il.addImage(img);
		focusTable.put("spin5",new Point(52,50));
		imageTable.put("spin5",img);
		
		img = ImageBlitter.crop(bigSmileySheet,1,103,104,100);
		il.addImage(img);
		focusTable.put("spin6",new Point(52,50));
		imageTable.put("spin6",img);
		
		img = ImageBlitter.crop(bigSmileySheet,107,103,104,100);
		il.addImage(img);
		focusTable.put("spin7",new Point(52,50));
		imageTable.put("spin7",img);
		
		img = ImageBlitter.crop(bigSmileySheet,213,103,104,100);
		il.addImage(img);
		focusTable.put("spin8",new Point(52,50));
		imageTable.put("spin8",img);
		
		img = ImageBlitter.crop(bigSmileySheet,319,103,104,100);
		il.addImage(img);
		focusTable.put("spin9",new Point(52,50));
		imageTable.put("spin9",img);
		
		img = ImageBlitter.crop(bigSmileySheet,425,103,104,100);
		il.addImage(img);
		focusTable.put("spin10",new Point(52,50));
		imageTable.put("spin10",img);
		
		img = ImageBlitter.crop(bigSmileySheet,1,205,104,100);
		il.addImage(img);
		focusTable.put("roar",new Point(52,50));
		imageTable.put("roar",img);
		
		System.out.println("loaded image data for BossSmiley");
		
	}
	
	// LOGIC METHODS
	
	
	
	
	
	// RENDERING METHODS
	
	protected void draw(Graphics2D g)
	{
		if(isRoaring)
			g.translate(mainLevel.rand.nextInt(11)-5,mainLevel.rand.nextInt(11)-5);
		else
			g.translate(3*GameMath.cos(hoverAngle),3*GameMath.sin(hoverAngle));
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
		
		int spd = 3;
		String curImgKey;
		
		if(isRoaring && frameNum < spd)
		{
			curImgKey = "roar";
			if(roarTimer == 0)
			{
				mainLevel.soundPlayer.play("sound/monster.wav");
				roarTimer = 100;
			}
			roarTimer--;
		}
		else
		{
			roarTimer = 0;
			curImgKey = "spin";
			int imgNum = 1;
			
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
			this.frameNum++;
			if(isRoaring)
				this.frameNum+=spd-1;
			if(this.frameNum >= 13*spd)
				this.frameNum = 0;
		}
		curImage = imageTable.get(curImgKey);
		
		Point curFocus = focusTable.get(curImgKey);
		fx = curFocus.x;
		fy = curFocus.y;

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
			nsc2(bullets,slaves,sfx);
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
			hp = 800;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[2] = 0;
			
			spellCardName = "";
		}
		
		double angleToPlayer = GameMath.getAngleTo(this.x, this.y, (mainLevel.player.x + this.x)/2, (mainLevel.player.y +this.y)/2);
		
		if(timer == 1)
		{
			isRoaring = true;
		}
		if(timer < 100 && timer % 4 == 0)
		{
			vars[2] += 13;
			double alpha = timer/100.0;
			
			for(double i = vars[2]; i <vars[2] + 360; i+=120)
			{
				double speed = (1-alpha)*4 + (alpha)*1.5;
				BulletSprite bullet = new BulletSprite(this.x+GameMath.cos(i)*52,this.y-GameMath.sin(i)*52, 'b', new Color(0xFF00CC));
				bullet.setVelocity(speed, i);
				enlistBullet(bullet, bullets);
				
				bullet = new BulletSprite(this.x+GameMath.cos(0-i)*52,this.y-GameMath.sin(0-i)*52, 'b', new Color(0xFF00CC));
				bullet.setVelocity(speed, 0-i);
				enlistBullet(bullet, bullets);
				
				if(difficulty == "Hard" || difficulty == "Lunatic") {
					int bulletAngleOffset = 2;
					bullet = new BulletSprite(this.x+GameMath.cos(i+bulletAngleOffset)*52,this.y-GameMath.sin(i+bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
					bullet.setVelocity(speed*0.95, i+bulletAngleOffset);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(this.x+GameMath.cos(0-i-bulletAngleOffset)*52,this.y-GameMath.sin(0-i-bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
					bullet.setVelocity(speed*0.95, 0-i-bulletAngleOffset);
					enlistBullet(bullet, bullets);
					
					bulletAngleOffset = 4;
					
					bullet = new BulletSprite(this.x+GameMath.cos(i+bulletAngleOffset)*52,this.y-GameMath.sin(i+bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
					bullet.setVelocity(speed*0.9, i+bulletAngleOffset);
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(this.x+GameMath.cos(0-i-bulletAngleOffset)*52,this.y-GameMath.sin(0-i-bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
					bullet.setVelocity(speed*0.9, 0-i-bulletAngleOffset);
					enlistBullet(bullet, bullets);
					
					if(difficulty == "Lunatic") {
						bulletAngleOffset = 6;
					
						bullet = new BulletSprite(this.x+GameMath.cos(i+bulletAngleOffset)*52,this.y-GameMath.sin(i+bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
						bullet.setVelocity(speed*0.85, i+bulletAngleOffset);
						enlistBullet(bullet, bullets);
						
						bullet = new BulletSprite(this.x+GameMath.cos(0-i-bulletAngleOffset)*52,this.y-GameMath.sin(0-i-bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
						bullet.setVelocity(speed*0.85, 0-i-bulletAngleOffset);
						enlistBullet(bullet, bullets);
						
						bulletAngleOffset = 7;
					
						bullet = new BulletSprite(this.x+GameMath.cos(i+bulletAngleOffset)*52,this.y-GameMath.sin(i+bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
						bullet.setVelocity(speed*0.8, i+bulletAngleOffset);
						enlistBullet(bullet, bullets);
						
						bullet = new BulletSprite(this.x+GameMath.cos(0-i-bulletAngleOffset)*52,this.y-GameMath.sin(0-i-bulletAngleOffset)*52, 'b', new Color(0xFF00CC));
						bullet.setVelocity(speed*0.8, 0-i-bulletAngleOffset);
						enlistBullet(bullet, bullets);
					}
				}
			}
		}
		if(timer == 100)
		{
			isRoaring = false;
		}
		
		String sound = "sound/Bart gun.wav";
		
		if(timer == 140)
		{
			mainLevel.soundPlayer.play(sound);
			for(double i = vars[2]; i <vars[2] + 360; i+=20)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
				bullet.setVelocity(2, i);
				bullet.isInverted = true;
				enlistBullet(bullet, bullets);
				
				if(difficulty == "Hard") {
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.6, i+180);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Lunatic") {
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.6, i+120);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.8, i-120);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
				}
			}
			
			this.vars[0] = (mainLevel.SCREENW/2) + 100*GameMath.cos(90+360/5*2);
			this.vars[1] = 100 - 50*GameMath.sin(90+360/5*2);
		}
		
		if(timer == 150)
		{
			mainLevel.soundPlayer.play(sound);
			for(double i = vars[2]; i <vars[2] + 360; i+=20)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
				bullet.setVelocity(2, i);
				bullet.isInverted = true;
				enlistBullet(bullet, bullets);
			}
			
			this.vars[0] = (mainLevel.SCREENW/2) + 100*GameMath.cos(90+360/5*4);
			this.vars[1] = 100 - 50*GameMath.sin(90+360/5*4);
		}
		
		if(timer == 160)
		{
			mainLevel.soundPlayer.play(sound);
			for(double i = vars[2]; i <vars[2] + 360; i+=20)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
				bullet.setVelocity(2, i);
				bullet.isInverted = true;
				enlistBullet(bullet, bullets);
				
				if(difficulty == "Hard") {
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.6, i+180);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Lunatic") {
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.6, i+120);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.8, i-120);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
				}
			}
			
			this.vars[0] = (mainLevel.SCREENW/2) + 100*GameMath.cos(90+360/5*1);
			this.vars[1] = 100 - 50*GameMath.sin(90+360/5*1);
		}
		
		if(timer == 170)
		{
			mainLevel.soundPlayer.play(sound);
			for(double i = vars[2]; i <vars[2] + 360; i+=20)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
				bullet.setVelocity(2, i);
				bullet.isInverted = true;
				enlistBullet(bullet, bullets);
			}
			
			this.vars[0] = (mainLevel.SCREENW/2) + 100*GameMath.cos(90+360/5*3);
			this.vars[1] = 100 - 50*GameMath.sin(90+360/5*3);
		}
		
		if(timer == 180)
		{
			mainLevel.soundPlayer.play(sound);
			for(double i = vars[2]; i <vars[2] + 360; i+=20)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
				bullet.setVelocity(2, i);
				bullet.isInverted = true;
				enlistBullet(bullet, bullets);
				
				if(difficulty == "Hard") {
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.6, i+180);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
				}
				
				if(difficulty == "Lunatic") {
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.6, i+120);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(this.x,this.y, 'd', new Color(0x557700));
					bullet.setVelocity(1.8, i-120);
					bullet.isInverted = true;
					enlistBullet(bullet, bullets);
				}
			}
			
			this.vars[0] = (mainLevel.SCREENW/2) + 100*GameMath.cos(90);
			this.vars[1] = 100 - 50*GameMath.sin(90);
		}
		
		
		if(timer >= 190)
		{
			this.vars[0] = mainLevel.SCREENW/2;
			this.vars[1] = 100;
		}
		if(timer >= 250)
		{
			timer = 0;
		}
		
		unEnlistDeadBullets();
		
		// movement
			
		this.x += (this.vars[0] - this.x)/5.0;
		this.y += (this.vars[1] - this.y)/5.0;
	}
	
	
	
	
	// NSC 2
	
	private void nsc2(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 800;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 45;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = 100;
			vars[2] = 0;
			
			isRoaring = false;
			
			spellCardName = "";
		//	portrait.vars[2] = 10;
		}
	//	portrait.spellCardPortrait();
		
		if(timer == 1 || slaves.size() == 0)
		{
			int enemySpacing = 90;
			
			if(difficulty == "Easy")	enemySpacing = 120;
			if(difficulty == "Hard" || difficulty == "Lunatic")	enemySpacing = 72;
			
			for(int i = 0; i < 360; i+= enemySpacing) {
				EnemySprite smiley = new EnemySprite(x,y,'c',200);
				smiley.skinID = 2;
				smiley.vars[0] = i;
				enlistBullet(smiley, slaves);
			}
		}
		
		if(timer < 60)
		{
			vars[2]++;
		}
		
		if(timer < 90 && difficulty == "Lunatic")	vars[2]++;
		if(timer == 122 || timer == 182)
		{
			isRoaring = true;
			
			
			double angleToPlayer = GameMath.getAngleTo(this.x, this.y, (mainLevel.player.x + this.x)/2, (mainLevel.player.y +this.y)/2);
			
			for(double i = angleToPlayer; i < angleToPlayer + 360;i+=10)
			{
				BulletSprite bullet = new BulletSprite(this.x,this.y, 'a', new Color(0x557700));
				bullet.setVelocity(1.5,i);
				bullet.isInverted = true;
				enlistBullet(bullet, bullets);
			}
		}
		
		if(timer > 240)
			timer -= 120;
		
		
		if(timer >= 60 && timer %10 == 0)
		{
			mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
		}
		
		for(Object s : slaves)
		{
			EnemySprite minion = (EnemySprite) s;
			
			if(minion.aiType == 0)
			{
				minion.x = this.x + this.vars[2]*GameMath.cos(minion.vars[0]);
				minion.y = this.y - this.vars[2]*GameMath.sin(minion.vars[0]);
				
				minion.vars[0]++;
				
				if(timer >= 60 && timer % 10 == 0)
				{
					
					
					BulletSprite bullet = new BulletSprite(minion.x,minion.y, 'b', new Color(0xFF00CC));
					bullet.setRotationalVelocity(2.5, vars[0] + timer,4,0.94);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(minion.x,minion.y, 'b', new Color(0xFF00CC));
					bullet.setRotationalVelocity(2.5, vars[0] + timer - 120,4,0.94);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
					
					bullet = new BulletSprite(minion.x,minion.y, 'b', new Color(0xFF00CC));
					bullet.setRotationalVelocity(2.5, vars[0] + timer + 120,4,0.94);
					bullet.aiMode = 2;
					enlistBullet(bullet, bullets);
				}
			}
			
		}
		
		unEnlistDeadBullets();
		
		
		// movement
			
		this.x += (this.vars[0] - this.x)/5.0;
		this.y += (this.vars[1] - this.y)/5.0;
	}
	
	
	
}
