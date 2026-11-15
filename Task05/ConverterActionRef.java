package task05;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * Alternative Concept for Task 5:
 * Shared Action object (AbstractAction) and Method Reference (this::convert).
 * One single Action object provides text, icon, mnemonic, tooltip, and execution logic,
 * binding seamlessly to JButton, JMenuItem, and KeyStroke accelerators.
 */
public class ConverterActionRef extends JFrame {
    private final JTextField amount = new JTextField(10);
    private final JComboBox<String> target = new JComboBox<>(new String[]{"USD", "EUR"});
    private final JLabel result = new JLabel(" ");

    // Shared Action instance encapsulating both state and behaviour
    private final Action convertAction = new AbstractAction("Convert") {
        {
            putValue(Action.SHORT_DESCRIPTION, "Convert currency (Ctrl+Enter)");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control ENTER"));
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            convert();
        }
    };

    public ConverterActionRef() {
        super("Currency Converter - Shared Action & Method Ref");
        setLayout(new FlowLayout());

        // Setup MenuBar demonstrating cross-component reuse of single Action object
        JMenuBar menuBar = new JMenuBar();
        JMenu actionMenu = new JMenu("Actions");
        actionMenu.add(new JMenuItem(convertAction)); // Reuses same Action!
        menuBar.add(actionMenu);
        setJMenuBar(menuBar);

        JButton convertButton = new JButton(convertAction); // Reuses same Action!

        add(new JLabel("INR"));
        add(amount);
        add(target);
        add(convertButton);
        add(result);

        // Bind Enter key in text field directly via method reference or shared action
        amount.addActionListener(this::handleActionEvent);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    private void handleActionEvent(ActionEvent e) {
        convert();
    }

    private void convert() {
        try {
            double val = Double.parseDouble(amount.getText().trim());
            String currency = (String) target.getSelectedItem();
            double rate = "USD".equals(currency) ? 0.012 : 0.011;
            result.setText(String.format("%.2f %s", val * rate, currency));
        } catch (NumberFormatException ex) {
            result.setText("Invalid amount");
        }
    }
}
