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

    /**
     * @param x The X position of the component
     * @param y The Y position of the component
     * @param width The width of the component
     * @param height The height of the component
     */
    public CanvasComponent(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the X position of this component
     *
     * @return The X position of this component
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the Y position of this component
     *
     * @return The Y position of this component
     */
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

    /**
     * Returns the width of this component
     *
     * @return This component's width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height of this component
     *
     * @return This component's height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns whether this component resides in the area described by
     * the cartesian coordinates (x, y)
     *
     * @param x The X position to check
     * @param y The Y position to check
     * @return Whether this component occupies this location
     */
    public boolean contains(int x, int y) {
        return this.x <= x && x < this.x + width
            && this.y <= y && y < this.y + height;
    }


    // ----- Visibility -----

    /**
     * Returns whether this component is drawn ("visible")
     *
     * @return Whether this component is visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the component's visibility
     *
     * @param visible Whether this component should be drawn ("visible")
     */
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

    /**
     * Returns whether this component needs to be repainted
     *
     * @return Whether this component needs to be repainted
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Marks this component and all parent components up the stack as
     * needing to be redrawn
     */
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

    /**
     * Paints this component with the provided {@link com.eueln.canvasapi.CanvasGraphics}
     * @param g The graphics to use while drawing
     */
    public abstract void paint(CanvasGraphics g);


    // ----- Event handling

    /**
     * Adds an interact listener to this component
     *
     * @param listener The interact listener to add
     */
    public void addInteractListener(InteractListener listener) {
        interactListeners.add(listener);
    }

    /**
     * Fires a click event on this component
     *
     * @param canvas The canvas involved in the event
     * @param player The player involved in the event
     * @param x The X position of the event
     * @param y The Y position of the event
     */
    public void fireClickEvent(Canvas canvas, Player player, int x, int y) {
        if (contains(x, y)) {
            for (InteractListener listener : interactListeners) {
                listener.onClick(canvas, player, x, y);
            }
        }
    }

    /**
     * Fires a hover event on this component
     *
     * @param canvas The canvas involved in the event
     * @param player The player involved in the event
     * @param x The X position of the event
     * @param y The Y position of the event
     */
    public void fireHoverEvent(Canvas canvas, Player player, int x, int y) {
        if (contains(x, y)) {
            for (InteractListener listener : interactListeners) {
                listener.onHover(canvas, player, x, y);
            }
        }
    }
}
