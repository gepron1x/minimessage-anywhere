package me.gepron1x.minimessageanywhere.pdc;

import me.gepron1x.minimessageanywhere.util.PacketItemData;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PacketItemDataType implements PersistentDataType<PersistentDataContainer, PacketItemData> {
    private static final NamespacedKey DISPLAY_NAME_KEY = NamespacedKey.minecraft("display_name");
    private static final NamespacedKey LORE_KEY = NamespacedKey.minecraft("lore");

    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<PacketItemData> getComplexType() {
        return PacketItemData.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull PacketItemData complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        Component displayName = complex.getDisplayName();
        List<Component> lore = complex.getLore();
        if(displayName != null) container.set(DISPLAY_NAME_KEY, DataType.COMPONENT, displayName);
        if(lore != null) container.set(LORE_KEY, DataType.COMPONENT_LIST, lore);
        return container;
    }

    @Override
    public @NotNull PacketItemData fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        return new PacketItemData(
                primitive.get(DISPLAY_NAME_KEY, DataType.COMPONENT),
                primitive.get(LORE_KEY, DataType.COMPONENT_LIST)
        );
    }

}
