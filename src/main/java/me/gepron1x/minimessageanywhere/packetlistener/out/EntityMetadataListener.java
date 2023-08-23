package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
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
        List<WrappedDataValue> wwoList = packet.getDataValueCollectionModifier().read(0);
        wwoList.forEach(value -> {
            if (!value.getSerializer().isOptional()) return;
            ((Optional<?>) value.getValue()).filter(WrappedChatComponent.class::isInstance)
                    .ifPresent(o -> {
                        value.setValue(handler.handle(event.getPlayer(), (WrappedChatComponent) o));
                    });
        });
        packet.getDataValueCollectionModifier().write(0, wwoList);
        event.setPacket(packet);
    }

}
