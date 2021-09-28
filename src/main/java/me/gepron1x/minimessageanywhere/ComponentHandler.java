package me.gepron1x.minimessageanywhere;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import me.gepron1x.minimessageanywhere.util.ProtocolLibraryComponentSerializer;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface ComponentHandler {
    Component handle(Audience audience, Component component);

    default WrappedChatComponent handle(Audience audience, WrappedChatComponent component) {
        ProtocolLibraryComponentSerializer serializer = ProtocolLibraryComponentSerializer.get();
        return serializer.serialize(handle(audience, serializer.deserialize(component)));
    }
    default Component handleIfNotNull(Audience audience, @Nullable Component component) {
        return component == null ? null : handle(audience, component);
    }
    default WrappedChatComponent handleIfNotNull(Audience audience, @Nullable WrappedChatComponent component) {
        return component == null ? null : handle(audience, component);
    }
}
