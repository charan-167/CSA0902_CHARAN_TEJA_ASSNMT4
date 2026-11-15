package task05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Style (c): Anonymous Inner Class implements ActionListener.
 */
public class ConverterStyleC_AnonymousInnerClass extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");
    private final JButton convertButton = new JButton("Convert");

    public ConverterStyleC_AnonymousInnerClass() {
        super("Currency Converter - Style C (Anonymous Inner Class)");
        setLayout(new FlowLayout());
        add(new JLabel("INR")); add(amount); add(target);
        add(convertButton); add(result);

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
        };

        convertButton.addActionListener(listener);
        amount.addActionListener(listener);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });
        pack();
    }
}
