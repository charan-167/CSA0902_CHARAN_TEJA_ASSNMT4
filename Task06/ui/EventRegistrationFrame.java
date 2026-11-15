package task06.ui;

import task06.model.Registration;
import task06.service.RegistrationService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

/**
 * Stage 5 - Swing Presentation Layer for Campus Event Registration System.
 * Incorporates JFrame, JMenuBar, JPanel, JTextField, JComboBox, JTable,
 * and executes all JDBC queries asynchronously off the EDT via SwingWorker.
 */
public class EventRegistrationFrame extends JFrame {
    private final RegistrationService service;

    // Form inputs
    private final JTextField studentIdField = new JTextField(12);
    private final JTextField nameField = new JTextField(15);
    private final JComboBox<String> departmentCombo = new JComboBox<>(new String[]{
        "-- Select Department --", "Computer Science", "Information Tech", "Electronics", "Mechanical", "Electrical", "Civil"
    });
    private final JComboBox<String> eventCombo = new JComboBox<>(new String[]{
        "-- Select Event --", "Codeathon", "Web Design Hackathon", "RoboWars", "AutoCAD Design Expo", "Quiz Bowl", "Cultural Dance"
    });
    private final JButton registerButton = new JButton("Register Student");
    private final JButton resetFormButton = new JButton("Clear Form");

    // Filter and Table components
    private final JComboBox<String> filterCombo = new JComboBox<>(new String[]{
        "All Events", "Codeathon", "Web Design Hackathon", "RoboWars", "AutoCAD Design Expo", "Quiz Bowl", "Cultural Dance"
    });
    private final JButton refreshButton = new JButton("Refresh");
    private final JButton exportButton = new JButton("Export to CSV");

    private final DefaultTableModel tableModel;
    private final JTable registrationTable;

    // Pagination controls
    private int currentPage = 0;
    private final int pageSize = 10;
    private final JButton prevPageButton = new JButton("< Prev Page");
    private final JButton nextPageButton = new JButton("Next Page >");
    private final JLabel pageLabel = new JLabel("Page 1");

    // Status bar & Loading indicator
    private final JLabel statusLabel = new JLabel(" Ready.");
    private final JProgressBar progressBar = new JProgressBar();

    public EventRegistrationFrame(RegistrationService service) {
        super("Campus Event Registration Management System");
        this.service = service;

        // Init Table Model
        String[] columns = {"Reg ID", "Student ID", "Student Name", "Department", "Event Name", "Registration Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        registrationTable = new JTable(tableModel);
        registrationTable.setRowHeight(22);
        registrationTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));

        initUI();
        initMenuBar();
        loadRegistrations(); // initial background load
    }

    private void initUI() {
        setLayout(new BorderLayout(8, 8));

        // Top Banner
        JPanel bannerPanel = new JPanel(new BorderLayout());
        bannerPanel.setBackground(new Color(30, 58, 138));
        JLabel banner = new JLabel("  CAMPUS CULTURAL & TECH FEST - REGISTRATION PORTAL", JLabel.LEFT);
        banner.setFont(new Font("SansSerif", Font.BOLD, 14));
        banner.setForeground(Color.WHITE);
        banner.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bannerPanel.add(banner, BorderLayout.CENTER);
        add(bannerPanel, BorderLayout.NORTH);

        // Center split: Form on Left, Table on Right
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(380);

        // Left Panel: Form
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("New Student Registration"));

        JPanel formGrid = new JPanel(new GridLayout(4, 2, 8, 12));
        formGrid.setBorder(BorderFactory.createEmptyBorder(12, 10, 12, 10));
        formGrid.add(new JLabel("Student ID:"));
        formGrid.add(studentIdField);
        formGrid.add(new JLabel("Full Name:"));
        formGrid.add(nameField);
        formGrid.add(new JLabel("Department:"));
        formGrid.add(departmentCombo);
        formGrid.add(new JLabel("Choose Event:"));
        formGrid.add(eventCombo);
        leftPanel.add(formGrid, BorderLayout.NORTH);

        JPanel formButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        registerButton.setBackground(new Color(16, 185, 129));
        registerButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        formButtonPanel.add(registerButton);
        formButtonPanel.add(resetFormButton);
        leftPanel.add(formButtonPanel, BorderLayout.CENTER);

        splitPane.setLeftComponent(leftPanel);

        // Right Panel: Filter, Table, Pagination
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Registered Participants"));

        JPanel filterBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        filterBar.add(new JLabel("Filter by Event:"));
        filterBar.add(filterCombo);
        filterBar.add(refreshButton);
        filterBar.add(exportButton);
        rightPanel.add(filterBar, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(registrationTable);
        rightPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel paginationBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        paginationBar.add(prevPageButton);
        paginationBar.add(pageLabel);
        paginationBar.add(nextPageButton);
        rightPanel.add(paginationBar, BorderLayout.SOUTH);

        splitPane.setRightComponent(rightPanel);
        add(splitPane, BorderLayout.CENTER);

        // Bottom Status Bar
        JPanel bottomBar = new JPanel(new BorderLayout(5, 5));
        bottomBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setPreferredSize(new Dimension(120, 16));
        bottomBar.add(statusLabel, BorderLayout.CENTER);
        bottomBar.add(progressBar, BorderLayout.EAST);
        add(bottomBar, BorderLayout.SOUTH);

        // Listeners
        registerButton.addActionListener(e -> onRegisterClicked());
        resetFormButton.addActionListener(e -> resetForm());
        filterCombo.addActionListener(e -> { currentPage = 0; loadRegistrations(); });
        refreshButton.addActionListener(e -> loadRegistrations());
        exportButton.addActionListener(e -> exportData());

        prevPageButton.addActionListener(e -> {
            if (currentPage > 0) {
                currentPage--;
                loadRegistrations();
            }
        });
        nextPageButton.addActionListener(e -> {
            currentPage++;
            loadRegistrations();
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 540);
        setLocationRelativeTo(null);
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File Menu
        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem refreshItem = new JMenuItem("Refresh Data");
        refreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
        refreshItem.addActionListener(e -> loadRegistrations());
        fileMenu.add(refreshItem);

        JMenuItem exportItem = new JMenuItem("Export to CSV...");
        exportItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        exportItem.addActionListener(e -> exportData());
        fileMenu.add(exportItem);

        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
        exitItem.addActionListener(e -> dispose());
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);

        // Help Menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About System");
        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(this,
            "Campus Event Registration System\nCourse Outcome 4 - Java GUI with Layered JDBC Architecture\nDeveloped with Swing & Asynchronous SwingWorker.",
            "About", JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);
    }

    public void onRegisterClicked() {
        String studentId = studentIdField.getText();
        String name = nameField.getText();
        String dept = (String) departmentCombo.getSelectedItem();
        String event = (String) eventCombo.getSelectedItem();

        setBusy(true, "Validating and submitting registration to database...");

        // Asynchronous DB insertion off EDT
        SwingWorker<RegistrationService.ServiceResult, Void> worker = new SwingWorker<>() {
            @Override
            protected RegistrationService.ServiceResult doInBackground() {
                return service.registerStudent(studentId, name, dept, event);
            }

            @Override
            protected void done() {
                setBusy(false, "Ready.");
                try {
                    RegistrationService.ServiceResult result = get();
                    if (result.isSuccess()) {
                        statusLabel.setText(" Registration successful for " + studentId);
                        JOptionPane.showMessageDialog(EventRegistrationFrame.this,
                            result.getMessage(),
                            "Registration Confirmed",
                            JOptionPane.INFORMATION_MESSAGE);
                        resetForm();
                        loadRegistrations(); // refresh table
                    } else {
                        statusLabel.setText(" Registration rejected: " + result.getMessage());
                        JOptionPane.showMessageDialog(EventRegistrationFrame.this,
                            result.getMessage(),
                            "Registration Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    statusLabel.setText(" Error: " + ex.getMessage());
                    JOptionPane.showMessageDialog(EventRegistrationFrame.this,
                        "System Error: " + ex.getMessage(),
                        "Failure",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    public void loadRegistrations() {
        String filter = (String) filterCombo.getSelectedItem();
        setBusy(true, "Querying records from database (Page " + (currentPage + 1) + ")...");

        // Asynchronous DB query using SwingWorker to prevent UI freezing
        SwingWorker<List<Registration>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Registration> doInBackground() throws Exception {
                return service.getRegistrations(filter, currentPage, pageSize);
            }

            @Override
            protected void done() {
                setBusy(false, "Ready.");
                try {
                    List<Registration> records = get();
                    tableModel.setRowCount(0);
                    for (Registration r : records) {
                        tableModel.addRow(r.toTableRow());
                    }
                    pageLabel.setText("Page " + (currentPage + 1));
                    prevPageButton.setEnabled(currentPage > 0);
                    nextPageButton.setEnabled(records.size() == pageSize);
                    statusLabel.setText(" Displayed " + records.size() + " registrations for filter: " + filter);
                } catch (Exception ex) {
                    statusLabel.setText(" Failed to load registrations.");
                    JOptionPane.showMessageDialog(EventRegistrationFrame.this,
                        "Error fetching data: " + ex.getMessage(),
                        "Database Read Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void exportData() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("event_registrations.csv"));
        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String filter = (String) filterCombo.getSelectedItem();
            try {
                service.exportRegistrations(file, filter);
                JOptionPane.showMessageDialog(this, "Data exported successfully to:\n" + file.getAbsolutePath(), "Export Complete", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Export Failed: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void resetForm() {
        studentIdField.setText("");
        nameField.setText("");
        departmentCombo.setSelectedIndex(0);
        eventCombo.setSelectedIndex(0);
    }

    private void setBusy(boolean busy, String msg) {
        registerButton.setEnabled(!busy);
        resetFormButton.setEnabled(!busy);
        refreshButton.setEnabled(!busy);
        filterCombo.setEnabled(!busy);
        progressBar.setVisible(busy);
        statusLabel.setText(" " + msg);
    }

    // Getters for automated headless testing
    public JTextField getStudentIdField() { return studentIdField; }
    public JTextField getNameField() { return nameField; }
    public JComboBox<String> getDepartmentCombo() { return departmentCombo; }
    public JComboBox<String> getEventCombo() { return eventCombo; }
    public JComboBox<String> getFilterCombo() { return filterCombo; }
    public DefaultTableModel getTableModel() { return tableModel; }
}
