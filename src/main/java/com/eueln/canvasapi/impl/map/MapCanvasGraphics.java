package com.eueln.canvasapi.impl.map;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasGraphics;
import com.eueln.canvasapi.impl.CanvasAPI;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

public class MapCanvasGraphics implements CanvasGraphics {
    private final Location loc;
    private final BlockFace face;
    private final int blocksWidth;
    private final int blocksHeight;
    private Canvas canvas;

    private final MapCanvasSection[] sections;

    public MapCanvasGraphics(Location loc, BlockFace face, int blocksWidth, int blocksHeight) {
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
    }

    public Location getLocation() {
        return loc.clone();
    }

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

    @Override
    public Canvas getCanvas() {
        return canvas;
    }

    // ----- Drawing Functions -----

    @Override
    public void drawRect(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int k = y; k < y + height; k++) {
                setPixel(i, k, (byte)38); // TODO: colors
            }
        }
    }

    @Override
    public void drawString(String str, int x, int y) {
        MapFont font = MinecraftFont.Font;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            MapFont.CharacterSprite sprite = font.getChar(ch);
            for (int row = 0; row < sprite.getHeight(); row++) {
                for (int column = 0; column < sprite.getWidth(); column++) {
                    if (sprite.get(row, column)) {
                        setPixel(x + column, y + row, (byte)38);
                    }
                }
            }
            x += sprite.getWidth() + 1;
        }
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


    // ----- Public property getters -----

    @Override
    public int getWidth() {
        return blocksWidth * 128;
    }

    @Override
    public int getHeight() {
        return blocksHeight * 128;
    }


    // ----- Called by Canvas -----

    @Override
    public void register(Canvas canvas) {
        if (this.canvas != null) {
            throw new IllegalStateException("Canvas already set");
        }
        CanvasAPI.instance().getMapManager().register(this);
        this.canvas = canvas;
    }

    @Override
    public void showTo(Player player) {
        CanvasAPI.instance().getMapManager().showTo(player, this);
    }

    @Override
    public void showToAll() {
        CanvasAPI.instance().getMapManager().showToAll(this);
    }

    @Override
    public void hideFrom(Player player) {
        CanvasAPI.instance().getMapManager().hideFrom(player, this);
    }


    // ----- Implementation details -----

    private void initSections() {
        for (int y = 0; y < blocksHeight; y++) {
            for (int x = 0; x < blocksWidth; x++) {
                sections[x + (y * blocksWidth)] = new MapCanvasSection(x, y);
            }
        }
    }

    protected void repaint() {
        canvas.paint(this);
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
