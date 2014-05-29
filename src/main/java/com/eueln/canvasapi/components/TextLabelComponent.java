package com.eueln.canvasapi.components;

import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;
import org.bukkit.map.MapFont;
import org.bukkit.map.MinecraftFont;

/**
 * A text label component
 */
public class TextLabelComponent extends CanvasComponent {
    private String text;
    private MapFont font = MinecraftFont.Font;
    private int scale = 1;

    /**
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param text The text to be drawn
     */
    public TextLabelComponent(int x, int y, String text) {
        super(x, y, 0, 0);
        this.text = text;
    }

    /**
     * Sets the text to be drawn
     *
     * @param text The new text to be drawn
     */
    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    /**
     * Sets the font and text scale to draw with
     *
     * @param font The new font to use
     * @param scale The new font scaling to use
     */
    public void setFont(MapFont font, int scale) {
        this.font = font;
        this.scale = scale;
        invalidate();
    }

    @Override
    public int getWidth() {
        return font.getWidth(text) * scale;
    }

    @Override
    public int getHeight() {
        return font.getHeight();
    }

    @Override
    public void paint(CanvasGraphics g) {
        g.setFont(font, scale);
        g.drawString(text, getX(), getY());
    }
}
