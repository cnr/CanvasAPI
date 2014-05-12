package com.eueln.canvasapi.impl.map;

class MapCanvasSection {
    private static final int START_ID = 0x4000;
    private static int currentMapId = START_ID;

    private final int mapId = currentMapId++;
    private final int x;
    private final int y;
    private final byte[][] data = new byte[128][128];

    private boolean changes = false;

    MapCanvasSection(int x, int y) {
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
        if (data[x][y] != value) {
            data[x][y] = value;
            changes = true;
        }
    }

    public boolean hasChanges() {
        return changes;
    }

    public void clearChanges() {
        changes = false;
    }

    public byte[][] getData() {
        return data;
    }
}
