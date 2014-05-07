package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.Canvas;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class MapCanvas extends Canvas {
    private final int blocksWidth;
    private final int blocksHeight;
    private final CanvasSection[] sections;

    protected MapCanvas(Location loc, BlockFace face, int blocksWidth, int blocksHeight) {
        super(loc, face, blocksWidth, blocksHeight);

        this.blocksWidth = blocksWidth;
        this.blocksHeight = blocksHeight;

        sections = new CanvasSection[blocksWidth * blocksHeight];
        initSections();
    }

    @Override
    public void setVisible(Player player, boolean visible) {
        // TODO
    }

    @Override
    public boolean isVisible(Player player) {
        // TODO
        return false;
    }

    private void initSections() {
        for (int y = 0; y < blocksHeight; y++) {
            for (int x = 0; x < blocksWidth; x++) {
                sections[x + (y * blocksWidth)] = new CanvasSection(x, y);
            }
        }
    }

    protected CanvasSection[] getSections() {
        return sections;
    }

    private static class CanvasSection {
        private static final int START_ID = 0x4000;
        private static int currentMapId = START_ID;

        private final int mapId = currentMapId++;
        private final int x;
        private final int y;
        private final byte[][] contents = new byte[128][128];

        private boolean changes = false;

        CanvasSection(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public int getMapId() {
            return mapId;
        }

        public void set(int x, int y, byte value) {
            if (contents[x][y] != value) {
                contents[x][y] = value;
                changes = true;
            }
        }

        public boolean hasChanges() {
            return changes;
        }

        public void clearChanges() {
            changes = false;
        }

        public byte[][] getContents() {
            return contents; // TODO: defensively copy
        }
    }

}
