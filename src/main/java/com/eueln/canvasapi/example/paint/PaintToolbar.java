package com.eueln.canvasapi.example.paint;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasContainer;
import com.eueln.canvasapi.components.SolidColorComponent;
import com.eueln.canvasapi.event.InteractListener;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

public class PaintToolbar extends CanvasContainer {
    @SuppressWarnings("deprecation")
    private static final byte[] COLORS = new byte[] {MapPalette.WHITE, MapPalette.DARK_GREEN, MapPalette.BROWN, MapPalette.RED, MapPalette.BLUE, MapPalette.LIGHT_GREEN, MapPalette.PALE_BLUE, MapPalette.DARK_GRAY };
    private static final int BUTTON_SIZE = 20;

    public PaintToolbar(final PaintComponent wrapper, int parentWidth, int parentHeight) {
        super((parentWidth / 2) - (COLORS.length * BUTTON_SIZE / 2), parentHeight - BUTTON_SIZE, BUTTON_SIZE * COLORS.length, BUTTON_SIZE);

        for (int i = 0; i < COLORS.length; i++) {
            final byte color = COLORS[i];
            SolidColorComponent component = new SolidColorComponent(BUTTON_SIZE * i, 0, BUTTON_SIZE, BUTTON_SIZE, color);
            component.addInteractListener(new InteractListener() {
                @Override
                public void onClick(Canvas canvas, Player player, int x, int y) {
                    player.sendMessage(ChatColor.GREEN + "Changed color");
                    wrapper.setColor(player, color);
                }

                @Override
                public void onHover(Canvas canvas, Player player, int x, int y) {}
            });

            add(component);
        }
    }
}
