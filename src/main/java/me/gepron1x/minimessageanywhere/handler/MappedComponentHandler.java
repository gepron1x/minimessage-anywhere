package me.gepron1x.minimessageanywhere.handler;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.ArrayList;
import java.util.List;

final class MappedComponentHandler implements ComponentHandler {
    private final List<ComponentHandler> handlers;

    MappedComponentHandler(List<ComponentHandler> handlers) {
        this.handlers = handlers;
    }


    @Override
    public Component handle(Audience audience, Component component) {
        for (ComponentHandler handler : handlers) {
            component = handler.handle(audience, component);
        }

        return component;
    }

    public List<ComponentHandler> getHandlers() {
        return handlers;
    }

    @Override
    public ComponentHandler andThen(ComponentHandler handler) {
        ArrayList<ComponentHandler> handlers = new ArrayList<>(this.handlers);
        if (handler instanceof MappedComponentHandler) {
            handlers.addAll(((MappedComponentHandler) handler).getHandlers());
        } else {
            handlers.add(handler);
        }
        return new MappedComponentHandler(handlers);
    }
}
