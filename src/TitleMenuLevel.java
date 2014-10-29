import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.util.ArrayList;
import java.util.Stack;

import gameEngine.*;


public class TitleMenuLevel extends Level
{	
	private YoBroMainPanel parentYoBro;
	
	////////////////// sprites
	
	MenuBackgroundsSprite background;
	
	MenuSprite txtTitle;
	MenuSprite txtStartGame;
	MenuSprite txtExtraStage;
	MenuSprite txtStagePractice;
	MenuSprite txtInstructions;
	MenuSprite txtOptions;
	MenuSprite txtQuit;
	
	MenuSprite txtGeneric;
	
	MenuSprite txtHeaderSelectDifficulty;
	MenuSprite iconEasy;
	MenuSprite iconNormal;
	MenuSprite iconHard;
	MenuSprite iconLunatic;
	
	MenuSprite txtHeaderSelectAttack;
	MenuSprite iconYoyo;
	MenuSprite iconBeatdown;
	MenuSprite iconFropick;
	
	MenuSprite txtHeaderOptions;
	MenuSprite txtStartingLives;
	MenuSprite txtMidiVol;
	MenuSprite txtSfxVol;
	MenuSprite txtKeyConfig;
	MenuSprite txtRestoreDefaults;
	MenuSprite txtBack1;
	
	MenuSprite txtHeaderKeyConfig;
	MenuSprite txtShootConfirm;
	MenuSprite txtBombCancel;
	MenuSprite txtSlow;
	MenuSprite txtSkip;
	MenuSprite txtPause;
	MenuSprite txtUp;
	MenuSprite txtDown;
	MenuSprite txtLeft;
	MenuSprite txtRight;
	MenuSprite txtApply;
	MenuSprite txtCancel;
	
	
	////////////////// obtained from parent
	
	private Keyboard keyboard;
	private ConfigYoBro config;
	private MidiPlayer midi;
	private SoundPlayer soundPlayer;
	
	
	// game vars
	
	String curMenu;
	int curOption;
	int prevOption; // used for skipping grayed-out options
	Stack<Integer> prevSelections;
	Stack<String> prevMenus;
	int fadeFromWhite;
	ConfigYoBro tempConfig;

	
	//////////////// Constructor
	
	public TitleMenuLevel(Component parent)
	{
		super(parent);
		parentYoBro = (YoBroMainPanel) parent;
		keyboard = parentYoBro.getKeyboard();
		config = parentYoBro.config;
		
		midi = parentYoBro.getMidiPlayer();
		soundPlayer = parentYoBro.soundPlayer;
		
		curMenu = "title";
		curOption = 0;
		prevOption = 0;
		fadeFromWhite = 255;
		
		prevSelections = new Stack<Integer>();
		prevMenus = new Stack<String>();
		
		soundPlayer.setVolume(config.soundVol);
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
		MenuSprite.loadImages(imgLoader);
		
		
		
		// make initial objects
		
		background = new MenuBackgroundsSprite(320,240);
		
		txtTitle = new MenuSprite(149,16,"title");
		txtStartGame = new MenuSprite(128,202,"startGame",true);
		txtExtraStage = new MenuSprite(130,237,"extraStage",true);
		txtExtraStage.isGrayedOut = true;
		txtStagePractice = new MenuSprite(131,269,"stagePractice",true);
		txtInstructions = new MenuSprite(133,298,"instructions",true);
		txtOptions = new MenuSprite(126,333,"options",true);
		txtQuit = new MenuSprite(121,363,"quit",true);
		
		
	//	txtGeneric = new MenuSprite(200,200,"genericText","Test String\nLine 2\n Line 3",false);
		
		txtHeaderSelectDifficulty = new MenuSprite(192,32,"headerSelectDifficulty");
		iconEasy = new MenuSprite(224,80,"easy",true);
		iconNormal = new MenuSprite(224,176,"normal",true);
		iconHard = new MenuSprite(224,272,"hard",true);
		iconLunatic = new MenuSprite(224,368,"lunatic",true);
		
		txtHeaderSelectAttack = new MenuSprite(175,31,"headerSelectAttack");
		iconYoyo = new MenuSprite(32,96,"yoyo",true);
		iconBeatdown = new MenuSprite(240,96,"beatdown",true);
		iconFropick = new MenuSprite(432,96,"fropick",true);
		
		txtHeaderOptions = new MenuSprite(224,32,"headerOptions");
		txtStartingLives = new MenuSprite(128,112,"startingLives",true);
		txtStartingLives.value = config.startingLives;
		txtMidiVol = new MenuSprite(128,144,"midiVol",true);
		txtSfxVol = new MenuSprite(128,176,"sfxVol",true);
		txtKeyConfig = new MenuSprite(128,208,"keyConfig",true);
		txtRestoreDefaults = new MenuSprite(128,240,"restoreDefaults",true);
		txtBack1 = new MenuSprite(128,272,"back",true);
		
		txtHeaderKeyConfig = new MenuSprite(208,32,"headerKeyConfig");
		txtShootConfirm = new MenuSprite(128,96,"shootConfirm","Z",true);
		txtBombCancel = new MenuSprite(128,128,"bombCancel","X",true);
		txtSlow = new MenuSprite(128,160,"slow","Shift",true);
		txtSkip = new MenuSprite(128,192,"skip","Enter",true);
		txtPause = new MenuSprite(128,224,"pause","Esc",true);
		txtUp = new MenuSprite(128,256,"up","up");
		txtDown = new MenuSprite(128,288,"down","down");
		txtLeft = new MenuSprite(128,320,"left","left");
		txtRight = new MenuSprite(128,355,"right","right");
		txtApply = new MenuSprite(128,387,"apply",true);
		txtCancel = new MenuSprite(128,416,"cancel",true);
		
		////////////////// wait for initial images to load
		
		System.out.println("waiting for ImageLoader");
		
		imgLoader.waitForAll();
		
		loopMusic("sound/th06_title.mid");
		
		System.out.println("finished loading TitleMenuLevel");
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
		MenuSprite.clean();
		
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
		System.out.println("loading music midiPath");
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
		if(curMenu.equals("title"))
		{
			if(keyboard.isTyped(config.VK_UP))
			{
				curOption--;
				if(curOption < 0)
					curOption = 5;
			}
			if(keyboard.isTyped(config.VK_DOWN))
			{
				curOption++;
				if(curOption > 5)
					curOption = 0;
			}
			if(keyboard.isTyped(config.VK_BOMB) || keyboard.isTyped(config.VK_PAUSE))
			{
					curOption = 5;
			}
			
			txtStartGame.isActive = false;
			txtStartGame.vars[0] = 128;
			txtExtraStage.isActive = false;
			txtExtraStage.vars[0] = 130;
			txtStagePractice.isActive = false;
			txtStagePractice.vars[0] = 131;
			txtInstructions.isActive = false;
			txtInstructions.vars[0] = 133;
			txtOptions.isActive = false;
			txtOptions.vars[0] = 126;
			txtQuit.isActive = false;
			txtQuit.vars[0] = 121;
			
			if(curOption == 0)
			{
				txtStartGame.isActive = true;
				txtStartGame.vars[0] = 128+16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.switchMenu("selectDifficulty");
					parentYoBro.gameMode = "Normal";
				}
			}
			if(curOption == 1)
			{
				txtExtraStage.isActive = true;
				txtExtraStage.vars[0] = 130+16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.switchMenu("selectAttack");
					parentYoBro.gameMode = "Extra";
				}
				
				
			}
			if(curOption == 2)
			{
				txtStagePractice.isActive = true;
				txtStagePractice.vars[0] = 131+16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.switchMenu("selectDifficulty");
					parentYoBro.gameMode = "Stage";
				}
			}
			if(curOption == 3)
			{
				txtInstructions.isActive = true;
				txtInstructions.vars[0] = 133+16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.switchMenu("selectAttack");
					parentYoBro.gameMode = "Tutorial";
				}
			}
			if(curOption == 4)
			{
				txtOptions.isActive = true;
				txtOptions.vars[0] = 126+16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.switchMenu("options");
				}
				
				txtStartingLives.isActive = false;
				txtMidiVol.isActive = false;
				txtSfxVol.isActive = false;
				txtKeyConfig.isActive = false;
				txtRestoreDefaults.isActive = false;
				txtBack1.isActive = false;
				
				txtStartingLives.x = 128;
				txtMidiVol.x = 128;
				txtSfxVol.x = 128;
				txtKeyConfig.x = 128;
				txtRestoreDefaults.x = 128;
				txtBack1.x = 128;
			}
			if(curOption == 5)
			{
				txtQuit.isActive = true;
				txtQuit.vars[0] = 121 + 16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					parentYoBro.endGame();
				}
			}
			
			txtStartGame.x += (txtStartGame.vars[0] - txtStartGame.x)/4.0;
			txtExtraStage.x += (txtExtraStage.vars[0] - txtExtraStage.x)/4.0;
			txtStagePractice.x += (txtStagePractice.vars[0] - txtStagePractice.x)/4.0;
			txtInstructions.x += (txtInstructions.vars[0] - txtInstructions.x)/4.0;
			txtOptions.x += (txtOptions.vars[0] - txtOptions.x)/4.0;
			txtQuit.x += (txtQuit.vars[0] - txtQuit.x)/4.0;
			
		}
		else if(curMenu.equals("selectDifficulty"))
		{
			if(keyboard.isTyped(config.VK_UP))
			{
				curOption--;
				if(curOption < -1)
					curOption = 2;
			}
			if(keyboard.isTyped(config.VK_DOWN))
			{
				curOption++;
				if(curOption > 2)
					curOption = -1;
			}
			
			iconEasy.isActive = false;
			iconEasy.vars[0] = 224;
			iconNormal.isActive = false;
			iconNormal.vars[0] = 224;
			iconHard.isActive = false;
			iconHard.vars[0] = 224;
			iconLunatic.isActive = false;
			iconLunatic.vars[0] = 224;
			
			if(curOption == -1)
			{
				iconEasy.isActive = true;
				iconEasy.vars[0] += 16;
				parentYoBro.difficulty = "Easy";
			}
			if(curOption == 0)
			{
				iconNormal.isActive = true;
				iconNormal.vars[0] += 16;
				parentYoBro.difficulty = "Normal";
			}
			if(curOption == 1)
			{
				iconHard.isActive = true;
				iconHard.vars[0] += 16;
				parentYoBro.difficulty = "Hard";
			}
			if(curOption == 2)
			{
				iconLunatic.isActive = true;
				iconLunatic.vars[0] += 16;
				parentYoBro.difficulty = "Lunatic";
			}
			
			iconEasy.x += (iconEasy.vars[0] - iconEasy.x)/4.0;
			iconNormal.x += (iconNormal.vars[0] - iconNormal.x)/4.0;
			iconHard.x += (iconHard.vars[0] - iconHard.x)/4.0;
			iconLunatic.x += (iconLunatic.vars[0] - iconLunatic.x)/4.0;
			
			if(keyboard.isTyped(config.VK_BOMB) || keyboard.isTyped(config.VK_PAUSE))
			{
				this.goBack();
			}
			if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
			{
				this.switchMenu("selectAttack");
			}
		}
		else if(curMenu.equals("selectAttack"))
		{
			if(keyboard.isTyped(config.VK_LEFT))
			{
				curOption--;
				if(curOption < 0)
					curOption = 2;
			}
			if(keyboard.isTyped(config.VK_RIGHT))
			{
				curOption++;
				if(curOption > 2)
					curOption = 0;
			}
			if(keyboard.isTyped(config.VK_BOMB) || keyboard.isTyped(config.VK_PAUSE))
			{
				this.goBack();
			}
			if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
			{
				parentYoBro.changeCurrentLevel("MainLevel");
				return;
			}
			
			iconYoyo.isActive = false;
			iconYoyo.vars[0] = 96;
			iconBeatdown.isActive = false;
			iconBeatdown.vars[0] = 96;
			iconFropick.isActive = false;
			iconFropick.vars[0] = 96;
			
			if(curOption == 0)
			{
				iconYoyo.isActive = true;
				iconYoyo.vars[0] -= 16;
				parentYoBro.attackMode = 1;
			}
			if(curOption == 1)
			{
				iconBeatdown.isActive = true;
				iconBeatdown.vars[0] -= 16;
				parentYoBro.attackMode = 2;
			}
			if(curOption == 2)
			{
				iconFropick.isActive = true;
				iconFropick.vars[0] -= 16;
				parentYoBro.attackMode = 3;
			}
			
			iconYoyo.y += (iconYoyo.vars[0] - iconYoyo.y)/4.0;
			iconBeatdown.y += (iconBeatdown.vars[0] - iconBeatdown.y)/4.0;
			iconFropick.y += (iconFropick.vars[0] - iconFropick.y)/4.0;
		}
		else if(curMenu.equals("options"))
		{
			if(keyboard.isTyped(config.VK_UP))
			{
				curOption--;
				if(curOption < 0)
					curOption = 5;
			}
			if(keyboard.isTyped(config.VK_DOWN))
			{
				curOption++;
				if(curOption > 5)
					curOption = 0;
			}
			if(keyboard.isTyped(config.VK_BOMB) || keyboard.isTyped(config.VK_PAUSE))
			{
				this.goBack();
			}
			
			txtStartingLives.isActive = false;
			txtStartingLives.vars[0] = 128;
			txtMidiVol.isActive = false;
			txtMidiVol.vars[0] = 128;
			txtSfxVol.isActive = false;
			txtSfxVol.vars[0] = 128;
			txtKeyConfig.isActive = false;
			txtKeyConfig.vars[0] = 128;
			txtRestoreDefaults.isActive = false;
			txtRestoreDefaults.vars[0] = 128;
			txtBack1.isActive = false;
			txtBack1.vars[0] = 128;

			txtStartingLives.value = config.startingLives;
			txtMidiVol.value = (int) (100*config.musicVol);
			txtSfxVol.value = (int) (100*config.soundVol);
			
			if(curOption == 0)
			{
				txtStartingLives.isActive = true;
				txtStartingLives.vars[0] -= 16;
				
				if(keyboard.isTyped(config.VK_LEFT) && config.startingLives > 0)
				{
					config.startingLives--;
				}
				if(keyboard.isTyped(config.VK_RIGHT) && config.startingLives < 5)
				{
					config.startingLives++;
				}
			}
			if(curOption == 1)
			{
				txtMidiVol.isActive = true;
				txtMidiVol.vars[0] -= 16;
				
				if(keyboard.isTyped(config.VK_LEFT))
				{
					config.musicVol -= 0.1;
				}
				if(keyboard.isTyped(config.VK_RIGHT))
				{
					config.musicVol += 0.1;
				}
				if(config.musicVol <0.0)
					config.musicVol = 0.0;
				if(config.musicVol > 1.0)
					config.musicVol = 1.0;
               
            midi.setVolume(config.musicVol);
				
			}
			if(curOption == 2)
			{
				txtSfxVol.isActive = true;
				txtSfxVol.vars[0] -= 16;
				
				boolean volChanged = false;
				
				if(keyboard.isTyped(config.VK_LEFT))
				{	
					volChanged = true;
					config.soundVol -= 0.1;
				}
				if(keyboard.isTyped(config.VK_RIGHT))
				{
					volChanged = true;
					config.soundVol += 0.1;
				}
				if(volChanged)
				{
					if(config.soundVol <0.0)
						config.soundVol = 0.0;
					if(config.soundVol > 1.0)
						config.soundVol = 1.0;
					
					soundPlayer.setVolume(config.soundVol);
					soundPlayer.play("sound/tok.wav");
				}
			}
			if(curOption == 3)
			{
				txtKeyConfig.isActive = true;
				txtKeyConfig.vars[0] -= 16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					tempConfig = new ConfigYoBro(config);
					this.switchMenu("keyConfig");
				}
			}
			if(curOption == 4)
			{
				txtRestoreDefaults.isActive = true;
				txtRestoreDefaults.vars[0] -= 16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					config.restoreDefaults();
				}
			}
			if(curOption == 5)
			{
				txtBack1.isActive = true;
				txtBack1.vars[0] -= 16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.goBack();
				}
			}
			
			
			txtStartingLives.x += (txtStartingLives.vars[0] - txtStartingLives.x)/4.0;
			txtMidiVol.x += (txtMidiVol.vars[0] - txtMidiVol.x)/4.0;
			txtSfxVol.x += (txtSfxVol.vars[0] - txtSfxVol.x)/4.0;
			txtKeyConfig.x += (txtKeyConfig.vars[0] - txtKeyConfig.x)/4.0;
			txtRestoreDefaults.x += (txtRestoreDefaults.vars[0] - txtRestoreDefaults.x)/4.0;
			txtBack1.x += (txtBack1.vars[0] - txtBack1.x)/4.0;
		}
		else if(curMenu.equals("keyConfig"))
		{
			if(keyboard.isTyped(config.VK_UP))
			{
				curOption--;
				if(curOption < 0)
					curOption = 10;
			}
			if(keyboard.isTyped(config.VK_DOWN))
			{
				curOption++;
				if(curOption > 10)
					curOption = 0;
			}
			
			int anyKey = keyboard.isAnyKeyTyped();
			if(anyKey == config.VK_UP || anyKey == config.VK_DOWN || anyKey == config.VK_LEFT || anyKey == config.VK_RIGHT)
				anyKey = -1;
			
			txtShootConfirm.isActive = false;
			txtShootConfirm.vars[0] = 128;
			txtShootConfirm.text = KeyEvent.getKeyText(tempConfig.VK_SHOT);
			txtBombCancel.isActive = false;
			txtBombCancel.vars[0] = 128;
			txtBombCancel.text = KeyEvent.getKeyText(tempConfig.VK_BOMB);
			txtSlow.isActive = false;
			txtSlow.vars[0] = 128;
			txtSlow.text = KeyEvent.getKeyText(tempConfig.VK_SLOW);
			txtSkip.isActive = false;
			txtSkip.vars[0] = 128;
			txtSkip.text = KeyEvent.getKeyText(tempConfig.VK_SKIP);
			txtPause.isActive = false;
			txtPause.vars[0] = 128;
			txtPause.text = KeyEvent.getKeyText(tempConfig.VK_PAUSE);
			txtUp.isActive = false;
			txtUp.vars[0] = 128;
			txtUp.text = KeyEvent.getKeyText(tempConfig.VK_UP);
			txtDown.isActive = false;
			txtDown.vars[0] = 128;
			txtDown.text = KeyEvent.getKeyText(tempConfig.VK_DOWN);
			txtLeft.isActive = false;
			txtLeft.vars[0] = 128;
			txtLeft.text = KeyEvent.getKeyText(tempConfig.VK_LEFT);
			txtRight.isActive = false;
			txtRight.vars[0] = 128;
			txtRight.text = KeyEvent.getKeyText(tempConfig.VK_RIGHT);
			txtApply.isActive = false;
			txtApply.vars[0] = 128;
			txtCancel.isActive = false;
			txtCancel.vars[0] = 128;
			
			if(curOption == 0)
			{
				txtShootConfirm.isActive = true;
				txtShootConfirm.vars[0] += 16;
				
				if(anyKey != -1 && !tempConfig.isVKTaken(anyKey))
					tempConfig.VK_SHOT = anyKey;	
			}
			if(curOption == 1)
			{
				txtBombCancel.isActive = true;
				txtBombCancel.vars[0] += 16;
				
				if(anyKey != -1)
					tempConfig.VK_BOMB = anyKey;	
			}
			if(curOption == 2)
			{
				txtSlow.isActive = true;
				txtSlow.vars[0] += 16;
				
				if(anyKey != -1)
					tempConfig.VK_SLOW = anyKey;	
			}
			if(curOption == 3)
			{
				txtSkip.isActive = true;
				txtSkip.vars[0] += 16;
				
				if(anyKey != -1)
					tempConfig.VK_SKIP = anyKey;	
			}
			if(curOption == 4)
			{
				txtPause.isActive = true;
				txtPause.vars[0] += 16;
				
				if(anyKey != -1)
					tempConfig.VK_PAUSE = anyKey;	
			}
			if(curOption == 5)
			{
				txtUp.isActive = true;
				txtUp.vars[0] += 16;
			}
			if(curOption == 6)
			{
				txtDown.isActive = true;
				txtDown.vars[0] += 16;
			}
			if(curOption == 7)
			{
				txtLeft.isActive = true;
				txtLeft.vars[0] += 16;
			}
			if(curOption == 8)
			{
				txtRight.isActive = true;
				txtRight.vars[0] += 16;
			}
			if(curOption == 9)
			{
				txtApply.isActive = true;
				txtApply.vars[0] += 16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					if(!tempConfig.hasKeyConflicts())
					{
						config = tempConfig;
						parentYoBro.config = config;
						this.goBack();
					}
				}
			}
			if(curOption == 10)
			{
				txtCancel.isActive = true;
				txtCancel.vars[0] += 16;
				
				if(keyboard.isTyped(config.VK_SHOT) || keyboard.isTyped(config.VK_SKIP))
				{
					this.goBack();
				}
			}
			
			
		}
		else
		{
			curMenu = "title";
			curOption = 0;
			prevOption = 0;
		}
		
		
		animateAll();
	}
	
	
	//////////////////////// Helper methods
	
	private void switchMenu(String menuName)
	{
		prevMenus.push(curMenu);
		prevSelections.push(curOption);
		
		curMenu = menuName;
		curOption = 0;
		prevOption = 0;
	}
	
	private void goBack()
	{
		curMenu = prevMenus.pop();
		if(curMenu == null)
		{
			curMenu = "title";
			return;
		}
		
		curOption = prevSelections.pop();
		prevOption = curOption;
	}
	
	//////////////////////// Animation methods
	
	/**
	*	animates all sprites that need to be animated during the unpaused timer loop
	*
	**/

	private void animateAll()
	{
		
		
		if(curMenu.equals("title"))
		{
			background.type = '1';
			txtTitle.animate(imgLoader);
			txtStartGame.animate(imgLoader);
			txtExtraStage.animate(imgLoader);
			txtStagePractice.animate(imgLoader);
			txtInstructions.animate(imgLoader);
			txtOptions.animate(imgLoader);
			txtQuit.animate(imgLoader);
		}
		else if(curMenu.equals("selectDifficulty"))
		{
			background.type = '2';
			txtHeaderSelectDifficulty.animate(imgLoader);
			iconEasy.animate(imgLoader);
			iconNormal.animate(imgLoader);
			iconHard.animate(imgLoader);
			iconLunatic.animate(imgLoader);
		}
		else if(curMenu.equals("selectAttack"))
		{
			background.type = '2';
			
			txtHeaderSelectAttack.animate(imgLoader);
			iconYoyo.animate(imgLoader);
			iconBeatdown.animate(imgLoader);
			iconFropick.animate(imgLoader);
		}
		else if(curMenu.equals("options"))
		{
			background.type = '2';
			
			txtHeaderOptions.animate(imgLoader);
			txtStartingLives.animate(imgLoader);
			txtMidiVol.animate(imgLoader);
			txtSfxVol.animate(imgLoader);
			txtKeyConfig.animate(imgLoader);
			txtRestoreDefaults.animate(imgLoader);
			txtBack1.animate(imgLoader);
		}
		else if(curMenu.equals("keyConfig"))
		{
			txtHeaderKeyConfig.animate(imgLoader);
			txtShootConfirm.animate(imgLoader);
			txtBombCancel.animate(imgLoader);
			txtSlow.animate(imgLoader);
			txtSkip.animate(imgLoader);
			txtPause.animate(imgLoader);
			txtUp.animate(imgLoader);
			txtDown.animate(imgLoader);
			txtLeft.animate(imgLoader);
			txtRight.animate(imgLoader);
			txtApply.animate(imgLoader);
			txtCancel.animate(imgLoader);
		}
		
		background.animate(imgLoader);
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
		
		if(curMenu.equals("title"))
		{
			txtTitle.render(g2D);
			txtStartGame.render(g2D);
			txtExtraStage.render(g2D);
			txtStagePractice.render(g2D);
			txtInstructions.render(g2D);
			txtOptions.render(g2D);
			txtQuit.render(g2D);
			
		//	txtGeneric.render(g2D);
		}
		else if(curMenu.equals("selectDifficulty"))
		{
			txtHeaderSelectDifficulty.render(g2D);
			iconEasy.render(g2D);
			iconNormal.render(g2D);
			iconHard.render(g2D);
			iconLunatic.render(g2D);
		}
		else if(curMenu == "selectAttack")
		{
			txtHeaderSelectAttack.render(g2D);
			iconYoyo.render(g2D);
			iconBeatdown.render(g2D);
			iconFropick.render(g2D);
		}
		else if(curMenu == "options")
		{
			txtHeaderOptions.render(g2D);
			txtStartingLives.render(g2D);
			txtMidiVol.render(g2D);
			txtSfxVol.render(g2D);
			txtKeyConfig.render(g2D);
			txtRestoreDefaults.render(g2D);
			txtBack1.render(g2D);
		}
		else if(curMenu.equals("keyConfig"))
		{
			txtHeaderKeyConfig.render(g2D);
			txtShootConfirm.render(g2D);
			txtBombCancel.render(g2D);
			txtSlow.render(g2D);
			txtSkip.render(g2D);
			txtPause.render(g2D);
			txtUp.render(g2D);
			txtDown.render(g2D);
			txtLeft.render(g2D);
			txtRight.render(g2D);
			txtApply.render(g2D);
			txtCancel.render(g2D);
		}
		
		g2D.setColor(new Color(255,255,255,fadeFromWhite));
		g2D.fillRect(0,0,640,480);
		fadeFromWhite -= 10;
		if(fadeFromWhite < 0)
			fadeFromWhite = 0;
	}
	

	
}



