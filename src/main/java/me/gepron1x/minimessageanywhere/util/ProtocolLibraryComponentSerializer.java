package me.gepron1x.minimessageanywhere.util;

import com.comphenix.protocol.wrappers.WrappedChatComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ProtocolLibraryComponentSerializer implements ComponentSerializer<Component, Component, WrappedChatComponent> {
    private final GsonComponentSerializer serializer;
    private static final ProtocolLibraryComponentSerializer INSTANCE = new ProtocolLibraryComponentSerializer();

    public static ProtocolLibraryComponentSerializer get() {
        return INSTANCE;
    }

    public ProtocolLibraryComponentSerializer(GsonComponentSerializer minecraftComponentSerializer) {
        this.serializer = minecraftComponentSerializer;
    }
    public ProtocolLibraryComponentSerializer() {
        this(GsonComponentSerializer.gson());
    }

    @Override
    public @NonNull Component deserialize(@NonNull WrappedChatComponent input) {
        return serializer.deserialize(input.getJson());
    }

    @Override
    public @NonNull WrappedChatComponent serialize(@NonNull Component component) {
        return WrappedChatComponent.fromJson(serializer.serialize(component));
    }
}
