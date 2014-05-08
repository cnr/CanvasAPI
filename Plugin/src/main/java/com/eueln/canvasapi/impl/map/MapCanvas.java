package com.eueln.canvasapi.impl.map;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.graphics.CanvasGraphics;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;

public class MapCanvas extends Canvas implements CanvasGraphics {
    private final int blocksWidth;
    private final int blocksHeight;
    private final CanvasSection[] sections;

    public MapCanvas(Location loc, BlockFace face, int blocksWidth, int blocksHeight) {
        super(loc, face, blocksWidth, blocksHeight);

        this.blocksWidth = blocksWidth;
        this.blocksHeight = blocksHeight;

        sections = new CanvasSection[blocksWidth * blocksHeight];
        initSections();

        MapManager.register(this);
    }

    @Override
    public void paint(CanvasGraphics g) {
        super.paint(g);
    }

    // ----- CanvasGraphics section -----

    @Override
    public void drawString(String str, int x, int y) {

    }

    @Override
    public void drawRect(int x, int y, int width, int height) {

    }

    private void setPixel(int x, int y, byte color) {
        int xSection = x >> 7;
        int ySection = y >> 7;

        if (xSection >= blocksWidth || ySection >= blocksHeight) {
            return;
        }

        int xPixel = x & 0x7F;
        int yPixel = y & 0x7F;

        getSection(xSection, ySection).set(xPixel, yPixel, color);
    }

    // ----- Map canvas partitioning

    private void initSections() {
        for (int y = 0; y < blocksHeight; y++) {
            for (int x = 0; x < blocksWidth; x++) {
                sections[x + (y * blocksWidth)] = new CanvasSection(x, y);
            }
        }
    }

    private CanvasSection getSection(int x, int y) {
        return sections[x + (y * blocksWidth)];
    }

    protected CanvasSection[] getSections() {
        return sections;
    }

    protected static class CanvasSection {
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
            return contents;
        }
    }
}
