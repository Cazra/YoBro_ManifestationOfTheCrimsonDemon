import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

import gameEngine.*;
import java.util.Hashtable;
import java.net.URL;


public class GraphicsTestLevel extends Level
{	
	private YoBroMainPanel parentYoBro;
	
	private Camera camera;
	
	// sprites
	
	private MenuBackgroundsSprite menuBg, menuBg2;
	private TestMode7Sprite testMode7;
	private TestWrapSprite testWrapSprite;
	private ArrayList<EnemySprite> bulletSpriteList;
	
	
	// obtained from parent
	
	private Keyboard keyboard;
	private ConfigYoBro config;
	private MidiPlayer midi;
	private float tempo;

	public GraphicsTestLevel(Component parent)
	{
		super(parent);
		parentYoBro = (YoBroMainPanel) parent;
		keyboard = parentYoBro.getKeyboard();
		config = parentYoBro.getConfig();
		
	/*	midi = parentYoBro.getMidiPlayer();
		midi.load("lazarus.mid");
		midi.play(true);
		midi.loop(-1);
		tempo = midi.getTempo();*/
	}
	
	/**
	*	loadData()
	*	loads image and sound data for this level into memory. This method should be called before 
	*	running the running the level's timer loop and render methods for the first time. 
	**/
	
	public void loadData()
	{
		// load image data
		
		MenuBackgroundsSprite.loadImages(imgLoader);
		BulletSprite.loadImages(imgLoader);
		PlayerBulletSprite.loadImages(imgLoader);
		PlayerSprite.loadImages(imgLoader);
		EnemySprite.loadImages(imgLoader);
		TestMode7Sprite.loadImages(imgLoader);
		TestWrapSprite.loadImages(imgLoader);
		
		// make initial objects
		
		menuBg = new MenuBackgroundsSprite(320,240);
		menuBg.scale(0.5,0.5);
		menuBg.rotate(180);
		
		menuBg2 = new MenuBackgroundsSprite(100,240);
		menuBg2.scale(0.3,0.3);
		menuBg2.rotate(90);
		
	/*	testMode7 = new TestMode7Sprite(100,100,400,300);
		testMode7.cameraX = 100;
		testMode7.cameraY = 100;
		testMode7.horizonY = 100;
		testMode7.camDist = 200;
		testMode7.elevation = 200;
		testMode7.blurTechnique = 1;*/
		
		testWrapSprite = new TestWrapSprite(100,100,400,300);
		
		// bullets
		
		Random random = new Random();
		
		bulletSpriteList = new ArrayList<EnemySprite>();
		
		for(int i = 0; i < 10; i++)
		{
			for(int j = 0; j < 10; j++)
			{
				EnemySprite sprite = new EnemySprite((i+1)*32, (j+1)*32, 'h',0);
				double randomTrans = random.nextDouble();
				sprite.vars[0] = random.nextDouble()*Math.PI*2;
				sprite.setParent(parent);
				bulletSpriteList.add(sprite);
			}
		}
		 
		// camera test
		
		camera = new Camera(320,240,640,480);
		camera.scale(1.0);
		camera.rotate(0);
		
		imgLoader.waitForAll();
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
		MenuBackgroundsSprite.clean();
		BulletSprite.clean();
		PlayerBulletSprite.clean();
		PlayerSprite.clean();
		EnemySprite.clean();
		keyboard = null;
		config = null;
		camera = null;
	}
	
	/**
	*	timerLoop()
	*	This method steps through one animation frame for the level. 
	*	This method should be called by the game's timer event handler before rendering the level and after loadData is used.
	*
	**/
	
	public void timerLoop()
	{
		YoBroMainPanel parentYoBro = (YoBroMainPanel) parent;
		/*
		if(keyboard.isTyped(config.VK_SKIP))
		{
			parentYoBro.startGame();
			return;
		}
		if(keyboard.isTyped(config.VK_SHOT))
		{
			parentYoBro.soundPlayer.play("sound/1up.WAV");
		//	Sound testSound = new Sound("gameEngine/yumyumtaco.wav");
		//	testSound.play();
		}
		if(keyboard.isTyped(config.VK_BOMB))
		{
			parentYoBro.soundPlayer.play("sound/BeamDown.wav");
		//	Sound testSound = new Sound("gameEngine/soundTest2.wav");
		//	testSound.play();
		}*/
	/*	if(keyboard.isPressed(config.VK_LEFT))
		{
			testMode7.cameraX--;
		}
		if(keyboard.isPressed(config.VK_RIGHT))
		{
			testMode7.cameraX++;
		}
		if(keyboard.isPressed(config.VK_UP))
		{
			testMode7.cameraY--;
		}
		if(keyboard.isPressed(config.VK_DOWN))
		{
			testMode7.cameraY++;
		}
		if(keyboard.isPressed(config.VK_SHOT))
		{
			testMode7.elevation++;
		}
		if(keyboard.isPressed(config.VK_BOMB) && testMode7.elevation > 0)
		{
			testMode7.elevation--;
		}
		if(keyboard.isPressed(config.VK_SKIP))
		{
			testMode7.cameraAngle+= 1;
		}
		*/
		
		if(keyboard.isPressed(config.VK_LEFT))
		{
			testWrapSprite.cameraX--;
		}
		if(keyboard.isPressed(config.VK_RIGHT))
		{
			testWrapSprite.cameraX++;
		}
		if(keyboard.isPressed(config.VK_UP))
		{
			testWrapSprite.cameraY--;
		}
		if(keyboard.isPressed(config.VK_DOWN))
		{
			testWrapSprite.cameraY++;
		}
		if(keyboard.isPressed(config.VK_SHOT))
		{
			testWrapSprite.cameraZoom*=1.01;
			testWrapSprite.setSemiTransparency(0.5);
		}
		if(keyboard.isPressed(config.VK_BOMB))
		{
			testWrapSprite.cameraZoom/=1.01;
			testWrapSprite.setSemiTransparency(0.0);
		}
		if(keyboard.isPressed(config.VK_SKIP))
		{
			testWrapSprite.cameraAngle+= 1;
		}
		
		
	//	testMode7.cameraY++;
		
	
		menuBg.animate(imgLoader);
		menuBg2.animate(imgLoader);
	//	testMode7.animate(imgLoader);
		testWrapSprite.animate(imgLoader);
		
		for(EnemySprite sprite : bulletSpriteList)
		{
			if(keyboard.isPressed(config.VK_DOWN))
			{
				sprite.setDirection('S');
			}
			if(keyboard.isPressed(config.VK_LEFT))
			{
				sprite.setDirection('W');
			}
			if(keyboard.isPressed(config.VK_RIGHT))
			{
				sprite.setDirection('E');
			}
			
		//	sprite.vars[1] += 1;
		//	if(sprite.vars[1] >= 360)
		//		sprite.vars[1] = 0;
		//	sprite.rotate(sprite.vars[1]);
		//	sprite.scale(1.0, 0.2);
			sprite.animate(imgLoader);
		}
		
		imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
	}
	
	/**
	*	render(Graphics2D g2D
	*
	*
	**/
	
	public void render(Graphics2D g2D)
	{
		g2D.setTransform(camera.getTransform());
			
		// Background graphic
		
		menuBg.render(g2D);
		menuBg2.render(g2D);
		
		for(EnemySprite sprite : bulletSpriteList)
		{
			sprite.render(g2D);
		}
		
	//	testMode7.render(g2D);
		testWrapSprite.render(g2D);
		
		// text
		
		g2D.setColor(new Color(0xFFFFFF));
		
		if(menuBg.boxCollision(menuBg2))
			g2D.drawString("COLLISION: TRUE", 300, 10);
		else
			g2D.drawString("COLLISION: FALSE", 300, 10);
		
		if(parentYoBro.ticktock == 1)
			g2D.drawString("tick",50,10);
		else
			g2D.drawString("tock",50,10);
		
		g2D.drawString("Last keycode pressed: " + keyboard.lastTyped, 300,70);
		
		
		int textY = 70;
		
		if(keyboard.isPressed(config.VK_UP))
			g2D.drawString("UP: + Keycode: " + config.VK_UP, 50,textY);
		else
			g2D.drawString("UP: - Keycode: " + config.VK_UP, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_DOWN))
			g2D.drawString("DOWN: + Keycode: " + config.VK_DOWN, 50,textY);
		else
			g2D.drawString("DOWN: - Keycode: " + config.VK_DOWN, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_LEFT))
			g2D.drawString("LEFT: + Keycode: " + config.VK_LEFT, 50,textY);
		else
			g2D.drawString("LEFT: - Keycode: " + config.VK_LEFT, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_RIGHT))
			g2D.drawString("RIGHT: + Keycode: " + config.VK_RIGHT, 50,textY);
		else
			g2D.drawString("RIGHT: - Keycode: " + config.VK_RIGHT, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_SHOT))
			g2D.drawString("SHOT: + Keycode: " + config.VK_SHOT, 50,textY);
		else
			g2D.drawString("SHOT: - Keycode: " + config.VK_SHOT, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_BOMB))
			g2D.drawString("BOMB: + Keycode: " + config.VK_SHOT, 50,textY);
		else
			g2D.drawString("BOMB: - Keycode: " + config.VK_SHOT, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_SLOW))
			g2D.drawString("SLOW: + Keycode: " + config.VK_SLOW, 50,textY);
		else
			g2D.drawString("SLOW: - Keycode: " + config.VK_SLOW, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_SKIP))
			g2D.drawString("SKIP: + Keycode: " + config.VK_SKIP, 50,textY);
		
		else
			g2D.drawString("SKIP: - Keycode: " + config.VK_SKIP, 50,textY);
		textY +=20;	
		
		if(keyboard.isPressed(config.VK_PAUSE))
			g2D.drawString("PAUSE: + Keycode: " + config.VK_PAUSE, 50,textY);
		else
			g2D.drawString("PAUSE: - Keycode: " + config.VK_PAUSE, 50,textY);
		textY +=20;	
		
		g2D.drawString("Starting lives: " + config.startingLives,50, textY);
	}
	
}



class TestMode7Sprite extends Mode7Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public TestMode7Sprite(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
	}
	
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		this.isDestroyed = true;
		numInstances--;
	}
	
	/**
	*	loadImages()
	*	loads and stores image data for this Sprite.
	**/
	
	public static void loadImages(ImageLoader imgLoader)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		
		// load our images
		
		imageURL = TestMode7Sprite.class.getResource("gameEngine/mode7TestGraphic.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for TestMode7Sprite");
		
	}
	
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
	}
	
	
	// RENDERING METHODS
	
	/**
	*	animate()
	*	Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
	*	Preconditions: none.
	*	Postconditions: curImage is set to the image of this Sprite's current animation frame 
	*		and necessary values for computing the next frame of animation are prepared.
	**/
	
	public void animate(ImageLoader il)
	{
		super.animate(il);
		
		
		curImage = imageTable.get("testImg");
		Point curFocus = focusTable.get("testImg");
		
		animateSemiTransparencyFrame(il, "testImg");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		
		
	}
	
	
	/**
	*	animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	*	Applies current semi-transparency to a frame of animation and stores the modified frame for later use.
	*	Preconditions: il is the ImgLoader passed in by animate(ImageLoader il).
	*		imgKey is the normal unmodified key name for the current frame of animation.
	*	Postconditions: the current image is modified with the current semitransparency. 
	*		If this semitransparent frame isn't already in the imageTable, it is added in.
	**/
	
	protected void animateSemiTransparencyFrame(ImageLoader il, String imgKey)
	{
		double keyTag = (Math.round(this.semiTransparency*256)/256.0);
		if(imageTable.containsKey(imgKey + "semiTrans" + keyTag))
		{
			curImage = imageTable.get(imgKey + "semiTrans" + keyTag);
		}
		else
		{
			Image semicurImage = ColorFilters.setSemiTransparency(curImage, this.semiTransparency);
			imageTable.put(imgKey + "semiTrans" + keyTag, semicurImage);
			
			curImage = semicurImage;
			il.addImage(curImage);
		}
	}
	
	
}


class TestWrapSprite extends WrappingImageSprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public TestWrapSprite(double x, double y, double w, double h)
	{
		super(x, y, w, h);
		numInstances++;
	}
	
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		this.isDestroyed = true;
		numInstances--;
	}
	
	/**
	*	loadImages()
	*	loads and stores image data for this Sprite.
	**/
	
	public static void loadImages(ImageLoader imgLoader)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		
		// load our images
		
		imageURL = TestWrapSprite.class.getResource("gameEngine/mode7TestGraphic.png");
		Image bullet1Img = tk.getImage(imageURL);
	//	bullet1Img = ColorFilters.setTransparentColor(bullet1Img, new Color(0xFF00FF));
		imgLoader.addImage(bullet1Img);
		
		// setup focus points for the images
		
		focusTable.put("testImg",new Point(0,0));
		
		// store them into our hashtables
		
		imageTable.put("testImg",bullet1Img);
		
		System.out.println("loaded image data for TestMode7Sprite");
		
	}
	
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
	}
	
	
	// RENDERING METHODS
	
	/**
	*	animate()
	*	Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
	*	Preconditions: none.
	*	Postconditions: curImage is set to the image of this Sprite's current animation frame 
	*		and necessary values for computing the next frame of animation are prepared.
	**/
	
	public void animate(ImageLoader il)
	{
		super.animate(il);
		
		curImage = imageTable.get("testImg");
		Point curFocus = focusTable.get("testImg");
		
		fx = curFocus.x;
		fy = curFocus.y;
		
		if(semiTransparency > 0.0 && colorTransformChanged) // apply semi-transparency
		{
		//	this.curImage = ColorFilters.setSemiTransparency(this.curImage, this.semiTransparency);
			colorTransformChanged = false;
		}	
		
		
	}
	
	
	
	
	
}

