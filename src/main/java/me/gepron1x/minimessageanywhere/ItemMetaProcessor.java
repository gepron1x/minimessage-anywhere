package me.gepron1x.minimessageanywhere;

import me.gepron1x.minimessageanywhere.pdc.DataType;
import me.gepron1x.minimessageanywhere.util.PacketBookData;
import me.gepron1x.minimessageanywhere.util.PacketItemData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMetaProcessor implements Processor<ItemMeta> {
    private final ComponentProcessor componentProcessor;
    private static final NamespacedKey BOOK_DATA = NamespacedKey.minecraft("book_data");
    private static final NamespacedKey ITEM_DATA = NamespacedKey.minecraft("item_data");
    private final Component fuckItalic = Component.empty().decoration(TextDecoration.ITALIC, false);


    public ItemMetaProcessor(ComponentProcessor componentProcessor) {
        this.componentProcessor = componentProcessor;
    }


    @Override
    public ItemMeta handle(ItemMeta meta) {
        PersistentDataContainer container = meta.getPersistentDataContainer();
        Component displayName = meta.displayName();
        List<Component> lore = meta.lore();
        container.set(ITEM_DATA, DataType.ITEM_DATA, new PacketItemData(displayName, lore));
        if(displayName != null)
            meta.displayName(fuckItalic.append(componentProcessor.handle(displayName)));
        if(lore != null)
            meta.lore(lore.stream().map(componentProcessor::handle).map(fuckItalic::append).collect(Collectors.toList()));
        if(meta instanceof BookMeta) {
            BookMeta bookMeta = (BookMeta) meta;
            Component author = bookMeta.author() == null ? null : componentProcessor.handle(bookMeta.author());
            Component title = bookMeta.title() == null ? null : componentProcessor.handle(bookMeta.title());
            List<Component> pages = bookMeta.pages().stream().map(componentProcessor::handle).collect(Collectors.toList());
            container.set(BOOK_DATA, DataType.BOOK_DATA, new PacketBookData(bookMeta.author(), bookMeta.title(), bookMeta.pages()));
            meta = bookMeta.toBuilder().pages(pages).author(author).title(title).build();
        }
        return meta;
    }

    @Override
    public ItemMeta reset(ItemMeta input) {
        PersistentDataContainer container = input.getPersistentDataContainer();
        PacketItemData itemData = container.get(ITEM_DATA, DataType.ITEM_DATA);
        if(itemData != null) {
            if(itemData.hasDisplayName()) input.displayName(itemData.getDisplayName());
            if(itemData.hasLore()) input.lore(itemData.getLore());
            container.remove(ITEM_DATA);
        }
        PacketBookData bookData = container.get(BOOK_DATA, DataType.BOOK_DATA);
        if(bookData != null && input instanceof BookMeta) {
            input = ((BookMeta) input).toBuilder()
                    .title(bookData.getTitle())
                    .author(bookData.getAuthor())
                    .pages(bookData.getPages()).build();
            container.remove(BOOK_DATA);
        }
        return input;
    }
}
