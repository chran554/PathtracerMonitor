package se.cha.frame;

import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage image = null;

    public ImagePanel() {
        setMinimumSize(new Dimension(400, 300));
        setPreferredSize(new Dimension(400, 300));
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        if (image != null) {
            setMinimumSize(new Dimension(image.getWidth(), image.getHeight()));
            setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        }
        this.image = image;

        repaint();
    }

    @Override
    public void paint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;

        final int width = graphics.getClipBounds().width;
        final int height = graphics.getClipBounds().height;

        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, width - 1, height - 1);

        if (image != null) {
            g.drawImage(image, 0,0, null);
        }

        g.dispose();
    }
}
