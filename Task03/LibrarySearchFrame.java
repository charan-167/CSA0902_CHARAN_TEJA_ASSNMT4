package task03;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * Campus Library Search Screen (Task 3).
 * Demonstrates proper layout manager nesting:
 * - Frame uses BorderLayout
 * - North panel uses BorderLayout + FlowLayout for title and search row
 * - Center holds a JScrollPane containing JTable (expands on window resize)
 * - South panel holds button row (FlowLayout) and status message label
 */
public class LibrarySearchFrame extends JFrame {
    private final JTextField searchField = new JTextField(18);
    private final JComboBox<String> categoryCombo = new JComboBox<>(new String[]{
        "All Categories", "Computer Science", "Electrical Engg", "Mathematics", "Literature"
    });
    private final JButton searchButton = new JButton("Search");

    private final DefaultTableModel tableModel;
    private final JTable resultsTable;

    private final JButton prevButton = new JButton("<< Previous");
    private final JButton nextButton = new JButton("Next >>");
    private final JButton reserveButton = new JButton("Reserve Selected Book");
    private final JLabel statusLabel = new JLabel(" Ready. Enter search keyword or pick a category.", SwingConstants.LEFT);

    public LibrarySearchFrame() {
        super("Campus Library Management System - Book Search");

        // 1. Root Frame Layout
        setLayout(new BorderLayout(0, 8));

        // 2. North Container (Title + Search Row)
        JPanel northPanel = new JPanel(new BorderLayout(0, 5));

        // Title Bar
        JLabel titleLabel = new JLabel("CAMPUS LIBRARY CATALOG SEARCH", SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(41, 128, 185));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        // Search Controls Row (FlowLayout preserves fixed control dimensions)
        JPanel searchRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        searchRow.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        searchRow.add(new JLabel("Keywords:"));
        searchRow.add(searchField);
        searchRow.add(new JLabel("Category:"));
        searchRow.add(categoryCombo);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        searchRow.add(searchButton);
        northPanel.add(searchRow, BorderLayout.CENTER);

        add(northPanel, BorderLayout.NORTH);

        // 3. Center Container (Results Table inside JScrollPane)
        // BorderLayout.CENTER expands in both X and Y dimensions as window resizes
        String[] columnNames = {"Book ID", "Title", "Author", "Category", "Status", "Rack"};
        Object[][] data = {
            {"CS-101", "Introduction to Algorithms (CLRS)", "Cormen et al.", "Computer Science", "Available", "R-04"},
            {"CS-204", "Operating System Concepts", "Silberschatz", "Computer Science", "Available", "R-02"},
            {"CS-309", "Java: The Complete Reference", "Herbert Schildt", "Computer Science", "Issued", "R-05"},
            {"MA-102", "Higher Engineering Mathematics", "B.S. Grewal", "Mathematics", "Available", "R-11"},
            {"EE-201", "Electric Circuits", "James Nilsson", "Electrical Engg", "Available", "R-08"},
            {"CS-405", "Database System Concepts", "Korth & Sudarshan", "Computer Science", "Available", "R-03"},
            {"CS-412", "Computer Networks: A Top-Down Approach", "Kurose & Ross", "Computer Science", "Issued", "R-06"},
            {"CS-501", "Design Patterns: Elements of Reusable OO Software", "Gang of Four", "Computer Science", "Available", "R-01"}
        };
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        resultsTable = new JTable(tableModel);
        resultsTable.setRowHeight(24);
        resultsTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        add(scrollPane, BorderLayout.CENTER);

        // 4. South Container (Navigation/Action Buttons + Status Label)
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBorder(BorderFactory.createEmptyBorder(4, 10, 8, 10));

        // Button row keeps preferred button heights and natural flow
        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 4));
        buttonRow.add(prevButton);
        buttonRow.add(nextButton);
        reserveButton.setBackground(new Color(39, 174, 96));
        buttonRow.add(reserveButton);
        southPanel.add(buttonRow, BorderLayout.NORTH);

        // Status Label at bottom
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        southPanel.add(statusLabel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

        // Action Handlers
        searchButton.addActionListener(e -> {
            String query = searchField.getText().trim();
            statusLabel.setText(" Showing results for query: '" + (query.isEmpty() ? "*" : query) + "' (" + tableModel.getRowCount() + " records listed)");
        });

        reserveButton.addActionListener(e -> {
            int row = resultsTable.getSelectedRow();
            if (row >= 0) {
                String title = (String) tableModel.getValueAt(row, 1);
                String status = (String) tableModel.getValueAt(row, 4);
                if ("Available".equalsIgnoreCase(status)) {
                    tableModel.setValueAt("Reserved", row, 4);
                    statusLabel.setText(" Reserved: '" + title + "'. Please collect from librarian.");
                    JOptionPane.showMessageDialog(this, "Book '" + title + "' reserved successfully!", "Reservation Confirmation", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Book is currently '" + status + "' and cannot be reserved.", "Reservation Error", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a book row from the table first.", "Selection Needed", JOptionPane.WARNING_MESSAGE);
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(750, 450);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibrarySearchFrame().setVisible(true));
    }
}
