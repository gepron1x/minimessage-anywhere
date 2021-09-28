package me.gepron1x.minimessageanywhere.processor;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.regex.Pattern;

public class RegexMiniMessageProcessor implements MiniMessageProcessor {
    private final Pattern pattern;

    public RegexMiniMessageProcessor(Pattern pattern) {
        this.pattern = pattern;


    }
    @Override
    public Component process(final MiniMessage miniMessage, final Component component) {
        TextReplacementConfig config = TextReplacementConfig.builder().match(pattern)
                .replacement((result, builder) -> miniMessage.parse(result.group(1))).build();
        return component.replaceText(config);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
