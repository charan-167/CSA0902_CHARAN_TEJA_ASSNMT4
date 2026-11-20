package task06;

import task06.dao.RegistrationDAO;
import task06.service.RegistrationService;
import task06.ui.EventRegistrationFrame;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Task6_Test {
    public static void main(String[] args) {
        try {
            RegistrationDAO dao = new RegistrationDAO("", "", "");
            RegistrationService service = new RegistrationService(dao);

            EventRegistrationFrame frame = new EventRegistrationFrame(service);
            frame.setSize(950, 540);
            frame.doLayout();
            frame.validate();

            File dir = new File("Task06/screenshots");
            if (!dir.exists()) dir.mkdirs();

            // 1. Initial / Normal State screenshot
            frame.getStudentIdField().setText("2024CS550");
            frame.getNameField().setText("Kola Charan");
            frame.getDepartmentCombo().setSelectedItem("Computer Science");
            frame.getEventCombo().setSelectedItem("Codeathon");

            BufferedImage imgNormal = new BufferedImage(950, 540, BufferedImage.TYPE_INT_RGB);
            Graphics2D g1 = imgNormal.createGraphics();
            g1.setColor(Color.WHITE);
            g1.fillRect(0, 0, 950, 540);
            frame.paint(g1);
            g1.dispose();
            ImageIO.write(imgNormal, "png", new File(dir, "event_reg_normal.png"));
            System.out.println("Saved event_reg_normal.png");

            // 2. Demonstration of Invalid Input Validation Error
            RegistrationService.ServiceResult invalidRes = service.registerStudent("", "", "-- Select Department --", "-- Select Event --");
            JOptionPane invalidPane = new JOptionPane(invalidRes.getMessage(), JOptionPane.ERROR_MESSAGE);
            JDialog errDialog = invalidPane.createDialog(frame, "Registration Error");
            errDialog.setSize(480, 180);
            BufferedImage imgErr = new BufferedImage(480, 180, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = imgErr.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, 480, 180);
            errDialog.paint(g2);
            g2.dispose();
            ImageIO.write(imgErr, "png", new File(dir, "event_reg_invalid_error.png"));
            System.out.println("Saved event_reg_invalid_error.png");

            // 3. Demonstration of Duplicate Registration Constraint Rejection
            // 2024CS101 is already registered for Codeathon in seed data
            RegistrationService.ServiceResult dupRes = service.registerStudent("2024CS101", "Aarav Sharma", "Computer Science", "Codeathon");
            JOptionPane dupPane = new JOptionPane(dupRes.getMessage(), JOptionPane.ERROR_MESSAGE);
            JDialog dupDialog = dupPane.createDialog(frame, "Duplicate Registration Blocked");
            dupDialog.setSize(520, 180);
            BufferedImage imgDup = new BufferedImage(520, 180, BufferedImage.TYPE_INT_RGB);
            Graphics2D g3 = imgDup.createGraphics();
            g3.setColor(Color.WHITE);
            g3.fillRect(0, 0, 520, 180);
            dupDialog.paint(g3);
            g3.dispose();
            ImageIO.write(imgDup, "png", new File(dir, "event_reg_duplicate_error.png"));
            System.out.println("Saved event_reg_duplicate_error.png");

            // 4. Demonstration of Event Filtering in Table
            frame.getFilterCombo().setSelectedItem("Codeathon");
            frame.loadRegistrations();
            Thread.sleep(300); // allow SwingWorker to complete
            frame.validate();

            BufferedImage imgFiltered = new BufferedImage(950, 540, BufferedImage.TYPE_INT_RGB);
            Graphics2D g4 = imgFiltered.createGraphics();
            g4.setColor(Color.WHITE);
            g4.fillRect(0, 0, 950, 540);
            frame.paint(g4);
            g4.dispose();
            ImageIO.write(imgFiltered, "png", new File(dir, "event_reg_filtered.png"));
            System.out.println("Saved event_reg_filtered.png");

            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
