package com.eueln.canvasapi;

import com.eueln.canvasapi.graphics.MGraphics;

import java.util.ArrayList;
import java.util.List;

public abstract class MContainer extends MComponent {
    private final List<MComponent> components = new ArrayList<>(); // components are drawn in the order they're inserted

    public MContainer(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void add(MComponent component) {
        components.add(component);
        component.setParent(this);
        invalidate();
    }

    public void add(MComponent component, int index) {
        components.add(index, component);
        component.setParent(this);
        invalidate();
    }

    public void remove(MComponent component) {
        components.remove(component);
        component.removeParent();
        invalidate();
    }

    public MComponent[] getComponents() {
        return components.toArray(new MComponent[components.size()]);
    }

    @Override
    public MComponent getComponentAt(int x, int y) {
        if (!contains(x, y)) {
            return null;
        }

        for (MComponent component : components) {
            MComponent componentAt = component.getComponentAt(x, y);
            if (componentAt != null) {
                return componentAt;
            }
        }
        return null;
    }

    @Override
    public void paint(MGraphics g) {
        for (MComponent component : components) {
            component.paint(g);
        }
    }
}
