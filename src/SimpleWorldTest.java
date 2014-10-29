import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import gameEngine.*;

public class SimpleWorldTest extends JFrame implements WindowListener, WindowFocusListener
{

	private JPanel screenP;
	private JPanel bgFraming;
	
	private int screenX;
	private int screenY;
	
	private GraphicsDevice grDev;
	private DisplayMode oldDisplay;
	
	/**
	*	Constructor
	*	Preconditions: fullscreen is a boolean value that sets Full-screen mode on or off.
	*	Postconditions: The game is started in its initial state and it loads all necessary starting data. 
	*		If fullscreen is true, the game will begin running in Full-Screen. Else it will run in windowed mode.
	**/
	
	public SimpleWorldTest(boolean fullscreen)
	{
		super("Yo Bro: Manifestation of the Crimson Demon");
		screenX = 640;	
		screenY = 480;
		this.setSize(screenX,screenY);
		
		// set up resolution change mode
		
		this.addWindowListener(this);
		this.addWindowFocusListener(this);
		
		grDev = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); // obtains your graphics device
		oldDisplay = grDev.getDisplayMode(); // retain original DisplayMode to revert back to when program finishes.
		
		// setup the game for full-screen if requested.
		
		if(fullscreen)
		{
			System.out.println("Trying to start program in Fullscreen mode.");
			
			if(grDev.isFullScreenSupported()) // makes sure fullscreen is supported before doing anything.
			{
				System.out.println("FullScreen is supported");
				this.setUndecorated(true);
				DisplayMode resChangeMode = new DisplayMode(640,480,32,DisplayMode.REFRESH_RATE_UNKNOWN); // create new DisplayMode with different resolution.
				
				try
				{
					grDev.setFullScreenWindow(this); // set fullscreen mode on. Otherwise this won't work
					grDev.setDisplayMode(resChangeMode); // change DisplayMode to our new resolution.
					System.out.println("Change resolution: Success!");
				}
				catch(Exception e)
				{
					System.out.println("Change resolution: FAIL!");
				}
			}
			
			this.setExtendedState(MAXIMIZED_BOTH);
		}

		// instantiate main window panel
		
		screenP = new SimpleWorld();
		bgFraming = new JPanel();
		
		// set background panel properties
		
		bgFraming.setBackground(Color.black);
		this.add(bgFraming);
		
		bgFraming.setLayout(null);
		
		// set screen panel properties
		
		bgFraming.add(screenP);
		screenP.setSize(screenX,screenY);
		
		// finishing touches on Game window
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		this.recenterScreen();
		try
		{
			screenP.requestFocusInWindow();
		}
		catch(Exception e)
		{
		}
		
		System.out.println("Game Window successfully created!!!");
	}
	
	/** 
	*	recenterScreen()
	*	Preconditions: none
	*	Postconditions: The game's main JPanel is centered within its window.
	**/
	
	public void recenterScreen()
	{
		int windowWidth = getWidth();
		int windowHeight = getHeight();
		
		screenP.setLocation((windowWidth - screenX)/2,(windowHeight - screenY)/2);
	}
	
	// WindowListener interface stuff
	
	public void windowActivated(WindowEvent e)
	{
		System.out.println("Window Activated");
	}
	
	public void windowClosed(WindowEvent e) // resets to old display resolution after program closes.
	{
		System.out.println("Yo-Bro program terminated. Restoring original display settings.");
		grDev.setDisplayMode(oldDisplay);
	}
	
	public void	windowClosing(WindowEvent e)
	{
		System.out.println("Window closing");
	}
	public void windowDeactivated(WindowEvent e)
	{
		System.out.println("Window deactivated");
	}
 	public void windowDeiconified(WindowEvent e)
	{
		System.out.println("Window Deiconified");
		try
		{
			this.recenterScreen();
			screenP.requestFocusInWindow();
		}
		catch(Exception ex)
		{
		}
	}
 	public void windowIconified(WindowEvent e)
	{
		System.out.println("Window Iconified");
	}
 	public void windowOpened(WindowEvent e) 
	{
		System.out.println("Window opened");
	}
	public void windowGainedFocus(WindowEvent e) 
	{
		System.out.println("Window gained focus");
		try
		{
			this.recenterScreen();
			screenP.requestFocusInWindow();
		}
		catch(Exception ex)
		{
		}
	}
	public void windowLostFocus(WindowEvent e) 
	{
		System.out.println("Window lost focus");
	}
	
	
	
	
	
	public static void main(String[] args)
	{
		SimpleWorldTest gui;
		
		for(String s : args)
			System.out.println(s);
		if(args.length == 0)
			gui = new SimpleWorldTest(false);
		else if(args[0].equals("fullscreen"))
			gui = new SimpleWorldTest(true);
		
	}

}