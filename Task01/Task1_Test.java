package task01;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Task1_Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FeeService service = new FeeService("", "", "");
                FeeWindowRepaired window = new FeeWindowRepaired(service);
                window.pack();
                window.setSize(480, 240);

                // Set sample data
                // Accessing child components through panel inspection or directly simulating appearance
                BufferedImage image = new BufferedImage(480, 240, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = image.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                window.paint(g2);
                g2.dispose();

                File dir = new File("Task01/screenshots");
                if (!dir.exists()) dir.mkdirs();
                File outFile = new File(dir, "fee_payment_repaired.png");
                ImageIO.write(image, "png", outFile);
                System.out.println("Saved Task 1 screenshot to: " + outFile.getAbsolutePath());
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
