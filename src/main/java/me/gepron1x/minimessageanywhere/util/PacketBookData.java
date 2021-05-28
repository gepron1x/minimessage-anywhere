package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class PacketBookData {
    private final Component author;
    private final Component title;
    private final List<Component> pages;
    public PacketBookData(@Nullable Component author, @Nullable Component title, @NotNull List<Component> pages) {
        this.author = author;
        this.title = title;
        this.pages = pages;
    }
    @Nullable
    public Component getAuthor() {
        return author;
    }
    @Nullable
    public Component getTitle() {
        return title;
    }
    @NotNull
    public List<Component> getPages() {
        return pages;
    }
    public boolean hasAuthor() {
        return author != null;
    }
    public boolean hasTitle() {
        return title != null;
    }


}
