package me.gepron1x.minimessageanywhere.handler;

import me.gepron1x.minimessageanywhere.hook.PlaceholderAPISupport;
import me.gepron1x.minimessageanywhere.processor.MiniMessageProcessor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MiniMessageComponentHandler implements ComponentHandler {
    private final MiniMessage miniMessage;
    private final MiniMessageProcessor processor;

    public MiniMessageComponentHandler(MiniMessage miniMessage, MiniMessageProcessor processor) {
        this.miniMessage = miniMessage;
        this.processor = processor;
    }

    @Override
    public Component handle(Audience audience, Component component) {
        return processor.process(miniMessage, component);
    }
}
