package me.gepron1x.minimessageanywhere.pdc;

import me.gepron1x.minimessageanywhere.util.PacketItemData;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

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
        if(complex.hasDisplayName()) container.set(DISPLAY_NAME_KEY, DataType.COMPONENT, complex.getDisplayName());
        if(complex.hasLore()) container.set(LORE_KEY, DataType.COMPONENT_LIST, complex.getLore());

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
