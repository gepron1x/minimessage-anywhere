package me.gepron1x.minimessageanywhere.handler;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.gepron1x.minimessageanywhere.util.ProtocolLibraryComponentSerializer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * An interface that represents a component modifier. All components being sent by the server are being handled here.
 */
@FunctionalInterface
public interface ComponentHandler {
    ComponentHandler IDENTITY = (a, c) -> c;
    /**
     * handles given component
     * @param audience - a receiver of the message
     * @param component - message
     * @return - result of handling
     */
    Component handle(Audience audience, Component component);

    /**
     * handles given wrapped component. a shortcut for ComponentHandler#handle
     * @param audience - a receiver of the message
     * @param component - message
     * @return result of handling
     */
    default WrappedChatComponent handle(Audience audience, WrappedChatComponent component) {
        ProtocolLibraryComponentSerializer serializer = ProtocolLibraryComponentSerializer.get();
        return serializer.serialize(handle(audience, serializer.deserialize(component)));
    }

    /**
     * handles a component if it's not null.
     * @param audience - a receiver of the message
     * @param component - message
     * @return null if input is null, result of handling else.
     */
    default Component handleIfNotNull(Audience audience, @Nullable Component component) {
        return component == null ? null : handle(audience, component);
    }

    /**
     * handles a component if it's not null.
     *
     * @param audience  - a receiver of the message
     * @param component - message
     * @return null if input is null, result of handling else.
     */
    default WrappedChatComponent handleIfNotNull(Audience audience, @Nullable WrappedChatComponent component) {
        return component == null ? null : handle(audience, component);
    }

    default ComponentHandler andThen(ComponentHandler handler) {
        return allOf(this, handler);
    }

    static ComponentHandler allOf(Collection<ComponentHandler> handlers) {
        ArrayList<ComponentHandler> temp = new ArrayList<>();
        for (ComponentHandler handler : handlers) {
            if (handler instanceof MappedComponentHandler) {
                temp.addAll(((MappedComponentHandler) handler).handlers());
            } else if (handler != IDENTITY) {
                temp.add(handler);
            }

        }
        return new MappedComponentHandler(temp);
    }

    static ComponentHandler allOf(ComponentHandler... handlers) {
        return allOf(Arrays.asList(handlers));
    }
}
