package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public final class Message implements ComponentLike {
    private final String value;
    private final MiniMessage miniMessage;
    private final List<TagResolver> placeholders;


    public Message(String value, MiniMessage miniMessage, List<TagResolver> placeholders) {
        this.value = value;
        this.miniMessage = miniMessage;
        this.placeholders = placeholders;
    }

    public Message(String value, MiniMessage miniMessage) {
        this(value, miniMessage, Collections.emptyList());
    }

    public Message with(TagResolver resolver) {
        ArrayList<TagResolver> temp = new ArrayList<>(placeholders);
        temp.add(resolver);
        return new Message(this.value, this.miniMessage, temp);
    }

    public Message with(Collection<TagResolver> templates) {
        if (templates.isEmpty()) return this;
        ArrayList<TagResolver> temp = new ArrayList<>(this.placeholders);
        temp.addAll(templates);
        return new Message(this.value, this.miniMessage, temp);
    }

    public Message with(TagResolver... resolvers) {
        if (resolvers.length == 0) return this;
        return with(Arrays.asList(resolvers));
    }

    public Message with(String key, String value) {
        return with(Placeholder.component(key, Component.text(value)));
    }

    public Message with(String key, Component value) {
        return with(Placeholder.component(key, value));
    }

    public Message with(String key, ComponentLike value) {
        return with(key, value.asComponent());
    }

    @Override
    public @NonNull Component asComponent() {
        return miniMessage.deserialize(value, TagResolver.resolver(placeholders));
    }

    public String getValue() {
        return this.value;
    }


}
