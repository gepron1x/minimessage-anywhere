package me.gepron1x.minimessageanywhere.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.ItemMetaProcessor;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ItemListener extends AbstractListener {
    private static final NamespacedKey DISPLAY_NAME_KEY = NamespacedKey.minecraft("display_name");
    private static final NamespacedKey LORE_KEY = NamespacedKey.minecraft("lore");
    private static final NamespacedKey TRANSLATION_DATA = NamespacedKey.minecraft("translation_data");
    private final ItemMetaProcessor metaProcessor;
    public ItemListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Client.SET_CREATIVE_SLOT);
        this.metaProcessor = new ItemMetaProcessor(plugin.getComponentProcessor());
    }


    private void processItem(@Nullable ItemStack itemStack) {
        if(itemStack == null || !itemStack.hasItemMeta()) return;
        itemStack.setItemMeta(metaProcessor.handle(itemStack.getItemMeta()));


    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        ItemStack itemStack = packet.getItemModifier().read(0);
        if(itemStack == null || !itemStack.hasItemMeta()) return;
        itemStack.setItemMeta(metaProcessor.handle(itemStack.getItemMeta()));
        packet.getItemModifier().write(0, itemStack);
        event.setPacket(packet);
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
