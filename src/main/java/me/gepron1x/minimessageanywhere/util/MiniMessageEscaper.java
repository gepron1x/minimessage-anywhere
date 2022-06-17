package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.minimessage.MiniMessage;


public final class MiniMessageEscaper {


    private final MiniMessage miniMessage;

    public MiniMessageEscaper(MiniMessage miniMessage) {

        this.miniMessage = miniMessage;
    }


    public String escape(String text) {
        return miniMessage.escapeTags(text);
    }


}
