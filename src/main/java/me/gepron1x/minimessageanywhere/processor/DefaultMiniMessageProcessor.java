package me.gepron1x.minimessageanywhere.processor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class DefaultMiniMessageProcessor implements MiniMessageProcessor {

    @Override
    public Component process(MiniMessage miniMessage, Component component) {
        String serialized = miniMessage.serialize(component);
        serialized = miniMessage.serialize(LegacyComponentSerializer.legacySection().deserialize(serialized));
        return miniMessage.deserialize(serialized);
    }
}
