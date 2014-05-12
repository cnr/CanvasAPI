package com.eueln.canvasapi;

import org.bukkit.entity.Player;

public interface CanvasGraphics {
    public void drawRect(int x, int y, int width, int height);
    public void drawString(String str, int x, int y);

    public int getWidth();
    public int getHeight();

    void register(Canvas canvas);

    void showTo(Player player);
    void showToAll();
    void hideFrom(Player player);
}
