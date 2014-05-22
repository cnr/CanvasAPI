package com.eueln.canvasapi.components;

import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;

import java.awt.image.BufferedImage;

public class ImageComponent extends CanvasComponent {
    private final BufferedImage image;

    public ImageComponent(int x, int y, BufferedImage image) {
        super(x, y, image.getWidth(), image.getHeight());
        this.image = image;
    }

    @Override
    public void paint(CanvasGraphics g) {
        g.drawImage(image, getX(), getY());
    }
}
