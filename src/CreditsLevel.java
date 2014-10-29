import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.ArrayList;

import gameEngine.*;


public class CreditsLevel extends Level
{	
	private YoBroMainPanel parentYoBro;
	
	////////////////// sprites

	// HUD sprites
	
	MenuBackgroundsSprite background;
	
	StageTextSprite textYoBroCredits;
	StageTextSprite textDesign;
	StageTextSprite textDesignCredits;
	StageTextSprite textGraphics;
	StageTextSprite textGraphicsCredits;
	StageTextSprite textProgramming;
	StageTextSprite textProgrammingCredits;
	StageTextSprite textSound;
	StageTextSprite textSoundCredits;
	StageTextSprite textMusic;
	StageTextSprite textMusicCredits;
	StageTextSprite textInspiredBy;
	StageTextSprite textInspiredByCredits;
	StageTextSprite textSpecialThanks;
	StageTextSprite textSpecialThanksCredits1;
	StageTextSprite textSpecialThanksCredits2;
	StageTextSprite textProducedBy;
	StageTextSprite textProducedByCredits;
	StageTextSprite textThankYou;
	
	SpriteList credits;
	
	////////////////// obtained from parent
	
	private Keyboard keyboard;
	private ConfigYoBro config;
	private MidiPlayer midi;
	public SoundPlayer soundPlayer;
	private double originalMidiVolume;
	
	
	// game vars
	
	private double scrollingSpeed;
	private float fadeIn;
	private long timer;

	
	//////////////// Constructor
	
	public CreditsLevel(Component parent)
	{
		super(parent);
		parentYoBro = (YoBroMainPanel) parent;
		keyboard = parentYoBro.getKeyboard();
		config = parentYoBro.getConfig();
		
		midi = parentYoBro.getMidiPlayer();
		soundPlayer = parentYoBro.soundPlayer;
		
		originalMidiVolume = config.musicVol;
		
		fadeIn = 1.0f;
		timer = 0;
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
		StageTextSprite.loadImages(imgLoader);
		
		// make initial objects
		
		credits = new SpriteList();
		
		Color color1 = new Color(0xCC22CC);
		Color color2 = new Color(0xBBBBFF);
		
		background = new MenuBackgroundsSprite(320,240,'C');
		
		textYoBroCredits = new StageTextSprite(320,500,'t');
		textYoBroCredits.color1 = color1;
		textYoBroCredits.color2 = color2;
		textYoBroCredits.fontSize = 20;
		textYoBroCredits.text = "Yo Bro: Manifestation of the Crimson Demon ~ Credits";
		credits.add(textYoBroCredits);
		
		textDesign = new StageTextSprite(320,500,'t');
		textDesign.color1 = color1;
		textDesign.color2 = color2;
		textDesign.fontSize = 20;
		textDesign.text = "Design:";
		credits.add(textDesign);
		
		textDesignCredits = new StageTextSprite(320,500,'t');
		textDesignCredits.color1 = color1;
		textDesignCredits.color2 = color2;
		textDesignCredits.fontSize = 16;
		textDesignCredits.text = "~\tStephen Lindberg";
		credits.add(textDesignCredits);
		
		
		textGraphics = new StageTextSprite(320,500,'t');
		textGraphics.color1 = color1;
		textGraphics.color2 = color2;
		textGraphics.fontSize = 20;
		textGraphics.text = "Graphics:";
		credits.add(textGraphics);
		
		textGraphicsCredits = new StageTextSprite(320,500,'t');
		textGraphicsCredits.color1 = color1;
		textGraphicsCredits.color2 = color2;
		textGraphicsCredits.fontSize = 16;
		textGraphicsCredits.text = "~\tStephen Lindberg";
		credits.add(textGraphicsCredits);
		
		
		textProgramming = new StageTextSprite(320,500,'t');
		textProgramming.color1 = color1;
		textProgramming.color2 = color2;
		textProgramming.fontSize = 20;
		textProgramming.text = "Programming:";
		credits.add(textProgramming);
		
		textProgrammingCredits = new StageTextSprite(320,500,'t');
		textProgrammingCredits.color1 = color1;
		textProgrammingCredits.color2 = color2;
		textProgrammingCredits.fontSize = 16;
		textProgrammingCredits.text = "~\tStephen Lindberg";
		credits.add(textProgrammingCredits);
		
		textSound = new StageTextSprite(320,500,'t');
		textSound.color1 = color1;
		textSound.color2 = color2;
		textSound.fontSize = 20;
		textSound.text = "Sound:";
		credits.add(textSound);
		
		textSoundCredits = new StageTextSprite(320,500,'t');
		textSoundCredits.color1 = color1;
		textSoundCredits.color2 = color2;
		textSoundCredits.fontSize = 16;
		textSoundCredits.text = "~\tClickteam sound libraries";
		credits.add(textSoundCredits);
		
		textMusic = new StageTextSprite(320,500,'t');
		textMusic.color1 = color1;
		textMusic.color2 = color2;
		textMusic.fontSize = 20;
		textMusic.text = "Music:";
		credits.add(textMusic);
		
		textMusicCredits = new StageTextSprite(320,500,'t');
		textMusicCredits.color1 = color1;
		textMusicCredits.color2 = color2;
		textMusicCredits.fontSize = 16;
		textMusicCredits.text = "~\tTouhou: Imperishible Night ~\tTouhou: Embodiment of the Scarlet Devil ~\tBomberman Hero ~\ttechnomidi.com";
		credits.add(textMusicCredits);
		
		textInspiredBy = new StageTextSprite(320,500,'t');
		textInspiredBy.color1 = color1;
		textInspiredBy.color2 = color2;
		textInspiredBy.fontSize = 20;
		textInspiredBy.text = "Inspired By:";
		credits.add(textInspiredBy);
		
		textInspiredByCredits = new StageTextSprite(320,500,'t');
		textInspiredByCredits.color1 = color1;
		textInspiredByCredits.color2 = color2;
		textInspiredByCredits.fontSize = 16;
		textInspiredByCredits.text = "~\tTouhou Project Shrine Maiden series";
		credits.add(textInspiredByCredits);
		
		
		textSpecialThanks = new StageTextSprite(320,500,'t');
		textSpecialThanks.color1 = color1;
		textSpecialThanks.color2 = color2;
		textSpecialThanks.fontSize = 20;
		textSpecialThanks.text = "Special Thanks to the following persons!";
		credits.add(textSpecialThanks);
		
		textSpecialThanksCredits1 = new StageTextSprite(320,500,'t');
		textSpecialThanksCredits1.color1 = color1;
		textSpecialThanksCredits1.color2 = color2;
		textSpecialThanksCredits1.fontSize = 16;
		textSpecialThanksCredits1.text = "~\tKeith Humberger ~" +
													"\tJames Masey ~";
		credits.add(textSpecialThanksCredits1);
		
		textSpecialThanksCredits2 = new StageTextSprite(320,500,'t');
		textSpecialThanksCredits2.color1 = color1;
		textSpecialThanksCredits2.color2 = color2;
		textSpecialThanksCredits2.fontSize = 16;
		textSpecialThanksCredits2.text = "~\tTennessee Technological University ~\t\tComputer Science Department ~";
		credits.add(textSpecialThanksCredits2);
		
		textProducedBy = new StageTextSprite(320,500,'t');
		textProducedBy.color1 = color1;
		textProducedBy.color2 = color2;
		textProducedBy.fontSize = 20;
		textProducedBy.text = "Produced by:";
		credits.add(textProducedBy);
		
		textProducedByCredits = new StageTextSprite(320,500,'t');
		textProducedByCredits.color1 = color1;
		textProducedByCredits.color2 = color2;
		textProducedByCredits.fontSize = 16;
		textProducedByCredits.text = "~\tNeonair Games";
		credits.add(textProducedByCredits);
		
		textThankYou = new StageTextSprite(320,500,'t');
		textThankYou.color1 = color1;
		textThankYou.color2 = color2;
		textThankYou.fontSize = 20;
		textThankYou.text = "Thank you very much for playing!!!";
		credits.add(textThankYou);
		
		
		////////////////// wait for initial images to load
		
		imgLoader.waitForAll();
		
		loopMusic("sound/music/life.mid");
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
		StageTextSprite.clean();
		
		keyboard = null;
		config = null;
		parentYoBro = null;

		
	}
	
	
	/**
	*	
	*	
	**/
	
	public void loopMusic(String midiPath)
	{
		midi.load(midiPath);
		midi.play(true);
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
		int wait1 = 200;
		int wait2 = 80;
		int timerSpeed = 10;
		
		
		if(keyboard.isPressed(config.VK_SKIP))
		{
			scrollingSpeed = 20;
			timer+=timerSpeed;
		}
		else
		{
			scrollingSpeed = 2;
			timer++;
		}
		
		long checkPt;

		checkPt = 200;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textYoBroCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textYoBroCredits.vars[0] = 3;
			textDesign.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textDesignCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textDesign.vars[0] = 3;
			textDesignCredits.vars[0] = 3;
			textProgramming.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textProgrammingCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textProgramming.vars[0] = 3;
			textProgrammingCredits.vars[0] = 3;
			textGraphics.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textGraphicsCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textGraphics.vars[0] = 3;
			textGraphicsCredits.vars[0] = 3;
			textSound.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textSoundCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textSound.vars[0] = 3;
			textSoundCredits.vars[0] = 3;
			textMusic.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textMusicCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textMusic.vars[0] = 3;
			textMusicCredits.vars[0] = 3;
			textInspiredBy.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textInspiredByCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textInspiredBy.vars[0] = 3;
			textInspiredByCredits.vars[0] = 3;
			textSpecialThanks.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textSpecialThanksCredits1.vars[0] = 1;
			
			timer+=3;
		}
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textSpecialThanks.vars[0] = 3;
			textSpecialThanksCredits1.vars[0] = 3;
			textSpecialThanksCredits2.vars[0] = 1;
			
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textSpecialThanksCredits2.vars[0] = 3;
			textProducedBy.vars[0] = 1;
			
			timer+=3;
		}
		
		
		checkPt += wait2;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textProducedByCredits.vars[0] = 1;
			
			timer+=3;
		}
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			textProducedBy.vars[0] = 3;
			textProducedByCredits.vars[0] = 3;
			textThankYou.vars[0] = 1;
			
			timer+=3;
		}
		
		
		
		checkPt += wait1;
		if(Math.abs(timer - checkPt) <= timerSpeed)
		{
			parentYoBro.changeCurrentLevel("TitleMenuLevel");
			return;
		}
		
		
		
		
		runCredits();
		
		animateAll();
		
	}
	
	
	//////////////////////// Helper methods
	
	private void runCredits()
	{
		for(Object o : credits)
		{
			StageTextSprite credit = (StageTextSprite) o;
			
			if(credit.vars[0] == 1)
			{
				if(credit.y > 200)
					credit.y -= scrollingSpeed;
				else
					credit.vars[0] = 2;
			}
			else if(credit.vars[0] == 3 && credit.y > -300)
			{
				credit.y -= scrollingSpeed;
			}
			
		}
	}
	
	//////////////////////// Animation methods
	
	/**
	*	animates all sprites that need to be animated during the unpaused timer loop
	*
	**/
	
	private void animateAll()
	{
		background.animate(imgLoader);
		credits.animate(imgLoader);
		
		imgLoader.waitForAll(); // wait for any dynamically loaded images to load.
	}
	
	
	
	/**
	*	render(Graphics2D g2D
	*
	*
	**/
	
	public void render(Graphics2D g2D)
	{
		background.render(g2D);
		
		credits.render(g2D);
		
		if(fadeIn > 0.05)
		{
			Color fadeInColor = new Color(1.0f,1.0f,1.0f,fadeIn);
			fadeIn *= 0.995;
			g2D.setColor(fadeInColor);
			g2D.fillRect(0,0,640,480);
		}
		
	}
	
}



