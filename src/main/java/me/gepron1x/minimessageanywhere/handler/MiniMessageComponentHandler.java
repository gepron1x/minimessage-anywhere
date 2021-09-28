package me.gepron1x.minimessageanywhere.handler;

import me.gepron1x.minimessageanywhere.processor.MiniMessageProcessor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;


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
