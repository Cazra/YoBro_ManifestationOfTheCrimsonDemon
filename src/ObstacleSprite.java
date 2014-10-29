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


public class ObstacleSprite extends Sprite {
    private static int numInstances = 0;
    private static Hashtable<String, Image> imageTable = new Hashtable<String, Image>();
    private static Hashtable<String, Point> focusTable = new Hashtable<String, Point>();
    
    public char type;
    public int skinID = 1; // used for palette swaps of same enemy types.
    private int frameNum;
    private String curImgKey;
    private char direction; 
    
    // enemy vars
    
    public long timer;
    public boolean destroyable = false;
    public double hp;
    public int aiType;
    
    public boolean isSpawner; // spawners are not affected by player bullets. They do not hurt the player either.
    
    public ArrayList<Sprite> dependents; // bullets, bullet spawners, and SFX associated with this enemy. These will be destroyed when their parent is destroyed.
    public ArrayList<ItemSprite> itemDrops;
    
    public ObstacleSprite(double x, double y, char t, double hitPoints) {
        super(x,y);
        numInstances++;
        
        type = t;
        direction = 'S';
        hp = hitPoints;
        
        dependents = new ArrayList<Sprite>();
        itemDrops = new ArrayList<ItemSprite>();
        
        initHitRadius();
    }
    
    
    public void initHitRadius() {
        if(type == 'a') {
            setRadius(75);
        }
    }
    
    
    // MEM MANAGEMENT METHODS
    
    /**
    *    clean()
    *    Used to unload image data and cleans up static members of this Sprite extension 
    *        when the parent component is done with it.
    **/
    
    public static void clean() {
        numInstances = 0;
        focusTable.clear();
        imageTable.clear();
    }
    
    /**
    *    destroy()
    *    decrements number of instances of this class and marks it as destroyed.
    **/
    
    public void destroy() {
        super.destroy();
        for(Sprite s : dependents) {
            s.destroy();
        }
        dependents.clear();
        itemDrops.clear();
        
        numInstances--;
    }
    
    
    /**
    *    loadImages()
    *    loads and stores image data for this Sprite.
    **/
    
    public static void loadImages(ImageLoader il) {
        // get our default toolkit
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        URL imageURL;
        Image img;
        
        // asteroid
        
        imageURL = EnemySprite.class.getResource("graphics/asteroid.png");
        img = tk.getImage(imageURL);
        img = ColorFilters.setTransparentColor(img, new Color(0xFF00FF));
        
        il.addImage(img);
        focusTable.put("ast", new Point(80,80));
        imageTable.put("ast", img);
    }
    
    // LOGIC METHODS
        
    public void setDirection(char dir) {
        if(dir != 'S' && dir != 'E' && dir != 'W')
            return;
        else
            this.direction = dir;
    }
    
    /**
	*	boolean damage(int dmg)
	*	Causes this enemy to take damage.
	*	Preconditions: dmg is how much damage is dealt.
	*	Postconditions: This enemy's hp drops by dmg. 
	*		If its HP after taking the damage is <= 0, returns true. Else returns false.
	**/
	public boolean damage(int dmg) {
		this.hp -= dmg;
		if(this.hp <= 0) {
			this.hp = 0;
			return true;
		}
		return false;
	}
    
    // RENDERING METHODS
	
	
	/**
	*	draw(Graphics2D g)
	*	Helper method draws the image of this Sprite after transformations are readied. Called by render(Graphics2D g).
	*	If you need to do something fancy while rendering this sprite's image, override this method.
	*	Preconditions: g is the Graphics2D object passed in by render(Graphcis2D g).
	*	Postconditions: this sprite is drawn, applying the transformations readied in render(Graphics2D g).
	**/
	
	protected void draw(Graphics2D g) {
		g.drawImage(this.curImage, null, null);
    }
    
    protected void animate(ImageLoader il)
	{
		super.animate(il);
		
        if(type == 'a') {
            curImgKey = "ast";
            curImage = imageTable.get(curImgKey);
            
            Point curFocus = focusTable.get(curImgKey);
            fx = curFocus.x;
            fy = curFocus.y;
            
            width = 160;
            height = 160;
            
            rotate(getRotate() + 2);
        }
    }
}


