package me.gepron1x.minimessageanywhere.processor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class DefaultMiniMessageProcessor implements MiniMessageProcessor {

    @Override
    public Component process(MiniMessage miniMessage, Component component) {
        String serialized = miniMessage.serialize(component);
        // serialized = miniMessage.serialize(LegacyComponentSerializer.legacySection().deserialize(serialized));
        return miniMessage.deserialize(serialized);
    }
}
