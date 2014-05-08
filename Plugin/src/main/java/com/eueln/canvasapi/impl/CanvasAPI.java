package com.eueln.canvasapi.impl;

import com.eueln.canvasapi.impl.map.MapManager;
import org.bukkit.plugin.java.JavaPlugin;

public class CanvasAPI extends JavaPlugin {

    @Override
    public void onEnable() {
        MapManager.init(this); // Seems like a safe workaround to exposing things statically
    }
}
