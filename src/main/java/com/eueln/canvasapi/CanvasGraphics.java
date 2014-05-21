package com.eueln.canvasapi;

import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

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

    public int getWidth() {
        return backend.getWidth();
    }

    public int getHeight() {
        return backend.getHeight();
    }
}
