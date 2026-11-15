package task05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Style (a): The Frame itself implements ActionListener.
 */
public class ConverterStyleA_FrameListener extends JFrame implements ActionListener {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    private final JButton convertButton = new JButton("Convert");

    public ConverterStyleA_FrameListener() {
        super("Currency Converter - Style A (Frame Listener)");
        setLayout(new FlowLayout());
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);

        // Frame is passed as listener
        convertButton.addActionListener(this);
        amount.addActionListener(this); // Task 5.2 requirement

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        pack();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Dispatches based on source or command
        if (e.getSource() == convertButton || e.getSource() == amount) {
            convert();
        }
    }

    private void convert() {
        try {
            double val = Double.parseDouble(amount.getText().trim());
            String currency = (String) target.getSelectedItem();
            double rate = currency.equals("USD") ? 0.012 : 0.011;
            double converted = val * rate;
            result.setText(String.format("%.2f %s", converted, currency));
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
        }
    }
}
