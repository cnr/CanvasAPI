package com.eueln.canvasapi;

import org.bukkit.World;
import org.bukkit.entity.Player;

public interface CanvasBackend {
    public int getWidth();
    public int getHeight();
    public void setPixel(int x, int y, byte color);
    public void update(Canvas canvas); // Send deltas to players

    public World getWorld(); // The world the canvas is visible in
    public void showTo(Player player);
    public void hideFrom(Player player);
}
