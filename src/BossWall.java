
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



public class BossWall extends BossSprite
{
	private static int numInstances = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	// enemy vars

	private int blinkTimer = 0;
	
	AKittehSatellite[] satellites = new AKittehSatellite[3];
	
	
	// CONSTRUCTOR
	
	public BossWall(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(20);
		this.frameNum = 0;
		this.lastAttackIndex = 6;
		portrait = new PortraitWallSprite(m.SCREENW +100,300,"Wall Fourthward ~ Gravity Hobo beyond ~ Time and Space");
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
		
		imageURL = BossWall.class.getResource("graphics/WallForthSheet.png");
		Image slushSheet = tk.getImage(imageURL);
		slushSheet = ColorFilters.setTransparentColor(slushSheet, new Color(0xFF00FF));
		
		// Front
		
		img = ImageBlitter.crop(slushSheet,1,1,33,38);
		il.addImage(img);
		focusTable.put("WallS1",new Point(16,18));
		imageTable.put("WallS1",img);
		
		img = ImageBlitter.crop(slushSheet,36,1,33,38);
		il.addImage(img);
		focusTable.put("WallS2",new Point(16,18));
		imageTable.put("WallS2",img);
		
		img = ImageBlitter.crop(slushSheet,71,1,33,38);
		il.addImage(img);
		focusTable.put("WallS3",new Point(16,18));
		imageTable.put("WallS3",img);
		
		img = ImageBlitter.crop(slushSheet,36,41,33,38);
		il.addImage(img);
		focusTable.put("WallS2blink",new Point(16,18));
		imageTable.put("WallS2blink",img);
		
		// west
		
		img = ImageBlitter.crop(slushSheet,1,41,33,38);
		il.addImage(img);
		focusTable.put("WallW",new Point(16,18));
		imageTable.put("WallW",img);
		
		// east
		
		img = ImageBlitter.crop(slushSheet,71,41,33,38);
		il.addImage(img);
		focusTable.put("WallE",new Point(16,18));
		imageTable.put("WallE",img);
		
		System.out.println("loaded image data for BossWall");
		
		PortraitWallSprite.loadImages(il);
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
		String imgKey = "Wall" + this.direction;
		setRadius(20);
		width = 33;
		height = 38;
		
		if(this.direction == 'S') {
			int imgNum = 1;
			
			if(frameNum > 10)
				imgNum = 2;
			if(frameNum > 15)
				imgNum = 3;
			if(frameNum > 25)
				imgNum = 2;

			imgKey += imgNum;
			
			if(imgNum == 2)
			{
				if(blinkTimer == 0)
					imgKey += "blink";
				else
					blinkTimer--;
			}
			if(imgNum == 2 && blinkTimer == 0)
				blinkTimer = mainLevel.rand.nextInt(25)+10;
			
			frameNum++;
			if(frameNum > 30)
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
		setSemiTransparency(0.0);
		if(attackIndex == 0) // nsc0
		{	
			stageBG.nextBGID = 2;
			nsc0(bullets,slaves,sfx); // bigCrunch(bullets,slaves,sfx); // 
		}
		if(attackIndex == 1) // Gravitational Pulse
		{	
			stageBG.nextBGID = 3;
			gravityPulse(bullets,slaves,sfx);
		}
		if(attackIndex == 2) // Orbital Slingshot
		{	
			stageBG.nextBGID = 2;
			orbitalSlingshot(bullets,slaves,sfx);
		}
		if(attackIndex == 3) // Gravitational Pulse
		{	
			stageBG.nextBGID = 4;
			bigCrunch(bullets,slaves,sfx);
		}
		if(attackIndex == 4) // nsc1 or nsc2
		{	
			stageBG.nextBGID = 2;
      if(difficulty == "Hard" || difficulty == "Lunatic") 
        nsc2(bullets,slaves,sfx);
      else
        nsc1(bullets,slaves,sfx);
		}
		if(attackIndex == 5)
		{
			stageBG.nextBGID = 5;
			singularityWell(bullets,slaves,sfx);
		}
		if(attackIndex == 6) // Ringed Giants
		{	
			stageBG.nextBGID = 5;
			ringedGiants(bullets,slaves,sfx);
		}
		
		// set health bar values and spell card text

		portrait.text = spellCardName;
		mainLevel.barBossHealth.caption2 = spellCardName;
		
		// increment timer
		
		timer++;
	}

	
	
	/**
	 * Rings of meteors and asteroids.
	 */
	private void nsc0(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
		}
		
		x += (vars[0] - x)/20.0;
		y += (vars[1] - y)/20.0;
		
		if(timer % 15 == 0) {
			mainLevel.soundPlayer.play("sound/WAVE03.WAV");
			BulletSprite bullet;
			int initAngle = mainLevel.rand.nextInt(360);
			double bulletSpd = 2;
			double rotSpd = (mainLevel.rand.nextDouble()*3+2)*(mainLevel.rand.nextInt(2)-0.5)*2;
			
			Color bulletColor = new Color(mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50));
			
			int spacing = 25;
			if(difficulty == "Easy") spacing = 40;
			if(difficulty == "Lunatic") spacing = 20;
			
			for(int i = 0; i < 360; i+= spacing) {
				bullet = new BulletSprite(this.x,this.y, 'd', bulletColor);
				bullet.setRotationalVelocity(bulletSpd, initAngle + i, rotSpd, 0.98);
				bullet.aiMode = 2;
				enlistBullet(bullet, bullets);
			}
		}
		
		if(timer % 5 == 0) {
			BulletSprite bullet;
			int initAngle = mainLevel.rand.nextInt(360);
			double bulletSpd = 1.5;
			double rotSpd = (mainLevel.rand.nextDouble()*3+2)*(mainLevel.rand.nextInt(2)-0.5)*2;
			
			Color bulletColor = new Color(mainLevel.rand.nextInt(80),mainLevel.rand.nextInt(80),mainLevel.rand.nextInt(80));

			bullet = new BulletSprite(this.x,this.y, 'd', bulletColor);
			bullet.scale(2,2);
			bullet.setRotationalVelocity(bulletSpd, initAngle, rotSpd, 0.98);
			bullet.aiMode = 2;
			enlistBullet(bullet, bullets);
			
		}
		
		if((timer % 40 == 0 && difficulty == "Hard") || (timer % 25 == 0 && difficulty == "Lunatic")) {
			BulletSprite bullet;
			int initAngle = mainLevel.rand.nextInt(360);
			
			Color bulletColor = new Color(mainLevel.rand.nextInt(80),mainLevel.rand.nextInt(80),mainLevel.rand.nextInt(80));

			bullet = new BulletSprite(this.x, this.y, 'i', bulletColor);
			bullet.setVelocity(4.0, initAngle);
			bullet.aiMode = -1;
			enlistBullet(bullet, bullets);
		}
		
		
		for(Object o : bullets) { // only hard/lunatic large aimed bullets
			BulletSprite bullet = (BulletSprite) o;
			
			if(bullet.aiMode == -1) { 
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				bullet.dx *= 0.95;
				bullet.dy *= 0.95;
				
				bullet.vars[0]++;
				
				if(bullet.vars[0] > 60) {
					double angleToPlayer = GameMath.getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
					bullet.setVelocity(2.5, angleToPlayer);
					bullet.aiMode = 0;
				}
			}
		}
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
	}
	
	
	/** 
	 * Gravitational Pulse ~ 10 g/ Gravitational Pulse ~ 18 g/ Gravitational Pulse ~ 47 g/ Gravitational Pulse ~ 100 g/
	 * Local gravity is greatly increased. 
	 * Watch out for falling debris! 
	 */
	private void gravityPulse(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 1000;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			
			if(difficulty == "Easy") spellCardName = "Gravity Pulse ~ 10 g";
			if(difficulty == "Normal") spellCardName = "Gravity Pulse ~ 18 g";
			if(difficulty == "Hard") spellCardName = "Gravity Pulse ~ 47 g";
			if(difficulty == "Lunatic") spellCardName = "Gravity Pulse ~ 100 g";
			portrait.vars[2] = 10;
			mainLevel.player.vars[9] = 0;
			
			mainLevel.soundPlayer.play("sound/WAVE09.WAV");
		}
		portrait.spellCardPortrait();
		
		// gravity
		if(mainLevel.keyboard.isPressed(mainLevel.config.VK_UP)) {
			mainLevel.player.vars[9] -= 0.3;
		}
		mainLevel.player.vars[9] += 0.2;
		
		if(mainLevel.player.vars[9] > 10) mainLevel.player.vars[9] = 10;
		if(mainLevel.player.vars[9] < 0) mainLevel.player.vars[9] = 0;
		
		mainLevel.player.y += mainLevel.player.vars[9];
		if(mainLevel.player.y > mainLevel.SCREENH) {
			mainLevel.player.y = mainLevel.SCREENH;
			mainLevel.player.vars[9] = 0;
		}
		mainLevel.pHitBox.y = mainLevel.player.y;
		
		if(timer % 120 == 0) mainLevel.soundPlayer.play("sound/WAVE09.WAV");
		if(timer >= 100) {
			if(timer % 60 == 0) {
				this.vars[0] += mainLevel.rand.nextInt(180)-90;
				this.vars[1] += mainLevel.rand.nextInt(40)-20;
				
				if(this.vars[0] < 0) vars[0] = 0;
				if(this.vars[0] > mainLevel.SCREENW) vars[0] = mainLevel.SCREENW;
				if(this.vars[1] < 20) vars[1] = 20;
				if(this.vars[1] > 200) vars[1] = 200;
			}
			
			int period = 50;
			if(difficulty == "Easy") period = 70;
			if(difficulty == "Lunatic") period = 35;
			
			if(timer % period == 0) {
				int offAngle = mainLevel.rand.nextInt(20)-10;
				
				EnemySprite spawner;
				spawner = new EnemySprite(x,y, 'e',9999);
				spawner.aiType = -1;
				spawner.vars[0] = offAngle;
				spawner.vars[1] = 2*GameMath.cos(spawner.vars[0]);
				spawner.vars[2] = -2*GameMath.sin(spawner.vars[0]);
				
				enlistBullet(spawner,slaves);
			}
			
			if(timer % period == period/2) {
				int offAngle = mainLevel.rand.nextInt(20)-10 + 180;
				
				EnemySprite spawner;
				spawner = new EnemySprite(x,y, 'e',9999);
				spawner.aiType = -1;
				spawner.vars[0] = offAngle;
				spawner.vars[1] = 2*GameMath.cos(spawner.vars[0]);
				spawner.vars[2] = -2*GameMath.sin(spawner.vars[0]);
				
				enlistBullet(spawner,slaves);
			}
		}
		
		// On Hard and Lunatic, lasers cover the bottom.
		if(difficulty == "Hard" || difficulty == "Lunatic") {
			if(timer % 100 == 0) {
				EnemySprite spawner;
				spawner = new EnemySprite(x,y, 'e',9999);
				spawner.aiType = -2;
				spawner.vars[0] = 0;
				spawner.vars[1] = mainLevel.SCREENH;
				spawner.vars[2] = 0;
				
				enlistBullet(spawner,slaves);
			}
			if(timer % 100 == 50) {
				EnemySprite spawner;
				spawner = new EnemySprite(x,y, 'e',9999);
				spawner.aiType = -2;
				spawner.vars[0] = mainLevel.SCREENW;
				spawner.vars[1] = mainLevel.SCREENH;
				spawner.vars[2] = 180;
				
				enlistBullet(spawner,slaves);
			}
		}
		
		for(Object o : slaves) {
			EnemySprite spawner = (EnemySprite) o;
			
			if(spawner.aiType == -1) { // normal spawners
				if(spawner.x < -100 || spawner.x > mainLevel.SCREENW + 100) spawner.destroy();
				spawner.x += spawner.vars[1];
				spawner.y += spawner.vars[2];
				BulletSprite bullet;
				if(spawner.vars[3] % 10 == 0) {
					if(difficulty == "Easy" || difficulty == "Normal") {
						bullet = new BulletSprite(spawner.x,spawner.y, 'c', new Color(mainLevel.rand.nextInt(100),0,mainLevel.rand.nextInt(200)+50));
						bullet.scale(1.3,0.8);
					}
					else {
						bullet = new BulletSprite(spawner.x,spawner.y, 'd', new Color(mainLevel.rand.nextInt(100),0,mainLevel.rand.nextInt(200)+50));
						bullet.scale(1.5,0.5);
					}
					bullet.setVelocity(mainLevel.rand.nextDouble()*2.5+0.5, spawner.vars[0]+90);
					
					enlistBullet(bullet, bullets);
				}
				if(spawner.vars[3] % 10 == 5) {
					if(difficulty == "Easy" || difficulty == "Normal") {
						bullet = new BulletSprite(spawner.x,spawner.y, 'c', new Color(mainLevel.rand.nextInt(100),0,mainLevel.rand.nextInt(200)+50));
						bullet.scale(1.3,0.8);
					}
					else {
						bullet = new BulletSprite(spawner.x,spawner.y, 'd', new Color(mainLevel.rand.nextInt(100),0,mainLevel.rand.nextInt(200)+50));
						bullet.scale(1.3,0.8);
					}
					bullet.setVelocity(mainLevel.rand.nextDouble()*2.5+0.5, spawner.vars[0]-90);
					enlistBullet(bullet, bullets);
				}
				spawner.vars[3]++;
			}
			if(spawner.aiType == -2) { // laser spawners
				spawner.x += (spawner.vars[0] - spawner.x) / 25.0;
				spawner.y += (spawner.vars[1] - spawner.y) / 25.0;
				
				if(Math.abs(spawner.x - spawner.vars[0]) < 4 && Math.abs(spawner.y - spawner.vars[1]) < 4) {
					BulletSprite laser = new BulletSprite(spawner.x, spawner.y, 'l', new Color(0x5522ff));
					laser.rotate(spawner.vars[2]);
					laser.isActive = false;
					laser.aiMode = -1;
					laser.screenKillable = false;
					enlistBullet(laser,bullets);
					
					spawner.destroy();
				}
			}
		}
		
		for(Object o : bullets) {
			BulletSprite bullet = (BulletSprite) o;
			
			if(bullet.aiMode == -1) { // lasers
				bullet.vars[0]++;
				
				if(bullet.vars[0] == 61) mainLevel.soundPlayer.play("sound/Laser 5.wav");
				
				int laserDeathTime = 100;

				
				if(bullet.vars[0] > 60)
					bullet.isActive = true;
				if(bullet.vars[0] > laserDeathTime)
					bullet.isActive = false;
				if(bullet.vars[0] > laserDeathTime + 10)
					bullet.destroy();
			}
		}
		
		
		
		
		
		unEnlistDeadBullets();
		
		
		// movement
		if(x < vars[0] - 2) setDirection('E');
		else if(x > vars[0] + 2) setDirection('W');
		else setDirection('S');
		
		this.x += (this.vars[0] - this.x)/25.0;
		this.y += (this.vars[1] - this.y)/25.0;
	}
	
	
	
	
	/** 
	 * Star Conjuration ~ Orbital Slingshot 
	 * Wall creates a small star to sling tiny planetoids around at high velocity. 
	 * The stars become heavy as they absorb bullets.
	 */
	private void orbitalSlingshot(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			this.vars[0] = this.x;
			this.vars[1] = this.y;
			this.setDirection('S');
			
			spellCardName = "Star summons ~ Orbital Slingshot";
			if(difficulty == "Hard" || difficulty == "Lunatic") spellCardName = "Ravenous Star ~ Orbital Railgun";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		int starTimeSpacing = 150;
		if(difficulty == "Lunatic") starTimeSpacing = 90;
		int loopEnd = 100 + starTimeSpacing*3;
		
		if(timer == 100) {
			double angleToPlayer = GameMath.getAngleTo(x,y,mainLevel.player.x, mainLevel.player.y);
			EnemySprite spawner;
			spawner = new EnemySprite(x,y, 'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 270;
			spawner.vars[1] = x + 150*GameMath.cos(spawner.vars[0]);
			spawner.vars[2] = y - 150*GameMath.sin(spawner.vars[0]);
			
			enlistBullet(spawner,slaves);
		}
		
		if(timer == 100 + starTimeSpacing) {
			double angleToPlayer = GameMath.getAngleTo(x,y,mainLevel.player.x, mainLevel.player.y);
			EnemySprite spawner;
			spawner = new EnemySprite(x,y, 'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 270+60;
			spawner.vars[1] = x + 150*GameMath.cos(spawner.vars[0]);
			spawner.vars[2] = y - 150*GameMath.sin(spawner.vars[0]);
			
			enlistBullet(spawner,slaves);
		}
		
		if(timer == 100 + starTimeSpacing*2) {
			EnemySprite spawner;
			spawner = new EnemySprite(x,y, 'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 270-60;
			spawner.vars[1] = x + 150*GameMath.cos(spawner.vars[0]);
			spawner.vars[2] = y - 150*GameMath.sin(spawner.vars[0]);
			
			enlistBullet(spawner,slaves);
		}
		
		
		if(timer >= 100 && timer % 100 < 60) {
      if(timer % 4 == 0 ) {
        double bulletSpeed = 2.0;
        int inc = 45;
        
        if(difficulty == "Easy") bulletSpeed = 0.5;
        if(difficulty == "Hard") bulletSpeed = 2.5;
        
        mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
        
        for(int i = (int) this.vars[2]; i < this.vars[2]+360; i += inc*2) {
          int bAngle = i;
          if(timer % 8 == 0) bAngle += inc;
          
          BulletSprite bullet = new BulletSprite(x,y,'b', new Color(0x773311));
          bullet.setVelocity(1+mainLevel.rand.nextDouble()*bulletSpeed, bAngle);
          enlistBullet(bullet, bullets);
        }
      }
		}
    else {
      this.vars[2] = mainLevel.rand.nextInt(360);
      
      if(timer >= loopEnd) {
        vars[0] = mainLevel.SCREENW/2 -100 + mainLevel.rand.nextInt(200);
        vars[1] = 100 - 30 + mainLevel.rand.nextInt(60);
        timer = 99;
      }
    }
		
		
		
		
		
		for(Object o : slaves) {
			EnemySprite spawner = (EnemySprite) o;
			
			if(spawner.aiType == -1) { // star spawners
				spawner.x += (spawner.vars[1] - spawner.x)/20.0;
				spawner.y += (spawner.vars[2] - spawner.y)/20.0;
				
				if(Math.abs(spawner.x - spawner.vars[1]) < 3 && Math.abs(spawner.y - spawner.vars[2]) < 3) { 
					BulletSprite bullet = new BulletSprite(spawner.x,spawner.y, 'i', new Color(0xffff99));
					bullet.aiMode = -1;
					bullet.scaleTarget = 2.0;
					bullet.scale(0.2,0.2);
					enlistBullet(bullet, bullets);
					
					spawner.destroy();
				}
			}
		}
		
		
		for(Object o : bullets) {
			BulletSprite bullet = (BulletSprite) o;
			
			if(bullet.aiMode == -1) { // stars: they absorb bullets and become more massive. They also apply gravity to other enemy bullets.
				// double newScale = bullet.getScaleX() + (bullet.vars[2] - bullet.getScaleX())/10.0;
				// bullet.scale(newScale,newScale);
				bullet.screenKillable = true;
				double starGrav = 150.0;
				if(difficulty == "Easy") starGrav = 50;
				if(difficulty == "Hard") starGrav = 150;
				if(difficulty == "Lunatic") starGrav = 150;
				starGrav *= bullet.scaleTarget*bullet.scaleTarget; // 50.0*bullet.vars[2]*bullet.vars[2];
				
				for(Object o2 : bullets) {
					BulletSprite oBul = (BulletSprite) o2;
					if(oBul.aiMode >= 0) {
						double dist = GameMath.getDist(bullet.x, bullet.y, oBul.x, oBul.y);
						
						oBul.dx += ((bullet.x - oBul.x)/dist)*starGrav/(dist*dist);
						oBul.dy += ((bullet.y - oBul.y)/dist)*starGrav/(dist*dist);
					}					
				}
				
				ArrayList<Sprite> cList = mainLevel.quadTree.query(bullet);
				for(Sprite s : cList) {
					if(s instanceof BulletSprite) {
						BulletSprite oBul = (BulletSprite) s;
						if(oBul.aiMode >= 0 && oBul.collision(bullet)) {
							bullet.scaleTarget += 0.01; // bullet.vars[2] += 0.02;
							oBul.destroy();
						}
					}
					if(s instanceof PlayerBulletSprite && s.collision(bullet)) {
						PlayerBulletSprite oBul = (PlayerBulletSprite) s;
						if(!oBul.permanent) {
							bullet.scaleTarget += 0.01; // bullet.vars[2] += 0.02;
							oBul.destroy();
						}
					}
				}
				
				// move the stars.
				if(bullet.scaleTarget < 3.0 || Math.abs(Math.min(bullet.vars[0],bullet.vars[1])) < 1) {
					int mass = 30000;
					if(difficulty == "Lunatic") mass = 20000;

          bullet.vars[0] += (mainLevel.player.x - bullet.x)/(mass/bullet.scaleTarget);
          bullet.vars[1] += (mainLevel.player.y - bullet.y)/(mass/bullet.scaleTarget);
				}
				
				bullet.x += bullet.vars[0];
				bullet.y += bullet.vars[1];
				
			}
			else {
				double newTheta = GameMath.getAngleTo(0,0,bullet.dx,bullet.dy);
				bullet.rotate(newTheta);
			}
		}
		
		
		
		
		unEnlistDeadBullets();
		
		
		// movement
		if(x < vars[0] - 2) setDirection('E');
		else if(x > vars[0] + 2) setDirection('W');
		else setDirection('S');
		
		this.x += (this.vars[0] - this.x)/15.0;
		this.y += (this.vars[1] - this.y)/15.0;
	}
	
	
	
	
	
	
	/** This one is a survival spell card. Rey is transported to a short-lived universe about to collapse in upon itself. */
	private void bigCrunch(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 45;
			vars[0] = 0.0;
			vars[1] = 0;
			if(difficulty == "Easy") spellCardName = "End of a Teeny Tiny Universe ~ Little Big Crunch";
			if(difficulty == "Normal") spellCardName = "End of a Tiny Universe ~ Debris Maze";
			if(difficulty == "Hard") spellCardName = "Tiny Universe's Demise ~ The Big Crunch";
			if(difficulty == "Lunatic") spellCardName = "Tiny Universe's Demise ~ Andy Asteroids?";
			
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		isActive = false;
		
		setSemiTransparency(vars[0]);
		vars[0] += (1.0 - vars[0])/40.0;
		
		if(timer % 120 == 0) mainLevel.soundPlayer.play("sound/WAVE09.WAV");
		
		if(timer == 100) {
			EnemySprite spawner;
			
			spawner = new EnemySprite(0,0,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 0;
			enlistBullet(spawner, slaves);
			
			spawner = new EnemySprite(mainLevel.SCREENW,0,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 270;
			enlistBullet(spawner, slaves);
			
			spawner = new EnemySprite(mainLevel.SCREENW,mainLevel.SCREENH,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 180;
			enlistBullet(spawner, slaves);
			
			spawner = new EnemySprite(0,mainLevel.SCREENH,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 90;
			enlistBullet(spawner, slaves);
		}
		
		
		for(Object o : slaves) {
			EnemySprite spawner = (EnemySprite) o;
			
			if(spawner.aiType == -1) { // star spawners
				spawner.x += 4*GameMath.cos(spawner.vars[0]);
				spawner.y -= 4*GameMath.sin(spawner.vars[0]);
				
				if(spawner.vars[0] == 0 && spawner.x >= mainLevel.SCREENW) spawner.vars[0] = 270;
				if(spawner.vars[0] == 270 && spawner.y >= mainLevel.SCREENH) spawner.vars[0] = 180;
				if(spawner.vars[0] == 180 && spawner.x <= 0) spawner.vars[0] = 90;
				if(spawner.vars[0] == 90 && spawner.y <= 0) spawner.vars[0] = 0;
				
				int bulletInterval = 61;
				if(difficulty == "Easy") bulletInterval = 91;
				
				if((timer + spawner.vars[0]*10) % bulletInterval == 0) {
					double angleToPlayer = GameMath.getAngleTo(spawner.x, spawner.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite bullet = new BulletSprite(spawner.x, spawner.y, 'e', new Color(0xaa8844));
					bullet.setVelocity(1.0, angleToPlayer);
					bullet.aiMode = -1;
					bullet.vars[0] = 1.0;
					bullet.vars[1] = mainLevel.rand.nextDouble()*2+0.5;
					enlistBullet(bullet,bullets);
				}
			/*	if((timer + spawner.vars[0]*10) % 131 == 0) {
					double angleToPlayer = GameMath.getAngleTo(spawner.x, spawner.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite bullet = new BulletSprite(spawner.x, spawner.y, 'i', new Color(0x886611));
					bullet.setVelocity(1.0, angleToPlayer);
					enlistBullet(bullet,bullets);
				}*/
			}
		}
		
		LinkedList<Sprite> newSprites = new LinkedList<Sprite>();
		
		
		for(Object o : bullets) {
			BulletSprite bullet = (BulletSprite) o;
			
			if(bullet.aiMode == -1) {
				double dist = GameMath.getDist(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
				double mass = 0.5;
						
				bullet.dx = ((mainLevel.player.x - bullet.x)/(dist))*mass/(bullet.vars[1]/2+0.5)/1.5;
				bullet.dy = ((mainLevel.player.y - bullet.y)/(dist))*mass/(bullet.vars[1]/2+0.5)/1.5;
				
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				bullet.scale(bullet.vars[0], bullet.vars[0]);
				bullet.vars[0] += (bullet.vars[1] - bullet.vars[0])/20.0;
				
				ArrayList<Sprite> cList = mainLevel.quadTree.query(bullet);
				for(Sprite s : cList) {
					if(s instanceof BulletSprite) {
						BulletSprite oBul = (BulletSprite) s;
						if(oBul != bullet && !bullet.isDestroyed && !oBul.isDestroyed && oBul.aiMode == -1 && oBul.collision(bullet) && bullet.vars[1] >= oBul.vars[1]) {
							if(difficulty == "Easy" || difficulty == "Normal") {
								bullet.vars[1] += oBul.vars[1]/8;
								bullet.dx /= 2.0;
								bullet.dy /= 2.0;
								oBul.destroy();
							}
							else {
								BulletSprite shard;
								int initAngle = mainLevel.rand.nextInt(360);
								int spacing = 120;
								if(difficulty == "Lunatic") spacing = 90;
								for(int i = 0; i < 360; i += spacing) {
									shard = new BulletSprite(bullet.x, bullet.y, 'm', new Color(0xaa0000));
									shard.setVelocity(1.0, initAngle + i);
									newSprites.add(shard);
								}
								for(int i = 0; i < 360; i += spacing) {
									shard = new BulletSprite(oBul.x, oBul.y, 'm', new Color(0xaa0000));
									shard.setVelocity(1.0, initAngle + i);
									newSprites.add(shard);
								}
								
								bullet.destroy();
								oBul.destroy();
							}
						}
					}
				}
			}
		}
		for(Sprite sprite : newSprites) {
			enlistBullet(sprite, bullets);
		}
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
	}
	
	
	/** Inverse burst-sprays centered on Wall */
	private void nsc1(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = mainLevel.SCREENH/2 - 50;
			vars[2] = 0;
			vars[3] = 2;
		}
		
		int lanespacing = 60;
		if(difficulty == "Easy") lanespacing = 80;
		if(difficulty == "Hard") lanespacing = 45;
		if(difficulty == "Lunatic") lanespacing = 30;
		
		if(timer >= 80  && timer % 64 < 32) {
      if(timer % 4 == 0) {
        BulletSprite bullet;
        double bulletSpd = 2;
        double angleToPlayer = vars[4];
        
        mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
        
        for(int i = 0; i < 360; i+= lanespacing) {
          bullet = new BulletSprite(x,y, 'b', new Color(0x00aaff));
          bullet.setVelocity(4, i + angleToPlayer);
          enlistBullet(bullet, bullets);
        }
      }
		}
    else {
      vars[4] = GameMath.getAngleTo(x,y, mainLevel.player.x, mainLevel.player.y);
    }
		
		if(timer > 100) {
			if(timer % 15 == 0) mainLevel.soundPlayer.play("sound/WAVE03.WAV");
			if(timer % 8 == 0) {
				
				vars[2] += vars[3];
				vars[3] += 3;
				
				BulletSprite bullet;
				double bulletSpd = 1.5;
				double bulletRad = mainLevel.SCREENH;
				
				int spacing = 90;
				if(difficulty == "Easy") spacing = 180;
				if(difficulty == "Lunatic") spacing = 60;
				
				for(int i = 0; i < 360; i+=spacing) {
					double bulletAngle = (i + vars[2]);
					bullet = new BulletSprite(this.x + bulletRad*GameMath.cos(bulletAngle),this.y - bulletRad*GameMath.sin(bulletAngle), 'b', new Color(0x00aaff));
					bullet.setVelocity(bulletSpd, bulletAngle+180);
					bullet.aiMode = -1;
					bullet.rotate(bulletAngle+180);
					enlistBullet(bullet, bullets);
				}
			}
		}
		
		for(Object o : bullets) {
			BulletSprite bullet = (BulletSprite) o;
			
			if(bullet.aiMode == -1) {
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				bullet.screenKillable = false;
				
				ArrayList<Sprite> cList = mainLevel.quadTree.query(bullet);
				for(Sprite s : cList) {
					if(s != this && bullet.collision(this)) {
						bullet.destroy();
					}
				}
			}
		}
		
		unEnlistDeadBullets();
		
		
		// movement
		if(x < vars[0] - 2) setDirection('E');
		else if(x > vars[0] + 2) setDirection('W');
		else setDirection('S');
		
		this.x += (this.vars[0] - this.x)/15.0;
		this.y += (this.vars[1] - this.y)/15.0;
	}
	
  
  
  
  
  
  
  
  
  /** Meteor rain attack */
	private void nsc2(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = mainLevel.SCREENH/2 - 50;
			vars[2] = 0;
			vars[3] = 0;
		}
		
		
    if(timer > 100 && timer % 4 == 0) {
      
      
      BulletSprite bullet;
      Color bulletColor = new Color(mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50));
      
      int limit = 2;
      if(difficulty == "Lunatic")
        limit = 3;
      
      for(int i = 0; i < limit; i++) {
        int meteorChance = mainLevel.rand.nextInt(10);
        char meteorSize = 'a';
        double meteorScale = 1;
        
        if(meteorChance <= 0)
          meteorSize = 'i';
        else if(meteorChance <= 3) {
          meteorSize = 'd';
          meteorScale = 2;
        }
        else if(meteorChance <= 7) {
          meteorSize = 'd';
        }
      
        bullet = new BulletSprite(mainLevel.rand.nextDouble()*mainLevel.SCREENW, 0, meteorSize, bulletColor); 
        bullet.setVelocity(0.2 + mainLevel.rand.nextDouble()*2, 270);
        bullet.scale(0.1,0.1);
        bullet.scaleTarget = meteorScale;
        enlistBullet(bullet, bullets);
      }
      
    }
    
    for(Object obj : bullets) {
			BulletSprite bullet = (BulletSprite) obj;
      
      bullet.dy *= 1.01;
		}
		
		unEnlistDeadBullets();
		
		
		// movement
		if(x < vars[0] - 2) setDirection('E');
		else if(x > vars[0] + 2) setDirection('W');
		else setDirection('S');
		
		this.x += (this.vars[0] - this.x)/15.0;
		this.y += (this.vars[1] - this.y)/15.0;
	}
  
	
  
  
  
  
  
  
  
  
	/** Traps Ray in a spiral galaxy. */
	private void singularityWell(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		ABGStage4Sprite stage4BG = (ABGStage4Sprite) stageBG;
		
		if(timer == 0) // initialize spell card variables
		{
			hp = 1200;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = mainLevel.SCREENH/2 - 100;
			spellCardName = "Astral Trap ~ Singularity Well";
			portrait.vars[2] = 10;
			
			stage4BG.spaceWarpBG.isWarped = false;
			mainLevel.player.vars[8] = mainLevel.SCREENW/2;
			mainLevel.player.vars[9] = mainLevel.SCREENH/2;
		}
		portrait.spellCardPortrait();
		
		
		if(timer == 2) stage4BG.spaceWarpBG.isWarped = false;
		if(timer == 120) stage4BG.spaceWarpBG.isWarped = true;
		
		if(timer >= 1) {
			if(timer % 60 == 0) {
				this.vars[0] += mainLevel.rand.nextInt(180)-90;
				this.vars[1] += mainLevel.rand.nextInt(40)-20;
				
				if(this.vars[0] < 0) vars[0] = 0;
				if(this.vars[0] > mainLevel.SCREENW) vars[0] = mainLevel.SCREENW;
				if(this.vars[1] < 20) vars[1] = 20;
				if(this.vars[1] > 200) vars[1] = 200;
			}
			
			if(timer == 50) {
				EnemySprite spawner;
				double angleToPlayer = GameMath.getAngleTo(x,y,mainLevel.player.x, mainLevel.player.y);
				
				this.vars[2] = 255*2;
				
				int spawnerSpacing = 40;
				if(difficulty == "Easy") spawnerSpacing = 72;
				if(difficulty == "Lunatic") spawnerSpacing = 33;
				
				for(int i = 0; i < 360; i += spawnerSpacing) {
					spawner = new EnemySprite(x,y, 'e',9999);
					spawner.aiType = -1;
					spawner.vars[0] = mainLevel.player.x;
					spawner.vars[1] = mainLevel.player.y;
					spawner.vars[2] = 80;
					spawner.vars[3] = i + angleToPlayer;
					spawner.vars[4] = 1;
					
					enlistBullet(spawner,slaves);
					
					
					spawner = new EnemySprite(x,y, 'e',9999);
					spawner.aiType = -1;
					spawner.vars[0] = mainLevel.player.x;
					spawner.vars[1] = mainLevel.player.y;
					spawner.vars[2] = 80;
					spawner.vars[3] = i + angleToPlayer;
					spawner.vars[4] = -1;
					
					enlistBullet(spawner,slaves);
				}
				
				mainLevel.player.vars[8] = mainLevel.player.x;
				mainLevel.player.vars[9] = mainLevel.player.y;
			}
		}
		
		boolean makepewpews = false;
		
		for(Object obj : slaves) {
			EnemySprite spawner = (EnemySprite) obj;
      BulletSprite bullet;
			Color bulletColor = new Color((int) Math.max(this.vars[2]/2, 0), 0, 255);
      
      if(spawner.vars[5] < 40) {
        
      }
			if(spawner.vars[5] < 70) {
				spawner.x += (spawner.vars[0] + spawner.vars[2]*GameMath.cos(spawner.vars[3]) - spawner.x) / 25.0;
				spawner.y += (spawner.vars[1] - spawner.vars[2]*GameMath.sin(spawner.vars[3]) - spawner.y) / 25.0;
			}
			if(spawner.vars[5] >= 70) {
				spawner.x = spawner.vars[0] + spawner.vars[2]*GameMath.cos(spawner.vars[3]);
				spawner.y = spawner.vars[1] - spawner.vars[2]*GameMath.sin(spawner.vars[3]);
				spawner.vars[3] += spawner.vars[4];
				
				if(timer % 3 == 0) {

					if(spawner.vars[4] == 1) {
						bullet = new BulletSprite(spawner.x, spawner.y, 'm', bulletColor);
						bullet.rotate(spawner.vars[3]+90);
						bullet.vars[0] = spawner.vars[3] + 180;
						bullet.aiMode = -1;
						
						enlistBullet(bullet, bullets);
					}
					else {
						bullet = new BulletSprite(spawner.x,spawner.y, 'a', bulletColor);
						bullet.setVelocity(5, spawner.vars[3] + 45);
						bullet.isInverted = true;
						enlistBullet(bullet, bullets);
						
						bullet = new BulletSprite(spawner.x,spawner.y, 'a', bulletColor);
						bullet.setVelocity(5, spawner.vars[3] - 45);
						bullet.isInverted = true;
						enlistBullet(bullet, bullets);
					}
					
					this.vars[2]--;
				}
				
			}
			if(spawner.vars[5] > 100) {
				if(spawner.vars[4] == 1) spawner.vars[2] *= 1.02;
				else spawner.vars[2] *= 1.0021;
			}
			
      if(spawner.vars[5] == 300 && (difficulty == "Hard" || difficulty == "Lunatic")) {
        spawner.destroy();
        timer = 1;
      }
			if(spawner.vars[5] > 400) {
				spawner.destroy();
				timer = 1;
			}
			spawner.vars[5]++;
		}
		for(Object obj : bullets) {
			BulletSprite bullet = (BulletSprite) obj;
		
			if(bullet.aiMode == -1) {
				bullet.vars[2]++;
				if(bullet.vars[2] > 60) {
					bullet.setVelocity(0.1, bullet.vars[0]);
					bullet.aiMode = -2;
					bullet.vars[2] = 0;
					bullet.rotate(bullet.theta);
				}
			}
			if(bullet.aiMode == -2) {
				bullet.vars[2]++;
				if(bullet.vars[2] < 60) {
					bullet.dx *= 1.05;
					bullet.dy *= 1.05;
				}
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
			}
		}
		
		unEnlistDeadBullets();
		
		
		// movement

		if(x < vars[0] - 2) setDirection('E');
		else if(x > vars[0] + 2) setDirection('W');
		else setDirection('S');
		
		this.x += (this.vars[0] - this.x)/25.0;
		this.y += (this.vars[1] - this.y)/25.0;
	}
	
	
	
	
	
	/** Downwards gravity and large bullets with orbitting small bullets */
	private void ringedGiants(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		ABGStage4Sprite stage4BG = (ABGStage4Sprite) stageBG;
		
		if(timer == 0) // initialize spell card variables
		{
			hp = 1400;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 60;
			vars[0] = mainLevel.SCREENW/2;
			vars[1] = mainLevel.SCREENH/2 - 100;
			spellCardName = "Falling Sky ~ Cascade of Ringed Giants";
			mainLevel.soundPlayer.play("sound/WAVE09.WAV");
			portrait.vars[2] = 10;
			stage4BG.spaceWarpBG.isWarped = false;
			mainLevel.player.vars[8] = mainLevel.SCREENW/2;
		}
		portrait.spellCardPortrait();
		
		// gravity
		if(mainLevel.keyboard.isPressed(mainLevel.config.VK_UP)) {
			mainLevel.player.vars[9] -= 0.3;
		}
		mainLevel.player.vars[9] += 0.2;
		
		if(mainLevel.player.vars[9] > 10) mainLevel.player.vars[9] = 10;
		if(mainLevel.player.vars[9] < 0) mainLevel.player.vars[9] = 0;
		
		mainLevel.player.y += mainLevel.player.vars[9];
		if(mainLevel.player.y > mainLevel.SCREENH) {
			mainLevel.player.y = mainLevel.SCREENH;
			mainLevel.player.vars[9] = 0;
		}
		mainLevel.pHitBox.y = mainLevel.player.y;
		
		if(timer % 120 == 0) mainLevel.soundPlayer.play("sound/WAVE09.WAV");
		
		if(timer > 60) {
			// create ringed giant spawners
			if(timer % 120 == 0) {
				EnemySprite spawner;
				spawner = new EnemySprite(x,y, 'e',9999);
				spawner.aiType = -1;
				spawner.vars[0] = mainLevel.rand.nextInt(mainLevel.SCREENW);
				spawner.vars[1] = mainLevel.rand.nextInt(mainLevel.SCREENH)/3;
				
				enlistBullet(spawner,slaves);
			}
		}
		
		for(Object obj : slaves) {
			EnemySprite spawner = (EnemySprite) obj;
			
			spawner.x += (spawner.vars[0] - spawner.x) / 25.0;
			spawner.y += (spawner.vars[1] - spawner.y) / 25.0;
			
			// create ringed giants
			if(Math.abs(spawner.x  - spawner.vars[0]) < 2 && Math.abs(spawner.y - spawner.vars[1]) < 2) {
				Color giantColor = new Color(mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50));
				Color ringColor = new Color(giantColor.getRed()/2, giantColor.getGreen()/2,giantColor.getBlue()/2);
				
				BulletSprite bullet;
				
				// the giant
				
				bullet = new BulletSprite(spawner.x, spawner.y, 'd', giantColor);
				bullet.scaleTarget = 6.0;
				bullet.setVelocity(0.5, 180 + 50 + mainLevel.rand.nextInt(80));
				bullet.aiMode = -1;
				
				enlistBullet(bullet, bullets);
				
				BulletSprite giant = bullet;
				
				// the ring 1
				
				for(int i = 0; i < 360; i += 30) {
					bullet = new BulletSprite(spawner.x, spawner.y, 'a', ringColor);
					bullet.setCircularVelocity(spawner.x, spawner.y, i, 5, 2, 0);
					bullet.aiMode = -2;
					
					bullet.vars[0] = giant.dx;
					bullet.vars[1] = giant.dy;
					bullet.vars[3] = 55;
					
					enlistBullet(bullet, bullets);
				}
				
				// the ring 2
				
				int ringSpacing = 30;
        if(difficulty == "Easy") {
          ringSpacing = 60;
        }
        if(difficulty == "Lunatic") {
          ringSpacing = 20;
        }
        
				for(int i = 0; i < 360; i+= ringSpacing) {
					bullet = new BulletSprite(spawner.x, spawner.y, 'e', ringColor);
					bullet.setCircularVelocity(spawner.x, spawner.y, i, 5, 1, 0);
					bullet.isInverted = true;
					bullet.aiMode = -2;
					
					bullet.vars[0] = giant.dx;
					bullet.vars[1] = giant.dy;
					bullet.vars[3] = 150;
					
					enlistBullet(bullet, bullets);
				}
        if(difficulty == "Hard") {
          for(int i = 0; i < 360; i+= ringSpacing) {
            bullet = new BulletSprite(spawner.x, spawner.y, 'e', ringColor);
            bullet.setCircularVelocity(spawner.x, spawner.y, i, 5, 1.2, 0);
            bullet.isInverted = true;
            bullet.aiMode = -2;
            
            bullet.vars[0] = giant.dx;
            bullet.vars[1] = giant.dy;
            bullet.vars[3] = 150;
            
            enlistBullet(bullet, bullets);
          }
        }
        if(difficulty == "Lunatic") {
          for(int i = 0; i < 360; i+= ringSpacing) {
            bullet = new BulletSprite(spawner.x, spawner.y, 'e', ringColor);
            bullet.setCircularVelocity(spawner.x, spawner.y, i, 5, 2, 0);
            bullet.isInverted = true;
            bullet.aiMode = -2;
            
            bullet.vars[0] = giant.dx;
            bullet.vars[1] = giant.dy;
            bullet.vars[3] = 150;
            
            enlistBullet(bullet, bullets);
          }
        }
				
				
				spawner.destroy();
			}
		}
		
		
		LinkedList<Sprite> newSprites = new LinkedList<Sprite>();
		
		for(Object obj : bullets) {
			BulletSprite bullet = (BulletSprite) obj;
			
			if(bullet.aiMode == -1) {
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				
				if(bullet.vars[1] < 60) {
					bullet.dx *= 1.02;
					bullet.dy *= 1.02;
				}
				
				if(bullet.y > mainLevel.SCREENH) {
					BulletSprite shard = new BulletSprite(bullet.x - 32 + mainLevel.rand.nextInt(64), mainLevel.SCREENH, 'm', bullet.color);
					shard.setProjectileVelocity(3.0, 45 + mainLevel.rand.nextInt(90), 0.05);
					newSprites.add(shard);
					
					shard = new BulletSprite(bullet.x - 32 + mainLevel.rand.nextInt(64), mainLevel.SCREENH, 'm', bullet.color);
					shard.setProjectileVelocity(3.0, 45 + mainLevel.rand.nextInt(90), 0.05);
					newSprites.add(shard);
				}
				
				bullet.vars[1]++;
			}
			if(bullet.aiMode == -2) {
				bullet.x = bullet.cx + bullet.rad * GameMath.cos(bullet.theta);
				bullet.y = bullet.cy - bullet.rad * GameMath.sin(bullet.theta);
				
				bullet.cx += bullet.vars[0];
				bullet.cy += bullet.vars[1];
				
				if(bullet.vars[2] < 60) {
					bullet.vars[0] *= 1.02;
					bullet.vars[1] *= 1.02;
				}
				
				bullet.rad += (bullet.vars[3] - bullet.rad)/40.0;
				bullet.theta += bullet.dx / bullet.rad;
				
				bullet.screenKillable = false;
				
				bullet.vars[2]++;
				
				if(bullet.vars[2] > 400) bullet.destroy();
			}
		}
		for(Sprite sprite : newSprites) {
			enlistBullet(sprite, bullets);
		}
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
		
		this.x += (this.vars[0] - this.x)/25.0;
		this.y += (this.vars[1] - this.y)/25.0;
	}
	
	
	
	
	// unused spellcards
	
	private void exWall0(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 30;
			vars[0] = 0;
			vars[1] = 0;
		}
		
		if(timer % 7 == 0) {
		
			BulletSprite bullet;
			int initAngle = mainLevel.rand.nextInt(360);
			double bulletSpd = 2;
			double rotSpd = (mainLevel.rand.nextDouble()*3+2)*(mainLevel.rand.nextInt(2)-0.5)*2;
			
			Color bulletColor = new Color(mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50),mainLevel.rand.nextInt(50));
			
			for(int i = 0; i < 360; i+=20) {
				bullet = new BulletSprite(this.x,this.y, 'd', bulletColor);
				bullet.setRotationalVelocity(bulletSpd, initAngle + i, rotSpd, 0.98);
				bullet.aiMode = 2;
				enlistBullet(bullet, bullets);
			}
		}
		
		
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
	}
	
	
	/** This one is a survival spell card. Rey is transported to a short-lived universe about to collapse in upon itself. */
	private void bigCrunch2(SpriteList bullets, SpriteList slaves, SpriteList sfx)
	{
		if(timer == 0) // initialize spell card variables
		{
			hp = 700;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 45;
			vars[0] = 0.0;
			vars[1] = 0;
			spellCardName = "Tiny Universe's End ~ The Big Crunch";
			portrait.vars[2] = 10;
		}
		portrait.spellCardPortrait();
		
		isActive = false;
		
		setSemiTransparency(vars[0]);
		vars[0] += (1.0 - vars[0])/40.0;
		
		if(timer % 120 == 0) mainLevel.soundPlayer.play("sound/WAVE09.WAV");
		
		if(timer == 100) {
			EnemySprite spawner;
			
			spawner = new EnemySprite(0,0,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 0;
			enlistBullet(spawner, slaves);
			
			spawner = new EnemySprite(mainLevel.SCREENW,0,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 270;
			enlistBullet(spawner, slaves);
			
			spawner = new EnemySprite(mainLevel.SCREENW,mainLevel.SCREENH,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 180;
			enlistBullet(spawner, slaves);
			
			spawner = new EnemySprite(0,mainLevel.SCREENH,'e',9999);
			spawner.aiType = -1;
			spawner.vars[0] = 90;
			enlistBullet(spawner, slaves);
		}
		
		
		for(Object o : slaves) {
			EnemySprite spawner = (EnemySprite) o;
			
			if(spawner.aiType == -1) { // star spawners
				spawner.x += 5*GameMath.cos(spawner.vars[0]);
				spawner.y -= 5*GameMath.sin(spawner.vars[0]);
				
				if(spawner.vars[0] == 0 && spawner.x >= mainLevel.SCREENW) spawner.vars[0] = 270;
				if(spawner.vars[0] == 270 && spawner.y >= mainLevel.SCREENH) spawner.vars[0] = 180;
				if(spawner.vars[0] == 180 && spawner.x <= 0) spawner.vars[0] = 90;
				if(spawner.vars[0] == 90 && spawner.y <= 0) spawner.vars[0] = 0;
				
				if((timer + spawner.vars[0]*10) % 31 == 0) {
					double angleToPlayer = GameMath.getAngleTo(spawner.x, spawner.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite bullet = new BulletSprite(spawner.x, spawner.y, 'e', new Color(0xaa8844));
					bullet.setVelocity(1.5, angleToPlayer);
					bullet.vars[0] = 1.0;
					bullet.vars[1] = 1.0;
					enlistBullet(bullet,bullets);
				}
			/*	if((timer + spawner.vars[0]*10) % 131 == 0) {
					double angleToPlayer = GameMath.getAngleTo(spawner.x, spawner.y, mainLevel.player.x, mainLevel.player.y);
					BulletSprite bullet = new BulletSprite(spawner.x, spawner.y, 'i', new Color(0x886611));
					bullet.setVelocity(1.0, angleToPlayer);
					enlistBullet(bullet,bullets);
				}*/
			}
		}
		
		for(Object o : bullets) {
			BulletSprite bullet = (BulletSprite) o;
			
			if(bullet.aiMode == 0) {
				double dist = Math.max(GameMath.getDist(bullet.x, bullet.y, mainLevel.SCREENW/2, mainLevel.SCREENH/2),10);
				double mass = 40;
						
				bullet.dx += ((mainLevel.SCREENW/2 - bullet.x)/dist)*mass/(dist*dist)/(bullet.vars[1]/2+0.5);
				bullet.dy += ((mainLevel.SCREENH/2 - bullet.y)/dist)*mass/(dist*dist)/(bullet.vars[1]/2+0.5);
				
				bullet.scale(bullet.vars[0], bullet.vars[0]);
				bullet.vars[0] += (bullet.vars[1] - bullet.vars[0])/20.0;
				
				ArrayList<Sprite> cList = mainLevel.quadTree.query(bullet);
				for(Sprite s : cList) {
					if(s instanceof BulletSprite) {
						BulletSprite oBul = (BulletSprite) s;
						if(oBul != bullet && !bullet.isDestroyed && !oBul.isDestroyed && oBul.collision(bullet) && bullet.vars[1] >= oBul.vars[1]) {
							bullet.vars[1] += oBul.vars[1]/4;
							bullet.dx /= 2.0;
							bullet.dy /= 2.0;
							oBul.destroy();
						}
					}
				}
			}
		}
		
		unEnlistDeadBullets();
		
		
		// movement

		this.setDirection('S');
	}
	
	
}






