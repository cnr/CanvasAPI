package com.eueln.canvasapi;

import com.eueln.canvasapi.impl.CanvasManager;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Canvas extends CanvasContainer {
    private final CanvasBackend backend;
    private final CanvasGraphics graphics;

    private final Set<Player> canSee = new HashSet<>();

    public Canvas(CanvasBackend backend) {
        this(backend, new CanvasGraphics(backend));
    }

    private Canvas(CanvasBackend backend, CanvasGraphics graphics) {
        super(0, 0, graphics.getWidth(), graphics.getHeight());
        this.backend = backend;
        this.graphics = graphics;
        CanvasManager.register(this);
    }

    /**
     * Forces this canvas to repaint immediately
     */
    public void repaint() {
        doPaint(graphics);
        backend.update(this);
    }

    /**
     * Returns the {@link com.eueln.canvasapi.CanvasBackend} associated
     * with this Canvas
     *
     * @return The backend belonging to this canvas
     */
    public CanvasBackend getBackend() {
        return backend;
    }

    // ----- Player visibility -----

    /**
     * Sets this canvas as visible to the player
     *
     * @param player The player whose visibility you want to modify
     * @param visible The visibility of this canvas to the player
     */
    public void setVisible(Player player, boolean visible) {
        if (visible) {
            canSee.add(player);
            CanvasManager.show(player, this);
        } else {
            canSee.remove(player);
            CanvasManager.hide(player, this);
        }
    }

    /**
     * Returns whether this canvas is specifically visible to the
     * player. NOTE: THIS DOES NOT ACCOUNT FOR GLOBAL VISIBILITY. ONLY
     * VISIBILITY AS SPECIFIED BY {@link com.eueln.canvasapi.Canvas#setVisible(org.bukkit.entity.Player, boolean)}
     * IS REFLECTED IN THIS RETURNED VALUE
     *
     * @param player The player for whom you want to check this canvas's visibility
     * @return Whether the player is able to see this canvas
     */
    public boolean isVisible(Player player) {
        return canSee.contains(player);
    }

    /**
     * Returns a copy of the set of players this canvas is visible to
     *
     * @return The set of players this canvas is visible to
     */
    public Set<Player> getVisibleTo() {
        Set<Player> ret = new HashSet<>(); // Defensively copying.. this probably isn't the best way to do this.
        ret.addAll(canSee);
        return ret;
    }

    // We're treating CanvasComponent's setVisible as a `visible to all` option
    // for Canvas

    /**
     * Toggles the global visibility of this canvas
     *
     * @param visibleToAll Whether this canvas should be visible to all
     *                     players
     */
    @Override
    public void setVisible(boolean visibleToAll) {
        super.setVisible(visibleToAll);
        if (visibleToAll) {
            CanvasManager.showAll(this);
        } else {
            CanvasManager.hideAll(this);
        }
    }

    /**
     * Returns whether this canvas is shown to all players
     *
     * @return Whether this canvas is shown to all players
     */
    public boolean isVisibleToAll() {
        return super.isVisible();
    }


    // ----- Events and listeners -----

    /**
     * Fire a click event for this canvas
     *
     * @param player The player involved in the event
     * @param x The X position of the event
     * @param y The Y position of the event
     */
    public void fireClickEvent(Player player, int x, int y) {
        fireClickEvent(this, player, x, y);
    }

    /**
     * Fire a hover event for this canvas
     *
     * @param player The player involved in the event
     * @param x The X position of the event
     * @param y The Y position of the event
     */
    public void fireHoverEvent(Player player, int x, int y) {
        fireHoverEvent(this, player, x, y);
    }
}
