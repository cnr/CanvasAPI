package com.eueln.canvasapi;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public abstract class Canvas extends CanvasContainer {
    private final Location loc;
    private final BlockFace face;

    private final Set<Player> canSee = new HashSet<>();

    protected Canvas(Location loc, BlockFace face, int width, int height) {
        super(0, 0, width, height);
        this.loc = loc;
        this.face = face;
    }

    public void setVisible(Player player, boolean visible) {
        if (visible) {
            canSee.add(player);
        } else {
            canSee.remove(player);
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

    public boolean isVisibleToAll() {
        return super.isVisible();
    }

    public Location getLocation() {
        return loc.clone(); // defensively copy
    }

    public BlockFace getBlockFace() {
        return face;
    }
}
