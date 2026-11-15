package task02;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

/**
 * Hospital Outpatient Appointment Desk (Task 2).
 * Implements event-driven GUI with cascading ComboBox updates,
 * input validation, and window close confirmation during active edits.
 */
public class AppointmentDeskFrame extends JFrame {
    private final JTextField patientIdField = new JTextField(12);
    private final JComboBox<String> departmentCombo = new JComboBox<>();
    private final JComboBox<String> doctorCombo = new JComboBox<>();
    private final JComboBox<String> timeSlotCombo = new JComboBox<>();
    private final JButton bookButton = new JButton("Book Appointment");
    private final JButton resetButton = new JButton("Reset Form");
    private final JLabel statusLabel = new JLabel("Welcome. Please fill details to book an appointment.", SwingConstants.CENTER);

    private final DoctorService doctorService;
    private boolean isBookingInProgress = false;

    public AppointmentDeskFrame(DoctorService doctorService) {
        super("Hospital Outpatient Appointment Desk");
        this.doctorService = doctorService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(12, 12));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(25, 118, 210));
        JLabel headerTitle = new JLabel("City Care Hospital - Outpatient Appointment Desk", SwingConstants.CENTER);
        headerTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        headerTitle.setForeground(Color.WHITE);
        headerTitle.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.add(headerTitle, BorderLayout.CENTER);
        add(headerPanel, BorderLayout.NORTH);

        // Center Form Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 12));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        formPanel.add(new JLabel("Patient ID:"));
        formPanel.add(patientIdField);

        formPanel.add(new JLabel("Department:"));
        departmentCombo.addItem("-- Select Department --");
        for (String dept : doctorService.getDepartments()) {
            departmentCombo.addItem(dept);
        }
        formPanel.add(departmentCombo);

        formPanel.add(new JLabel("Doctor:"));
        doctorCombo.addItem("-- Select Doctor --");
        doctorCombo.setEnabled(false);
        formPanel.add(doctorCombo);

        formPanel.add(new JLabel("Time Slot:"));
        timeSlotCombo.addItem("-- Select Time Slot --");
        timeSlotCombo.addItem("09:00 AM - 09:30 AM");
        timeSlotCombo.addItem("10:00 AM - 10:30 AM");
        timeSlotCombo.addItem("11:30 AM - 12:00 PM");
        timeSlotCombo.addItem("02:00 PM - 02:30 PM");
        timeSlotCombo.addItem("03:30 PM - 04:00 PM");
        formPanel.add(timeSlotCombo);

        add(formPanel, BorderLayout.CENTER);

        // South Panel: Buttons and Status
        JPanel southPanel = new JPanel(new BorderLayout(8, 8));
        southPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 15, 15));

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        bookButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        buttonRow.add(bookButton);
        buttonRow.add(resetButton);
        southPanel.add(buttonRow, BorderLayout.NORTH);

        statusLabel.setForeground(new Color(33, 33, 33));
        southPanel.add(statusLabel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // Event Listeners
        // 1. Cascading ComboBox: Refresh doctors when department selection changes
        departmentCombo.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                onDepartmentSelected((String) departmentCombo.getSelectedItem());
            }
        });

        // 2. Track user activity to determine if booking is in progress
        patientIdField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { checkDirty(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { checkDirty(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { checkDirty(); }
            private void checkDirty() {
                isBookingInProgress = !patientIdField.getText().trim().isEmpty();
            }
        });

        // 3. Book Button Action
        bookButton.addActionListener(e -> onBookClicked());

        // 4. Reset Button Action
        resetButton.addActionListener(e -> resetForm());

        // 5. Window Closing Confirmation when in progress
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });

        pack();
        setSize(520, 360);
        setLocationRelativeTo(null);
    }

    private void onDepartmentSelected(String selectedDept) {
        doctorCombo.removeAllItems();
        if (selectedDept == null || "-- Select Department --".equals(selectedDept)) {
            doctorCombo.addItem("-- Select Doctor --");
            doctorCombo.setEnabled(false);
            return;
        }

        List<String> doctors = doctorService.getDoctorsForDepartment(selectedDept);
        doctorCombo.addItem("-- Select Doctor --");
        for (String doc : doctors) {
            doctorCombo.addItem(doc);
        }
        doctorCombo.setEnabled(true);
        isBookingInProgress = true;
        statusLabel.setText("Department chosen: " + selectedDept + ". Please select a doctor.");
    }

    private void onBookClicked() {
        String patientId = patientIdField.getText();
        String dept = (String) departmentCombo.getSelectedItem();
        String doctor = (String) doctorCombo.getSelectedItem();
        String slot = (String) timeSlotCombo.getSelectedItem();

        DoctorService.BookingResult result = doctorService.bookAppointment(patientId, dept, doctor, slot);
        if (result.isSuccess()) {
            statusLabel.setForeground(new Color(15, 125, 40));
            statusLabel.setText("Status: " + result.getBookingRef() + " confirmed.");
            JOptionPane.showMessageDialog(this, result.getMessage(), "Booking Confirmed", JOptionPane.INFORMATION_MESSAGE);
            isBookingInProgress = false;
            resetForm();
        } else {
            statusLabel.setForeground(Color.RED);
            statusLabel.setText("Status: " + result.getMessage());
            JOptionPane.showMessageDialog(this, result.getMessage(), "Booking Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        patientIdField.setText("");
        departmentCombo.setSelectedIndex(0);
        doctorCombo.removeAllItems();
        doctorCombo.addItem("-- Select Doctor --");
        doctorCombo.setEnabled(false);
        timeSlotCombo.setSelectedIndex(0);
        statusLabel.setForeground(Color.BLACK);
        statusLabel.setText("Form cleared. Ready for next patient.");
        isBookingInProgress = false;
    }

    public void handleWindowClose() {
        if (isBookingInProgress) {
            int choice = JOptionPane.showConfirmDialog(
                this,
                "A booking entry is currently in progress.\nAre you sure you want to discard changes and exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (choice == JOptionPane.YES_OPTION) {
                dispose();
            }
        } else {
            dispose();
        }
    }

    // Getters for automated testing
    public JTextField getPatientIdField() { return patientIdField; }
    public JComboBox<String> getDepartmentCombo() { return departmentCombo; }
    public JComboBox<String> getDoctorCombo() { return doctorCombo; }
    public JComboBox<String> getTimeSlotCombo() { return timeSlotCombo; }
    public JButton getBookButton() { return bookButton; }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DoctorService service = new DoctorService();
            new AppointmentDeskFrame(service).setVisible(true);
        });
    }
}
