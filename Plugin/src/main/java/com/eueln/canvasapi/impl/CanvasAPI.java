package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;
import com.eueln.canvasapi.impl.map.MapCanvasGraphics;
import com.eueln.canvasapi.impl.map.MapManager;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class CanvasAPI extends JavaPlugin implements Listener {
    private static CanvasAPI _instance;

    private MapManager mapManager;

    public static CanvasAPI instance() {
        return _instance;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    @Override
    public void onEnable() {
        _instance = this;
        getServer().getPluginManager().registerEvents(this, this);

        // Init MapManager
        mapManager = new MapManager(this);
        getServer().getPluginManager().registerEvents(mapManager, this);
        getServer().getScheduler().runTaskTimer(this, mapManager, 1L, 1L); // 1L might be too fast.
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Canvas canvas = new Canvas(new MapCanvasGraphics(player.getLocation().add(0, 3, 0), BlockFace.SOUTH, 2, 2));
        CanvasComponent component = new CanvasComponent(0, 0, 100, 100) {
            @Override
            public void paint(CanvasGraphics g) {
                g.drawRect(50, 50, 100, 100);
            }
        };
        canvas.add(component);
        //component.setVisible(false);
        canvas.setVisible(player, true);
    }
}
