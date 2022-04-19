package se.cha.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFrame extends JFrame {

    protected BufferedImage image = null;
    protected JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    public ImageFrame(String title) {
        super(title);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();

        pack();
    }

    public void centerFrame() {
        setLocationRelativeTo(null);
    }

    public void centerImage() {
        final JScrollBar hbar = scrollPane.getHorizontalScrollBar();
        final JScrollBar vbar = scrollPane.getVerticalScrollBar();
        hbar.setValue(((hbar.getMaximum() - hbar.getMinimum()) / 2) + hbar.getMinimum());
        vbar.setValue(((vbar.getMaximum() - vbar.getMinimum()) / 2) + vbar.getMinimum());
    }

    protected void initUI() {
        add(scrollPane);
    }


    public void setImage(BufferedImage image) {
        this.image = image;

        final ImageIcon imageIcon = new ImageIcon(image);
        final JLabel label = new JLabel(imageIcon);
        scrollPane.setViewportView(label);

        resizeFrame();
    }

    public BufferedImage getImage() {
        return image;
    }

    protected void resizeFrame() {
        // Make sure window fit within desktop area
        final Rectangle maxWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final int width = getWidth();
        final int height = getHeight();
        setSize(Math.min(width, maxWindowBounds.width), Math.min(height, maxWindowBounds.height));
    }
}
