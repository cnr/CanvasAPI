package com.eueln.canvasapi.components;

import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;

/**
 * A solid color component
 */
public class SolidColorComponent extends CanvasComponent {
    private byte color;

    /**
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param width The width of the component
     * @param height The height of the component
     * @param color The color to be used by this component
     */
    public SolidColorComponent(int x, int y, int width, int height, byte color) {
        super(x, y, width, height);

        this.color = color;
    }

    /**
     * Sets the color of this component
     *
     * @param color The color to be used
     */
    public void setColor(byte color) {
        this.color = color;
        invalidate();
    }

    @Override
    public void paint(CanvasGraphics g) {
        g.setColor(color);
        g.drawRect(getX(), getY(), getWidth(), getHeight());
    }
}
