package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
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
        var metadata = packet.getDataValueCollectionModifier().read(0);
        metadata.forEach(value -> {
            if (value.getValue() instanceof ItemStack item) {
                new ConvertedItemStack(this.handler, event.getPlayer(), item).convert();
                value.setValue(item);
            }
        });
        packet.getDataValueCollectionModifier().write(0, metadata);

    }
}
