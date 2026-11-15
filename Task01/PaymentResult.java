package task01;

/**
 * Value object representing the outcome of a fee payment operation.
 */
public class PaymentResult {
    private final boolean success;
    private final String message;
    private final String transactionId;

    public PaymentResult(boolean success, String message, String transactionId) {
        this.success = success;
        this.message = message;
        this.transactionId = transactionId;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getTransactionId() {
        return transactionId;
    }
}
