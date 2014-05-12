package com.eueln.canvasapi;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Canvas extends CanvasContainer {
    private final CanvasGraphics graphics;

    private final Set<Player> canSee = new HashSet<>();

    public Canvas(CanvasGraphics graphicsProvider) {
        super(0, 0, graphicsProvider.getWidth(), graphicsProvider.getHeight());
        this.graphics = graphicsProvider;
        graphicsProvider.register(this);
    }


    // ----- Player visibility -----

    public void setVisible(Player player, boolean visible) {
        if (visible) {
            canSee.add(player);
            graphics.showTo(player);
        } else {
            canSee.remove(player);
            graphics.hideFrom(player);
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
        super.setVisible(true);
        graphics.showToAll();
    }

    public boolean isVisibleToAll() {
        return super.isVisible();
    }


    // ----- Events and listeners -----

    public void fireInteractEvent(Player player, int x, int y) {
        fireInteractEvent(this, player, x, y);
    }
}
