package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.ComponentProcessor;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.Collection;

public abstract class AbstractListener extends PacketAdapter {
    protected final MiniMessageAnywhere plugin;
    protected final ComponentProcessor componentProcessor;

    public AbstractListener(MiniMessageAnywhere plugin, PacketType... types) {
        super(plugin, ListenerPriority.HIGHEST, types);
        this.plugin = plugin;
        this.componentProcessor = plugin.getComponentProcessor();
    }
    public AbstractListener(MiniMessageAnywhere plugin, Collection<PacketType> types) {
        this(plugin, types.toArray(types.toArray(new PacketType[0])));
    }

    @Override
    public abstract void onPacketSending(PacketEvent event);
    @Override
    public void onPacketReceiving(PacketEvent event) {}
}

