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

    /**
     * Returns the absolute X position of this component as relative to
     * the topmost component in the hierarchy
     *
     * @return This component's absolute X position
     */
    public int getAbsoluteX() {
        CanvasContainer target = this.parent;

        int x = getX();
        while (target != null) {
            x += target.getX();
            target = target.getParent();
        }
        return x;
    }

    /**
     * Returns the absolute Y position of this component as relative to
     * the topmost component in the hierarchy
     *
     * @return This component's absolute Y position
     */
    public int getAbsoluteY() {
        CanvasContainer target = this.parent;

        int y = getY();
        while (target != null) {
            y += target.getY();
            target = target.getParent();
        }
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

    protected CanvasContainer getParent() {
        return parent;
    }

    protected void removeParent() {
        this.parent = null;
    }


    // ----- Paint-triggering
    public boolean isValid() {
        return valid;
    }

    public void invalidate() {
        valid = false;
        if (parent != null && parent.isValid()) {
            parent.invalidate();
        }
    }

    protected void doPaint(CanvasGraphics g) {
        paint(g);
        valid = true;
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
