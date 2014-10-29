import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import gameEngine.*;

public class Stage2 extends Stage
{
	// essential members
	
	// Sprites
	
	ABGStage2Sprite background;
	
	BossShurbert shurbert;
	BossSwirlyKyu swirlyKyu;
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
	
	public Stage2(MainLevel ml, YoBroMainPanel mp)
	{
		super(ml, mp);
		spawnerX = 0;
		spawnerY = 0;
	}
	
	public Stage2(MainLevel ml, YoBroMainPanel mp, long initTimer)
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
		
		ABGStage2Sprite.loadImages(mainLevel.imgLoader);
		BossShurbert.loadImages(mainLevel.imgLoader);
		BossSwirlyKyu.loadImages(mainLevel.imgLoader);
		KyuArmSprite.loadImages(mainLevel.imgLoader);
		
		mainLevel.imgLoader.waitForAll();
		
		// load music
		mainLevel.loopMusic("sound/music/strife_pluto_mmx.mid");
	//	mainLevel.loopMusic("sound/Soul Food Red as Pepperoni.mid");
		
		// initialize Sprites and SpriteLists.
		
		background = new ABGStage2Sprite(mainLevel);
		
	//	bossBullets = new SpriteList();
		bossSlaves = new SpriteList();
		
		shurbert = new BossShurbert(-50,100, mainLevel);
		shurbert.stageBG = background;
		swirlyKyu = new BossSwirlyKyu(-100, mainLevel.SCREENW/2, mainLevel);
		swirlyKyu.stageBG = background;
		
		textStageNum = new StageTextSprite(150, 140, '2',"");
		textStageNum.setSemiTransparency(1.0);
		textStageNum.isVisible = false;
	//	textStageTitle = new StageTextSprite(200,200,'t',"Chemical Lake Zone");
		textStageTitle = new StageTextSprite(200,200,'T',"stage2");
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
		shurbert.destroy();
		bossSlaves.destroyAll();
		BossShurbert.clean();
		BossSwirlyKyu.clean();
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
		
		///////////// WAVE 1: Cascade of bladebots
		
		checkPt = 100;
		if(timer >= checkPt && timer <= checkPt +300)
		{ // enemy wave 1
			if(this.timer % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),-20, 'd', 1);
				enemy.aiType = 1;
				enemy.vars[0] = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y) + mainLevel.rand.nextInt(40)-20;
				enemy.vars[1] = 3;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppPS    "));
			}
		}
		
		////////// WAVE 2: gunbots with lines of bladebots
		
		checkPt += 400;
		if(timer >= checkPt && timer <= checkPt +800)
		{ 
			if(this.timer % 70 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),-20, 'a', 50);
				enemy.aiType = 2;
				enemy.vars[0] = enemy.x;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
				enemy.itemDrops.add(makeItem("ppSS"));
			}
		}
		if(timer >= checkPt+300 && timer <= checkPt +400)
		{ 
			if(this.timer % 50 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20,200, 'd', 1);
				enemy.aiType = 1;
				enemy.vars[0] = 180;
				enemy.vars[1] = 3;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppPS    "));
			}
		}
		if(timer >= checkPt+500 && timer <= checkPt +600)
		{ 
			if(this.timer % 50 == 0)
			{
				EnemySprite enemy = new EnemySprite(-20,250, 'd', 1);
				enemy.aiType = 1;
				enemy.vars[0] = 0;
				enemy.vars[1] = 3;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppPS    "));
			}
		}
		if(timer >= checkPt+700 && timer <= checkPt +800)
		{ 
			if(this.timer % 50 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20,200, 'd', 1);
				enemy.aiType = 1;
				enemy.vars[0] = 180;
				enemy.vars[1] = 3;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppPS    "));
			}
		}
		
		checkPt += 900;
		
		
		///// MIDBOSS

		if(timer >= checkPt && timer <= checkPt +50)
		{
			if(timer == checkPt)
			{
				shurbert.aiMode = 0;
				shurbert.vars[0] = mainLevel.SCREENW/2;
				shurbert.vars[1] = 100;
				shurbert.setDirection('E');
				
				shurbert.itemDrops.add(makeItem("p"));
				shurbert.itemDrops.add(makeItem("p"));
				shurbert.itemDrops.add(makeItem("p"));
				shurbert.itemDrops.add(makeItem("p"));
				shurbert.itemDrops.add(makeItem("P"));
				
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
			
			this.runMidBossLogic();
		}
		
		
		///////////// WAVE 3: Explosive barrels
		
		
		checkPt += 100;
		int wave3 = checkPt-1;
		if(timer == checkPt)
		{ 
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2,-10, 'f', 10);
				enemy.aiType = 4;
				enemy.vars[0] = 3;
				enemies.add(enemy);
			
		}
		
		checkPt += 100;
		if(timer == checkPt)
		{ 
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/4,-10, 'f', 10);
				enemy.aiType = 4;
				enemy.vars[0] = 2;
				enemies.add(enemy);
			
		}
		checkPt += 20;
		if(timer == checkPt)
		{ 	
				EnemySprite enemy = new EnemySprite(3*mainLevel.SCREENW/4,-10, 'f', 10);
				enemy.aiType = 4;
				enemy.vars[0] = 3.5;
				enemies.add(enemy);
			
		}
		
		/*
		checkPt += 50;
		if(timer == checkPt)
		{
			timer = wave3;
		}
		*/
		
		// barrels and strafing bots
		
		checkPt += 100;
		if(timer >= checkPt && timer < checkPt + 600)
		{
			if(timer % 75 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW - 40) + 20,-10, 'f', 10);
				enemy.aiType = 4;
				enemy.vars[0] = 3.5;
				enemies.add(enemy);
			}
		}
		
		if(timer >= checkPt && timer < checkPt + 200)
		{
			if(timer % 30 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW +10,100, 'a', 10);
				enemy.aiType = 5;
				enemy.vars[0] = 180;
				enemy.vars[1] = 3;
				enemies.add(enemy);
			}
		}
		
		if(timer >= checkPt + 200 && timer < checkPt + 400)
		{
			if(timer % 30 == 0)
			{
				EnemySprite enemy = new EnemySprite(-10,60, 'a', 10);
				enemy.aiType = 5;
				enemy.vars[0] = 0;
				enemy.vars[1] = 3;
				enemies.add(enemy);
			}
		}
		
		if(timer >= checkPt + 400 && timer < checkPt + 600)
		{
			if(timer % 30 == 0)
			{
				EnemySprite enemy = new EnemySprite(-10,80, 'a', 10);
				enemy.aiType = 5;
				enemy.vars[0] = 0;
				enemy.vars[1] = 3;
				enemies.add(enemy);
			}
			if(timer % 30 == 15)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW + 10,40, 'a', 10);
				enemy.aiType = 5;
				enemy.vars[0] = 180;
				enemy.vars[1] = 3;
				enemies.add(enemy);
			}
		}
		
		checkPt += 600;
		
		///// BOSS LOGIC
		
		checkPt += 200;
		if(timer == checkPt) // clear bullets and magnet all onscreen items.
		{
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
		
		
		checkPt += 100;
		if(timer >= checkPt && timer <= checkPt +100)
		{
			if(timer == checkPt)
			{
				swirlyKyu.aiMode = 1;
				swirlyKyu.x = mainLevel.SCREENW + 20;
				swirlyKyu.vars[0] = mainLevel.SCREENW/2;
				swirlyKyu.leftClaw.x = swirlyKyu.x;
				swirlyKyu.leftClaw.y = swirlyKyu.y;
				swirlyKyu.rightClaw.x = swirlyKyu.x;
				swirlyKyu.rightClaw.y = swirlyKyu.y;
				swirlyKyu.vars[1] = 100;
				swirlyKyu.setDirection('W');
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
			if(enemy.aiType == 1) // waves of bladebots
			{
				if(enemy.vars[2] == 0)
					enemy.vars[2] = mainLevel.rand.nextInt(120);
					
				enemy.lookingAngle = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				enemy.x += enemy.vars[1]*GameMath.cos(enemy.vars[0]);
				enemy.y -= enemy.vars[1]*GameMath.sin(enemy.vars[0]);
				
				if(difficulty == "Hard" && enemy.vars[2] % 120 == 0)
				{
					mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
					double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
					
					BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'b', new Color(0xFF00CC));
					bullet.setVelocity(2, angleToPlayer);
					bullets.add(bullet);
				}
				if(difficulty == "Lunatic" && enemy.vars[2] % 60 == 0)
				{
					mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
					double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
					
					BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'b', new Color(0xFF00CC));
					bullet.setVelocity(2, angleToPlayer);
					bullets.add(bullet);
				}
				enemy.vars[2]++;
				
				if(isOffscreen(enemy))
					enemy.destroy();
				
			}
			if(enemy.aiType == 2) // waves of gunbots move to a position, shoot, then move in direction of player.
			{
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				if(Math.abs(enemy.x - enemy.vars[0]) < 4.0 && Math.abs(enemy.y - enemy.vars[1]) < 4.0)
				{
					mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
					BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
					bullet.setVelocity(2, angleToPlayer);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
					bullet.setVelocity(2.5, angleToPlayer);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
					bullet.setVelocity(3, angleToPlayer);
					bullets.add(bullet);
					
					for(double i = 20 ; i < 90; i+= 20)
					{
						bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
						bullet.setVelocity(2, angleToPlayer + i);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
						bullet.setVelocity(2.5, angleToPlayer + i);
						bullets.add(bullet);
						
						if(difficulty != "Easy")
						{
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(3, angleToPlayer + i);
							bullets.add(bullet);
						}
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
						bullet.setVelocity(2, angleToPlayer - i);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
						bullet.setVelocity(2.5, angleToPlayer - i);
						bullets.add(bullet);
						
						if(difficulty != "Easy")
						{
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(3, angleToPlayer - i);
							bullets.add(bullet);
						}
						
						if(difficulty == "Lunatic")
						{
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(1.5, angleToPlayer + i +10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(4, angleToPlayer + i + 10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(2.7, angleToPlayer + i+10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(1.5, angleToPlayer - i - 10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(4, angleToPlayer - i - 10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(2.7, angleToPlayer + i-10);
							bullets.add(bullet);

						}
						
						if(difficulty == "Hard")
						{
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(1.5, angleToPlayer + i +10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(4, angleToPlayer + i + 10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(1.5, angleToPlayer - i - 10);
							bullets.add(bullet);
							
							bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x880088));
							bullet.setVelocity(4, angleToPlayer - i - 10);
							bullets.add(bullet);

						}
					}
					
					enemy.aiType = 3;
					enemy.vars[0] = angleToPlayer;
					enemy.vars[1] = 3;
					enemy.vars[2] = 0;
				}
				else
				{
					enemy.x += (enemy.vars[0] - enemy.x)*0.05;
					enemy.y += (enemy.vars[1] - enemy.y)*0.05;
				}
			}
		/*	if(enemy.aiType == -1) // like aiType 2, but shoots a whip-like stream of bullets
			{
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				if(Math.abs(enemy.x - enemy.vars[0]) < 4.0 && Math.abs(enemy.y - enemy.vars[1]) < 4.0)
				{
					if(enemy.vars[2] == 0)
					{
						enemy.vars[3] = angleToPlayer;
					}
					if(enemy.vars[2] % 4 == 0)
					{
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x8800FF));
						bullet.setVelocity(enemy.vars[2]/32+2, enemy.vars[3]);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x8800FF));
						bullet.setVelocity(enemy.vars[2]/32+1, enemy.vars[3]-5);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x8800FF));
						bullet.setVelocity(enemy.vars[2]/32+1, enemy.vars[3]+5);
						bullets.add(bullet);
						
					}
					if(enemy.vars[2] > 40)
					{
						enemy.aiType = 3;
						enemy.vars[0] = angleToPlayer;
						enemy.vars[1] = 3;
					}
					enemy.vars[2]++;
				}
				else
				{
					enemy.x += (enemy.vars[0] - enemy.x)*0.05;
					enemy.y += (enemy.vars[1] - enemy.y)*0.05;
				}
			}*/
			if(enemy.aiType == 3) // straight-line movement
			{
				enemy.lookingAngle = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				if(enemy.vars[2] > 20)
				{
					enemy.x += enemy.vars[1]*GameMath.cos(enemy.vars[0]);
					enemy.y -= enemy.vars[1]*GameMath.sin(enemy.vars[0]);
				}
				else
					enemy.vars[2]++;
				
				if(isOffscreen(enemy))
					enemy.destroy();
				
			}
			if(enemy.aiType == 4) // barrel
			{
				enemy.y += enemy.vars[0];
				
				if(getSqrDist(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y) <= 100*100)
				{
					killEnemy(enemy);
				}
				
				if(isOffscreen(enemy))
					enemy.destroy();
				
			}
			if(enemy.aiType == 5) // strafing bots
			{
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				enemy.x += enemy.vars[1]*GameMath.cos(enemy.vars[0]);
				enemy.y -= enemy.vars[1]*GameMath.sin(enemy.vars[0]);
				
				if(enemy.vars[2] == 0)
					enemy.vars[2] = mainLevel.rand.nextInt(40);
				
				if(enemy.vars[0] == 0)
					enemy.setDirection('E');
				else
					enemy.setDirection('W');
				
				int shotCoolDown = 40;
				
				if(difficulty == "Easy")
					shotCoolDown = 60;
				if(difficulty == "Hard")
					shotCoolDown = 20;
				if(difficulty == "Lunatic")
					shotCoolDown = 20;
				
				if(enemy.vars[2] % shotCoolDown == 0)
				{
					mainLevel.soundPlayer.play("sound/Grapling.wav");
					BulletSprite bullet;
					bullet = new BulletSprite(enemy.x, enemy.y, 'm', new Color(0x7777FF));
					bullet.scale(2.0,2.0);
					bullet.setVelocity(3, angleToPlayer);
					bullets.add(bullet);
					
					if(difficulty == "Lunatic")
					{
						bullet = new BulletSprite(enemy.x, enemy.y, 'm', new Color(0x7777FF));
						bullet.scale(2.0,2.0);
						bullet.setVelocity(3, angleToPlayer+20);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y, 'm', new Color(0x7777FF));
						bullet.scale(2.0,2.0);
						bullet.setVelocity(3, angleToPlayer-20);
						bullets.add(bullet);
					}
					
				}
				
				enemy.vars[2]++;
				
				if(enemy.x < -10 && enemy.vars[0] == 180)
					enemy.destroy();
				else if(enemy.x > mainLevel.SCREENW + 10)
					enemy.destroy();
				
			}
			
			
			
			quadTree.insert(enemy);
			
		}
	}
	
	private void runMidBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "Shurbert";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		
		// AI modes
		
		if(shurbert.aiMode == 0) // glide into playable area
		{
			shurbert.x += (shurbert.vars[0] - shurbert.x)/15.0;
			shurbert.y += (shurbert.vars[1] - shurbert.y)/15.0;
			
			if(Math.abs(shurbert.x - shurbert.vars[0]) < 1.0 && Math.abs(shurbert.y - shurbert.vars[1]) < 1.0)
			{
				shurbert.aiMode = 1;
				shurbert.vars[0] = 0;
			}
		}
		else if(shurbert.aiMode == 1) // stop for midboss dialogue (if any)
		{
			shurbert.isActive = false;
			shurbert.setDirection('S');

			// Dialogue happens
			
			// After dialogue, initiate first attack
			
			shurbert.aiMode = 2;
			shurbert.timer = 0;
		}
		else if(shurbert.aiMode == 2) // begin midboss attack pattern
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			shurbert.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)shurbert.hp;
			
			if(shurbert.attackIndex == 2)
				shurbert.aiMode = 3;
		}
		else if (shurbert.aiMode == 3)
		{
			shurbert.isActive = false;
			background.nextBGID = 0;
			shurbert.aiMode = 4;
			timer++;
			timerPaused = false;
					
		}
		
		
		
		quadTree.insert(shurbert);
	}
	
	private void runBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "Swirly & Kyu";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		if(!swirlyKyu.portrait.emote.equals("attack"))
		{
			swirlyKyu.portrait.vars[0] = CITRUS_OFF_X;
			swirlyKyu.portrait.vars[1] = CITRUS_OFF_Y;
		}
		
		if (swirlyKyu.aiMode == 1)
		{
			
			swirlyKyu.isActive = false;
			
			swirlyKyu.vars[0] = mainLevel.SCREENW/2;
			swirlyKyu.vars[1] = 100;
			swirlyKyu.x += (swirlyKyu.vars[0] - swirlyKyu.x)/15.0;
			swirlyKyu.y += (swirlyKyu.vars[1] - swirlyKyu.y)/15.0;
			
			runDialogScript();
			
			//System.out.println(swirlyKyu.portrait.x + " " + swirlyKyu.portrait.y);
		}
		else if(swirlyKyu.aiMode == 2) // begin boss attack patterns
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			swirlyKyu.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)swirlyKyu.hp;
			
			if(swirlyKyu.attackIndex == swirlyKyu.lastAttackIndex + 1)
			{
				swirlyKyu.aiMode = 3;
				swirlyKyu.isActive = false;
				mainLevel.dBox.paragraph++;
				background.nextBGID = 0;
			}
		}
		else if(swirlyKyu.aiMode == 3)
		{
			runDialogScript();
		}
		else if(swirlyKyu.aiMode == 4)
		{
			this.endStage = true;
		}
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.x += (mainLevel.reyPortrait.vars[0] - mainLevel.reyPortrait.x) * 0.05;
			mainLevel.reyPortrait.y += (mainLevel.reyPortrait.vars[1] - mainLevel.reyPortrait.y) * 0.05;
		}
		if(!swirlyKyu.portrait.emote.equals("attack"))
		{
			swirlyKyu.portrait.x += (swirlyKyu.portrait.vars[0] - swirlyKyu.portrait.x) * 0.05;
			swirlyKyu.portrait.y += (swirlyKyu.portrait.vars[1] - swirlyKyu.portrait.y) * 0.05;
		}
		
		quadTree.insert(swirlyKyu);
		quadTree.insert(swirlyKyu.leftClaw);
		quadTree.insert(swirlyKyu.rightClaw);
		
		swirlyKyu.leftClaw.move();
		swirlyKyu.rightClaw.move();
		
		for(KyuArmSprite arm : swirlyKyu.leftArm)
		{
			arm.move();
			quadTree.insert(arm);
			
		}
		for(KyuArmSprite arm : swirlyKyu.rightArm)
		{
			arm.move();
			quadTree.insert(arm);
		}
	}
	
	
	private void runDialogScript()
	{
		shurbert.isActive = false;
		shurbert.setDirection('S');
		swirlyKyu.isActive = false;
		swirlyKyu.setDirection('S');
		
		// Dialogue happens
		
		DialogBoxSprite dBox = mainLevel.dBox;
		dBox.isVisible = true;
		
		if(keyboard.isPressed(config.VK_SKIP) || keyboard.isTyped(config.VK_SHOT))
			dBox.paragraph++;
		
		int para = 0;

		if(dBox.paragraph == para)
		{
			//setPlayerPortrait(":)","speak");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Zat iz enough! Vat is\n" +
							"ze meaning of zis tomfoolery!?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(">:(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Uhh... whoops.. sorry bro!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(">:(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Didn't mean to trash your lab.\n" +
							"Just chasing some jerks...";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("kyu","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "ERROR! Th3 0nly jerk eye 5ee \n" +
							"is the 0ne 1n from of me!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			
			int attackMode = mainLevel.parentYoBro.attackMode;
			
			if(attackMode == 1)
			{
				dBox.text = "You even yoyoed ze\n" + 
								"intern in ze proboscis!";
			}
			if(attackMode == 2)
			{
				dBox.text = "You even danced-stomped ze\n" + 
								"intern in ze gluteals!";
			}
			if(attackMode == 3)
			{
				dBox.text = "You even fro-picked ze \n" + 
								"intern in ze patellar";
			}
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Zis iz going to cost me a fortune!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Oh... that bro all right?\n" +
							"I can explain. I was in...";
		}
		para++;
		
		if(dBox.paragraph == para)
		{	
			mainLevel.loopMusic("sound/music/boss2temp.mid");
			dBox.paragraph++;
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("kyu","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Boss! How ab0ut w3 use h1m\n" + 
							"as a te5t subject?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("kyu","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Eye calculate that'll c0ver the \n" + 
							"dama9es appr0priately!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "woah! Just chill out guys!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Vonderful idea Kyubot! \n" + 
							"Let us settle zis vith SCIENCE!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("kyu","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "HAHAHA! Th1ngs are about to\n" + 
							"g3t 20% cooler in h3re!";
		}
		para++;
		
		// After dialogue, initiate first attack
		if(dBox.paragraph == para)
		{
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			swirlyKyu.portrait.setSemiTransparency( 0.5);
			swirlyKyu.aiMode = 2;
			mainLevel.barBossHealth.lagValue = 0;
			swirlyKyu.timer = 0;
			dBox.isVisible = false;
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Looks like they're out cold!";
			swirlyKyu.fogOn = false;
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Sounds of laughter from the east...\n" + 
							"The culprit's that way!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			swirlyKyu.aiMode = 4;
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
		
		swirlyKyu.portrait.emote = emote;

		if(mode.equals("speak"))
		{
			swirlyKyu.portrait.vars[0] = CITRUS_SPEAK_X;
			swirlyKyu.portrait.vars[1] = CITRUS_SPEAK_Y;
			swirlyKyu.portrait.setSemiTransparency(0.0);
		}
		if(mode.equals("listen"))
		{
			swirlyKyu.portrait.vars[0] = CITRUS_SPEAK_X;
			swirlyKyu.portrait.vars[1] = CITRUS_SPEAK_Y;
			swirlyKyu.portrait.setSemiTransparency(0.5);
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
		ArrayList<Sprite> cList = quadTree.query(shurbert);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(shurbert.collision(pBullet) && shurbert.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(shurbert.damage(pBullet.damage))
					{
						mainLevel.spawnItems(shurbert.itemDrops, shurbert.x, shurbert.y);
						shurbert.isActive = false;
						shurbert.timer = 0;
						shurbert.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						shurbert.attackIndex++;
						break;
					}
					
				}
				
				
			}
		}
		
		cList = quadTree.query(swirlyKyu);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(swirlyKyu.collision(pBullet) && swirlyKyu.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(swirlyKyu.damage(pBullet.damage))
					{
						mainLevel.spawnItems(swirlyKyu.itemDrops, swirlyKyu.x, swirlyKyu.y);
						swirlyKyu.isActive = false;
						swirlyKyu.timer = 0;
						swirlyKyu.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						swirlyKyu.attackIndex++;
						break;
					}
					
				}
			}
		}
		
		cList = quadTree.query(swirlyKyu.leftClaw);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(swirlyKyu.leftClaw.collision(pBullet) && swirlyKyu.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(swirlyKyu.leftClaw.damage(pBullet.damage))
					{
						mainLevel.spawnItems(swirlyKyu.itemDrops, swirlyKyu.x, swirlyKyu.y);
						swirlyKyu.isActive = false;
						swirlyKyu.timer = 0;
						swirlyKyu.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						swirlyKyu.attackIndex++;
						break;
					}
					
				}
			}
		}
		
		
		cList = quadTree.query(swirlyKyu.rightClaw);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(swirlyKyu.rightClaw.collision(pBullet) && swirlyKyu.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(swirlyKyu.rightClaw.damage(pBullet.damage))
					{
						mainLevel.spawnItems(swirlyKyu.itemDrops, swirlyKyu.x, swirlyKyu.y);
						swirlyKyu.isActive = false;
						swirlyKyu.timer = 0;
						swirlyKyu.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						swirlyKyu.attackIndex++;
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
		for(int i = 0; i < 10; i++)
		{
		//	splosion = FXSprite.makeSimpleOrb2(smiley.x, smiley.y);
			splosion = FXSprite.makeBurst1(smiley.x, smiley.y);
			mainLevel.sfx.add(splosion);
		}
		
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
		if(enemy.aiType == 1 && difficulty != "Easy")
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
		}
		if(enemy.aiType == 2)
		{	
			int spacing = 60;
			
			if(difficulty == "Hard")
				spacing = 45;
			if(difficulty == "Lunatic")
				spacing = 30;
			
			double initAngle = mainLevel.rand.nextInt(360);
			for(double i = initAngle; i < initAngle + 360; i+= spacing)
			{
				BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0xFFFF00));
				bullet.setVelocity(1, i);
				bullets.add(bullet);
			}
		}
		if(enemy.aiType == 4)
		{	
			mainLevel.soundPlayer.play("sound/EXPLOD05.WAV");
			for(double i = 0; i < 6; i++)
			{
				BulletSprite bullet;
				
				if(difficulty != "Easy")
				{
					bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0x555500));
					bullet.setProjectileVelocity(mainLevel.rand.nextInt(6)/2.0+2, mainLevel.rand.nextInt(90)+45, 0.05);
					bullet.aiMode = 3;
					bullets.add(bullet);
				}
				
				bullet = new BulletSprite(enemy.x, enemy.y, 'a', new Color(0xAA5500));
				bullet.setProjectileVelocity(mainLevel.rand.nextInt(6)/2.0+2, mainLevel.rand.nextInt(90)+45, 0.05);
				bullet.aiMode = 3;
				bullets.add(bullet);
				
				bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0x990000));
				bullet.setProjectileVelocity(mainLevel.rand.nextInt(6)/2.0+1, mainLevel.rand.nextInt(90)+45, 0.02);
				bullet.aiMode = 3;
				bullets.add(bullet);
				
				if(difficulty == "Lunatic")
				{
					bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xCC0000));
					bullet.scale(1.5,1.5);
					bullet.setProjectileVelocity(mainLevel.rand.nextInt(6)/2.0+1, mainLevel.rand.nextInt(90)+45, 0.02);
					bullet.aiMode = 3;
					bullets.add(bullet);
				}
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
		
		if(shurbert.isActive)
		{
			double dist = getSqrDist(x,y, shurbert.x, shurbert.y);
			if(nearestEnemy == null || dist < smallestDist)
			{
				nearestEnemy = shurbert;
				smallestDist = dist;
			}
		}
		
		if(swirlyKyu.isActive)
		{
			double dist = getSqrDist(x,y, swirlyKyu.x, swirlyKyu.y);
			if(nearestEnemy == null || dist < smallestDist)
			{
				nearestEnemy = swirlyKyu;
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
		shurbert.animate(il);
		swirlyKyu.animate(il);
		swirlyKyu.portrait.animate(il);
		
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
		shurbert.render(g2D);
		swirlyKyu.render(g2D);
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
		g2D.setColor(new Color(0xFF44BB11, true));
		g2D.fillRect(0, 0, mainLevel.SCREENW, mainLevel.SCREENH);
		background.render(g2D);
	}
	
	/**
	*	void renderForeground(Graphics g2D)
	*	This method renders all the stage's foreground graphics. It should be called in mainLevel's render method.
	**/
	
	public void renderForeground(Graphics2D g2D)
	{
		swirlyKyu.renderFog(g2D,mainLevel.player.x, mainLevel.player.y);
		swirlyKyu.portrait.render(g2D);
		textStageNum.render(g2D);
		textStageTitle.render(g2D);
	}

}


