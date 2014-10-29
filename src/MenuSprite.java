
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class MenuSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	private String type;
	public int value;
	public String text;
	public boolean isActive;
	public boolean isGrayedOut;
	public boolean isSwitchable;
	
	// CONSTRUCTOR
	
	public MenuSprite(double x, double y, String t, boolean switchable)
	{
		this(x,y,t,"",switchable);
	}
	
	public MenuSprite(double x, double y, String t, String txt, boolean switchable)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		isActive = false;
		isGrayedOut = false;
		isSwitchable = switchable;
		value = 0;
		text = txt;
	}
	
	public MenuSprite(double x, double y, String t)
	{
		this(x,y,t,false);
	}
	
	public MenuSprite(double x, double y, String t, String txt)
	{
		this(x,y,t,txt,false);
	}
	
	// MEM MANAGEMENT METHODS
	
	/**
	*	destroy()
	*	decrements number of instances of this class and marks it as destroyed.
	**/
	
	public void destroy()
	{
		super.destroy();
		numInstances--;
	}
	
	/**
	*	loadImages()
	*	loads and stores image data for this Sprite.
	**/
	
	public static void loadImages(ImageLoader il)
	{
		// get our default toolkit
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		URL imageURL;
		Image img;

		imageURL = MenuSprite.class.getResource("graphics/TitleMenuSheet.png");
		Image menuSheet = tk.getImage(imageURL);
		menuSheet = ColorFilters.setTransparentColor(menuSheet, new Color(0xFF00FF));
		
		// title and menu headers
		
		img = ImageBlitter.crop(menuSheet,1,131,266,150);
		il.addImage(img);
		focusTable.put("title",new Point(130,0));
		imageTable.put("title",img);
		
		img = ImageBlitter.crop(menuSheet,1,283,280,40);
		il.addImage(img);
		focusTable.put("headerSelectDifficulty",new Point(0,0));
		imageTable.put("headerSelectDifficulty",img);
		
		img = ImageBlitter.crop(menuSheet,1,325,297,29);
		il.addImage(img);
		focusTable.put("headerSelectAttack",new Point(130,0));
		imageTable.put("headerSelectAttack",img);
		
		img = ImageBlitter.crop(menuSheet,269,131,181,52);
		il.addImage(img);
		focusTable.put("headerOptions",new Point(0,0));
		imageTable.put("headerOptions",img);
		
		img = ImageBlitter.crop(menuSheet,452,143,196,40);
		il.addImage(img);
		focusTable.put("headerKeyConfig",new Point(0,0));
		imageTable.put("headerKeyConfig",img);
		
		// main menu options
		
		img = ImageBlitter.crop(menuSheet,1,1,129,21);
		il.addImage(img);
		focusTable.put("startGameOn",new Point(64,10));
		imageTable.put("startGameOn",img);
		
		img = ImageBlitter.crop(menuSheet,132,1,129,21);
		il.addImage(img);
		focusTable.put("startGameOff",new Point(64,10));
		imageTable.put("startGameOff",img);
		
		img = ImageBlitter.crop(menuSheet,1,24,133,26);
		il.addImage(img);
		focusTable.put("extraStageOn",new Point(66,13));
		imageTable.put("extraStageOn",img);
		
		img = ImageBlitter.crop(menuSheet,136,24,133,26);
		il.addImage(img);
		focusTable.put("extraStageOff",new Point(66,13));
		imageTable.put("extraStageOff",img);
		
		img = ImageBlitter.crop(menuSheet,271,24,133,26);
		il.addImage(img);
		focusTable.put("extraStageGray",new Point(66,13));
		imageTable.put("extraStageGray",img);
		
		img = ImageBlitter.crop(menuSheet,1,52,166,26);
		il.addImage(img);
		focusTable.put("stagePracticeOn",new Point(83,13));
		imageTable.put("stagePracticeOn",img);
		
		img = ImageBlitter.crop(menuSheet,169,52,166,26);
		il.addImage(img);
		focusTable.put("stagePracticeOff",new Point(83,13));
		imageTable.put("stagePracticeOff",img);
		
		img = ImageBlitter.crop(menuSheet,1,80,138,21);
		il.addImage(img);
		focusTable.put("instructionsOn",new Point(69,10));
		imageTable.put("instructionsOn",img);
		
		img = ImageBlitter.crop(menuSheet,141,80,138,21);
		il.addImage(img);
		focusTable.put("instructionsOff",new Point(69,10));
		imageTable.put("instructionsOff",img);
		
		img = ImageBlitter.crop(menuSheet,1,103,92,26);
		il.addImage(img);
		focusTable.put("optionsOn",new Point(46,13));
		imageTable.put("optionsOn",img);
		
		img = ImageBlitter.crop(menuSheet,95,103,92,26);
		il.addImage(img);
		focusTable.put("optionsOff",new Point(46,13));
		imageTable.put("optionsOff",img);
		
		img = ImageBlitter.crop(menuSheet,189,103,50,22);
		il.addImage(img);
		focusTable.put("quitOn",new Point(25,11));
		imageTable.put("quitOn",img);
		
		img = ImageBlitter.crop(menuSheet,241,103,50,22);
		il.addImage(img);
		focusTable.put("quitOff",new Point(25,11));
		imageTable.put("quitOff",img);
		
		// difficulty menu options
		
		img = ImageBlitter.crop(menuSheet,0,355,250,81);
		il.addImage(img);
		focusTable.put("easyOn",new Point(0,0));
		imageTable.put("easyOn",img);
		
		img = ImageBlitter.crop(menuSheet,0,436,250,81);
		il.addImage(img);
		focusTable.put("easyOff",new Point(0,0));
		imageTable.put("easyOff",img);
		
		img = ImageBlitter.crop(menuSheet,0,517,250,81);
		il.addImage(img);
		focusTable.put("normalOn",new Point(0,0));
		imageTable.put("normalOn",img);
		
		img = ImageBlitter.crop(menuSheet,0,598,250,81);
		il.addImage(img);
		focusTable.put("normalOff",new Point(0,0));
		imageTable.put("normalOff",img);
		
		img = ImageBlitter.crop(menuSheet,0,679,250,81);
		il.addImage(img);
		focusTable.put("hardOn",new Point(0,0));
		imageTable.put("hardOn",img);
		
		img = ImageBlitter.crop(menuSheet,0,760,250,81);
		il.addImage(img);
		focusTable.put("hardOff",new Point(0,0));
		imageTable.put("hardOff",img);
		
		img = ImageBlitter.crop(menuSheet,0,841,250,81);
		il.addImage(img);
		focusTable.put("lunaticOn",new Point(0,0));
		imageTable.put("lunaticOn",img);
		
		img = ImageBlitter.crop(menuSheet,0,922,250,81);
		il.addImage(img);
		focusTable.put("lunaticOff",new Point(0,0));
		imageTable.put("lunaticOff",img);
		
		// select attack options
		
		img = ImageBlitter.crop(menuSheet,250,355,180,300);
		il.addImage(img);
		focusTable.put("yoyoOn",new Point(0,0));
		imageTable.put("yoyoOn",img);
		
		img = ImageBlitter.crop(menuSheet,250,655,180,300);
		il.addImage(img);
		focusTable.put("yoyoOff",new Point(0,0));
		imageTable.put("yoyoOff",img);
		
		img = ImageBlitter.crop(menuSheet,430,355,180,300);
		il.addImage(img);
		focusTable.put("beatdownOn",new Point(0,0));
		imageTable.put("beatdownOn",img);
		
		img = ImageBlitter.crop(menuSheet,430,655,180,300);
		il.addImage(img);
		focusTable.put("beatdownOff",new Point(0,0));
		imageTable.put("beatdownOff",img);
		
		img = ImageBlitter.crop(menuSheet,610,355,180,300);
		il.addImage(img);
		focusTable.put("fropickOn",new Point(0,0));
		imageTable.put("fropickOn",img);
		
		img = ImageBlitter.crop(menuSheet,610,655,180,300);
		il.addImage(img);
		focusTable.put("fropickOff",new Point(0,0));
		imageTable.put("fropickOff",img);
		
		// options options
		
		img = ImageBlitter.crop(menuSheet,269,185,159,26);
		il.addImage(img);
		focusTable.put("startingLivesOn",new Point(0,0));
		imageTable.put("startingLivesOn",img);
		
		img = ImageBlitter.crop(menuSheet,430,185,159,26);
		il.addImage(img);
		focusTable.put("startingLivesOff",new Point(0,0));
		imageTable.put("startingLivesOff",img);
		
		img = ImageBlitter.crop(menuSheet,269,213,141,21);
		il.addImage(img);
		focusTable.put("midiVolOn",new Point(0,0));
		imageTable.put("midiVolOn",img);
		
		img = ImageBlitter.crop(menuSheet,412,213,141,21);
		il.addImage(img);
		focusTable.put("midiVolOff",new Point(0,0));
		imageTable.put("midiVolOff",img);
		
		img = ImageBlitter.crop(menuSheet,269,236,141,21);
		il.addImage(img);
		focusTable.put("sfxVolOn",new Point(0,0));
		imageTable.put("sfxVolOn",img);
		
		img = ImageBlitter.crop(menuSheet,412,236,141,21);
		il.addImage(img);
		focusTable.put("sfxVolOff",new Point(0,0));
		imageTable.put("sfxVolOff",img);
		
		img = ImageBlitter.crop(menuSheet,283,259,127,26);
		il.addImage(img);
		focusTable.put("keyConfigOn",new Point(0,0));
		imageTable.put("keyConfigOn",img);
		
		img = ImageBlitter.crop(menuSheet,412,259,127,26);
		il.addImage(img);
		focusTable.put("keyConfigOff",new Point(0,0));
		imageTable.put("keyConfigOff",img);
		
		img = ImageBlitter.crop(menuSheet,283,287,189,21);
		il.addImage(img);
		focusTable.put("restoreDefaultsOn",new Point(0,0));
		imageTable.put("restoreDefaultsOn",img);
		
		img = ImageBlitter.crop(menuSheet,474,287,189,21);
		il.addImage(img);
		focusTable.put("restoreDefaultsOff",new Point(0,0));
		imageTable.put("restoreDefaultsOff",img);
		
		img = ImageBlitter.crop(menuSheet,300,310,57,21);
		il.addImage(img);
		focusTable.put("backOn",new Point(0,0));
		imageTable.put("backOn",img);
		
		img = ImageBlitter.crop(menuSheet,359,310,57,21);
		il.addImage(img);
		focusTable.put("backOff",new Point(0,0));
		imageTable.put("backOff",img);
		
		img = ImageBlitter.crop(menuSheet,418,310,14,21);
		il.addImage(img);
		focusTable.put("0Off",new Point(0,0));
		imageTable.put("0Off",img);
		
		img = ImageBlitter.crop(menuSheet,433,310,11,21);
		il.addImage(img);
		focusTable.put("1Off",new Point(0,0));
		imageTable.put("1Off",img);
		
		img = ImageBlitter.crop(menuSheet,445,310,15,21);
		il.addImage(img);
		focusTable.put("2Off",new Point(0,0));
		imageTable.put("2Off",img);
		
		img = ImageBlitter.crop(menuSheet,461,310,14,21);
		il.addImage(img);
		focusTable.put("3Off",new Point(0,0));
		imageTable.put("3Off",img);
		
		img = ImageBlitter.crop(menuSheet,476,310,16,21);
		il.addImage(img);
		focusTable.put("4Off",new Point(0,0));
		imageTable.put("4Off",img);
		
		img = ImageBlitter.crop(menuSheet,493,310,15,21);
		il.addImage(img);
		focusTable.put("5Off",new Point(0,0));
		imageTable.put("5Off",img);
		
		
		img = ImageBlitter.crop(menuSheet,510,310,14,21);
		il.addImage(img);
		focusTable.put("0On",new Point(0,0));
		imageTable.put("0On",img);
		
		img = ImageBlitter.crop(menuSheet,525,310,11,21);
		il.addImage(img);
		focusTable.put("1On",new Point(0,0));
		imageTable.put("1On",img);
		
		img = ImageBlitter.crop(menuSheet,537,310,15,21);
		il.addImage(img);
		focusTable.put("2On",new Point(0,0));
		imageTable.put("2On",img);
		
		img = ImageBlitter.crop(menuSheet,553,310,14,21);
		il.addImage(img);
		focusTable.put("3On",new Point(0,0));
		imageTable.put("3On",img);
		
		img = ImageBlitter.crop(menuSheet,568,310,16,21);
		il.addImage(img);
		focusTable.put("4On",new Point(0,0));
		imageTable.put("4On",img);
		
		img = ImageBlitter.crop(menuSheet,585,310,15,21);
		il.addImage(img);
		focusTable.put("5On",new Point(0,0));
		imageTable.put("5On",img);
		
		// key config options
		
		img = ImageBlitter.crop(menuSheet,406,1,184,21);
		il.addImage(img);
		focusTable.put("shootConfirmOn",new Point(0,0));
		imageTable.put("shootConfirmOn",img);
		
		img = ImageBlitter.crop(menuSheet,592,1,184,21);
		il.addImage(img);
		focusTable.put("shootConfirmOff",new Point(0,0));
		imageTable.put("shootConfirmOff",img);
		
		img = ImageBlitter.crop(menuSheet,406,24,167,21);
		il.addImage(img);
		focusTable.put("bombCancelOn",new Point(0,0));
		imageTable.put("bombCancelOn",img);
		
		img = ImageBlitter.crop(menuSheet,592,24,167,21);
		il.addImage(img);
		focusTable.put("bombCancelOff",new Point(0,0));
		imageTable.put("bombCancelOff",img);
		
		img = ImageBlitter.crop(menuSheet,406,47,59,21);
		il.addImage(img);
		focusTable.put("slowOn",new Point(0,0));
		imageTable.put("slowOn",img);
		
		img = ImageBlitter.crop(menuSheet,592,47,59,21);
		il.addImage(img);
		focusTable.put("slowOff",new Point(0,0));
		imageTable.put("slowOff",img);
		
		img = ImageBlitter.crop(menuSheet,406,70,162,26);
		il.addImage(img);
		focusTable.put("skipOn",new Point(0,0));
		imageTable.put("skipOn",img);
		
		img = ImageBlitter.crop(menuSheet,592,70,162,26);
		il.addImage(img);
		focusTable.put("skipOff",new Point(0,0));
		imageTable.put("skipOff",img);
		
		img = ImageBlitter.crop(menuSheet,467,47,72,21);
		il.addImage(img);
		focusTable.put("pauseOn",new Point(0,0));
		imageTable.put("pauseOn",img);
		
		img = ImageBlitter.crop(menuSheet,653,47,72,21);
		il.addImage(img);
		focusTable.put("pauseOff",new Point(0,0));
		imageTable.put("pauseOff",img);
		
		img = ImageBlitter.crop(menuSheet,650,126,34,26);
		il.addImage(img);
		focusTable.put("up",new Point(0,0));
		imageTable.put("up",img);
		
		img = ImageBlitter.crop(menuSheet,686,126,67,21);
		il.addImage(img);
		focusTable.put("down",new Point(0,0));
		imageTable.put("down",img);
		
		img = ImageBlitter.crop(menuSheet,650,154,45,21);
		il.addImage(img);
		focusTable.put("left",new Point(0,0));
		imageTable.put("left",img);
		
		img = ImageBlitter.crop(menuSheet,697,154,63,26);
		il.addImage(img);
		focusTable.put("right",new Point(0,0));
		imageTable.put("right",img);
		
		img = ImageBlitter.crop(menuSheet,406,98,69,26);
		il.addImage(img);
		focusTable.put("applyOn",new Point(0,0));
		imageTable.put("applyOn",img);
		
		img = ImageBlitter.crop(menuSheet,592,98,69,26);
		il.addImage(img);
		focusTable.put("applyOff",new Point(0,0));
		imageTable.put("applyOff",img);
		
		img = ImageBlitter.crop(menuSheet,477,98,79,21);
		il.addImage(img);
		focusTable.put("cancelOn",new Point(0,0));
		imageTable.put("cancelOn",img);
		
		img = ImageBlitter.crop(menuSheet,663,98,79,21);
		il.addImage(img);
		focusTable.put("cancelOff",new Point(0,0));
		imageTable.put("cancelOff",img);
		
		
		System.out.println("loaded image data for MenuSprite");
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
	*	draw(Graphics2D g)
	*	Helper method draws the image of this Sprite after transformations are readied. Called by render(Graphics2D g).
	*	If you need to do something fancy while rendering this sprite's image, override this method.
	*	Preconditions: g is the Graphics2D object passed in by render(Graphcis2D g).
	*	Postconditions: this sprite is drawn, applying the transformations readied in render(Graphics2D g).
	**/
	
	protected void draw(Graphics2D g)
	{
		if(type.equals("startingLives"))
		{
			g.drawImage(this.curImage, null, null);
			
			g.translate(180,0);
			if(this.value == 0)
				curImage = imageTable.get("0On");
			else
				curImage = imageTable.get("0Off");
			g.drawImage(this.curImage, null, null);
			
			g.translate(30,0);
			if(this.value == 1)
				curImage = imageTable.get("1On");
			else
				curImage = imageTable.get("1Off");
			g.drawImage(this.curImage, null, null);
			
			g.translate(30,0);
			if(this.value == 2)
				curImage = imageTable.get("2On");
			else
				curImage = imageTable.get("2Off");
			g.drawImage(this.curImage, null, null);
			
			g.translate(30,0);
			if(this.value == 3)
				curImage = imageTable.get("3On");
			else
				curImage = imageTable.get("3Off");
			g.drawImage(this.curImage, null, null);
			
			g.translate(30,0);
			if(this.value == 4)
				curImage = imageTable.get("4On");
			else
				curImage = imageTable.get("4Off");
			g.drawImage(this.curImage, null, null);
			
			g.translate(30,0);
			if(this.value == 5)
				curImage = imageTable.get("5On");
			else
				curImage = imageTable.get("5Off");
			g.drawImage(this.curImage, null, null);
			
		}
		else if(type.equals("midiVol") || type.equals("sfxVol"))
		{
			g.drawImage(this.curImage, null, null);
			
			g.translate(180,0);
			g.setColor(Color.BLACK);
			g.fillRect(0,0,204,24);
			g.setColor(new Color(0xFF0000));
			g.fillRect(2,2,2*this.value,20);
			g.setColor(new Color(0xFFAAAA));
			g.fillRect(2,6,2*this.value,12);
		}
		else if(type.equals("shootConfirm") || type.equals("bombCancel") || type.equals("slow") || 
					type.equals("skip") || type.equals("pause") || type.equals("up") ||
					type.equals("down") || type.equals("left") || type.equals("right"))
		{
			g.drawImage(this.curImage,null,null);
			g.translate(220,18);
			drawText(g);
		}
		else if(type.equals("genericText"))
		{
			drawText(g);
		}
		else
			g.drawImage(this.curImage, null, null);
	}
	
	
	
	private void drawText(Graphics2D g)
	{
		String[] tokens = this.text.split("\n");
		for(String str : tokens)
		{
			Stroke origStroke = g.getStroke();
			Color fillColor;
			if(isActive)
				fillColor = new Color(0xFFAAAA);
			else
				fillColor = new Color(0xAA0000);
			Font font = new Font("Arial",Font.BOLD, 28);
			FontRenderContext frc = g.getFontRenderContext();
			TextLayout tl = new TextLayout(str, font, frc);
			Shape shape = tl.getOutline(transform);
			
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(6));
			g.draw(shape);
			g.setStroke(origStroke);
			g.setColor(fillColor);
			g.setFont(font);
			g.drawString(str,0,0);
			g.translate(0,30);
		}
	}
	
	
	/**
	*	animate()
	*	Prepares the current animation frame and then prepares the sprite for computing its next frame of animation. Called by render(Graphics2D g).
	*	Preconditions: none.
	*	Postconditions: curImage is set to the image of this Sprite's current animation frame 
	*		and necessary values for computing the next frame of animation are prepared.
	**/
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		
		String imgKey = "" + type;
		
		if(isGrayedOut)
		{
			imgKey += "Gray";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 1;
			height = 1;
		}
		else if(isSwitchable)
		{
			if(isActive)
			{
				imgKey += "On";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 1;
				height = 1;
			}
			else
			{
				imgKey += "Off";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 1;
				height = 1;
			}
		}
		else
		{
			curImage = imageTable.get(imgKey);
				
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 1;
			height = 1;
		}
		
		
	}
	
	
}
