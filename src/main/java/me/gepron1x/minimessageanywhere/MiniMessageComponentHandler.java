package me.gepron1x.minimessageanywhere;

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
    private final PlaceholderAPISupport papi;
    private final Map<String, String> placeholders;

    public MiniMessageComponentHandler(MiniMessage miniMessage, MiniMessageProcessor processor, PlaceholderAPISupport papi, Map<String, String> placeholders) {
        this.miniMessage = miniMessage;
        this.processor = processor;
        this.papi = papi;
        this.placeholders = placeholders;
    }

    @Override
    public Component handle(Audience audience, Component component) {
        Player player = audience instanceof Player ? (Player) audience : null;
        List<Template> templates = new ArrayList<>(placeholders.size());

        for(Map.Entry<String, String> entry : placeholders.entrySet()) {
            Component value = miniMessage.parse(papi.applyPlaceholders(player, entry.getValue()), templates);
            templates.add(Template.of(entry.getKey(), value));
        }
        return processor.process(miniMessage, component, templates);
    }
}
