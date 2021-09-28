package me.gepron1x.minimessageanywhere.processor;


import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.List;
import java.util.regex.Pattern;

public interface MiniMessageProcessor {
    DefaultMiniMessageProcessor DEFAULT = new DefaultMiniMessageProcessor();


    Component process(MiniMessage miniMessage, Component component);

    static DefaultMiniMessageProcessor all() {
        return DEFAULT;
    }
    static RegexMiniMessageProcessor regex(Pattern pattern) {
        return new RegexMiniMessageProcessor(pattern);
    }

}
