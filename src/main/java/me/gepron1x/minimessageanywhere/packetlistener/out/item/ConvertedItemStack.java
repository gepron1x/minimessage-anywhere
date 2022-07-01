package me.gepron1x.minimessageanywhere.packetlistener.out.item;

import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.pdc.DataType;
import me.gepron1x.minimessageanywhere.util.PacketBookData;
import me.gepron1x.minimessageanywhere.util.PacketItemData;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.List;

public final class ConvertedItemStack {

    private static final NamespacedKey ITEM_DATA = MiniMessageAnywhere.key("item_data"), BOOK_DATA = MiniMessageAnywhere.key("book_data");

    private final ComponentHandler handler;
    private final Audience audience;
    private final ItemStack itemStack;

    public ConvertedItemStack(ComponentHandler handler, Audience audience, ItemStack itemStack) {

        this.handler = handler;
        this.audience = audience;
        this.itemStack = itemStack;
    }

    public void convert() {
        if (!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();


        Component displayName = meta.displayName();
        Component displayNameHandled = this.handler.handleIfNotNull(audience, displayName);
        List<Component> lore = meta.lore();
        PacketItemData data = new PacketItemData(displayName, lore);
        pdc.set(ITEM_DATA, DataType.ITEM_DATA, data);

        meta.displayName(displayNameHandled);
        meta.lore(lore == null ? null : processList(audience, lore));


        if (itemStack.getType() == Material.WRITTEN_BOOK) {
            BookMeta bookMeta = (BookMeta) meta;
            List<Component> pages = bookMeta.pages();
            Component title = bookMeta.title();
            Component author = bookMeta.author();
            pdc.set(BOOK_DATA, DataType.BOOK_DATA, new PacketBookData(author, title, pages));

            //noinspection ResultOfMethodCallIgnored
            bookMeta.title(handler.handleIfNotNull(audience, title));
            //noinspection ResultOfMethodCallIgnored
            bookMeta.author(handler.handleIfNotNull(audience, author));

            for (int i = 1; i <= bookMeta.getPageCount(); i++) {
                bookMeta.page(i, handler.handle(audience, bookMeta.page(i)));
            }
        }


        itemStack.setItemMeta(meta);

    }

    public void revert() {
        if (!itemStack.hasItemMeta()) return;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();


        PacketItemData itemData = pdc.get(ITEM_DATA, DataType.ITEM_DATA);

        if (itemData != null) {
            meta.displayName(itemData.getDisplayName());
            meta.lore(itemData.getLore());
            pdc.remove(ITEM_DATA);
        }


        PacketBookData bookData = pdc.get(BOOK_DATA, DataType.BOOK_DATA);
        if (itemStack.getType() == Material.WRITTEN_BOOK && bookData != null) {
            BookMeta book = (BookMeta) meta;
            //noinspection ResultOfMethodCallIgnored
            book.pages(bookData.getPages());
            //noinspection ResultOfMethodCallIgnored
            book.author(bookData.getAuthor());
            //noinspection ResultOfMethodCallIgnored
            book.title(bookData.getTitle());

            pdc.remove(BOOK_DATA);
        }

        itemStack.setItemMeta(meta);

    }

    private List<Component> processList(Audience audience, List<Component> components) {
        List<Component> newList = new ArrayList<>(components.size());
        for (Component component : components) {
            newList.add(handler.handle(audience, component));
        }
        return newList;
    }
}
