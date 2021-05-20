package me.gepron1x.minimessageanywhere.processors;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.Optional;


public class EntityMetadataListener extends AbstractListener {
    public EntityMetadataListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final WrappedDataWatcher wdw = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
        Object nick = wdw.getObject(2);
        if(nick == null) return;
        ((Optional<?>) nick).ifPresent(obj -> {
            WrappedChatComponent name = WrappedChatComponent.fromHandle(obj);
            wdw.setObject(2, Optional.of(componentProcessor.handle(name).getHandle()));
        });
        packet.getWatchableCollectionModifier().write(0, wdw.getWatchableObjects());
        event.setPacket(packet);
    }
}
