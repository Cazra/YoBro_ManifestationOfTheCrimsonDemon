
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class NumberSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public long value;
	private String animString;
	public long max;
	public int minDigits;
	public char type;
	public double lagValue;
	
	public String caption1;
	public String caption2;
	
	// CONSTRUCTOR
	
	public NumberSprite(double x, double y, long initVal, long max)
	{
		super(x,y);
		numInstances++;
		
		this.value = initVal;
		animString = "";
		
		this.max = max;
		this.minDigits = -1;
		type = 'n';
		lagValue = 0;
	}
	
	public NumberSprite(double x, double y, long initVal, long max, char t)
	{
		this(x,y,initVal,max);
		type = t;
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
		
		// digits
		
		imageURL = NumberSprite.class.getResource("graphics/LevelInterfaceIcons.png");
		Image hudSheet = tk.getImage(imageURL);
		hudSheet = ColorFilters.setTransparentColor(hudSheet, new Color(0xFF00FF));
		
		img = ImageBlitter.crop(hudSheet,1,41,10,16);
		il.addImage(img);
		focusTable.put("0",new Point(0,0));
		imageTable.put("0",img);
		
		img = ImageBlitter.crop(hudSheet,13,41,8,16);
		il.addImage(img);
		focusTable.put("1",new Point(0,0));
		imageTable.put("1",img);
		
		img = ImageBlitter.crop(hudSheet,23,41,10,16);
		il.addImage(img);
		focusTable.put("2",new Point(0,0));
		imageTable.put("2",img);
		
		img = ImageBlitter.crop(hudSheet,35,41,10,16);
		il.addImage(img);
		focusTable.put("3",new Point(0,0));
		imageTable.put("3",img);
		
		img = ImageBlitter.crop(hudSheet,47,41,10,16);
		il.addImage(img);
		focusTable.put("4",new Point(0,0));
		imageTable.put("4",img);
		
		img = ImageBlitter.crop(hudSheet,59,41,10,16);
		il.addImage(img);
		focusTable.put("5",new Point(0,0));
		imageTable.put("5",img);
		
		img = ImageBlitter.crop(hudSheet,71,41,10,16);
		il.addImage(img);
		focusTable.put("6",new Point(0,0));
		imageTable.put("6",img);
		
		img = ImageBlitter.crop(hudSheet,83,41,10,16);
		il.addImage(img);
		focusTable.put("7",new Point(0,0));
		imageTable.put("7",img);
		
		img = ImageBlitter.crop(hudSheet,95,41,10,16);
		il.addImage(img);
		focusTable.put("8",new Point(0,0));
		imageTable.put("8",img);
		
		img = ImageBlitter.crop(hudSheet,107,41,10,16);
		il.addImage(img);
		focusTable.put("9",new Point(0,0));
		imageTable.put("9",img);
		
		img = ImageBlitter.crop(hudSheet,119,41,10,16);
		il.addImage(img);
		focusTable.put("-",new Point(0,0));
		imageTable.put("-",img);
		
		img = ImageBlitter.crop(hudSheet,119,41,10,16);
		il.addImage(img);
		focusTable.put("-",new Point(0,0));
		imageTable.put("-",img);
		
		img = ImageBlitter.crop(hudSheet,131,41,33,15);
		il.addImage(img);
		focusTable.put("max",new Point(0,0));
		imageTable.put("max",img);
		
		System.out.println("loaded image data for NumberSprite");

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
	*	Special animation 
	**/
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
		
		fx = 0;
		fy = 0;
		
		if(type == 'n')
		{
			if(this.value >= this.max && this.max != -1)
			{
				height = 15;
				width = 33;
				return;
			}
			
			height = 16;
			width = 0;
			long curVal = this.value;
			
			animString = "";
			
			boolean finished = false;
			boolean isNegative = false;
			while(!finished)
			{
				if(curVal < 0)
				{
					curVal *= -1;
					isNegative = true;
				}
				else
				{
					int nextDigit = (int) (curVal % 10);
					
					if(nextDigit == 1)
						width += 8;
					else
						width += 10;
						
					animString = nextDigit + animString;
					curVal /= 10;
				}
				
				if(curVal == 0)
					finished = true;
			}
			
			while(animString.length() < this.minDigits)
			{
				animString = "0" + animString;
			}
			
			if(isNegative)
			{
				animString = "-" + animString;
			}
		}
		if(type == 'b')
		{
			lagValue += (value - lagValue)/10.0;
			if(lagValue < 0)
				lagValue = 0;
		}
		
	}
	
	
	
	/**
	*	Overridden draw(Graphics2D g) method
	**/
	
	protected void draw(Graphics2D g)
	{
		//g.drawImage(this.curImage, null, null);
		
		//animString = "12345";
		if(type == 'n')
		{
			if(this.value >= this.max && this.max != -1)
			{
				this.curImage = imageTable.get("max");
				g.drawImage(this.curImage, null, null);
				return;
			}
			
			for(int i = 0; i < animString.length(); i++)
			{
				char digit = animString.charAt(i);
				int dx;
				if(digit == '1')
					dx = 8;
				else
					dx = 10;
				
				this.curImage = imageTable.get("" + digit);
				g.drawImage(this.curImage, null, null);
				g.translate(dx,0);
			}
		}
		if(type == 'b')
		{
			int maxWidth = 350;
			g.setColor(new Color(0xFF0000));
			g.fillRect(0, 0, (int) Math.min(maxWidth * lagValue/max, maxWidth), 8);
			
			g.setColor(new Color(0xFF9999));
			g.fillRect(0, 2, (int) Math.min(maxWidth * lagValue/max, maxWidth), 4);
			
			g.translate(0,14);
			Font origFont = g.getFont();
			g.setFont(new Font("Arial",Font.BOLD, 10));
			
			if(caption1 != null)
			{
				g.setColor(new Color(0xFF555555));
				for(int i = -1; i <= 1; i++)
				{
					for(int j = -1; j <= 1; j++)
					{
						if(!(i ==0 && j == 0))
							g.drawString(caption1,i,j);
					}
				}
				g.setColor(new Color(0xFFDDDDDD));
				g.drawString(caption1,0,0);
			}
			g.translate(350,0);
			
			if(caption2 != null)
			{
				g.setColor(new Color(0xFF555555));
				for(int i = -1; i <= 1; i++)
				{
					for(int j = -1; j <= 1; j++)
					{
						if(!(i ==0 && j == 0))
							drawRightJustifiedString(caption2,i,j,g);
					}
				}
				g.setColor(new Color(0xFFDDDDDD));
				drawRightJustifiedString(caption2,0,0,g);
			}
		}
	}
	
	private void drawRightJustifiedString(String s, int x, int y, Graphics2D g)
	{
		 FontMetrics fm = g.getFontMetrics();
		 x-= fm.stringWidth(s);
		 g.drawString(s, x, y);
	}
	
}
