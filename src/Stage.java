import java.awt.*;
import java.util.Random;
import gameEngine.*;

public abstract class Stage
{
	// essential members
	protected MainLevel mainLevel;
	protected long timer;
	protected boolean timerPaused;
	protected String difficulty;
	protected Random rand;
	protected Keyboard keyboard;
	protected ConfigYoBro config;
	
	public boolean endStage;
	
	// Sprite lists
	
    public SpriteList obstacles;
	public SpriteList enemies;
	public SpriteList bullets;
	public QuadTreeNode quadTree;
	
	//
	
	
	/*
	*	Constructor
	*	Preconditions: ml is a reference to the game's main level interface which contains this stage.
	*	Postconditions: This stage is initialized and loads its data.
	*/
	
	public Stage(MainLevel ml, YoBroMainPanel mp)
	{
		mainLevel = ml;
		difficulty = mp.difficulty;
		keyboard = mp.keyboard;
		config = mp.config;
		rand = ml.rand;
		timer = -1;
		timerPaused = false;
		
		enemies = new SpriteList();
		bullets = new SpriteList();
		
		endStage = false;
		
		loadData();
	}
	
	
	
	/**
	*	void loadData()
	*	This method should load any additional sprite images or sounds/music/things that this particular stage uses.
	*	It should also initialize any initial sprites or level variables.
	*/
	
	protected abstract void loadData();
	
	/**
	*	void clean()
	*	This method should free any resources that this stage uses.
	*/
	
	protected void clean()
	{
		keyboard = null;
		mainLevel = null;
		rand = null;
		config = null;
		quadTree = null;
		
		enemies.destroyAll();
		bullets.destroyAll();
	}
	
	/**
	*	void stageTimerLoop()
	*	This method increments the stage's timer and runs the stage's logic based on the timer's current value.
	*	Use the reference to mainLevel to have elements of the stage interact with the player, 
	*	player bullets, and the global main level variables.
	**/
	
	public void stageTimerLoop()
	{
		timer++;
		quadTree = mainLevel.quadTree;
	}
	
	/**
	*	Sprite getNearestEnemy(double x, double y)
	*	Returns the enemy closest to (x,y). Returns null if there are no enemies.
	*	This is used by the Homing Yoyo attack.
	**/
	
	public abstract Sprite getNearestEnemy(double x, double y);
	
	
	
	/**
	*	ItemSprite makeItem(String items)
	*	produces a random ItemSprite from the types give in items.
	**/
	
	public ItemSprite makeItem(String items)
	{
		int randItem = rand.nextInt(items.length());
		char itemType = items.charAt(randItem);
		
		return new ItemSprite(0,0, itemType);
	}
	
	/**
	*	void animate(ImageLoader il)
	*	This method should call the animate method for all of its sprites. It should be called in mainLevel's animateAllUnpaused() method.
	*	Preconditions: il is the mainLevel's ImageLoader.
	**/
	
	public abstract void animate(ImageLoader il);
	
	/**
	*	void renderEnemies(Graphics g2D)
	*	This method renders all the stage's enemies. It should be called in mainLevel's render method.
	**/
	
	public abstract void renderEnemies(Graphics2D g2D);
	
	/**
	*	void renderBullets(Graphics g2D)
	*	This method renders all the stage's enemy bullets. It should be called in mainLevel's render method.
	**/
	
	public abstract void renderBullets(Graphics2D g2D);
	
	/**
	*	void renderBackground(Graphics g2D)
	*	This method renders all the stage's background graphics. It should be called in mainLevel's render method.
	**/
	
	public abstract void renderBackground(Graphics2D g2D);

	/**
	*	void renderForeground(Graphics g2D)
	*	This method renders all the stage's foreground graphics. It should be called in mainLevel's render method.
	**/
	
	public abstract void renderForeground(Graphics2D g2D);


	

}


