package com.eueln.canvasapi.impl.map;

import com.eueln.canvasapi.impl.CanvasAPI;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.*;

public class MapManager implements Listener, Runnable {
    private static final MapManager _instance = new MapManager();

    private final Set<MapCanvas> registered = new HashSet<>();
    private final Map<MapCanvas, List<Player>> lastVisibleMap = new HashMap<>();

    private MapManager() {}

    public static void init(CanvasAPI plugin) {
        // Register events
        plugin.getServer().getPluginManager().registerEvents(_instance, plugin);

        // Schedule our task
        plugin.getServer().getScheduler().runTaskTimer(plugin, _instance, 1L, 1L);
    }

    protected static void register(MapCanvas canvas) {
        _instance.registered.add(canvas);
    }

    @Override
    public void run() {
        for (MapCanvas canvas : registered) {
            updateCanvas(canvas);
        }
    }

    private void updateCanvas(MapCanvas canvas) {
        // Get the players who could see this canvas as of the last update
        List<Player> lastVisible = lastVisibleMap.get(canvas);
        if (lastVisible == null) {
            lastVisible = new ArrayList<>();
        }

        // Paint the canvas and send updated sections to those who could see it previously
        canvas.paint(canvas);
        for (MapCanvas.CanvasSection section : canvas.getSections()) {
            if (section.hasChanges()) {
                section.clearChanges();
                updateSection(lastVisible, section);
            }
        }

        // Resolve player deltas

        // Try to find new players
        Set<Player> visibleTo = canvas.getVisibleTo();
        for (Player player : visibleTo) {
            if (lastVisible.contains(player)) {
                continue;
            }

            // We've found a new player!
            lastVisible.add(player);
            sendCanvas(player, canvas);
        }

        // Find players who can no longer see the canvas
        Iterator<Player> it = lastVisible.iterator();
        while (it.hasNext()) {
            Player player = it.next();
            if (visibleTo.contains(player)) {
                continue;
            }

            // We've found a player that's been removed!
            it.remove();
            hideCanvas(player, canvas);
        }
    }

    private void updateSection(List<Player> players, MapCanvas.CanvasSection section) {
        // TODO
    }

    private void sendCanvas(Player player, MapCanvas canvas) {
        // TODO
    }

    private void hideCanvas(Player player, MapCanvas canvas) {
        // TODO
    }
}
