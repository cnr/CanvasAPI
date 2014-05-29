package com.eueln.canvasapi.components;

import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A static image component with optional initial scaling
 */
public class ImageComponent extends CanvasComponent {
    private final BufferedImage image;

    /**
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param image The image to be used (with width and height being
     *              derived from the image)
     */
    public ImageComponent(int x, int y, BufferedImage image) {
        this(x, y, image, image.getWidth(), image.getHeight());
    }

    /**
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param image The image to be used
     * @param width The width to scale the image to
     * @param height The height to scale the image to
     */
    public ImageComponent(int x, int y, BufferedImage image, int width, int height) {
        super(x, y, width, height);

        // Scale the image if necessary
        if (image.getWidth() != width || image.getHeight() != height) {
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.drawImage(scaled, 0, 0, null);
            g.dispose();
        }

        this.image = image;
    }

    @Override
    public void paint(CanvasGraphics g) {
        g.drawImage(image, getX(), getY());
    }
}
