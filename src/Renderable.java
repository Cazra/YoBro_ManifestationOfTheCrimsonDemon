import java.awt.Graphics;
import java.util.Hashtable;

/**
 *		Renderable interface
 *		by Stephen Lindberg
 *		last modified 10/1/11
 *
 *		An interface describing any type of game entity that can be rendered to a Graphics context.
 */

public interface Renderable
{
	/**
	 * render(Graphics,Camera)
	 *		The parent component of THIS should use this method to draw THIS to its context.
	 *	Preconditions: g is the parent component's Graphics context.
	 *						c is the Camera being used with the parent component.
	 *	Postconditions: THIS is rendered to g, using c's camera transformation. 
	 *						Returns true if THIS can be rendered successfully or if THIS is flagged as invisible.
	 *						Returns false if THIS cannot be successfully rendered and THIS is not flagged as invisible.
	 **/
	public boolean render(Graphics g);
	
	
	/**
	 *	loadImages()
	 *		This method loads any external image files used by THIS into memory. 
	 *		It should be called when all the other Renderables that share THIS's scope load their external files.
	 *	Preconditions:	imageTable is the Hashtable containing all the loaded external images for this level. 
	 *	Postconditions: All external images used by of THIS are loaded into imageTable if they are not already present in there. 
	 *	Throws: Exception if it is unable to load any of THIS's external images.
	 **/
	public void loadImages(Hashtable imageTable) throws Exception;
}
