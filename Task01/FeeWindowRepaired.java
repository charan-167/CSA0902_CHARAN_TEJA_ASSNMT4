package task01;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * REPAIRED DESIGN (Task 1.2)
 * Demonstrates separation of presentation and business logic.
 * Window only collects input and displays output; background processing
 * uses SwingWorker to avoid freezing the Event Dispatch Thread (EDT).
 */
public class FeeWindowRepaired extends JFrame {
    private final JTextField regNoField = new JTextField(15);
    private final JTextField amountField = new JTextField(15);
    private final JButton payButton = new JButton("Pay Fee");
    private final JButton clearButton = new JButton("Clear");
    private final JButton receiptButton = new JButton("Generate Receipt");
    private final JLabel statusLabel = new JLabel("Ready", SwingConstants.CENTER);
    private final JProgressBar progressBar = new JProgressBar();

    private final FeeService feeService;
    private String lastTransactionId = null;

    public FeeWindowRepaired(FeeService feeService) {
        super("Student Fee Payment System - Repaired Design");
        this.feeService = feeService;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout(10, 10));

        // Form Panel
        JPanel formPanel = new JPanel(new GridLayout(2, 2, 8, 8));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 5, 15));
        formPanel.add(new JLabel("Register Number:"));
        formPanel.add(regNoField);
        formPanel.add(new JLabel("Fee Amount (INR):"));
        formPanel.add(amountField);
        add(formPanel, BorderLayout.NORTH);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(payButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(receiptButton);
        add(buttonPanel, BorderLayout.CENTER);

        // Status & Progress Panel
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 10, 15));
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        bottomPanel.add(progressBar, BorderLayout.NORTH);
        statusLabel.setForeground(new Color(0, 70, 140));
        bottomPanel.add(statusLabel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        // Event Handling with clean, separate handlers
        payButton.addActionListener(e -> onPayClicked());
        clearButton.addActionListener(e -> onClearClicked());
        receiptButton.addActionListener(e -> onReceiptClicked());

        // Proper Window Closing
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    FeeWindowRepaired.this,
                    "Are you sure you want to close the Fee Payment window?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private void onPayClicked() {
        String regNo = regNoField.getText();
        String amount = amountField.getText();

        // Immediate quick UI pre-validation
        if (regNo.trim().isEmpty() || amount.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in both Register Number and Amount.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Disable buttons and show progress bar so user knows processing is ongoing
        setUiBusy(true, "Contacting payment gateway and verifying bank confirmation...");

        // Offload long-running task to SwingWorker to keep EDT 100% responsive
        SwingWorker<PaymentResult, Void> worker = new SwingWorker<>() {
            @Override
            protected PaymentResult doInBackground() {
                return feeService.processPayment(regNo, amount);
            }

            @Override
            protected void done() {
                setUiBusy(false, "Ready");
                try {
                    PaymentResult result = get();
                    if (result.isSuccess()) {
                        lastTransactionId = result.getTransactionId();
                        statusLabel.setText("Success: " + result.getMessage());
                        JOptionPane.showMessageDialog(FeeWindowRepaired.this,
                            result.getMessage() + "\nTransaction ID: " + lastTransactionId,
                            "Payment Successful",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        statusLabel.setText("Failed: " + result.getMessage());
                        JOptionPane.showMessageDialog(FeeWindowRepaired.this,
                            result.getMessage(),
                            "Payment Failed",
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Unexpected error occurred.");
                    JOptionPane.showMessageDialog(FeeWindowRepaired.this,
                        "Error: " + ex.getMessage(),
                        "Execution Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void onClearClicked() {
        regNoField.setText("");
        amountField.setText("");
        statusLabel.setText("Ready - Fields cleared");
        lastTransactionId = null;
    }

    private void onReceiptClicked() {
        String regNo = regNoField.getText();
        String amount = amountField.getText();
        if (lastTransactionId == null) {
            JOptionPane.showMessageDialog(this,
                "No successful payment found to generate receipt.\nPlease complete a fee payment first.",
                "Receipt Unavailable",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        String receiptText = feeService.generateReceipt(regNo, amount, lastTransactionId);
        JTextArea receiptArea = new JTextArea(receiptText, 8, 30);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(receiptArea), "Official Fee Receipt", JOptionPane.INFORMATION_MESSAGE);
    }

    private void setUiBusy(boolean busy, String statusMessage) {
        payButton.setEnabled(!busy);
        clearButton.setEnabled(!busy);
        receiptButton.setEnabled(!busy);
        regNoField.setEditable(!busy);
        amountField.setEditable(!busy);
        progressBar.setVisible(busy);
        statusLabel.setText(statusMessage);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FeeService service = new FeeService("jdbc:h2:mem:feedb;DB_CLOSE_DELAY=-1", "sa", "");
            new FeeWindowRepaired(service).setVisible(true);
        });
    }
}
