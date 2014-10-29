import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.ArrayList;

import gameEngine.*;


public class MainLevel extends Level
{	
	public YoBroMainPanel parentYoBro;
	public final int ORIGINX = 32;
	public final int ORIGINY = 16;
	public final int SCREENW = 400;
	public final int SCREENH = 448;
	
	private final int INITBOMBS = 3;
	private final int MAGNETLINE = 100;
	
	boolean typedUP;
	boolean typedDOWN;
	boolean typedENTER;
	boolean typedESC;
	boolean typedX;
	boolean typedZ;
	
	////////////////// sprites
	
	QuadTreeNode quadTree;
	
	// HUD sprites
	
	HUDSprite panelMask;
	HUDSprite textLives;
	HUDSprite textBombs;
	HUDSprite textPower;
	HUDSprite textGraze;
	HUDSprite textScore;
	HUDSprite iconBombs;
	HUDSprite iconLives;
	HUDSprite iconEnemyHere;
	NumberSprite numberPower;
	NumberSprite numberGraze;
	NumberSprite numberScore;
	NumberSprite numberSCTimer;
	NumberSprite barBossHealth;
	
	DialogBoxSprite dBox;
	PortraitReySprite reyPortrait;
	
	StageTextSprite textPaused;
	StageTextSprite textAreYouSure;
	StageTextSprite textContinue;
	StageTextSprite textContRem;
	StageTextSprite textResume;
	StageTextSprite textRetry;
	StageTextSprite textReturnToTitle;
	StageTextSprite textYes;
	StageTextSprite textNo;
	StageTextSprite textStageClear;
	
	StageTextSprite textDemoClear;
	
	// Game sprites
	
	PlayerSprite player;
	PlayerSprite pHitBox;
	
	SpriteList yoyos1;
	SpriteList yoyos2;
	SpriteList yoyos3;
	SpriteList yoyoBombs1;
	SpriteList yoyoBombs2;
	
	SpriteList ibSideSpawners;
	PlayerBulletSprite ibMidSpawner;
	SpriteList ibArrows;
	SpriteList ibBombLaser;
	
	SpriteList froPicks;
	
	SpriteList powerUps;
	
	SpriteList sfx;
	
	
	////////////////// obtained from parent
	
	public Keyboard keyboard;
	public ConfigYoBro config;
	public MidiPlayer midi;
	public SoundPlayer soundPlayer;
	private double originalMidiVolume;
	
	
	// game vars
	
	public Random rand;
	public String difficulty;
	
	private int power;
	private long score;
	private int graze;
	private boolean isPaused;
	private int stageNum;
	
	private double unfocusedSpeed = 3.5;
	private double focusedSpeed = 1.3;
	
	public Stage curStage;
	private int curStageNum;
	private int numContinues;
	
	public int screenShake;

	
	//////////////// Constructor
	
	public MainLevel(Component parent)
	{
		super(parent);
		parentYoBro = (YoBroMainPanel) parent;
		keyboard = parentYoBro.getKeyboard();
		config = parentYoBro.getConfig();
		
		midi = parentYoBro.getMidiPlayer();
		soundPlayer = parentYoBro.soundPlayer;
		difficulty = parentYoBro.difficulty;
		
		rand = new Random();
		
		power = 0;
		numContinues = 3;
		isPaused = false;
		screenShake = 0;
		
		originalMidiVolume = config.musicVol;
	}
	
	
	
	
	private void loadFirstStage()
	{
		if(parentYoBro.gameMode.equals("Normal"))
		{
			curStage = new Stage1(this, parentYoBro, -100);
			curStageNum = 1;
		}
		if(parentYoBro.gameMode.equals("Stage"))
		{
			parentYoBro.selectedStage = 4;
			if(parentYoBro.selectedStage == 1)
			{
				curStage = new Stage1(this, parentYoBro, -100); //2700);
				curStageNum = 0;
			}
			if(parentYoBro.selectedStage == 2)
			{
				curStage = new Stage2(this, parentYoBro, 2500);
				curStageNum = 0;
			}
			if(parentYoBro.selectedStage == 3)
			{
				curStage = new Stage3(this, parentYoBro, -100); // 1700); // 
				curStageNum = 0;
			}
			if(parentYoBro.selectedStage == 4)
			{
				curStage = new Stage4(this, parentYoBro, 6200); // 3500); //5200); //-100);
				curStageNum = 0;
			}
			
		}
		if(parentYoBro.gameMode.equals("Tutorial"))
		{
			curStage = new TutorialStage(this, parentYoBro, -100);
			curStageNum = 0;
		}
	}
	
	
	
	/**
	*	loadData()
	*	loads image and sound data for this level into memory. This method should be called before 
	*	running the running the level's timer loop and render methods for the first time. 
	**/
	
	public void loadData()
	{
		System.out.println("Loading MainLevel");
		// load image data
		
		HUDSprite.loadImages(imgLoader);
		BulletSprite.loadImages(imgLoader);
		PlayerBulletSprite.loadImages(imgLoader);
		PlayerSprite.loadImages(imgLoader);
		EnemySprite.loadImages(imgLoader);
        ObstacleSprite.loadImages(imgLoader);
		NumberSprite.loadImages(imgLoader);
		ItemSprite.loadImages(imgLoader);
		StageTextSprite.loadImages(imgLoader);
		PortraitReySprite.loadImages(imgLoader);
		FXSprite.loadImages(imgLoader);
		
		// make initial objects
		
		panelMask = new HUDSprite(0,0,'A');
		textLives = new HUDSprite(448,128,'a');
		textBombs = new HUDSprite(448,160,'b');
		textPower = new HUDSprite(448,192,'c');
		textGraze = new HUDSprite(448,80,'d');
		textScore = new HUDSprite(448,48,'e');
		numberPower = new NumberSprite(512,208-16, power, 127);
		numberGraze = new NumberSprite(512, 80, 0, -1);
		numberScore = new NumberSprite(512, 48, 0, -1);
		numberSCTimer = new NumberSprite(410,16, 0, -1);
		numberSCTimer.minDigits = 2;
		iconEnemyHere = new HUDSprite(-40,464,'E');
		
		textPaused = new StageTextSprite(SCREENW/2,140,'P');
		textAreYouSure = new StageTextSprite(SCREENW/2,140,'A');
		textContinue = new StageTextSprite(SCREENW/2,100,'C');
		textContRem = new StageTextSprite(SCREENW/2,140,'c');
		textResume = new StageTextSprite(SCREENW/2,200,'u');
		textRetry = new StageTextSprite(SCREENW/2,232,'r');
		textReturnToTitle = new StageTextSprite(SCREENW/2,264,'q');
		textYes = new StageTextSprite(SCREENW/2,200,'y');
		textNo = new StageTextSprite(SCREENW/2,232,'n');
		textStageClear = new StageTextSprite(SCREENW/2,140,'s');
		
		textDemoClear = new StageTextSprite(SCREENW/2,200,'t');
		textDemoClear.color1 = new Color(0xAAFF00);
		textDemoClear.color2 = new Color(0xFF0000);
		textDemoClear.text = "Thank you for playing Yo Bro! ~" + 
									"This is the end of the 1 stage demo version. ~" + 
									"Please watch for the completed game ~" + 
									"coming soon! ~ ~" + 
									"Now try to complete the demo on ~ " + 
									"a harder difficulty!";
									
		
		barBossHealth = new NumberSprite(48,20,0,0,'b');
		
		iconBombs = new HUDSprite(512, 160, 'C');
		iconLives = new HUDSprite(512, 128, 'B');
		
		iconBombs.value = INITBOMBS;
		iconLives.value = parentYoBro.config.startingLives;
		
		dBox = new DialogBoxSprite(50,SCREENH - 150);
		dBox.isVisible = false;
		reyPortrait = new PortraitReySprite(-200,300);
	//	reyPortrait.isVisible = false;
		
		player = new PlayerSprite((SCREENW)/2, SCREENH - 40 , 'r');
		pHitBox = new PlayerSprite(player.x, player.y, 'h');
		pHitBox.setSemiTransparency(1.0);
		
		powerUps = new SpriteList();
		
		sfx = new SpriteList();
		
		
		// attack mode specific sprites
		
		if(parentYoBro.attackMode == 1)
		{
			this.initHomingYoyo();
		}
		if(parentYoBro.attackMode == 2)
		{
			this.initIllusionBeatdown();
		}
		if(parentYoBro.attackMode == 3)
		{
			this.initPersuasionFroPick();
		}
		
		
		////////////////// wait for initial images to load
		
		imgLoader.waitForAll();
		System.out.println("finished loading MainLevel");
		
		loadFirstStage();
	}
	
	
	
	private void initHomingYoyo()
	{
		// forward yoyos
		
		yoyos1 = new SpriteList();
		
		// homing yoyos
		
		yoyos2 = new SpriteList();
		
		// shield yoyos
		
		yoyos3 = new SpriteList();
		
		// yoyo bombs
		
		yoyoBombs1 = new SpriteList();
		yoyoBombs2 = new SpriteList();
		
		//
		
		PlayerBulletSprite bullet;
		
		bullet = new PlayerBulletSprite(0,0,'a',2);
		bullet.vars[0] = 0;
		bullet.setSemiTransparency(0.0);
		bullet.id = 0;
		bullet.permanent = true;
		yoyos3.add(bullet);

		bullet = new PlayerBulletSprite(0,0,'a',2);
		bullet.vars[0] = 180;
		bullet.setSemiTransparency(0.0);
		bullet.id = 0;
		bullet.permanent = true;
		yoyos3.add(bullet);

		bullet = new PlayerBulletSprite(0,0,'a',2);
		bullet.vars[0] = 90;
		bullet.setSemiTransparency(0.0);
		bullet.id = 1;
		bullet.permanent = true;
		yoyos3.add(bullet);

		bullet = new PlayerBulletSprite(0,0,'a',2);
		bullet.vars[0] = 270;
		bullet.setSemiTransparency(0.0);
		bullet.id = 1;
		bullet.permanent = true;
		yoyos3.add(bullet);
	}
	
	
	private void initIllusionBeatdown()
	{
		ibMidSpawner = new PlayerBulletSprite(player.x,player.y,'g',0);
		
		ibSideSpawners = new SpriteList();
		
		ibBombLaser = new SpriteList();
		
		PlayerBulletSprite bullet;
		
		bullet = new PlayerBulletSprite(player.x, player.y, 'e',0); // Right
		bullet.rotate(0);
		bullet.vars[0] = 2;
		bullet.targetX = player.x;
		bullet.targetY = player.y;
		ibSideSpawners.add(bullet);
		
		bullet = new PlayerBulletSprite(player.x, player.y, 'e',0); // Up
		bullet.rotate(90);
		bullet.vars[0] = 1;
		bullet.targetX = player.x;
		bullet.targetY = player.y;
		ibSideSpawners.add(bullet);
		
		bullet = new PlayerBulletSprite(player.x, player.y, 'e',0); // Down
		bullet.rotate(270);
		bullet.vars[0] = -1;
		bullet.targetX = player.x;
		bullet.targetY = player.y;
		ibSideSpawners.add(bullet);
		
		bullet = new PlayerBulletSprite(player.x, player.y, 'e',0); // Left
		bullet.rotate(180);
		bullet.vars[0] = -2;
		bullet.targetX = player.x;
		bullet.targetY = player.y;
		ibSideSpawners.add(bullet);
		
		ibArrows = new SpriteList();
		
	}
	
	
	private void initPersuasionFroPick()
	{
		froPicks = new SpriteList();
	}
	
	
	/**
	*	clean()
	*	This method unloads the level's image and sound data from memory. Any other memory management clean-up for
	*	the level is also taken care of here.
	*
	**/
	
	public void clean()
	{
		super.clean();
		
		player.destroy();
		pHitBox.destroy();
		panelMask.destroy();
		textLives.destroy();
		textBombs.destroy();
		textPower.destroy();
		textGraze.destroy();
		textScore.destroy();
		iconBombs.destroy();
		iconLives.destroy();
		numberPower.destroy();
		numberGraze.destroy();
		numberScore.destroy();
		numberSCTimer.destroy();
		barBossHealth.destroy();
		dBox.destroy();
		
		powerUps.destroyAll();
		sfx.destroyAll();
		
		if(parentYoBro.attackMode == 1)
		{
			yoyos1.destroyAll();
			yoyos2.destroyAll();
			yoyos3.destroyAll();
			yoyoBombs1.destroyAll();
			yoyoBombs2.destroyAll();
		}
		if(parentYoBro.attackMode == 2)
		{
			ibMidSpawner.destroy();
			ibSideSpawners.destroyAll();
			ibArrows.destroyAll();
		}
		if(parentYoBro.attackMode == 3)
		{
			froPicks.destroyAll();
		}
		
		HUDSprite.clean();
		NumberSprite.clean();
		BulletSprite.clean();
		PlayerBulletSprite.clean();
		PlayerSprite.clean();
		EnemySprite.clean();
		ItemSprite.clean();
		
		curStage.clean();
		
		keyboard = null;
		config = null;
		parentYoBro = null;
		curStage = null;
		rand = null;
		
		quadTree = null;
		
	}
	
	
	/**
	*	
	*	
	**/
	
	public void loopMusic(String midiPath)
	{
		midi.load(midiPath);
		midi.play(true);
      midi.setVolume(config.musicVol);
		midi.loop(-1);
	}
	
	
	/**
	*	timerLoop()
	*	This method steps through one animation frame for the level. 
	*	This method should be called by the game's timer event handler before rendering the level and after loadData is used.
	*
	**/
	
	public void timerLoop()
	{
		// The quadtree must be reinstantiated each loop since its entire contents are going to change anyways.
		
		//quadTree = new QuadTreeNode(ORIGINX, ORIGINY, ORIGINX + SCREENW, ORIGINY + SCREENH,0);
		quadTree = new QuadTreeNode(0,0,SCREENW,SCREENH,0);
		
	
		// unpaused loop
		
		if(!this.isPaused)
		{	
			// reset some variables
		
			numberSCTimer.isVisible = false;
			barBossHealth.isVisible = false;
			iconEnemyHere.x = -40;
			iconEnemyHere.setSemiTransparency(0.3);
			
			if(screenShake > 0)
			screenShake--;
		
			// PAUSE button!
			
			if(keyboard.isTyped(config.VK_PAUSE))
			{
				textPaused.vars[0] = 0;
				textPaused.vars[1] = 0;
				this.isPaused = true;
				midi.stop();
			}
			
		
			if(!player.isDead)
			{
				// player movement
				
				double curSpeed;
				double hbCurTrans = pHitBox.getSemiTransparency();
				if(keyboard.isPressed(config.VK_SLOW))
				{
					curSpeed = focusedSpeed;
					if(hbCurTrans > 0.1)
					{
						pHitBox.setSemiTransparency(hbCurTrans/2.0);
					}
				}
				else
				{
					curSpeed = unfocusedSpeed;
					
					if(hbCurTrans < 1.0)
					{
						pHitBox.setSemiTransparency(hbCurTrans *2.0);
					}
				}
				if(parentYoBro.attackMode == 2 && player.isBombing)
				{
					curSpeed /= 4.0;
				}
				
				if(keyboard.isPressed(config.VK_UP))
				{
					player.setDirection('N');
					player.y -= curSpeed;
				}
				if(keyboard.isPressed(config.VK_DOWN))
				{
					player.setDirection('E');
					player.y += curSpeed;
				}
				if(keyboard.isPressed(config.VK_LEFT))
				{
					player.setDirection('W');
					player.x -= curSpeed;
				}
				if(keyboard.isPressed(config.VK_RIGHT))
				{
					player.setDirection('E');
					player.x += curSpeed;
				}
				
				// prevent player from moving outside playable area.
				
				if(player.x < 10)
					player.x = 10;
				if(player.x > SCREENW - 10)
					player.x = SCREENW - 10;
				if(player.y < 10)
					player.y = 10;
				if(player.y > SCREENH - 10)
					player.y = SCREENH - 10;
				
				
				// set hitbox position
				
				pHitBox.x = player.x;
				pHitBox.y = player.y;
				quadTree.insert(pHitBox);
				quadTree.insert(player); // used as a graze radius.
			}
			else if(player.respawnCounter == 19)
			{
				
				
				ArrayList<ItemSprite> items = new ArrayList<ItemSprite>();
				
				iconLives.value--;
				iconBombs.value = INITBOMBS;
				
				power -= 20;
				if(power < 0)
					power = 0;
				
				if(iconLives.value < 0)
				{
					items.add(new ItemSprite(player.x, player.y, 'F'));
					items.add(new ItemSprite(player.x, player.y, 'F'));
					items.add(new ItemSprite(player.x, player.y, 'F'));
					items.add(new ItemSprite(player.x, player.y, 'F'));
					spawnItems(items,player.x,player.y);
					if(this.promptContinue()) 
						return;
				}
				else
				{
					items.add(new ItemSprite(player.x, player.y, 'P'));
					items.add(new ItemSprite(player.x, player.y, 'p'));
					items.add(new ItemSprite(player.x, player.y, 'p'));
					items.add(new ItemSprite(player.x, player.y, 'p'));
					items.add(new ItemSprite(player.x, player.y, 'p'));
					items.add(new ItemSprite(player.x, player.y, 'p'));
					spawnItems(items,player.x,player.y);
				}
				
				player.x = SCREENW / 2; 
				player.y = SCREENH - 40;
			}
			else if(keyboard.isTyped(config.VK_BOMB) && player.respawnCounter < 6 && iconBombs.value > 0)
			{
				player.isDead = false;
				player.respawnCounter = -1;
				player.scale(1.0,1.0);
				player.setSemiTransparency(1.0);
			}
			
			// Handle player shots
				
			if(parentYoBro.attackMode == 1)
			{
				this.homingYoyo();
			}
			if(parentYoBro.attackMode == 2)
			{
				this.illusionBeatdown();
			}
			if(parentYoBro.attackMode == 3)
			{
				this.persuasionFropick();
			}
			
			
			cheats();
			curStage.stageTimerLoop();
			
			// check player collisions with enemy bullets and enemies
			
			this.bomb2BulletCollisions();
			this.player2EnemyCollisions();
			
			// run logic for powerups and their collisions with the player;
			
			this.powerUps();
			this.moveSFX();
			
			
			
			this.animateAllUnpaused();
		}
		else // paused loop
		{
			textPaused.isVisible = false;
			textAreYouSure.isVisible = false;
			textContinue.isVisible = false;
			textContRem.isVisible = false;
			textResume.isVisible = false;
			textRetry.isVisible = false;
			textReturnToTitle.isVisible = false;
			textYes.isVisible = false;
			textNo.isVisible = false;
			textStageClear.isVisible = false;
			
			textResume.isActive = false;
			textRetry.isActive = false;
			textReturnToTitle.isActive = false;
			
			textYes.isActive = false;
			textNo.isActive = false;
			
			if(textPaused.vars[0] == 0) // paused
			{
				if(this.pausedMenu())
					return;
			}
			else if(textPaused.vars[0] == 1) // continue?
			{
				if(this.continueMenu()) return;
			}
			else if(textPaused.vars[0] == 3) // stage clear
			{
				if(stageCleared())
					return;
			}
			
			this.animateAllPaused();
		}
		
		if(curStage.endStage)
		{
			this.isPaused = true;
			textPaused.vars[0] = 3;
		}
	}
	
	
	
	
	private void cheats()
	{
		if(keyboard.isPressed(config.VK_SKIP))
			power++;
			
		if(keyboard.isPressed(KeyEvent.VK_1))
		{
			curStage.endStage = true;
			curStageNum = -1;
		}
		if(keyboard.isPressed(KeyEvent.VK_2))
		{
			curStage.endStage = true;
			curStageNum = 1;
		}
		if(keyboard.isPressed(KeyEvent.VK_3))
		{
			curStage.endStage = true;
			curStageNum = 2;
		}
		if(keyboard.isPressed(KeyEvent.VK_4))
		{
			curStage.endStage = true;
			curStageNum = 3;
		}
	}
	
	//////////////////////// Helper methods
	
	
	/**
	*	void player2EnemyCollisions()
	*	Checks for player collisions with enemies and enemy bullets.
	*	Also manages temporary invulnerability.
	**/
	
	private void player2EnemyCollisions()
	{
		if(!player.isDead)
		{
			if(player.tempInvincibility > 0) // the player is temporarily invincible.
			{
				player.tempInvincibility -= 1;
				
				// blink to show player that they are temporarily invulnerable.
				if(player.tempInvincibility % 2 == 0)
					player.setSemiTransparency(0.5);
				else
					player.setSemiTransparency(0.0);
			}
			else
			{
				player.setSemiTransparency(0.0);
			}

			// check collisions with enemies and enemy bullets
			ArrayList<Sprite> cList = quadTree.query(pHitBox);
			
			for(Sprite s : cList)
			{
				if(s instanceof EnemySprite)
				{
					EnemySprite enemy = (EnemySprite) s;
					if(player.tempInvincibility == 0 && !enemy.isSpawner && pHitBox.collision(s))
					{
						killPlayer();
					}
				}
				if(s instanceof BulletSprite)
				{
					BulletSprite bullet = (BulletSprite) s;
					
					if(bullet.isActive && player.tempInvincibility == 0 && pHitBox.collision(bullet))
					{
						killPlayer();
					}
				}
				if(s instanceof BossSprite)
				{
					if(player.tempInvincibility == 0 && ((BossSprite) s).isActive && s.collision(pHitBox))
					{
						killPlayer();
					}
				}
                if(s instanceof ObstacleSprite) {
                    if(player.tempInvincibility == 0 && s.collision(pHitBox))
					{
						killPlayer();
					}
                }
			}
			
			// check grazings with enemies and enemy bullets
			cList = quadTree.query(player);
			
			for(Sprite s : cList)
			{
				if( s instanceof BulletSprite)
				{
					BulletSprite bullet = (BulletSprite) s;
					if(player.collision(s) && player.tempInvincibility == 0 && !bullet.isGrazed)
					{
						graze++;
						bullet.isGrazed = true;
						soundPlayer.play("sound/tok.wav");
					}
				}
			}
			
		}
	}
	
	
	/**
	*	void bomb2BulletCollisions()
	*	Checks for collisions between enemy bullets and bomb bullets.
	*	Bomb bullets destroy most enemy bullets.
	**/
	
	private void bomb2BulletCollisions()
	{
		if(parentYoBro.attackMode == 1)
		{
			for(Object o : yoyoBombs1)
			{
				PlayerBulletSprite pbs = (PlayerBulletSprite) o;
				// check collisions with enemies and enemy bullets
				if(pbs.isBomb)
				{
					ArrayList<Sprite> cList = quadTree.query(pbs);
					
					for(Sprite s : cList)
					{
						if(s instanceof BulletSprite)
						{
							BulletSprite bullet = (BulletSprite) s;
							
							if(bullet.killable && pbs.collision(bullet))
							{
								this.createBulletPoint(bullet);
								bullet.destroy();
							}
						}
					}
				}
			}
			for(Object o : yoyoBombs2)
			{
				PlayerBulletSprite pbs = (PlayerBulletSprite) o;
				// check collisions with enemies and enemy bullets
				if(pbs.isBomb)
				{
					ArrayList<Sprite> cList = quadTree.query(pbs);
					
					for(Sprite s : cList)
					{
						if(s instanceof BulletSprite)
						{
							BulletSprite bullet = (BulletSprite) s;
							
							if(bullet.killable && pbs.collision(bullet))
							{
								this.createBulletPoint(bullet);
								bullet.destroy();
							}
						}
					}
				}
			}
		}
		else if(parentYoBro.attackMode == 2)
		{
			for(Object o : ibArrows)
			{
				PlayerBulletSprite pbs = (PlayerBulletSprite) o;
				// check collisions with enemies and enemy bullets
				if(pbs.isBomb)
				{
					ArrayList<Sprite> cList = quadTree.query(pbs);
					
					for(Sprite s : cList)
					{
						if(s instanceof BulletSprite)
						{
							BulletSprite bullet = (BulletSprite) s;
							
							if(bullet.killable && pbs.collision(bullet))
							{
								this.createBulletPoint(bullet);
								bullet.destroy();
							}
						}
					}
				}
			}
			for(Object o : ibBombLaser)
			{
				PlayerBulletSprite pbs = (PlayerBulletSprite) o;
				// check collisions with enemies and enemy bullets
				if(pbs.isBomb)
				{
					ArrayList<Sprite> cList = quadTree.query(pbs);
					
					for(Sprite s : cList)
					{
						if(s instanceof BulletSprite)
						{
							BulletSprite bullet = (BulletSprite) s;
							
							if(bullet.killable && pbs.collision(bullet))
							{
								this.createBulletPoint(bullet);
								bullet.destroy();
							}
						}
					}
				}
			}
		}
		else if(parentYoBro.attackMode == 3)
		{
			for(Object o : froPicks)
			{
				PlayerBulletSprite pbs = (PlayerBulletSprite) o;
				// check collisions with enemies and enemy bullets
				if(pbs.isBomb)
				{
					ArrayList<Sprite> cList = quadTree.query(pbs);
					
					for(Sprite s : cList)
					{
						if(s instanceof BulletSprite)
						{
							BulletSprite bullet = (BulletSprite) s;
							
							if(bullet.killable && pbs.collision(bullet))
							{
								this.createBulletPoint(bullet);
								bullet.destroy();
							}
						}
					}
				}
			}
		}
			
		
	}
	
	public void killPlayer()
	{
		if(!player.isDead)
		{
			player.isDead = true;
			soundPlayer.play("sound/die2.wav");
		}
	}
	
	
	public void spawnItems(ArrayList<ItemSprite> items, double x, double y)
	{
		for(ItemSprite item : items)
		{
			//System.out.println("spawned a " + item.type);
			double dx = (rand.nextInt(200)-100)/12.5; // 
			double dy = (0-rand.nextInt(5))/5.0 - 1; // 
			item.vars[0] = 10;
			item.dx = dx;
			item.dy = dy;
			item.x = x;
			item.y = y;
			item.setSemiTransparency(0.3);
			
			if(item.isValidType())
			{
				powerUps.add(item);
			}
		}
		items.clear();
	}
	
	
	public void createBulletPoint(BulletSprite bullet)
	{
		//System.out.println("spawned a " + item.type);
		ItemSprite item = new ItemSprite(bullet.x, bullet.y, 's');
		item.dx = 0;
		item.dy = -1.0;
		item.isMagneted = true;
		
		FXSprite bulletDie = FXSprite.makeSimpleOrb1(bullet.x,bullet.y);
		sfx.add(bulletDie);
		
		powerUps.add(item);
	}
	
	public void convertAllBulletsToPoints()
	{
		for(Object o : curStage.bullets)
		{
			BulletSprite bullet = (BulletSprite) o;
			
			createBulletPoint(bullet);
		}
	}
	
	/**
	*	void powerUps()
	*	manages logic and runs player-powerup collision detection.
	**/
	
	private void powerUps()
	{
		for(Object o : powerUps)
		{
			ItemSprite item = (ItemSprite) o;
			
			if(!item.isMagneted)
			{
				if(item.vars[0] > 0) // firework-type spread when it first spawns.
				{
					item.vars[0]--;
					item.x += item.dx;
				}
				else
				{
					item.dy += 0.01; // descend slowly
				}
				item.y += item.dy;
				
				if(item.y > SCREENH + 10)
					item.destroy();
			}
			else
			{
				double dx = (player.x - item.x)/10.0;
				double dy = (player.y - item.y)/10.0;
				
				if(Math.abs(dx) < 4)
					dx = 4 * Math.abs(dx)/dx;
				if(Math.abs(dy) < 4)
					dy = 4 * Math.abs(dy)/dy;
				
				item.x += dx;
				item.y += dy;
				
				
			}
			quadTree.insert(item); 
			
			// magnet line
			
			if(player.y <= this.MAGNETLINE && power >= 127)
			{
				item.isMagneted = true;
			}
			
			if(player.isDead)
			{
				item.isMagneted = false;
			}
		}
		
		// check for magneting
		
		if(!player.isDead)
		{
			ArrayList<Sprite> cList = quadTree.query(player);
			
			for(Sprite s : cList)
			{
				if(s instanceof ItemSprite)
				{
					ItemSprite item = (ItemSprite) s;
					if(player.collision(item))
					{
						item.isMagneted = true;
					}
				}
			}
			
			
			// check collisions with items
			
			cList = quadTree.query(pHitBox);
			
			for(Sprite s : cList)
			{
				if(s instanceof ItemSprite)
				{
					ItemSprite item = (ItemSprite) s;
					if(pHitBox.collision(item))
					{
						long points = 0;
						
						switch(item.type)
						{
							case 'p':
								power += 1;
								points = Math.round((SCREENH - player.y)*10.0 / SCREENH)*100;
								if(soundPlayer.soundsPlaying() < 8)
								{
									soundPlayer.play("sound/BLEEP03.WAV");
								}
								break;
							case 'P':
								power += 5;
								points = Math.round((SCREENH - player.y)*10.0 / SCREENH)*100;
								if(soundPlayer.soundsPlaying() < 8)
								{
									soundPlayer.play("sound/BLEEP03.WAV");
								}
								break;
							case 'F':
								power = 127;
							//	points = Math.round((SCREENH - player.y)*10.0 / SCREENH)*100; // NO POINTS FOR USING A CONTINUE!!!
								break;
							case 's':
								points = 103;
								if(soundPlayer.soundsPlaying() < 8)
								{
									soundPlayer.play("sound/CLICK01.WAV");
								}
								break;
							case 'S':
								power += 1;
								points = Math.round((SCREENH - player.y)*20.0 / SCREENH)*5001;
								if(soundPlayer.soundsPlaying() < 8)
								{
									soundPlayer.play("sound/BLEEP03.WAV");
								}
								break;
							case 'B':
								if(iconBombs.value < 7)
									iconBombs.value++;
								else
									points = 12345;
								break;
							case '1':
								if(iconLives.value < 7)
									iconLives.value++;
								else if(iconBombs.value < 7)
									iconBombs.value++;
								else
									points = 12345;
								soundPlayer.play("sound/1up.WAV");
								break;
							default:
								break;
						}
						
						if(power > 127)	power = 127;
						gainPoints(points);
						item.destroy();
					}
				}
			}
		}
	}
	
	
	
	public void gainPoints(long points)
	{
		score += points;
	}
	
	
	public void moveSFX()
	{
		for(Object o : sfx)
		{
			FXSprite fx = (FXSprite) o;
			fx.move();
		}
	}
	
	
	
	// PLAYER ATTACK METHODS
	
	/**
	*	homingYoyo()
	*	This method manages the logic for the player's homing yoyo attack mode.
	**/
	
	private void homingYoyo()
	{	
		if(player.vars[0] > 0)
			player.vars[0] -= 2;
		if(player.vars[1] > 0)
			player.vars[1] -= 2;
		
		// SHOOTING YOYOS
		
		if(keyboard.isPressed(config.VK_SHOT) && player.vars[0] <= 0 && !player.isDead && !player.isBombing) // small yoyos
		{
			PlayerBulletSprite bullet;
			
			char type = 'a';
			int dmg = 4;
			if(this.power >= 127)
			{
				type = 'b';
				dmg = 6;
			}
			
			double yoyoPathScale = 1.0;
			if(keyboard.isPressed(config.VK_SLOW))
				yoyoPathScale = 0.5;
			
			bullet = new PlayerBulletSprite(0,0,type,dmg);
			bullet.vars[1] = yoyoPathScale;
			bullet.vars[0] = -80+rand.nextInt(5);
			bullet.setSemiTransparency(0.8);
			yoyos1.add(bullet);
			
			bullet = new PlayerBulletSprite(0,0,type,dmg/2);
			bullet.vars[1] = yoyoPathScale;
			bullet.vars[0] = -75+rand.nextInt(5);
			bullet.setSemiTransparency(0.5);
			yoyos1.add(bullet);
			
			bullet = new PlayerBulletSprite(0,0,type,dmg/2);
			bullet.vars[1] = yoyoPathScale;
			bullet.vars[0] = -70+rand.nextInt(5);
			bullet.setSemiTransparency(0.2);
			yoyos1.add(bullet);
			
			soundPlayer.play("sound/yoyo.wav");
			
			player.vars[0] = 32;
		}
		if(keyboard.isPressed(config.VK_SHOT) && player.vars[1] <= 0 && !player.isDead && !player.isBombing) // homing yoyos
		{
			PlayerBulletSprite bullet;
			Sprite closestEnemy = this.getNearestEnemy(player.x, player.y);
			
			bullet = new PlayerBulletSprite(0,0,'b',8);
			bullet.vars[0] = rand.nextInt(10) + 20;
			bullet.targetSprite = closestEnemy;
			bullet.setSemiTransparency(0.4);
			yoyos2.add(bullet);
			
			bullet = new PlayerBulletSprite(0,0,'b',0);
			bullet.vars[0] = rand.nextInt(10)+10;
			bullet.targetSprite = closestEnemy;
			bullet.setSemiTransparency(0.6);
			yoyos2.add(bullet);
			
			bullet = new PlayerBulletSprite(0,0,'b',0);
			bullet.vars[0] = rand.nextInt(10);
			bullet.targetSprite = closestEnemy;
			bullet.setSemiTransparency(0.8);
			yoyos2.add(bullet);
			
			
			player.vars[1] = 64;
		}
		
		/////////// bombing
		
		if(keyboard.isPressed(config.VK_BOMB) && !player.isBombing && !player.isDead && iconBombs.value > 0)
		{
			player.isBombing = true;
			player.vars[2] = 0;
			iconBombs.value--;
			soundPlayer.play("sound/AlienInjection.wav");
			
			for(Object o : powerUps)
			{
				ItemSprite item = (ItemSprite) o;
				item.isMagneted = true;
			}
			
			PlayerBulletSprite yoyo;
				
			yoyo = new PlayerBulletSprite(player.x, player.y , 'l', 3);
			yoyo.permanent = true;
			yoyo.isBomb = true;
			yoyo.vars[1] = 0;
			yoyo.vars[2] = 2.5;
			yoyoBombs1.add(yoyo);
			
			yoyo = new PlayerBulletSprite(player.x, player.y , 'l', 3);
			yoyo.permanent = true;
			yoyo.isBomb = true;
			yoyo.vars[1] = 90;
			yoyo.vars[2] = 3;
			yoyoBombs1.add(yoyo);
			
			yoyo = new PlayerBulletSprite(player.x, player.y , 'l', 3);
			yoyo.permanent = true;
			yoyo.isBomb = true;
			yoyo.vars[1] = 180;
			yoyo.vars[2] = 4;
			yoyoBombs1.add(yoyo);
			
			yoyo = new PlayerBulletSprite(player.x, player.y , 'l', 3);
			yoyo.permanent = true;
			yoyo.isBomb = true;
			yoyo.vars[1] = 270;
			yoyo.vars[2] = 5;
			yoyoBombs1.add(yoyo);
		}
		
		if(player.isBombing)
		{
			player.tempInvincibility = (int) (30 + player.vars[2] % 2);

			player.vars[2] += 1;
			
			if(player.vars[2] > 90+180)
			{
				player.vars[2] = 0;
				player.isBombing = false;
			}
		}
		
		
		
		// YOYO MOVEMENT LOGIC
		
		
		for(Object s : yoyos1) // group 1 yoyo logic
		{
			PlayerBulletSprite yoyo = (PlayerBulletSprite) s;
			
			yoyo.x = player.x + yoyo.vars[1] * 64 * GameMath.cos(yoyo.vars[0]);
			yoyo.y = player.y - 80/yoyo.vars[1] - 80* GameMath.sin(yoyo.vars[0])/yoyo.vars[1];
			yoyo.vars[0] += 16;
			yoyo.rotate(yoyo.vars[0]);
			
			quadTree.insert(yoyo);
			
			if(yoyo.vars[0] >= 270)
				yoyo.destroy();
		}
		yoyos1.update();
		
		for(Object s : yoyos2) // group 2 homing yoyo logic
		{
			PlayerBulletSprite yoyo = (PlayerBulletSprite) s;
			
			yoyo.x = player.x*Math.abs(GameMath.cos(yoyo.vars[0])) + (1-Math.abs(GameMath.cos(yoyo.vars[0]))) * yoyo.targetSprite.x + 32 * Math.abs(GameMath.sin(yoyo.vars[0] * 2)) * GameMath.cos(yoyo.vars[0]);
			yoyo.y = player.y*Math.abs(GameMath.cos(yoyo.vars[0])) + (1-Math.abs(GameMath.cos(yoyo.vars[0]))) * yoyo.targetSprite.y - 32 * Math.abs(GameMath.sin(yoyo.vars[0] * 2)) * GameMath.sin(yoyo.vars[0]);
			yoyo.vars[0] += 10;
			yoyo.rotate(yoyo.vars[0]*2);
			
			quadTree.insert(yoyo);
			
			if(yoyo.vars[0] >= 180)
				yoyo.destroy();
		}
		yoyos2.update();
		
		
		for(Object s : yoyos3) // group 3 yoyo shield logic
		{
			PlayerBulletSprite yoyo = (PlayerBulletSprite) s;
			
			if(keyboard.isPressed(config.VK_SLOW))
			{
				yoyo.vars[0] += 16;
				yoyo.vars[1] += (1.0 - yoyo.vars[1])/2.0;
			}
			else
			{
				yoyo.vars[0] += 8;
				yoyo.vars[1] += (2.5 - yoyo.vars[1])/2.0;
			}
			
			
			yoyo.x = player.x + yoyo.vars[1] * 32 * GameMath.cos(yoyo.vars[0]);
			yoyo.y = player.y - yoyo.vars[1] * 32 * GameMath.sin(yoyo.vars[0]);
			
			yoyo.rotate(0-yoyo.vars[0]);
			
			if(yoyo.vars[0] >= 360)
				yoyo.vars[0] -= 360;
			
			yoyo.isVisible = false;
			if(power >= 50 && yoyo.id == 0 && !player.isDead)
			{
				yoyo.isVisible = true;
				quadTree.insert(yoyo);
			}
			if(power >= 100 && yoyo.id == 1 && !player.isDead)
			{
				yoyo.isVisible = true;
				quadTree.insert(yoyo);
			}
		}
		
		
		for(Object s : yoyoBombs1) //  yoyo bombs 1 logic
		{
			PlayerBulletSprite yoyo = (PlayerBulletSprite) s;
			
			if(yoyo.vars[0] < 330)
			{
				Point2D.Double point = new Point2D.Double(100-100*GameMath.cos(yoyo.vars[0]), 100*GameMath.sin(yoyo.vars[0]));
				AffineTransform rotate = AffineTransform.getRotateInstance(GameMath.d2r(0-yoyo.vars[1]));
				rotate.transform(point,point);
				yoyo.x = player.x + point.x;
				yoyo.y = player.y + point.y;
				
				yoyo.vars[0]+= yoyo.vars[2];
			}
			else if(yoyo.vars[0] >= 330 && yoyo.targetSprite == null)
			{
				yoyo.targetSprite = getNearestEnemy(yoyo.x,yoyo.y);
				yoyo.vars[0] = 360;
				yoyo.vars[1] = yoyo.x;
				yoyo.vars[2] = yoyo.y;
			}
			else
			{
				yoyo.x = yoyo.vars[1]*Math.abs(GameMath.cos(yoyo.vars[0])) + (1-Math.abs(GameMath.cos(yoyo.vars[0]))) * yoyo.targetSprite.x + 64 * Math.abs(GameMath.sin(yoyo.vars[0] * 2)) * GameMath.cos(yoyo.vars[0]);
				yoyo.y = yoyo.vars[2]*Math.abs(GameMath.cos(yoyo.vars[0])) + (1-Math.abs(GameMath.cos(yoyo.vars[0]))) * yoyo.targetSprite.y - 64 * Math.abs(GameMath.sin(yoyo.vars[0] * 2)) * GameMath.sin(yoyo.vars[0]);
				yoyo.vars[0] += 4;
				
				if(yoyo.vars[0] >= 90+360)
				{
					// create burst
					for(int i = 0 ; i < 10; i++)
					{
						PlayerBulletSprite burstYoyo = new PlayerBulletSprite(yoyo.x, yoyo.y,'m',1);
						double angle = rand.nextInt(360);
						double speed = rand.nextInt(20)*0.25;
						burstYoyo.vars[0] = speed*GameMath.cos(angle);
						burstYoyo.vars[1] = speed*GameMath.sin(angle);
						burstYoyo.permanent = true;
						burstYoyo.isBomb = true;
						yoyoBombs2.add(burstYoyo);
					}
					
					soundPlayer.play("sound/EXPLOD05.WAV");
					screenShake = 80;
					yoyo.destroy();
				}
			}
			
			yoyo.rotate(yoyo.vars[0]*4);
			
			quadTree.insert(yoyo);
		}
		yoyoBombs1.update();
		
		for(Object s : yoyoBombs2) //  yoyo bombs 2 logic
		{
			PlayerBulletSprite yoyo = (PlayerBulletSprite) s;
			
			yoyo.x += yoyo.vars[0];
			yoyo.y += yoyo.vars[1];
			
			yoyo.scale(1.0-yoyo.vars[2]/50.0, 1.0-yoyo.vars[2]/50.0);
			yoyo.setSemiTransparency(0.2 + yoyo.vars[2]/50.0);
			
			yoyo.vars[2]++;
			
			if(yoyo.vars[2] >= 40)
				yoyo.destroy();
			quadTree.insert(yoyo);
		}
		yoyoBombs2.update();
		
		// speed powerups
		
		if(power >= 40)
		{
			player.vars[0] -=2;
			player.vars[1] -=1;
		}
		if(power >= 60)
		{
			player.vars[1] -=1;
		}
		if(power >= 80)
		{
			player.vars[1] -=1;
		}
		if(power >= 100)
		{
			player.vars[0] -=2;
			player.vars[1] -=1;
		}
		if(power >= 120)
		{
			player.vars[1] -=2;
		}
		if(power >= 127)
		{
			player.vars[1] -=4;
		}
		
		if(player.vars[0] < 0)
			player.vars[0] = 0;
		if(player.vars[1] < 0)
			player.vars[1] = 0;
		
	}
	
	
	private Sprite getNearestEnemy(double x, double y)
	{
		Sprite nearest = curStage.getNearestEnemy(x,y);
		if(nearest == null)
			nearest = new EnemySprite(player.x, player.y - 200,'a',0); // it's really just a point 200 pixels in front of the player.
		return nearest;
	}
	
	
	/**
	*	illusionBeatdown()
	*	This method manages the logic for the player's illusion beatdown attack mode.
	**/
	
	private void illusionBeatdown()
	{
		if(player.vars[0] > 0)
			player.vars[0] -= 1;
		if(player.vars[1] > 0)
			player.vars[1] -= 1;
			
		///////// Mid spawner logic
		// mid spawner movement	
		
		ibMidSpawner.targetX = player.x;
		ibMidSpawner.targetY = player.y;
		ibMidSpawner.vars[2] = 25;
		if(keyboard.isPressed(config.VK_SLOW) && !player.isDead)
			ibMidSpawner.vars[2] = -25;
		if(player.isBombing)
			ibMidSpawner.vars[2] = 50;
			
		ibMidSpawner.x += (ibMidSpawner.targetX - ibMidSpawner.x)/2;
		ibMidSpawner.y += (ibMidSpawner.targetY + ibMidSpawner.vars[2] - ibMidSpawner.y)/2;
		
		// mid spawner shooting
		
		ibMidSpawner.type = 'g';
		if(power >= 40 || player.isBombing)
			ibMidSpawner.type = 'h';
		
		if(keyboard.isPressed(config.VK_SHOT) && player.vars[0] <= 0 && !player.isDead && !player.isBombing)
		{
			char type = 'c';
			int dmg = 3;
			if(power >= 40)
			{
				type = 'd';
				dmg = 5;
			}
			
			PlayerBulletSprite bullet;
			
			bullet = new PlayerBulletSprite(ibMidSpawner.x, ibMidSpawner.y, type, dmg);
			bullet.spawner = ibMidSpawner;
			bullet.vars[0] = rand.nextInt(10);
			bullet.rotate(rand.nextInt(4)*90);
			ibArrows.add(bullet);
			
			soundPlayer.play("sound/SCRAPE01.WAV");
			
			player.vars[0] = 6;
		}
		
		
		///////////// Side spawner logic
		
		for(Object s : ibSideSpawners)
		{
			PlayerBulletSprite spawner = (PlayerBulletSprite) s;
			spawner.isVisible = false;
			if(power >= 80 || player.isBombing)
				spawner.isVisible = true;
			
			// side spawner movement
			
			spawner.vars[2] = 25;
			spawner.vars[1] = 2;
			if(keyboard.isPressed(config.VK_SLOW) && !player.isDead && !player.isBombing)
			{
				spawner.vars[1] = 1;
				spawner.vars[2] = -25;
			}
			else
			{
				if(player.isBombing)
				{
					spawner.vars[2] = 50;
					spawner.vars[1] = 4;
				}
				spawner.targetX = player.x;
				spawner.targetY = player.y;
			}
			
			spawner.x += ((spawner.targetX + spawner.vars[0] * spawner.vars[1] * 15) - spawner.x)/2;
			spawner.y += (spawner.targetY + spawner.vars[2] - spawner.y)/2;
		}
		
		// side spawner shooting
		
		PlayerBulletSprite upSpawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(2);
		PlayerBulletSprite downSpawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(1);
		PlayerBulletSprite rightSpawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(3);
		PlayerBulletSprite leftSpawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(0);
		
		upSpawner.type = 'e';
		downSpawner.type = 'e';
		leftSpawner.type = 'e';
		rightSpawner.type = 'e';
		
		if(power >= 25)
		{
			upSpawner.isVisible = true;
			downSpawner.isVisible = true;
		}
		
		if(power >= 100)
		{
			upSpawner.type = 'f';
			downSpawner.type = 'f';
		}
		if(power >= 127)
		{
			leftSpawner.type = 'f';
			rightSpawner.type = 'f';
		}
		
		
		if(keyboard.isPressed(config.VK_SHOT) && player.vars[1] <= 0 && power >= 25 && !player.isDead && !player.isBombing)
		{
			PlayerBulletSprite spawner; 
			PlayerBulletSprite bullet;
			
			char type = 'c';
			int dmg = 2;
			
			if(power >= 100)
			{
				type = 'd';
				dmg = 4;
			}
			
			spawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(rand.nextInt(2)+1); // pick randomly between up and down arrow to fire
			bullet = new PlayerBulletSprite(spawner.x, spawner.y, type, dmg);
			bullet.vars[0] = rand.nextInt(10);
			bullet.rotate(spawner.getRotate());
			bullet.spawner = spawner;
			ibArrows.add(bullet);
			
			if(power < 127)
			{
				type ='c';
				dmg = 2;
			}
			
			if(power >= 80)
			{
				spawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(rand.nextInt(2)*3); // pick randomly between right and left arrow to fire
				bullet = new PlayerBulletSprite(spawner.x, spawner.y, type, dmg);
				bullet.vars[0] = rand.nextInt(10);
				bullet.rotate(spawner.getRotate());
				bullet.spawner = spawner;
				ibArrows.add(bullet);
			}
			
			player.vars[1] = 8;
		}
		
		/////////// bombing
		
		if(keyboard.isPressed(config.VK_BOMB) && !player.isBombing && !player.isDead && iconBombs.value > 0)
		{
			player.isBombing = true;
			player.vars[2] = 1;
			iconBombs.value--;
			soundPlayer.play("sound/HITSNTH4.WAV");
			
			// create bomb laser
			
			PlayerBulletSprite laser = new PlayerBulletSprite(player.x, player.y, 'n', 5);
			laser.permanent = true;
			laser.isBomb = true;
			laser.scale(0.1,25.0);
			laser.setSemiTransparency(0.3);
			ibBombLaser.add(laser);
		}
		
		if(player.isBombing)
		{
			player.tempInvincibility = (int) (30 + player.vars[2] % 2);
			screenShake = 100;

			for(Object o : powerUps)
			{
				ItemSprite item = (ItemSprite) o;
				item.isMagneted = true;
			}	
			
			player.vars[2]++;
			
			for(Object s : ibBombLaser)
			{
				PlayerBulletSprite laser = (PlayerBulletSprite) s;
				
				laser.x = player.x;
				laser.y = player.y-(16*25);
				
				if(player.vars[2] > 40)
				{
					laser.scale(laser.getScaleX() + (15.0 - laser.getScaleX())*0.25, 25.0);
					
					PlayerBulletSprite spawner; 
					PlayerBulletSprite bullet;
					
					for(Object o : ibSideSpawners)
					{
						spawner = (PlayerBulletSprite) o;
						spawner.type = 'f';
					}
					
					char type = 'd';
					int dmg = 4;
					
					int spawnerID = rand.nextInt(5);
					if(spawnerID != 4)
					{
						spawner = (PlayerBulletSprite) ibSideSpawners.sprList.get(spawnerID); // pick randomly between up and down arrow to fire
						
						bullet = new PlayerBulletSprite(spawner.x, spawner.y, type, dmg);
						bullet.vars[0] = rand.nextInt(10);
						bullet.rotate(spawner.getRotate());
						bullet.scale(2.0,2.0);
						bullet.spawner = spawner;
						ibArrows.add(bullet);
					}
					else
					{
						bullet = new PlayerBulletSprite(ibMidSpawner.x, ibMidSpawner.y, type, dmg);
						bullet.spawner = ibMidSpawner;
						bullet.vars[0] = rand.nextInt(10);
						bullet.rotate(rand.nextInt(4)*90);
						bullet.scale(2.0,2.0);
						ibArrows.add(bullet);
					}
				}
				if(player.vars[2] == 40)
				{
					soundPlayer.play("sound/BeamDown.wav");
				}
				if(player.vars[2] > 200)
				{
					laser.setSemiTransparency(laser.getSemiTransparency() + (1.0 - laser.getSemiTransparency())*0.02);
				}
				if(player.vars[2] > 270)
				{
					laser.destroy();
				}
				
				quadTree.insert(laser);
			}
			
			if(player.vars[2] > 270)
			{
				player.vars[2] = 0;
				player.isBombing = false;
				
			}
		}
		
		
		
		// arrow movement

		for(Object s : ibArrows)
		{
			PlayerBulletSprite arrow = (PlayerBulletSprite) s;
			
			arrow.x = arrow.spawner.x;
			arrow.y = arrow.spawner.y - arrow.vars[0];
			arrow.vars[0] += arrow.vars[1];
			arrow.vars[1] += 2;
			if(arrow.vars[1] >= 25)
				arrow.vars[1] = 25;
			
			quadTree.insert(arrow);
			
			if(arrow.y <= -10)
				arrow.destroy();
		}
		
		if(power >= 40)
		{
			player.vars[0] -= 1;
		}
		if(power >= 60)
		{
			player.vars[1] -= 1;
		}
		if(power >= 90)
		{
			player.vars[0] -= 1;
		}
		if(power >= 110)
		{
			player.vars[1] -= 1;
		}
		
		if(player.vars[0] < 0)
			player.vars[0] = 0;
		if(player.vars[1] < 0)
			player.vars[1] = 0;
	}
	
	/**
	*	persuasionFropick()
	*	This method manages the logic for the player's persuasion fropick attack mode.
	**/
	
	private void persuasionFropick()
	{
		if(player.vars[0] > 0)
			player.vars[0] -= 1;
		
		/////////// shooting
		
		if(keyboard.isPressed(config.VK_SHOT) && player.vars[0] <= 0 && !player.isDead && !player.isBombing)
		{
			char type = 'i';
			int dmg = 2;
			
			if(!keyboard.isPressed(config.VK_SLOW))
			{
				PlayerBulletSprite froPick;
				
				type = 'i';
				dmg = 2;
				
				if(power >= 60)
				{
					type = 'j';
					dmg = 3;
				}
				
			/*	froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
				froPick.vars[0] = 90;
				froPick.vars[1] = rand.nextInt(10);
				froPick.rotate(froPick.vars[0]);
				froPicks.add(froPick);*/
				
				froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
				froPick.vars[0] = 90 + rand.nextInt(16) - 8;
				froPick.vars[1] = rand.nextInt(10);
				froPick.rotate(froPick.vars[0]);
				froPicks.add(froPick);
				
				froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
				froPick.vars[0] = 90 + rand.nextInt(16) - 8;
				froPick.vars[1] = rand.nextInt(10);
				froPick.rotate(froPick.vars[0]);
				froPicks.add(froPick);
				
				
				type = 'i';
				dmg = 2;
				
				if(power >= 25)
				{
					if(power >= 60)
					{
						type = 'j';
						dmg = 3;
					}
					
					froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
					froPick.vars[0] = 90 - rand.nextInt(30);
					froPick.vars[1] = rand.nextInt(10);
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
					
					froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
					froPick.vars[0] = 90 + rand.nextInt(30);
					froPick.vars[1] = rand.nextInt(10);
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
				}
				
				type = 'i';
				dmg = 2;
				
				if(power >= 40)
				{
					if(power >= 100)
					{
						type = 'j';
						dmg = 3;
					}
					
					froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
					froPick.vars[0] = 90 - 45 + rand.nextInt(15);
					froPick.vars[1] = rand.nextInt(10);
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
					
					froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
					froPick.vars[0] = 90 + 45 - rand.nextInt(15);
					froPick.vars[1] = rand.nextInt(10);
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
				}
				
				
				type = 'i';
				dmg = 2;
				if(power >= 80)
				{
					if(power >= 127)
					{
						type = 'j';
						dmg = 3;
					}
					
					froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
					froPick.vars[0] = 90 - 60 + rand.nextInt(40);
					froPick.vars[1] = rand.nextInt(10);
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
					
					froPick = new PlayerBulletSprite(player.x, player.y-20, type, dmg);
					froPick.vars[0] = 90 + 60 - rand.nextInt(40);
					froPick.vars[1] = rand.nextInt(10);
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
				}
				
			}
			else
			{
				PlayerBulletSprite froPick;

				type = 'j';
				dmg = 3;
				
				froPick = new PlayerBulletSprite(player.x, player.y-20-rand.nextInt(10), type, dmg);
				froPick.vars[0] = 90;
				froPick.rotate(froPick.vars[0]);
				froPicks.add(froPick);
				
				type = 'i';
				dmg = 2;

				if(power >= 25)
				{
					if(power >= 40)
					{
						type = 'j';
						dmg = 3;
					}
					
					froPick = new PlayerBulletSprite(player.x + rand.nextInt(50)-25 , player.y-20-rand.nextInt(10), type, dmg);
					froPick.vars[0] = 90;
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
				}
				
				type = 'i';
				dmg = 2;

				if(power >= 40)
				{
					if(power >= 80)
					{
						type = 'j';
						dmg = 3;
					}
					
					froPick = new PlayerBulletSprite(player.x + rand.nextInt(50)-25 , player.y-20-rand.nextInt(10), type, dmg);
					froPick.vars[0] = 90;
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
				}
				
				type = 'i';
				dmg = 2;

				if(power >= 80)
				{
					if(power >= 127)
					{
						type = 'j';
						dmg = 3;
					}
					
					froPick = new PlayerBulletSprite(player.x + rand.nextInt(50)-25 , player.y-20-rand.nextInt(10), type, dmg);
					froPick.vars[0] = 90;
					froPick.rotate(froPick.vars[0]);
					froPicks.add(froPick);
				}
			}
			
			soundPlayer.play("sound/SCRAPE01.WAV");
			
			player.vars[0] = 9;
		}
		
		/////////// bombing
		
		if(keyboard.isPressed(config.VK_BOMB) && !player.isBombing && !player.isDead && iconBombs.value > 0)
		{
			player.isBombing = true;
			player.vars[2] = 90;
			iconBombs.value--;
			soundPlayer.play("sound/WHOOSH09.WAV");
			
			
		}
		
		if(player.isBombing)
		{
			player.tempInvincibility = (int) (30 + player.vars[2] % 2);
			screenShake = 70;
			PlayerBulletSprite froPick;
				
			for(Object o : powerUps)
			{
				ItemSprite item = (ItemSprite) o;
				item.isMagneted = true;
			}	
			
			froPick = new PlayerBulletSprite(player.x + 20*GameMath.cos(player.vars[2]), player.y - 20 * GameMath.sin(player.vars[2]) , 'k', 10);
			froPick.permanent = true;
			froPick.isBomb = true;
			froPick.vars[0] = player.vars[2];
			froPick.vars[1] = 8;
			froPick.rotate(froPick.vars[0]);
			froPicks.add(froPick);
			
			froPick = new PlayerBulletSprite(player.x + 20*GameMath.cos(player.vars[2]), player.y - 20 * GameMath.sin(player.vars[2]), 'j', 5);
			froPick.permanent = true;
			froPick.isBomb = true;
			froPick.vars[0] = player.vars[2]+10;
			froPick.vars[1] = 3;
			froPick.rotate(froPick.vars[0]);
			froPicks.add(froPick);
			
			froPick = new PlayerBulletSprite(player.x + 20*GameMath.cos(player.vars[2]), player.y - 20 * GameMath.sin(player.vars[2]), 'j', 5);
			froPick.permanent = true;
			froPick.isBomb = true;
			froPick.vars[0] = player.vars[2]-10;
			froPick.vars[1] = 3;
			froPick.rotate(froPick.vars[0]);
			froPicks.add(froPick);
			
			froPick = new PlayerBulletSprite(player.x + 20*GameMath.cos(player.vars[2]), player.y - 20 * GameMath.sin(player.vars[2]), 'j', 5);
			froPick.permanent = true;
			froPick.isBomb = true;
			froPick.vars[0] = player.vars[2];
			froPick.rotate(froPick.vars[0]);
			froPicks.add(froPick);

			player.vars[2] += 20;
			
			if(player.vars[2] > 720+90)
			{
				player.vars[2] = 0;
				player.isBombing = false;
			}
		}
		
		
		/////////////// fropick movement
		
		for(Object o : froPicks)
		{
			PlayerBulletSprite froPick = (PlayerBulletSprite) o;
			
			froPick.x += froPick.vars[1] * GameMath.cos(froPick.vars[0]);
			froPick.y -= froPick.vars[1] * GameMath.sin(froPick.vars[0]);
			
			froPick.vars[1]+= 0.4;
			quadTree.insert(froPick);
			
			if(froPick.x < -40 || froPick.x > SCREENW + 40 || froPick.y < -40 || froPick.y > SCREENH + 40)
				froPick.destroy();
		}
		
		
		if(power >= 30)
			player.vars[0] -= 1;
		if(power >= 70)
			player.vars[0] -= 1;
		if(power >= 110)
			player.vars[0] -= 1;
		if(power >= 127)
			player.vars[0] -= 1;
		
		if(player.vars[0] < 0)
			player.vars[0] = 0;
		
	}
	
	
	/////////////// PAUSE and CONTINUE/GAMEOVER interfaces
	
	
	private boolean promptContinue()
	{
		midi.stop();
		if(numContinues == 0)
		{
			// Game Over
			parentYoBro.changeCurrentLevel("TitleMenuLevel");
			return true;
		}
		else
		{
			// set pause to paused and set promptContinue interface to on.
			textPaused.vars[0] = 1;
			textPaused.vars[1] = 0;
			this.isPaused = true;
			return false;
		}
	}
	
	/**
	*	boolean pausedMenu()
	*	returns true if changing away from MainLevel.
	**/
	
	private boolean pausedMenu()
	{
		if(textAreYouSure.vars[0] == 0) {
			textPaused.isVisible = true;
			textResume.isVisible = true;
			textRetry.isVisible = true;
			textReturnToTitle.isVisible = true;
			
			if(textPaused.vars[1] == 0)
			{
				textResume.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.isPaused = false;
					midi.play(false);
				}
			}
			if(textPaused.vars[1] == 1)
			{
				textRetry.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					textAreYouSure.vars[0] = 1;
					textPaused.vars[1] = 0;
				}
			}
			if(textPaused.vars[1] == 2)
			{
				textReturnToTitle.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					textAreYouSure.vars[0] = 2;
					textPaused.vars[1] = 0;
				}
			}
			
			
			if(keyboard.isTyped(config.VK_UP))
				textPaused.vars[1]--;
			if(keyboard.isTyped(config.VK_DOWN))
				textPaused.vars[1]++;
			
			if(textPaused.vars[1] < 0)
				textPaused.vars[1] = 2;
			if(textPaused.vars[1] > 2)
				textPaused.vars[1] = 0;
		}
		else {
			textAreYouSure.isVisible = true;
			textYes.isVisible = true;
			textNo.isVisible = true;
		
			if(textPaused.vars[1] == 0)
			{
				textYes.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					if(textAreYouSure.vars[0] == 1) {
						parentYoBro.changeCurrentLevel("MainLevel");
						return true;
					}
					if(textAreYouSure.vars[0] == 2) {
						parentYoBro.changeCurrentLevel("TitleMenuLevel");
						return true;
					}
				}
			}
			if(textPaused.vars[1] == 1)
			{
				textNo.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					textPaused.vars[1] = textAreYouSure.vars[0];
					textAreYouSure.vars[0] = 0;
					return false;
				}
			}
			
			
			if(keyboard.isTyped(config.VK_UP))
				textPaused.vars[1]--;
			if(keyboard.isTyped(config.VK_DOWN))
				textPaused.vars[1]++;
			
			if(textPaused.vars[1] < 0)
				textPaused.vars[1] = 1;
			if(textPaused.vars[1] > 1)
				textPaused.vars[1] = 0;
		}
		return false;
	}
	
	/**
	*	boolean continueMenu()
	*	returns true if changing away from MainLevel.
	**/
	
	private boolean continueMenu()
	{
		textYes.isVisible = true;
		textNo.isVisible = true;
		
		if(textAreYouSure.vars[0] == 0) {
			textContinue.isVisible = true;
			textContRem.isVisible = true;
			textContRem.vars[0] = numContinues;
			
			if(textPaused.vars[1] == 0)
			{
				textYes.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.isPaused = false;
					midi.play(false);
					
					if(!parentYoBro.gameMode.equals("Tutorial"))
					numContinues--;
					iconLives.value = config.startingLives;
				}
			}
			if(textPaused.vars[1] == 1)
			{
				textNo.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					textAreYouSure.vars[0] = 1;
					textPaused.vars[1] = 0;
				}
			}
		}
		else {
			textAreYouSure.isVisible = true;
			
			if(textPaused.vars[1] == 0)
			{
				textYes.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					parentYoBro.changeCurrentLevel("TitleMenuLevel");
					return true;
				}
			}
			if(textPaused.vars[1] == 1)
			{
				textNo.isActive = true;
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					textAreYouSure.vars[0] = 0;
					textPaused.vars[1] = 1;
					return false;
				}
			}
		}
		
		if(keyboard.isTyped(config.VK_UP))
			textPaused.vars[1]--;
		if(keyboard.isTyped(config.VK_DOWN))
			textPaused.vars[1]++;
		
		if(textPaused.vars[1] < 0)
			textPaused.vars[1] = 1;
		if(textPaused.vars[1] > 1)
			textPaused.vars[1] = 0;
			
		return false;
	}
	
	
	/**
	*	boolean stageCleared()
	*	returns true if changing away from MainLevel.
	**/
	
	private boolean stageCleared()
	{
		textPaused.vars[1]++;
		textStageClear.isVisible = true;
		
		config.musicVol *= 0.99;
		dBox.paragraph = 0;
		
		boolean exitMainLevel = false;
		
		if(((keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP)) && textPaused.vars[1] > 100) || textPaused.vars[1] > 250)
		{
			// load next stage
			
			config.musicVol = this.originalMidiVolume;
			
			if(curStageNum == 0) // End of tutorial stage
			{
				parentYoBro.changeCurrentLevel("TitleMenuLevel");
				exitMainLevel = true;
			}
			else if(curStageNum == -1)
			{
				// start Stage 2
				curStageNum = 1;
				
				curStage = new Stage1(this, parentYoBro, -100);
				
			//	parentYoBro.changeCurrentLevel("CreditsLevel");
			//	exitMainLevel = true;
			}
			else if(curStageNum == 1)
			{
				// start Stage 2
				curStageNum = 2;
				
				curStage = new Stage2(this, parentYoBro, -100);
				
			//	parentYoBro.changeCurrentLevel("CreditsLevel");
			//	exitMainLevel = true;
			}
			else if(curStageNum == 2)
			{
				// start Stage 3
				curStageNum = 3;
				
				curStage = new Stage3(this, parentYoBro, -100);
				
			//	parentYoBro.changeCurrentLevel("TitleMenuLevel");
			//	exitMainLevel = true;
			}
			else if(curStageNum == 3)
			{
				// start Stage 4
				curStageNum = 4;
				
				curStage = new Stage4(this, parentYoBro, -100);
				
			//	parentYoBro.changeCurrentLevel("CreditsLevel");
			//	exitMainLevel = true;
			}
			else if(curStageNum == 4)
			{
				// start Stage 5
				curStageNum = 5;
				
			//	curStage = new Stage5(this, parentYoBro, -100);
				
				parentYoBro.changeCurrentLevel("CreditsLevel");
				exitMainLevel = true;
			}
			
			this.reyPortrait.x = -200;
			this.reyPortrait.y = 300;
			this.dBox.isVisible = false;
			this.isPaused = false;
			textPaused.vars[1] = 0;
		}
		
		
		return exitMainLevel;
	}
	
	
	//////////////////////// Animation methods
	
	/**
	*	animates all sprites that need to be animated during the unpaused timer loop
	*
	**/
	
	private void animateAllUnpaused()
	{
		sfx.animate(imgLoader);
		
		player.animate(imgLoader);
		pHitBox.animate(imgLoader);
		
		if(parentYoBro.attackMode == 1)
		{
			yoyos1.animate(imgLoader);
			yoyos2.animate(imgLoader);
			yoyos3.animate(imgLoader);
			yoyoBombs1.animate(imgLoader);
			yoyoBombs2.animate(imgLoader);
		}
		
		if(parentYoBro.attackMode == 2)
		{
			ibSideSpawners.animate(imgLoader);
			ibMidSpawner.animate(imgLoader);
			
			ibArrows.animate(imgLoader);
			ibBombLaser.animate(imgLoader);
		}
		
		if(parentYoBro.attackMode == 3)
		{
			froPicks.animate(imgLoader);
		}
		
		panelMask.animate(imgLoader);
		textLives.animate(imgLoader);
		textBombs.animate(imgLoader);
		textPower.animate(imgLoader);
		textGraze.animate(imgLoader);
		textScore.animate(imgLoader);
		reyPortrait.animate(imgLoader);
		
		iconEnemyHere.animate(imgLoader);
		
		numberPower.value = power;
		numberPower.animate(imgLoader);
		
		numberGraze.value = graze;
		numberGraze.animate(imgLoader);
		
		numberScore.value = score;
		numberScore.animate(imgLoader);
		
		numberSCTimer.animate(imgLoader);
		barBossHealth.animate(imgLoader);
		
		iconBombs.animate(imgLoader);
		iconLives.animate(imgLoader);
		
		curStage.animate(imgLoader);
		
		powerUps.animate(imgLoader);
		
		imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
	}
	
	/**
	*	animates all sprites that need to be animated during the paused timer loop
	*
	**/
	
	private void animateAllPaused()
	{
		textPaused.animate(imgLoader);
		textAreYouSure.animate(imgLoader);
		textContinue.animate(imgLoader);
		textContRem.animate(imgLoader);
		textResume.animate(imgLoader);
		textRetry.animate(imgLoader);
		textReturnToTitle.animate(imgLoader);
		textYes.animate(imgLoader);
		textNo.animate(imgLoader);
		textStageClear.animate(imgLoader);
		
		imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
	}
	
	
	/**
	*	render(Graphics2D g2D
	*
	*
	**/
	
	public void render(Graphics2D g2D)
	{
		
		
		AffineTransform originalTransform = g2D.getTransform();
		g2D.translate(this.ORIGINX, this.ORIGINY);
		
		// screen shake transform
		
		if(screenShake > 0)
		{
			g2D.translate((rand.nextInt(screenShake)-screenShake/2)/10,(rand.nextInt(screenShake)-screenShake/2)/10);
		}
		
		// Background layer
		
		curStage.renderBackground(g2D);
		
		// player bullet layer
		
		if(parentYoBro.attackMode == 1)
		{
			yoyos1.render(g2D);
			yoyos2.render(g2D);
			yoyos3.render(g2D);
			yoyoBombs1.render(g2D);
			yoyoBombs2.render(g2D);
		}
		
		if(parentYoBro.attackMode == 2)
		{
			ibSideSpawners.render(g2D);
			ibMidSpawner.render(g2D);
			
			ibArrows.render(g2D);
			ibBombLaser.render(g2D);
		}
		
		if(parentYoBro.attackMode == 3)
		{
			froPicks.render(g2D);
		}
		
		// enemy layer
		
		curStage.renderEnemies(g2D);
		
		// player layer
		
		player.render(g2D);
		powerUps.render(g2D);
		
		
		// enemy bullet layer
		
		curStage.renderBullets(g2D);
		
		// sFx layer
		
		sfx.render(g2D);
		
		// top-mid layer
		
		pHitBox.render(g2D);
		
		// top layer ( stage title graphics / bgm title should go here)
		
		curStage.renderForeground(g2D);
		reyPortrait.render(g2D);
		dBox.render(g2D);
		
		
		
		
		// HUD graphics
		
		if(this.isPaused)
		{
			if(textPaused.vars[0] == 0) // paused
			{
				g2D.setColor(new Color(0x667777FF, true));
				g2D.fillRect(0,0,SCREENW,SCREENH);
			}
			else if(textPaused.vars[0] == 1) { // continue?
				g2D.setColor(new Color(0x667777FF, true));
				g2D.fillRect(0,0,SCREENW,SCREENH);
			}
			else if(textPaused.vars[0] == 3) // cleared stage
			{
				
				g2D.setColor(new Color(215,176,47,(int) Math.min(textPaused.vars[1]*2,180)));
				g2D.fillRect(0,0,SCREENW,SCREENH);
			/*	if(curStageNum == 1)
				{
					textDemoClear.render(g2D);
				}*/
			}
			
			textPaused.render(g2D);
			textAreYouSure.render(g2D);
			textContinue.render(g2D);
			textContRem.render(g2D);
			textResume.render(g2D);
			textRetry.render(g2D);
			textReturnToTitle.render(g2D);
			textYes.render(g2D);
			textNo.render(g2D);
			textStageClear.render(g2D);
		}
		
		g2D.setTransform(originalTransform);
		
		barBossHealth.render(g2D);
		
		numberSCTimer.render(g2D);
		panelMask.render(g2D);
		textLives.render(g2D);
		textBombs.render(g2D);
		textPower.render(g2D);
		textGraze.render(g2D);
		textScore.render(g2D);
		numberPower.render(g2D);
		numberGraze.render(g2D);
		numberScore.render(g2D);
		iconBombs.render(g2D);
		iconLives.render(g2D);
		iconEnemyHere.render(g2D);
	}
	
}



