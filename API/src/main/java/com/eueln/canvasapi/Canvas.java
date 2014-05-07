package com.eueln.canvasapi;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public abstract class Canvas extends MContainer {
    private final Location loc;
    private final BlockFace face;

    protected Canvas(Location loc, BlockFace face, int width, int height) {
        super(0, 0, width, height);
        this.loc = loc;
        this.face = face;
    }

    public abstract void setVisible(Player player, boolean visible);
    public abstract boolean isVisible(Player player);

    public boolean isVisibleToAll() {
        return super.isVisible();
    }
}
