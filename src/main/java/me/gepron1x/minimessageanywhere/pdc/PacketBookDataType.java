package me.gepron1x.minimessageanywhere.pdc;

import me.gepron1x.minimessageanywhere.util.PacketBookData;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PacketBookDataType implements PersistentDataType<PersistentDataContainer, PacketBookData> {

    private static final NamespacedKey AUTHOR_KEY = NamespacedKey.minecraft("author");
    private static final NamespacedKey TITLE_KEY = NamespacedKey.minecraft("title");
    private static final NamespacedKey PAGES_KEY = NamespacedKey.minecraft("pages");
    @Override
    public @NotNull Class<PersistentDataContainer> getPrimitiveType() {
        return PersistentDataContainer.class;
    }

    @Override
    public @NotNull Class<PacketBookData> getComplexType() {
        return PacketBookData.class;
    }

    @Override
    public @NotNull PersistentDataContainer toPrimitive(@NotNull PacketBookData complex, @NotNull PersistentDataAdapterContext context) {
        PersistentDataContainer container = context.newPersistentDataContainer();
        Component author = complex.getAuthor();
        Component title = complex.getTitle();
        if(author != null) container.set(AUTHOR_KEY, DataType.COMPONENT, author);
        if(title != null) container.set(TITLE_KEY, DataType.COMPONENT, title);
        container.set(PAGES_KEY, DataType.COMPONENT_LIST, complex.getPages());
        return container;
    }

    @Override
    public @NotNull PacketBookData fromPrimitive(@NotNull PersistentDataContainer primitive, @NotNull PersistentDataAdapterContext context) {
        return new PacketBookData(
                primitive.get(AUTHOR_KEY, DataType.COMPONENT),
                primitive.get(TITLE_KEY, DataType.COMPONENT),
                Objects.requireNonNull(primitive.get(PAGES_KEY, DataType.COMPONENT_LIST), "pages cannot be null")
        );
    }
}
