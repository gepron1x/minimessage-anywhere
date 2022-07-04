package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.packetlistener.out.item.ConvertedItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class ItemFrameListener extends AbstractListener {


    public ItemFrameListener(Plugin plugin, ComponentHandler handler) {
        super(plugin, handler, PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final WrappedDataWatcher wdw = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
        if (!(wdw.getObject(8) instanceof ItemStack itemStack)) {
            return;
        }
        new ConvertedItemStack(this.handler, event.getPlayer(), itemStack).convert();
        wdw.setObject(8, itemStack);

    }
}
