package crc;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;

public class screen2image {

    private static final DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");

    public static void screenCapture(Rectangle bounds) throws Exception {

        Date date = new Date();
        Robot robot = new Robot();
        if (System.getProperty("os.name").toLowerCase().indexOf("win") < 0) {
            
            BufferedImage screenShot = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
            ImageIO.write(screenShot, "PNG", new File("sc-" + sdf.format(date) + ".png"));
            
        } else {
            
            String directoryName = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory()
                    + "/CRC Screenshots";
            String fileName = "sc-" + sdf.format(date) + ".png";

            File directory = new File(directoryName);
            if (!directory.exists()) {
                directory.mkdir();
            }

            File file = new File(directoryName + "/" + fileName);
            BufferedImage screenShot = robot.createScreenCapture(bounds);
            ImageIO.write(screenShot, "PNG", file);
            
        }
    }
}
