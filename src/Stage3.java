import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import gameEngine.*;

public class Stage3 extends Stage
{
	// essential members
	
	// Sprites
	
	ABGStage3Sprite background;
	
	BossSmiley bossSmiley;
	BossAKitteh adventureKitteh;
	SpriteList bossSlaves;
	
	StageTextSprite textStageNum;
	StageTextSprite textStageTitle;
	
	// misc vars
	
	int spawnerX;
	int spawnerY;
	
	int CITRUS_SPEAK_X = mainLevel.SCREENW - 200;
		int CITRUS_SPEAK_Y = 100;
		int CITRUS_LISTEN_X = mainLevel.SCREENW - 180;
		int CITRUS_LISTEN_Y = 130;
		int CITRUS_OFF_X = mainLevel.SCREENW + 50;
		int CITRUS_OFF_Y = 300;
		
		int PLAYER_SPEAK_X = 00;
		int PLAYER_SPEAK_Y = 80;
		int PLAYER_LISTEN_X = -30;
		int PLAYER_LISTEN_Y = 110;
		int PLAYER_OFF_X = -200;
		int PLAYER_OFF_Y = 300;
	
	/*
	*	Constructor
	*	Preconditions: ml is a reference to the game's main level interface which contains this stage.
	*	Postconditions: This stage is initialized and loads its data.
	*/
	
	public Stage3(MainLevel ml, YoBroMainPanel mp)
	{
		super(ml, mp);
		spawnerX = 0;
		spawnerY = 0;
	}
	
	public Stage3(MainLevel ml, YoBroMainPanel mp, long initTimer)
	{
		this(ml,mp);
		timer = initTimer;
	}
	
	
	/**
	*	void loadData()
	*	This method should load any additional sprite images or sounds/music/things that this particular stage uses.
	*	It should also initialize any initial sprites or level variables.
	*/
	
	protected void loadData()
	{
		// load additional graphics
		
		ABGStage3Sprite.loadImages(mainLevel.imgLoader);
		BossSmiley.loadImages(mainLevel.imgLoader);
		BossAKitteh.loadImages(mainLevel.imgLoader);
		
		mainLevel.imgLoader.waitForAll();
		
		// load music
		mainLevel.loopMusic("sound/music/badApple.mid");
	//	mainLevel.loopMusic("sound/Soul Food Red as Pepperoni.mid");
		
		// initialize Sprites and SpriteLists.
		
		background = new ABGStage3Sprite(mainLevel);
		
	//	bossBullets = new SpriteList();
		bossSlaves = new SpriteList();
		
		bossSmiley = new BossSmiley(-100,0, mainLevel);
		bossSmiley.stageBG = background;
		adventureKitteh = new BossAKitteh(-100, 0, mainLevel);
		adventureKitteh.stageBG = background;
		
		textStageNum = new StageTextSprite(150, 140, '3',"");
		textStageNum.setSemiTransparency(1.0);
		textStageNum.isVisible = false;
	//	textStageTitle = new StageTextSprite(200,200,'t',"Crimson Park");
		textStageTitle = new StageTextSprite(200,200,'T',"stage3");
		textStageTitle.setSemiTransparency(1.0);
		textStageTitle.isVisible = false;
		textStageTitle.vars[1] = 5;
		
	}
	
	
	/**
	*	void clean()
	*	This method should free any resources that this stage uses.
	*/
	
	protected void clean()
	{
		super.clean();
		enemies.destroyAll();
		bullets.destroyAll();
		bossSmiley.destroy();
		bossSlaves.destroyAll();
		BossSmiley.clean();
		BossAKitteh.clean();
		KyuArmSprite.clean();
		
	}
	
	/**
	*	void stageTimerLoop()
	*	This method increments the stage's timer and runs the stage's logic based on the timer's current value.
	*	Use the reference to mainLevel to have elements of the stage interact with the player, 
	*	player bullets, and the global main level variables.
	**/
	
	public void stageTimerLoop()
	{
		super.stageTimerLoop();

		int checkPt;
		
	//	System.out.println(timer);
		
		checkPt = 0;
		if(timer >= checkPt && timer < checkPt +60)
		{ // stage title appears
			textStageNum.isVisible = true;
			textStageNum.setSemiTransparency(textStageNum.getSemiTransparency()*0.95);
		
			textStageTitle.isVisible = true;
			textStageTitle.setSemiTransparency(textStageTitle.getSemiTransparency()*0.95);
			textStageTitle.vars[0]+= 18;
			textStageTitle.vars[1] -= 2.0/30;
			
			
			if(textStageTitle.vars[0] > 360)
				textStageTitle.vars[0] -= 360;
				
			textStageTitle.scale(1.0,textStageTitle.vars[1] * GameMath.cos(textStageTitle.vars[0]));
		}
		
		
		checkPt = 100;
		if(timer >= checkPt && timer < checkPt +60)
		{ // stage title disappears
			textStageNum.setSemiTransparency(textStageNum.getSemiTransparency()*1.1);
			textStageTitle.setSemiTransparency(textStageTitle.getSemiTransparency()*1.1);
			
			if(textStageNum.getSemiTransparency() >0.9)
			{
				textStageNum.isVisible = false;
				textStageTitle.isVisible = false;
			}
		}
		
		///////////// WAVE 0: stampede of smileys
		
		int stampedeCheck = 300;
		int midbossCheck = 1500;
		
		checkPt = stampedeCheck;
		if(timer >= checkPt && timer < checkPt +30)
		{
			if(mainLevel.screenShake < 100)
				mainLevel.screenShake = 100;
		}
		if(timer >= checkPt && timer < checkPt +60)
		{
			FXSprite dust = FXSprite.makeSimpleOrb1(mainLevel.rand.nextInt(mainLevel.SCREENW),mainLevel.SCREENH-mainLevel.rand.nextInt(30));
			mainLevel.sfx.add(dust);
			dust = FXSprite.makeSimpleOrb1(mainLevel.rand.nextInt(mainLevel.SCREENW),mainLevel.SCREENH-mainLevel.rand.nextInt(30));
			mainLevel.sfx.add(dust);
		}
		if(timer >= checkPt && timer < checkPt + 120)
		{
			background.vars[0]+=0.04;
		}
		
		checkPt = stampedeCheck + 60;
		if(timer >=checkPt && timer < checkPt+500)
		{
			EnemySprite enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),mainLevel.SCREENH + 20, 'c', 1);
			enemy.aiType = 1;
			enemy.vars[0] = mainLevel.rand.nextDouble()*4-2;
			enemy.vars[1] = 1-mainLevel.rand.nextDouble()*5;
			enemies.add(enemy);
			
			enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),mainLevel.SCREENH + 20, 'c', 1);
			enemy.aiType = 1;
			enemy.vars[0] = mainLevel.rand.nextDouble()*4-2;
			enemy.vars[1] = 1-mainLevel.rand.nextDouble()*4.5;
			enemies.add(enemy);
			
			if(mainLevel.screenShake < 30)
				mainLevel.screenShake = 30;
			
			
		}
		
		
		// WAVE 1 : leaping smileys
		
		
		int leapWarning = 45;
		int leapDuration = 90;
		
		{
			checkPt += 120;
			if(timer >=checkPt && timer < checkPt + leapWarning)
			{
				FXSprite dust = FXSprite.makeSimpleOrb1(50+mainLevel.rand.nextDouble()*40,mainLevel.SCREENH-mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
			}
			if(timer >=checkPt + leapWarning && timer < checkPt + leapWarning + leapDuration && timer % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(70,mainLevel.SCREENH + 20, 'c', 10);
				enemy.aiType = 2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
			}
			
			
			checkPt += leapWarning + leapDuration;
			if(timer >=checkPt && timer < checkPt + leapWarning)
			{
				FXSprite dust = FXSprite.makeSimpleOrb1(mainLevel.SCREENW-90+mainLevel.rand.nextDouble()*40,mainLevel.SCREENH-mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
			}
			if(timer >=checkPt + leapWarning && timer < checkPt + leapWarning + leapDuration && timer % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-70,mainLevel.SCREENH + 20, 'c', 10);
				enemy.aiType = 2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
			}
			
			checkPt += leapWarning + leapDuration;
			if(timer >=checkPt && timer < checkPt + leapWarning)
			{
				FXSprite dust = FXSprite.makeSimpleOrb1(50+mainLevel.rand.nextDouble()*40,mainLevel.SCREENH-mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
			}
			if(timer >=checkPt + leapWarning && timer < checkPt + leapWarning + leapDuration && timer % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(70,mainLevel.SCREENH + 20, 'c', 10);
				enemy.aiType = 2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
			}
			
			checkPt += leapWarning + leapDuration;
			if(timer >=checkPt && timer < checkPt + leapWarning)
			{
				FXSprite dust = FXSprite.makeSimpleOrb1(mainLevel.SCREENW/2-50+mainLevel.rand.nextDouble()*100,mainLevel.SCREENH-mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
				dust = FXSprite.makeSimpleOrb1(mainLevel.SCREENW/2-50+mainLevel.rand.nextDouble()*100,mainLevel.SCREENH-mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
			}
			if(timer == checkPt + leapWarning)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2,mainLevel.SCREENH + 20, 'g', 99999);
				enemy.aiType = 3;
				enemy.vars[3] = 0.1;
				enemy.vars[1] = -8.5;
				enemies.add(enemy);
			}
		}
		
		
		///////// Wave 1a : Turn around!
		
		{
			checkPt += (leapWarning + leapDuration);
			if(timer >=checkPt && timer < checkPt + 90)
			{
				background.mode7BG.cameraAngle += 2;
			}
			
			checkPt += 200;
			if(timer >= checkPt && timer < checkPt +30)
			{
				if(mainLevel.screenShake < 100)
					mainLevel.screenShake = 100;
			}
			if(timer >= checkPt && timer < checkPt +60)
			{
				FXSprite dust = FXSprite.makeSimpleOrb1(mainLevel.rand.nextInt(mainLevel.SCREENW),mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
				dust = FXSprite.makeSimpleOrb1(mainLevel.rand.nextInt(mainLevel.SCREENW),mainLevel.rand.nextInt(30));
				mainLevel.sfx.add(dust);
				
			//	background.vars[0]+=0.1;
			}
			if(timer >= checkPt && timer < checkPt + 120)
			{
				background.vars[0]+=0.03;
			}
			
			checkPt += 60;
			if(timer >=checkPt && timer < checkPt+230)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),-20, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = mainLevel.rand.nextDouble()*4-2;
				enemy.vars[1] = mainLevel.rand.nextDouble()*5;
				enemy.vars[3] = -0.1;
				enemies.add(enemy);
				
				enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),-20, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = mainLevel.rand.nextDouble()*4-2;
				enemy.vars[1] = mainLevel.rand.nextDouble()*4.5;
				enemy.vars[3] = -0.1;
				enemies.add(enemy);
				
				if(mainLevel.screenShake < 30)
					mainLevel.screenShake = 30;
			}
		}
		
		//////// WAVE 1b : a few more leapers
		
		checkPt += 30;
		if(timer >=checkPt && timer < checkPt + leapWarning)
		{
			FXSprite dust = FXSprite.makeSimpleOrb1(mainLevel.SCREENW-90+mainLevel.rand.nextDouble()*40,mainLevel.rand.nextInt(30));
			mainLevel.sfx.add(dust);
			
			dust = FXSprite.makeSimpleOrb1(50+mainLevel.rand.nextDouble()*40,mainLevel.rand.nextInt(30));
			mainLevel.sfx.add(dust);
		}
		if(timer >=checkPt + leapWarning && timer < checkPt + leapWarning + leapDuration*3 && timer % 10 == 0)
		{
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-70,-20, 'c', 10);
			enemy.aiType = 2;
			enemy.vars[3] = -0.13;
			enemies.add(enemy);
			
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			
			enemy = new EnemySprite(70,-20, 'c', 10);
			enemy.aiType = 2;
			enemy.vars[3] = -0.13;
			enemies.add(enemy);
			
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
		}
		
		
		if(timer >= checkPt && timer < checkPt +30)
		{
			if(mainLevel.screenShake < 100)
				mainLevel.screenShake = 100;
		}
		
		///// MIDBOSS
		
		checkPt += 200;
		
		if(timer >= checkPt && timer <= checkPt +50)
		{
			if(timer == checkPt)
			{
				bossSmiley.aiMode = 0;
				bossSmiley.vars[0] = mainLevel.SCREENW/2;
				bossSmiley.vars[1] = 100;
				bossSmiley.setDirection('E');
				
				bossSmiley.itemDrops.add(makeItem("p"));
				bossSmiley.itemDrops.add(makeItem("p"));
				bossSmiley.itemDrops.add(makeItem("p"));
				bossSmiley.itemDrops.add(makeItem("p"));
				bossSmiley.itemDrops.add(makeItem("P"));
				
			/*	for(Object o: bullets)
				{
					BulletSprite bullet = (BulletSprite) o;
					mainLevel.createBulletPoint(bullet);
					bullet.destroy();
				}*/
				for(Object o: mainLevel.powerUps)
				{
					ItemSprite item = (ItemSprite) o;
					item.isMagneted = true;
				}
			}
			if(timer == checkPt + 1) // freeze the timer so that we don't progress any further until the midboss has been dealt with.
				timerPaused = true;
			
			this.runMidBossLogic();
		}
		
		checkPt += (leapWarning + leapDuration);
		if(timer >=checkPt && timer < checkPt + 90)
		{
			background.mode7BG.cameraAngle -= 2;
			background.vars[0]-=0.06;
		}
		checkPt += 300;
		if(timer >= checkPt && timer <= checkPt+1) // midboss 2!
		{
			if(timer == checkPt)
			{
				adventureKitteh.aiMode = 1;
				adventureKitteh.x =  -100;
				adventureKitteh.isSlashing = true;
				adventureKitteh.vars[0] = mainLevel.SCREENW/2;
				adventureKitteh.vars[1] = 100;
				adventureKitteh.setDirection('E');
				adventureKitteh.itemDrops.add(makeItem("1"));
				
				for(Object o: bullets)
				{
					BulletSprite bullet = (BulletSprite) o;
					mainLevel.createBulletPoint(bullet);
					bullet.destroy();
				}
			}
			if(timer == checkPt + 1) // freeze the timer so that we don't progress any further until the midboss has been dealt with.
				timerPaused = true;
			
			this.runBossLogic();
		}
		
		/////// WAVE 2
		
		checkPt += 30;
		if(timer == checkPt)
		{ 
			for(int i = 20; i < 150; i+=50)
			{
				EnemySprite enemy = new EnemySprite(i,-10, 'c', 30);
				enemy.aiType = 4;
				enemy.vars[0] = 270;
				enemy.vars[1] = 5;
				enemies.add(enemy);
				enemy.itemDrops.add(makeItem("PppS"));
			}
		}
		
		checkPt += 80;
		if(timer == checkPt)
		{ 
			for(int i = mainLevel.SCREENW-150; i < mainLevel.SCREENW-80; i+=50)
			{
				EnemySprite enemy = new EnemySprite(i,-10, 'c', 30);
				enemy.aiType = 4;
				enemy.vars[0] = 270;
				enemy.vars[1] = 5;
				enemies.add(enemy);
				enemy.itemDrops.add(makeItem("PppS"));
			}
		}
		
		checkPt += 40;
		if(timer == checkPt)
		{ 
			for(int i = mainLevel.SCREENW-80; i < mainLevel.SCREENW-20; i+=50)
			{
				EnemySprite enemy = new EnemySprite(i,-10, 'c', 30);
				enemy.aiType = 4;
				enemy.vars[0] = 270;
				enemy.vars[1] = 5;
				enemies.add(enemy);
				enemy.itemDrops.add(makeItem("PppS"));
			}
		}
		
		checkPt += 40;
		if(timer == checkPt)
		{ 
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2,-10, 'c', 200);
			enemy.aiType = 5;
			enemies.add(enemy);
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));

			
		}
		
		
		checkPt += 120;
		if(timer == checkPt)
		{ 
			for(int i = 20; i < mainLevel.SCREENW - 20; i+=100)
			{
				EnemySprite enemy = new EnemySprite(i,-10, 'c', 30);
				enemy.aiType = 4;
				enemy.vars[0] = 270;
				enemy.vars[1] = 5;
				enemies.add(enemy);
				enemy.itemDrops.add(makeItem("PppS"));
			}
		}
		
		checkPt += 180;
		if(timer == checkPt)
		{ 
			for(int i = mainLevel.SCREENW/2-80; i < mainLevel.SCREENW/2+80; i+=50)
			{
				EnemySprite enemy = new EnemySprite(i,-10, 'c', 30);
				enemy.aiType = 4;
				enemy.vars[0] = 270;
				enemy.vars[1] = 5;
				enemies.add(enemy);
				enemy.itemDrops.add(makeItem("PppS"));
			}
		}
		
		
		checkPt += 120;
		if(timer == checkPt)
		{ 
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/4,-10, 'c', 200);
			enemy.aiType = 5;
			enemies.add(enemy);
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			
			enemy = new EnemySprite(mainLevel.SCREENW/4*3,-10, 'c', 200);
			enemy.aiType = 5;
			enemies.add(enemy);
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
			enemy.itemDrops.add(makeItem("PppS"));
		}
		
		checkPt += 180;
		// last wave of leapers
		if(timer >=checkPt && timer < checkPt + leapDuration*2 && timer % 10 == 0)
		{
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-70,-20, 'c', 10);
			enemy.aiType = 2;
			enemy.vars[3] = -0.13;
			enemies.add(enemy);
			
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			
			enemy = new EnemySprite(70,-20, 'c', 10);
			enemy.aiType = 2;
			enemy.vars[3] = -0.13;
			enemies.add(enemy);
			
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
			enemy.itemDrops.add(makeItem("ppSS"));
		}
		
		
		
		checkPt += 320;
		if(timer >= checkPt && timer <= checkPt +50) // Boss for real this time.
		{
			if(timer == checkPt)
			{
				adventureKitteh.aiMode = 7;
				adventureKitteh.vars[0] = mainLevel.SCREENW/2;
				adventureKitteh.vars[1] = 100;
				adventureKitteh.setDirection('E');
				
				for(Object o: bullets)
				{
					BulletSprite bullet = (BulletSprite) o;
					mainLevel.createBulletPoint(bullet);
					bullet.destroy();
				}
				for(Object o: mainLevel.powerUps)
				{
					ItemSprite item = (ItemSprite) o;
					item.isMagneted = true;
				}
			}
			if(timer == checkPt + 1) // freeze the timer so that we don't progress any further until the midboss has been dealt with.
				timerPaused = true;
			
			this.runBossLogic();
		}
		
		// TEST LOOP AROUND
		
		if(timerPaused)
			timer--;
		
		checkPt += 200;
		if(timer > checkPt)
			timer = 0;
		
		this.runEnemyPatterns();
		this.runBulletPatterns();
		this.enemy2BulletCollisions();
		
		this.updateSpriteLists();
	}
	
	
	
	private void runEnemyPatterns()
	{
		for(Object o : enemies)
		{
			EnemySprite enemy = (EnemySprite) o;
			
			if(enemy.aiType == 0)
			{
				System.out.println("bad ai");
				enemy.destroy();
			}
			if(enemy.aiType == 1) // stampeding smileys
			{
				if(enemy.vars[3] == 0)
					enemy.vars[3] = 0.1;
					
				enemy.x += enemy.vars[0];
				enemy.y += enemy.vars[1];
				enemy.vars[1] += enemy.vars[3];
				
				if(enemy.vars[1]*enemy.vars[3] >= 0 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 2) // leaping smileys
			{
				if(enemy.vars[3] == 0)
					enemy.vars[3] = 0.13;
				double accel = enemy.vars[3];
				double initV = 10.0;
				if(enemy.vars[2] == 0) // aim at player
				{
					enemy.vars[2] = 1;
					double dX = Math.abs(mainLevel.player.x - enemy.x);
					double dY = mainLevel.player.y - enemy.y;
					double lamda = (accel*dX*dX)/(2*initV*initV);
					double theta;
					double u = (dX + Math.sqrt(dX*dX - 4*(lamda*lamda - lamda*dY)))/(2*lamda);
				//	System.out.println("u " + u);
					
					if(!Double.isNaN(u))
					{
						theta = (Math.atan(u));
						if(mainLevel.player.x - enemy.x < 0)
							theta = Math.PI - theta;
					}
					else
					{
						theta = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y)/180.0*Math.PI;
					}
					
				//	System.out.println("enemy launch angle: " + (theta*180/Math.PI));
					
					enemy.vars[0] = initV*Math.cos(theta);
					enemy.vars[1] = 0-initV*Math.sin(theta);
					
				}
				
				if(enemy.vars[2] == 1 && Math.abs(enemy.vars[1]) < 8)
				{
					mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
				
					double angle = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
					enemy.vars[2] = 2;
					BulletSprite bullet;
					
					if(enemy.vars[3] < 0 && mainLevel.difficulty != "Easy")
					{
						bullet = new BulletSprite(enemy.x, enemy.y, 'e', new Color(0x5500FF));
						bullet.setVelocity(2.5, angle+20+mainLevel.rand.nextInt(11)-5);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'e', new Color(0x5500FF));
						bullet.setVelocity(2.5, angle-20+mainLevel.rand.nextInt(11)-5);
						bullets.add(bullet);
					}

					bullet = new BulletSprite(enemy.x, enemy.y, 'e', new Color(0x5500FF));
					bullet.setVelocity(2, angle+mainLevel.rand.nextInt(11)-5);
					bullets.add(bullet);
					
					
				}
				
				
				
				enemy.x += enemy.vars[0];
				enemy.y += enemy.vars[1];
				enemy.vars[1] += accel;
				
				if(enemy.vars[1]*enemy.vars[3] >= 0 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 3) /////////////// midboss sneak peak
			{	
				enemy.y += enemy.vars[1];
				
				if(Math.abs(enemy.vars[1]) > 0.1)
					enemy.vars[1] += enemy.vars[3];
				else
				{
					int bulletInterval = 5;
					int bulletSpacing = 35;
					
					if(difficulty == "Lunatic") {
						bulletInterval = 3;
						bulletSpacing = 41;
					}
					
					if(enemy.vars[0] % bulletInterval == 0)
					{
						mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
						
						for(int i = 0; i < 1; i++)
						{
							for(double j = 1.5; j <= 3.5; j++)
							{
								BulletSprite bullet = new BulletSprite(enemy.x - 50*GameMath.cos(enemy.vars[0]), enemy.y + 50*GameMath.sin(enemy.vars[0]), 'b', new Color(0xFF00CC));
								bullet.setVelocity(j, 90 + enemy.vars[0]*11 - i*bulletSpacing+j*2);
								bullets.add(bullet);
								
								bullet = new BulletSprite(enemy.x + 50*GameMath.cos(enemy.vars[0]), enemy.y + 50*GameMath.sin(enemy.vars[0]), 'b', new Color(0xFF00CC));
								bullet.setVelocity(j, 90 - enemy.vars[0]*11 + i*bulletSpacing-j*2);
								bullets.add(bullet);
							}
						}
						
						if(mainLevel.difficulty == "Hard" || mainLevel.difficulty == "Lunatic") {
							int randAngle = mainLevel.rand.nextInt(360);
							BulletSprite bullet = new BulletSprite(enemy.x - 50*GameMath.cos(randAngle), enemy.y + 50*GameMath.sin(randAngle), 'd', new Color(0xAA0055));
							bullet.setVelocity(mainLevel.rand.nextDouble()+1, randAngle);
							bullet.scale(2,2);
							bullets.add(bullet);
							
							if(mainLevel.difficulty == "Lunatic") {
								randAngle = mainLevel.rand.nextInt(360);
								bullet = new BulletSprite(enemy.x - 50*GameMath.cos(randAngle), enemy.y + 50*GameMath.sin(randAngle), 'd', new Color(0xAA0055));
								bullet.setVelocity(mainLevel.rand.nextDouble()+1, randAngle);
								bullet.scale(2,2);
								bullets.add(bullet);
							}
						}
					}
				
					enemy.vars[0]++;
					if(enemy.vars[0] > 120)
					{
						enemy.vars[1] = -0.2;
						enemy.vars[3] = -0.1;
					}
				}
				
				if(enemy.vars[1]*enemy.vars[3] >= 0 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 4) /////////////// team smileys
			{
				enemy.x += GameMath.cos(enemy.vars[0])*enemy.vars[1];
				enemy.y -= GameMath.sin(enemy.vars[0])*enemy.vars[1];
				
				if(enemy.vars[1] > 0.5 && enemy.vars[0] == 270)
				{
					enemy.vars[1] *= 0.95;
				}
				else if(enemy.vars[2] < 120)
				{
					int bulletInterval = 5;
					if(difficulty == "Easy")
						bulletInterval = 8;
					if(difficulty == "Hard")
						bulletInterval = 4;
					if(difficulty == "Lunatic")
						bulletInterval = 3;
					
					if(enemy.vars[2] % bulletInterval == 0)
					{
						mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
						
						for(double i = 1.0; i <= 2.0; i += 1)
						{
							if(enemy.x <= mainLevel.SCREENW/2)
							{
								BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x6600BB));
								bullet.setVelocity(i, 60 - enemy.vars[2]*2);
								bullets.add(bullet);
							}
							else
							{
								BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x6600BB));
								bullet.setVelocity(i, 120 + enemy.vars[2]*2);
								bullets.add(bullet);
							}
						}
					}
					enemy.vars[2]++;
				}
				else
				{
					if(enemy.x <= mainLevel.SCREENW/2)
					{
						enemy.vars[1] = 2;
						enemy.vars[0] += (180 - enemy.vars[0])/20;
					}
					else
					{
						enemy.vars[1] = 2;
						enemy.vars[0] += (360 - enemy.vars[0])/20;
					}
				}
				
				if(enemy.vars[2] >= 120 && isOffscreen(enemy))
					enemy.destroy();
			}
			else if(enemy.aiType == 5) // powerful smiley
			{
				enemy.skinID = 2;
				if(enemy.vars[0] == 0)
				{
					enemy.y += (100 - enemy.y)/15.0;
					if(enemy.y > 95)
					{
						enemy.vars[0] = 1;
					}
				}

				if(enemy.vars[0] == 1)
				{
					double angle = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
					
					if(enemy.vars[1] % 60 == 0) {
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'i', new Color(0x6600BB));
						bullet.setVelocity(2, angle);
						bullets.add(bullet);
						
						for(int i = 0; i < 6; i++) {
							bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0x6600BB));
							bullet.scale(2.0, 2.0);
							bullet.setVelocity(1.7 + mainLevel.rand.nextDouble(), angle+mainLevel.rand.nextInt(20)-10);
							bullets.add(bullet);
						}
					}
					
					int bulletInterval = 3;
					if(difficulty == "Easy")
						bulletInterval = 6;
					if(difficulty == "Hard")
						bulletInterval = 2;
					if(difficulty == "Lunatic")
						bulletInterval = 2;
					
					if(enemy.vars[1] % bulletInterval == 0) {
						mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
						
						double angleOff = GameMath.sin(enemy.vars[1]*5)*90;
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'c', new Color(0x6600BB));
						bullet.isInverted = true;
						bullet.setVelocity(3, angle + angleOff);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'c', new Color(0x6600BB));
						bullet.isInverted = true;
						bullet.setVelocity(3, angle - angleOff);
						bullets.add(bullet);
					}
					
					
					/*
					double alpha = enemy.vars[1]/250;
					double speed = alpha*4 + (1-alpha)*0.5;
					
					if(enemy.vars[1] % 5 == 0)
					{
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'c', new Color(0x6600BB));
						bullet.isInverted = true;
						bullet.setVelocity(speed, enemy.vars[1]*2);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'c', new Color(0x6600BB));
						bullet.isInverted = true;
						bullet.setVelocity(speed, enemy.vars[1]*2+180);
						bullets.add(bullet);
					}
					if(enemy.vars[1] % 5 == 2)
					{
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'c', new Color(0x6600BB));
						bullet.isInverted = true;
						bullet.setVelocity(speed, 0-enemy.vars[1]*2);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'c', new Color(0x6600BB));
						bullet.isInverted = true;
						bullet.setVelocity(speed, 0-enemy.vars[1]*2+180);
						bullets.add(bullet);
					}
					
					*/
					
					enemy.vars[1]++;
					
					if(enemy.vars[1] > 250)
						enemy.vars[0] = 2;
				}
				if(enemy.vars[0] == 2)
				{
					enemy.y++;
					
					if(isOffscreen(enemy))
						enemy.destroy();
				}
				
				
				
			}
			
			
			
			quadTree.insert(enemy);
			
		}
	}
	
	private void runMidBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "D;<";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		
		// AI modes
		
		if(bossSmiley.aiMode == 0) // glide into playable area
		{
			bossSmiley.x += (bossSmiley.vars[0] - bossSmiley.x)/15.0;
			bossSmiley.y += (bossSmiley.vars[1] - bossSmiley.y)/15.0;
			
			if(Math.abs(bossSmiley.x - bossSmiley.vars[0]) < 1.0 && Math.abs(bossSmiley.y - bossSmiley.vars[1]) < 1.0)
			{
				bossSmiley.aiMode = 1;
			}
		}
		else if(bossSmiley.aiMode == 1) // stop for midboss dialogue (if any)
		{
			bossSmiley.isActive = false;
			bossSmiley.setDirection('S');

			// Dialogue happens
			
			// After dialogue, initiate first attack
			
			bossSmiley.aiMode = 2;
			bossSmiley.timer = 0;
		}
		else if(bossSmiley.aiMode == 2) // begin midboss attack pattern
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			bossSmiley.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)bossSmiley.hp;
			
			if(bossSmiley.attackIndex == 2)
				bossSmiley.aiMode = 3;
		}
		else if (bossSmiley.aiMode == 3)
		{
			bossSmiley.isActive = false;
			bossSmiley.isRoaring = false;
			background.nextBGID = 0;
			bossSmiley.aiMode = 4;
			timer++;
			timerPaused = false;
					
		}
		
		
		
		quadTree.insert(bossSmiley);
	}
	
	private void runBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "Adventure Kitteh";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		if(!adventureKitteh.portrait.emote.equals("attack"))
		{
			adventureKitteh.portrait.vars[0] = CITRUS_OFF_X;
			adventureKitteh.portrait.vars[1] = CITRUS_OFF_Y;
		}
		
		if (adventureKitteh.aiMode == 1)
		{
			
			adventureKitteh.isActive = false;
			
			adventureKitteh.vars[0] = mainLevel.SCREENW/2;
			adventureKitteh.vars[1] = 100;
			adventureKitteh.x += (adventureKitteh.vars[0] - adventureKitteh.x)/15.0;
			adventureKitteh.y += (adventureKitteh.vars[1] - adventureKitteh.y)/15.0;
			
			if(Math.abs(adventureKitteh.x - adventureKitteh.vars[0]) < 1)
				adventureKitteh.isSlashing = false;
			runDialogScript();
			
			//System.out.println(adventureKitteh.portrait.x + " " + adventureKitteh.portrait.y);
		}
		else if(adventureKitteh.aiMode == 2) // begin midboss attack patterns
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			adventureKitteh.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)adventureKitteh.hp;
		}
		else if(adventureKitteh.aiMode == 3)
		{
			adventureKitteh.aiMode = 4;
			adventureKitteh.isActive = false;
			
			mainLevel.barBossHealth.isVisible = false;
			mainLevel.numberSCTimer.isVisible = false;
			
			mainLevel.dBox.paragraph++;
			background.nextBGID = 0;
			adventureKitteh.attackIndex++;
		}
		else if(adventureKitteh.aiMode == 4)
		{
			adventureKitteh.vars[0] = mainLevel.SCREENW/2;
			adventureKitteh.vars[1] = 100;
			adventureKitteh.x += (adventureKitteh.vars[0] - adventureKitteh.x)/15.0;
			adventureKitteh.y += (adventureKitteh.vars[1] - adventureKitteh.y)/15.0;
			
			runDialogScript();
		}
		else if(adventureKitteh.aiMode == 5) // leave screen, drop 1-up
		{
			mainLevel.barBossHealth.caption1 = "";
			adventureKitteh.vars[0] = mainLevel.SCREENW/2;
			adventureKitteh.vars[1] = -100;
			adventureKitteh.x += (adventureKitteh.vars[0] - adventureKitteh.x)/15.0;
			adventureKitteh.y += (adventureKitteh.vars[1] - adventureKitteh.y)/15.0;
			
			runDialogScript();
		}
		else if (adventureKitteh.aiMode == 6)
		{
			adventureKitteh.isActive = false;
			background.nextBGID = 0;
			adventureKitteh.y += (adventureKitteh.vars[1] - adventureKitteh.y)/15.0;
			
			if(mainLevel.reyPortrait.x < PLAYER_OFF_X + 10 && adventureKitteh.y < -10)
			{
				timer++;
				timerPaused = false;
				mainLevel.dBox.paragraph++;
				adventureKitteh.aiMode = 7;
			}
		}
		else if(adventureKitteh.aiMode == 7)
		{
			mainLevel.barBossHealth.caption1 = "Adventure Kitteh";
			adventureKitteh.vars[0] = mainLevel.SCREENW/2;
			adventureKitteh.vars[1] = 100;
			adventureKitteh.x += (adventureKitteh.vars[0] - adventureKitteh.x)/15.0;
			adventureKitteh.y += (adventureKitteh.vars[1] - adventureKitteh.y)/15.0;
			
			runDialogScript();
		}
		else if(adventureKitteh.aiMode == 8)
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			adventureKitteh.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)adventureKitteh.hp;
			
			if(adventureKitteh.attackIndex == adventureKitteh.lastAttackIndex + 1)
			{
				adventureKitteh.aiMode = 9;
				adventureKitteh.isActive = false;
				mainLevel.dBox.paragraph++;
				background.nextBGID = 2;
				mainLevel.barBossHealth.caption1 = "";
			}
		}
		else if(adventureKitteh.aiMode == 9)
		{
			runDialogScript();
		}
		else if(adventureKitteh.aiMode == 10)
		{
			this.endStage = true;
		}
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.x += (mainLevel.reyPortrait.vars[0] - mainLevel.reyPortrait.x) * 0.05;
			mainLevel.reyPortrait.y += (mainLevel.reyPortrait.vars[1] - mainLevel.reyPortrait.y) * 0.05;
		}
		if(!adventureKitteh.portrait.emote.equals("attack"))
		{
			adventureKitteh.portrait.x += (adventureKitteh.portrait.vars[0] - adventureKitteh.portrait.x) * 0.05;
			adventureKitteh.portrait.y += (adventureKitteh.portrait.vars[1] - adventureKitteh.portrait.y) * 0.05;
		}
		
		quadTree.insert(adventureKitteh);
	}
	
	
	private void runDialogScript()
	{
		bossSmiley.isActive = false;
		bossSmiley.setDirection('S');
		adventureKitteh.isActive = false;
		//adventureKitteh.setDirection('S');
		
		// Dialogue happens
		
		DialogBoxSprite dBox = mainLevel.dBox;
		dBox.isVisible = true;
		
		if(keyboard.isPressed(config.VK_SKIP) || keyboard.isTyped(config.VK_SHOT))
			dBox.paragraph++;
		
		int para = 0;

		if(dBox.paragraph == para)
		{
			//setPlayerPortrait(":)","speak");
			setCitrusPortrait(">:O","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "MYAAAAAAAA!!!! Your terror \n" +
							"streak ends here, Mr. Zombie!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			//setPlayerPortrait(":)","speak");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Prepare to be smitten in a\n" +
							"smity smiting of smitiness, myaa!!";
			adventureKitteh.setDirection('S');
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			adventureKitteh.portrait.setSemiTransparency( 0.5);
			adventureKitteh.aiMode = 2;
			mainLevel.barBossHealth.lagValue = 0;
			adventureKitteh.timer = 0;
			dBox.isVisible = false;
		}
		para++;
		
		
		if(dBox.paragraph == para)
		{
		//	setPlayerPortrait(":)","speak");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "He's strong! I'd better\n" +
							"bail for now, myaa.";
		}
		para++;
		
		
		if(dBox.paragraph == para)
		{
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			adventureKitteh.portrait.setSemiTransparency( 0.5);
			adventureKitteh.aiMode = 5;
			mainLevel.barBossHealth.lagValue = 0;
			adventureKitteh.timer = 0;
			dBox.isVisible = false;
			dBox.paragraph++;
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Hold up, attack cat!\n" +
							"Not sure what yo problem is,";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "but let's just chill, aight?\n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			adventureKitteh.portrait.setSemiTransparency( 0.5);
			adventureKitteh.aiMode = 6;
			mainLevel.barBossHealth.lagValue = 0;
			adventureKitteh.timer = 0;
			dBox.isVisible = false;
		}
		para++;
		
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Geez, you're pretty quick for a zombie!\n" +
							"Just what are you, a vampire, myaa?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Who, me? A zombie?\n" +
							"Bro, you've watched way too many";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Zombie thriller flicks..." +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","listen");
			setCitrusPortrait("TuT","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Well, you look as pale as the\n" +
							"undead to me...";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Can't help it, kitten.\n" +
							"The Artist drew me this way!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Hater gonna hate?\n" +
							"";
		}
		para++;
		
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("TuT","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Well, how about you prove it then...\n" +
							"Everyone knows that undead are evil.";
		}
		para++;
		
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "The Sword of Powah which I wield\n" +
							"is a bane to zombies, demons, ";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "tentacled horrors, clowns, ponies, \n" +
							"werewolves, and so on... myaa.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "If you truly aren't an undead villain \n" +
							"leading these yellow head things in";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "in an anarchic revolt, then you\n" +
							"won't be reduced to a pile of dust!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(">:(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "!!\n" +
							"";
		}
		para++;
		
		// Cue start of boss fight
		
		if(dBox.paragraph == para)
		{
			mainLevel.loopMusic("sound/music/advKitteh.mid");
			
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			adventureKitteh.portrait.setSemiTransparency( 0.5);
			adventureKitteh.aiMode = 8;
			mainLevel.barBossHealth.lagValue = 0;
			adventureKitteh.timer = 0;
			dBox.isVisible = false;
		}
		para++;
		
		// Dialogue after boss is defeated
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "I win, kitty cat!\n" +
							"Now, perhaps you can tell me";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "what's with these haters?\n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "You mean the scary, yellow, toothy\n" +
							"head things, mya?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "I thought they were part of your\n" +
							"super-evil, demonic, undead army!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "For real? No way, man!\n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Them lil' toothy jive turkeys\n" +
							"pilfered my pizza!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Myaa! So then you didn't come from\n" +
							"that ominous death pillar in the sky?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "?\n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Haven't you noticed that big \n" +
							"flying coorporate monolith?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Nope, can't say I have...\n" +
							"Think you could point me to it?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "I mean, we cool, right?\n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Well, I guess is has some sort of \n" +
							"camouflage or something, mya...";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "I'll take you to where I spotted it \n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Just please don't suck my blood \n" +
							"or nom on my brains, mya!!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":(","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "*facepalm*\n" +
							"";
		}
		para++;
		
		// end dialogue, end stage
		
		if(dBox.paragraph == para)
		{
			adventureKitteh.aiMode = 10;
		}
		para++;
	}
	
	
	
	
	private void setPlayerPortrait(String emote, String mode)
	{
		
		mainLevel.reyPortrait.emote = emote;
		
		if(mode.equals("speak"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_SPEAK_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_SPEAK_Y;
			mainLevel.reyPortrait.setSemiTransparency(0.0);
		}
		if(mode.equals("listen"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_LISTEN_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_LISTEN_Y;
			mainLevel.reyPortrait.setSemiTransparency(0.5);
		}
	}
	
	
	private void setCitrusPortrait(String emote, String mode)
	{
		
		adventureKitteh.portrait.emote = emote;

		if(mode.equals("speak"))
		{
			adventureKitteh.portrait.vars[0] = CITRUS_SPEAK_X;
			adventureKitteh.portrait.vars[1] = CITRUS_SPEAK_Y;
			adventureKitteh.portrait.setSemiTransparency(0.0);
		}
		if(mode.equals("listen"))
		{
			adventureKitteh.portrait.vars[0] = CITRUS_LISTEN_X;
			adventureKitteh.portrait.vars[1] = CITRUS_LISTEN_Y;
			adventureKitteh.portrait.setSemiTransparency(0.5);
		}
	}
	
	
	
	private void runBulletPatterns()
	{
		for(Object o : bullets)
		{
			BulletSprite bullet = (BulletSprite) o;
			
			bullet.move();
			
			if(isOffscreen(bullet) && bullet.screenKillable)
				bullet.destroy();
			
			quadTree.insert(bullet);
		}
	}
	
	
	private boolean isOffscreen(Sprite s)
	{
		if(s.x < 0-s.width * 2)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
		if(s.x > mainLevel.SCREENW + s.width *2)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
		if(s.y < 0-s.height * 2)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
		if(s.y > mainLevel.SCREENH + s.height * 2)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
			
		return false;
	}
	
	
	/**
	*	void enemy2BulletCollisions()
	*	checks for collisions between player bullets and enemies. If a collision occurs, that player bullet is destroyed and that enemy takes damage.
	*	
	*
	**/
	
	private void enemy2BulletCollisions()
	{
		for(Object o : enemies)
		{
			EnemySprite smiley = (EnemySprite) o;
			ArrayList<Sprite> cList = quadTree.query(smiley);
			
			for(Sprite s : cList)
			{
				if(s instanceof PlayerBulletSprite)
				{
					PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
					
					if(!smiley.isSpawner && smiley.collision(pBullet))
					{
						if(smiley.damage(pBullet.damage))
						{
							killEnemy(smiley);
							break;
						}
						
						if(!pBullet.permanent)
							pBullet.destroy();
						
						
					}
				}
			}
			
		}

		
		// collisions for midboss/boss
		ArrayList<Sprite> cList = quadTree.query(bossSmiley);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(bossSmiley.collision(pBullet) && bossSmiley.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(bossSmiley.damage(pBullet.damage))
					{
						mainLevel.spawnItems(bossSmiley.itemDrops, bossSmiley.x, bossSmiley.y);
						bossSmiley.isActive = false;
						bossSmiley.timer = 0;
						bossSmiley.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						bossSmiley.attackIndex++;
						break;
					}
					
				}
				
				
			}
		}
		
		cList = quadTree.query(adventureKitteh);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(adventureKitteh.collision(pBullet) && adventureKitteh.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(adventureKitteh.damage(pBullet.damage))
					{
						mainLevel.spawnItems(adventureKitteh.itemDrops, adventureKitteh.x, adventureKitteh.y);
						adventureKitteh.isActive = false;
						adventureKitteh.timer = 0;
						adventureKitteh.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						adventureKitteh.attackIndex++;
						break;
					}
					
				}
			}
		}
		
	}
	
	private void killEnemy(EnemySprite smiley)
	{
		mainLevel.spawnItems(smiley.itemDrops, smiley.x, smiley.y);
						
		FXSprite splosion;
		splosion = FXSprite.makeBurst1(smiley.x, smiley.y);
		mainLevel.sfx.add(splosion);
	/*	for(int i = 0; i < 10; i++)
		{
		//	splosion = FXSprite.makeSimpleOrb2(smiley.x, smiley.y);
			splosion = FXSprite.makeBurst1(smiley.x, smiley.y);
			mainLevel.sfx.add(splosion);
		}*/
		
		// run death throes bullet patterns, then destroy
		
		enemyDeathThroes(smiley);
		mainLevel.soundPlayer.play("sound/zap01.wav");
		smiley.destroy();
	}
	
	/**
	*	enemyDeathThroes(EnemySprite enemy)
	*	Called by enemy2BulletCollisions() in the event of an enemy's death. If the enemy's aiType has any 
	*	death throes bullet pattern, then it is executed here.
	**/
	
	private void enemyDeathThroes(EnemySprite enemy)
	{
		/*if(enemy.aiType == 1 && difficulty != "Easy")
		{	
			double initAngle = mainLevel.rand.nextInt(360);
			
			int spacing = 90;
			
			if(difficulty == "Lunatic")
				spacing = 45;
			
			for(double i = initAngle; i < initAngle + 360; i+= spacing)
			{
				BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xFFFF00));
				bullet.setVelocity(2, i);
				bullets.add(bullet);
			}
		}*/
		if(enemy.aiType == 2 && (difficulty == "Hard" || difficulty == "Lunatic")) {
			double initAngle = mainLevel.rand.nextInt(360);
			int spacing = 180;
			if(difficulty == "Lunatic")
				spacing = 120;
				
			for(double i = initAngle; i < initAngle + 360; i+= spacing)
			{
				BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0xFFFF00));
				bullet.setVelocity(mainLevel.rand.nextDouble()+0.5, i);
				bullets.add(bullet);
			}
		}
		
	}
	
	
	
	
	/**
	*	void updateSpriteLists()
	*	updates the stage's SpriteLists after collisions have been dealed with.
	*
	**/
	
	private void updateSpriteLists()
	{	
		enemies.update();
		bullets.update();
	}
	
	
	/*
	*	Point getNearestEnemy(double x, double y)
	*	gets the enemy closest to (x,y). Returns null if there are no enemies.
	*	This is used by the Homing Yoyo attack.
	*/
	
	public Sprite getNearestEnemy(double x, double y)
	{
		Sprite nearestEnemy = null;
		double smallestDist = 0;
		
		for(Object o : enemies)
		{
			EnemySprite enemy = (EnemySprite) o;
			double dist = getSqrDist(x,y, enemy.x, enemy.y);
			if(!enemy.isSpawner && (nearestEnemy == null || dist < smallestDist))
			{
				nearestEnemy = enemy;
				smallestDist = dist;
			}
		}
		
		if(bossSmiley.isActive)
		{
			double dist = getSqrDist(x,y, bossSmiley.x, bossSmiley.y);
			if(nearestEnemy == null || dist < smallestDist)
			{
				nearestEnemy = bossSmiley;
				smallestDist = dist;
			}
		}
		
		if(adventureKitteh.isActive)
		{
			double dist = getSqrDist(x,y, adventureKitteh.x, adventureKitteh.y);
			if(nearestEnemy == null || dist < smallestDist)
			{
				nearestEnemy = adventureKitteh;
				smallestDist = dist;
			}
		}
		
		return nearestEnemy;
	}
	
	private double getSqrDist(double x1, double y1, double x2, double y2)
	{
		return (x1-x2)*(x1-x2) + (y1-y2)*(y1-y2);
	}
	
	
	/////////// RENDERING STUFF
	
	/**
	*	void animate(ImageLoader il)
	*	This method should call the animate method for all of its sprites. It should be called in mainLevel's animateAllUnpaused() method.
	*	Preconditions: il is the mainLevel's ImageLoader.
	**/
	
	public void animate(ImageLoader il)
	{
		background.animate(il);
		enemies.animate(il);
		bullets.animate(il);
	//	bossBullets.animate(il);
		bossSlaves.animate(il);
		bossSmiley.animate(il);
		adventureKitteh.animate(il);
		adventureKitteh.portrait.animate(il);
		
		textStageNum.animate(il);
		textStageTitle.animate(il);
		
	}
	
	/**
	*	void renderEnemies(Graphics g2D)
	*	This method renders all the stage's enemies. It should be called in mainLevel's render method.
	**/
	
	public void renderEnemies(Graphics2D g2D)
	{
		enemies.render(g2D);
		bossSlaves.render(g2D);
		bossSmiley.render(g2D);
		adventureKitteh.render(g2D);
	}
	
	/**
	*	void renderBullets(Graphics g2D)
	*	This method renders all the stage's enemy bullets. It should be called in mainLevel's render method.
	**/
	
	public void renderBullets(Graphics2D g2D)
	{
		bullets.render(g2D);
	//	bossBullets.render(g2D);
	}
	
	/**
	*	void renderBackground(Graphics g2D)
	*	This method renders all the stage's background graphics. It should be called in mainLevel's render method.
	**/
	
	public void renderBackground(Graphics2D g2D)
	{
		g2D.setColor(new Color(0xFFffBB88, true));
		g2D.fillRect(0, 0, mainLevel.SCREENW, mainLevel.SCREENH);
		background.render(g2D);
	}
	
	/**
	*	void renderForeground(Graphics g2D)
	*	This method renders all the stage's foreground graphics. It should be called in mainLevel's render method.
	**/
	
	public void renderForeground(Graphics2D g2D)
	{
		adventureKitteh.portrait.render(g2D);
		textStageNum.render(g2D);
		textStageTitle.render(g2D);
	}

}


