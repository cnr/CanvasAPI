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

    public void repaint() {
        paint(graphics);
    }

    public CanvasBackend getBackend() {
        return backend;
    }

    // ----- Player visibility -----

    public void setVisible(Player player, boolean visible) {
        if (visible) {
            canSee.add(player);
            CanvasManager.show(player, this);
        } else {
            canSee.remove(player);
            CanvasManager.hide(player, this);
        }
    }

    public boolean isVisible(Player player) {
        return canSee.contains(player);
    }

    public Set<Player> getVisibleTo() {
        Set<Player> ret = new HashSet<>(); // Defensively copying.. this probably isn't the best way to do this.
        ret.addAll(canSee);
        return ret;
    }

    // We're treating CanvasComponent's setVisible as a `visible to all` option
    // for Canvas
    @Override
    public void setVisible(boolean visibleToAll) {
        super.setVisible(visibleToAll);
        if (visibleToAll) {
            CanvasManager.showAll(this);
        } else {
            CanvasManager.hideAll(this);
        }
    }

    public boolean isVisibleToAll() {
        return super.isVisible();
    }


    // ----- Events and listeners -----

    public void fireClickEvent(Player player, int x, int y) {
        fireClickEvent(this, player, x, y);
    }

    public void fireHoverEvent(Player player, int x, int y) {
        fireHoverEvent(this, player, x, y);
    }
}
