package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;


public final class MiniMessageEscaper {


    private final MiniMessage miniMessage;

    public MiniMessageEscaper(MiniMessage miniMessage) {

        this.miniMessage = miniMessage;
    }


    public String escape(String text) {
        text = miniMessage.serialize(LegacyComponentSerializer.legacySection().deserialize(text));
        return miniMessage.escapeTags(text);
    }


}
