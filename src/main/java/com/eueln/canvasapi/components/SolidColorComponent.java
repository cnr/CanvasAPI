package com.eueln.canvasapi.components;

import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;

public class SolidColorComponent extends CanvasComponent {
    private byte color;

    public SolidColorComponent(int x, int y, int width, int height, byte color) {
        super(x, y, width, height);

        this.color = color;
    }

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
