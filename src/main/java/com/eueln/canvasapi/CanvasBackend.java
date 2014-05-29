package com.eueln.canvasapi;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public interface CanvasBackend {
    /**
     * Returns the width (in "pixels") that this backend is supporting
     *
     * @return This backend's width
     */
    public int getWidth();

    /**
     * Returns the height (in "pixels") that this backend is supporting
     *
     * @return This backend's height
     */
    public int getHeight();

    /**
     * Sets a pixel to the specified color
     *
     * @param x The X position
     * @param y The Y position
     * @param color The color to be set
     */
    public void setPixel(int x, int y, byte color);

    /**
     * Called after an attached canvas has been repainted to prompt
     * this backend to update/resend visible components to the player
     *
     * @param canvas The canvas that was repainted
     */
    public void update(Canvas canvas);

    /**
     * Returns the world in which this backend resides
     *
     * @return This backend's world
     */
    public World getWorld(); // The world the canvas is visible in

    /**
     * Prompts this backend to show the canvas to this player
     *
     * @param player The player to whom the canvas should be displayed
     */
    public void showTo(Player player);

    /**
     * Prompts this backend to hide the canvas from this player
     *
     * @param player The player from whom the canvas should be hidden
     */
    public void hideFrom(Player player);

    /**
     * Returns the coordinates (in the form of a vector) that represent
     * the area touched by this player.
     *
     * This is used for interaction events
     *
     * @param player The player to calculate the touched point for
     * @return A vector with its X and Y positions as the touched point
     *         or null if the player is not touching this point
     */
    public Vector getTouchedPoint(Player player);
}
