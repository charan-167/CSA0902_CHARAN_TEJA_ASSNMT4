package task04;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Label;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

/**
 * Alternative Concept for Task 4: Off-screen BufferedImage caching.
 * Rather than retaining O(n) Point objects and redrawing every point in O(n) time,
 * this version paints each dot directly into an off-screen BufferedImage once (O(1)),
 * and paint(Graphics) simply blits the pre-rendered image in O(1) time.
 */
public class DrawPadOffscreenImage extends Frame {
    private int clickCount = 0;
    private final Label status = new Label("Clicks: 0");
    private BufferedImage canvasBuffer;
    private Graphics2D bufferGraphics;

    public DrawPadOffscreenImage() {
        super("Draw Pad - Offscreen Image Buffer");
        add(status, BorderLayout.SOUTH);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ensureBufferExists();
                clickCount++;
                status.setText("Clicks: " + clickCount);

                // Draw dot once onto the persistent off-screen raster
                bufferGraphics.setColor(new Color(46, 204, 113));
                bufferGraphics.fillOval(e.getX() - 3, e.getY() - 3, 6, 6);

                // Blit updated buffer to screen
                repaint();
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { dispose(); }
        });

        setSize(400, 300);
    }

    private void ensureBufferExists() {
        if (canvasBuffer == null || canvasBuffer.getWidth() != getWidth() || canvasBuffer.getHeight() != getHeight()) {
            BufferedImage newBuffer = new BufferedImage(Math.max(1, getWidth()), Math.max(1, getHeight()), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = newBuffer.createGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, newBuffer.getWidth(), newBuffer.getHeight());
            if (canvasBuffer != null) {
                g2.drawImage(canvasBuffer, 0, 0, null);
                bufferGraphics.dispose();
            }
            canvasBuffer = newBuffer;
            bufferGraphics = g2;
            bufferGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        ensureBufferExists();
        // Blit pre-rendered buffer to screen in constant O(1) time
        g.drawImage(canvasBuffer, 0, 0, null);
    }

    public static void main(String[] args) {
        new DrawPadOffscreenImage().setVisible(true);
    }
}
