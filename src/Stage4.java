import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import gameEngine.*;

public class Stage4 extends Stage
{
	// essential members
	
	// Sprites
	
	ABGStage4Sprite background;
	
	BossPlane bossPlane;
	BossWall wallForth;
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
	
	public Stage4(MainLevel ml, YoBroMainPanel mp)
	{
		super(ml, mp);
		spawnerX = 0;
		spawnerY = 0;
	}
	
	public Stage4(MainLevel ml, YoBroMainPanel mp, long initTimer)
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
		
		ABGStage4Sprite.loadImages(mainLevel.imgLoader);
		
		BossPlane.loadImages(mainLevel.imgLoader);
		BossWall.loadImages(mainLevel.imgLoader);
		
		mainLevel.imgLoader.waitForAll();
		
		// load music
		mainLevel.loopMusic("sound/music/SBKrobot.mid");
	//	mainLevel.loopMusic("sound/Soul Food Red as Pepperoni.mid");
		
		// initialize Sprites and SpriteLists.
		
		background = new ABGStage4Sprite(mainLevel);
		
	//	bossBullets = new SpriteList();
		bossSlaves = new SpriteList();
		
		bossPlane = new BossPlane(0-mainLevel.SCREENW/2,-100, mainLevel);
		bossPlane.stageBG = background;
		
		wallForth = new BossWall(-100, 0, mainLevel);
		wallForth.stageBG = background;
		
		textStageNum = new StageTextSprite(150, 140, '4',"");
		textStageNum.setSemiTransparency(1.0);
		textStageNum.isVisible = false;
	//	textStageTitle = new StageTextSprite(200,200,'t',"I Can Show You The World");
		textStageTitle = new StageTextSprite(200,200,'T',"stage4");
		textStageTitle.setSemiTransparency(1.0);
		textStageTitle.isVisible = false;
		textStageTitle.vars[1] = 5;
        
        obstacles = new SpriteList();
		
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
		bossSlaves.destroyAll();
		BossPlane.clean();
		BossWall.clean();
		
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
		
		this.runEnemyPatterns();
		this.runBulletPatterns();
        this.runObstaclePatterns();
		
		int checkPt;
		
	//	System.out.println(timer);
		
		// Surprise starting wave
		
		checkPt = 0;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(50,-10, 'b', 20);
				enemy.aiType = 1;
				enemy.vars[1] = 270;
				enemy.vars[2] = (int)(timer/5)%10-2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		checkPt = 150;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW - 50,-10, 'b', 20);
				enemy.aiType = 1;
				enemy.vars[1] = 270;
				enemy.vars[2] = (int)(timer/5)%10-2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		checkPt = 300;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(120,-10, 'b', 20);
				enemy.aiType = 1;
				enemy.vars[1] = 270;
				enemy.vars[2] = (int)(timer/5)%10-2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		checkPt = 450;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW - 120,-10, 'b', 20);
				enemy.aiType = 1;
				enemy.vars[1] = 270;
				enemy.vars[2] = (int)(timer/5)%10-2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("p"));
			}
		}
		
		// Stage Title
		
		checkPt = 600;
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
		
		
		checkPt = 700;
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
		
		
		// robots with swervy bullets
		
		checkPt += 200;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(50,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
                
                enemy = new EnemySprite(mainLevel.SCREENW-50,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
		}
		
		checkPt += 100;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-100,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				
				enemy = new EnemySprite(100,-10, 'a', 20);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
		}
		
		checkPt += 100;
		if(timer >= checkPt && timer < checkPt + 100)
		{                
            if(this.timer % 20 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20,100, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 170;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
            if(this.timer % 20 == 10)
			{
				EnemySprite enemy = new EnemySprite(20,100, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 10;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
		}
		
		checkPt += 150;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-50,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 250;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				
				enemy = new EnemySprite(50,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 290;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				
				enemy = new EnemySprite(-50,100, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 330;
				enemy.setDirection('E');
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				
				enemy = new EnemySprite(mainLevel.SCREENW + 50, 100, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 210;
				enemy.setDirection('W');
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
		}
		
		checkPt += 150;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(50,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
        
        checkPt += 20;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(100,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
        
        checkPt += 20;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(150,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
        
        checkPt += 20;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(200,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
        
        checkPt += 20;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(250,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
		
        checkPt += 20;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(300,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
        
        checkPt += 20;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(350,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
		}
        
		// blade bot swarms
		
		checkPt += 200;
		if(timer >= checkPt && timer <= checkPt +70)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20,70, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 180;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
		}
		
		checkPt += 100;
		if(timer >= checkPt && timer <= checkPt +70)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(-20,150, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 0;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
		}
		
		checkPt += 100;
		if(timer >= checkPt && timer <= checkPt +70)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW -80, -20, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 240;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
		}
		
		checkPt += 100;
		if(timer >= checkPt && timer <= checkPt +70)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(80, -20, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 300;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
		}
		
		if(timer == checkPt + 70) background.nextBGID = 1;
		
		// missiles!!!
		
		checkPt += 200;
		if(timer == checkPt)
		{
			EnemySprite missile = new EnemySprite(mainLevel.SCREENW -80, -20, 'h', 50);
			missile.aiType = 4;
			missile.vars[1] = 270;
			missile.vars[2] = 2;
			enemies.add(missile);
			
			EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
			enemies.add(target);
			
			missile.dependents.add(target);
			
			mainLevel.soundPlayer.play("sound/missile1.wav");
		}
		
		checkPt += 50;
		if(timer == checkPt)
		{
			EnemySprite missile = new EnemySprite(80, -20, 'h', 50);
			missile.aiType = 4;
			missile.vars[1] = 270;
			missile.vars[2] = 2;
			enemies.add(missile);
			
			EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
			enemies.add(target);
			
			missile.dependents.add(target);
			
			mainLevel.soundPlayer.play("sound/missile1.wav");
		}
		
		checkPt += 150;
		if(timer >= checkPt && timer < checkPt + 120 && timer % 30 == 0)
		{
			EnemySprite missile = new EnemySprite(mainLevel.SCREENH * (timer % 120)/120.0, -20, 'h', 50);
			missile.aiType = 4;
			missile.vars[1] = 270;
			missile.vars[2] = 2;
			enemies.add(missile);
			
			EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
			enemies.add(target);
			
			missile.dependents.add(target);
			
			mainLevel.soundPlayer.play("sound/missile1.wav");
		}
		
		// a few more blade bots
		checkPt += 120;
		checkPt += 200;
		if(timer >= checkPt && timer <= checkPt +70)
		{ // enemy wave 1.1
			if(this.timer % 10 == 0)
			{
				EnemySprite enemy = new EnemySprite(40,-50, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
            
            if(this.timer % 10 == 5)
			{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-40,-50, 'd', 10);
				enemy.aiType = 3;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
			}
		}
		
		// a few more missiles
		
		checkPt += 200;
		if(timer >= checkPt && timer < checkPt + 120 && timer % 35 == 0)
		{
			EnemySprite missile = new EnemySprite(mainLevel.SCREENH * (120-(timer % 120))/120.0, -20, 'h', 50);
			missile.aiType = 4;
			missile.vars[1] = 270;
			missile.vars[2] = 2;
			enemies.add(missile);
			
			EnemySprite target = new EnemySprite(mainLevel.player.x, mainLevel.player.y, 'i', 9999);
			enemies.add(target);
			
			missile.dependents.add(target);
			
			mainLevel.soundPlayer.play("sound/missile1.wav");
		}
		
		// midboss time!!!!
		
      checkPt += 200;
		
		if(timer >= checkPt && timer <= checkPt +50)
		{
			if(timer == checkPt)
			{
				bossPlane.aiMode = 0;
				bossPlane.vars[0] = mainLevel.SCREENW/2;
				bossPlane.vars[1] = 150;

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
      
      // eye demons
      
      checkPt += 200;
		if(timer >= checkPt && timer <= checkPt +50)
		{ // enemy wave 1.1
			if(this.timer % 5 == 0)
			{
				EnemySprite enemy = new EnemySprite(50,-10, 'b', 20);
				enemy.aiType = 1;
				enemy.vars[1] = 270;
				enemy.vars[2] = (int)(timer/5)%10-2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
            enemy.itemDrops.add(makeItem("p"));
            enemy.itemDrops.add(makeItem("p"));
            
            enemy = new EnemySprite(mainLevel.SCREENW-50,-10, 'b', 20);
				enemy.aiType = 1;
				enemy.vars[1] = 270;
				enemy.vars[2] = (int)(timer/5)%10-2;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("S"));
            enemy.itemDrops.add(makeItem("p"));
            enemy.itemDrops.add(makeItem("p"));
			}
		}
      
      checkPt += 200;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
            
            enemy = new EnemySprite(mainLevel.SCREENW/2+32,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
		}
      
      checkPt += 60;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW*.25,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
            
            enemy = new EnemySprite(mainLevel.SCREENW*.25+32,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
		}
      
      checkPt += 60;
		if(timer == checkPt)
		{
				EnemySprite enemy = new EnemySprite(mainLevel.SCREENW*.75,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
            
            enemy = new EnemySprite(mainLevel.SCREENW*.75+32,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
		}
      
      
      // some more gun bots
      checkPt += 100;
      if(timer == checkPt) {
            EnemySprite enemy;
            
            for(int i = 30; i < mainLevel.SCREENW -20; i+= 80) {
                enemy = new EnemySprite(i,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
            }
      }
      
      checkPt += 30;
      if(timer == checkPt) {
            EnemySprite enemy;
            
            for(int i = 70; i < mainLevel.SCREENW -20; i+= 80) {
                enemy = new EnemySprite(i,-10, 'a', 70);
				enemy.aiType = 2;
				enemy.vars[1] = 270;
				enemies.add(enemy);
				
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
				enemy.itemDrops.add(makeItem("PpSS"));
            }
      }
      
      // suddenly an asteroid!
      checkPt += 30;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW + 160, mainLevel.SCREENH*0.25, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = 160;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obstacles.add(obs);
      }
      
      
      checkPt += 150;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(-160, mainLevel.SCREENH*0.5, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -30;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.5,0.5);
            obstacles.add(obs);
      }
      
      checkPt += 60;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW+160, mainLevel.SCREENH*0.60, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = 210;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.8,0.8);
            obstacles.add(obs);
      }
      
      
      checkPt += 40;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(-160, mainLevel.SCREENH*0.70, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -10;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obstacles.add(obs);
      }
      
      if(timer >= checkPt && timer <= checkPt +70)
      {
        if(this.timer % 10 == 0)
        {
          EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20,40, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 190;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
              
              
        }
           if(this.timer % 10 == 5)
        {  
              EnemySprite enemy = new EnemySprite(-20,40, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = -10;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
           }
      }
      
      checkPt += 50;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW+160, mainLevel.SCREENH*0.20, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -70;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obstacles.add(obs);
      }
      
    
      
      checkPt += 150;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(-160, mainLevel.SCREENH*0.5, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -30;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.7,0.7);
            obstacles.add(obs);
      }
      
      checkPt += 60;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW+160, mainLevel.SCREENH*0.10, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = 210;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.6,0.6);
            obstacles.add(obs);
      }
      
      if(timer >= checkPt && timer <= checkPt +70)
      {
        if(this.timer % 10 == 0)
        {
          EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20, mainLevel.SCREENH - 70, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 170;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
              
              
        }
           if(this.timer % 10 == 5)
        {  
              EnemySprite enemy = new EnemySprite(-20, mainLevel.SCREENH - 70, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 10;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
           }
      }
      
      checkPt += 70;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(-160, mainLevel.SCREENH*0.70, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -10;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.3,0.3);
            obstacles.add(obs);
      }
      
      checkPt += 30;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW+160, mainLevel.SCREENH*0.20, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -70;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obstacles.add(obs);
      }
      
      if(timer >= checkPt && timer <= checkPt +120)
      {
        if(this.timer % 10 == 0)
        {
          EnemySprite enemy = new EnemySprite(mainLevel.SCREENW+20, mainLevel.SCREENH*0.50, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 180;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
              
              
        }
           if(this.timer % 10 == 5)
        {  
              EnemySprite enemy = new EnemySprite(-20, mainLevel.SCREENH*0.50, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 00;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
           }
      }
      
      
      checkPt += 30;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW + 160, mainLevel.SCREENH*0.25, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = 160;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.8,0.8);
            obstacles.add(obs);
      }
      
      
      checkPt += 150;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(-160, mainLevel.SCREENH*0.5, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -30;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obstacles.add(obs);
      }
      
      if(timer >= checkPt && timer <= checkPt +120)
      {
        if(this.timer % 10 == 0)
        {
          EnemySprite enemy = new EnemySprite(mainLevel.SCREENW-50, -20, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 270;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
              
              
        }
           if(this.timer % 10 == 5)
        {  
              EnemySprite enemy = new EnemySprite(-20, mainLevel.SCREENH*0.80, 'd', 10);
          enemy.aiType = 3;
          enemy.vars[1] = 0;
          enemies.add(enemy);
          
          enemy.itemDrops.add(makeItem("S"));
           }
      }
      
      checkPt += 60;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(mainLevel.SCREENW+160, mainLevel.SCREENH*0.60, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = 210;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obs.scale(0.7,0.7);
            obstacles.add(obs);
      }
      
      
      checkPt += 40;
      if(timer == checkPt) {
            ObstacleSprite obs = new ObstacleSprite(-160, mainLevel.SCREENH*0.70, 'a', 99999);
            obs.vars[1] = 2;
            obs.vars[0] = -10;
            obs.vars[2] = 400;
            obs.aiType = 1;
            obstacles.add(obs);
      }
      
      
      checkPt += 200;
      if(timer == checkPt) {
			EnemySprite enemy = new EnemySprite(mainLevel.SCREENW/2, -20, 'a', 1000);
			enemy.skinID = 2;
			enemy.aiType = 5;
			enemies.add(enemy);
			
			enemy.itemDrops.add(makeItem("B"));
      }
      
		if(timer == checkPt + 1000) background.nextBGID = 1;
		if(timer == checkPt + 1200) background.nextBGID = 2;
		
		// Warning! Boss character approaching!
		checkPt += 1400;
		if(timer >= checkPt && timer <= checkPt +50) 
		{
			if(timer == checkPt)
			{
				wallForth.aiMode = 7;
				wallForth.vars[0] = mainLevel.SCREENW/2;
				wallForth.vars[1] = 100;
				wallForth.setDirection('E');
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
		
		
		this.enemy2BulletCollisions();
		this.obstacle2AnyCollisions();
		this.updateSpriteLists();
	}
	
	
	
	private void runEnemyPatterns()
	{
		for(Object o : enemies)
		{
			EnemySprite enemy = (EnemySprite) o;
			
			if(enemy.aiType == 0) // the enemy sits there and does nothing.
			{
			//	System.out.println("bad ai");
			//	enemy.destroy();
			}
			if(enemy.aiType == 1) // eye demons 1 (swoop fly-by)
			{	
				enemy.vars[0]++;
				
				double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				double speed = 4;
				
				if(enemy.vars[0] < 200)	{
					double rotation = GameMath.rotateTowardAngle(enemy.vars[1], angleToPlayer);
					enemy.vars[1] += 3*rotation/Math.abs(rotation);
				}
				if(enemy.vars[0] == 10 || enemy.vars[0] == 200) {
					BulletSprite bullet;
					
					mainLevel.soundPlayer.play("sound/Bart gun.wav");
					
          double inc = 0.25;
          if(difficulty == "Hard" || difficulty == "Lunatic") {
            inc = 0.2;
          }
          
          double wideness = 3;
          if(difficulty == "Easy") {
            wideness = 2;
          }
          if(difficulty == "Hard") {
            wideness = 6;
          }
          if(difficulty == "Lunatic") {
            wideness = 10;
          }
          
					for(double i = 0.0; i <= 1.0; i+= inc) {
						double bulletSpd = i*4 + (1-i)*2;
						double bulletAngOff = i*10 + (1-i)*-10 + enemy.vars[2]*wideness;
						
						bullet = new BulletSprite(enemy.x, enemy.y,'e',new Color(0x0088FF));	
						bullet.setVelocity(bulletSpd, angleToPlayer+bulletAngOff);
						bullets.add(bullet);
            
						if(difficulty != "Easy") {
              bullet = new BulletSprite(enemy.x, enemy.y,'e',new Color(0x0088FF));	
              bullet.setVelocity(bulletSpd, angleToPlayer+bulletAngOff+80);
              bullets.add(bullet);
              
              bullet = new BulletSprite(enemy.x, enemy.y,'e',new Color(0x0088FF));	
              bullet.setVelocity(bulletSpd, angleToPlayer+bulletAngOff-80);
              bullets.add(bullet);
            }
					}
					
				}
				
				enemy.x += speed*GameMath.cos(enemy.vars[1]);
				enemy.y -= speed*GameMath.sin(enemy.vars[1]);
				
				enemy.rotate(enemy.vars[1]);
				
				if(enemy.vars[0] > 100 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 2) // robots 1 (swervy shots)
			{	
				double speed;
				
				if(enemy.vars[0] < 30) speed = 3;
				else if(enemy.vars[0] < 180) speed = 0;
				else speed = 3;
				
				if(enemy.vars[0] == 50) enemy.vars[3] = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
				
				
				if(enemy.vars[0] >= 50 && enemy.vars[0] < 180 && enemy.vars[0] % 5 == 0) {
					BulletSprite bullet;
					double bulletSpd = 7;
					double dTheta = 15;
					double dTheta2 = 15;
          
          if(difficulty == "Easy") {
            bulletSpd = 4;
            dTheta = 15;
            dTheta2 = 30;
          }
          if(difficulty == "Hard") {
            bulletSpd = 9;
            dTheta = 14;
          }
					
					mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
					
					bullet = new BulletSprite(enemy.x + 10*GameMath.cos(enemy.vars[3]),enemy.y - 10*GameMath.sin(enemy.vars[3]),'a',new Color(0xffaa00));
					bullet.scale(1.5, 0.6);
					bullet.setSwervingVelocity(bulletSpd, enemy.vars[3] - 10, dTheta, dTheta2);
					bullets.add(bullet);
					
					bullet = new BulletSprite(enemy.x + 10*GameMath.cos(enemy.vars[3]),enemy.y - 10*GameMath.sin(enemy.vars[3]),'a',new Color(0xffaa00));
					bullet.scale(1.5, 0.6);
					bullet.setSwervingVelocity(bulletSpd, enemy.vars[3] + 10, 0-dTheta, dTheta2);
					bullets.add(bullet);
          
          if(difficulty == "Lunatic") {
            bullet = new BulletSprite(enemy.x + 10*GameMath.cos(enemy.vars[3]),enemy.y - 10*GameMath.sin(enemy.vars[3]),'a',new Color(0xffaa00));
            bullet.scale(1.5, 0.6);
            bullet.setSwervingVelocity(bulletSpd, enemy.vars[3] - 30, dTheta, dTheta2);
            bullets.add(bullet);
            
            bullet = new BulletSprite(enemy.x + 10*GameMath.cos(enemy.vars[3]),enemy.y - 10*GameMath.sin(enemy.vars[3]),'a',new Color(0xffaa00));
            bullet.scale(1.5, 0.6);
            bullet.setSwervingVelocity(bulletSpd, enemy.vars[3] + 30, 0-dTheta, dTheta2);
            bullets.add(bullet);
          }
				}
				
				enemy.vars[2] += (speed - enemy.vars[2])/10.0;
				enemy.x += enemy.vars[2]*GameMath.cos(enemy.vars[1]);
				enemy.y -= enemy.vars[2]*GameMath.sin(enemy.vars[1]);
				
				enemy.vars[0]++;
				
				if(enemy.vars[0] > 100 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 3) // bladebots 1
			{	
				double speed = 5;
				
				if(enemy.vars[0] > 50 && enemy.vars[0] <= 90) enemy.vars[1] += 9;
				if(enemy.vars[0] == 60) {
					BulletSprite bullet;
					double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
					
          double upperSpd = 3;
          double lowerSpd = 1.5;
          double inc = 0.2;
          double angle = 20;
          
          if(difficulty == "Easy") {
            upperSpd = 2;
            lowerSpd = 2;
            inc = 0.25;
            angle = 30;
          }
          if(difficulty == "Hard") {
            upperSpd = 1;
            lowerSpd = 3;
            inc = 0.1;
            angle = 40;
          }
          if(difficulty == "Lunatic") {
            upperSpd = 1;
            lowerSpd = 4;
            inc = 0.1;
            angle = 40;
          }
          
					for(double i = 0.0; i <= 1.0; i+= inc) {
						double bulletSpd = i*upperSpd + (1-i)*lowerSpd;
						double bulletAngle = angleToPlayer + i*angle + (1-i)*-angle + 10;
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xff0000));
						bullet.setVelocity(bulletSpd, bulletAngle);
						
						bullets.add(bullet);
					}
				}
				
				if(enemy.vars[0] >110 && enemy.vars[0] <= 150) enemy.vars[1] -= 9;
				if(enemy.vars[0] == 140) {
					BulletSprite bullet;
					double angleToPlayer = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y);
					double inc = 16;
          
          if(difficulty == "Easy") {
            inc = 30;
          }
          if(difficulty == "Hard") {
            inc = 14;
          }
          if(difficulty == "Lunatic") {
            inc = 12;
          }
          
					for(int i = 0; i < 2; i+= 1) {
						for(int j = 0; j < 360; j+= 16) {
							bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0x00ff00));
							bullet.setVelocity(1+i, angleToPlayer + j);
							
							bullets.add(bullet);
						}
					}
				}
				
				
				
				enemy.x += speed*GameMath.cos(enemy.vars[1]);
				enemy.y -= speed*GameMath.sin(enemy.vars[1]);
				
				enemy.vars[0]++;
				
				if(enemy.vars[0] > 160 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 4) // missiles (aimed at dependent target)
			{	
				enemy.vars[2] *= 1.05;
				
				EnemySprite target = (EnemySprite) enemy.dependents.get(0);
				double angleToTarget = GameMath.getAngleTo(enemy.x, enemy.y, target.x, target.y);
				
				double rotation = GameMath.rotateTowardAngle(enemy.vars[1], angleToTarget);
				enemy.vars[1] += 1*rotation/Math.abs(rotation);
				enemy.rotate(enemy.vars[1]);
				
        double explSize = 2.0;
        double density = 5;
        
        if(difficulty == "Easy") {
          explSize = 1.5;
          density = 4;
        }
        if(difficulty == "Hard") {
          explSize = 2.3;
          density = 6;
        }
        if(difficulty == "Hard") {
          explSize = 2.8;
          density = 7;
        }
        
				// explode if it comes close to the player or the target.
				if(GameMath.getDist(enemy.x,enemy.y,target.x,target.y) < 32 || enemy.collision(mainLevel.player)) {
					for(double i = 0; i < density; i++)
					{
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'i', new Color(0xaaFF00));
						bullet.setFadingVelocity(0.5+mainLevel.rand.nextDouble()*(explSize-1), mainLevel.rand.nextInt(360), 80, 1.5);
						bullets.add(bullet);
					}
					for(double i = 0; i < density+4; i++)
					{
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xffaa00));
						bullet.setFadingVelocity(1.0+mainLevel.rand.nextDouble()*(explSize-0.5), mainLevel.rand.nextInt(360), 50, 3.0);
						bullets.add(bullet);
					}
					for(double i = 0; i < density+8; i++)
					{
						BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xff0000));
						bullet.setFadingVelocity(1.5+mainLevel.rand.nextDouble()*explSize, mainLevel.rand.nextInt(360), 30, 2.0);
						bullets.add(bullet);
					}
					
					mainLevel.screenShake = 20;
					mainLevel.soundPlayer.play("sound/cannon3.WAV");
					
					enemy.destroy();
				}
				
			//	target.x += (mainLevel.player.x - target.x)/25.0;
			//	target.y += (mainLevel.player.y - target.y)/25.0;
				
				enemy.x += enemy.vars[2]*GameMath.cos(enemy.vars[1]);
				enemy.y -= enemy.vars[2]*GameMath.sin(enemy.vars[1]);
				
				enemy.vars[0]++;
				
				if(enemy.vars[0] > 160 && isOffscreen(enemy))
					enemy.destroy();
			}
			if(enemy.aiType == 5) {
				if(enemy.vars[0] < 60) enemy.y += (200 - enemy.y)/35.0;
				if(enemy.vars[0] >= 60 && enemy.vars[0] < 800) {
        
          int shotRate = 70;
          int angleInc = 20;
          if(difficulty == "Easy") {
            angleInc = 40;
          }
          if(difficulty == "Hard") {
            shotRate = 56;
          }
          if(difficulty == "Lunatic") {
            shotRate = 40;
          }
          
					if(enemy.vars[0] % shotRate == 0) {
						int offsetAngle = ((int) enemy.vars[0]) % 17;
						
						for(int i = 0; i < 360; i += angleInc) {
							BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0x55ff00));
							bullet.setVelocity(4,i+offsetAngle);
							bullet.aiMode = -900; // custom
							bullet.vars[0] = 1;
							bullets.add(bullet);
						}
					}
					if(enemy.vars[0] % shotRate == shotRate/2) {
						int offsetAngle = ((int) enemy.vars[0]) % 17;
						
						for(int i = 0; i < 360; i += angleInc) {
							BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xff0055));
							bullet.setVelocity(4,i+offsetAngle);
							bullet.aiMode = -900; // custom
							bullet.vars[0] = -1;
							bullets.add(bullet);
						}
					}
					/*
					if(enemy.vars[0] % 120 == 0) {
						int offsetAngle = ((int) enemy.vars[0]) % 17;
						
						for(int i = 0; i < 360; i += 20) {
							BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0x55ff00));
							bullet.scale(2.0,2.0);
							bullet.setRotationalVelocity(2,i+offsetAngle,5,0.98);
							bullet.vars[0] = 1;
							bullets.add(bullet);
						}
					}
					if(enemy.vars[0] % 120 == 60) {
						int offsetAngle = ((int) enemy.vars[0]) % 17;
						
						for(int i = 0; i < 360; i += 20) {
							BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xff0055));
							bullet.scale(2.0,2.0);
							bullet.setRotationalVelocity(2,i+offsetAngle,-5,0.98);
							bullet.vars[0] = -1;
							bullets.add(bullet);
						}
					}
					*/
					
				/*	if(enemy.vars[0] % 5 == 0) {
						BulletSprite bullet;
						double bulletSpd = 7;
						double dTheta = 15*enemy.vars[5] + 20*(1-enemy.vars[5]);
						double dTheta2 = 10*enemy.vars[5] + 12*(1-enemy.vars[5]);
						
						enemy.vars[3] = GameMath.getAngleTo(enemy.x, enemy.y, mainLevel.player.x, mainLevel.player.y) + mainLevel.rand.nextDouble()*6 - 3;
						enemy.vars[4]+= 5;
						enemy.vars[5] = Math.abs(GameMath.sin(enemy.vars[4]));
						
						mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
						
						bullet = new BulletSprite(enemy.x + 10*GameMath.cos(enemy.vars[3]),enemy.y - 10*GameMath.sin(enemy.vars[3]),'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setSwervingVelocity(bulletSpd, enemy.vars[3], dTheta, dTheta2);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x + 10*GameMath.cos(enemy.vars[3]),enemy.y - 10*GameMath.sin(enemy.vars[3]),'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setSwervingVelocity(bulletSpd, enemy.vars[3], 0-dTheta, dTheta2);
						bullets.add(bullet);
					}
					*/
					if(enemy.vars[0] % 6 == 0) {
						mainLevel.soundPlayer.play("sound/DISTZAP.WAV");
						enemy.vars[3]+= 8;
						
						BulletSprite bullet;
						int bulletSpd = 4;
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setVelocity(bulletSpd,enemy.vars[3]);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setVelocity(bulletSpd,0-enemy.vars[3]);
						bullets.add(bullet);
						
						enemy.vars[3]+= 120;
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setVelocity(bulletSpd,enemy.vars[3]);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setVelocity(bulletSpd,0-enemy.vars[3]);
						bullets.add(bullet);
						
						enemy.vars[3]+= 120;
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setVelocity(bulletSpd,enemy.vars[3]);
						bullets.add(bullet);
						
						bullet = new BulletSprite(enemy.x, enemy.y,'a',new Color(0xffaa00));
						bullet.scale(1.5, 0.6);
						bullet.setVelocity(bulletSpd,0-enemy.vars[3]);
						bullets.add(bullet);
						
						enemy.vars[3]+= 120;
					}
					
				}
				
				if(enemy.vars[0] >= 800) enemy.y++;
				
				enemy.vars[0]++;
				
				if(enemy.vars[0] > 160 && isOffscreen(enemy))
					enemy.destroy();
			}
			
			
			
			quadTree.insert(enemy);
			
		}
		
	}
	
    
    /** Runs the ai patterns for obstacle type sprites. */
    private void runObstaclePatterns() {
        for(Object o : obstacles)
		{
			ObstacleSprite sprite = (ObstacleSprite) o;
			
			if(sprite.aiType == 0) { // the enemy sits there and does nothing.
			}
            if(sprite.aiType == 1) { // straight line constant speed, limited lifespan
                sprite.x += sprite.vars[1]*GameMath.cos(sprite.vars[0]);
                sprite.y -= sprite.vars[1]*GameMath.sin(sprite.vars[1]);
                
                sprite.vars[2]--;
                if(sprite.vars[2] <0)
                    sprite.destroy();
            }
            
            quadTree.insert(sprite);
        }
    }
    
	private void runMidBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "Bomber Mac 15";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		
		// AI modes
		
		if(bossPlane.aiMode == 0) // glide into playable area
		{
			bossPlane.x += (bossPlane.vars[0] - bossPlane.x)/30.0;
			bossPlane.y += (bossPlane.vars[1] - bossPlane.y)/30.0;
         bossPlane.tilt = -20;
         
			if(Math.abs(bossPlane.x - bossPlane.vars[0]) < 2.0 && Math.abs(bossPlane.y - bossPlane.vars[1]) < 2.0)
			{
				bossPlane.aiMode = 1;
			}
		}
		else if(bossPlane.aiMode == 1) // stop for midboss dialogue (if any)
		{
			bossPlane.isActive = false;
			// Dialogue happens
			
			// After dialogue, initiate first attack
			
			bossPlane.aiMode = 2;
         bossPlane.tilt = 0;
			bossPlane.timer = 0;
		}
		else if(bossPlane.aiMode == 2) // begin midboss attack pattern
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			bossPlane.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)bossPlane.hp;
			
			if(bossPlane.attackIndex >= 1)
				bossPlane.aiMode = 3;
		}
		else if (bossPlane.aiMode == 3)
		{
			bossPlane.isActive = false;
			// background.nextBGID = 1;
			bossPlane.aiMode = 4;
			timer++;
			timerPaused = false;
					
		}
		
		
		
		quadTree.insert(bossPlane);
	}
	
	private void runBossLogic()
	{
		// dialog portrait positions
		
		mainLevel.barBossHealth.caption1 = "Wall Fourthward";
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.vars[0] = PLAYER_OFF_X;
			mainLevel.reyPortrait.vars[1] = PLAYER_OFF_Y;
		}
		if(!wallForth.portrait.emote.equals("attack"))
		{
			wallForth.portrait.vars[0] = CITRUS_OFF_X;
			wallForth.portrait.vars[1] = CITRUS_OFF_Y;
		}
		

		if(wallForth.aiMode == 7)
		{
			mainLevel.barBossHealth.caption1 = "Wall Fourthward";
			wallForth.vars[0] = mainLevel.SCREENW/2;
			wallForth.vars[1] = 100;
			wallForth.x += (wallForth.vars[0] - wallForth.x)/20.0;
			wallForth.y += (wallForth.vars[1] - wallForth.y)/20.0;
			if(Math.abs(wallForth.x  - wallForth.vars[0]) < 2) wallForth.setDirection('S');
			runDialogScript();
		}
		else if(wallForth.aiMode == 8)
		{
			mainLevel.barBossHealth.isVisible = true;
			mainLevel.numberSCTimer.isVisible = true;
			
			wallForth.runSpellCardLogic(bullets, bossSlaves, mainLevel.sfx);
			mainLevel.barBossHealth.value = (long)wallForth.hp;
			
			if(wallForth.attackIndex == wallForth.lastAttackIndex + 1)
			{
				wallForth.aiMode = 9;
				wallForth.isActive = false;
				mainLevel.dBox.paragraph++;
				background.nextBGID = 2;
				mainLevel.barBossHealth.caption1 = "";
			}
		}
		else if(wallForth.aiMode == 9)
		{
			runDialogScript();
		}
		else if(wallForth.aiMode == 10)
		{
			this.endStage = true;
		}
		
		if(!mainLevel.reyPortrait.equals("attack"))
		{
			mainLevel.reyPortrait.x += (mainLevel.reyPortrait.vars[0] - mainLevel.reyPortrait.x) * 0.05;
			mainLevel.reyPortrait.y += (mainLevel.reyPortrait.vars[1] - mainLevel.reyPortrait.y) * 0.05;
		}
		if(!wallForth.portrait.emote.equals("attack"))
		{
			wallForth.portrait.x += (wallForth.portrait.vars[0] - wallForth.portrait.x) * 0.05;
			wallForth.portrait.y += (wallForth.portrait.vars[1] - wallForth.portrait.y) * 0.05;
		}
		
		quadTree.insert(wallForth);
	}
	
	
	private void runDialogScript()
	{
		wallForth.isActive = false;
		//wallForth.setDirection('S');
		
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
			dBox.text = "What?! You again?\n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			//setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "You already caused enough\n" +
							"trouble last time you interferred!";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Sorry, have we met? \n" +
							"";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Can't say I've seen a hobo\n" +
							"in a floating box.";
		}
		para++;
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Well, I recall seeing you before.\n" +
							"";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "That is, I last saw you\n" +
							"several thousand years in the past...";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "It seems this is the first time\n" +
							"you've come across me here";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "but you acted like you already\n" +
							"faced me before back then. ";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Wuuhhhh??? Are you some kind\n" +
							"of gimmicky fourth stage boss?";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Perhaps a Doctor?\n" +
							"";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Of course not, I'm just your typical astral\n" +
							"vagabond with phenominal cosmic powers.";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "And I'm afraid I can't just simply\n" +
							"allow you to proceed any further.";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":)","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "I am bound by an infernal Contract\n" +
							"to prevent intruders from entering the Monolith,";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "but there are some tiny .\n" +
							"loopholes in the document. ";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "So you'll let me in? \n" +
							"That's pretty rad of you, man!";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":)","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "I'll go ahead, then...\n" +
							"";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Nice try kid, but the Contract \n" +
							"cannot be gingerly tiptoed around.";
		}
		para++;
		
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(">:(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "It compels me to protect\n" +
							"the exterior of the Monolith. ";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":D","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "It doesn't say that I have to win,\n" +
							"but our battle must be convincing. ";
		}
		para++;
    
		// Cue start of boss fight
		
		if(dBox.paragraph == para)
		{
			mainLevel.loopMusic("sound/music/th12Nue.mid");
			
			mainLevel.reyPortrait.setSemiTransparency(0.5);
			wallForth.portrait.setSemiTransparency( 0.5);
			wallForth.aiMode = 8;
			mainLevel.barBossHealth.lagValue = 0;
			wallForth.timer = 0;
			dBox.isVisible = false;
		}
		para++;
		
		
		if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "Well, it looks like I'm all out of \n" +
							"energy to power the asteroid barrier.";
		}
		para++;
		
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":'(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "You going in then, kid?\n" +
							"";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(">:(","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "Yeah sure, but hold on...\n" +
							"What's all this about \"contracts\",";
		}
		para++;
		
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "and time-travelling?\n" +
							"";
		}
		para++;
		
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait(":'(","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "The mastermind behind the monolith\n" +
							"defeats his enemies";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "and uses ancient magical contracts \n" +
							"to recruit them for his corporate schemes.";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "So he controls you...\n" +
							"Legally-binding papers";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":D","speak");
			setCitrusPortrait(":'(","listen");
			
			dBox.textColor = DialogBoxSprite.BLUE;
			dBox.text = "recruiting his foes.\n" +
							"";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "It's a labyrinth in that Monolith.\n" +
							"Good luck, kid.";
		}
		para++;
    
    if(dBox.paragraph == para)
		{
			setPlayerPortrait(":)","listen");
			setCitrusPortrait("D':","speak");
			
			dBox.textColor = DialogBoxSprite.RED;
			dBox.text = "And try not to take a wrong \n" +
							"turn into the kitchen!";
		}
		para++;
    
		// end dialogue, end stage
		
		if(dBox.paragraph == para)
		{
			wallForth.aiMode = 10;
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
		
		wallForth.portrait.emote = emote;

		if(mode.equals("speak"))
		{
			wallForth.portrait.vars[0] = CITRUS_SPEAK_X;
			wallForth.portrait.vars[1] = CITRUS_SPEAK_Y;
			wallForth.portrait.setSemiTransparency(0.0);
		}
		if(mode.equals("listen"))
		{
			wallForth.portrait.vars[0] = CITRUS_LISTEN_X;
			wallForth.portrait.vars[1] = CITRUS_LISTEN_Y;
			wallForth.portrait.setSemiTransparency(0.5);
		}
	}
	
	
	
	private void runBulletPatterns()
	{
		for(Object o : bullets)
		{
			BulletSprite bullet = (BulletSprite) o;
			
			bullet.move();
			
			// custom movements
			if(bullet.aiMode == -900) {
				bullet.x += bullet.dx;
				bullet.y += bullet.dy;
				bullet.dx*=0.95;
				bullet.dy*=0.95;
				bullet.vars[1]++;
				if(bullet.vars[1] > 30) {
					bullet.setRotationalVelocity(7, bullet.theta + 90*bullet.vars[0], 10*bullet.vars[0],0.93);
					bullet.aiMode = -901;
				}
			}
			if(bullet.aiMode == -901) {
				bullet.x += bullet.dx*GameMath.cos(bullet.theta);
				bullet.y -= bullet.dx*GameMath.sin(bullet.theta);
				bullet.rotate(bullet.theta);
				bullet.theta += bullet.dy;
				bullet.dy*= bullet.rad;
				
				bullet.vars[1]++;
				if(bullet.vars[1] > 30 && bullet.vars[1] < 90) bullet.dx *= 0.97;
				
				bullet.screenKillable = true;
			}
			
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
		ArrayList<Sprite> cList = quadTree.query(bossPlane);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(bossPlane.collision(pBullet) && bossPlane.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(bossPlane.damage(pBullet.damage))
					{
						mainLevel.spawnItems(bossPlane.itemDrops, bossPlane.x, bossPlane.y);
						bossPlane.isActive = false;
						bossPlane.timer = 0;
						bossPlane.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						bossPlane.attackIndex++;
						break;
					}
					
				}
				
				
			}
		}
		
		cList = quadTree.query(wallForth);
		for(Sprite s : cList)
		{
			if(s instanceof PlayerBulletSprite)
			{
				PlayerBulletSprite pBullet = (PlayerBulletSprite) s;
				
				if(wallForth.collision(pBullet) && wallForth.isActive)
				{
					if(!pBullet.permanent)
						pBullet.destroy();
					
					if(wallForth.damage(pBullet.damage))
					{
						mainLevel.spawnItems(wallForth.itemDrops, wallForth.x, wallForth.y);
						wallForth.isActive = false;
						wallForth.timer = 0;
						wallForth.hp = 200;
						mainLevel.numberSCTimer.value = 60;
					//	System.out.println("skipped due to damage");
						wallForth.attackIndex++;
						break;
					}
					
				}
			}
		}
		
	}
	
    
    private void obstacle2AnyCollisions()
	{
		for(Object o : obstacles)
		{
			ObstacleSprite obs = (ObstacleSprite) o;
			ArrayList<Sprite> cList = quadTree.query(obs);
			
			for(Sprite s : cList)
			{
				if(s instanceof PlayerBulletSprite)
				{
            if(obs.collision(s)) {
                PlayerBulletSprite pBullet = (PlayerBulletSprite) s;	
                if(!pBullet.permanent)
                    pBullet.destroy();
            }
				}
        if(s instanceof BulletSprite)
				{
            if(obs.collision(s)) {
                BulletSprite pBullet = (BulletSprite) s;	
                pBullet.destroy();
            }
				}
        if(s instanceof EnemySprite) {
            if(obs.collision(s)) {
                EnemySprite en = (EnemySprite) s;
                killEnemy(en);
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
		//	splosion = FXSprite.makeBurst1(smiley.x, smiley.y);
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
		
		if(enemy.aiType == 3)
		{	
      int numShards = 4;
      
      if(difficulty == "Easy") {
        numShards = 3;
      }
      if(difficulty == "Hard") {
        numShards = 5;
      }
      if(difficulty == "Lunatic") {
        numShards = 6;
      }
      
			for(double i = 0; i < numShards; i++)
			{
				BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'm', new Color(0xaaFF00));
				bullet.setVelocity(1+mainLevel.rand.nextDouble()*3, mainLevel.rand.nextInt(360));
				bullets.add(bullet);
			}
		}
		if(enemy.aiType == 4) {
      double explSize = 2.0;
      double density = 5;
      
      if(difficulty == "Easy") {
        explSize = 1.5;
        density = 4;
      }
      if(difficulty == "Hard") {
        explSize = 2.3;
        density = 6;
      }
      if(difficulty == "Hard") {
        explSize = 2.8;
        density = 7;
      }
      
			for(double i = 0; i < density; i++)
      {
        BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'i', new Color(0xaaFF00));
        bullet.setFadingVelocity(0.5+mainLevel.rand.nextDouble()*(explSize-1), mainLevel.rand.nextInt(360), 80, 1.5);
        bullets.add(bullet);
      }
      for(double i = 0; i < density+4; i++)
      {
        BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xffaa00));
        bullet.setFadingVelocity(1.0+mainLevel.rand.nextDouble()*(explSize-0.5), mainLevel.rand.nextInt(360), 50, 3.0);
        bullets.add(bullet);
      }
      for(double i = 0; i < density+8; i++)
      {
        BulletSprite bullet = new BulletSprite(enemy.x, enemy.y, 'd', new Color(0xff0000));
        bullet.setFadingVelocity(1.5+mainLevel.rand.nextDouble()*explSize, mainLevel.rand.nextInt(360), 30, 2.0);
        bullets.add(bullet);
      }
			
			mainLevel.screenShake = 20;
			mainLevel.soundPlayer.play("sound/cannon3.WAV");
			
			enemy.destroy();
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
		
		if(bossPlane.isActive)
		{
			double dist = getSqrDist(x,y, bossPlane.x, bossPlane.y);
			if(nearestEnemy == null || dist < smallestDist && dist < mainLevel.SCREENH*mainLevel.SCREENH)
			{
				nearestEnemy = bossPlane;
				smallestDist = dist;
			}
		}
		
		if(wallForth.isActive)
		{
			double dist = getSqrDist(x,y, wallForth.x, wallForth.y);
			if(nearestEnemy == null || dist < smallestDist)
			{
				nearestEnemy = wallForth;
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
        obstacles.animate(il);
		enemies.animate(il);
		bullets.animate(il);
	//	bossBullets.animate(il);
		bossSlaves.animate(il);
		bossPlane.animate(il);
		wallForth.animate(il);
		wallForth.portrait.animate(il);
		
		textStageNum.animate(il);
		textStageTitle.animate(il);
		
	}
	
	/**
	*	void renderEnemies(Graphics g2D)
	*	This method renders all the stage's enemies. It should be called in mainLevel's render method.
	**/
	
	public void renderEnemies(Graphics2D g2D)
	{
        obstacles.render(g2D);
		bossSlaves.render(g2D);
		bossPlane.render(g2D);
      enemies.render(g2D);
		wallForth.render(g2D);
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
		g2D.setColor(new Color(0xFF8833cc, true));
		g2D.fillRect(0, 0, mainLevel.SCREENW, mainLevel.SCREENH);
		background.render(g2D);
	}
	
	/**
	*	void renderForeground(Graphics g2D)
	*	This method renders all the stage's foreground graphics. It should be called in mainLevel's render method.
	**/
	
	public void renderForeground(Graphics2D g2D)
	{
		wallForth.portrait.render(g2D);
		textStageNum.render(g2D);
		textStageTitle.render(g2D);
		g2D.setColor(new Color(0xaaaaaa));
		g2D.drawString("frame: " + timer, 10, mainLevel.SCREENH-10);
	}

}


