
import java.awt.Image;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Font;
import java.awt.FontMetrics;
import java.util.Hashtable;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public abstract class DialogPortraitSprite extends Sprite
{
	public String emote;
	public String text;
	public double textX,textY;
	public double textSemiTrans;
	
	
	// CONSTRUCTOR
	
	public DialogPortraitSprite(double x, double y, String txt)
	{
		super(x,y);		
		text = txt;
		textSemiTrans = 0.0;
		textX = 128;
		textY = 150;
		
		emote = ":)";
	}
	
	// MEM MANAGEMENT METHODS
	
	
	// RENDERING METHODS
	
	protected void animate(ImageLoader il)
	{
		super.animate(il);
	}
	
	

	/**
	*	Overridden draw(Graphics2D g) method
	**/
	
	protected void draw(Graphics2D g)
	{
		super.draw(g);

		if(!text.equals(""))
		{
			GradientPaint gp;
			Font origFont = g.getFont();
			g.setFont(new Font("Arial",Font.BOLD, 14));
			
			String[] tokens = text.split("~");
			g.translate(textX,textY);
			
			for(String token : tokens)
			{
				Color color1 = new Color(1.0f,1.0f,1.0f, (float) ((1.0-semiTransparency)*(1.0-textSemiTrans)));
				Color color2 = new Color(0.7f,0.7f,1.0f, (float) ((1.0-semiTransparency)*(1.0-textSemiTrans)));
				gp = new GradientPaint(new Point(0,0), color1, new Point(0,-8), color2);
				
				g.setColor(Color.BLACK);
				drawCenteredString(token,-1,-1,g);
				drawCenteredString(token,-1,1,g);
				drawCenteredString(token,1,-1,g);
				drawCenteredString(token,1,1,g);
				drawCenteredString(token,-1,0,g);
				drawCenteredString(token,0,-1,g);
				drawCenteredString(token,1,0,g);
				drawCenteredString(token,0,1,g);
				g.setPaint(gp);
				drawCenteredString(token,0,0,g);
				g.translate(0,16);
			}
			
			g.setFont(origFont);
		}
			
	}
	
	private void drawCenteredString(String s, int x, int y, Graphics2D g)
	{
		 FontMetrics fm = g.getFontMetrics();
		 x-= fm.stringWidth(s)/2;
		 g.drawString(s, x, y);
	}
	
	
	public abstract void spellCardPortrait();
	
	
}
