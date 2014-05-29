package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasBackend;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class CanvasManager implements Listener, Runnable {
    private static final CanvasManager _instance = new CanvasManager();
    private final CanvasAPI plugin;
    private final Set<Canvas> registered = new HashSet<>();

    private CanvasManager() {
        // Register our own events, the <s>best</s> worst way!
        CanvasAPI plugin = JavaPlugin.getPlugin(CanvasAPI.class);
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
        Bukkit.getScheduler().runTaskTimer(plugin, this, 1L, 1L); // 1L might be too fast.
    }

    @Override
    public void run() {
        for (Canvas canvas : registered) {
            if (canvas.isValid()) {
                continue;
            }
            canvas.repaint();
        }
    }


    // ----- Events -----

    // Hide and show relevant canvases when the player teleports
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getFrom().getWorld() == event.getTo().getWorld()) {
            return;
        }

        final Player player = event.getPlayer();

        for (Canvas canvas : registered) {
            hide(player, canvas); // The world guarantee is already there
        }

        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
            @Override
            public void run() {
                for (Canvas canvas : registered) {
                    show(player, canvas); // The world guarantee is already there
                }
            }
        });
    }

    // Fire canvas interact events
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        for (Canvas canvas : registered) {
            CanvasBackend backend = canvas.getBackend();
            if (backend.getWorld() != event.getPlayer().getWorld() || !canvas.isVisible(event.getPlayer())) {
                continue;
            }

            Vector vector = backend.getTouchedPoint(event.getPlayer());
            if (vector != null) {
                canvas.fireClickEvent(event.getPlayer(), vector.getBlockX(), vector.getBlockY());
            }
        }
    }

    // This seems like it could get out of hand. Maybe we should avoid / remove this entirely.
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMove(PlayerMoveEvent event) {
        for (final Canvas canvas : registered) {
            final CanvasBackend backend = canvas.getBackend();
            final Player player = event.getPlayer();

            if (player.getWorld() != backend.getWorld() || !canvas.isVisible(event.getPlayer())) {
                continue;
            }

            Bukkit.getScheduler().runTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Vector vector = backend.getTouchedPoint(player);
                    if (vector != null) {
                        canvas.fireHoverEvent(player, vector.getBlockX(), vector.getBlockY());
                    }
                }
            });
        }
    }


    // ----- Static calls from Canvas -----

    public static void register(Canvas canvas) {
        CanvasManager manager = _instance; // So we have a friendlier name to work with
        if (!manager.registered.add(canvas)) {
            throw new IllegalArgumentException("Canvas already registered");
        }
    }


    public static void showAll(Canvas canvas) {
        CanvasBackend backend = canvas.getBackend();
        for (Player player : backend.getWorld().getPlayers()) {

            // Send canvas to players that couldn't previously see it
            if (!canvas.isVisible(player)) {
                show(player, canvas);
            }
        }
    }

    public static void hideAll(Canvas canvas) {
        CanvasBackend backend = canvas.getBackend();
        for (Player player : backend.getWorld().getPlayers()) {

            // Hide the canvas to players that could see it
            if (canvas.isVisible(player)) {
                hide(player, canvas);
            }
        }
    }

    public static void show(Player player, Canvas canvas) {
        CanvasBackend backend = canvas.getBackend();

        // Make sure the player is in the correct world before trying
        if (player.getWorld() == backend.getWorld()) {
            backend.showTo(player);
        }
    }

    public static void hide(Player player, Canvas canvas) {
        CanvasBackend backend = canvas.getBackend();

        // Make sure the player could previously see it
        if (player.getWorld() == backend.getWorld()) {
            backend.hideFrom(player);
        }
    }
}
