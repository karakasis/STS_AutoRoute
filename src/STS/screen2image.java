package STS;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

public class screen2image {

    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void screenCapture(JFrame parent) throws Exception {

        Date date = new Date();
        Robot robot = new Robot();
        if (System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
            
            String fileName = "sc-" + sdf.format(date) + ".png";
            BufferedImage img = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_ARGB);
            parent.paint(img.getGraphics());
            File outputfile = new File("/" + fileName);
            ImageIO.write(img, "png", outputfile);
            
            //BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            //ImageIO.write(screenShot, "PNG", new File("sc-" + sdf.format(date) + ".png"));

        } else {

            String directoryName = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()
                    + "/STS Screenshots";
            String fileName = "sc-" + sdf.format(date) + ".png";

            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            BufferedImage img = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_ARGB);
            parent.paint(img.getGraphics());
            File outputfile = new File(directoryName + "/" + fileName);
            ImageIO.write(img, "png", outputfile);

            //File file = new File(directoryName + "/" + fileName);
            //BufferedImage screenShot = robot.createScreenCapture(bounds);
            //ImageIO.write(screenShot, "PNG", file);

        }
    }
}
