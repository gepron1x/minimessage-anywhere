package me.gepron1x.minimessageanywhere.processor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.List;

public class DefaultMiniMessageProcessor implements MiniMessageProcessor {

    @Override
    public Component process(MiniMessage miniMessage, Component component, List<Template> templates) {
        String serialized = miniMessage.serialize(component);
        return miniMessage.parse(serialized, templates);
    }
}
