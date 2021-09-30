package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.pdc.DataType;
import me.gepron1x.minimessageanywhere.util.PacketBookData;
import me.gepron1x.minimessageanywhere.util.PacketItemData;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public class ItemListener extends AbstractListener {

    private static final ComponentHandler DISABLE_ITALIC = (audience, component) -> component.decoration(TextDecoration.ITALIC, false);

    private final ComponentHandler itemHandler;


    private final NamespacedKey itemDataKey, bookDataKey;


    public ItemListener(MiniMessageAnywhere plugin, ComponentHandler handler, boolean disableItalic) {
        super(plugin,
                handler,
                PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS,
                PacketType.Play.Client.SET_CREATIVE_SLOT
        );
        this.itemHandler = disableItalic ? handler.andThen(DISABLE_ITALIC) : handler;
        this.itemDataKey = new NamespacedKey(plugin, "item_data");
        this.bookDataKey = new NamespacedKey(plugin, "book_data");
    }

    
    private void processItem(Audience audience, @Nullable ItemStack itemStack) {
        if (itemStack == null || !itemStack.hasItemMeta()) return;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();


        Component displayName = meta.displayName();
        Component displayNameHandled = itemHandler.handleIfNotNull(audience, displayName);
        List<Component> lore = meta.lore();
        PacketItemData data = new PacketItemData(displayName, lore);
        pdc.set(itemDataKey, DataType.ITEM_DATA, data);

        meta.displayName(displayNameHandled);
        meta.lore(lore == null ? null : processList(audience, lore));


        if(itemStack.getType() == Material.WRITTEN_BOOK) {
            BookMeta bookMeta = (BookMeta) meta;
            List<Component> pages = bookMeta.pages();
            Component title = displayName == null ? bookMeta.title() : displayName;
            Component author = bookMeta.author();
            pdc.set(bookDataKey, DataType.BOOK_DATA, new PacketBookData(author, title, pages));
            bookMeta.title(handler.handleIfNotNull(audience, title));
            bookMeta.author(handler.handleIfNotNull(audience, author));

            for (int i = 1; i <= bookMeta.getPageCount(); i++) {
                bookMeta.page(i, handler.handle(audience, bookMeta.page(i)));
            }
        }


        itemStack.setItemMeta(meta);
    }

    private List<Component> processList(Audience audience, List<Component> components) {
        List<Component> newList = new ArrayList<>(components);
        for(Component component : components) {
            newList.add(handler.handle(audience, component));
        }
        return newList;
    }


    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ItemStack itemStack = packet.getItemModifier().read(0);
        if(itemStack == null || !itemStack.hasItemMeta()) return;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();



        PacketItemData itemData = pdc.get(itemDataKey, DataType.ITEM_DATA);

        if(itemData != null) {
            meta.displayName(itemData.getDisplayName());
            meta.lore(itemData.getLore());
            pdc.remove(itemDataKey);
        }


        PacketBookData bookData = pdc.get(bookDataKey, DataType.BOOK_DATA);
        if(itemStack.getType() == Material.WRITTEN_BOOK && bookData != null) {
            BookMeta book = (BookMeta) meta;
            book.pages(bookData.getPages());
            book.author(bookData.getAuthor());
            book.title(bookData.getTitle());

            pdc.remove(bookDataKey);
        }

        itemStack.setItemMeta(meta);
        packet.getItemModifier().write(0, itemStack);
        event.setPacket(packet);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType type = event.getPacketType();
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();
        if(type == PacketType.Play.Server.SET_SLOT) {
            ItemStack item = packet.getItemModifier().read(0);
            processItem(player, item);
            packet.getItemModifier().write(0, item);
        } else if(type == PacketType.Play.Server.WINDOW_ITEMS) {
            List<ItemStack> items = packet.getItemListModifier().read(0);
            for(ItemStack item : items) {
                processItem(player, item);
            }
            packet.getItemListModifier().write(0, items);
        }
        event.setPacket(packet);
    }
}
