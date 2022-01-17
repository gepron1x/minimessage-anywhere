package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.placeholder.Placeholder;
import net.kyori.adventure.text.minimessage.placeholder.PlaceholderResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public final class Message implements ComponentLike {
    private final String value;
    private final MiniMessage miniMessage;
    private final List<Placeholder<?>> placeholders;


    public Message(String value, MiniMessage miniMessage, List<Placeholder<?>> placeholders) {
        this.value = value;
        this.miniMessage = miniMessage;
        this.placeholders = placeholders;
    }

    public Message(String value, MiniMessage miniMessage) {
        this(value, miniMessage, Collections.emptyList());
    }

    public Message with(Placeholder<?> placeholder) {
        ArrayList<Placeholder<?>> temp = new ArrayList<>(placeholders);
        temp.add(placeholder);
        return new Message(this.value, this.miniMessage, temp);
    }

    public Message with(Collection<Placeholder<?>> templates) {
        if (templates.isEmpty()) return this;
        ArrayList<Placeholder<?>> temp = new ArrayList<>(this.placeholders);
        temp.addAll(templates);
        return new Message(this.value, this.miniMessage, temp);
    }

    public Message with(Placeholder<?>... templates) {
        if (templates.length == 0) return this;
        return with(Arrays.asList(templates));
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
        return miniMessage.deserialize(value, PlaceholderResolver.placeholders(placeholders));
    }

    public String getValue() {
        return this.value;
    }

    public MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

}
