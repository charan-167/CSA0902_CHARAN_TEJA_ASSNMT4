package task04;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Task4_Test {
    public static void main(String[] args) {
        try {
            DrawPad pad = new DrawPad();
            pad.setSize(400, 300);
            pad.doLayout();

            File dir = new File("Task04/screenshots");
            if (!dir.exists()) dir.mkdirs();

            // Simulate user clicking 5 locations
            Point[] clickPoints = {
                new Point(60, 80),
                new Point(140, 120),
                new Point(200, 70),
                new Point(280, 180),
                new Point(330, 110)
            };

            for (Point p : clickPoints) {
                pad.getPoints().add(p);
            }
            pad.getStatusLabel().setText("Clicks: " + pad.getPoints().size());

            // 1. Screenshot after clicks
            BufferedImage imgBefore = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
            Graphics2D g1 = imgBefore.createGraphics();
            g1.setColor(Color.WHITE);
            g1.fillRect(0, 0, 400, 300);
            pad.paint(g1);
            g1.dispose();
            ImageIO.write(imgBefore, "png", new File(dir, "drawpad_after_clicks.png"));
            System.out.println("Saved drawpad_after_clicks.png");

            // 2. Simulate OS minimization and restoration (OS sends expose/damage event triggering full repaint)
            // In repaired version, paint() traverses stored points, so dots persist!
            BufferedImage imgAfter = new BufferedImage(400, 300, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = imgAfter.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 400, 300);
            pad.paint(g2); // repainted from scratch!
            g2.dispose();
            ImageIO.write(imgAfter, "png", new File(dir, "drawpad_after_restore.png"));
            System.out.println("Saved drawpad_after_restore.png");

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
