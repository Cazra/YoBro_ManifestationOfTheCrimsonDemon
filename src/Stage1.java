import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import gameEngine.*;

public class Stage1 extends Stage
{
	// essential members
	
	// Sprites
	
	ABGStage1Sprite background;
	
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
	
	/*
	*	Constructor
	*	Preconditions: ml is a reference to the game's main level interface which contains this stage.
	*	Postconditions: This stage is initialized and loads its data.
	*/
	
	public Stage1(MainLevel ml, YoBroMainPanel mp)
	{
		super(ml, mp);
		spawnerX = 0;
		spawnerY = 0;
		
		
	}
	
	public Stage1(MainLevel ml, YoBroMainPanel mp, long initTimer)
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
		System.out.println("Loading Stage 1");
		// load additional graphics
		
		BossCitrus.loadImages(mainLevel.imgLoader);
		ABGStage1Sprite.loadImages(mainLevel.imgLoader);
		
		// load music
		mainLevel.loopMusic("sound/music/NightHighway.mid");
	//	mainLevel.loopMusic("sound/Soul Food Red as Pepperoni.mid");
		
		// initialize Sprites and SpriteLists.
		
		background = new ABGStage1Sprite(mainLevel);
		
	//	bossBullets = new SpriteList();
		bossSlaves = new SpriteList();
		
		citrus = new BossCitrus(-50,0, mainLevel);
		citrus.stageBG = background;
		
		textStageNum = new StageTextSprite(150, 140, '1',"");
		textStageNum.setSemiTransparency(1.0);
		textStageNum.isVisible = false;
		//textStageTitle = new StageTextSprite(200,200,'t',"Yellow Adversaries Stalk the Night");
		textStageTitle = new StageTextSprite(200,200,'T',"stage1");
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
		citrus.destroy();
		bossSlaves.destroyAll();
		BossCitrus.clean();
		ABGStage1Sprite.clean();
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
		
		
		
		
		checkPt = 100;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.1
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.2
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("P"));
			}
		}
		
		checkPt += 200;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.3
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p "));
			}
		}
		
		checkPt += 170;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.4
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pppP  "));
			}
		}
		
		
		checkPt += 200;
		if(timer >= checkPt && timer <= checkPt +100)
		{ // wave 2.1
			if(timer == checkPt)
				spawnerX = 20;
				
			if((timer - checkPt) % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(spawnerX,-10, 'c', 5);
				enemy.aiType = 3;
				enemy.vars[0] = 0;
				enemy.vars[1] = 40;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("SSppP"));
				
				spawnerX += 40;
			}
			
			
		}
		
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 2.2
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		
		checkPt += 100;
		if(timer >= checkPt && timer <= checkPt +100)
		{ // wave 2.3
			if(timer == checkPt)
				spawnerX = mainLevel.SCREENW - 20;
				
			if((timer - checkPt) % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(spawnerX,-10, 'c', 5);
				enemy.aiType = 3;
				enemy.vars[0] = 0;
				enemy.vars[1] = 40;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("ppSSP"));
				
				spawnerX -= 40;
			}
			
			
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 2.4
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("P"));
			}
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 2.5
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("P"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
		}
		
		///// MIDBOSS
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +100)
		{
			if(timer == checkPt)
			{
				citrus.aiMode = 0;
				citrus.vars[0] = mainLevel.SCREENW/2;
				citrus.vars[1] = 100;
				citrus.setDirection('E');
				
				citrus.itemDrops.add(makeItem("p"));
				citrus.itemDrops.add(makeItem("p"));
				citrus.itemDrops.add(makeItem("p"));
				citrus.itemDrops.add(makeItem("p"));
				citrus.itemDrops.add(makeItem("P"));
				
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
		
		///// PART 2
		
		checkPt += 50;
		if(timer >= checkPt && timer <= checkPt +500)
		{ // enemy wave 3.1
			if( (timer + checkPt) % 40 == 0)
			{
				spawnerX = mainLevel.rand.nextInt(mainLevel.SCREENW - 40) + 20;
				
				EnemySprite enemy = new EnemySprite(spawnerX,-10, 'c', 5);
				enemy.aiType = 4;
				enemy.vars[0] = 0;
				enemy.vars[1] = 25;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS  "));
			}
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 3.2
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 3.3
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 4.1.1
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
		}
		
		checkPt += 70;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 4.1.2
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 4.2.1
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("pS"));
			}
		}
		
		checkPt += 70;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 4.2.2
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("P"));
			}
		}
		
		
		checkPt += 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 4.3.1
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("P"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
		}
		
		checkPt += 70;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 4.3.2
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(400-120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(400-140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(400-80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(400-100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -80;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			
			if(this.timer == checkPt)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+15)
			{
				EnemySprite enemy = new EnemySprite(140,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("P"));
			}
			if(this.timer == checkPt+30)
			{
				EnemySprite enemy = new EnemySprite(80,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
			if(this.timer == checkPt+45)
			{
				EnemySprite enemy = new EnemySprite(100,-10, 'c', 1);
				enemy.aiType = 1;
				enemy.vars[0] = -100;
				enemy.vars[1] = 100;
				enemies.add(enemy);
			}
		}
		
		
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
				citrus.aiMode = 5;
				citrus.x = mainLevel.SCREENW + 20;
				citrus.vars[0] = mainLevel.SCREENW/2;
				citrus.vars[1] = 100;
				citrus.setDirection('W');
			}
			if(timer == checkPt + 1) // freeze the timer so that we don't progress any further until the midboss has been dealt with.
				timerPaused = true;
			
			this.runMidBossLogic();
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
			EnemySprite smiley = (EnemySprite) o;
			
			if(smiley.aiType == 0)
			{
				System.out.println("bad ai");
				smiley.destroy();
			}
			else if(smiley.aiType == 1)
			{
				double speed = Math.min(5, smiley.vars[1]);
				smiley.x += speed*GameMath.cos(smiley.vars[0]);
				smiley.y -= speed*GameMath.sin(smiley.vars[0]);
				
				smiley.vars[1] *= .9;
				
				if(smiley.vars[1] < 0.1)
				{
					smiley.aiType = 2;
					smiley.vars[1] = 5;
					if(smiley.x < mainLevel.SCREENW/2)
						smiley.vars[0] = 0 + rand.nextInt(40) - 20;
					else
						smiley.vars[0] = 180 + rand.nextInt(40) - 20;
					
					if(!difficulty.equals("Easy"))
						mainLevel.soundPlayer.play("sound/BIGLAS3.WAV");
					if(difficulty.equals("Normal"))
					{
						BulletSprite bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
						double angle = getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
						bullet.setVelocity(3.0,angle);
						bullets.add(bullet);
						
						bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
						bullet.setVelocity(3.0,angle + 60);
						bullets.add(bullet);
						
						bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
						bullet.setVelocity(3.0,angle - 60);
						bullets.add(bullet);
					}
					if(difficulty.equals("Hard"))
					{
						double angle = getAngleTo(smiley.x, smiley.y, mainLevel.player.x, mainLevel.player.y);
						
						for(double i = 1.0; i <= 3.0; i += 1.0) {
							BulletSprite bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
							bullet.setVelocity(i,angle);
							bullets.add(bullet);
							
							bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
							bullet.setVelocity(i,angle + 60);
							bullets.add(bullet);
							
							bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
							bullet.setVelocity(i,angle - 60);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Lunatic"))
					{
						double angle = getAngleTo(smiley.x, smiley.y, mainLevel.player.x, mainLevel.player.y);
						
						for(double i = angle-20; i <= angle+20 ; i+=20)
						{
							BulletSprite bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
							bullet.setVelocity(3.0,i);
							bullets.add(bullet);
							
							bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
							bullet.setVelocity(2.0,i);
							bullets.add(bullet);
							
							bullet = new BulletSprite(smiley.x, smiley.y,'a',new Color(0xFF7700));
							bullet.setVelocity(1.0,i);
							bullets.add(bullet);
						}
					}
				}
			}
			else if(smiley.aiType == 2) // move in straight line.
			{
				double speed = smiley.vars[1];
				smiley.x += speed*cos(smiley.vars[0]);
				smiley.y -= speed*sin(smiley.vars[0]);
				
				if(isOffscreen(smiley))
					smiley.destroy();
			}
			else if(smiley.aiType == 3)
			{
				smiley.y += Math.min(7, smiley.vars[1]);
				
				smiley.vars[1] *= .9;
				smiley.vars[0] *= .9;
				
				if(smiley.vars[1] < 0.1 && smiley.vars[0] < 0.1)
				{
					smiley.aiType = 2;
					smiley.vars[1] = 5;
					
					smiley.vars[0] = 90 + rand.nextInt(40) - 20;
					
					
					BulletSprite bullet;
					double angle = getAngleTo(smiley.x, smiley.y, mainLevel.player.x, mainLevel.player.y);
					
					mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
					
					if(difficulty.equals("Easy"))
					{	
						bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));	
						bullet.setVelocity(2.0,angle);
						bullets.add(bullet);
					}
					if(difficulty.equals("Normal"))
					{
						bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));	
						bullet.setVelocity(3.0,angle);
						bullets.add(bullet);
						
						bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
						angle = getAngleTo(bullet.x, bullet.y, mainLevel.player.x, mainLevel.player.y);
						bullet.setVelocity(2.0,angle);
						bullets.add(bullet);
					}
					if(difficulty.equals("Hard"))
					{
						for(double i = 1.0; i <= 3.0 ; i+= 0.5)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));	
							bullet.setVelocity(i,angle);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Lunatic"))
					{
						for(double i = 0.4; i <= 3.0 ; i+= 0.2)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));	
							bullet.setVelocity(i,angle);
							bullets.add(bullet);
						}
					}
				}
			}
			else if(smiley.aiType == 4)
			{
				smiley.y += Math.min(10, smiley.vars[1]);
				
				smiley.vars[1] *= .9;
				smiley.vars[0] *= .9;
				
				smiley.vars[2]++;
				
				if(smiley.vars[2] == 10) {
					BulletSprite bullet;
					double angle = rand.nextInt(30);
					
					mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
					
					if(difficulty.equals("Easy"))
					{
						for(double i = angle ; i < angle + 360; i += 90)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.setVelocity(2.0,i);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Normal"))
					{
						for(double i = angle ; i < angle + 360; i += 60)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.setVelocity(3.0,i);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Hard"))
					{
						for(double i = angle ; i < angle + 360; i += 40)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.aiMode = 2;
							bullet.setRotationalVelocity(3.0,i, 3, 0.95);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Lunatic"))
					{
						for(double i = angle ; i < angle + 360; i += 40)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.aiMode = 2;
							bullet.setRotationalVelocity(4.0,i, 4, 0.95);
							bullets.add(bullet);
						}
					}
				}
				
				if(smiley.vars[1] < 0.1 && smiley.vars[0] < 0.1)
				{
					smiley.aiType = 2;
					smiley.vars[1] = 5;
					
					smiley.vars[0] = 90 + rand.nextInt(30) - 15;
					
					BulletSprite bullet;
					double angle = rand.nextInt(30);
					
					mainLevel.soundPlayer.play("sound/flabbyLaser.wav");
					
					if(difficulty.equals("Easy"))
					{
						for(double i = angle ; i < angle + 360; i += 90)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.setVelocity(2.0,i);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Normal"))
					{
						for(double i = angle ; i < angle + 360; i += 60)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.setVelocity(3.0,i);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Hard"))
					{
						for(double i = angle ; i < angle + 360; i += 40)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.aiMode = 2;
							bullet.setRotationalVelocity(3.0,i, 3, 0.95);
							bullets.add(bullet);
						}
					}
					if(difficulty.equals("Lunatic"))
					{
						for(double i = angle ; i < angle + 360; i += 40)
						{
							bullet = new BulletSprite(smiley.x, smiley.y,'e',new Color(0x5500FF));
							bullet.aiMode = 2;
							bullet.setRotationalVelocity(4.0,i, 4, 0.95);
							bullets.add(bullet);
						}
					}
				}
			}
			
			
			quadTree.insert(smiley);
			
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
		
		if(citrus.aiMode == 0) // glide into playable area
		{
			citrus.x += (citrus.vars[0] - citrus.x)/15.0;
			citrus.y += (citrus.vars[1] - citrus.y)/15.0;
			
			if(Math.abs(citrus.x - citrus.vars[0]) < 1.0 && Math.abs(citrus.y - citrus.vars[1]) < 1.0)
			{
				citrus.aiMode = 1;
				citrus.vars[0] = 0;
			}
		}
		else if(citrus.aiMode == 1) // stop for midboss dialogue (if any)
		{
			citrus.isActive = false;
			citrus.setDirection('S');
			citrus.y = citrus.vars[1] + 2*sin(citrus.vars[0]);
			
			citrus.vars[0]++;
			
			// Dialogue happens
			
			// After dialogue, initiate first attack
			
			citrus.aiMode = 2;
			citrus.timer = 0;
		}
		else if(citrus.aiMode == 2) // begin midboss attack pattern
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			citrus.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)citrus.hp;
			
			if(citrus.attackIndex == 2)
				citrus.aiMode = 3;
		}
		else if (citrus.aiMode == 3)
		{
			background.nextBGID = 0;
			citrus.setDirection('S');
			citrus.isActive = false;
			
			if(citrus.timer > 30)
			{
				citrus.aiMode = 4;
				citrus.timer = 0;
				timer++;
				timerPaused = false;
				citrus.vars[0] = 1;
			}
				
			citrus.timer++;
			
		}
		else if (citrus.aiMode == 4)
		{
			if(citrus.y > -20)
			{
				citrus.y -= citrus.vars[0];
				citrus.vars[0] *= 1.1;
			}
		}
		else if(citrus.aiMode == 5) // stop for boss dialogue (if any)
		{	
			runDialogScript();
		}
		else if(citrus.aiMode == 6) // glide into playable area
		{
			citrus.x += (citrus.vars[0] - citrus.x)/15.0;
			citrus.y += (citrus.vars[1] - citrus.y)/15.0;
			
			setPlayerPortrait(":)","listen");
			
			if(Math.abs(citrus.x - citrus.vars[0]) < 1.0 && Math.abs(citrus.y - citrus.vars[1]) < 1.0)
			{
				citrus.aiMode = 7;
				citrus.vars[0] = 0;
			}
		}
		else if(citrus.aiMode == 7) // stop for boss dialogue (if any)
		{
			runDialogScript();
		}
		else if(citrus.aiMode == 8) // begin boss attack patterns
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			citrus.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)citrus.hp;
			
			if(citrus.attackIndex == citrus.lastAttackIndex + 1)
			{
				citrus.aiMode = 9;
				citrus.isActive = false;
				mainLevel.dBox.paragraph++;
			}
		}
		else if(citrus.aiMode == 9)
		{
			runDialogScript();
		}
		else if(citrus.aiMode == 10)
		{
			this.endStage = true;
		}
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.x += (mainLevel.reyPortrait.vars[0] - mainLevel.reyPortrait.x) * 0.05;
			mainLevel.reyPortrait.y += (mainLevel.reyPortrait.vars[1] - mainLevel.reyPortrait.y) * 0.05;
		}
		if(!citrus.portrait.emote.equals("attack"))
		{
			citrus.portrait.x += (citrus.portrait.vars[0] - citrus.portrait.x) * 0.05;
			citrus.portrait.y += (citrus.portrait.vars[1] - citrus.portrait.y) * 0.05;
		}
		
		quadTree.insert(citrus);
	}
	
	
	
	
	private void runDialogScript()
	{
		citrus.isActive = false;
		citrus.setDirection('S');
		citrus.y = citrus.vars[1] + 2*sin(citrus.vars[0]);
		citrus.vars[0]++;
		
		// Dialogue happens
		
		DialogBoxSprite dBox = mainLevel.dBox;
		dBox.isVisible = true;
		
		if(keyboard.isPressed(config.VK_SKIP) || keyboard.isTyped(config.VK_SHOT))
			dBox.paragraph++;
		
		int para = 0;

		if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Man, they got away!\n";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":(","speak");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Those smileys absconded with \n" +
							"my take-out pizza!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Who is that ahead? \n" +
							"The crazy fruit guy again?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Wonder what he wants...";
		}
		para++;
		
		if(dBox.paragraph == para) ///// Citrus flies in
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","listen");
			dBox.paragraph++;
			citrus.aiMode = 6;
			
			citrus.vars[0] = mainLevel.SCREENW/2;
			citrus.vars[1] = 100;
		}
		para++;
		

		if(dBox.paragraph == para) ///// dialog continues
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Hey there, guy! What's up? \n" +
							"Sorry bout attacking ya earlier!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "I just discovered the decimal system\n" + 
							"and got a bit carried away!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":)","listen");
		
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Hey, grapefruit-head-dude!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":)","listen");
		
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Have you seen any smilies\n" +
							"with a pizza box?";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Who ya callin' a fruit!?\n" + 
							"I'm so totally gonna rough ya up!";
		}
		para++;
		
		// After dialogue, initiate first attack
		if(dBox.paragraph == para)
		{
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			citrus.portrait.setSemiTransparency( 0.5);
			
			mainLevel.loopMusic("sound/music/battytwist2.mid");
			citrus.aiMode = 8;
			mainLevel.barBossHealth.lagValue = 0;
			citrus.timer = 0;
			dBox.isVisible = false;
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			background.nextBGID = 0;
		
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Looks like I win, bro!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":(","speak");
		
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Awwgh!!! The pizza thieves escaped...\n" +
							"Time to pursue them!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			citrus.aiMode = 10;
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
		
		citrus.portrait.emote = emote;

		if(mode.equals("speak"))
		{
			citrus.portrait.vars[0] = CITRUS_SPEAK_X;
			citrus.portrait.vars[1] = CITRUS_SPEAK_Y;
			citrus.portrait.setSemiTransparency(0.0);
		}
		if(mode.equals("listen"))
		{
			citrus.portrait.vars[0] = CITRUS_SPEAK_X;
			citrus.portrait.vars[1] = CITRUS_SPEAK_Y;
			citrus.portrait.setSemiTransparency(0.5);
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
								splosion = FXSprite.makeBurst1(smiley.x, smiley.y);
								mainLevel.sfx.add(splosion);
							}
							mainLevel.soundPlayer.play("sound/zap01.wav");
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
	
	
	///////// HELPER MATH
	
	private double d2r(double degrees)
	{
		return degrees/360.0*2*Math.PI;
	}
	
	private double sin(double degrees)
	{
		return Math.sin(d2r(degrees));
	}
	
	private double cos(double degrees)
	{
		return Math.cos(d2r(degrees));
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
		g2D.setColor(new Color(0xFF110011, true));
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


