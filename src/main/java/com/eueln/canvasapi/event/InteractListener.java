package com.eueln.canvasapi.event;

import com.eueln.canvasapi.Canvas;
import org.bukkit.entity.Player;

public interface InteractListener {
    /**
     * This method is called when a player interacts by clicking on
     * the component listened to
     *
     * @param canvas The canvas involved in this event
     * @param player The player involved in this event
     * @param x The X coordinate of the event
     * @param y The Y coordinate of the event
     */
    public void onClick(Canvas canvas, Player player, int x, int y);

    /**
     * This method is called when a player interacts by hovering over
     * the component listened to with his/her crosshair
     *
     * @param canvas The canvas involved in this event
     * @param player The player involved in this event
     * @param x The X coordinate of the event
     * @param y The Y coordinate of the event
     */
    public void onHover(Canvas canvas, Player player, int x, int y);
}
