package com.eueln.canvasapi.event;

import com.eueln.canvasapi.Canvas;
import org.bukkit.entity.Player;

public interface InteractListener {
    public void onClick(Canvas canvas, Player player, int x, int y);
    public void onHover(Canvas canvas, Player player, int x, int y);
}
