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

        CanvasComponent target = this;
        for (CanvasComponent component : components) {
            if (component.contains(x - getX(), y - getY())) {
                target = component;
            }
        }

        return target;
    }

    @Override
    public void paint(CanvasGraphics g) {
        g.updateOffsets(this);

        for (CanvasComponent component : components) {
            if (component.isVisible()) {
                component.doPaint(g);

                if (component instanceof CanvasContainer) {
                    // Our offsets were clobbered by this container; update them again
                    g.updateOffsets(this);
                }
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
                component.fireClickEvent(canvas, player, x - getX(), y - getY());
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
                component.fireHoverEvent(canvas, player, x - getX(), y - getY());
            }
        }
    }
}
