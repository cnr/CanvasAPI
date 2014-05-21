package com.eueln.canvasapi.impl.map;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasBackend;
import com.eueln.canvasapi.impl.util.MathUtil;
import net.minecraft.server.v1_7_R3.EntityItemFrame;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapCanvasBackend implements CanvasBackend {
    private final Location loc;
    private final BlockFace face;
    private final int blocksWidth;
    private final int blocksHeight;

    private final MapCanvasSection[] sections;
    private final EntityItemFrame[] frames;

    public MapCanvasBackend(Location loc, BlockFace face, int blocksWidth, int blocksHeight) {
        this.loc = loc;
        // Floor the location values
        loc.setX(loc.getBlockX());
        loc.setY(loc.getBlockY());
        loc.setZ(loc.getBlockZ());

        this.face = face;
        this.blocksWidth = blocksWidth;
        this.blocksHeight = blocksHeight;

        sections = new MapCanvasSection[blocksWidth * blocksHeight];
        initSections();

        frames = FrameUtil.createFrames(this);
    }

    public Location getLocation() {
        return loc.clone();
    }

    @Override
    public World getWorld() {
        return loc.getWorld();
    }

    public BlockFace getBlockFace() {
        return face;
    }

    public int getBlocksWidth() {
        return blocksWidth;
    }

    public int getBlocksHeight() {
        return blocksHeight;
    }


    // ----- CanvasBackend implementation -----

    @Override
    public int getWidth() {
        return blocksWidth * 128;
    }

    @Override
    public int getHeight() {
        return blocksHeight * 128;
    }

    @Override
    public void setPixel(int x, int y, byte color) {
        int xSection = x >> 7;
        int ySection = y >> 7;

        if (xSection >= blocksWidth || ySection >= blocksHeight) {
            return;
        }

        int xPixel = x & 0x7F;
        int yPixel = y & 0x7F;

        getSection(xSection, ySection).set(xPixel, yPixel, color);
    }

    @Override
    public void update(Canvas canvas) {
        for (MapCanvasSection section : sections) {
            if (section.hasChanges()) {
                section.clearChanges();
                resendSection(canvas, section);
            }
        }
    }

    private void resendSection(Canvas canvas, MapCanvasSection section) {
        List<Player> sendSectionTo = new ArrayList<>();

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (canvas.isVisible(player)) {
                // TODO: check if they're in a different world
                // it's going to require lots of juggling.
                sendSectionTo.add(player);
            }
        }

        FrameUtil.sendSection(sendSectionTo, section.getMapId(), section.getData());
    }


    @Override
    public void showTo(Player player) {
        // Spawn the frame
        FrameUtil.spawn(player, frames);

        // Send the maps from each section
        for (MapCanvasSection section : sections) {
            FrameUtil.sendSection(Arrays.asList(player), section.getMapId(), section.getData());
        }
    }

    @Override
    public void hideFrom(Player player) {
        FrameUtil.destroy(player, frames);
    }

    @Override
    public Vector getTouchedPoint(Player player) {
        Vector vector = MathUtil.getTouchedPoint(player, getLocation(), getBlockFace(), getPositiveXFace());
        vector.multiply(128);

        // We're abusing vector for this.
        if (vector.getBlockX() >= 0 && vector.getBlockY() >= 0 && vector.getBlockX() < getWidth() && vector.getBlockY() < getHeight()) {
            return vector;
        }
        return null;
    }


    // ----- Implementation details -----

    private void initSections() {
        for (int y = 0; y < blocksHeight; y++) {
            for (int x = 0; x < blocksWidth; x++) {
                sections[x + (y * blocksWidth)] = new MapCanvasSection(x, y);
            }
        }
    }

    protected MapCanvasSection getSection(int x, int y) {
        return sections[x + (y * blocksWidth)];
    }

    protected MapCanvasSection[] getSections() {
        return sections;
    }

    public BlockFace getPositiveXFace() {
        switch (getBlockFace()) {
            case NORTH:
                return BlockFace.WEST;

            case EAST:
                return BlockFace.NORTH;

            case SOUTH:
                return BlockFace.EAST;

            case WEST:
                return BlockFace.SOUTH;

            default:
                throw new IllegalArgumentException();
        }
    }
}
