package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public final class Message implements ComponentLike {
    private final String value;
    private final MiniMessage miniMessage;
    private final List<Template> templates;


    public Message(String value, MiniMessage miniMessage, List<Template> templates) {
        this.value = value;
        this.miniMessage = miniMessage;
        this.templates = templates;
    }

    public Message(String value, MiniMessage miniMessage) {
        this(value, miniMessage, Collections.emptyList());
    }

    public Message with(Template template) {
        ArrayList<Template> temp = new ArrayList<>(templates);
        temp.add(template);
        return new Message(this.value, this.miniMessage, temp);
    }

    public Message with(Collection<Template> templates) {
        ArrayList<Template> temp = new ArrayList<>(this.templates);
        temp.addAll(templates);
        return new Message(this.value, this.miniMessage, temp);
    }

    public Message with(Template... templates) {
        return with(Arrays.asList(templates));
    }

    public Message with(String key, String value) {
        return with(Template.of(key, value));
    }

    public Message with(String key, Component value) {
        return with(Template.of(key, value));
    }

    public Message with(String key, ComponentLike value) {
        return with(key, value.asComponent());
    }

    @Override
    public @NonNull Component asComponent() {
        return miniMessage.parse(value, templates);
    }

    public String getValue() {
        return this.value;
    }

    public MiniMessage getMiniMessage() {
        return this.miniMessage;
    }

    public List<Template> getTemplates() {
        return Collections.unmodifiableList(this.templates);
    }
}
