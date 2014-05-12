package com.eueln.canvasapi.impl.map;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.impl.CanvasAPI;
import com.eueln.canvasapi.impl.util.MathUtil;
import net.minecraft.server.v1_7_R3.EntityItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.*;

public class MapManager implements Listener, Runnable {
    private final CanvasAPI plugin;
    private final Set<MapCanvasGraphics> registered = new HashSet<>();
    private final Map<MapCanvasGraphics, EntityItemFrame[]> frames = new HashMap<>();

    public MapManager(CanvasAPI plugin) {
        this.plugin = plugin;
    }

    public void register(MapCanvasGraphics graphics) {
        registered.add(graphics);
        frames.put(graphics, FrameUtil.createFrames(graphics));
    }

    public void showTo(Player player, MapCanvasGraphics graphics) {
        // Make sure the player is in the correct world before trying
        if (player.getWorld() == graphics.getWorld()) {

            // Spawn the frame
            FrameUtil.spawn(player, frames.get(graphics));

            // Send the maps from each section
            for (MapCanvasSection section : graphics.getSections()) {
                FrameUtil.sendSection(Arrays.asList(player), section.getMapId(), section.getData());
            }
        }
    }

    public void showToAll(MapCanvasGraphics graphics) {
        for (Player player : graphics.getWorld().getPlayers()) {

            // Send canvas to players that couldn't previously see it
            if (!graphics.getCanvas().isVisible(player)) {
                showTo(player, graphics);
            }
        }
    }

    public void hideFrom(Player player, MapCanvasGraphics graphics) {
        // Make sure the player could previously see it
        if (player.getWorld() == graphics.getWorld()) {
            FrameUtil.destroy(player, frames.get(graphics));
        }
    }


    // ----- Canvas repainting and resending of changed sections -----

    @Override
    public void run() {
        // Repaint all canvases
        for (MapCanvasGraphics graphics : registered) {
            graphics.repaint();

            // Send updated sections to players
            for (MapCanvasSection section : graphics.getSections()) {
                if (section.hasChanges()) {
                    section.clearChanges();
                    resendSection(graphics.getCanvas(), section);
                }
            }
        }
    }

    private void resendSection(Canvas canvas, MapCanvasSection section) {
        List<Player> sendSectionTo = new ArrayList<>();

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            if (canvas.isVisible(player)) {
                sendSectionTo.add(player);
            }
        }

        FrameUtil.sendSection(sendSectionTo, section.getMapId(), section.getData());
    }


    // ----- Player events

    // Hide and show relevant canvases when the player teleports
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() == event.getTo().getWorld()) {
            return;
        }

        final Player player = event.getPlayer();

        for (MapCanvasGraphics graphics : registered) {
            hideFrom(player, graphics); // The world guarantee is already there
        }

        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (MapCanvasGraphics graphics : registered) {
                    showTo(player, graphics); // The world guarantee is already there
                }
            }
        });
    }

    // Fire canvas interact events
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        for (MapCanvasGraphics graphics : registered) {
            if (graphics.getWorld() != event.getPlayer().getWorld()) {
                continue;
            }
            Canvas canvas = graphics.getCanvas();

            org.bukkit.util.Vector vector = MathUtil.getTouchedPoint(event.getPlayer(), graphics);
            int touchedX = (int)Math.floor(vector.getX() * 128);
            int touchedY = (int)Math.floor(vector.getY() * 128);

            if (touchedX >= 0 && touchedY >= 0 && touchedX < canvas.getWidth() && touchedY < canvas.getHeight()) {
                canvas.fireClickEvent(event.getPlayer(), touchedX, touchedY);
            }
        }
    }

    // This seems like it could get out of hand. Maybe we should avoid / remove this entirely.
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        for (MapCanvasGraphics graphics : registered) {
            if (graphics.getWorld() != event.getPlayer().getWorld()) {
                continue;
            }
            Canvas canvas = graphics.getCanvas();

            org.bukkit.util.Vector vector = MathUtil.getTouchedPoint(event.getPlayer(), graphics);
            int touchedX = (int)Math.floor(vector.getX() * 128);
            int touchedY = (int)Math.floor(vector.getY() * 128);

            if (touchedX >= 0 && touchedY >= 0 && touchedX < canvas.getWidth() && touchedY < canvas.getHeight()) {
                canvas.fireHoverEvent(event.getPlayer(), touchedX, touchedY);
            }
        }
    }
}
