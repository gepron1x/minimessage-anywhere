package me.gepron1x.minimessageanywhere.util;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class PacketItemData {
    private final List<Component> lore;
    private final Component displayName;

    public PacketItemData(@Nullable Component displayName, @Nullable List<Component> lore) {
        this.displayName = displayName;
        this.lore = lore;
    }
    public boolean hasLore() {
        return lore != null;
    }
    public boolean hasDisplayName() {
        return displayName != null;
    }
    @Nullable
    public Component getDisplayName() {
        return displayName;
    }
    @Nullable
    public List<Component> getLore() {
        return lore;
    }

}
