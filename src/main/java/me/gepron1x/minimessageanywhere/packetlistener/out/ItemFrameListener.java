package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BukkitConverters;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.packetlistener.out.item.ConvertedItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Optional;

public final class ItemFrameListener extends AbstractListener {

    private static final WrappedDataWatcher.WrappedDataWatcherObject ITEM_FRAME_ICON = new WrappedDataWatcher.WrappedDataWatcherObject(8, WrappedDataWatcher.Registry.getItemStackSerializer(true));

    public ItemFrameListener(Plugin plugin, ComponentHandler handler) {
        super(plugin, handler, PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final WrappedDataWatcher wdw = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
        Object item = wdw.getObject(ITEM_FRAME_ICON);
        if (item == null) return;
        wdw.setObject(ITEM_FRAME_ICON, ((Optional<?>) item)
                .map(BukkitConverters.getItemStackConverter()::getSpecific)
                .map(i -> {
                    new ConvertedItemStack(this.handler, event.getPlayer(), i).convert();
                    return i;
                }).map(BukkitConverters.getItemStackConverter()::getGeneric)
        );

    }
}
