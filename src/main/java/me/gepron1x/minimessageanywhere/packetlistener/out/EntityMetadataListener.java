package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.gepron1x.minimessageanywhere.ComponentHandler;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.Optional;

import static com.comphenix.protocol.wrappers.WrappedDataWatcher.*;


public class EntityMetadataListener extends AbstractListener {


    private final WrappedDataWatcherObject customName;

    public EntityMetadataListener(MiniMessageAnywhere plugin, ComponentHandler handler, int customNameIndex) {
        super(plugin, handler, PacketType.Play.Server.ENTITY_METADATA);
        this.customName = new WrappedDataWatcherObject(customNameIndex, Registry.getChatComponentSerializer(true));

    }
    public EntityMetadataListener(MiniMessageAnywhere plugin, ComponentHandler handler) {
        this(plugin, handler, 2);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        final WrappedDataWatcher wdw = new WrappedDataWatcher(packet.getWatchableCollectionModifier().read(0));
        Object name = wdw.getObject(customName);
        if(name == null) return;

        ((Optional<?>) name)
                .map(WrappedChatComponent::fromHandle)
                .map(c -> handler.handle(event.getPlayer(), c))
                .map(WrappedChatComponent::getHandle)
                .ifPresent(obj -> wdw.setObject(customName, Optional.of(obj)));

        packet.getWatchableCollectionModifier().write(0, wdw.getWatchableObjects());
        event.setPacket(packet);
    }
}
