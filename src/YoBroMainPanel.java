import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import javax.swing.*;
import java.awt.image.*;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import java.util.ArrayList;
import java.util.Random;

import gameEngine.*;


public class YoBroMainPanel extends JPanel
{
	// essential members
	public Frame parentFrame;
	private Timer timer;
	public double prevFPS;
	
	public Keyboard keyboard;
	public ConfigYoBro config;
	public MidiPlayer midiPlayer; 
	public SoundPlayer soundPlayer;
	
	public String gameMode = "Normal"; // "Normal","Extra","Stage","Tutorial","Spell"
	public String difficulty = "Normal"; // "Easy", "Normal", "Hard", "Lunatic"
	public int attackMode = 1; // 1 = homing yoyo, 2 = illusion beatdown, 3 = persuasion fropick
	public int selectedStage = 1; // global for stage practice.
	public String selectedSpellCard = ""; // global for spell card practice identifies which spell card to load
	
	private boolean changingLevel;
	private Level currentLevel;
	
	// Keyboard codes
	
	int VK_UP;
	int VK_DOWN;
	int VK_LEFT;
	int VK_RIGHT;
	int VK_SHOT;
	int VK_BOMB;
	int VK_SLOW;
	int VK_SKIP;
	int VK_PAUSE;
	
	// non-essential/temporary members
	
	public int ticktock;
	boolean timerReady;
	ImageLoader imgLoader;
	
	Camera camera;
	MenuBackgroundsSprite menuBg, menuBg2;
	ArrayList<BasicBulletSprite> bulletSpriteList;
	
	
		
	
	public YoBroMainPanel(Frame parent)
	{
		super(true);
		parentFrame = parent;
		this.setBackground(Color.black);
	
		this.setFocusable(true);
		
		// set up keyboard input
		
		keyboard = new Keyboard();
		this.addKeyListener( keyboard);
		
		// set up config file
		
		config = new ConfigYoBro();
	
		// load images/sounds/etc.
		Toolkit.getDefaultToolkit().sync();
		midiPlayer = new MidiPlayer();
		soundPlayer = new SoundPlayer();
		GameMath.setUpTrigTables();
		
		// set up the timer
		
		TimerListener timerListener = new TimerListener();
		
		prevFPS = 0;
		timerReady = true;
		timer = new Timer(0,timerListener);
		this.setFPS(60);
	//	timer.start();
	
		startGame();
		
		
	}
	
	/**
	*	startGame()
	*	Initializes any global game variables and loads the first level. 
	**/
	
	public void startGame()
	{
		changeCurrentLevel("TitleMenuLevel");
		//changeCurrentLevel("GraphicsTest");
		//changeCurrentLevel("TextMaker");
	}
	
	/**
	*	changeCurrentLevel(String levelName)
	*	Unloads current level and loads a new level.
	*	Preconditions: levelName is the key String for the level we are loading.
	*	Postconditions: The current level is unloaded. If levelName is a valid key String, 
	*		then its level is loaded. Otherwise the game crashes spectacularly.
	**/
	
	public void changeCurrentLevel(String levelName)
	{
		timer.stop();
		
		if(currentLevel != null)
			currentLevel.clean();
		
		System.gc();
		
		changingLevel = true;
		
		if(levelName.equals("GraphicsTest"))
			currentLevel = new GraphicsTestLevel(this);
		else if(levelName.equals("MainLevel"))
			currentLevel = new MainLevel(this);
		else if(levelName.equals("TitleMenuLevel"))
			currentLevel = new TitleMenuLevel(this);
		else if(levelName.equals("CreditsLevel"))
			currentLevel = new CreditsLevel(this);
		else if(levelName.equals("TextMaker"))
			currentLevel = new StageTextMakerLevel(this);
		else
		{
			System.out.println("Change Level Failed. Bad level name.");
			return;
		}
		
		currentLevel.loadData();
		
		timer.start();
	}
	
	
	
	/**
	*	endGame()
	*	exits the game and closes its window.
	**/
	
	public void endGame()
	{
		timer.stop();
		changingLevel = true;
		
		if(currentLevel != null)
			currentLevel.clean();
		
		config.saveConfig();
	//	parentFrame.setVisible(false);
	//	parentFrame.dispose();
		System.exit(0);
	}
	
	
	
	/** 
	*	setFPS()
	*	Preconditions: fps is a quantity of frames per second
	*	Postconditions: Sets the timer's refresh rate so that it fires fps times per second.
	**/
	
	public void setFPS(int fps)
	{
		int mspf = (int) (1000.0 /fps + 0.5);
		timer.setDelay(mspf);
	}
	
	
	/**
	*	getKeyboard()
	*	Allows easy access for the game's levels to access the global keyboard.
	**/
	
	public Keyboard getKeyboard()
	{
		return keyboard;
	}
	
	/**
	*	getConfig()
	*	Allows easy access for the game's levels to access the global configurations.
	**/
	
	public ConfigYoBro getConfig()
	{
		return config;
	}
	
	/**
	*	getMidiPlayer()
	*	Allows easy access for the game's levels to access the global midi player.
	**/
	
	public MidiPlayer getMidiPlayer()
	{
		return midiPlayer;
	}
	
	
	// Event listener for the timer objects
	
	private class TimerListener implements ActionListener
	{
		long startTime = System.currentTimeMillis();
		long lastTime = this.startTime;
		int ticks = 0;
			
		public void actionPerformed(ActionEvent e)
		{
			try {
		
				Object source = e.getSource();
				if(source == timer)
				{
					// perform a loop through the game's logic
					synchronized(this)
					{
						if(timerReady)
						{
							timerReady = false;
							if(midiPlayer == null)
								System.out.println("MidiPlayer is null");
							currentLevel.timerLoop();//timerLoop();
							repaint();
							timerReady = true;
						}
						
					}
					
				//	midiPlayer.setVolume(config.musicVol);
					
					// Logic for Frames per Second counter
					
					this.ticks++;
					
					long currentTime = System.currentTimeMillis();
					
					if(currentTime - startTime >= 500) 
					{
						prevFPS =  1000.0 * ticks/(1.0*currentTime - startTime);
						System.out.println(prevFPS);
						startTime = currentTime;
						ticks = 0;
						ticktock = 1- ticktock; // boolean-like tick-tock every time we check fps
					}
					
					lastTime = currentTime;
				}
			}
			catch(Throwable error) {
				try {
					String filename = "YoBroCrashLog" + System.currentTimeMillis() + ".txt";
					FileWriter fw = new FileWriter(filename);
					PrintWriter pw = new PrintWriter(fw,true);
					error.printStackTrace(pw);
					pw.close();
					fw.close();
				}
				catch (Exception ex) {}
			}
		}
	}
	
	// The meaty bits of the Game Engine code
	
	
	/*
	public void timerLoop()
	{
	//	camera.scale(camera.getScale()*0.999);
	//	camera.rotate(camera.getRotate()+1);
		
		menuBg.animate(imageLoader);
		menuBg2.animate(imageLoader);
		
		for(BasicBulletSprite sprite : bulletSpriteList)
		{
			sprite.animate(imageLoader);
		}
		
		imageLoader.waitForAll(); // wait for any dynamically loaded images to load.
		this.repaint();
	}
	*/
	
	public void paintComponent(Graphics g)
	{
			super.paintComponent(g);
			
			Graphics2D g2D = (Graphics2D) g;
			g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
			g2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
			g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
			g2D.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
			g2D.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
			g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
			
			if(currentLevel != null && !changingLevel)
				currentLevel.render(g2D);
			
			changingLevel = false;
			
			g2D.setTransform(new AffineTransform());
			double roundedFPS = Math.round(prevFPS*10)/10.0;
			
			g2D.setColor(new Color(0xFFFFFF));
			g2D.drawString("FPS: " + roundedFPS, 570,470);
			g2D.drawString("Sounds playing: " + soundPlayer.soundsPlaying(), 400,470);
			g2D.drawString("Bullet count: " + BulletSprite.getCount(), 200,470);
			g.dispose();
			
			
			
	}
	
}
