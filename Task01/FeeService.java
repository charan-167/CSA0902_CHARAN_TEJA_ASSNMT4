package task01;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * FeeService encapsulates business validation and database persistence for fee payments.
 * It separates business and data logic from the presentation layer (FeeWindow).
 */
public class FeeService {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public FeeService(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    /**
     * Validates input fields and executes payment asynchronously or synchronously.
     * Uses PreparedStatement to prevent SQL injection and try-with-resources to prevent leaks.
     */
    public PaymentResult processPayment(String regNo, String amountStr) {
        // 1. Validation
        if (regNo == null || regNo.trim().isEmpty()) {
            return new PaymentResult(false, "Validation Error: Register Number cannot be empty.", null);
        }
        String cleanRegNo = regNo.trim().toUpperCase();

        if (amountStr == null || amountStr.trim().isEmpty()) {
            return new PaymentResult(false, "Validation Error: Amount cannot be empty.", null);
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr.trim());
            if (amount <= 0) {
                return new PaymentResult(false, "Validation Error: Amount must be greater than zero.", null);
            }
        } catch (NumberFormatException e) {
            return new PaymentResult(false, "Validation Error: Invalid numeric format for amount.", null);
        }

        // 2. Simulated payment gateway confirmation delay (mocking bank confirmation)
        try {
            Thread.sleep(1500); // executed on worker thread, never on EDT
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return new PaymentResult(false, "Payment interrupted during bank processing.", null);
        }

        // 3. Database Persistence using PreparedStatement
        // For demonstration, if DB connection fails, we report gracefully without crashing
        String sql = "INSERT INTO fees (reg_no, amount, payment_date) VALUES (?, ?, CURRENT_TIMESTAMP)";
        try {
            if (dbUrl != null && !dbUrl.isEmpty()) {
                try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                     PreparedStatement pstmt = conn.prepareStatement(sql)) {
                    pstmt.setString(1, cleanRegNo);
                    pstmt.setDouble(2, amount);
                    pstmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            // In a production system, log exception; here return actionable message
            return new PaymentResult(false, "Database Error: " + e.getMessage(), null);
        }

        String transactionId = "TXN" + System.currentTimeMillis();
        return new PaymentResult(true, "Payment of INR " + String.format("%.2f", amount) + " successful for " + cleanRegNo, transactionId);
    }

    public String generateReceipt(String regNo, String amountStr, String txnId) {
        if (regNo == null || regNo.trim().isEmpty()) {
            return "Cannot generate receipt: No valid registration number provided.";
        }
        return "====================================\n"
             + "        OFFICIAL FEE RECEIPT        \n"
             + "====================================\n"
             + "Transaction ID : " + (txnId != null ? txnId : "N/A (Pending)") + "\n"
             + "Student Reg No : " + regNo.trim().toUpperCase() + "\n"
             + "Amount Paid    : INR " + amountStr + "\n"
             + "Status         : CONFIRMED\n"
             + "Date           : " + new java.util.Date() + "\n"
             + "====================================";
    }
}
