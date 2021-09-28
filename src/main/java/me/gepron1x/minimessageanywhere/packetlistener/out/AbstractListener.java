package me.gepron1x.minimessageanywhere.packetlistener.out;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.handler.ComponentHandler;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;

import java.util.Arrays;
import java.util.Collection;

public abstract class AbstractListener extends PacketAdapter {
    protected final MiniMessageAnywhere plugin;
    protected final ComponentHandler handler;

    public AbstractListener(MiniMessageAnywhere plugin, ComponentHandler handler, PacketType... types) {
        this(plugin, handler, Arrays.asList(types));

    }
    public AbstractListener(MiniMessageAnywhere plugin, ComponentHandler handler, Collection<PacketType> types) {
        super(plugin, types);
        this.plugin = plugin;
        this.handler = handler;
    }





    @Override
    public abstract void onPacketSending(PacketEvent event);
    @Override
    public void onPacketReceiving(PacketEvent event) {}
}

