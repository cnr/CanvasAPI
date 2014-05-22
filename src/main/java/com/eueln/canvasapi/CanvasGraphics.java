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

    public CanvasGraphics(CanvasBackend backend) {
        this.backend = backend;
    }


    // ----- Drawing methods -----

    public void drawRect(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int k = y; k < y + height; k++) {
                backend.setPixel(i, k, color);
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
                        backend.setPixel(x + column, y + row, color);
                    }
                }
            }
            x += sprite.getWidth() + 1;
        }
    }

    @SuppressWarnings("deprecation")
    public void drawImage(BufferedImage image, int startX, int startY) {
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);

                byte color = MapPalette.matchColor((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                backend.setPixel(x + startX, y + startY, color);
            }
        }
    }


    // ----- Graphics settings -----

    public void setColor(int color) {
        this.color = (byte) color;
    }

    public void setFont(MapFont font) {
        this.font = font;
    }


    public int getWidth() {
        return backend.getWidth();
    }

    public int getHeight() {
        return backend.getHeight();
    }
}
