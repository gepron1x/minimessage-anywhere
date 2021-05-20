package me.gepron1x.minimessageanywhere.listener;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketEvent;
import me.gepron1x.minimessageanywhere.MiniMessageAnywhere;
import me.gepron1x.minimessageanywhere.wrapper.WrapperPlayServerMap;

import java.util.Arrays;


public class MapListener extends AbstractListener {

    public MapListener(MiniMessageAnywhere plugin) {
        super(plugin, PacketType.Play.Server.MAP);
    }


    @Override
    public void onPacketSending(final PacketEvent event) {
        WrapperPlayServerMap mapPacket = new WrapperPlayServerMap(event.getPacket());
        mapPacket.setMapIcons(Arrays.stream(mapPacket.getMapIcons())
                .peek(icon -> icon.name = componentProcessor.handle(icon.name))
                .toArray(WrapperPlayServerMap.MapIcon[]::new));
        event.setPacket(mapPacket.getHandle());
    }
}
