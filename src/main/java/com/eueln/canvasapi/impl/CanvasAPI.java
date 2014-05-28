package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.components.ImageComponent;
import com.eueln.canvasapi.components.SolidColorComponent;
import com.eueln.canvasapi.components.TextLabelComponent;
import com.eueln.canvasapi.event.InteractListener;
import com.eueln.canvasapi.impl.map.MapCanvasBackend;
import org.bukkit.ChatColor;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.java.JavaPlugin;

import javax.imageio.ImageIO;
import java.io.IOException;

public class CanvasAPI extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Canvas canvas = new Canvas(new MapCanvasBackend(player.getLocation().add(0, 3, 0), BlockFace.SOUTH, 6, 6));

        // Add a solid white component
        canvas.add(new SolidColorComponent(50, 50, 100, 100, MapPalette.WHITE));

        // Add a long text label component
        TextLabelComponent testLabel = new TextLabelComponent(10, 10, "Testing One Two Threeeeeeeeeeeee");
        testLabel.setFont(MinecraftFont.Font, 3);
        canvas.add(testLabel);

        // Add an image component using the test image provided
        try {
            canvas.add(new ImageComponent(5, 155, ImageIO.read(getResource("testimage.jpg"))));
        } catch (IOException ignored) {}

        // Add a debugging interact listener for the main canvas
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

        // Set the canvas as visible for the player
        canvas.setVisible(player, true);
    }
}
