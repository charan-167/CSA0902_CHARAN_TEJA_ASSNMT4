package task05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Task 5.2 Requirement Change:
 * - Enter key pressed in amount field triggers conversion
 * - Result shown in a JOptionPane dialog box
 */
public class ConverterEnhanced extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    private final JButton convertButton = new JButton("Convert");

    public ConverterEnhanced() {
        super("Currency Converter - Enhanced with Dialog");
        setLayout(new FlowLayout());
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);

        // Single shared lambda action
        ActionListener convertAction = e -> performConversion();
        convertButton.addActionListener(convertAction);
        amount.addActionListener(convertAction); // Triggers on Enter key!

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    private void performConversion() {
        String input = amount.getText().trim();
        String currency = (String) target.getSelectedItem();
        try {
            double val = Double.parseDouble(input);
            double rate = "USD".equals(currency) ? 0.012 : 0.011;
            double converted = val * rate;
            String message = String.format("INR %.2f = %.2f %s (Exchange Rate: %.3f)", val, converted, currency, rate);
            result.setText(String.format("%.2f %s", converted, currency));
            JOptionPane.showMessageDialog(this, message, "Conversion Result", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
            JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount!\nGiven input: '" + input + "'", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
