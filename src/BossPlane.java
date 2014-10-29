
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Random;
import java.net.URL;
import gameEngine.*;



public class BossPlane extends BossSprite
{
	private static int numInstances = 0;

	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public double tilt = 0;
	private java.awt.Rectangle wingBox = new java.awt.Rectangle(-175,-25,350,25);
   
   private Point2D shooter1, shooter2, shooter3, shooter4, shooter5, shooter6;
	
	// enemy vars
	
	int hoverAngle;
	
	// CONSTRUCTOR
	
	public BossPlane(double x, double y, MainLevel m)
	{
		super(x,y,m);
		
		numInstances++;
		this.setRadius(40);
		this.width = 400;
		this.height = 200;
		this.lastAttackIndex = 0;
		portrait = null;
		hoverAngle = 0;
	//	isActive = true;
   
      shooter1 = new Point2D.Double();
      shooter2 = new Point2D.Double();
      shooter3 = new Point2D.Double();
      shooter4 = new Point2D.Double();
      shooter5 = new Point2D.Double();
      shooter6 = new Point2D.Double();
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
		
		imageURL = EnemySprite.class.getResource("graphics/Midboss4Sheet.png");
		Image bigSmileySheet = tk.getImage(imageURL);
		bigSmileySheet = ColorFilters.setTransparentColor(bigSmileySheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(bigSmileySheet,1,1,400,100);
		il.addImage(img);
		focusTable.put("alive1",new Point(200,50));
		imageTable.put("alive1",img);
		
		img = ImageBlitter.crop(bigSmileySheet,1,103,400,100);
		il.addImage(img);
		focusTable.put("alive2",new Point(200,50));
		imageTable.put("alive2",img);
		
		img = ImageBlitter.crop(bigSmileySheet,1,205,400,100);
		il.addImage(img);
		focusTable.put("dead",new Point(200,50));
		imageTable.put("dead",img);
		
		System.out.println("loaded image data for BossPlane");
		
	}
	
	// LOGIC METHODS
	
	
	
	
	
	// RENDERING METHODS
	
   public boolean render(Graphics2D g)
	{
      super.render(g);
      /*
      g.setColor(new Color(0x00ff00));
      g.fillOval((int)shooter1.getX() - 10, (int)shooter1.getY() - 10, 20, 20);
      g.fillOval((int)shooter2.getX() - 10, (int)shooter2.getY() - 10, 20, 20);
      g.fillOval((int)shooter3.getX() - 10, (int)shooter3.getY() - 10, 20, 20);
      g.setColor(new Color(0xffff00));
      g.fillOval((int)shooter4.getX() - 10, (int)shooter4.getY() - 10, 20, 20);
      g.fillOval((int)shooter5.getX() - 10, (int)shooter5.getY() - 10, 20, 20);
      g.fillOval((int)shooter6.getX() - 10, (int)shooter6.getY() - 10, 20, 20);
      */
      return true;
   }
   
	protected void draw(Graphics2D g)
	{
		g.translate(3*GameMath.cos(hoverAngle),3*GameMath.sin(hoverAngle));
		g.drawImage(this.curImage, null, null);
		
		// draw the hit box/circle
	/*	g.translate(fx,fy);
		g.setColor(new Color(0xff0000));
		g.fillOval((int)(0-r), (int)(0-r), (int)r*2, (int)r*2);
		g.fill(wingBox);*/
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

		String curImgKey;
		
		if(!isActive && attackIndex > 0)
		{
         curImgKey = "dead";
         tilt = -70;
         this.y += vars[3];
         vars[3] += 0.1;
		}
		else
		{
			curImgKey = "alive";
			int imgNum = 1;
			if(frameNum > 1) imgNum++;
			
			this.frameNum++;
			if(frameNum > 3) frameNum = 0;
			
			curImgKey += imgNum;
		}
		curImage = imageTable.get(curImgKey);
		
		Point curFocus = focusTable.get(curImgKey);
		fx = curFocus.x;
		fy = curFocus.y;

		hoverAngle += 5;
		
		// rotate according to the plane's tilt.
		rotate(rotation + (tilt-rotation)/15.0);
      
      // position the plane's shooters
      AffineTransform shooterTrans = new AffineTransform();
      shooterTrans.translate(x,y);
      shooterTrans.rotate(0-GameMath.d2r(rotation));
      
      
      shooter1 = shooterTrans.transform(new Point2D.Double(-180,-5), shooter1);
      shooter2 = shooterTrans.transform(new Point2D.Double(-140,-5), shooter2);
      shooter3 = shooterTrans.transform(new Point2D.Double(-75,-5), shooter3);
      shooter4 = shooterTrans.transform(new Point2D.Double(75,-5), shooter4);
      shooter5 = shooterTrans.transform(new Point2D.Double(140,-5), shooter5);
      shooter6 = shooterTrans.transform(new Point2D.Double(180,-5), shooter6);
	//	animateBlurs(il);
	}
	
	
	/**
	*	collision(Sprite other)
	*	BossPlane has to detect collisions between its hit circle and for a hit rectangle around its wings.
	**/
	
	public boolean collision(Sprite other)
	{
	//	System.out.println("collision for plane");
		return super.collision(other) || this.planeBox2CircleCollision(other);
	}
	
	private boolean planeBox2CircleCollision(Sprite other) {
		int x1 = (int)this.x + wingBox.x;
		int y1 = (int)this.y + wingBox.y;
		int w1 = wingBox.width;
		int h1 = wingBox.height;
		
		AffineTransform thisTrans = AffineTransform.getTranslateInstance(this.x,this.y);
		thisTrans.concatenate(this.transform);
		thisTrans.translate(0-this.x, 0-this.y);
		
		Point2D[] tPoints = new Point2D[4];
		tPoints[0] = thisTrans.transform(new Point(x1,y1), tPoints[0]);
		tPoints[1] = thisTrans.transform(new Point(x1+w1,y1), tPoints[1]);
		tPoints[2] = thisTrans.transform(new Point(x1+w1,y1+h1), tPoints[2]);
		tPoints[3] = thisTrans.transform(new Point(x1,y1+h1), tPoints[3]);
		
		// loop over all the vectors forming the wingbox
	//	System.out.println("wing box test: ");
		for(int i = 0; i < 4; i++) {
			Point2D q = tPoints[i];
			Point2D r = tPoints[(i+1)%4];
			
			double dist = GameMath.isPointAboveVector(q,r,new Point2D.Double(other.x, other.y));
		//	System.out.print(dist + " ");
			if(dist > other.getRadius()) return false;
		}
	//	System.out.println();
		
		return true;
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
            mainLevel.spawnItems(this.itemDrops, this.x, this.y);
            attackIndex=2;
		//	nsc2(bullets,slaves,sfx);
		}
		
		
		// set health bar values and spell card text

	//	portrait.text = spellCardName;
		mainLevel.barBossHealth.caption2 = spellCardName;
		
      isVisible = true;
      
		// increment timer
		
		timer++;
	}

	
	
	
	// NSC1
	
	private void nsc1(SpriteList bullets, SpriteList slaves, SpriteList sfx) {
      if(timer == 0) // initialize spell card variables
		{
			hp = 2000;
			
			mainLevel.barBossHealth.max = (long)hp;
			mainLevel.barBossHealth.value = (long)hp;
			
			mainLevel.numberSCTimer.value = 99;
         vars[0] = 0;
         vars[1] = 0;
			vars[2] = 0;
			
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("S"));
			itemDrops.add(mainLevel.curStage.makeItem("P"));
			itemDrops.add(mainLevel.curStage.makeItem("P"));
			itemDrops.add(mainLevel.curStage.makeItem("P"));
			itemDrops.add(mainLevel.curStage.makeItem("P"));
			itemDrops.add(mainLevel.curStage.makeItem("B"));
			
			spellCardName = "";
		}
		
      int time1 = 50; // rapid bullets
      int time2 = time1 + 120; // fire a couple missiles
      int time3 = time2 + 150; // charge!!!
      int time4 = time3 + 450; 
      
		double angleToPlayer = GameMath.getAngleTo(this.x, this.y, mainLevel.player.x, mainLevel.player.y);
      
      if(vars[0] >= time1 && vars[0] < time2) {
         double shooterAngle1 = GameMath.getAngleTo(shooter1.getX(),shooter1.getY(), mainLevel.player.x, mainLevel.player.y);
         double shooterAngle2 = GameMath.getAngleTo(shooter6.getX(),shooter6.getY(), mainLevel.player.x, mainLevel.player.y);
         
         if(vars[0] % 10 == 0) {
            mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
            
            double inc = 20;
            if(difficulty == "Easy") {
              inc = 40;
            }
            if(difficulty == "Lunatic") {
              inc = 10;
            }
            
            for(int i = 0; i < 360; i+= inc) {
               BulletSprite bullet;
               
               bullet = new BulletSprite(shooter1.getX(),shooter1.getY(), 'a', new Color(0xaadd00));
               bullet.scale(1.5,0.6);
               bullet.setVelocity(4.5, shooterAngle1 + i);
               enlistBullet(bullet, bullets);
               
               bullet = new BulletSprite(shooter6.getX(),shooter6.getY(), 'a', new Color(0xaadd00));
               bullet.scale(1.5,0.6);
               bullet.setVelocity(4.5, shooterAngle2 + i);
               enlistBullet(bullet, bullets);
               
               if(difficulty == "Hard" || difficulty == "Lunatic") {
                 bullet = new BulletSprite(shooter1.getX(),shooter1.getY(), 'a', new Color(0xaadd00));
                 bullet.scale(1.5,0.6);
                 bullet.setVelocity(3, shooterAngle1 + i);
                 enlistBullet(bullet, bullets);
                 
                 bullet = new BulletSprite(shooter6.getX(),shooter6.getY(), 'a', new Color(0xaadd00));
                 bullet.scale(1.5,0.6);
                 bullet.setVelocity(3, shooterAngle2 + i);
                 enlistBullet(bullet, bullets);
               }
            }
         }
      }
      if(vars[0] >= time2 && vars[0] < time3) {
         if(vars[0] == time2 + 60) {
            double shooterAngle = GameMath.getAngleTo(shooter2.getX(),shooter2.getY(), mainLevel.player.x, mainLevel.player.y);
            EnemySprite missile = new EnemySprite(shooter2.getX(), shooter2.getY(), 'h', 50);
            missile.aiType = 4;
            missile.vars[1] = 270;
            missile.vars[2] = 1;
            mainLevel.curStage.enemies.add(missile);
            
            EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
            mainLevel.curStage.enemies.add(target);
            
            missile.dependents.add(target);
            
            mainLevel.soundPlayer.play("sound/missile1.wav");
         }
         if(vars[0] == time2 + 100) {
            double shooterAngle = GameMath.getAngleTo(shooter5.getX(),shooter5.getY(), mainLevel.player.x, mainLevel.player.y);
            EnemySprite missile = new EnemySprite(shooter5.getX(), shooter5.getY(), 'h', 50);
            missile.aiType = 4;
            missile.vars[1] = 270;
            missile.vars[2] = 1;
            mainLevel.curStage.enemies.add(missile);
            
            EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
            mainLevel.curStage.enemies.add(target);
            
            missile.dependents.add(target);
            
            mainLevel.soundPlayer.play("sound/missile1.wav");
         }

      }
      if(vars[0] >= time3 && vars[0] < time4) {
         if(vars[0] < time3+60) {
            vars[2] -= 0.2;
            
            double inc = 20;
            if(difficulty == "Easy") {
              inc = 30;
            }
            if(difficulty == "Lunatic") {
              inc = 10;
            }
            
            if(vars[0] % 10 == 0) {
               mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
               double shooterAngle1 = mainLevel.rand.nextInt(360);
            
               for(int i = 0; i < 360; i+= inc) {
                  BulletSprite bullet;
                  
                  double bSpd = 1+mainLevel.rand.nextDouble()*2;
                  
                  bullet = new BulletSprite(shooter1.getX(),shooter1.getY(), 'b', new Color(0xffff00));
                  bullet.setVelocity(bSpd, shooterAngle1 + i);
                  enlistBullet(bullet, bullets);
                  
                  if(difficulty == "Hard" || difficulty == "Lunatic") {
                    bullet = new BulletSprite(shooter1.getX(),shooter1.getY(), 'b', new Color(0xffff00));
                    bullet.setVelocity(bSpd*.75, shooterAngle1 + i);
                    enlistBullet(bullet, bullets);
                  }
               }
            
            }
            
            if(vars[0] % 10 == 5) {
               mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
               double shooterAngle2 = mainLevel.rand.nextInt(360);
            
               for(int i = 0; i < 360; i+= inc) {
                  BulletSprite bullet;
                  
                  double bSpd = 1+mainLevel.rand.nextDouble()*2;
                  
                  bullet = new BulletSprite(shooter6.getX(),shooter6.getY(), 'b', new Color(0xffff00));
                  bullet.setVelocity(bSpd, shooterAngle2 + i);
                  enlistBullet(bullet, bullets);
                  
                  if(difficulty == "Hard" || difficulty == "Lunatic") {
                    bullet = new BulletSprite(shooter6.getX(),shooter6.getY(), 'b', new Color(0xffff00));
                    bullet.setVelocity(bSpd*.75, shooterAngle2 + i);
                    enlistBullet(bullet, bullets);
                  }
               }
            
            }
         }
         
         if(vars[0] == time3+100) {
            x = mainLevel.SCREENW/2 + 800;
            y = mainLevel.SCREENH-100;
            tilt = 20;
            vars[1] = -8;
            vars[2] = 0;
         }
         if(vars[0] == time3+101) {
            EnemySprite missile = new EnemySprite(mainLevel.SCREENW + 20, shooter2.getY(), 'h', 50);
            missile.aiType = 4;
            missile.vars[1] = 180;
            missile.vars[2] = 1;
            mainLevel.curStage.enemies.add(missile);
            
            EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
            mainLevel.curStage.enemies.add(target);
            
            missile.dependents.add(target);
            
            mainLevel.soundPlayer.play("sound/missile1.wav");
         }
         
         if(vars[0] == time3+141) {
            EnemySprite missile = new EnemySprite(shooter3.getX(), shooter3.getY(), 'h', 50);
            missile.aiType = 4;
            missile.vars[1] = 180;
            missile.vars[2] = 1;
            mainLevel.curStage.enemies.add(missile);
            
            EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
            mainLevel.curStage.enemies.add(target);
            
            missile.dependents.add(target);
            
            mainLevel.soundPlayer.play("sound/missile1.wav");
         }
         
         if(vars[0] == time3+240) {
            x = mainLevel.SCREENW/2 - 800;
            y = mainLevel.SCREENH/2 - 100;
            tilt = -20;
            vars[1] = 8;
            vars[2] = 0;
         }
         if(vars[0] == time3+241) {
            EnemySprite missile = new EnemySprite(-20, shooter5.getY(), 'h', 50);
            missile.aiType = 4;
            missile.vars[1] = 0;
            missile.vars[2] = 1;
            mainLevel.curStage.enemies.add(missile);
            
            EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
            mainLevel.curStage.enemies.add(target);
            
            missile.dependents.add(target);
            
            mainLevel.soundPlayer.play("sound/missile1.wav");
         }
         if(vars[0] == time3+281) {
            EnemySprite missile = new EnemySprite(shooter4.getX(), shooter4.getY(), 'h', 50);
            missile.aiType = 4;
            missile.vars[1] = 0;
            missile.vars[2] = 1;
            mainLevel.curStage.enemies.add(missile);
            
            EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
            mainLevel.curStage.enemies.add(target);
            
            missile.dependents.add(target);
            
            mainLevel.soundPlayer.play("sound/missile1.wav");
         }
         
         if(vars[0] == time3+390) {
            x = mainLevel.SCREENW/2;
            y = -500;
            tilt = 0;
            vars[1] = 0;
            vars[2] = 0;
         }
         if(vars[0] >= time3 + 350 && vars[0] <time4) {
            y += (150-y)/30.0;
         }
      }
      
      
      unEnlistDeadBullets();
      
      this.x += this.vars[1];
      this.y += this.vars[2];
      
      vars[0]++;
      if(vars[0] > time4) vars[0] = 1;
	}
	
	
	
}
