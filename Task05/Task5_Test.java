package task05;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Task5_Test {
    public static void main(String[] args) {
        try {
            Converter converter = new Converter();
            converter.setSize(420, 160);
            converter.doLayout();

            File dir = new File("Task05/screenshots");
            if (!dir.exists()) dir.mkdirs();

            // 1. Valid Conversion Test
            converter.getAmountField().setText("5000");
            converter.getTargetCombo().setSelectedItem("USD");
            converter.triggerConvert(); // triggers convert logic
            converter.validate();

            BufferedImage imgValid = new BufferedImage(420, 160, BufferedImage.TYPE_INT_RGB);
            Graphics2D g1 = imgValid.createGraphics();
            g1.setColor(Color.WHITE);
            g1.fillRect(0, 0, 420, 160);
            converter.paint(g1);
            g1.dispose();
            ImageIO.write(imgValid, "png", new File(dir, "converter_valid.png"));
            System.out.println("Saved converter_valid.png (Result: " + converter.getResultLabel().getText() + ")");

            // 2. Invalid Amount Test
            converter.getAmountField().setText("abcXYZ");
            converter.triggerConvert();
            converter.validate();

            BufferedImage imgInvalid = new BufferedImage(420, 160, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = imgInvalid.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 420, 160);
            converter.paint(g2);
            g2.dispose();
            ImageIO.write(imgInvalid, "png", new File(dir, "converter_invalid.png"));
            System.out.println("Saved converter_invalid.png (Result: " + converter.getResultLabel().getText() + ")");

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
