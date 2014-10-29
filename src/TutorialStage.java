import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.ArrayList;
import gameEngine.*;

public class TutorialStage extends Stage
{
	// essential members
	
	// Sprites
	
	ABGTutorialSprite background;
	
	BossCitrus citrus;
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
		
	boolean movedLeft,movedRight,movedUp,movedDown,movedUpLeft,movedUpRight,movedDownLeft,movedDownRight;
	
	boolean dodged1;
	
	int killCount;
	
	
	/*
	*	Constructor
	*	Preconditions: ml is a reference to the game's main level interface which contains this stage.
	*	Postconditions: This stage is initialized and loads its data.
	*/
	
	public TutorialStage(MainLevel ml, YoBroMainPanel mp)
	{
		super(ml, mp);
		spawnerX = 0;
		spawnerY = 0;
		
		movedLeft = false;
		movedRight = false;
		movedUp = false;
		movedDown = false;
		movedUpLeft = false;
		movedUpRight = false;
		movedDownLeft = false;
		movedDownRight = false;
		
		dodged1 = false;
		killCount = 0;
	}
	
	public TutorialStage(MainLevel ml, YoBroMainPanel mp, long initTimer)
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
		
		BossCitrus.loadImages(mainLevel.imgLoader);
		ABGTutorialSprite.loadImages(mainLevel.imgLoader);
		
		// load music
		mainLevel.loopMusic("sound/BombermanHero_-_Monogenic.mid");
		
		// initialize Sprites and SpriteLists.

		background = new ABGTutorialSprite(mainLevel);
		
	//	bossBullets = new SpriteList();
		bossSlaves = new SpriteList();
		
		citrus = new BossCitrus(-50,0, mainLevel);
		textStageNum = new StageTextSprite(150, 140, '1',"");
		textStageNum.setSemiTransparency(1.0);
		textStageNum.isVisible = false;
		textStageTitle = new StageTextSprite(200,200,'t',"Tutorial Stage");
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
		citrus.destroy();
		bossSlaves.destroyAll();
		BossCitrus.clean();
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
		DialogBoxSprite dBox = mainLevel.dBox;
		
		checkPt = 0;
		if(timer >= checkPt && timer < checkPt +60)
		{ // stage title appears
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
			textStageTitle.setSemiTransparency(textStageTitle.getSemiTransparency()*1.1);
			
			if(textStageTitle.getSemiTransparency() >0.9)
			{
				textStageTitle.isVisible = false;
			}
		}
		
		checkPt += 150;
		if(timer == checkPt)
		{ // start text
			runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{
			dBox.paragraph++;
			runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{
			dBox.paragraph++;
			runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{
			dBox.paragraph++;
			runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt) // try moving around!!
		{ // start text
			dBox.paragraph++;
			runDialogScript();
		}
		if(timer == checkPt+2) // prevent stage from progressing until the player moves in all eight directions.
		{
			timer--;
			
			if(keyboard.isPressed(config.VK_LEFT))
			{
				movedLeft = true;
				if(keyboard.isPressed(config.VK_UP))
				{
					movedUpLeft = true;
				}
				if(keyboard.isPressed(config.VK_DOWN))
				{
					movedDownLeft = true;
				}
			}
			if(keyboard.isPressed(config.VK_RIGHT))
			{
				movedRight = true;
				if(keyboard.isPressed(config.VK_UP))
				{
					movedUpRight = true;
				}
				if(keyboard.isPressed(config.VK_DOWN))
				{
					movedDownRight = true;
				}
			}
			if(keyboard.isPressed(config.VK_UP))
			{
				movedUp = true;
			}
			if(keyboard.isPressed(config.VK_DOWN))
			{
				movedDown = true;
			}
			
			if(movedUp && movedUpRight && movedRight && movedDownRight && movedDown && movedDownLeft && movedLeft && movedUpLeft)
				timer++;
			
			
		}
		
		
		checkPt += 50;
		if(timer == checkPt) 
		{ // Good!
			dBox.paragraph++;
			runDialogScript();
		}
		
		
		
		checkPt += 100;
		int checkpoint1 = checkPt-1;
		if(timer == checkPt)
		{ 
			dBox.paragraph = 6;
			runDialogScript();
		}
		
		checkPt += 100;
		if(timer == checkPt)
		{  // spawn in enemy
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2,-10, 'a', 9999);
			enemy.aiType = 1;
			enemy.vars[1] = 150;
			enemies.add(enemy);
			dodged1 = true;
		}
		
		if(mainLevel.player.isDead)
		{
			dodged1 = false;
		}
		
		checkPt += 500;
		if(timer == checkPt)
		{  
			mainLevel.dBox.isVisible = true;
			if(!dodged1)
			{
				int failMessage = mainLevel.rand.nextInt(3);
				dBox.paragraph = 7 + failMessage;
				runDialogScript();
			}
			else
			{
				dBox.paragraph = 10;
				runDialogScript();
			}
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
			if(!dodged1)
			{
				timer = checkpoint1;
			}
			else
			{
				dBox.paragraph++;
				runDialogScript();
			}
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 200;
		int checkpoint2 = checkPt - 1;
		if(timer == checkPt)
		{  
				dBox.paragraph = 13;
				runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt) // slow dodging
		{  
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2,0, 'a', 9999);
			enemy.aiType = 3;
			enemy.isVisible = false;
			enemies.add(enemy);
			dodged1 = true;
		}
		
		checkPt += 600;
		if(timer == checkPt)
		{  
			mainLevel.dBox.isVisible = true;
			if(!dodged1)
			{
				int failMessage = mainLevel.rand.nextInt(3);
				dBox.paragraph = 14 + failMessage;
				runDialogScript();
			}
			else
			{
				dBox.paragraph = 17;
				runDialogScript();
			}
		}
		
		
		checkPt += 200;
		if(timer == checkPt)
		{  
			if(!dodged1)
			{
				timer = checkpoint2;
			}
			else
			{
				dBox.paragraph++;
				runDialogScript();
			}
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		
		checkPt += 200;
		int checkpoint3 = checkPt+1;
		if(timer == checkPt) // shooting tutorial
		{  
			dBox.isVisible = false;
			killCount = 0;
		}
		if(timer >= checkPt && timer <= checkPt + 100 && timer % 30 == 0)
		{
			EnemySprite enemy = new EnemySprite(-10,200, 'd', 1);
			enemy.aiType = 4;
			enemy.vars[0] = 3;
			enemy.itemDrops.add(makeItem("ppP"));
			enemies.add(enemy);
		}
		if(timer >= checkPt + 100 && timer <= checkPt + 200 && timer % 30 == 0)
		{
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+10,150, 'd', 1);
			enemy.aiType = 4;
			enemy.vars[0] = -3;
			enemy.itemDrops.add(makeItem("ppP"));
			enemies.add(enemy);
		}
		if(timer >= checkPt && timer <= checkPt + 200 && killCount >= 20) // success: destroy remaining enemies, magnet all items.
		{
			for(Object o : enemies)
			{
				EnemySprite enemy = (EnemySprite) o;
				mainLevel.spawnItems(enemy.itemDrops, enemy.x, enemy.y);
				
				FXSprite splosion;
				splosion = FXSprite.makeBurst1(enemy.x, enemy.y);
				mainLevel.sfx.add(splosion);
				for(int i = 0; i < 10; i++)
				{
					splosion = FXSprite.makeSimpleOrb2(enemy.x, enemy.y);
					mainLevel.sfx.add(splosion);
				}
				
				enemy.destroy();
				timer = checkPt + 202;
			}
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
		if(timer == checkPt + 201)
		{
			timer = checkpoint3;
		}
		
		checkPt += 300;
		int checkpoint4 = checkPt -1;
		if(timer == checkPt)
		{  
			dBox.isVisible = true;
			dBox.paragraph=22;
			runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt) // bomb tutorial (this section is designed to force the player to use a bomb)
		{  
			dBox.isVisible = false;
			killCount = 0;
			dodged1 = true;
		}
		if(timer >= checkPt && timer <= checkPt + 100 && timer % 5 == 0)
		{
			EnemySprite enemy = new EnemySprite(mainLevel.rand.nextInt(mainLevel.SCREENW),-10, 'a', 1);
			enemy.aiType = 5;
			enemy.vars[1] = 2 + mainLevel.rand.nextInt(3)*.75;
			enemies.add(enemy);
		}
		
		
		checkPt += 400;
		if(timer == checkPt)
		{  
			mainLevel.dBox.isVisible = true;
			if(!dodged1)
			{
				int failMessage = mainLevel.rand.nextInt(3);
				dBox.paragraph = 23 + failMessage;
				runDialogScript();
			}
			else
			{
				dBox.paragraph = 26;
				runDialogScript();
			}
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
			if(!dodged1)
			{
				timer = checkpoint4;
			}
			else
			{
				dBox.paragraph++;
				runDialogScript();
			}
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
				ArrayList<ItemSprite> items = new ArrayList<ItemSprite>();
				items.add(new ItemSprite(0,0,'B'));
				mainLevel.spawnItems(items,mainLevel.SCREENW/2,300);
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
				ArrayList<ItemSprite> items = new ArrayList<ItemSprite>();
				items.add(new ItemSprite(0,0,'1'));
				mainLevel.spawnItems(items,mainLevel.SCREENW/2,300);
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
				ArrayList<ItemSprite> items = new ArrayList<ItemSprite>();
				items.add(new ItemSprite(0,0,'F'));
				mainLevel.spawnItems(items,mainLevel.SCREENW/2,300);
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 200;
		if(timer == checkPt)
		{  
				dBox.paragraph++;
				runDialogScript();
		}
		
		checkPt += 300;
		if(timer == checkPt)
		{  
			endStage = true;
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
			if(enemy.aiType == 1)
			{
				enemy.y += (enemy.vars[1] - enemy.y)/16.0;
				if(Math.abs(enemy.y - enemy.vars[1]) < 2)
				{
					enemy.aiType = 2;
					enemy.y = enemy.vars[1];
				}
			}
			if(enemy.aiType == 2)
			{
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				mainLevel.dBox.isVisible = false;
				
				enemy.lookingAngle = angleToPlayer;
				
				if(enemy.vars[0] % 10 == 0)
				{
					BulletSprite bullet;
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0x5500FF));	
					bullet.setVelocity(3.0,angleToPlayer);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0x5500FF));	
					bullet.setVelocity(3.0,angleToPlayer+10);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0x5500FF));	
					bullet.setVelocity(3.0,angleToPlayer-10);
					bullets.add(bullet);
				}
				
				if(enemy.vars[0] > 300)
				{
					FXSprite splosion;
					splosion = FXSprite.makeBurst1(enemy.x, enemy.y);
					mainLevel.sfx.add(splosion);
					enemy.destroy();
				}
				
				enemy.vars[0]++;
			}
			if(enemy.aiType == 3)
			{
				mainLevel.dBox.isVisible = false;
				BulletSprite bullet;
				FXSprite splosion;
				if(enemy.vars[0] % 10 == 0)
				{
					enemy.x = mainLevel.rand.nextInt(mainLevel.SCREENW);
					splosion = FXSprite.makeBurst1(enemy.x, enemy.y);
					mainLevel.sfx.add(splosion);
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0x3300CC));	
					bullet.scale(1.1,1.0);
					bullet.setVelocity(2.7,-90);
					bullets.add(bullet);
					
					enemy.x = mainLevel.rand.nextInt(mainLevel.SCREENW);
					splosion = FXSprite.makeBurst1(enemy.x, enemy.y);
					mainLevel.sfx.add(splosion);
					bullet = new BulletSprite(enemy.x, enemy.y,'d',new Color(0x5500FF));	
					bullet.setVelocity(3.0,-90);
					bullets.add(bullet);
					
					enemy.x = mainLevel.rand.nextInt(mainLevel.SCREENW);
					splosion = FXSprite.makeBurst1(enemy.x, enemy.y);
					mainLevel.sfx.add(splosion);
					bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0x330088));	
					bullet.scale(1.2,1.0);
					bullet.setVelocity(2.3,-90);
					bullets.add(bullet);
				}
				if(enemy.vars[0] > 400)
				{
					enemy.destroy();
				}
				
				enemy.vars[0]++;
			}
			if(enemy.aiType == 4)
			{
				enemy.x += enemy.vars[0];
				if(enemy.vars[0] < 0)
					enemy.setDirection('W');
				else
					enemy.setDirection('E');
				
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				enemy.lookingAngle = angleToPlayer;
				
				
				if(enemy.vars[1] % 60 == 30)
				{
					BulletSprite bullet;
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0xFF0055));	
					bullet.setVelocity(3.0,angleToPlayer);
					bullets.add(bullet);
				}
				
				if(enemy.x < -10 || enemy.x > mainLevel.SCREENW+10)
					enemy.destroy();
				
				enemy.vars[1]++;
			}
			if(enemy.aiType == 5)
			{
				enemy.y += enemy.vars[1];
				
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				
				if(enemy.vars[0] % 20 == 0)
				{
					BulletSprite bullet;
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0xFF0055));	
					bullet.setVelocity(3.0,angleToPlayer);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0xFF0055));	
					bullet.setVelocity(3.0,angleToPlayer + 10);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0xFF0055));	
					bullet.setVelocity(3.0,angleToPlayer - 10);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0xFF0055));	
					bullet.setVelocity(3.0,angleToPlayer + 30);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x, enemy.y,'c',new Color(0xFF0055));	
					bullet.setVelocity(3.0,angleToPlayer - 30);
					bullets.add(bullet);
				}
				
				if(enemy.y > mainLevel.SCREENH+10)
					enemy.destroy();
				
				enemy.vars[0]++;
			}
			
			
			
			quadTree.insert(enemy);
			
		}
	}
	
	private void runMidBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "Citrus";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		if(!citrus.portrait.emote.equals("attack"))
		{
			citrus.portrait.vars[0] = CITRUS_OFF_X;
			citrus.portrait.vars[1] = CITRUS_OFF_Y;
		}

		
		// AI modes
		
		if(citrus.aiMode == 0) // stop for boss dialogue (if any)
		{	
			runDialogScript();
		}
		
		quadTree.insert(citrus);
	}
	
	
	
	
	private void runDialogScript()
	{
		citrus.isActive = false;
		
		// Dialogue happens
		
		DialogBoxSprite dBox = mainLevel.dBox;
		dBox.isVisible = true;
		
		int para = 0;

		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Welcome to the tutorial stage!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Here, you'll learn the basics of \n" +
							"how to play Yo Bro.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "This awesome bro riding the flying \n" + 
							"skateboard is Rey. Rey Haiku.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Use the arrow keys to move \n" +
							"around the screen.";
		}
		para++;
		
		if(dBox.paragraph == para) // 4
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Try moving up, down, left,right, and \n" +
							"in each diagonal direction!";
		}
		para++;

		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Good!";
		}
		para++;
		
		if(dBox.paragraph == para) // 6
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Now try moving around to \n" +
							"dodge this enemy's bullets!";
		}
		para++;
		
		if(dBox.paragraph == para) // 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Yo, Man, that was terrible! Try again!";
		}
		para++;
		
		if(dBox.paragraph == para) // 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "That was lame, Bro! Remember to \n" +
							"dodge this time!";
		}
		para++;
		
		if(dBox.paragraph == para) // 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "I've seen shrine maidens\n" +
							"dodge better than that! AGAIN!!";
		}
		para++;
		
		if(dBox.paragraph == para) // 10
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Cool!";
		}
		para++;
		
		if(dBox.paragraph == para) // 11
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Usually, enemy bullets will be\n" +
							"closer together than that. ";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Hold the " + KeyEvent.getKeyText(config.VK_SLOW) + " key to  \n" +
							"move in slow mode.";
		}
		para++;
		
		if(dBox.paragraph == para) // 13
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Now dodge these bullets \n" + 
							"using slow mode!!!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Try using the keyboard with \n" + 
							"your fingers, not your face.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Bro-tip: Dodge the bullets. \n" +
							"don't give them hugs!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "That was lame! This time, \n" +
							"try taking it slow, bro.";
		}
		para++;
		
		if(dBox.paragraph == para) // 17
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Great!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Rey can do more than just dodge. He\n" +
							"can also return fire with his own attacks!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Hold the " + KeyEvent.getKeyText(config.VK_SHOT) + " key to shoot\n" +
							"using the attack mode you selected.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Now, go dull out the harshness \n" + 
							"on 20 enemies.";
		}
		para++;
		
		if(dBox.paragraph == para) // 21
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Also, collect the red P blocks they drop. \n" + 
							"They will increase your power!";
		}
		para++;
		
		if(dBox.paragraph == para) // 22
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Nice work so far! \n" +
							"But can you handle THIS?";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "When things get tough, press " + KeyEvent.getKeyText(config.VK_BOMB) + "  \n" +
							"to use bombs wisely!";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Too tough? Try pressing "+ KeyEvent.getKeyText(config.VK_BOMB) + "  \n" +
							"to use a powerful bomb attack.";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "That looks like it hurt! \n" +
							"Bro tip: Press " + KeyEvent.getKeyText(config.VK_BOMB) + " to nuke everything!";
		}
		para++;
		
		if(dBox.paragraph == para) // 26
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Awesome, bro! Remember though, you\n" +
							"have only a limited number of bombs.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "You can get more bombs by collecting \n" + 
							"green B-looking items...";
		}
		para++;
		
		if(dBox.paragraph == para) // 28
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "like this!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "You can also get more lives by \n" + 
							"scoring enough points, or...";
		}
		para++;
		
		if(dBox.paragraph == para) // 30
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "grabbing purple, starry, 1up-y  \n" + 
							"items like this!";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Also, if you lose all your lives,  \n" + 
							"and you choose to continue...";
		}
		para++;
		
		if(dBox.paragraph == para) // 32
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "one of these will spawn from   \n" + 
							"where you game-overed.";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "These golden F blocks will \n" + 
							"fill your power up to MAX!";
		}
		para++;
		
		if(dBox.paragraph == para) 
		{
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "This concludes the tutorial stage.";
		}
		para++;
		
		
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
		if(s.x < 0-s.width)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
		if(s.x > mainLevel.SCREENW + s.width)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
		if(s.y < 0-s.height)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
		if(s.y > mainLevel.SCREENH + s.height)
		{
		//	System.out.println(s + " went offscreen");
			return true;
		}
			
		return false;
	}
	
	
	private double getAngleTo(double origX, double origY, double destX, double destY)
	{
		double dx = destX - origX;
		if(dx == 0)
			dx = 0.0001;
		double dy = origY - destY;
		
		if(dx > 0)
		{
			return Math.atan(dy/dx)/(2.0*Math.PI)*360.0;
		}
		else
		{
			return 180 + Math.atan(dy/dx)/(2.0*Math.PI)*360.0;
		}
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
							mainLevel.spawnItems(smiley.itemDrops, smiley.x, smiley.y);
							
							FXSprite splosion;
							splosion = FXSprite.makeBurst1(smiley.x, smiley.y);
							mainLevel.sfx.add(splosion);
							for(int i = 0; i < 10; i++)
							{
							//	splosion = FXSprite.makeSimpleOrb2(smiley.x, smiley.y);
								splosion = FXSprite.makeSimpleOrb2(smiley.x, smiley.y);
								mainLevel.sfx.add(splosion);
							}
							mainLevel.soundPlayer.play("sound/zap01.wav");
							killCount++;
							
							smiley.destroy();
						}
						
						if(!pBullet.permanent)
							pBullet.destroy();
						
						
					}
				}
			}
			
		}
		
		
		// collisions for midboss/boss
		ArrayList<Sprite> cList = quadTree.query(citrus);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(citrus.collision(pBullet) && citrus.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(citrus.damage(pBullet.damage))
					{
						mainLevel.spawnItems(citrus.itemDrops, citrus.x, citrus.y);
						citrus.isActive = false;
						citrus.timer = 0;
						citrus.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						citrus.attackIndex++;
						break;
					}
					
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
		
		if(citrus.isActive)
		{
			double dist = getSqrDist(x,y, citrus.x, citrus.y);
			if(nearestEnemy == null || dist < smallestDist)
			{
				nearestEnemy = citrus;
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
		citrus.animate(il);
		citrus.portrait.animate(il);
		
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
		citrus.render(g2D);
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
		g2D.setColor(new Color(0xFFAAAAFF, true));
		g2D.fillRect(0, 0, mainLevel.SCREENW, mainLevel.SCREENH);
		background.render(g2D);
		
	}
	
	/**
	*	void renderForeground(Graphics g2D)
	*	This method renders all the stage's foreground graphics. It should be called in mainLevel's render method.
	**/
	
	public void renderForeground(Graphics2D g2D)
	{
		citrus.portrait.render(g2D);
		textStageNum.render(g2D);
		textStageTitle.render(g2D);
	}

}


