package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.graphics.CanvasGraphics;
import com.eueln.canvasapi.impl.map.MapCanvas;
import com.eueln.canvasapi.impl.map.MapManager;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CanvasAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        MapManager.init(this); // Seems like a safe workaround to exposing things statically
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Canvas canvas = new MapCanvas(player.getLocation().add(0, 3, 0), BlockFace.SOUTH, 2, 2);
        CanvasComponent component = new CanvasComponent(0, 0, 100, 100) {
            @Override
            public void paint(CanvasGraphics g) {
                g.drawRect(0, 0, 100, 100);
            }
        };
        canvas.add(component);
        component.setVisible(false);
        canvas.setVisible(player, true);
    }
}
