package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Optional;

import static com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import static com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;


public class EntityMetadataListener extends AbstractListener {


    private final WrappedDataWatcherObject customName;


    public EntityMetadataListener(Plugin plugin, ComponentHandler handler, int customNameIndex) {
        super(plugin, handler, PacketType.Play.Server.ENTITY_METADATA);
        this.customName = new WrappedDataWatcherObject(customNameIndex, Registry.getChatComponentSerializer(true));

    }

    public EntityMetadataListener(MiniMessageAnywhere plugin, ComponentHandler handler) {
        this(plugin, handler, 2);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        final PacketContainer packet = event.getPacket();
        List<WrappedWatchableObject> wwoList = packet.getWatchableCollectionModifier().read(0);
        if (wwoList == null) return;
        final WrappedDataWatcher wdw = new WrappedDataWatcher(wwoList);
        Object name = wdw.getObject(customName);
        if (name == null) return;

        wdw.setObject(customName,
                ((Optional<?>) name)
                        .map(WrappedChatComponent::fromHandle)
                        .map(c -> handler.handle(event.getPlayer(), c))
                        .map(WrappedChatComponent::getHandle)
        );


        packet.getWatchableCollectionModifier().write(0, wdw.getWatchableObjects());
        event.setPacket(packet);
    }

}
