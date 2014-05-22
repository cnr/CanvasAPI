package com.eueln.canvasapi;

import org.bukkit.map.MapFont;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MinecraftFont;

import java.awt.*;
import java.awt.image.BufferedImage;

public class CanvasGraphics {
    private final CanvasBackend backend;

    public CanvasGraphics(CanvasBackend backend) {
        this.backend = backend;
    }

    public void drawRect(int x, int y, int width, int height) {
        for (int i = x; i < x + width; i++) {
            for (int k = y; k < y + height; k++) {
                backend.setPixel(i, k, (byte) 38); // TODO: colors
            }
        }
    }

    public void drawString(String str, int x, int y) {
        MapFont font = MinecraftFont.Font;

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            MapFont.CharacterSprite sprite = font.getChar(ch);
            for (int row = 0; row < sprite.getHeight(); row++) {
                for (int column = 0; column < sprite.getWidth(); column++) {
                    if (sprite.get(row, column)) {
                        backend.setPixel(x + column, y + row, (byte)38);
                    }
                }
            }
            x += sprite.getWidth() + 1;
        }
    }

    public void drawImage(BufferedImage image, int startX, int startY) {
        drawImage(image, startX, startY, image.getWidth(), image.getHeight());
    }

    @SuppressWarnings("deprecation")
    public void drawImage(BufferedImage image, int startX, int startY, int width, int height) {
        // Scale the image if necessary
        if (image.getWidth() != width || image.getHeight() != height) {
            Image scaled = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics g = image.getGraphics();
            g.drawImage(scaled, 0, 0, null);
            g.dispose();
        }

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int rgb = image.getRGB(x, y);

                byte color = MapPalette.matchColor((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
                backend.setPixel(x + startX, y + startY, color);
            }
        }
    }

    public int getWidth() {
        return backend.getWidth();
    }

    public int getHeight() {
        return backend.getHeight();
    }
}
