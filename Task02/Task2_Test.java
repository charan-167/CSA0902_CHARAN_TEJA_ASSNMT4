package task02;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Task2_Test {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DoctorService service = new DoctorService();
                AppointmentDeskFrame frame = new AppointmentDeskFrame(service);
                frame.setSize(520, 360);
                frame.doLayout();

                File dir = new File("Task02/screenshots");
                if (!dir.exists()) dir.mkdirs();

                // 1. Initial State Screenshot
                BufferedImage img1 = new BufferedImage(520, 360, BufferedImage.TYPE_INT_RGB);
                Graphics2D g1 = img1.createGraphics();
                g1.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                frame.paint(g1);
                g1.dispose();
                ImageIO.write(img1, "png", new File(dir, "appointment_desk_main.png"));

                // 2. Populated booking state
                frame.getPatientIdField().setText("PAT8082");
                frame.getDepartmentCombo().setSelectedItem("Cardiology");
                frame.getDoctorCombo().setSelectedItem("Dr. Rajesh Sharma");
                frame.getTimeSlotCombo().setSelectedItem("10:00 AM - 10:30 AM");

                BufferedImage img2 = new BufferedImage(520, 360, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = img2.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                frame.paint(g2);
                g2.dispose();
                ImageIO.write(img2, "png", new File(dir, "appointment_booked_confirm.png"));

                System.out.println("Saved Task 2 screenshots to Task02/screenshots/");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }
        });
    }
}
