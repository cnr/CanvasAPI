package com.eueln.canvasapi;

import com.eueln.canvasapi.event.InteractListener;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CanvasComponent {
    private int x;
    private int y;
    private int width;
    private int height;

    private CanvasContainer parent;
    private boolean visible = true;
    private boolean valid = true;

    private final List<InteractListener> interactListeners = new ArrayList<>();

    public CanvasComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean contains(int x, int y) {
        return this.x <= x && x < this.x + width
            && this.y <= y && y < this.y + height;
    }

    public CanvasComponent getComponentAt(int x, int y) {
        return contains(x, y) ? this : null;
    }


    // ----- Visibility -----

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        invalidate();
    }


    // ----- Component hierarchy -----
    protected void setParent(CanvasContainer parent) {
        if (this.parent != null) {
            this.parent.remove(this);
        }

        this.parent = parent;
    }

    protected void removeParent() {
        this.parent = null;
    }


    // ----- Paint-triggering
    protected boolean isValid() {
        return valid;
    }

    public void invalidate() {
        valid = false;
        if (parent != null && parent.isValid()) {
            parent.invalidate();
        }
    }

    public abstract void paint(CanvasGraphics g);


    // ----- Event handling

    public void addInteractListener(InteractListener listener) {
        interactListeners.add(listener);
    }

    public void fireClickEvent(Canvas canvas, Player player, int x, int y) {
        if (contains(x, y)) {
            for (InteractListener listener : interactListeners) {
                listener.onClick(canvas, player, x, y);
            }
        }
    }

    public void fireHoverEvent(Canvas canvas, Player player, int x, int y) {
        if (contains(x, y)) {
            for (InteractListener listener : interactListeners) {
                listener.onHover(canvas, player, x, y);
            }
        }
    }
}
