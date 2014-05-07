package com.eueln.canvasapi;

import com.eueln.canvasapi.graphics.MGraphics;

public abstract class MComponent {
    private int x;
    private int y;
    private int width;
    private int height;

    private MContainer parent;
    private boolean visible = true;
    private boolean valid = true;

    public MComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean contains(int x, int y) {
        return this.x <= x && x < this.x + width
            && this.y <= y && y < this.y + height;
    }

    public MComponent getComponentAt(int x, int y) {
        return contains(x, y) ? this : null;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        invalidate();
    }

    protected void setParent(MContainer parent) {
        if (this.parent != null) {
            this.parent.remove(this);
        }

        this.parent = parent;
    }

    protected void removeParent() {
        this.parent = null;
    }

    protected boolean isValid() {
        return valid;
    }

    public void invalidate() {
        valid = false;
        if (parent.isValid()) {
            parent.invalidate();
        }
    }

    public abstract void paint(MGraphics g);
}
