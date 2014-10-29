
import java.awt.*;
import java.awt.font.TextLayout;
import java.awt.font.FontRenderContext;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class StageTextSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public Color color1;
	public Color color2;
	public int fontSize;
	
	private char type;
	public String text;
	public boolean isActive;
	
	// CONSTRUCTOR
	
	public StageTextSprite(double x, double y, char t, String txt)
	{
		super(x,y);
		numInstances++;
		
		type = t;
		text = txt;
		isActive = false;
		
		color1 = new Color(1.0f,1.0f,1.0f, (float) (1.0-semiTransparency));
		color2 = new Color(0.7f,0.7f,1.0f, (float) (1.0-semiTransparency));
		fontSize = 14;
	}
	
	public StageTextSprite(double x, double y, char t)
	{
		this(x,y,t,"");
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
		
		// stage 1, stage 2, stage 3....
		
		imageURL = StageTextSprite.class.getResource("graphics/StageTitleSheet.png");
		Image hudSheet = tk.getImage(imageURL);
		hudSheet = ColorFilters.setTransparentColor(hudSheet, new Color(0xFF00FF));
		
		System.out.println("loaded image data for StageTextSprite");
		
		img = ImageBlitter.crop(hudSheet,1,1,100,33);
		il.addImage(img);
		focusTable.put("stage1",new Point(0,0));
		imageTable.put("stage1",img);
		
		img = ImageBlitter.crop(hudSheet,103,1,100,33);
		il.addImage(img);
		focusTable.put("stage2",new Point(0,0));
		imageTable.put("stage2",img);
		
		img = ImageBlitter.crop(hudSheet,205,1,100,33);
		il.addImage(img);
		focusTable.put("stage3",new Point(0,0));
		imageTable.put("stage3",img);
		
		img = ImageBlitter.crop(hudSheet,307,1,100,33);
		il.addImage(img);
		focusTable.put("stage4",new Point(0,0));
		imageTable.put("stage4",img);
		
		img = ImageBlitter.crop(hudSheet,409,1,100,33);
		il.addImage(img);
		focusTable.put("stage5",new Point(0,0));
		imageTable.put("stage5",img);
		
		img = ImageBlitter.crop(hudSheet,1,36,149,33);
		il.addImage(img);
		focusTable.put("stageF",new Point(0,0));
		imageTable.put("stageF",img);
		
		img = ImageBlitter.crop(hudSheet,152,36,161,33);
		il.addImage(img);
		focusTable.put("stageE",new Point(0,0));
		imageTable.put("stageE",img);
		
		// stage names
		
		imageURL = StageTextSprite.class.getResource("graphics/LevelTitles.png");
		Image titleSheet = tk.getImage(imageURL);
		titleSheet = ColorFilters.setTransparentColor(titleSheet, new Color(0xFF00FF));

		img = ImageBlitter.crop(titleSheet,1,1,136,30);
		il.addImage(img);
		focusTable.put("stage1t",new Point(68,6));
		imageTable.put("stage1t",img);
		
		img = ImageBlitter.crop(titleSheet,1,33,140,12);
		il.addImage(img);
		focusTable.put("stage2t",new Point(70,6));
		imageTable.put("stage2t",img);
		
		img = ImageBlitter.crop(titleSheet,1,47,162,12);
		il.addImage(img);
		focusTable.put("stage3t",new Point(81,6));
		imageTable.put("stage3t",img);
		
		img = ImageBlitter.crop(titleSheet,1,61,184,12);
		il.addImage(img);
		focusTable.put("stage4t",new Point(92,6));
		imageTable.put("stage4t",img);
		
		img = ImageBlitter.crop(titleSheet,1,75,161,17);
		il.addImage(img);
		focusTable.put("stage5t",new Point(80,6));
		imageTable.put("stage5t",img);
		
		// pause menus
		
		img = ImageBlitter.crop(hudSheet,1,71,84,19);
		il.addImage(img);
		focusTable.put("resumeOn",new Point(42,0));
		imageTable.put("resumeOn",img);
		
		img = ImageBlitter.crop(hudSheet,1,92,84,19);
		il.addImage(img);
		focusTable.put("resumeOff",new Point(42,0));
		imageTable.put("resumeOff",img);
		
		img = ImageBlitter.crop(hudSheet,87,71,55,23);
		il.addImage(img);
		focusTable.put("retryOn",new Point(27,0));
		imageTable.put("retryOn",img);
		
		img = ImageBlitter.crop(hudSheet,87,96,55,23);
		il.addImage(img);
		focusTable.put("retryOff",new Point(27,0));
		imageTable.put("retryOff",img);
		
		img = ImageBlitter.crop(hudSheet,144,71,143,19);
		il.addImage(img);
		focusTable.put("quitOn",new Point(71,0));
		imageTable.put("quitOn",img);
		
		img = ImageBlitter.crop(hudSheet,144,92,143,19);
		il.addImage(img);
		focusTable.put("quitOff",new Point(71,0));
		imageTable.put("quitOff",img);
		
		img = ImageBlitter.crop(hudSheet,315,36,100,50);
		il.addImage(img);
		focusTable.put("paused",new Point(50,0));
		imageTable.put("paused",img);
		
		img = ImageBlitter.crop(hudSheet,417,36,226,48);
		il.addImage(img);
		focusTable.put("areYouSure",new Point(113,0));
		imageTable.put("areYouSure",img);
		
		img = ImageBlitter.crop(hudSheet,289,88,147,42);
		il.addImage(img);
		focusTable.put("continue",new Point(73,0));
		imageTable.put("continue",img);
		
		img = ImageBlitter.crop(hudSheet,1,113,38,19);
		il.addImage(img);
		focusTable.put("yesOn",new Point(19,0));
		imageTable.put("yesOn",img);
		
		img = ImageBlitter.crop(hudSheet,1,134,38,19);
		il.addImage(img);
		focusTable.put("yesOff",new Point(19,0));
		imageTable.put("yesOff",img);
		
		img = ImageBlitter.crop(hudSheet,41,113,29,19);
		il.addImage(img);
		focusTable.put("noOn",new Point(14,0));
		imageTable.put("noOn",img);
		
		img = ImageBlitter.crop(hudSheet,41,134,29,19);
		il.addImage(img);
		focusTable.put("noOff",new Point(14,0));
		imageTable.put("noOff",img);
		
		img = ImageBlitter.crop(hudSheet,72,132,147,32);
		il.addImage(img);
		focusTable.put("contRem3",new Point(74,0));
		imageTable.put("contRem3",img);
		
		img = ImageBlitter.crop(hudSheet,221,132,147,32);
		il.addImage(img);
		focusTable.put("contRem2",new Point(74,0));
		imageTable.put("contRem2",img);
		
		img = ImageBlitter.crop(hudSheet,370,132,147,32);
		il.addImage(img);
		focusTable.put("contRem1",new Point(74,0));
		imageTable.put("contRem1",img);
		
		img = ImageBlitter.crop(hudSheet,72,167,147,32);
		il.addImage(img);
		focusTable.put("contRem0",new Point(74,0));
		imageTable.put("contRem0",img);
		
		// Stage clear text
		
		img = ImageBlitter.crop(hudSheet,438,86,170,33);
		il.addImage(img);
		focusTable.put("stageClear",new Point(85,0));
		imageTable.put("stageClear",img);
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
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		
		if(type == '1')
		{
			String imgKey = "stage1";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 100;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == '2')
		{
			String imgKey = "stage2";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 100;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == '3')
		{
			String imgKey = "stage3";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 100;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == '4')
		{
			String imgKey = "stage4";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 100;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == '5')
		{
			String imgKey = "stage5";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 100;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 'F')
		{
			String imgKey = "stageF";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 149;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 'E')
		{
			String imgKey = "stageE";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 'T') // title text (raster)
		{
			String imgKey = text + "t";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 't') // title text
		{
			fx = 0;
			fy = -6;
		}
		
		if(type == 'P') // paused
		{
			String imgKey = "paused";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 149;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 'A') // are you sure
		{
			String imgKey = "areYouSure";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 149;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 'C') // continue
		{
			String imgKey = "continue";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 149;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 'c') // continues remaining
		{
			String imgKey = "contRem" + (int)this.vars[0];
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 149;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		if(type == 's') // stage clear
		{
			String imgKey = "stageClear";
			curImage = imageTable.get(imgKey);
			
			Point curFocus = focusTable.get(imgKey);
			fx = curFocus.x;
			fy = curFocus.y;
			
			width = 149;
			height = 33;
			
			animateSemiTransparencyFrame(il,imgKey);
		}
		
		if(type == 'u') // resume (unpause)
		{
			if(isActive)
			{
				String imgKey = "resumeOn";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
			else
			{
				String imgKey = "resumeOff";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
		}
		if(type == 'r') // retry
		{
			if(isActive)
			{
				String imgKey = "retryOn";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
			else
			{
				String imgKey = "retryOff";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
		}
		if(type == 'q') // return to title
		{
			if(isActive)
			{
				String imgKey = "quitOn";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
			else
			{
				String imgKey = "quitOff";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
		}
		
		if(type == 'y') // yes
		{
			if(isActive)
			{
				String imgKey = "yesOn";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
			else
			{
				String imgKey = "yesOff";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
		}
		
		if(type == 'n') // yes
		{
			if(isActive)
			{
				String imgKey = "noOn";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
			else
			{
				String imgKey = "noOff";
				curImage = imageTable.get(imgKey);
				
				Point curFocus = focusTable.get(imgKey);
				fx = curFocus.x;
				fy = curFocus.y;
				
				width = 149;
				height = 33;
				
				animateSemiTransparencyFrame(il,imgKey);
			}
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
		
	}
	

	/**
	*	Overridden draw(Graphics2D g) method
	**/
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		if(this.type == 't')
		{
			Font origFont = g.getFont();
			
			Font font = new Font("Arial",Font.BOLD, fontSize);
			g.setFont(font);
			
			String[] tokens = text.split("~");
			GradientPaint gp = new GradientPaint(new Point(0,0), color1, new Point(0,-8), color2);
			
			for(String token : tokens)
			{
				g.setColor(Color.BLACK);
				drawCenteredString(token,1,1,g);
				drawCenteredString(token,1,-1,g);
				drawCenteredString(token,-1,1,g);
				drawCenteredString(token,-1,-1,g);
				
				drawCenteredString(token,1,0,g);
				drawCenteredString(token,0,-1,g);
				drawCenteredString(token,0,1,g);
				drawCenteredString(token,-1,0,g);

				g.setPaint(gp);
				drawCenteredString(token,0,0,g);
				
				g.translate(0,20);
			}
			
			g.setFont(origFont);
		}
		else
			super.draw(g);
	}
	
	/*
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
	}*/
	
	private void drawCenteredString(String s, int x, int y, Graphics2D g)
	{
		
		FontMetrics fm = g.getFontMetrics();
		
		x-= fm.stringWidth(s)/2;
		 
		
		g.drawString(s, x,y);
	}
	
	
}
