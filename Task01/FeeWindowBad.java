package task01;

import java.awt.Button;
import java.awt.Frame;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * ORIGINAL DEFECTIVE DESIGN (As given in Task 1 specification)
 * Included for forensic analysis and diagnosis.
 */
public class FeeWindowBad extends Frame implements ActionListener {
    TextField regNo = new TextField();
    TextField amount = new TextField();
    Button pay = new Button("Pay"), clear = new Button("Clear"), receipt = new Button("Receipt");

    public void actionPerformed(ActionEvent e) {
        // DEFECT 1: == string comparison instead of .equals()
        if (e.getActionCommand() == "Pay") {
            // DEFECT 2: Unhandled NumberFormatException on bad/empty input
            double a = Double.parseDouble(amount.getText());
            // DEFECT 3: Tightly coupled DB access directly inside GUI listener
            // DEFECT 4: Unhandled checked SQLException and leaking connection
            // DEFECT 5: SQL Injection via direct concatenation
            // DEFECT 6: Thread.sleep(3000) blocks Event Dispatch Thread (freezing GUI)
            // DEFECT 7: Feedback only to console, nothing visible to user
            try {
                Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/feesdb", "root", "root");
                c.createStatement().executeUpdate("INSERT INTO fees VALUES('" + regNo.getText() + "'," + a + ")");
                Thread.sleep(3000); // wait for bank confirmation
                System.out.println("Paid");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            // DEFECT 8: Both Clear and Receipt buttons fall into this else branch!
            regNo.setText("");
            amount.setText("");
        }
    }
}
