
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Random;
import java.net.URL;
import gameEngine.*;


public abstract class AnimatedBackgroundSprite extends Sprite
{
	protected int frameNum;
	protected String curImgKey;
	protected MainLevel mainLevel;
	
	// public vars
	
	public long timer;
	public int curBGID;
	public int nextBGID;
	public double transitionPercent;
	
	// CONSTRUCTOR
	
	public AnimatedBackgroundSprite(MainLevel ml)
	{
		super(0,0);
		timer = 0;
		curBGID = 0;
		mainLevel = ml;
	}
	
	
	// MEM MANAGEMENT METHODS
	

	

	// LOGIC METHODS

	protected void runTransition()
	{
		if(transitionPercent < 1.0 && curBGID != nextBGID)
		{
			transitionPercent += 0.1;
		}
		else
		{
			curBGID = nextBGID;
			transitionPercent = 0.0;
		}
	}
	
	// RENDERING METHODS
	
	

}
