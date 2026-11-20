package task06;

import task06.dao.RegistrationDAO;
import task06.service.RegistrationService;
import task06.ui.EventRegistrationFrame;

import javax.swing.*;

/**
 * Application Entry Point for Task 6 - Campus Event Registration System.
 */
public class Task6_App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            // MySQL parameters (default local configuration)
            String mysqlUrl = "jdbc:mysql://localhost:3306/campus_events_db?useSSL=false&allowPublicKeyRetrieval=true";
            String user = "root";
            String password = "password";

            RegistrationDAO dao = new RegistrationDAO(mysqlUrl, user, password);
            RegistrationService service = new RegistrationService(dao);
            EventRegistrationFrame frame = new EventRegistrationFrame(service);
            frame.setVisible(true);
        });
    }
}
