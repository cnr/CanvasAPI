package com.eueln.canvasapi.example.paint;

import com.eueln.canvasapi.CanvasContainer;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaintComponent extends CanvasContainer {
    private final Map<UUID, Byte> colors = new HashMap<>();

    public PaintComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
        add(new PaintArea(this, width, height));
        add(new PaintToolbar(this, width, height));
    }

    @SuppressWarnings("deprecation")
    protected byte getColor(Player player) {
        if (colors.containsKey(player.getUniqueId())) {
            return colors.get(player.getUniqueId());
        } else {
            return MapPalette.DARK_GRAY;
        }
    }

    protected void setColor(Player player, byte color) {
        colors.put(player.getUniqueId(), color);
    }
}
