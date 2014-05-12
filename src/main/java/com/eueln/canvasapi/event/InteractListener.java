package com.eueln.canvasapi.event;

import com.eueln.canvasapi.Canvas;
import org.bukkit.entity.Player;

public interface InteractListener {
    public void onInteract(Canvas canvas, Player player, int x, int y);
}
