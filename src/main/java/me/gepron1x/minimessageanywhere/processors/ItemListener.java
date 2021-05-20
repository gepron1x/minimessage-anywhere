package me.gepron1x.minimessageanywhere.processors;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import me.gepron1x.minimessageanywhere.pdc.DataType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.stream.Collectors;


public class ItemListener extends AbstractListener {
    private static final NamespacedKey DISPLAY_NAME_KEY = NamespacedKey.minecraft("display_name");
    private static final NamespacedKey LORE_KEY = NamespacedKey.minecraft("lore");
    private static final NamespacedKey TRANSLATION_DATA = NamespacedKey.minecraft("translation_data");
    public ItemListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Client.SET_CREATIVE_SLOT);
    }


    private void processItem(ItemStack itemStack) {
        if(itemStack == null || itemStack.getType() == Material.AIR) return;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataContainer data = container.getAdapterContext().newPersistentDataContainer();
        if(meta.hasDisplayName()) {
            Component displayName = meta.displayName();
            meta.displayName(componentProcessor.handle(displayName));
            data.set(DISPLAY_NAME_KEY, DataType.COMPONENT, displayName);
        }
        if(meta.hasLore()) {
            List<Component> lore = meta.lore();
            meta.lore(lore.stream().map(componentProcessor::handle).collect(Collectors.toList()));
            data.set(LORE_KEY, DataType.COMPONENT_LIST, lore);
        }
       if(!data.isEmpty()) container.set(TRANSLATION_DATA, PersistentDataType.TAG_CONTAINER, data);
       itemStack.setItemMeta(meta);

    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ItemStack itemStack = packet.getItemModifier().read(0);
        if(itemStack == null || itemStack.getType() == Material.AIR) return;
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        PersistentDataContainer data = container.get(TRANSLATION_DATA, PersistentDataType.TAG_CONTAINER);
        if(data == null) return;
        if(data.has(DISPLAY_NAME_KEY, DataType.COMPONENT)) {
            meta.displayName(data.get(DISPLAY_NAME_KEY, DataType.COMPONENT));
        }
        if(data.has(LORE_KEY, DataType.COMPONENT_LIST)) {
            meta.lore(data.get(LORE_KEY, DataType.COMPONENT_LIST));
        }
        container.remove(TRANSLATION_DATA);
        itemStack.setItemMeta(meta);
        packet.getItemModifier().write(0, itemStack);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType type = event.getPacketType();
        PacketContainer packet = event.getPacket();
        if(type == PacketType.Play.Server.SET_SLOT) {
            ItemStack item = packet.getItemModifier().read(0);
            processItem(item);
            packet.getItemModifier().write(0, item);
        } else if(type == PacketType.Play.Server.WINDOW_ITEMS) {
            List<ItemStack> items = packet.getItemListModifier().read(0);
            for(ItemStack item : items) {
                processItem(item);
            }
            packet.getItemListModifier().write(0, items);
        }
        event.setPacket(packet);
    }
}
