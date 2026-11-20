package task03;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Task3_Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LibrarySearchFrame frame = new LibrarySearchFrame();
                File dir = new File("Task03/screenshots");
                if (!dir.exists()) dir.mkdirs();

                Object[][] dimensions = {
                    {500, 350, "library_search_small.png"},
                    {750, 450, "library_search_default.png"},
                    {1000, 650, "library_search_large.png"}
                };

                for (Object[] dim : dimensions) {
                    int w = (Integer) dim[0];
                    int h = (Integer) dim[1];
                    String filename = (String) dim[2];

                    frame.setSize(w, h);
                    frame.doLayout();
                    frame.validate();

                    BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                    Graphics2D g2 = image.createGraphics();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    frame.paint(g2);
                    g2.dispose();

                    File outFile = new File(dir, filename);
                    ImageIO.write(image, "png", outFile);
                    System.out.println("Saved " + filename + " (" + w + "x" + h + ")");
                }

                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
