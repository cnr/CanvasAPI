package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;
import com.eueln.canvasapi.components.ImageComponent;
import com.eueln.canvasapi.event.InteractListener;
import com.eueln.canvasapi.impl.map.MapCanvasBackend;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.io.IOException;

public class CanvasAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Canvas canvas = new Canvas(new MapCanvasBackend(player.getLocation().add(0, 3, 0), BlockFace.SOUTH, 6, 6));
        CanvasComponent component = new CanvasComponent(0, 0, 100, 100) {
            @Override
            public void paint(CanvasGraphics g) {
                g.drawRect(50, 50, 100, 100);
                g.drawString("Testing One Two Threeeeeeeeeeeee", 10, 10);
            }
        };
        canvas.add(component);
        canvas.addInteractListener(new InteractListener() {
            @Override
            public void onClick(Canvas canvas, Player player, int x, int y) {
                player.sendMessage(ChatColor.GREEN + String.format("Interact: %d, %d", x, y));
            }

            @Override
            public void onHover(Canvas canvas, Player player, int x, int y) {
                player.sendMessage(ChatColor.GREEN + String.format("Hover: %d, %d", x, y));
            }
        });
        try {
            canvas.add(new ImageComponent(5, 155, ImageIO.read(getResource("testimage.jpg"))));
        } catch (IOException ignored) {}
        //component.setVisible(false);
        canvas.setVisible(player, true);
    }
}
