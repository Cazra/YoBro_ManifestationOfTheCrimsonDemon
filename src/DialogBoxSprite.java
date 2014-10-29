
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public class DialogBoxSprite extends Sprite
{
	private static int numInstances = 0;
	private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
	private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
	
	public String text;
	public int paragraph;
	
	public Color textColor;
	
	public static final Color BLUE = new Color(0x9999FF);
	public static final Color RED = new Color(0xFF9999);
	
	// CONSTRUCTOR
	
	public DialogBoxSprite(double x, double y)
	{
		super(x,y);
		numInstances++;
		
		this.text = "";
		
		this.paragraph = 0;
		
		this.textColor = BLUE;
		
		fx = 0;
		fy = 0;
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
		// The DialogBox Sprite consists of only a semitransparent gradient-color rectangle and a gradient colored next. No external images needed.

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
	}
	
	
	
	/**
	*	Overridden draw(Graphics2D g) method
	**/
	
	protected void draw(Graphics2D g)
	{
		GradientPaint gp;
		Font origFont = g.getFont();
		g.setFont(new Font("Arial",Font.BOLD, 14));
		
		gp	= new GradientPaint(new Point(0,0), new Color(0xDD000000,true), new Point(0,80), new Color(0x44333333,true));
		g.setPaint(gp);
		g.fillRect(0,0,300,80);
		
		g.translate(10,22);
		
		String[] tokens = text.split("\n");
		
		for(String token : tokens)
		{
			gp = new GradientPaint(new Point(0,0), new Color(0xFFFFFF), new Point(0,-8), textColor);
			g.setPaint(gp);
			g.drawString(token,0,0);
			g.translate(0,22);
		}
		
		g.setFont(origFont);
		
	}
	
	
	
	
	
	
	
	
	
	
}
