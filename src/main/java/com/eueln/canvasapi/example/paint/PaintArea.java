package com.eueln.canvasapi.example.paint;

import com.eueln.canvasapi.Canvas;
import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;
import com.eueln.canvasapi.event.InteractListener;
import org.bukkit.entity.Player;
import org.bukkit.map.MapPalette;

public class PaintArea extends CanvasComponent {
    private final PaintComponent wrapper;
    private final byte[][] contents;

    @SuppressWarnings("deprecation")
    protected PaintArea(final PaintComponent wrapper, int width, int height) {
        super(0, 0, width, height);
        this.wrapper = wrapper;
        this.contents = new byte[width / 4][height / 4];
        for (int i = 0; i < contents.length; i++) {
            for (int k = 0; k < contents[i].length; k++) {
                contents[i][k] = MapPalette.WHITE;
            }
        }

        this.addInteractListener(new InteractListener() {
            @Override
            public void onClick(Canvas canvas, Player player, int x, int y) {
                contents[x >> 2][y >> 2] = wrapper.getColor(player);
                invalidate();
            }

            @Override
            public void onHover(Canvas canvas, Player player, int x, int y) {}
        });
    }

    @Override
    public void paint(CanvasGraphics g) {
        for (int i = 0; i < contents.length; i++) {
            for (int k = 0; k < contents[i].length; k++) {
                g.setColor(contents[i][k]);
                g.drawRect(i << 2, k << 2, 4, 4);
            }
        }
    }
}
