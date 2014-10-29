import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Screen2Image
{
    public static void takeScreenShot()
    {
		try
		{
			 Robot robot = new Robot();
			 BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
			 ImageIO.write(screenShot, "JPG", new File("screenShot.jpg"));
			 System.out.println("took a screenshot");
		}
		catch(Exception e)
		{
			System.out.println("screenshot failed: ");
			e.printStackTrace();
		}
	}
}
