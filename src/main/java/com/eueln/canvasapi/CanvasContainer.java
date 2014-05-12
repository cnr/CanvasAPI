package com.eueln.canvasapi;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public abstract class CanvasContainer extends CanvasComponent {
    private final List<CanvasComponent> components = new ArrayList<>(); // components are drawn in the order they're inserted

    public CanvasContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void add(CanvasComponent component) {
        components.add(component);
        component.setParent(this);
        invalidate();
    }

    public void add(CanvasComponent component, int index) {
        components.add(index, component);
        component.setParent(this);
        invalidate();
    }

    public void remove(CanvasComponent component) {
        components.remove(component);
        component.removeParent();
        invalidate();
    }

    public CanvasComponent[] getComponents() {
        return components.toArray(new CanvasComponent[components.size()]);
    }

    @Override
    public CanvasComponent getComponentAt(int x, int y) {
        if (!contains(x, y)) {
            return null;
        }

        for (CanvasComponent component : components) {
            CanvasComponent componentAt = component.getComponentAt(x, y);
            if (componentAt != null) {
                return componentAt;
            }
        }

        return this;
    }

    @Override
    public void paint(CanvasGraphics g) {
        for (CanvasComponent component : components) {
            if (component.isVisible()) {
                component.paint(g);
            }
        }
    }

    @Override
    public void fireClickEvent(Canvas canvas, Player player, int x, int y) {
        CanvasComponent component = getComponentAt(x, y);
        if (component != null) {

            if (component == this) {
                super.fireClickEvent(canvas, player, x, y);
            } else {
                component.fireClickEvent(canvas, player, x, y);
            }
        }
    }

    @Override
    public void fireHoverEvent(Canvas canvas, Player player, int x, int y) {
        CanvasComponent component = getComponentAt(x, y);
        if (component != null) {

            if (component == this) {
                super.fireHoverEvent(canvas, player, x, y);
            } else {
                component.fireHoverEvent(canvas, player, x, y);
            }
        }
    }
}
