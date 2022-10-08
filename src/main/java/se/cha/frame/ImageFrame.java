package se.cha.frame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageFrame extends JFrame {

    protected JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    protected ImagePanel imagePanel = new ImagePanel();

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
        scrollPane.setViewportView(imagePanel);
        add(scrollPane);
    }

    public void setImage(BufferedImage image) {
        imagePanel.setImage(image);
        resizeFrame();
    }

    public BufferedImage getImage() {
        return imagePanel.getImage();
    }

    protected void resizeFrame() {
        // Make sure window fit within desktop area
        final Rectangle maxWindowBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        final int width = getWidth();
        final int height = getHeight();
        setSize(Math.min(width, maxWindowBounds.width), Math.min(height, maxWindowBounds.height));
    }
}
