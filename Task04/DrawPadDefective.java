package task04;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Defective DrawPad as initially presented in Task 4.
 */
public class DrawPadDefective extends Frame {
    int count = 0;
    Label status = new Label("Clicks: 0");

    public DrawPadDefective() {
        super("Defective DrawPad");
        add(status, BorderLayout.SOUTH);
        addMouseListener(new MouseAdapter() {
            // DEFECT: 'c' is lowercase: mouseclicked instead of mouseClicked.
            // Never called by AWT event dispatch!
            public void mouseclicked(MouseEvent e) {
                count++;
                Graphics g = getGraphics();
                g.fillOval(e.getX(), e.getY(), 6, 6);
            }
        });
        setSize(400, 300);
    }
}
