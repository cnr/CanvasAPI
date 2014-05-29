package com.eueln.canvasapi;

import org.bukkit.map.MapFont;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MinecraftFont;

import java.awt.image.BufferedImage;

public class CanvasGraphics {
    private final CanvasBackend backend;

    @SuppressWarnings("deprecation")
    private byte color = MapPalette.WHITE;
    private MapFont font = MinecraftFont.Font;
    private int fontScale = 1;

    private int xOffset;
    private int yOffset;

    public CanvasGraphics(CanvasBackend backend) {
        this.backend = backend;
    }


    // ----- Drawing methods -----

    public void drawRect(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int k = y; k < y + height; k++) {
                setOffsetPixel(i, k, color);
            }
        }
    }

    public void drawString(String str, int x, int y) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            MapFont.CharacterSprite sprite = font.getChar(ch);
            for (int row = 0; row < sprite.getHeight(); row++) {
                for (int column = 0; column < sprite.getWidth(); column++) {
                    if (sprite.get(row, column)) {

                        for (int scaledX = 0; scaledX < fontScale; scaledX++) {
                            for (int scaledY = 0; scaledY < fontScale; scaledY++) {
                                setOffsetPixel(x + (column * fontScale) + scaledX, y + (row * fontScale) + scaledY, color);
                            }
                        }
                    }
                }
            }
            x += (sprite.getWidth() * fontScale) + fontScale;
        }
    }

    @SuppressWarnings("deprecation")
    public void drawImage(BufferedImage image, int startX, int startY) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);

                byte color = MapPalette.matchColor((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                setOffsetPixel(x + startX, y + startY, color);
            }
        }
    }

    private void setOffsetPixel(int x, int y, byte color) {
        x += xOffset;
        y += yOffset;

        if (x < 0 || y < 0 || x >= getWidth() || y >= getWidth()) {
            return;
        }

        backend.setPixel(x, y, color);
    }


    // ----- Graphics settings -----

    public void setColor(java.awt.Color color) {
        setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    public void setColor(org.bukkit.Color color) {
        setColor(color.getRed(), color.getGreen(), color.getBlue());
    }

    @SuppressWarnings("deprecation")
    public void setColor(int r, int g, int b) {
        setColor(MapPalette.matchColor(r, g, b));
    }

    public void setColor(byte color) {
        this.color = color;
    }

    public void setFont(MapFont font, int scale) {
        this.font = font;
        this.fontScale = scale;
    }


    public int getWidth() {
        return backend.getWidth();
    }

    public int getHeight() {
        return backend.getHeight();
    }

    /*
     * Updates the positional offset and bounds
     *
     * This is called by a container to make sure its components can
     * draw as if the origin of the container is the origin of the
     * graphics object.
     *
     * TODO: consider preventing components from drawing outside of the bounding box
     */
    protected void updateOffsets(CanvasContainer container) {
        xOffset = container.getAbsoluteX();
        yOffset = container.getAbsoluteY();
    }
}
