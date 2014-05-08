package com.eueln.canvasapi;

import com.eueln.canvasapi.graphics.CanvasGraphics;

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
        return null;
    }

    @Override
    public void paint(CanvasGraphics g) {
        for (CanvasComponent component : components) {
            if (component.isVisible()) {
                component.paint(g);
            }
        }
    }
}
