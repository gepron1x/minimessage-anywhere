package me.gepron1x.minimessageanywhere.handler;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.Objects;

/**
 * A global component handler. here you can add your own handlers.
 */
public final class GlobalComponentHandler implements ComponentHandler {

    private final SortedMultiset<HandlerEntry> entries = TreeMultiset.create(Comparator.comparing(e -> -e.priority.getPriority()));

    /**
     * creates a new GlobalComponentHandler with given handlers.
     * @param handlers - handlers to add
     */
    public GlobalComponentHandler(@NotNull ComponentHandler @NotNull... handlers) {
        for(ComponentHandler handler : handlers) {
            entries.add(new HandlerEntry(handler, HandlerPriority.NORMAL));
        }
    }

    /**
     * creates a new empty GlobalComponentHandler.
     */
    public GlobalComponentHandler() {

    }

    /**
     * adds a handler.
     * @param handler - handler to add
     * @param priority - a handler priority.
     */
    public void addHandler(ComponentHandler handler, HandlerPriority priority) {
        entries.add(new HandlerEntry(handler, priority));
    }

    /**
     * adds a handler with NORMAL priority
     * @param handler - handler to add
     */
    public void addHandler(ComponentHandler handler) {
        addHandler(handler, HandlerPriority.NORMAL);
    }

    /**
     * removes a handler from GlobalHandler.
     * @param handler - handler to remove
     * @return true if handler removed successfully, false otherwise.
     */

    public boolean removeHandler(ComponentHandler handler) {
        return entries.removeIf(entry -> entry.handler.equals(handler));
    }

    /**
     * removes all handlers.
     */
    public void clear() {
        entries.clear();
    }


    @Override
    public Component handle(Audience audience, Component component) {

        for(HandlerEntry entry : entries) {
            component = entry.handler.handle(audience, component);
        }
        return component;
    }


    private static class HandlerEntry {
        HandlerEntry(ComponentHandler handler, HandlerPriority priority) {
            this.handler = handler;

            this.priority = priority;
        }

        final ComponentHandler handler;
        final HandlerPriority priority;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HandlerEntry that = (HandlerEntry) o;
            return handler.equals(that.handler) && priority == that.priority;
        }

        @Override
        public int hashCode() {
            return Objects.hash(handler, priority);
        }
    }
}
