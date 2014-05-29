package com.eueln.canvasapi.components;

import com.eueln.canvasapi.CanvasComponent;
import com.eueln.canvasapi.CanvasGraphics;
import org.bukkit.map.MapFont;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MinecraftFont;

/**
 * A text label component. The default font is the minecraft font at
 * the 1x scale, and the default color is white.
 */
public class TextLabelComponent extends CanvasComponent {
    private String text;
    private MapFont font = MinecraftFont.Font;
    private int scale = 1;
    @SuppressWarnings("deprecation")
    private byte color = MapPalette.WHITE;

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
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param text The text to be drawn
     * @param font The font to be used
     * @param scale The font scale to be used
     */
    @SuppressWarnings("deprecation")
    public TextLabelComponent(int x, int y, String text, MapFont font, int scale) {
        this(x, y, text);
        setFont(font, scale);
    }

    /**
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param text The text to be drawn
     * @param color The color to draw the text
     */
    public TextLabelComponent(int x, int y, String text, byte color) {
        this(x, y, text);
        setColor(color);
    }

    /**
     * @param x The X position of this component
     * @param y The Y position of this component
     * @param text The text to be drawn
     * @param font The font to be used
     * @param scale The font scale to be used
     * @param color The color to draw the text
     */
    public TextLabelComponent(int x, int y, String text, MapFont font, int scale, byte color) {
        this(x, y, text);
        setFont(font, scale);
        setColor(color);
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

    /**
     * Sets the color for text drawing
     *
     * @param color The color to draw with
     */
    public void setColor(byte color) {
        this.color = color;
        invalidate();
    }

    @Override
    public int getWidth() {
        return font.getWidth(text) * scale;
    }

    @Override
    public int getHeight() {
        return font.getHeight() * scale;
    }

    @Override
    public void paint(CanvasGraphics g) {
        g.setFont(font, scale);
        g.setColor(color);
        g.drawString(text, getX(), getY());
    }
}
